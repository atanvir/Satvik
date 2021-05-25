package com.satvick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.satvick.R;
import com.satvick.adapters.LifeCommonCategoryAdapter;
import com.satvick.databinding.ActivityLifeCategoryBinding;
import com.satvick.databinding.ActivityLifeTabBinding;
import com.satvick.databinding.ActivityProductDetailsBinding;
import com.satvick.model.LifeResponseModel;
import com.satvick.model.LifeTabBean;
import com.satvick.model.TabModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.satvick.utils.HelperClass.showInternetAlert;

public class LifeCategoryActivity extends YouTubeBaseActivity implements View.OnClickListener, YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener {

    private ActivityLifeCategoryBinding binding;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityLifeCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        if(showInternetAlert(this)) lifeCategoryApi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (youTubePlayer == null) binding.playerView.initialize(getString(R.string.youtube_api), this);
        else youTubePlayer.cueVideo(getString(R.string.video_id),0);
    }

    public void init(){
        binding.playerView.initialize(getString(R.string.youtube_api), this);
        binding.toolbar.tvTitle.setText(getIntent().getStringExtra("title"));
    }

    public void initCtrl(){
        binding.toolbar.ivBack.setOnClickListener(this);
    }

    private void lifeCategoryApi() {
        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<LifeResponseModel> call = apiInterface.lifeCategoryApi(getIntent().getLongExtra("_id",0)+"");
        call.enqueue(new Callback<LifeResponseModel>() {
            public void onResponse(Call<LifeResponseModel> call, Response<LifeResponseModel> response) {
                myDialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body());
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), LifeCategoryActivity.this);
                }
                else CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), LifeCategoryActivity.this);

            }

            @Override
            public void onFailure(Call<LifeResponseModel> call, Throwable t) {
                myDialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),LifeCategoryActivity.this);
            }
        });
    }

    private void setDataToUI(LifeResponseModel body) {
        // Category
        binding.rvSections.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSections.setAdapter(new LifeCommonCategoryAdapter(this,getCategoryData(body.getAppsatvicklife())));
    }

    private List<TabModel> getCategoryData(LifeResponseModel.Appsatvicklife data) {
        List<TabModel> list= new ArrayList<>();
        list.add(new TabModel(Long.parseLong(String.valueOf(1)),null,"Blogs",getSubCategories(data.getBlogs_array())));
        list.add(new TabModel(Long.parseLong(String.valueOf(2)),null,"V-Logs",getSubCategories(data.getVblogs_array())));
        list.add(new TabModel(Long.parseLong(String.valueOf(3)),null,"Workshop",getSubCategories(data.getWorkshop_array())));
        return list;
    }

    private List<LifeTabBean> getSubCategories(List<LifeResponseModel.RandomBlog> randomBlogs) {
        List<LifeTabBean> list=new ArrayList<>();
        for(int i=0;i<randomBlogs.size();i++){
            list.add(new LifeTabBean(randomBlogs.get(i).getId(),
                                     randomBlogs.get(i).getImage(),
                                     randomBlogs.get(i).getTitle(),
                                     randomBlogs.get(i).getSlug(),
                                     randomBlogs.get(i).getPaymentMode(),
                                     randomBlogs.get(i).getPrice(),
                                     randomBlogs.get(i).getShortDesc()));
        }
        return list;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack : onBackPressed(); break;
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            this.youTubePlayer=youTubePlayer;
            this.youTubePlayer.cueVideo(getString(R.string.video_id));
            this.youTubePlayer.setPlayerStateChangeListener(this);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {
        youTubePlayer.seekToMillis(0);

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override
    public void onBackPressed() {
//        youTubePlayer.release();
        super.onBackPressed();
    }
}