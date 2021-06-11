package com.satvick.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.satvick.R;
import com.satvick.databinding.ActivityCancelOrderBinding;
import com.satvick.model.MyOrderDetailsModel;
import com.satvick.model.RequestForOrderModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderManageActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityCancelOrderBinding binding;
    private MyDialog dialog;
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    private MyOrderDetailsModel.Response data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCancelOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
    }

    private void init() {
        data=getIntent().getParcelableExtra("data");
        dialog=new MyDialog(this);
        if(data.getStatus().equals(GlobalVariables.delivred))  {
            binding.edComment.setHint("Reason for return");
            binding.toolbar.tvTitle.setText("Return");
        }
        else {
            binding.edComment.setHint("Reason for cancellion");
            binding.toolbar.tvTitle.setText("Cancel");
        }
    }

    private void initCtrl(){
        binding.toolbar.ivBack.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: onBackPressed(); break;
            case R.id.tvCancel: onBackPressed(); break;
            case R.id.tvConfirm: if (HelperClass.showInternetAlert(OrderManageActivity.this)) orderManagementApi(); break;
        }
    }

    private void orderManagementApi() {
        dialog.showDialog();
        Call<RequestForOrderModel> call = apiInterface.getRequestForOrderResult(HelperClass.getCacheData(this).first,
                                                                                HelperClass.getCacheData(this).second,
                                                                                "",
                                                                                binding.edComment.getText().toString(),
                                                                                data.size, data.color, data.id+"",
                                                                                data.getStatus().equals(GlobalVariables.delivred)?"return":"cancel",
                                                                                data.getStatus().equals(GlobalVariables.delivred)?data.paymentType:"",
                                                                                "", data.productId+"");
        call.enqueue(new Callback<RequestForOrderModel>() {
            @Override
            public void onResponse(Call<RequestForOrderModel> call, Response<RequestForOrderModel> response) {
                dialog.hideDialog();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) CommonUtil.orderStatusPopUp(OrderManageActivity.this,response.body().getMessage());
                    else if(response.body().getStatus().equals("FAILURE")) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),OrderManageActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",OrderManageActivity.this);
            }

            @Override
            public void onFailure(Call<RequestForOrderModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),OrderManageActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
