package com.satvick.activities;

import android.app.Dialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListPopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityReturnItemBinding;
import com.satvick.databinding.PopUpReturnOrderBinding;
import com.satvick.databinding.PopUpReturnPolicyBinding;
import com.satvick.databinding.PopUpReturnRequestBinding;
import com.satvick.model.RequestForOrderModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.HelperClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ReturnItemActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityReturnItemBinding binding;
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
    String mPaymentMode="";
    String otherReason="";
    String symbol;
    Double convertedPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_return_item);
        symbol = SharedPreferenceWriter.getInstance(this).getString("symbol");
        convertedPrice = Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString("converted_amount"));
        binding.ivBack.setOnClickListener(this);
        binding.tvProceed.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvAddNewAddress.setOnClickListener(this);
        binding.rlReturnPolicy.setOnClickListener(this);
        binding.chbProductIsUnused.setOnClickListener(this);
        binding.chbAllTagsIntact.setOnClickListener(this);
        binding.llChooseModeOfRefund.setOnClickListener(this);
        binding.tvSelectMode.setOnClickListener(this);


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
        }

        if (mImageUrl != null) {
            Picasso.with(this).load(mImageUrl).into(binding.ivProduct);
        }

        binding.tvRupee.setText(symbol+" "+Math.round(Double.parseDouble(mAmount)*convertedPrice));
        binding.tvOrderNumber.setText(mOrderId);
        binding.tvProductName.setText(mBrand);
        binding.tvProductDesc.setText(mProductDesc);
        binding.tvSizeContent.setText(mSize);
        binding.tvSize.setText(getIntent().getStringExtra("size_label")+":");
        binding.tvLocation.setText(mLocation);
        if(mDiscountedPrice.equalsIgnoreCase("0"))
        {
            binding.tvOff.setVisibility(View.GONE);
            binding.tvCuttedText.setVisibility(View.GONE);
        }
        else
        {
            binding.tvOff.setVisibility(View.VISIBLE);
            binding.tvCuttedText.setVisibility(View.VISIBLE);
            binding.tvOff.setText(mDiscountedPrice);
            binding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(mMrp)*convertedPrice));
            binding.tvCuttedText.setPaintFlags(binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }

        binding.tvName.setText(mBuyer);

        binding.tvRefundAmount.setText("Refund Amount "+symbol+" "+Math.round(Double.parseDouble(mAmount)*convertedPrice));
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat desireFormat=new SimpleDateFormat("dd MMM 20yy");
        String orderDate="";
        try
        {

            Date date=dateFormat.parse(mOrderDate);
            orderDate=desireFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.tvDate.setText(orderDate);

        binding.chbProductIsUnused.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chbProductIsUnused.setButtonDrawable(ContextCompat.getDrawable(ReturnItemActivity.this, R.drawable.sale_box));
                    otherReason="Product Is unused";
                } else {
                    binding.chbProductIsUnused.setButtonDrawable(ContextCompat.getDrawable(ReturnItemActivity.this, R.drawable.new_rect));
                }
            }
        });

        binding.chbAllTagsIntact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chbAllTagsIntact.setButtonDrawable(ContextCompat.getDrawable(ReturnItemActivity.this, R.drawable.sale_box));
                    otherReason="All the tags are intact";
                } else {
                    binding.chbAllTagsIntact.setButtonDrawable(ContextCompat.getDrawable(ReturnItemActivity.this, R.drawable.new_rect));
                }
            }
        });

       binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup radioGroup, int i) {
               int c_id = radioGroup.getCheckedRadioButtonId();
               Log.v("radioGroup", "c_id: " + c_id);

               int MyReasonIsNotListedHere_id = binding.tvMyReasonIsNotListed.getId();
               int TheProductDidNotFit_id = binding.tvTheProductDidNotFit.getId();
               int ReceivedADefective_id = binding.tvReceivedADefective.getId();
               int ReceivedAWrong_id=binding.tvReceivedAWrong.getId();
               int NotWorth_id=binding.tvNotWorth.getId();


               if (c_id == MyReasonIsNotListedHere_id) {
                   Log.w("radioGroup", "MyReasonIsNotListedHere_id: " + MyReasonIsNotListedHere_id);
                   reason = "My Reason Is Not Listed Here";
               }

               if (c_id == TheProductDidNotFit_id) {
                   Log.w("radioGroup", "TheProductDidNotFit_id: " + TheProductDidNotFit_id);
                   reason = "The Product Did't Fit Me";
               }

               if (c_id == ReceivedADefective_id) {
                   Log.w("radioGroup", "ReceivedADefective_id: " + ReceivedADefective_id);
                   reason = "Received A Defective Product";
               }

               if (c_id == ReceivedAWrong_id) {
                   Log.w("radioGroup", "ReceivedAWrong_id: " + ReceivedAWrong_id);
                   reason = "Received A Wrong Product";
               }

               if (c_id == NotWorth_id) {
                   Log.w("radioGroup", "NotWorth_id: " + NotWorth_id);
                   reason = "Not Worth The Value";
               }
           }
       });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean validateForm() {
        boolean isValidate=true;

        mAdditionalComment = binding.edtAdditionalComment.getText().toString();

        if (reason.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select issue", Toast.LENGTH_SHORT).show();
            return false;
        }else if(otherReason.equals("")){
            Toast.makeText(this, "Please select other reason", Toast.LENGTH_SHORT).show();
            return false;
        } else if(mPaymentMode.equals("")){
            Toast.makeText(this, "Please choose mode of refund", Toast.LENGTH_SHORT).show();
            return false;
        }else if (mAdditionalComment.length()==0) {
            Toast.makeText(this, "Please add comment", Toast.LENGTH_SHORT).show();
            return false;
        }

        return isValidate;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.tvProceed:

                //openPopUp();

                if(validateForm()){
                    if (HelperClass.showInternetAlert(ReturnItemActivity.this)) {
                        callReturnItemApi(binding.rlRecentOrdersOngoing);//hit api
                    }
                }

                break;

            case R.id.tvCancel:
                startActivity(new Intent(this, MyOrderActivity.class).putExtra("from","CancelOrderActivity"));
                break;

            case R.id.tvAddNewAddress:
                startActivity(new Intent(this, AddNewAddressClickActivity.class).putExtra("from","ReturnItemActivity"));
                break;

            case R.id.rlReturnPolicy:
                openPopupReturnPolicy();
                break;

            case R.id.tvSelectMode:
                showMode();
                break;
        }
    }

    private void callReturnItemApi(final View view) {

        final MyDialog myDialog=new MyDialog(ReturnItemActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<RequestForOrderModel> call = apiInterface.getRequestForOrderResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN),
                SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID),
                reason,
                binding.edtAdditionalComment.getText().toString(),
                mSize,
                mColor,
                mOrderId,
                "return",
                mPaymentMode,
                otherReason,
                mProductId);
        call.enqueue(new Callback<RequestForOrderModel>() {
            @Override
            public void onResponse(Call<RequestForOrderModel> call, Response<RequestForOrderModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        Toast.makeText(ReturnItemActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        openPopUpReturnOrder();
                    } else {
                        Toast.makeText(ReturnItemActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(ReturnItemActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callReturnItemApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(ReturnItemActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(ReturnItemActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                }
            }

            @Override
            public void onFailure(Call<RequestForOrderModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void showMode() {
        final List<String> modeList = new ArrayList<>();
        modeList.add("Bank Transfer");
        modeList.add("Wallet");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, modeList);

        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedMode = modeList.get(i);
                binding.tvSelectMode.setText(selectedMode);

                if (selectedMode.equals("Bank Transfer")) {
                    mPaymentMode="Bank Transfer";
                } else {
                    mPaymentMode="Wallet";
                }
                listPopupWindow.dismiss();
            }
        });

        listPopupWindow.setAnchorView(binding.tvSelectMode);
        listPopupWindow.setAdapter(arrayAdapter);
        listPopupWindow.show();
    }

    private void openPopUpReturnOrder() {
        PopUpReturnOrderBinding binding;
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding=DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_return_order, null, false);
        dialog.setContentView(binding.getRoot());

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        binding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openPopUp();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MyOrderActivity.class).putExtra("from","CancelOrderActivity"));
    }

    private void openPopupReturnPolicy() {


        PopUpReturnPolicyBinding binding;
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_return_policy, null, false);
        dialog.setContentView(binding.getRoot());

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        binding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openPopUp() {

        PopUpReturnRequestBinding binding;
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_return_request, null, false);
        dialog.setContentView(binding.getRoot());

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);


        binding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReturnItemActivity.this, MyOrderActivity.class).putExtra("from","CancelOrderActivity"));
            }
        });
        dialog.show();
    }

}
