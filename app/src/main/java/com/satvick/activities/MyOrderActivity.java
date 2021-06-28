package com.satvick.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.satvick.R;
import com.satvick.adapters.MyOrderAdapter;
import com.satvick.adapters.ProductListAdapter;
import com.satvick.databinding.ActivityMyOrderBinding;

import com.satvick.databinding.PopUpCancellationConfirmBinding;
import com.satvick.databinding.PopUpCancellationRequestBinding;
import com.satvick.model.AdapterInterface;
import com.satvick.model.CartListModel2;
import com.satvick.model.MyOrderDetailsModel;
import com.satvick.model.MyOrderHelp;
import com.satvick.model.RequestForOrderModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener, AdapterInterface {
    private ActivityMyOrderBinding binding;
    private MyDialog dialog;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
    }

    private void init() {
        dialog=new MyDialog(this);
        if (getIntent().getStringExtra("from") != null) {
            if (getIntent().getStringExtra("from").equalsIgnoreCase("HelpCenterActivity")) orderHelpApi();
            else orderApi();
        } else orderApi();
    }

    private void initCtrl(){
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: onBackPressed(); break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void orderHelpApi() {
        dialog.showDialog();
        Call<MyOrderHelp> call = apiInterface.getMyOrderHelp(HelperClass.getCacheData(this).first, HelperClass.getCacheData(this).second);
        call.enqueue(new Callback<MyOrderHelp>() {
            @Override
            public void onResponse(Call<MyOrderHelp> call, Response<MyOrderHelp> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        binding.tvTitle1.setText("Recent Purchased");
                        binding.view.setVisibility(View.GONE);
                        binding.myOrdersRecycler.setAdapter(new ProductListAdapter(MyOrderActivity.this, response.body().getResponse(), MyOrderActivity.class.getSimpleName()));
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(), MyOrderActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(), "Internal Server Error!", MyOrderActivity.this);
            }

            @Override
            public void onFailure(Call<MyOrderHelp> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(), t.getMessage(), MyOrderActivity.this);
            }
        });
    }


   @Override
    public void onClickListner(Context context, int postion, String status, List<MyOrderDetailsModel.Response> myOrderDetailsModelList) {
        if (status.equalsIgnoreCase("OrderAgain")) {
            callAddToCartOrBaglistApi(context,postion,myOrderDetailsModelList);
        }else if(status.equalsIgnoreCase("Cancel")){
            cancelOrderPopup(myOrderDetailsModelList.get(postion));
        } else {
            Intent intent = null;
            if (status.equalsIgnoreCase("Cancel")) {
                intent = new Intent(context, OrderManageActivity.class);
                intent.putExtra("data",myOrderDetailsModelList.get(postion));


            } else if (status.equalsIgnoreCase("Track Order")) {
                intent = new Intent(context, MyOrderTrackActivity.class);

            } else if (status.equalsIgnoreCase("Return")) {
                intent = new Intent(context, ReturnItemActivity.class);
                intent.putExtra("data",myOrderDetailsModelList.get(postion));

            } else if (status.equalsIgnoreCase("Exchange")) {
                intent = new Intent(context, RequestForExchangeProductActivity.class);

            }
            if (!myOrderDetailsModelList.get(postion).getCoupanCode().equalsIgnoreCase("")) {
                intent.putExtra(GlobalVariables.coupan_code, myOrderDetailsModelList.get(postion).getCoupanCode());
            }
            intent.putExtra("from", "MyOrderAdapter");
            intent.putExtra("imgURl", myOrderDetailsModelList.get(postion).getImage());
            intent.putExtra("productDesc", myOrderDetailsModelList.get(postion).getProductName());
            intent.putExtra("brand", myOrderDetailsModelList.get(postion).getBrand());
            intent.putExtra("size", myOrderDetailsModelList.get(postion).getSize());
            intent.putExtra("color", myOrderDetailsModelList.get(postion).getColor());
            intent.putExtra("qty", myOrderDetailsModelList.get(postion).getQuantity());
            intent.putExtra("mrp", myOrderDetailsModelList.get(postion).getMrp());
            intent.putExtra("amount", myOrderDetailsModelList.get(postion).getAmount());
            intent.putExtra("discountedPrice", myOrderDetailsModelList.get(postion).getPercentage());
            intent.putExtra("order_id", myOrderDetailsModelList.get(postion).getOrderNumber());
            intent.putExtra("dispatch_by", myOrderDetailsModelList.get(postion).getDispatchBy());
            intent.putExtra("order_date", myOrderDetailsModelList.get(postion).getOrderDate());
            intent.putExtra("payment_type", myOrderDetailsModelList.get(postion).getPaymentType());
            intent.putExtra("buyer", myOrderDetailsModelList.get(postion).getBuyer());
            intent.putExtra("location", myOrderDetailsModelList.get(postion).getLocation());
            intent.putExtra("id", myOrderDetailsModelList.get(postion).getId());
            intent.putExtra("sku", myOrderDetailsModelList.get(postion).getSku());
            intent.putExtra("product_id", myOrderDetailsModelList.get(postion).getProductId());
            intent.putExtra(GlobalVariables.order_status, myOrderDetailsModelList.get(postion).getStatus());
            intent.putExtra("size_label", "" + myOrderDetailsModelList.get(postion).getSize_label());
            if (myOrderDetailsModelList.get(postion).getNotifyStatus() != null) {
                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) myOrderDetailsModelList.get(postion).getNotifyStatus());
            }
            context.startActivity(intent);
        }
    }

    public void cancelOrderPopup(MyOrderDetailsModel.Response response) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        PopUpCancellationConfirmBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_cancellation_confirm, null, false);
        dialog.setContentView(binding.getRoot());

        binding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        binding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                orderManagementApi(response);
            }
        });
        dialog.show();
    }


    private void orderManagementApi(MyOrderDetailsModel.Response data) {
        dialog.showDialog();
        Call<RequestForOrderModel> call = apiInterface.getRequestForOrderResult(HelperClass.getCacheData(this).first,
                                                                                HelperClass.getCacheData(this).second,
                                                                                "",
                                                                                "",
                                                                                data.size, data.color, data.id+"",
                                                                                data.getStatus().equals(GlobalVariables.delivred)?"return":"cancel",
                                                                                data.getStatus().equals(GlobalVariables.delivred)?data.paymentType:"",
                                                                                "", data.productId+"");
        call.enqueue(new Callback<RequestForOrderModel>() {
            @Override
            public void onResponse(Call<RequestForOrderModel> call, Response<RequestForOrderModel> response) {
                dialog.hideDialog();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) CommonUtil.orderStatusPopUp(MyOrderActivity.this,response.body().getMessage());
                    else if(response.body().getStatus().equals("FAILURE")) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),MyOrderActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",MyOrderActivity.this);
            }

            @Override
            public void onFailure(Call<RequestForOrderModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),MyOrderActivity.this);
            }
        });
    }



    private void callAddToCartOrBaglistApi(Context context,int postion, List<MyOrderDetailsModel.Response> list) {
        dialog.showDialog();
        Call<CartListModel2> call = apiInterface.getAddToCartResult3(HelperClass.getCacheData(this).first,
                                                                     HelperClass.getCacheData((Activity) context).second,
                                                                     list.get(postion).getProductId()+"",
                                                                        list.get(postion).getColor(), list.get(postion).getSize(),
                                                                        list.get(postion).getQuantity()+"");
        call.enqueue(new Callback<CartListModel2>() {
            @Override
            public void onResponse(Call<CartListModel2> call, Response<CartListModel2> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) Toast.makeText(MyOrderActivity.this, "Product Added to the cart!", Toast.LENGTH_SHORT).show();
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(), MyOrderActivity.this);
                }
                else CommonUtil.setUpSnackbarMessage(binding.getRoot(), "Internal Server Error!", MyOrderActivity.this);
            }

            @Override
            public void onFailure(Call<CartListModel2> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(), t.getMessage(), MyOrderActivity.this);
            }
        });



    }

    private void orderApi() {
        dialog.showDialog();
        Call<MyOrderDetailsModel> call = apiInterface.getMyOrderResult(HelperClass.getCacheData(this).first, HelperClass.getCacheData(this).second);
        call.enqueue(new Callback<MyOrderDetailsModel>() {
            @Override
            public void onResponse(Call<MyOrderDetailsModel> call, Response<MyOrderDetailsModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        binding.myOrdersRecycler.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this));
                        binding.myOrdersRecycler.setAdapter(new MyOrderAdapter(MyOrderActivity.this, response.body().getResponse(),MyOrderActivity.this));
                    }
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(), MyOrderActivity.this);
                }else CommonUtil.setUpSnackbarMessage(binding.getRoot(), "Internal Server Error!", MyOrderActivity.this);
            }

            @Override
            public void onFailure(Call<MyOrderDetailsModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(), t.getMessage(), MyOrderActivity.this);
            }
        });
    }
}
