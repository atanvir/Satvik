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

import com.airbnb.lottie.LottieAnimationView;
import com.satvick.R;
import com.satvick.activities.MyOrderActivity;
import com.satvick.activities.OrderConfirmationActivity;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityWebviewBinding;
import com.satvick.model.LifeResponseModel;
import com.satvick.model.PlaceOrderModel;
import com.satvick.model.ViewAddressModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.BillingHelper;
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
    private String encVal;
    private String vResponse;

    private ActivityWebviewBinding binding;
    private MyDialog dialog;
    private ApiInterface apiInterface;
    private ViewAddressModel.Viewaddress viewAddress;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding=ActivityWebviewBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_webview);
        init();
    }

    public void init(){
        dialog = new MyDialog(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        viewAddress=getIntent().getParcelableExtra("data");
        getRSAkey();
    }

    public void getRSAkey() {
        dialog.showDialog();
        Call<RSAResponseModel> call= apiInterface.getRSAPublicKey();
        call.enqueue(new Callback<RSAResponseModel>() {
            @Override
            public void onResponse(Call<RSAResponseModel> call, retrofit2.Response<RSAResponseModel> response) {
                dialog.hideDialog();
                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                    vResponse = response.body().getResponse().getRsa();     ///save retrived rsa key
                    if (vResponse.contains("!ERROR!")) {
                        CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",WebViewActivity.this);
                    } else {
                        getIntent().putExtra(AvenuesParams.ORDER_ID,response.body().getResponse().getOrder_id());
                        new RenderView().execute();
                    }
                } else {
                    CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",WebViewActivity.this);
                }

            }

            @Override
            public void onFailure(Call<RSAResponseModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),WebViewActivity.this);
            }
        });

    }

    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.showDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (!ServiceUtility.chkNull(vResponse).equals("") && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer();
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, getIntent().getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, getIntent().getStringExtra(AvenuesParams.CURRENCY)));
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.hideDialog();
            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    Log.e("response", html);

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
                    Log.e("pageUrl", url);
                    dialog.hideDialog();
                    if (url.indexOf("/ccavResponseHandler.jsp") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                    if (url.contains("https://soulahe.com/api/indipay/response")) {
                        if (getIntent().getStringExtra("cameFrom").equalsIgnoreCase(OrderConfirmationActivity.class.getSimpleName())) orderPlaceApi();
                        else placeLifeOrderApi();
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    dialog.showDialog();
                }
            });


            try {
                String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(getIntent().getStringExtra(AvenuesParams.ACCESS_CODE), "UTF-8") + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(getIntent().getStringExtra(AvenuesParams.MERCHANT_ID), "UTF-8") + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(getIntent().getStringExtra(AvenuesParams.ORDER_ID), "UTF-8") + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(getIntent().getStringExtra(AvenuesParams.REDIRECT_URL), "UTF-8") + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(getIntent().getStringExtra(AvenuesParams.CANCEL_URL), "UTF-8") + "&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");
                webview.postUrl(Constants.TRANS_URL, postData.getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    private void placeLifeOrderApi() {
        dialog.showDialog();
        Call<LifeResponseModel> call = apiInterface.placeLifeOrder(HelperClass.getCacheData(this).first,HelperClass.getCacheData(this).second,getIntent().getStringExtra("life_id"),getIntent().getStringExtra(AvenuesParams.ORDER_ID)," ");
        call.enqueue(new Callback<LifeResponseModel>() {
            public void onResponse(Call<LifeResponseModel> call, Response<LifeResponseModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) HelperClass.orderStatusDailog(WebViewActivity.this,response.body().getMessage());
                else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",WebViewActivity.this);
            }

            @Override
            public void onFailure(Call<LifeResponseModel> call, Throwable t) {
                dialog.hideDialog();
            }
        });

    }

    private void orderPlaceApi() {
        dialog.showDialog();
        Call<PlaceOrderModel> call = apiInterface.getPlaceOrderResult(HelperClass.getCacheData(this).first,
                                                                      HelperClass.getCacheData(this).second,
                                                                      SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.gift_wrapup_status),
                                                                      viewAddress.getAddress(), BillingHelper.getInstance().getBillingData().COUPON_CODE,
                                                                      "Online", getIntent().getStringExtra("order_number"), "",
                                                                      viewAddress.getState(), viewAddress.getCity(), viewAddress.getCountry(),
                                                                      viewAddress.getPincode(), viewAddress.getPhone(),viewAddress.getName(),
                                                                      viewAddress.getType(),"Rs");

        call.enqueue(new Callback<PlaceOrderModel>() {
            @Override
            public void onResponse(Call<PlaceOrderModel> call, Response<PlaceOrderModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    SharedPreferenceWriter.getInstance(WebViewActivity.this).clearPreferenceValue(SharedPreferenceKey.gift_wrapup_status, "no");
                    SharedPreferenceWriter.getInstance(WebViewActivity.this).writeBooleanValue(GlobalVariables.COUPON_APPLIED, false);
                    BillingHelper.getInstance().removeData();
                    HelperClass.orderStatusDailog(WebViewActivity.this,response.body().getMessage());
                } else {
                    CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),WebViewActivity.this);
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),WebViewActivity.this);
            }
        });
    }

}