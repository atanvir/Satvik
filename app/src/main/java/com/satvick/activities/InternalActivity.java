package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.adapters.MenBannerAdapter;
import com.satvick.adapters.MenSectionDressesAndJumpsuitsAdapter;
import com.satvick.adapters.MenSectionEthnicCollectionAdapter;
import com.satvick.adapters.MenSectionFlashSaleAdapter;
import com.satvick.adapters.MenSectionFootWearAdapter;
import com.satvick.adapters.MenSectionFossilSmartWatchesAdapter;
import com.satvick.adapters.MenSectionGymClothsAdapter;
import com.satvick.adapters.MenSectionLatestTopsAdapter;
import com.satvick.adapters.MenSectionShopByOccassionAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityMenBinding;
import com.satvick.model.InnerPagesModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InternalActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMenBinding binding;

    MenBannerAdapter menBannerAdapter;
    MenSectionFlashSaleAdapter menSectionFlashSaleAdapter;
    MenSectionShopByOccassionAdapter menSectionShopByOccassionAdapter;
    MenSectionEthnicCollectionAdapter menSectionEthnicCollectionAdapter;
    MenSectionDressesAndJumpsuitsAdapter menSectionDressesAndJumpsuitsAdapter;
    MenSectionLatestTopsAdapter menSectionLatestTopsAdapter;
    MenSectionFootWearAdapter menSectionFootWearAdapter;
    MenSectionFossilSmartWatchesAdapter menSectionFossilSmartWatchesAdapter;
    MenSectionGymClothsAdapter menSectionGymClothsAdapter;

    List<InnerPagesModel.Subcategory_slider> subcategory_sliderList = new ArrayList<>();
    List<InnerPagesModel.Flash_sale> flashSaleList = new ArrayList<>();
    List<InnerPagesModel.Occasion_list> occasionLists = new ArrayList<>();
    List<InnerPagesModel.Bannerlist> bannerList = new ArrayList<>();
    List<InnerPagesModel.Other_section> otherSectionList = new ArrayList<>();

    List<InnerPagesModel.Data> otherData1=new ArrayList<>();
    List<InnerPagesModel.Data> otherData2=new ArrayList<>();
    List<InnerPagesModel.Data> otherData3=new ArrayList<>();
    List<InnerPagesModel.Data> otherData4=new ArrayList<>();
    List<InnerPagesModel.Data> otherData5=new ArrayList<>();
    List<InnerPagesModel.Data> otherData6=new ArrayList<>();
    List<InnerPagesModel.Data> otherData7=new ArrayList<>();

    String comeFrom="";
    String filterData="";
    String type="";
    String userId="";
    String titleNameFirst="";
    String titleNameSecond="";
    String titleNameThird="";
    private  LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_men);

        binding.ivBack.setOnClickListener(this);
        binding.rlTop.setOnClickListener(this);
        binding.ivSearch.setOnClickListener(this);
        binding.tvFlashSaleViewAll.setOnClickListener(this);
        binding.tvShopByOccassionViewAll.setOnClickListener(this);
        binding.tvEthnicCollectionViewAll.setOnClickListener(this);
        binding.tvDressesAndJumpSuitsViewAll.setOnClickListener(this);
        binding.tvLatestTopsViewAll.setOnClickListener(this);
        binding.ivBanner3.setOnClickListener(this);
        binding.ivBanner2.setOnClickListener(this);
        binding.ivBanner.setOnClickListener(this);
        binding.ivBannerr.setOnClickListener(this);
        binding.ivSave.setOnClickListener(this);

        if(getIntent()!=null){
            comeFrom=getIntent().getStringExtra("from");
            filterData=getIntent().getStringExtra("filterData");
            type=getIntent().getStringExtra("type");
        }

        binding.tvTitle.setText(filterData.replace("_"," "));
        binding.tvTitle.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


        if (SharedPreferenceWriter.getInstance(InternalActivity.this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false")||
                SharedPreferenceWriter.getInstance(InternalActivity.this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
            userId = "1";
        } else {
            userId = SharedPreferenceWriter.getInstance(InternalActivity.this).getString(SharedPreferenceKey.USER_ID);
        }

        if (HelperClass.showInternetAlert(InternalActivity.this)) {
            callInnerPagesApi(binding.mainRl);//hit api
        }


        setLatestTopsMenAdapter();

        setFossilSmartWatchesMenAdapter();

        setNewStylishTopMenAdapter();
    }

    private void callInnerPagesApi(final View view) {
        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<InnerPagesModel> call = apiInterface.getInnerPageResult(filterData,userId);
        call.enqueue(new Callback<InnerPagesModel>() {
            @Override
            public void onResponse(Call<InnerPagesModel> call, Response<InnerPagesModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    InnerPagesModel data = response.body();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        flashSaleList=data.getResponse().getFlash_sale();

                        if(flashSaleList.size()>0)
                        {
                            binding.rlFlashSale.setVisibility(View.VISIBLE);
                            binding.flashSaleRecyclerView.setVisibility(View.VISIBLE);
                            binding.rl1.setVisibility(View.VISIBLE);
                        }

                        subcategory_sliderList=data.getResponse().getSubcategory_slider();
                        if(subcategory_sliderList.size()>0)
                        {
                            binding.rll.setVisibility(View.VISIBLE);
                        }

//                        occasionLists=data.getResponse().getOccasion_list();
//                        if(occasionLists.size()>0)
//                        {
//                            binding.rl2.setVisibility(View.VISIBLE);
//                        }
                        bannerList=data.getResponse().getBannerlist();
                        otherSectionList=data.getResponse().getOther_section();
                        for(int i=0;i<otherSectionList.size();i++){

                            if(i==0){
                                titleNameFirst=otherSectionList.get(i).getSectionName();

                                if(otherSectionList.get(i).getData()!=null && otherSectionList.get(i).getData().size()>0){

                                    for(int j=0;j<otherSectionList.get(i).getData().size();j++){

                                        binding.rl3.setVisibility(View.VISIBLE);

                                        otherData1.add(new InnerPagesModel.Data(
                                                otherSectionList.get(i).getData().get(j).getLike_Status(),
                                                otherSectionList.get(i).getData().get(j).getProduct_id(),
                                                otherSectionList.get(i).getData().get(j).getPercentage(),
                                                otherSectionList.get(i).getData().get(j).getMrp(),
                                                otherSectionList.get(i).getData().get(j).getSp(),
                                                otherSectionList.get(i).getData().get(j).getName(),
                                                otherSectionList.get(i).getData().get(j).getBrand(),
                                                otherSectionList.get(i).getData().get(j).getImage()));
                                    }
                                }
                            }else if(i==1){
                                titleNameSecond=otherSectionList.get(i).getSectionName();
                                if(otherSectionList.get(i).getData()!=null && otherSectionList.get(i).getData().size()>0) {

                                    for(int j=0;j<otherSectionList.get(i).getData().size();j++){

                                        binding.rl4.setVisibility(View.VISIBLE);

                                        otherData2.add(new InnerPagesModel.Data(
                                                otherSectionList.get(i).getData().get(j).getLike_Status(),
                                                otherSectionList.get(i).getData().get(j).getProduct_id(),
                                                otherSectionList.get(i).getData().get(j).getPercentage(),
                                                otherSectionList.get(i).getData().get(j).getMrp(),
                                                otherSectionList.get(i).getData().get(j).getSp(),
                                                otherSectionList.get(i).getData().get(j).getName(),
                                                otherSectionList.get(i).getData().get(j).getBrand(),
                                                otherSectionList.get(i).getData().get(j).getImage()));
                                    }
                                }
                            }else if(i==2){
                                if(otherSectionList.get(i).getData()!=null && otherSectionList.get(i).getData().size()>0) {

                                    titleNameThird=otherSectionList.get(i).getSectionName();

                                    for(int j=0;j<otherSectionList.get(i).getData().size();j++){

                                        binding.rl5.setVisibility(View.VISIBLE);

                                        otherData3.add(new InnerPagesModel.Data(
                                                otherSectionList.get(i).getData().get(j).getLike_Status(),
                                                otherSectionList.get(i).getData().get(j).getProduct_id(),
                                                otherSectionList.get(i).getData().get(j).getPercentage(),
                                                otherSectionList.get(i).getData().get(j).getMrp(),
                                                otherSectionList.get(i).getData().get(j).getSp(),
                                                otherSectionList.get(i).getData().get(j).getName(),
                                                otherSectionList.get(i).getData().get(j).getBrand(),
                                                otherSectionList.get(i).getData().get(j).getImage()));
                                    }
                                }
                            }

                        }
                        binding.tvEthnicCollection.setText(titleNameFirst);
                        binding.tvDressesAndJumpSuits.setText(titleNameSecond);
                        binding.tvLatestGymTees.setText(titleNameThird);
                        //binding.tvLatestIphoneCases.setText();
                        //binding.tvExclusiveAppleWatch.setText();
                        //binding.tvGymCloths.setText();

                        /////////////API hited adapters///////////////////
                        setFlashSaleMenRecyclerView(flashSaleList);
                        setMenBannerRecyclerView(subcategory_sliderList);
                        setShopByOccassionMenRecyclerView(occasionLists);
                        if(bannerList.size()>0){
                            binding.ivBanner2.setVisibility(View.VISIBLE);
                            setBanner(bannerList);
                        }
                        setEthnicCollectionRecyclerView(otherData1);
                        setDressesAndJumpSuitsRecyclerView(otherData2);
                        setGymTeesMenAdapter(otherData3);
                    } else {
                        Toast.makeText(InternalActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(InternalActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callInnerPagesApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(InternalActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(InternalActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<InnerPagesModel> call, Throwable t) {
                myDialog.hideDialog();            }
        });
    }

    private void setBanner(List<InnerPagesModel.Bannerlist> bannerList) {
        int banner_size=bannerList.size();

        Glide.with(InternalActivity.this).load(bannerList.get(0).getImage()).into(binding.ivBanner2);
        if(banner_size==2)
        {
            binding.ivBanner3.setVisibility(View.VISIBLE);
          Glide.with(InternalActivity.this).load(bannerList.get(1).getImage()).into(binding.ivBanner3);
        }
         else
            {
             binding.ivBanner3.setVisibility(View.GONE);
         }

    }


    private void setMenBannerRecyclerView(List<InnerPagesModel.Subcategory_slider> subcategory_sliderList) {
        if (subcategory_sliderList.size() <= 3) {
            binding.ivBanner.setVisibility(View.INVISIBLE);
            binding.ivBannerr.setVisibility(View.INVISIBLE);
        } else
        {
            binding.ivBanner.setVisibility(View.VISIBLE);
            binding.ivBannerr.setVisibility(View.VISIBLE);
        }

        menBannerAdapter = new MenBannerAdapter(this,subcategory_sliderList,filterData,type);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.bannerMenRecyclerView.setLayoutManager(linearLayoutManager);
        binding.bannerMenRecyclerView.setAdapter(menBannerAdapter);

    }


    private void setFlashSaleMenRecyclerView(List<InnerPagesModel.Flash_sale> flashSaleList) {

        menSectionFlashSaleAdapter = new MenSectionFlashSaleAdapter(this,flashSaleList,filterData,type);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.flashSaleRecyclerView.setLayoutManager(linearLayoutManager);
        binding.flashSaleRecyclerView.setAdapter(menSectionFlashSaleAdapter);
    }


    private void setShopByOccassionMenRecyclerView(List<InnerPagesModel.Occasion_list> occasionLists) {

        menSectionShopByOccassionAdapter = new MenSectionShopByOccassionAdapter(this,occasionLists,filterData,type);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.shopByOccasionRecyclerView.setLayoutManager(linearLayoutManager);
        binding.shopByOccasionRecyclerView.setAdapter(menSectionShopByOccassionAdapter);

    }


    private void setEthnicCollectionRecyclerView(List<InnerPagesModel.Data> otherData1) {

        menSectionEthnicCollectionAdapter = new MenSectionEthnicCollectionAdapter(this,otherData1,filterData,type);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.ethnicCollectionRecyclerView.setLayoutManager(linearLayoutManager);
        binding.ethnicCollectionRecyclerView.setAdapter(menSectionEthnicCollectionAdapter);
    }


    private void setDressesAndJumpSuitsRecyclerView(List<InnerPagesModel.Data> otherData2) {

        menSectionDressesAndJumpsuitsAdapter = new MenSectionDressesAndJumpsuitsAdapter(this, otherData2,filterData,type);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.dressesAndJumpSuitsRecyclerView.setLayoutManager(linearLayoutManager);
        binding.dressesAndJumpSuitsRecyclerView.setAdapter(menSectionDressesAndJumpsuitsAdapter);
    }


    private void setGymTeesMenAdapter(List<InnerPagesModel.Data> otherData3) {

        menSectionGymClothsAdapter = new MenSectionGymClothsAdapter(this,otherData3,filterData,type);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.gymRecyclerView.setLayoutManager(linearLayoutManager);
        binding.gymRecyclerView.setAdapter(menSectionGymClothsAdapter);
    }


    private void setLatestTopsMenAdapter() {

        menSectionLatestTopsAdapter = new MenSectionLatestTopsAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.latestIphoneRecyclerView.setLayoutManager(linearLayoutManager);
        binding.latestIphoneRecyclerView.setAdapter(menSectionLatestTopsAdapter);
    }


    private void setNewStylishTopMenAdapter() {

        menSectionFootWearAdapter = new MenSectionFootWearAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.gymClothsRecyclerView.setLayoutManager(linearLayoutManager);
        binding.gymClothsRecyclerView.setAdapter(menSectionFootWearAdapter);
    }


    private void setFossilSmartWatchesMenAdapter() {
        menSectionFossilSmartWatchesAdapter = new MenSectionFossilSmartWatchesAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.exclusiveAppleWatchesRecyclerView.setLayoutManager(linearLayoutManager);
        binding.exclusiveAppleWatchesRecyclerView.setAdapter(menSectionFossilSmartWatchesAdapter);

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
               // startActivity(new Intent(this, MainActivity.class).putExtra("from", "InternalScreens"));
                break;

            case R.id.ivSearch:
                startActivity(new Intent(InternalActivity.this,SearchScreenActivity.class));
                break;

            case R.id.tvFlashSaleViewAll:
                Intent intent=new Intent(InternalActivity.this,ProductListActivity.class);
                intent.putExtra("from",GlobalVariables.flashSale);
                intent.putExtra(GlobalVariables.section_name,"Flash Sale");
                startActivity(intent);


                break;

           case R.id.tvEthnicCollectionViewAll:
               //others section one
               callIntent(String.valueOf(otherSectionList.get(0).getSubcatId()),otherSectionList.get(0).getSectionName());


                break;


            case R.id.tvDressesAndJumpSuitsViewAll:
                //other section two
                callIntent(String.valueOf(otherSectionList.get(1).getSubcatId()),otherSectionList.get(1).getSectionName());

                break;


            case R.id.tvLatestTopsViewAll:
                //other section three
                callIntent(String.valueOf(otherSectionList.get(2).getSubcatId()),otherSectionList.get(2).getSectionName());
                break;

            case R.id.ivBanner3:
                callIntent(String.valueOf(bannerList.get(1).getFilter_data()),bannerList.get(1).getName());

                break;

            case R.id.ivBanner2:
                callIntent(String.valueOf(bannerList.get(0).getFilter_data()),bannerList.get(0).getName());
                break;

            case R.id.ivBannerr:
                //right
                binding.bannerMenRecyclerView.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition()+1);
                break;

            case R.id.ivBanner:
                //left
                binding.bannerMenRecyclerView.getLayoutManager().scrollToPosition(linearLayoutManager.findFirstVisibleItemPosition()-1);
                break;

            case R.id.ivSave:
                startActivity(new Intent(InternalActivity.this, MyWishListActivity.class));
                break;

        }
    }

    private void callIntent(String subcatid,String section_name) {
        Intent intent=new Intent(InternalActivity.this,ProductListActivity.class);
        intent.putExtra("from","MenActivity");
        intent.putExtra(GlobalVariables.subcatid,subcatid);
        intent.putExtra(GlobalVariables.section_name,section_name);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

      if(comeFrom.equalsIgnoreCase("Flash Sale"))
      {
          startActivity(new Intent(this,MainActivity.class).putExtra("from",comeFrom));
          this.overridePendingTransition(R.anim.slide_in,R.anim.slide_out);

      }
      else {
          finish();
      }
    }
}
