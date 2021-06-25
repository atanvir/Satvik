package com.satvick.activities;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.satvick.R;
import com.satvick.adapters.LifeCategoryTabAdapter;
import com.satvick.adapters.LifeCommonCategoryAdapter;
import com.satvick.databinding.ActivityLifeTabBinding;
import com.satvick.model.BannerBean;
import com.satvick.model.HomeResponseModel;
import com.satvick.model.LifeResponseModel;
import com.satvick.model.LifeTabBean;
import com.satvick.model.TabModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.satvick.utils.HelperClass.showInternetAlert;

public class LifeActivity extends YouTubeBaseActivity implements View.OnClickListener, YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener, Callback<LifeResponseModel> {
    private ActivityLifeTabBinding binding;
    private YouTubePlayer youTubePlayer;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLifeTabBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        if(showInternetAlert(this)) lifeApi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (youTubePlayer == null) binding.playerView.initialize(getString(R.string.youtube_api), this);
        else youTubePlayer.cueVideo(getString(R.string.video_id),0);
       }

    public void init(){
        binding.toolbar.tvTitle.setText(getIntent().getStringExtra("title"));
    }

    public void initCtrl(){
        binding.toolbar.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack : onBackPressed(); break;
        }
    }

    private void lifeApi() {
        Call<LifeResponseModel> call = apiInterface.appsatvicklife();
        call.enqueue(this);
    }

    private void setDataToUI(LifeResponseModel body) {
        // Tabs
        binding.rvTabs.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.rvTabs.setAdapter(new LifeCategoryTabAdapter(this,body.getAppsatvicklife().getCategory()));

        binding.tvLabel.setText(Html.fromHtml(body.getAppsatvicklife().getPageContent()));

        // Category
        binding.rvSections.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSections.setAdapter(new LifeCommonCategoryAdapter(this,getCategoryData(body.getAppsatvicklife())));

    }

    private List<TabModel> getCategoryData(LifeResponseModel.Appsatvicklife data) {
        List<TabModel> list= new ArrayList<>();
        list.add(new TabModel(Long.parseLong(String.valueOf(1)),null,"",getSubCategories(data.getRandomBlog())));
        list.add(new TabModel(Long.parseLong(String.valueOf(2)),data.getTestimonials(),"",null));
        list.add(new TabModel(Long.parseLong(String.valueOf(3)),null,"",getSubCategories(data.getRandomBlogTwo())));
        return list;
    }

    private List<LifeTabBean> getSubCategories(List<LifeResponseModel.RandomBlog> randomBlogs) {
        List<LifeTabBean> list=new ArrayList<>();
        for(int i=0;i<randomBlogs.size();i++){
            list.add(new LifeTabBean(randomBlogs.get(i).getId(), randomBlogs.get(i).getImage(),
                                     randomBlogs.get(i).getTitle(), randomBlogs.get(i).getSlug(),
                                     randomBlogs.get(i).getPaymentMode(), randomBlogs.get(i).getPrice(),
                                     randomBlogs.get(i).getShortDesc()));
        }
        return list;
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

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
    public void onBackPressed() {
        super.onBackPressed();
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
    public void onResponse(Call<LifeResponseModel> call, Response<LifeResponseModel> response) {
        binding.progressBar.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body());
            else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), LifeActivity.this);
        }
        else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error", LifeActivity.this);
    }

    @Override
    public void onFailure(Call<LifeResponseModel> call, Throwable t) {
        binding.progressBar.setVisibility(View.GONE);
        CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(), LifeActivity.this);
    }
}
