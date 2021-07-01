package com.satvick.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.potyvideo.library.globalEnums.EnumAspectRatio;
import com.potyvideo.library.globalInterfaces.AndExoPlayerListener;
import com.satvick.R;
import com.satvick.databinding.ActivityLifeDescriptionBinding;
import com.satvick.databinding.SatvikLifeBinding;
import com.satvick.model.LifeResponseModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;
import com.satvick.utils.ServerTimeCalculator;


import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.satvick.utils.HelperClass.showInternetAlert;
import static com.satvick.utils.HelperClass.showSatvikLifeArticle;

public class LifeDescriptionActivity extends AppCompatActivity implements View.OnClickListener, Callback<LifeResponseModel> {

    private ActivityLifeDescriptionBinding binding;
    private LifeResponseModel.Blog data;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLifeDescriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        if(showInternetAlert(this)) lifeCategoryApi();
    }



    public void init(){
        binding.tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        binding.toolbar.tvTitle.setText(/*getIntent().getStringExtra("title")*/"Satvik Life");
    }

    public void initCtrl(){
        binding.toolbar.ivBack.setOnClickListener(this);
        binding.ivPlay.setOnClickListener(this);
        binding.btnBuyNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack : onBackPressed(); break;
            case R.id.ivPlay : binding.btnBuyNow.performClick(); break;
            case R.id.btnBuyNow : showSatvikLifeArticle(this,Math.round(data.getPrice())+"",data.getId().toString());break;
        }
    }


    private void lifeCategoryApi() {
        Call<LifeResponseModel> call = apiInterface.lifeContentApi(getIntent().getLongExtra("_id",0)+"", HelperClass.getCacheData(this).second);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<LifeResponseModel> call, Response<LifeResponseModel> response) {
        binding.progressBar.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body());
            else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), LifeDescriptionActivity.this);
        }
        else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error", LifeDescriptionActivity.this);
    }

    @Override
    public void onFailure(Call<LifeResponseModel> call, Throwable t) {
        binding.progressBar.setVisibility(View.GONE);
        CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(), LifeDescriptionActivity.this);
    }

    private void setDataToUI(LifeResponseModel body) {
        data= body.getAppsatvicklifecontent().getBlog();
        Glide.with(this).load(body.getAppsatvicklifecontent().getBlog().getImage()).into(binding.ivPhone);
        binding.tvTitle.setText(body.getAppsatvicklifecontent().getBlog().getTitle());
        binding.tvDate.setText("Posted On: "+ServerTimeCalculator.getDateDifference(body.getAppsatvicklifecontent().getBlog().getCreatedAt()));
        binding.tvVideoTitle.setText(""+body.getAppsatvicklifecontent().getBlog().getVideoTitle());
        if(body.getAppsatvicklifecontent().getBlog().getPaymentMode().equalsIgnoreCase("Free") ||
                body.getAppsatvicklifecontent().getHave_subscribe().equalsIgnoreCase("1")) {
            binding.btnBuyNow.setVisibility(View.GONE);
            binding.tvDescription.setText(Html.fromHtml(body.getAppsatvicklifecontent().getBlog().getLongDesc()));
            if(body.getAppsatvicklifecontent().getBlog().getVideo()!=null){
                binding.videoview.setVisibility(View.VISIBLE);
                binding.tvVideoTitle.setVisibility(View.VISIBLE);
                binding.videoview.setSource(body.getAppsatvicklifecontent().getBlog().getVideo());
                binding.videoview.setShowTimeOut(4000);
            }else{
                binding.videoview.setVisibility(View.GONE);
            }

        }else{
            binding.btnBuyNow.setVisibility(View.VISIBLE);
            binding.tvDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            binding.tvDescription.setText("Price: "+ Math.round(body.getAppsatvicklifecontent().getBlog().getPrice())+" | Duration: "+body.getAppsatvicklifecontent().getBlog().getDuration() +" Days");
            if(body.getAppsatvicklifecontent().getBlog().getVideo()!=null){
                Glide.with(this).load(body.getAppsatvicklifecontent().getBlog().getVideoImage()).apply(new RequestOptions().placeholder(R.drawable.splash_logo)).into(binding.ivVideoThumbNail);
                binding.tvVideoTitle.setVisibility(View.VISIBLE);
                binding.ivVideoThumbNail.setVisibility(View.VISIBLE);
                binding.ivPlay.setVisibility(View.VISIBLE);
            }else{
                binding.videoview.setVisibility(View.GONE);
            }
        }
    }
}