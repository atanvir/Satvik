package com.satvick.fragments.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.satvick.R;
import com.satvick.activities.LifeDescriptionActivity;
import com.satvick.activities.MyReferralsListAdapter;
import com.satvick.adapters.SubscriptionAdapter;
import com.satvick.databinding.FragmentSubscriptionBinding;
import com.satvick.model.ArticleModel;
import com.satvick.model.LifeResponseModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.satvick.utils.HelperClass.showInternetAlert;

public class  SubscriptionFragment extends Fragment implements Callback<ArticleModel>, View.OnClickListener {

    private FragmentSubscriptionBinding binding;
    private MyDialog dialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscription, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        initCtrl();
        if(showInternetAlert(requireActivity())) articlesApi();
    }

    public void init(){
        binding.toolbar.tvTitle.setText("Articles");
    }

    public void initCtrl(){
        binding.toolbar.ivBack.setOnClickListener(this);
    }

    private void articlesApi() {
        dialog=new MyDialog(requireActivity());
        dialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ArticleModel> call = apiInterface.articlesApi(HelperClass.getCacheData(requireActivity()).first,HelperClass.getCacheData(requireActivity()).second);
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<ArticleModel> call, Response<ArticleModel> response) {
        dialog.hideDialog();
        if (response.isSuccessful()) {
            if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                binding.rvSubscription.setLayoutManager(new LinearLayoutManager(requireActivity()));
                binding.rvSubscription.setAdapter(new SubscriptionAdapter(requireActivity(),response.body().getResponse()));
            }
            else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), requireActivity());
        }
        else CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), requireActivity());
    }

    @Override
    public void onFailure(Call<ArticleModel> call, Throwable t) {
        dialog.hideDialog();
        CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(), requireActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack : requireActivity().onBackPressed(); break;
        }
    }
}
