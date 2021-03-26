package com.satvick.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.satvick.R;
import com.satvick.adapters.ColorProductDetailAdapter;
import com.satvick.adapters.SelectSizeProductDetailsAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityRequestForExchangeNewBinding;
import com.satvick.databinding.PopUpExchangeRequestBinding;
import com.satvick.model.ProductDetailsResponse;
import com.satvick.model.RequestForOrderModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestForExchangeProductActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityRequestForExchangeNewBinding binding;
    String reason="",mBrand="",mImageUrl="",mProductDesc="",mSize="",mColor="",mQty="",
            mMrp="",mAmount="",mDiscountedPrice="",mOrderId="",mOrderNumber="",mDispatchBy="",
            mOrderDate="",mPaymentType="",mBuyer="",mLocation="",mProductId="",mCouponCode="";

    List<String> getColor;
    List<String> getSize;

    ColorProductDetailAdapter colorProductDetailAdapter;
    SelectSizeProductDetailsAdapter selectSizeProductDetailsAdapter;

    String sizeText=null;
    String colorText=null;
    String additionalComment="";

    String sizeName="";
    String colorName="";
    private String symbol;
    private Double convertedPice;

    private ProductDetailsResponse data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_for_exchange_new);
        this.convertedPice = Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString("converted_amount"));
        this.symbol = SharedPreferenceWriter.getInstance(this).getString("symbol");

        binding.ivBack.setOnClickListener(this);
        binding.tvRequestExchange.setOnClickListener(this);
        binding.tvAddNewAddress.setOnClickListener(this);
        binding.edtAdditionalComment.setOnClickListener(this);

        Intent intent=getIntent();
        if(intent!=null){
            mBrand=getIntent().getStringExtra("brand");
            mImageUrl=getIntent().getStringExtra("imgURl");
            mProductDesc=getIntent().getStringExtra("productDesc");
            mSize=getIntent().getStringExtra("size");
            mColor=getIntent().getStringExtra("color");
            mQty=String.valueOf(getIntent().getIntExtra("qty",0));
            mMrp=getIntent().getStringExtra("mrp");
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
            mCouponCode=getIntent().getStringExtra(GlobalVariables.coupan_code);
        }


        if (mImageUrl != null) {
            Picasso.with(this).load(mImageUrl).into(binding.ivProduct);
        }
        if (mSize == null) {
            binding.tvYouOrdered.setVisibility(View.GONE);
            binding.tvChooseAnother.setVisibility(View.GONE);
        }
        if (mSize.equalsIgnoreCase("")) {
            binding.tvYouOrdered.setVisibility(View.GONE);
            binding.tvChooseAnother.setVisibility(View.GONE);
        }

        binding.tvChooseAnother.setText("Choose Another "+getIntent().getStringExtra("size_label"));


        binding.tvProductName.setText(mBrand);
        binding.tvProductDesc.setText(mProductDesc);
        binding.tvName.setText(mBuyer);
        binding.tvAddressDefault.setText(mLocation);
        if(mDiscountedPrice.equalsIgnoreCase("0"))
        {
            binding.tvOff.setVisibility(View.GONE);
            binding.tvCuttedText.setVisibility(View.GONE);

        }
        else
        {
            binding.tvOff.setVisibility(View.GONE);
            binding.tvCuttedText.setVisibility(View.GONE);
            binding.tvOff.setText(mDiscountedPrice+"% OFF");
            binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(mAmount)*convertedPice));
            binding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(mMrp)*convertedPice));
        }

        if(mCouponCode!=null)
        {
            binding.tvApplied.setText(mCouponCode);
        }
        else
        {
            binding.llOffer.setVisibility(View.GONE);
        }

        binding.tvCuttedText.setPaintFlags(binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        callProductDetailsApi(binding.mainRl);

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int c_id = radioGroup.getCheckedRadioButtonId();
                Log.v("radioGroup", "c_id: " + c_id);

                int IReceivedADefectiveProduct_id = binding.tvIReceived.getId();
                int SizeTooLarge_id = binding.tvSizeTooLarge.getId();
                int SizeTooSmall_id = binding.tvSizeTooSmall.getId();


                if (c_id == IReceivedADefectiveProduct_id) {
                    Log.w("radioGroup", "IReceivedADefectiveProduct_id: " + IReceivedADefectiveProduct_id);
                    reason = "I Received A Defective/Wrong Product";
                }

                if (c_id == SizeTooLarge_id) {
                    Log.w("radioGroup", "SizeTooLarge_id: " + SizeTooLarge_id);
                    reason = "Size Too Large";
                }

                if (c_id == SizeTooSmall_id) {
                    Log.w("radioGroup", "SizeTooSmall_id: " + SizeTooSmall_id);
                    reason = "Size Too Small";
                }
            }
        });

    }

    private void callProductDetailsApi(final View view) {

        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);

        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ProductDetailsResponse> call = apiInterface.getProductDetailsResult(token, userId, mProductId);
        call.enqueue(new Callback<ProductDetailsResponse>() {
            @Override
            public void onResponse(Call<ProductDetailsResponse> call, Response<ProductDetailsResponse> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();

                    data = response.body();

                    if (response.body().getStatus().equals("SUCCESS")) {

                        // Toast.makeText(ProductDetailsActivityFinal.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        setData(data);

                    } else {
                        Toast.makeText(RequestForExchangeProductActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(RequestForExchangeProductActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callProductDetailsApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(RequestForExchangeProductActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(RequestForExchangeProductActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();


                }
            }

            @Override
            public void onFailure(Call<ProductDetailsResponse> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void setData(ProductDetailsResponse data) {
        getColor=data.getProductdetails().getColor();
        getSize=data.getProductdetails().getSize();
        colorText = data.getProductdetails().getDefaultcolor();
        setSizeAdapter(getSize);
        setColorAdapter(getColor);
    }


    private void setSizeAdapter(final List<String> getSize) {
        if(getSize.size()>0) {
            selectSizeProductDetailsAdapter = new SelectSizeProductDetailsAdapter(this, getSize);
            binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerSize.setAdapter(selectSizeProductDetailsAdapter);
            selectSizeProductDetailsAdapter.setListener(new SelectSizeProductDetailsAdapter.SelectSizeListener() {
                @Override
                public void itemClick(View view, int pos) {
                    sizeText = pos + "";
                    sizeName = getSize.get(pos);
                }
            });
        } else {
            binding.tvExchange.setVisibility(View.GONE);
            binding.recyclerSize.setVisibility(View.GONE);
        }

    }

    private void setColorAdapter(final List<String> getColor) {
        if(getColor.size()>0) {
            colorProductDetailAdapter = new ColorProductDetailAdapter(this, getColor);

            binding.recyclerColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerColor.setAdapter(colorProductDetailAdapter);


            colorProductDetailAdapter.setListener(new ColorProductDetailAdapter.ColorsChangedListener() {
                @Override
                public void itemClick(View view, String colornm, int pos) {
                    colorText = pos + "";
                    //colorName=colornm;
                    colorName = getColor.get(pos);
                }
            });
        }else{
            binding.tvLabelColor.setVisibility(View.GONE);
            binding.recyclerColor.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.ivSearch:
                startActivity(new Intent(RequestForExchangeProductActivity.this, SearchScreenActivity.class));
                break;

            case R.id.ivWishList:
                startActivity(new Intent(RequestForExchangeProductActivity.this, MyWishListActivity.class));
                break;

            case R.id.ivBag:
                startActivity(new Intent(RequestForExchangeProductActivity.this, MainActivity.class).putExtra("from", "RequestForExchangeProductActivity"));
                break;

            case R.id.tvRequestExchange:
                if(validateForm()){
                    if (HelperClass.showInternetAlert(RequestForExchangeProductActivity.this)) {
                        callRequestForOrderApi(binding.mainRl);//hit api
                    }
                }
                break;

            case R.id.tvAddNewAddress:
                startActivity(new Intent(this, AddNewAddressClickActivity.class).putExtra("from", "RequestForExchangeProductActivity"));
                break;

        }
    }

    private void callRequestForOrderApi(final View view) {

        final MyDialog myDialog=new MyDialog(RequestForExchangeProductActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<RequestForOrderModel> call = apiInterface.getRequestForOrderResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN),
                SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID),
                reason,
                binding.edtAdditionalComment.getText().toString(),
                sizeName,colorName,mOrderId,"exchange","","",mProductId);
        call.enqueue(new Callback<RequestForOrderModel>() {
            @Override
            public void onResponse(Call<RequestForOrderModel> call, Response<RequestForOrderModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        Toast.makeText(RequestForExchangeProductActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        openPopup();

                    } else {
                        Toast.makeText(RequestForExchangeProductActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(RequestForExchangeProductActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callProductDetailsApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(RequestForExchangeProductActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(RequestForExchangeProductActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();

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

        additionalComment = binding.edtAdditionalComment.getText().toString();

        if(sizeText==null && getSize.size()>0) {
            Toast.makeText(this, "Please select "+getIntent().getStringExtra("size_label"), Toast.LENGTH_SHORT).show();
            return false;
        }else if(colorText==null && getColor.size()>0) {
            Toast.makeText(this, "Please select color", Toast.LENGTH_SHORT).show();
            return false;
        }else if (additionalComment.length()==0) {
            Toast.makeText(this, "Please add comment", Toast.LENGTH_SHORT).show();
            return false;
        }else if (reason.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select reason", Toast.LENGTH_SHORT).show();
            return false;
        }
        return isValidate;
    }

    private void openPopup() {
        PopUpExchangeRequestBinding binding;
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_exchange_request, null, false);
        dialog.setContentView(binding.getRoot());

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);


        binding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestForExchangeProductActivity.this, MyOrderActivity.class).putExtra("from","CancelOrderActivity"));
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RequestForExchangeProductActivity.this, MyOrderActivity.class).putExtra("from","CancelOrderActivity"));
    }
}
