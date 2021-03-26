package com.satvick.activities;

import android.content.Context;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.adapters.SearchAdapter;
import com.satvick.databinding.ActivitySearchScreenBinding;
import com.satvick.model.SearchListModel;

import com.satvick.model.Searchlist;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchScreenActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    ActivitySearchScreenBinding binding;
    SearchAdapter searchAdapter;
    List<Searchlist> searchListModelList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_screen);
        binding.ivBack.setOnClickListener(this);
        binding.ivSearch.setOnClickListener(this);
        binding.edtSearch.setOnClickListener(this);
        binding.ivCross.setOnClickListener(this);
        binding.edtSearch.addTextChangedListener(this);
        binding.edtSearch.requestFocus();
        binding.edtSearch.setFocusable(true);


        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }





    }

    private void callSearchListApi(final View view, final String key) {

            final MyDialog myDialog=new MyDialog(this);

            Retrofit retrofit = ApiClient.getClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            Call<SearchListModel> call = apiInterface.getSearchListResult(key);
            call.enqueue(new Callback<SearchListModel>() {
                @Override
                public void onResponse(Call<SearchListModel> call, Response<SearchListModel> response) {

                    if (response.isSuccessful()) {


                        SearchListModel data = response.body();

                        if (response.body().getStatus().equals("SUCCESS")) {



                            searchListModelList = data.getSearchlist();
                            searchAdapter = new SearchAdapter(SearchScreenActivity.this, searchListModelList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchScreenActivity.this);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setAdapter(searchAdapter);

                        } else {
                            Toast.makeText(SearchScreenActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                        mSnackbar.setActionTextColor(ContextCompat.getColor(SearchScreenActivity.this,R.color.colorWhite));
                        mSnackbar.setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                callSearchListApi(view,key);
                                Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                                snackbar.getView().setBackground(ContextCompat.getDrawable(SearchScreenActivity.this,R.drawable.drawable_gradient_line));
                                snackbar.show();
                            }
                        });
                        mSnackbar.getView().setBackground(ContextCompat.getDrawable(SearchScreenActivity.this,R.drawable.drawable_gradient_line));
                        mSnackbar.show();
                    }
                }

                @Override
                public void onFailure(Call<SearchListModel> call, Throwable t) {
                    myDialog.hideDialog();
                }
            });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ivBack:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                finish();
                break;

//            case R.id.edtSearch:
//                searchFunctionality();
//                break;

            case R.id.ivCross:
                binding.edtSearch.setText("");
                break;

        }
    }

//    private void searchFunctionality() {
//
//        binding.edtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                // Toast.makeText(getApplicationContext(), "before text change", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (searchAdapter != null) {
//                    searchAdapter.getFilter().filter(charSequence);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                // Toast.makeText(getApplicationContext(), "after text change", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        callSearchListApi(binding.mainRl, String.valueOf(s));
    }

    @Override
    public void afterTextChanged(Editable s) {


    }
}
