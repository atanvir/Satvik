package com.satvick.ccavenue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.satvick.R;
import com.satvick.activities.LifeDescriptionActivity;
import com.satvick.activities.MyOrderActivity;
import com.satvick.activities.PlaceOrderAddressActivity;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.model.LifeResponseModel;
import com.satvick.model.PlaceOrderModel;
import com.satvick.model.ViewAddressModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.satvick.database.SharedPreferenceKey.gift_wrapup_status;

public class WebViewActivity extends AppCompatActivity {
    Intent mainIntent;
    String encVal;
    String vResponse;
    private List<ViewAddressModel.Viewaddress> viewAddressModelList = new ArrayList<>();
    private String coupan_code;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        mainIntent = getIntent();
        Log.e("id:",mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
        get_RSA_key(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
    }


    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (!ServiceUtility.chkNull(vResponse).equals("") && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer();
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            LoadingDialog.cancelLoading();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    Log.e("response",html);

                    // process the html source code to get final status of transaction
                    String status = null;
                    if (html.indexOf("Failure") != -1) {
                        status = "Transaction Declined!";
                    } else if (html.indexOf("Success") != -1) {
                        status = "Transaction Successful!";
                    } else if (html.indexOf("Aborted") != -1) {
                        status = "Transaction Cancelled!";
                    } else {
                        status = "Status Not Known!";
                    }
                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                    intent.putExtra("transStatus", status);
                    startActivity(intent);
                }
            }

            final WebView webview = findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    Log.e("pageUrl",url);
                    LoadingDialog.cancelLoading();
                    if (url.indexOf("/ccavResponseHandler.jsp") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                    if(url.contains("https://soulahe.com/api/indipay/response")){
                        if(getIntent().getStringExtra("cameFrom").equalsIgnoreCase(PlaceOrderAddressActivity.class.getSimpleName())) {
                            viewAddressModelList=getIntent().getParcelableArrayListExtra("data");
                            coupan_code=getIntent().getStringExtra("coupan_code");
                            orderPlaceApi();
                        }else{
                            placeLifeOrderApi();
                        }
                    }



                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
                }
            });


            try {
                String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), "UTF-8") + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID), "UTF-8") + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ORDER_ID), "UTF-8") + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL), "UTF-8") + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.CANCEL_URL), "UTF-8") + "&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");
                webview.postUrl(Constants.TRANS_URL, postData.getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    private void placeLifeOrderApi() {
        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<LifeResponseModel> call = apiInterface.placeLifeOrder(HelperClass.getCacheData(this).first,HelperClass.getCacheData(this).second,getIntent().getStringExtra("life_id"),getIntent().getStringExtra(AvenuesParams.ORDER_ID)," ");
        call.enqueue(new Callback<LifeResponseModel>() {
            public void onResponse(Call<LifeResponseModel> call, Response<LifeResponseModel> response) {
                myDialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        CommonMessagePopup(response.body().getMessage());
                    }
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        CommonMessagePopup(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LifeResponseModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });

    }


    private void orderPlaceApi() {
        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);
        String gift_wrap_status = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.gift_wrapup_status);
        final MyDialog myDialog = new MyDialog(this);
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
        Call<PlaceOrderModel> call = apiInterface.getPlaceOrderResult(token, userId, gift_wrap_status, location, coupan_code, "Online", getIntent().getStringExtra("order_number"), "", state, city, country, pincode, phone, name, address_type, currency != "Rs." ? currency : "Rs");

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
                    myDialog.hideDialog();
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderModel> call, Throwable t) {
                Log.e("failure", t.getMessage());
                myDialog.hideDialog();
            }
        });
    }


    public void get_RSA_key(final String ac, final String od) {
        LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<RSAResponseModel> call= apiInterface.getRSAPublicKey();
        call.enqueue(new Callback<RSAResponseModel>() {
            @Override
            public void onResponse(Call<RSAResponseModel> call, retrofit2.Response<RSAResponseModel> response) {
                LoadingDialog.cancelLoading();
                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                    vResponse = response.body().getResponse().getRsa();     ///save retrived rsa key
                    if (vResponse.contains("!ERROR!")) {
                        show_alert(vResponse);
                    } else {
                        getIntent().putExtra(AvenuesParams.ORDER_ID,response.body().getResponse().getOrder_id());
                        new RenderView().execute();

                        // Calling async task to get display content
                    }
                } else {
                    show_alert("No response");
                }

            }

            @Override
            public void onFailure(Call<RSAResponseModel> call, Throwable t) {

            }
        });

//        StringRequest stringRequest = new StringRequest(Request.Method.GET, mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //Toast.makeText(WebViewActivity.this,response,Toast.LENGTH_LONG).show();
//                        LoadingDialog.cancelLoading();
//
//                        if (response != null && !response.equals("")) {
//                            vResponse = response;     ///save retrived rsa key
//                            if (vResponse.contains("!ERROR!")) {
//                                show_alert(vResponse);
//                            } else {
//                                new RenderView().execute();   // Calling async task to get display content
//                            }
//                        } else {
//                            show_alert("No response");
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        LoadingDialog.cancelLoading();
//                        //Toast.makeText(WebViewActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put(AvenuesParams.ACCESS_CODE, ac);
//                params.put(AvenuesParams.ORDER_ID, od);
//                return params;
//            }
//
//        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                30000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
    }

    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                WebViewActivity.this).create();

        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");

        alertDialog.setMessage(msg);


        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        alertDialog.show();
    }

    private void CommonMessagePopup(String message) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black);
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
                startActivity(new Intent(WebViewActivity.this, MyOrderActivity.class));
                SharedPreferenceWriter.getInstance(WebViewActivity.this).clearPreferenceValue(gift_wrapup_status, "no");
                SharedPreferenceWriter.getInstance(WebViewActivity.this).writeBooleanValue(GlobalVariables.COUPON_APPLIED, false);

            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(WebViewActivity.this, MyOrderActivity.class));
                SharedPreferenceWriter.getInstance(WebViewActivity.this).clearPreferenceValue(gift_wrapup_status, "no");
                SharedPreferenceWriter.getInstance(WebViewActivity.this).writeBooleanValue(GlobalVariables.COUPON_APPLIED, false);

            }
        });

        dialog.show();


    }
}