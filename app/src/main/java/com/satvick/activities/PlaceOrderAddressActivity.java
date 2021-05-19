package com.satvick.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.satvick.R;
import com.satvick.ccavenue.AvenuesParams;
import com.satvick.ccavenue.WebViewActivity;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityPlaceOrderBinding;
import com.satvick.model.CODAvailableModel;
import com.satvick.model.GenerateOrderIdModel;
import com.satvick.model.PlaceOrderModel;
import com.satvick.model.ViewAddressModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.satvick.database.SharedPreferenceKey.gift_wrapup_status;

public class PlaceOrderAddressActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityPlaceOrderBinding binding;
    String comeFrom = "";
    String totalPrice = "";
    String product_id = "", product_quantity = "", discount = "", coupan_code = "", orderId = "", gift_wrap = "";
    List<ViewAddressModel.Viewaddress> viewAddressModelList = new ArrayList<>();
    ViewAddressModel data;
    private String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_order);
        init();
        symbol = SharedPreferenceWriter.getInstance(this).getString("symbol");
        product_id = getIntent().getStringExtra("product_id");
        product_quantity = getIntent().getStringExtra("product_quantity");
        coupan_code = getIntent().getStringExtra("coupan_code");
        discount = getIntent().getStringExtra("discount");
        gift_wrap = getIntent().getStringExtra("gift_wrap");
        binding.tvDeliveryCharges.setText("+" + getIntent().getStringExtra("shippingCharges"));
        if (discount.equalsIgnoreCase("0")) {
            binding.llCouponApply.setVisibility(View.GONE);
        } else {
            if (!getIntent().getStringExtra("discount").isEmpty()) {
                binding.tvCouponDiscount.setText("-" + Math.round(Double.parseDouble(getIntent().getStringExtra("discount"))));
                binding.llCouponApply.setVisibility(View.VISIBLE);
            }

        }

        if (gift_wrap != null && !gift_wrap.equalsIgnoreCase("")) {
            if (!gift_wrap.equalsIgnoreCase("0"))
                binding.llGiftWrapPrice.setVisibility(View.VISIBLE);
            else binding.llGiftWrapPrice.setVisibility(View.GONE);
        }
        binding.tvGiftWrapPrice.setText("+" + gift_wrap);
        binding.tvTotalMrp.setText(getIntent().getStringExtra("total"));
        binding.tvTotal.setText(symbol + " " + getIntent().getStringExtra("grandTotal"));
        callViewAddressApi();

    }

    private void init() {
        binding.addNewAddressText.setOnClickListener(this);
        binding.tvEditORChangeAddress.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.llContinue.setOnClickListener(this);
        binding.tvAddNewAddress.setOnClickListener(this);
    }

    private void callViewAddressApi() {
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);
        final MyDialog myDialog = new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ViewAddressModel> call = apiInterface.getViewAddressResult(userId);
        call.enqueue(new Callback<ViewAddressModel>() {
            @Override
            public void onResponse(Call<ViewAddressModel> call, Response<ViewAddressModel> response) {

                if (response.isSuccessful()) {
                    binding.submainRl.setVisibility(View.VISIBLE);
                    myDialog.hideDialog();
                    data = response.body();
                    viewAddressModelList = response.body().getViewaddress();

                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        if (response.body().getViewaddress().size() > 0) {
                            //Address found
                            binding.layoutAddress1.setVisibility(View.VISIBLE);
                            binding.layoutAddress2.setVisibility(View.VISIBLE);
                            binding.layoutAddress3.setVisibility(View.VISIBLE);
                            binding.layoutAddress4.setVisibility(View.VISIBLE);
                            binding.tvAddNewAddress.setVisibility(View.GONE);
                            binding.newaddressView.setVisibility(View.GONE);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.lllcc.getLayoutParams();
                            params.addRule(RelativeLayout.BELOW, R.id.layout_address_4);

                            if (data.getViewaddress().get(0).getRemark().equalsIgnoreCase("1") || data.getViewaddress().get(0).getRemark().equalsIgnoreCase("0")) {
                                binding.tvName.setText(data.getViewaddress().get(0).getName());
                                binding.tvAddress.setText(data.getViewaddress().get(0).getAddress());
                                binding.tvContactNumber.setText(data.getViewaddress().get(0).getPhone());
                                binding.addNewAddressText.setVisibility(View.GONE);
                            } else if (data.getViewaddress().get(0).getRemark().equalsIgnoreCase("2")) {
                                binding.tvName.setText(data.getViewaddress().get(0).getName());
                                binding.tvAddress.setText(data.getViewaddress().get(0).getAddress());
                                binding.tvContactNumber.setText(data.getViewaddress().get(0).getPhone());
                                binding.addNewAddressText.setVisibility(View.VISIBLE);
                            } else {
                                binding.tvName.setVisibility(View.GONE);
                                binding.tvAddress.setVisibility(View.GONE);
                                binding.tvContactNumber.setVisibility(View.GONE);
                            }
                        } else {
                            //Address not found
                            binding.layoutAddress1.setVisibility(View.GONE);
                            binding.layoutAddress2.setVisibility(View.GONE);
                            binding.layoutAddress3.setVisibility(View.GONE);
                            binding.layoutAddress4.setVisibility(View.GONE);
                            binding.tvAddNewAddress.setVisibility(View.VISIBLE);
                            binding.newaddressView.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.lllcc.getLayoutParams();
                            params.addRule(RelativeLayout.BELOW, R.id.tvAddNewAddress);

                            // Toast.makeText(PlaceOrderAddressActivity.this, "Address is not found", Toast.LENGTH_SHORT).show();

                        }
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(PlaceOrderAddressActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ViewAddressModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void orderPlaceApi(String comeFrom, String orderNumber) {

        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);
        String gift_wrap_status = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.gift_wrapup_status);
        final MyDialog myDialog = new MyDialog(PlaceOrderAddressActivity.this);
        myDialog.showDialog();


        if (gift_wrap_status.equalsIgnoreCase("")) {

            gift_wrap_status = "no";
        }


        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        String location = "";
        String state = "";
        String city = "";
        String country = "";
        String pincode = "";
        String phone = "";
        String name = "";
        String address_type = "";

        for (int i = 0; i < viewAddressModelList.size(); i++) {
            if (viewAddressModelList.size() > 1) {
                if (viewAddressModelList.get(i).getRemark().equalsIgnoreCase("1")) {
                    location += viewAddressModelList.get(i).getAddress() + "," + viewAddressModelList.get(i).getTown() + "," + viewAddressModelList.get(i).getCity() + "," + viewAddressModelList.get(i).getState() + "," + viewAddressModelList.get(i).getPincode() + "," + viewAddressModelList.get(i).getCountry();
                    state += viewAddressModelList.get(i).getState();
                    city += viewAddressModelList.get(i).getCity();
                    country += viewAddressModelList.get(i).getCountry();
                    pincode += viewAddressModelList.get(i).getPincode();
                    phone += viewAddressModelList.get(i).getPhone();
                    name += viewAddressModelList.get(i).getName();
                    address_type = viewAddressModelList.get(i).getType();
                }

            } else {
                if (viewAddressModelList.get(i).getRemark().equalsIgnoreCase("1")) {
                    location += viewAddressModelList.get(i).getAddress() + "," + viewAddressModelList.get(i).getTown() + "," + viewAddressModelList.get(i).getCity() + "," + viewAddressModelList.get(i).getState() + "," + viewAddressModelList.get(i).getPincode() + "," + viewAddressModelList.get(i).getCountry();
                    state += viewAddressModelList.get(i).getState();
                    city += viewAddressModelList.get(i).getCity();
                    country += viewAddressModelList.get(i).getCountry();
                    pincode += viewAddressModelList.get(i).getPincode();
                    phone += viewAddressModelList.get(i).getPhone();
                    name += viewAddressModelList.get(i).getName();
                    address_type = viewAddressModelList.get(i).getType();

                } else {
                    location += viewAddressModelList.get(i).getAddress() + "," + viewAddressModelList.get(i).getTown() + "," + viewAddressModelList.get(i).getCity() + "," + viewAddressModelList.get(i).getState() + "," + viewAddressModelList.get(i).getPincode() + "," + viewAddressModelList.get(i).getCountry();
                    state += viewAddressModelList.get(i).getState();
                    city += viewAddressModelList.get(i).getCity();
                    country += viewAddressModelList.get(i).getCountry();
                    pincode += viewAddressModelList.get(i).getPincode();
                    phone += viewAddressModelList.get(i).getPhone();
                    name += viewAddressModelList.get(i).getName();
                    address_type = viewAddressModelList.get(i).getType();
                }
            }

        }

        Log.e("location", location);
        if (coupan_code == null) {
            coupan_code = "";
        }

        String currency = SharedPreferenceWriter.getInstance(this).getString("currency");
        Call<PlaceOrderModel> call = apiInterface.getPlaceOrderResult(token, userId, gift_wrap_status, location, coupan_code, comeFrom, orderNumber, "", state, city, country, pincode, phone, name, address_type, currency != "Rs." ? currency : "Rs");

        call.enqueue(new Callback<PlaceOrderModel>() {
            @Override
            public void onResponse(Call<PlaceOrderModel> call, Response<PlaceOrderModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();

                    PlaceOrderModel data = response.body();

                    if (data.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        CommonMessagePopup(response.body().getMessage());

                    } else if (data.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {

                        CommonMessagePopup(response.body().getMessage());
                    }

                } else {
                    Log.d("OnFAil:", response.body().toString());
                    myDialog.hideDialog();
                    Toast.makeText(PlaceOrderAddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderModel> call, Throwable t) {
                Log.e("failure", t.getMessage());
                myDialog.hideDialog();
            }
        });
    }

    private void CommonMessagePopup(String message) {
        final Dialog dialog = new Dialog(PlaceOrderAddressActivity.this, android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_mesage_popup);
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottieAnimationView);
        ImageView closeiv = dialog.findViewById(R.id.closeiv);
        TextView messagetxt = dialog.findViewById(R.id.messagetxt);

        if (message.equalsIgnoreCase("Your order completed successfully")) {
            message = "Your order placed successfully";
            messagetxt.setText(message);
            lottieAnimationView.setAnimation("done.json");

        } else {
            messagetxt.setText(message);
            lottieAnimationView.setAnimation("error.json");

        }

        Button okbtn = dialog.findViewById(R.id.okbtn);

        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(PlaceOrderAddressActivity.this, MyOrderActivity.class));
                SharedPreferenceWriter.getInstance(PlaceOrderAddressActivity.this).clearPreferenceValue(gift_wrapup_status, "no");
                SharedPreferenceWriter.getInstance(PlaceOrderAddressActivity.this).writeBooleanValue(GlobalVariables.COUPON_APPLIED, false);

            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(PlaceOrderAddressActivity.this, MyOrderActivity.class));
                SharedPreferenceWriter.getInstance(PlaceOrderAddressActivity.this).clearPreferenceValue(gift_wrapup_status, "no");
                SharedPreferenceWriter.getInstance(PlaceOrderAddressActivity.this).writeBooleanValue(GlobalVariables.COUPON_APPLIED, false);

            }
        });

        dialog.show();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //Address found
            case R.id.addNewAddressText:
                callingIntent("PlaceOrderAddressActivity", getIntent().getStringExtra("product_id"), getIntent().getStringExtra("product_quantity"));
                break;

            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.tvEditORChangeAddress:
                if (data.getViewaddress().get(0).getRemark().equalsIgnoreCase("1")) {
                    Intent intent2 = new Intent(this, EditAddressActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("from", "PlaceOrderAddressActivity");
                    bundle2.putString("name", data.getViewaddress().get(0).getName());
                    bundle2.putString("mobile_number", data.getViewaddress().get(0).getPhone());
                    bundle2.putString("pin_code", data.getViewaddress().get(0).getPincode());
                    bundle2.putString("state", data.getViewaddress().get(0).getState());
                    bundle2.putString("address", data.getViewaddress().get(0).getAddress());
                    bundle2.putString("country", data.getViewaddress().get(0).getCountry());
                    bundle2.putString("locality_or_town", data.getViewaddress().get(0).getTown());
                    bundle2.putString("city_or_district", data.getViewaddress().get(0).getCity());
                    bundle2.putString("address_id", String.valueOf(data.getViewaddress().get(0).getId()));
                    bundle2.putString("remark", String.valueOf(data.getViewaddress().get(0).getRemark()));
                    bundle2.putString("latitude", String.valueOf(data.getViewaddress().get(0).getLatitude()));
                    bundle2.putString("longitude", String.valueOf(data.getViewaddress().get(0).getLongitude()));
                    bundle2.putString("product_id", getIntent().getStringExtra("product_id"));
                    bundle2.putString("product_quantity", getIntent().getStringExtra("product_quantity"));
                    bundle2.putString("total", getIntent().getStringExtra("total"));
                    bundle2.putString("grandTotal", getIntent().getStringExtra("grandTotal"));
                    bundle2.putString("shippingCharges", getIntent().getStringExtra("shippingCharges"));
                    bundle2.putString("product_id", getIntent().getStringExtra("product_id"));
                    bundle2.putString("product_quantity", getIntent().getStringExtra("product_quantity"));
                    bundle2.putString("gift_wrap", getIntent().getStringExtra("gift_wrap"));
                    bundle2.putString("discount", getIntent().getStringExtra("discount"));
                    bundle2.putString("coupan_code", getIntent().getStringExtra("coupan_code"));
                    intent2.putExtras(bundle2);
                    startActivity(intent2);
                } else if (data.getViewaddress().get(0).getRemark().equalsIgnoreCase("0")) {

                    Intent intent2 = new Intent(this, EditAddressActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("from", "PlaceOrderAddressActivity");
                    bundle2.putString("name", data.getViewaddress().get(0).getName());
                    bundle2.putString("mobile_number", data.getViewaddress().get(0).getPhone());
                    bundle2.putString("pin_code", data.getViewaddress().get(0).getPincode());
                    bundle2.putString("state", data.getViewaddress().get(0).getState());
                    bundle2.putString("address", data.getViewaddress().get(0).getAddress());
                    bundle2.putString("country", data.getViewaddress().get(0).getCountry());
                    bundle2.putString("locality_or_town", data.getViewaddress().get(0).getTown());
                    bundle2.putString("city_or_district", data.getViewaddress().get(0).getCity());
                    bundle2.putString("address_id", String.valueOf(data.getViewaddress().get(0).getId()));
                    bundle2.putString("remark", String.valueOf(data.getViewaddress().get(0).getRemark()));
                    bundle2.putString("latitude", String.valueOf(data.getViewaddress().get(0).getLatitude()));
                    bundle2.putString("longitude", String.valueOf(data.getViewaddress().get(0).getLongitude()));
                    bundle2.putString("product_id", getIntent().getStringExtra("product_id"));
                    bundle2.putString("product_quantity", getIntent().getStringExtra("product_quantity"));
                    bundle2.putString("total", getIntent().getStringExtra("total"));
                    bundle2.putString("grandTotal", getIntent().getStringExtra("grandTotal"));
                    bundle2.putString("shippingCharges", getIntent().getStringExtra("shippingCharges"));
                    bundle2.putString("product_id", getIntent().getStringExtra("product_id"));
                    bundle2.putString("product_quantity", getIntent().getStringExtra("product_quantity"));
                    bundle2.putString("gift_wrap", getIntent().getStringExtra("gift_wrap"));
                    bundle2.putString("discount", getIntent().getStringExtra("discount"));
                    bundle2.putString("coupan_code", getIntent().getStringExtra("coupan_code"));
                    intent2.putExtras(bundle2);
                    startActivity(intent2);
                }

                break;

            case R.id.llContinue:

                if (binding.tvAddNewAddress.getVisibility() == View.GONE) {
                    checkCODOptionApi();
                } else {
                    CommonUtil.setUpSnackbarMessage(binding.mainRl, "Please add Atleast One Address First", PlaceOrderAddressActivity.this);
                }
                break;

            //Address not found
            case R.id.tvAddNewAddress:
                Intent intent = new Intent(this, AddNewAddressClickActivity.class);
                intent.putExtra("from", GlobalVariables.PlaceOrderAddressActivity);
                intent.putExtra("total", getIntent().getStringExtra("total"));
                intent.putExtra("grandTotal", getIntent().getStringExtra("grandTotal"));
                intent.putExtra("shippingCharges", getIntent().getStringExtra("shippingCharges"));
                intent.putExtra("product_id", product_id);
                intent.putExtra("product_quantity", product_quantity);
                intent.putExtra("gift_wrap", getIntent().getStringExtra("gift_wrap"));
                intent.putExtra("discount", getIntent().getStringExtra("discount"));
                intent.putExtra("coupan_code", getIntent().getStringExtra("coupan_code"));
                startActivity(intent);
                break;
        }
    }

    private void callingIntent(String from, String product_id, String product_quantity) {
        Intent intent = new Intent(this, AddNewAddressClickActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("product_id", product_id);
        intent.putExtra("product_quantity", product_quantity);
        intent.putExtra("total", getIntent().getStringExtra("total"));
        intent.putExtra("grandTotal", getIntent().getStringExtra("grandTotal"));
        intent.putExtra("shippingCharges", getIntent().getStringExtra("shippingCharges"));
        intent.putExtra("product_id", product_id);
        intent.putExtra("product_quantity", product_quantity);
        intent.putExtra("gift_wrap", getIntent().getStringExtra("gift_wrap"));
        intent.putExtra("discount", getIntent().getStringExtra("discount"));
        intent.putExtra("coupan_code", getIntent().getStringExtra("coupan_code"));
        startActivity(intent);

    }

    private void checkCODOptionApi() {
        final MyDialog myDialog = new MyDialog(this);
        myDialog.showDialog();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.codAvailable(SharedPreferenceWriter.getInstance(this).getString("user_id")).enqueue(new Callback<CODAvailableModel>() {
            public void onResponse(Call<CODAvailableModel> call, Response<CODAvailableModel> response) {
                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                        openSelectPaymentPopup(response.body().getCodavailable().getCodAvailable());
                    } else {
                        CommonUtil.setUpSnackbarMessage(PlaceOrderAddressActivity.this.binding.getRoot(), response.body().getMessage(), PlaceOrderAddressActivity.this);
                    }
                } else {
                    myDialog.hideDialog();
                }
            }

            public void onFailure(Call<CODAvailableModel> param1Call, Throwable param1Throwable) {
                myDialog.hideDialog();
            }

        });
    }


    private void openSelectPaymentPopup(String codeOption) {
        final BottomSheetDialog bottomSheetDialogg = new BottomSheetDialog(this);
        View vieww = this.getLayoutInflater().inflate(R.layout.pop_up_select_mode_of_payment, null);
        bottomSheetDialogg.setContentView(vieww);
        bottomSheetDialogg.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        bottomSheetDialogg.dismiss();

        RadioGroup radioGroup = vieww.findViewById(R.id.radioGroup);
        final RadioButton rdbOnline = vieww.findViewById(R.id.rdbOnline);
        final RadioButton rdbCashOnDelivery = vieww.findViewById(R.id.rdbCashOnDelivery);
        Button btnCancel = vieww.findViewById(R.id.btnCancel);
        Button btnContinue = vieww.findViewById(R.id.btnContinue);
        rdbOnline.setText(Html.fromHtml("<font color=#303030>Online </font> <font color=#D81B60> <br></br>Free Shipping</font>"));
        rdbCashOnDelivery.setText(Html.fromHtml("<font color=#303030>Cash Delivery </font> <font color=#D81B60> <br></br>" + symbol + " " + getIntent().getStringExtra("shippingCharges") + " /- Extra Shipping</font>"));
        rdbCashOnDelivery.setVisibility(View.GONE);

        if (codeOption.equalsIgnoreCase("false")) {
            rdbCashOnDelivery.setEnabled(false);
            rdbCashOnDelivery.setVisibility(View.GONE);
            rdbCashOnDelivery.setTextColor(ContextCompat.getColor(this, R.color.grey));
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int c_id = radioGroup.getCheckedRadioButtonId();
                Log.v("radioGroup", "c_id: " + c_id);

                int online_id = rdbOnline.getId();
                int cashOnDelivery_id = rdbCashOnDelivery.getId();

                if (c_id == online_id) {
                    Log.w("radioGroup", "online_id: " + online_id);
                    rdbOnline.setButtonDrawable(ContextCompat.getDrawable(PlaceOrderAddressActivity.this, R.drawable.radio_button_salected));
                    comeFrom = "Online";
                    binding.llDeliveryCharges.setVisibility(View.GONE);
                    binding.tvTotal.setText(symbol + " " + Math.round(Double.parseDouble(PlaceOrderAddressActivity.this.getIntent().getStringExtra("grandTotal"))));
                } else {
                    rdbOnline.setButtonDrawable(ContextCompat.getDrawable(PlaceOrderAddressActivity.this, R.drawable.radio_button_un_salected_));
                }

                if (c_id == cashOnDelivery_id) {
                    Log.w("radioGroup", "cashOnDelivery_id: " + cashOnDelivery_id);
                    rdbCashOnDelivery.setButtonDrawable(ContextCompat.getDrawable(PlaceOrderAddressActivity.this, R.drawable.radio_button_salected));
                    comeFrom = "Cash On Delivery";
                    binding.llDeliveryCharges.setVisibility(View.VISIBLE);
                    binding.tvTotal.setText(symbol + " " + Math.round(Double.parseDouble(PlaceOrderAddressActivity.this.getIntent().getStringExtra("grandTotal")) + Double.parseDouble(PlaceOrderAddressActivity.this.getIntent().getStringExtra("shippingCharges"))));

                } else {
                    rdbCashOnDelivery.setButtonDrawable(ContextCompat.getDrawable(PlaceOrderAddressActivity.this, R.drawable.radio_button_un_salected_));
                }

            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!comeFrom.equals("")) {
                    if (comeFrom.equalsIgnoreCase("Online")) {
                        bottomSheetDialogg.dismiss();
                        generateOrderIdApi(comeFrom);

                    } else {
                        bottomSheetDialogg.dismiss();
                        generateOrderIdApi(comeFrom);
                    }
                } else {
                    Toast.makeText(PlaceOrderAddressActivity.this, "Please select payment mode", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogg.dismiss();
            }
        });

        bottomSheetDialogg.show();

    }

    private synchronized String generateOrderIdApi(final String comeFrom) {
        final String[] orderNumber = new String[1];
        final MyDialog myDialog = new MyDialog(PlaceOrderAddressActivity.this);
        myDialog.showDialog();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


        Call<GenerateOrderIdModel> call = apiInterface.getOrderId();
        Log.d("OnOrdrPlacedHit", call.request().toString());
        call.enqueue(new Callback<GenerateOrderIdModel>() {
            @Override
            public void onResponse(Call<GenerateOrderIdModel> call, Response<GenerateOrderIdModel> response) {
                Log.d("OnOrdrPlacedRes", response.body().toString());

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().status.equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        SharedPreferenceWriter.getInstance(PlaceOrderAddressActivity.this).writeBooleanValue(GlobalVariables.COUPON_APPLIED, false);
                        orderNumber[0] = response.body().generateordercode.orderNumber;

                        if (comeFrom.equalsIgnoreCase("Cash On Delivery")) {
                            orderPlaceApi(comeFrom, orderNumber[0]);

                        } else if (comeFrom.equalsIgnoreCase("Online")) {
                            Log.e("orderNumber", orderNumber[0]);
                            orderPlaceApi(comeFrom, orderNumber[0]);  /*edited on 17 may*/

                            Intent intent = new Intent(PlaceOrderAddressActivity.this, WebViewActivity.class);
                            intent.putExtra(AvenuesParams.ACCESS_CODE, getString(R.string.access_code_key));
                            intent.putExtra(AvenuesParams.MERCHANT_ID, getString(R.string.merchant_id_key));
                            intent.putExtra(AvenuesParams.ORDER_ID, orderNumber[0]);
                            intent.putExtra(AvenuesParams.CURRENCY, "INR");
                            intent.putExtra(AvenuesParams.AMOUNT, "1");
                            intent.putExtra(AvenuesParams.REDIRECT_URL, getString(R.string.redirect_url_key));
                            intent.putExtra(AvenuesParams.CANCEL_URL, getString(R.string.cancel_url_key));
                            intent.putExtra(AvenuesParams.RSA_KEY_URL, getString(R.string.rsa_key_url));
                            // startActivity(intent);  /*edited on 17 may*/

                        }

                    } else if (response.body().status.equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(PlaceOrderAddressActivity.this, response.body().message, Toast.LENGTH_SHORT).show();

                    }

                } else {
                    myDialog.hideDialog();
                }

            }

            @Override
            public void onFailure(Call<GenerateOrderIdModel> call, Throwable t) {

            }
        });

        return orderNumber[0];
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class).putExtra("from", "addressMain"));
    }
}
