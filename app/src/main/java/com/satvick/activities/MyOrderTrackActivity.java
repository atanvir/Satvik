package com.satvick.activities;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;

import com.squareup.picasso.Picasso;
import com.satvick.R;
import com.satvick.adapters.TrackOrderAdapter;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ItemMyOrdersMainBinding;
import com.satvick.model.MyOrderDetailsModel;
import com.satvick.utils.GlobalVariables;

import java.util.List;

public class MyOrderTrackActivity extends AppCompatActivity implements View.OnClickListener {

    ItemMyOrdersMainBinding binding;

    String reason="";

    String  mBrand="",mImageUrl="",mProductDesc="",mSize="",mColor="",mQty="",mOrderStatus="",
            mMrp="",mAmount="",mDiscountedPrice="",mOrderId="",mOrderNumber="",mDispatchBy="",
            mOrderDate="",mPaymentType="",mBuyer="",mLocation="",mProductId="",mCouponCode="";

    String mAdditionalComment="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.item_my_orders_main);
        binding.ivBack.setOnClickListener(this);

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
            mOrderStatus=getIntent().getStringExtra(GlobalVariables.order_status);
        }

        if(!mImageUrl.isEmpty()){
            Picasso.with(this).load(mImageUrl).into(binding.ivProduct);
        }
        binding.tvSize.setText(getIntent().getStringExtra("size_label")+":");

        binding.tvProductName.setText(mBrand);
        binding.tvProductDesc.setText(mProductDesc);
        binding.tvSizeContent.setText(mSize);
        binding.tvQuantityContent.setText(mQty);
        binding.tvAddress.setText(mLocation);
        if(!mDiscountedPrice.equalsIgnoreCase("0"))
        {
            binding.tvOff.setText(mDiscountedPrice+"% OFF");
        }
        else
        {
            binding.llRupeee.setVisibility(View.GONE);
            binding.tvOff.setVisibility(View.GONE);
        }
        binding.tvOrderStatus.setText(mOrderStatus);
        if(mOrderStatus.equalsIgnoreCase(GlobalVariables.new_order))
        {
            binding.tvOutForDelivery.setText("Out for delivery");
        }else if(mOrderStatus.equalsIgnoreCase(GlobalVariables.dispatched))
        {
            binding.tvOutForDelivery.setText("Item Dispatch");
        }else if(mOrderStatus.equalsIgnoreCase(GlobalVariables.delivred))
        {
            binding.tvOutForDelivery.setText("Order placed");
        }
        String symbol = SharedPreferenceWriter.getInstance(this).getString("symbol");
        Double converted_amount = Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString("converted_amount"));



//       double totalAmount = Double.parseDouble(mAmount)*Double.parseDouble(mQty);
//       double totalMrp = Double.parseDouble(mMrp)*Double.parseDouble(mQty);


        binding.tvPrice.setText(symbol+" "+Math.round(Double.parseDouble(mAmount)*converted_amount));
        binding.tvCuttedText.setText(symbol+" "+Math.round(Double.parseDouble(mMrp)*converted_amount));
        binding.tvCuttedText.setPaintFlags(binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if(mCouponCode!=null)
        {
            binding.tvBlumentor.setText(mCouponCode);
        }
        else
        {
            binding.llSold.setVisibility(View.GONE);
        }


        List<MyOrderDetailsModel.Notifystatus> allData=getIntent().getParcelableArrayListExtra("list");
        binding.orderDateTxt.setText(mOrderDate.split("/")[0]);
        binding.deliveryDateText.setText(mDispatchBy.split("/")[0]);
        if(allData!=null && allData.size()>0) {
            binding.tvOutForDelivery.setText(allData.get(allData.size() - 1).getContent());
            binding.rvTrackOrder.setLayoutManager(new LinearLayoutManager(this));
            binding.rvTrackOrder.setAdapter(new TrackOrderAdapter(this, getIntent().<MyOrderDetailsModel.Notifystatus>getParcelableArrayListExtra("list")));
        }
        else
        {
            binding.tvOutForDelivery.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MyOrderActivity.class).putExtra("from","CancelOrderActivity"));
    }
}
