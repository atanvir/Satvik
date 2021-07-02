package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.satvick.R;
import com.satvick.adapters.FilterProductListBySizeAdapter;
import com.satvick.adapters.PriceRangeAdapter;
import com.satvick.databinding.ActivityFilterProductListBinding;
import com.satvick.fcm.MyFirebaseMessagingService;
import com.satvick.model.FilterProductListModel;
import com.satvick.model.SizeCheckedListModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterProductListActivity extends AppCompatActivity implements View.OnClickListener, PriceRangeAdapter.PriceRangerClickListner {
    private ActivityFilterProductListBinding binding;
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    private List<SizeCheckedListModel> sizeList = new ArrayList<>();
    private String maxValue="100",minValue="10000",type="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityFilterProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        filterDataApi();
    }

    private void init() {

    }

    private void initCtrl() {
        binding.llSize.setOnClickListener(this);
        binding.llPrice.setOnClickListener(this);
        binding.llClose.setOnClickListener(this);
        binding.llApply.setOnClickListener(this);
        binding.tvClearAll.setOnClickListener(this);
    }

    private void filterDataApi() {
        Call<FilterProductListModel> call = apiInterface.getFilterListResult(getIntent().getStringExtra(GlobalVariables.subcatid),
                                                                             getIntent().getStringExtra(GlobalVariables.type));
        call.enqueue(new Callback<FilterProductListModel>() {
            @Override
            public void onResponse(Call<FilterProductListModel> call, Response<FilterProductListModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(GlobalVariables.SUCCESS)) { setDataToUI(response.body()); }
                    else CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),FilterProductListActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",FilterProductListActivity.this);
            }

            @Override
            public void onFailure(Call<FilterProductListModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),FilterProductListActivity.this);
            }
        });
    }

    private void setDataToUI(FilterProductListModel data) {
        if(data!=null) {

            // Sizes
            sizeList.clear();
            for (int i = 0; i < data.getFilterSection().getSize().size(); i++) {
                if(!data.getFilterSection().getSize().get(i).equalsIgnoreCase("")) {
                    SizeCheckedListModel sizeCheckedListModel = new SizeCheckedListModel(data.getFilterSection().getSize().get(i));
                    sizeList.add(sizeCheckedListModel);
                }
            }

            if(sizeList.isEmpty()) setBackground("price");

            if (getIntent().getStringExtra(GlobalVariables.type).equalsIgnoreCase(GlobalVariables.flashSale) ||
                getIntent().getStringExtra(GlobalVariables.type).equalsIgnoreCase(GlobalVariables.search)) {
                setBackground("price");
            }
            else{
                if(!sizeList.isEmpty()) {
                    setBackground("size");
                    binding.llSize.setVisibility(View.VISIBLE);
                }else{
                    setBackground("price");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSize: setBackground("size"); break;
            case R.id.llPrice: setBackground("price"); break;
            case R.id.llClose: onBackPressed(); break;
            case R.id.llApply: type="Filter"; applyFilter(); break;
            case R.id.tvClearAll: sizeList.clear(); minValue=""; maxValue=""; type=""; applyFilter(); break;
        }
    }

    private void applyFilter() {
        List<String> selectedSizedList=new ArrayList<>();
        for(int i=0;i<sizeList.size();i++){
            if(sizeList.get(i).isSizeChecked()) selectedSizedList.add(sizeList.get(i).getSizeName());
        }

        Intent intent=new Intent();
        intent.putExtra("filterSize", TextUtils.join(",",selectedSizedList));
        intent.putExtra(GlobalVariables.catid,getIntent().getStringExtra(GlobalVariables.catid));
        intent.putExtra(GlobalVariables.subcatid,getIntent().getStringExtra(GlobalVariables.subcatid));
        intent.putExtra(GlobalVariables.subsubcatid,getIntent().getStringExtra(GlobalVariables.subsubcatid));
        intent.putExtra(GlobalVariables.minValue,minValue);
        intent.putExtra(GlobalVariables.maxValue,maxValue);
        intent.putExtra(GlobalVariables.type,type);
        setResult(RESULT_OK,intent);
        finish();
    }



    private void setBackground(String type) {
        if(type.equalsIgnoreCase("price")){
            binding.llSize.setBackgroundColor(getResources().getColor(R.color.colorView));
            binding.tvSize.setTextColor(getResources().getColor(R.color.colorBlack));
            binding.llPrice.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            binding.tvPrice.setTextColor(getResources().getColor(R.color.colorLogOut));

            binding.rvFilter.setLayoutManager(new LinearLayoutManager(this));
            binding.rvFilter.setAdapter(new PriceRangeAdapter(this,this));


        }else if(type.equalsIgnoreCase("size")){
            binding.llSize.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            binding.tvSize.setTextColor(getResources().getColor(R.color.colorBlack));
            binding.llPrice.setBackgroundColor(getResources().getColor(R.color.colorView));
            binding.tvPrice.setTextColor(getResources().getColor(R.color.colorLogOut));

            binding.rvFilter.setLayoutManager(new LinearLayoutManager(this));
            binding.rvFilter.setAdapter( new FilterProductListBySizeAdapter(this, sizeList));
        }
    }
    
    @Override
    public void getPriceRange(long minValue, long maxValue) {
        this.maxValue= ""+maxValue;
        this.minValue= ""+minValue;
    }
}
