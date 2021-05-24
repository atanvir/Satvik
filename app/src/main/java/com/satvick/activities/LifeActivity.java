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

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
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

public class LifeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLifeTabBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLifeTabBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        if(showInternetAlert(this)) lifeApi();
    }

    public void init(){
        getLifecycle().addObserver(binding.playerView);
        binding.playerView.enableBackgroundPlayback(false);

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
        final MyDialog myDialog=new MyDialog(LifeActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<LifeResponseModel> call = apiInterface.appsatvicklife();
        call.enqueue(new Callback<LifeResponseModel>() {
            public void onResponse(Call<LifeResponseModel> call, Response<LifeResponseModel> response) {
                    myDialog.hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body());
                        else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), LifeActivity.this);
                    }
                    else CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), LifeActivity.this);

            }

            @Override
            public void onFailure(Call<LifeResponseModel> call, Throwable t) {
                Log.e("exception",""+t.getLocalizedMessage());
                myDialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),LifeActivity.this);
            }
        });
    }

    private void setDataToUI(LifeResponseModel body) {
        // Tabs
        binding.rvTabs.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.rvTabs.setAdapter(new LifeCategoryTabAdapter(this,body.getAppsatvicklife().getCategory(),binding.playerView));

        binding.tvLabel.setText(Html.fromHtml(body.getAppsatvicklife().getPageContent()));

        // Category
        binding.rvSections.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSections.setAdapter(new LifeCommonCategoryAdapter(this,getCategoryData(body.getAppsatvicklife())));

    }

    private List<TabModel> getCategoryData(LifeResponseModel.Appsatvicklife data) {
        List<TabModel> list= new ArrayList<>();
        list.add(new TabModel(Long.parseLong(String.valueOf(1)),null,"",getSubCategories(data.getRandomBlog())));
        list.add(new TabModel(Long.parseLong(String.valueOf(2)),getBannerList(),"",null));
        list.add(new TabModel(Long.parseLong(String.valueOf(3)),null,"",getSubCategories(data.getRandomBlogTwo())));
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


    private List<BannerBean> getBannerList() {
        List<BannerBean> list= new ArrayList<>();
        list.add( new BannerBean("","","https://soulahe.com/public/lifeimg/9120249210524115734.png","How many emails landed in your inbox this month offering you a path to a better version of yourself? A new year carries the energy of a fresh slate, a page turn, a new chapter or maybe even a whole new book. In modern culture, a new year is viewed as an opportunity to wipe the slate clean and invite a brighter future. ","-Tanvir Ahmed"));
        list.add( new BannerBean("","","https://soulahe.com/public/lifeimg/9120249210524115734.png","How many emails landed in your inbox this month offering you a path to a better version of yourself? A new year carries the energy of a fresh slate, a page turn, a new chapter or maybe even a whole new book. In modern culture, a new year is viewed as an opportunity to wipe the slate clean and invite a brighter future. ","-Tanvir Ahmed"));
        return list;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.playerView.release();

    }
}
