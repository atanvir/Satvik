package com.satvick.activities;

import android.os.Bundle;
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
import com.satvick.utils.GlobalVariables;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApplyCouponActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityApplyCouponBinding binding;

    String totalPrice="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_apply_coupon);
        totalPrice=getIntent().getStringExtra("total_price");
        binding.btnApplyCode.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    private void callApplyCouponApi(final View view) {
        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ApplyCouponModel> call = apiInterface.getApplyCouponResult(binding.edtCoupon.getText().toString(),totalPrice,
                                                                        SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID),
                                                                        SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN));

        call.enqueue(new Callback<ApplyCouponModel>() {
            @Override
            public void onResponse(Call<ApplyCouponModel> call, Response<ApplyCouponModel> response) {

                if (response.isSuccessful()) {

                    ApplyCouponModel data = response.body();
                    myDialog.hideDialog();

                    if (data.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        setResult(RESULT_OK);
                        finish();
                    } else if(data.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(ApplyCouponActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_CANCELED);
                        finish();

                    }

                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(ApplyCouponActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callApplyCouponApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(ApplyCouponActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(ApplyCouponActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ApplyCouponModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }




    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnApplyCode:
                if (!binding.edtCoupon.getText().toString().isEmpty()
                        && binding.edtCoupon.getText().toString() != null
                        && !binding.edtCoupon.getText().toString().equals("null")
                        && !binding.edtCoupon.getText().toString().equals("")) {

                    callApplyCouponApi(binding.rlMain);
                }else {
                    Toast.makeText(this,"Please enter coupon code",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
