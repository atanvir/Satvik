package com.satvick.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityApplyCouponBinding;
import com.satvick.model.ApplyCouponModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.BillingHelper;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApplyCouponActivity extends AppCompatActivity implements View.OnClickListener, Callback<ApplyCouponModel> {
    private ActivityApplyCouponBinding binding;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityApplyCouponBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
    }

    private void init(){
    }

    private void initCtrl(){
        binding.btnApplyCode.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
        case R.id.btnApplyCode: if (!TextUtils.isEmpty(binding.edtCoupon.getText().toString())) applyCouponApi(); else Toast.makeText(this,"Please enter coupon code",Toast.LENGTH_SHORT).show(); break;
        case R.id.ivBack: onBackPressed(); break;
        }
    }

    private void applyCouponApi() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ApplyCouponModel> call = apiInterface.getApplyCouponResult(binding.edtCoupon.getText().toString(),
                BillingHelper.getInstance().getBillingData().getTOTAL(),
                HelperClass.getCacheData(this).second,
                HelperClass.getCacheData(this).first);
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<ApplyCouponModel> call, Response<ApplyCouponModel> response) {
        binding.progressBar.setVisibility(View.GONE);
        if (response.isSuccessful()) {
            if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) { setResult(RESULT_OK); finish();}
            else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) { CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),ApplyCouponActivity.this); }
        } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",ApplyCouponActivity.this);
    }

    @Override
    public void onFailure(Call<ApplyCouponModel> call, Throwable t) {
        binding.progressBar.setVisibility(View.GONE);
        CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),ApplyCouponActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
