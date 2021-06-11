package com.satvick.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.satvick.activities.OrderManageActivity;
import com.satvick.activities.MyOrderTrackActivity;
import com.satvick.activities.RequestForExchangeProductActivity;
import com.satvick.activities.ReturnItemActivity;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterImplementation implements AdapterInterface {
    private MyDialog dialog;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


    @Override
    public void onClickListner(Context context, int postion, String status, List<MyOrderDetailsModel.Response> myOrderDetailsModelList) {

        dialog = new MyDialog(context);
        if (status.equalsIgnoreCase("OrderAgain")) {
            callAddToCartOrBaglistApi(context,postion,myOrderDetailsModelList);
        } else {

            Intent intent = null;
            if (status.equalsIgnoreCase("Cancel")) {
                intent = new Intent(context, OrderManageActivity.class);

            } else if (status.equalsIgnoreCase("Track Order")) {
                intent = new Intent(context, MyOrderTrackActivity.class);

            } else if (status.equalsIgnoreCase("Return")) {
                intent = new Intent(context, ReturnItemActivity.class);

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
            Log.e("postion", String.valueOf(postion));

            Log.e("mrp", myOrderDetailsModelList.get(postion).getAmount());

        }
    }

    private void callAddToCartOrBaglistApi(Context context,int postion, List<MyOrderDetailsModel.Response> list) {
        dialog.showDialog();
        Call<CartListModel2> call = apiInterface.getAddToCartResult3(HelperClass.getCacheData((Activity) context).first,
                                                                     HelperClass.getCacheData((Activity) context).second,
                                                            list.get(postion).getProductId()+"",
                                                                     list.get(postion).getColor(), list.get(postion).getSize(),
                                                              list.get(postion).getQuantity()+"");
        call.enqueue(new Callback<CartListModel2>() {
            @Override
            public void onResponse(Call<CartListModel2> call, Response<CartListModel2> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) orderApi(context);
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(context, "Internal Server Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CartListModel2> call, Throwable t) {
                dialog.hideDialog();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void orderApi(Context context) {
        dialog.showDialog();
        Call<MyOrderDetailsModel> call = apiInterface.getMyOrderResult(HelperClass.getCacheData((Activity) context).first,
                                                                       HelperClass.getCacheData((Activity) context).second);
        call.enqueue(new Callback<MyOrderDetailsModel>() {
            @Override
            public void onResponse(Call<MyOrderDetailsModel> call, Response<MyOrderDetailsModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {}
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else Toast.makeText(context, "Internal Server Error!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<MyOrderDetailsModel> call, Throwable t) {
                dialog.hideDialog();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void callingIntent(String class_name, String total_price, String product_id,
                               String product_quantity, MyOrderDetailsModel.Response obj) {

    }
}
