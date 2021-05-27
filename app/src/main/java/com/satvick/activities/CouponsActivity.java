package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.adapters.CouponsAdapter;
import com.satvick.databinding.ActivityCouponsBinding;
import com.satvick.model.CouponsListModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CouponsActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityCouponsBinding binding;

    List<CouponsListModel.Response> couponsListResponseList = new ArrayList<>();
    CouponsAdapter couponsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_coupons);

        CouponsApi(binding.mainRl);//hit api

        binding.ivBack.setOnClickListener(this);
    }

    private void CouponsApi(final View view) {

        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<CouponsListModel> call = apiInterface.getCouponsListResult();
        call.enqueue(new Callback<CouponsListModel>() {
            @Override
            public void onResponse(Call<CouponsListModel> call, Response<CouponsListModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();

                    CouponsListModel data = response.body();

                    if (response.body().getStatus().equals("SUCCESS")) {

                       // Toast.makeText(CouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        couponsListResponseList=data.getResponse();

                        setCouponsListAdapter(couponsListResponseList);
                    } else {
                        Toast.makeText(CouponsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(CouponsActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            CouponsApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(CouponsActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(CouponsActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                }
            }

            @Override
            public void onFailure(Call<CouponsListModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void setCouponsListAdapter(List<CouponsListModel.Response> couponsListResponseList) {
        couponsAdapter = new CouponsAdapter(this,couponsListResponseList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(couponsAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
