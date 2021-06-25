package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.satvick.R;
import com.satvick.adapters.AddressAdapter;
import com.satvick.ccavenue.AvenuesParams;
import com.satvick.ccavenue.WebViewActivity;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityOrderConfirmationBinding;
import com.satvick.model.GenerateOrderIdModel;
import com.satvick.model.PaymentResponseModel;
import com.satvick.model.ShippingChargesModel;
import com.satvick.model.ViewAddressModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.BillingHelper;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderConfirmationActivity extends AppCompatActivity implements View.OnClickListener, AddressAdapter.setOnAddressClick {

    private ActivityOrderConfirmationBinding binding;
    private ApiInterface apiInterface;
    private ViewAddressModel.Viewaddress viewAddress;
    private final int NEW_ADDRESS_REQUEST=12,
                      UPDATE_ADDRESS_REQUEST=21;
    private List<ViewAddressModel.Viewaddress> addressList;
    private boolean isValidPinCode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        addressApi();
    }

    private void init() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        binding.toolbar.tvTitle.setText("Address");
        binding.tvTotalMrp.setText(BillingHelper.getInstance().getBillingData().TOTAL);
        binding.tvCouponDiscount.setText("-" + Math.round(Double.parseDouble(BillingHelper.getInstance().getBillingData().DISCOUNT!=null?BillingHelper.getInstance().getBillingData().DISCOUNT:"0")));
        binding.tvDeliveryCharges.setText("+" + BillingHelper.getInstance().getBillingData().SHIPPING_CHARGES);
        binding.tvGiftWrapPrice.setText("+" + BillingHelper.getInstance().getBillingData().GIFT_WRAP);
        binding.tvTotal.setText("₹" + " " + BillingHelper.getInstance().getBillingData().SUB_TOTAL);

        if (!BillingHelper.getInstance().getBillingData().DISCOUNT.isEmpty() &&
            !BillingHelper.getInstance().getBillingData().DISCOUNT.equalsIgnoreCase("0")) binding.llCouponApply.setVisibility(View.VISIBLE);
        if (!BillingHelper.getInstance().getBillingData().GIFT_WRAP.isEmpty() &&
            !BillingHelper.getInstance().getBillingData().GIFT_WRAP.equalsIgnoreCase("0")) binding.llGiftWrapPrice.setVisibility(View.VISIBLE);
    }

    private void initCtrl(){
        binding.toolbar.ivBack.setOnClickListener(this);
        binding.llContinue.setOnClickListener(this);
        binding.tvAddNewAddress.setOnClickListener(this);
    }


    private void addressApi() {
        Call<ViewAddressModel> call = apiInterface.getViewAddressResult(HelperClass.getCacheData(this).second);
        call.enqueue(new Callback<ViewAddressModel>() {
            @Override
            public void onResponse(Call<ViewAddressModel> call, Response<ViewAddressModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) loadAddressUI(response.body().getViewaddress());
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),OrderConfirmationActivity.this);
                }else{
                    CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",OrderConfirmationActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ViewAddressModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),OrderConfirmationActivity.this);
            }
        });
    }

    private void loadAddressUI(List<ViewAddressModel.Viewaddress> data) {
        this.addressList=data;
        for(int i=0;i<addressList.size();i++){ addressList.get(i).setRemark("1"); }
        if(addressList.isEmpty()) { binding.tvAddNewAddress.setVisibility(View.VISIBLE); binding.viewAddress.setVisibility(View.VISIBLE); }
        else binding.tvAddNewAddress.setVisibility(View.GONE); binding.viewAddress.setVisibility(View.GONE);
        binding.rvAddress.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAddress.setAdapter(new AddressAdapter(this,addressList,this,false));
    }

    private void shippingChargesApi(String addressId) {
        String giftWrap=SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.gift_wrapup_status);
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ShippingChargesModel> call = apiInterface.getShippingCharges(HelperClass.getCacheData(this).second,addressId,giftWrap.equalsIgnoreCase("Yes")?"1":"0");
        call.enqueue(new Callback<ShippingChargesModel>() {
            @Override
            public void onResponse(Call<ShippingChargesModel> call, Response<ShippingChargesModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) loadBilling(response.body().getGetshippingcharges().getPayment());
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(),OrderConfirmationActivity.this);
                } else { isValidPinCode=false; CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Sorry we do not serve this area! Please change your delivery address",OrderConfirmationActivity.this); }
            }

            @Override
            public void onFailure(Call<ShippingChargesModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),OrderConfirmationActivity.this);
            }
        });
    }

    private void loadBilling(ShippingChargesModel.ChargesModel.PaymentModel model) {
        isValidPinCode=true;
        binding.llDeliveryCharges.setVisibility(View.VISIBLE);
        BillingHelper.getInstance().getBillingData().setSHIPPING_CHARGES(model.getShipping());
        BillingHelper.getInstance().getBillingData().setSUB_TOTAL(""+Math.round(Double.parseDouble(model.getPayableWithShipAndGift())));

        if(Double.parseDouble(model.getPayable())>1000) {
            String subTotal= String.valueOf(Double.parseDouble(BillingHelper.getInstance().getBillingData().SUB_TOTAL) - Double.parseDouble(BillingHelper.getInstance().getBillingData().SHIPPING_CHARGES));
            binding.tvDeliveryCharges.setText("-"+BillingHelper.getInstance().getBillingData().SHIPPING_CHARGES);
            binding.tvTotal.setText("₹" + " " + subTotal);
            BillingHelper.getInstance().getBillingData().setSUB_TOTAL(subTotal);

        }else {
            binding.tvDeliveryCharges.setText("+"+BillingHelper.getInstance().getBillingData().SHIPPING_CHARGES);
            binding.tvTotal.setText("₹" + " " + BillingHelper.getInstance().getBillingData().SUB_TOTAL);
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvAddNewAddress:
            Intent intent = new Intent(this, AddressActivity.class);
            startActivityForResult(intent, NEW_ADDRESS_REQUEST);
            break;

            case R.id.ivBack: onBackPressed(); break;

            case R.id.llContinue:
            if(viewAddress==null) CommonUtil.setUpSnackbarMessage(binding.getRoot(), "Please add at least one address first", OrderConfirmationActivity.this);
            else if(!isValidPinCode) CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Sorry we do not serve this area! Please change your delivery address",this);
            else generateOrderIdApi();
            break;
        }
    }


    private void generateOrderIdApi() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<GenerateOrderIdModel> call = apiInterface.getOrderId();
        call.enqueue(new Callback<GenerateOrderIdModel>() {
            @Override
            public void onResponse(Call<GenerateOrderIdModel> call, Response<GenerateOrderIdModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().status.equalsIgnoreCase(GlobalVariables.SUCCESS)) CCAvenueLaucher(response.body().generateordercode.orderNumber);
                    else if (response.body().status.equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().message,OrderConfirmationActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",OrderConfirmationActivity.this);
            }

            @Override
            public void onFailure(Call<GenerateOrderIdModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),OrderConfirmationActivity.this);
            }
        });
    }

    private void CCAvenueLaucher(String orderId) {
        Intent intent = new Intent(OrderConfirmationActivity.this, WebViewActivity.class);
        intent.putExtra("cameFrom", OrderConfirmationActivity.class.getSimpleName());
        intent.putExtra(AvenuesParams.ACCESS_CODE, getString(R.string.access_code_key));
        intent.putExtra("data",viewAddress);
        intent.putExtra(AvenuesParams.MERCHANT_ID, getString(R.string.merchant_id_key));
        intent.putExtra(AvenuesParams.ORDER_ID, orderId);
        intent.putExtra("order_number",orderId);
        intent.putExtra(AvenuesParams.CURRENCY, "INR");
        intent.putExtra(AvenuesParams.AMOUNT, BillingHelper.getInstance().getBillingData().SUB_TOTAL);
        intent.putExtra(AvenuesParams.REDIRECT_URL, getString(R.string.redirect_url_key));
        intent.putExtra(AvenuesParams.CANCEL_URL, getString(R.string.cancel_url_key));
        intent.putExtra(AvenuesParams.RSA_KEY_URL, getString(R.string.rsa_key_url));
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onAddressClick(int pos) {
        for(int i=0;i<addressList.size();i++){
            addressList.get(i).setRemark("1");
        }
        addressList.get(pos).setRemark("0");
        this.viewAddress=addressList.get(pos);
        binding.rvAddress.getAdapter().notifyDataSetChanged();
        shippingChargesApi(""+viewAddress.getId());

    }

    @Override
    public void onAddressRemove(int pos) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case NEW_ADDRESS_REQUEST: addressApi(); break;
                case UPDATE_ADDRESS_REQUEST: addressApi(); break;
            }
        }
    }
}
