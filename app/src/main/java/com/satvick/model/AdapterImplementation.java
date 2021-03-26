package com.satvick.model;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import com.satvick.activities.CancelOrderActivity;
import com.satvick.activities.MyOrderTrackActivity;
import com.satvick.activities.RequestForExchangeProductActivity;
import com.satvick.activities.ReturnItemActivity;
import com.satvick.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

public class AdapterImplementation implements AdapterInterface {
    @Override
    public void onClickListner(Context context, int postion, String status, List<MyOrderDetailsModel.Response> myOrderDetailsModelList) {

        Intent intent = null;
        if(status.equalsIgnoreCase("Cancel"))
        {
            intent=new Intent(context, CancelOrderActivity.class);
        }else if(status.equalsIgnoreCase("Track Order"))
        {
            intent=new Intent(context, MyOrderTrackActivity.class);
        }else if(status.equalsIgnoreCase("Return"))
        {
            intent=new Intent(context, ReturnItemActivity.class);

        }else if(status.equalsIgnoreCase("Exchange"))
        {
            intent=new Intent(context, RequestForExchangeProductActivity.class);



        }
        if(!myOrderDetailsModelList.get(postion).getCoupanCode().equalsIgnoreCase(""))
        {
            intent.putExtra(GlobalVariables.coupan_code,myOrderDetailsModelList.get(postion).getCoupanCode());
        }
        intent.putExtra("from","MyOrderAdapter");
        intent.putExtra("imgURl",myOrderDetailsModelList.get(postion).getImage());
        intent.putExtra("productDesc",myOrderDetailsModelList.get(postion).getProductName());
        intent.putExtra("brand",myOrderDetailsModelList.get(postion).getBrand());
        intent.putExtra("size",myOrderDetailsModelList.get(postion).getSize());
        intent.putExtra("color",myOrderDetailsModelList.get(postion).getColor());
        intent.putExtra("qty",myOrderDetailsModelList.get(postion).getQuantity());
        intent.putExtra("mrp",myOrderDetailsModelList.get(postion).getMrp());
        intent.putExtra("amount",myOrderDetailsModelList.get(postion).getAmount());
        intent.putExtra("discountedPrice",myOrderDetailsModelList.get(postion).getPercentage());
        intent.putExtra("order_id",myOrderDetailsModelList.get(postion).getOrderNumber());
        intent.putExtra("dispatch_by",myOrderDetailsModelList.get(postion).getDispatchBy());
        intent.putExtra("order_date",myOrderDetailsModelList.get(postion).getOrderDate());
        intent.putExtra("payment_type",myOrderDetailsModelList.get(postion).getPaymentType());
        intent.putExtra("buyer",myOrderDetailsModelList.get(postion).getBuyer());
        intent.putExtra("location",myOrderDetailsModelList.get(postion).getLocation());
        intent.putExtra("id",myOrderDetailsModelList.get(postion).getId());
        intent.putExtra("sku",myOrderDetailsModelList.get(postion).getSku());
        intent.putExtra("product_id",myOrderDetailsModelList.get(postion).getProductId());
        intent.putExtra(GlobalVariables.order_status,myOrderDetailsModelList.get(postion).getStatus());
        intent.putExtra("size_label", ""+myOrderDetailsModelList.get(postion).getSize_label());
        if(myOrderDetailsModelList.get(postion).getNotifyStatus()!=null) {
            intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) myOrderDetailsModelList.get(postion).getNotifyStatus());
        }
        context.startActivity(intent);
        Log.e("postion", String.valueOf(postion));

        Log.e("mrp",myOrderDetailsModelList.get(postion).getAmount());

    }
}
