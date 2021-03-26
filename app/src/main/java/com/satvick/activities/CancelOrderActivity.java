package com.satvick.activities;

import android.app.Dialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityCancelOrderBinding;
import com.satvick.databinding.PopUpCancellationRequestBinding;
import com.satvick.model.RequestForOrderModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.HelperClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CancelOrderActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityCancelOrderBinding binding;

    String reason="";
    String mBrand="";
    String mImageUrl="";
    String mProductDesc="";
    String mSize="";
    String mColor="";
    String mQty="";
    String mMrp="";
    String mAmount="";
    String mDiscountedPrice="";
    String mOrderId="";
    String mDispatchBy="";
    String mOrderDate="";
    String mPaymentType="";
    String mBuyer="";
    String mLocation="";
    String mOrderNumber="";
    String mProductId="";
    String mAdditionalComment="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cancel_order);
        init();




        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int c_id = radioGroup.getCheckedRadioButtonId();
                Log.v("radioGroup", "c_id: " + c_id);

                int incorrectSize_id = binding.tvIncorrectSize.getId();
                int productNotRequired_id = binding.tvProductNotRequired.getId();
                int cashIssue_id = binding.tvCashIsssue.getId();
                int orderByMistake_id=binding.tvOrderByMistake.getId();
                int wantToChange_id=binding.tvWantToChange.getId();


                if (c_id == incorrectSize_id) {
                    Log.w("radioGroup", "incorrectSize_id: " + incorrectSize_id);
                    reason = "Incorrect Size Ordered";
                }

                if (c_id == productNotRequired_id) {
                    Log.w("radioGroup", "productNotRequired_id: " + productNotRequired_id);
                    reason = "Product Not Required Anymore";
                }

                if (c_id == cashIssue_id) {
                    Log.w("radioGroup", "cashIssue_id: " + cashIssue_id);
                    reason = "Cash Issue";
                }

                if (c_id == orderByMistake_id) {
                    Log.w("radioGroup", "orderByMistake_id: " + orderByMistake_id);
                    reason = "Ordered By Mistake";
                }

                if (c_id == wantToChange_id) {
                    Log.w("radioGroup", "wantToChange_id: " + wantToChange_id);
                    reason = "Wants To Change Style?Color";
                }

            }
        });
    }

    private void init() {
        binding.ivBack.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvConfirm.setOnClickListener(this);


        Intent intent=getIntent();
        if(intent!=null){
            mBrand=getIntent().getStringExtra("brand");
            mImageUrl=getIntent().getStringExtra("imgURl");
            mProductDesc=getIntent().getStringExtra("productDesc");
            mSize=getIntent().getStringExtra("size");
            mColor=getIntent().getStringExtra("color");
            mQty=String.valueOf(getIntent().getIntExtra("qty",0));
            mMrp=getIntent().getStringExtra("mrp");
            Log.e("Cancel_mrp",getIntent().getStringExtra("mrp"));
            mAmount=getIntent().getStringExtra("amount");
            mDiscountedPrice= String.valueOf(getIntent().getIntExtra("discountedPrice",0));
            mOrderNumber=getIntent().getStringExtra("order_id");
            mOrderId=String.valueOf(getIntent().getIntExtra("id",0));
            mDispatchBy=getIntent().getStringExtra("dispatch_by");
            mOrderDate=getIntent().getStringExtra("order_date");
            mPaymentType=getIntent().getStringExtra("payment_type");
            mBuyer=getIntent().getStringExtra("buyer");
            mLocation=getIntent().getStringExtra("location");
            mProductId=String.valueOf(getIntent().getIntExtra("product_id",0));
        }


        if(mPaymentType.equalsIgnoreCase("Cash On Delivery"))
        {
            binding.llTotalRefund.setVisibility(View.GONE);
        }
        else
        {
            binding.llTotalRefund.setVisibility(View.VISIBLE);
            Double convertedPrice = Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString("converted_amount"));
            String symbol = SharedPreferenceWriter.getInstance(this).getString("symbol");
            binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(mAmount)*convertedPrice));
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.tvCancel:
                startActivity(new Intent(this, MyOrderActivity.class).putExtra("from","CancelOrderActivity"));
                break;

            case R.id.tvConfirm:
                if(validateForm()){
                    if (HelperClass.showInternetAlert(CancelOrderActivity.this)) {
                        callRequestForOrderApi();//hit api
                    }
                }

                break;
        }
    }

    private void callRequestForOrderApi() {
        final MyDialog myDialog=new MyDialog(CancelOrderActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<RequestForOrderModel> call = apiInterface.getRequestForOrderResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN),
                SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID),
                reason,
                binding.edtAdditionalComment.getText().toString(),
                mSize,mColor,mOrderId,"cancel","","",mProductId);
        call.enqueue(new Callback<RequestForOrderModel>() {
            @Override
            public void onResponse(Call<RequestForOrderModel> call, Response<RequestForOrderModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        openPopUp(response.body().getMessage());

                    } else {
                        Toast.makeText(CancelOrderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    Toast.makeText(CancelOrderActivity.this, R.string.service_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestForOrderModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }


    private boolean validateForm() {
        boolean isValidate=true;

        mAdditionalComment = binding.edtAdditionalComment.getText().toString();

        if (reason.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select reason", Toast.LENGTH_SHORT).show();
           return false;
        }
        return isValidate;
    }


    private void openPopUp(String message) {

        PopUpCancellationRequestBinding binding;
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_cancellation_request, null, false);
        dialog.setContentView(binding.getRoot());

        binding.tvCancellationRequest.setText(message);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);


        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callApi
                startActivity(new Intent(CancelOrderActivity.this, MyOrderActivity.class).putExtra("from","CancelOrderActivity"));
            }
        });
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MyOrderActivity.class).putExtra("from","CancelOrderActivity"));
    }
}
