package com.satvick.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.MediaController;

import com.biteMe.utils.helpers.AppBarStateChangeListener;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.satvick.R;
import com.satvick.databinding.ActivityLifeDescriptionBinding;
import com.satvick.model.LifeResponseModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.ServerTimeCalculator;

import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.satvick.utils.HelperClass.showInternetAlert;

public class LifeDescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLifeDescriptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLifeDescriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        if(showInternetAlert(this)) lifeCategoryApi();
    }

    private void lifeCategoryApi() {
        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<LifeResponseModel> call = apiInterface.lifeContentApi(getIntent().getLongExtra("_id",0)+"");
        call.enqueue(new Callback<LifeResponseModel>() {
            public void onResponse(Call<LifeResponseModel> call, Response<LifeResponseModel> response) {
                myDialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body());
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), LifeDescriptionActivity.this);
                }
                else CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), LifeDescriptionActivity.this);

            }

            @Override
            public void onFailure(Call<LifeResponseModel> call, Throwable t) {
                myDialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),LifeDescriptionActivity.this);
            }
        });
    }

    private void setDataToUI(LifeResponseModel body) {
        if(body.getAppsatvicklifecontent().getBlog().getVideo()!=null){
            loadVideo(body);
        }
        Glide.with(this).load(body.getAppsatvicklifecontent().getBlog().getImage()).into(binding.ivPhone);
        binding.tvTitle.setText(body.getAppsatvicklifecontent().getBlog().getTitle());
        binding.tvDate.setText("Posted On: "+ServerTimeCalculator.getDateDifference(body.getAppsatvicklifecontent().getBlog().getCreatedAt()));
        if(body.getAppsatvicklifecontent().getBlog().getPaymentMode().equalsIgnoreCase("Free")) {
            binding.btnBuyNow.setVisibility(View.GONE);
            binding.tvDescription.setText(Html.fromHtml(body.getAppsatvicklifecontent().getBlog().getLongDesc()));
        }else{
            binding.btnBuyNow.setVisibility(View.VISIBLE);
            binding.tvDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            binding.tvDescription.setText("Price: "+ Math.round(body.getAppsatvicklifecontent().getBlog().getPrice())+" | Duration: "+body.getAppsatvicklifecontent().getBlog().getDuration() +" Days");
        }
    }

    private void loadVideo(LifeResponseModel body) {
        binding.videoview.setVisibility(View.VISIBLE);
        binding.tvVideoTitle.setVisibility(View.VISIBLE);
        binding.tvVideoTitle.setText(""+body.getAppsatvicklifecontent().getBlog().getVideoTitle());
        binding.videoview.setSource(body.getAppsatvicklifecontent().getBlog().getVideo());
        binding.videoview.setPlayWhenReady(false);


    }

    public void init(){
        binding.toolbar.tvTitle.setText(/*getIntent().getStringExtra("title")*/"Satvik Life");
    }

    public void initCtrl(){
        binding.toolbar.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ivBack :
            onBackPressed();
            break;
        }
    }


}