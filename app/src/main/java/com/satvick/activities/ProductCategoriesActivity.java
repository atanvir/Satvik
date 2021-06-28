package com.satvick.activities;

import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputType;
import android.view.View;
import com.satvick.R;
import com.satvick.adapters.InnerCategoriesAdapter;
import com.satvick.adapters.InnerProductAdapter;
import com.satvick.databinding.ActivityProductCategoriesBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoriesActivity extends AppCompatActivity implements View.OnClickListener, Callback<InnerPagesModel> {

    private ActivityProductCategoriesBinding binding;
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProductCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        innnerPagesApi();
    }


    private void init() {
        binding.tvTitle.setText(getIntent().getStringExtra("filterData")!=null?getIntent().getStringExtra("filterData").replace("_"," "):"");
        binding.tvTitle.setRawInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    private void initCtrl() {
        binding.ivBack.setOnClickListener(this);
        binding.ivSearch.setOnClickListener(this);
        binding.ivSave.setOnClickListener(this);
        binding.ivLeftSlide.setOnClickListener(this);
        binding.ivRightSlider.setOnClickListener(this);
    }


    private void innnerPagesApi() {
        Call<InnerPagesModel> call = apiInterface.getInnerPageResult(getIntent().getStringExtra("filterData"),
                                                                     HelperClass.getCacheData(this).second);
        call.enqueue(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: onBackPressed(); break;
            case R.id.ivSearch: CommonUtil.startNewActivity(this,SearchScreenActivity.class); break;
            case R.id.ivSave: CommonUtil.startNewActivity(this, WishListActivity.class); break;
            case R.id.ivLeftSlide: binding.rvCategories.smoothScrollToPosition(((LinearLayoutManager)binding.rvCategories.getLayoutManager()).findFirstVisibleItemPosition());  break;
            case R.id.ivRightSlider: binding.rvCategories.smoothScrollToPosition(((LinearLayoutManager)binding.rvCategories.getLayoutManager()).findLastVisibleItemPosition() + 1); break;
        }
    }

    @Override
    public void onResponse(Call<InnerPagesModel> call, Response<InnerPagesModel> response) {
        binding.progressBar.setVisibility(View.GONE);
        if(response.isSuccessful()){
            if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body());
            if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),ProductCategoriesActivity.this);
        }else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",this);
    }

    @Override
    public void onFailure(Call<InnerPagesModel> call, Throwable t) {
        binding.progressBar.setVisibility(View.GONE);
        CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),this);
    }


    private void setDataToUI(InnerPagesModel data) {

        // Categories
        if(!data.getResponse().getSubcategory_slider().isEmpty()){
            binding.clCategories.setVisibility(View.VISIBLE);
            binding.rvCategories.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            binding.rvCategories.setAdapter(new InnerCategoriesAdapter(this,data.getResponse().getSubcategory_slider()));
        }

        // Products
        List<InnerPagesModel.Other_section> list=new ArrayList<>();
        if(!data.getResponse().getFlash_sale().isEmpty()) list.add(getFlashSaleData(data));
        if(!data.getResponse().getBannerlist().isEmpty()) list.add(getBanners(data));
        list.addAll(data.getResponse().getOther_section());
        Collections.shuffle(list);

        binding.rvProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProducts.setAdapter(new InnerProductAdapter(this,list));
    }

    private InnerPagesModel.Other_section getFlashSaleData(InnerPagesModel data) {
        InnerPagesModel.Other_section sectionsData=new InnerPagesModel.Other_section();
        sectionsData.setFlash_sale(data.getResponse().getFlash_sale());
        return sectionsData;
    }


    private InnerPagesModel.Other_section getBanners(InnerPagesModel data) {
        InnerPagesModel.Other_section sectionsData=new InnerPagesModel.Other_section();
        sectionsData.setBannerList(data.getResponse().getBannerlist());
        return sectionsData;
    }

}
