package com.satvick.activities;

import android.content.Context;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.satvick.R;
import com.satvick.adapters.SearchAdapter;
import com.satvick.databinding.ActivitySearchScreenBinding;
import com.satvick.model.SearchListModel;

import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchScreenActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private ActivitySearchScreenBinding binding;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
    }

    private void init() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        binding.edtSearch.requestFocus();
        binding.edtSearch.setFocusable(true);
    }

    private void initCtrl(){
        binding.ivBack.setOnClickListener(this);
        binding.ivSearch.setOnClickListener(this);
        binding.edtSearch.setOnClickListener(this);
        binding.ivCross.setOnClickListener(this);
        binding.edtSearch.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: onBackPressed(); break;
            case R.id.ivCross: binding.edtSearch.setText(""); break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchApi(s+"");
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private void searchApi(String key) {
        binding.progressbar.setVisibility(View.VISIBLE);
        Call<SearchListModel> call = apiInterface.getSearchListResult(key);
        call.enqueue(new Callback<SearchListModel>() {
            @Override
            public void onResponse(Call<SearchListModel> call, Response<SearchListModel> response) {
                binding.progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        binding.recyclerView.setLayoutManager( new LinearLayoutManager(SearchScreenActivity.this));
                        binding.recyclerView.setAdapter(new SearchAdapter(SearchScreenActivity.this, response.body().getSearchlist()));

                    } else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),SearchScreenActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",SearchScreenActivity.this);
            }

            @Override
            public void onFailure(Call<SearchListModel> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }
}
