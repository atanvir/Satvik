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
import com.google.gson.Gson;
import com.satvick.R;
import com.satvick.activities.MyOrderActivity;
import com.satvick.activities.OrderConfirmationActivity;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityWebviewBinding;
import com.satvick.model.LifeResponseModel;
import com.satvick.model.PaymentResponseModel;
import com.satvick.model.PlaceOrderModel;
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

    public void init() {
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
            LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");

//            dialog.showDialog();
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
//            dialog.hideDialog();
            LoadingDialog.cancelLoading();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
//                    String sdk= "<head><head></head><body><pre style=\"word-wrap: break-word; white-space: pre-wrap;\">{\"status\":\"SUCCESS\",\"indipay\":{\"order_id\":\"G8W\",\"tracking_id\":\"110181627297\",\"order_status\":\"Success\",\"status_message\":\"SUCCESS\"},\"message\":\"Payment Response\",\"requestKey\":\"api/indipay/response\"}</pre></body></head>";
                    PaymentResponseModel paymentResponse = null;
                    try {
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        XmlPullParser parser = factory.newPullParser();
                        parser.setInput(new StringReader(html));

                        int eventType = parser.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            String tagname = parser.getName()+"";
                            Log.e("tag","==>>"+tagname);
                            switch (eventType) {
                                case XmlPullParser.TEXT: paymentResponse=new Gson().fromJson(parser.getText(), PaymentResponseModel.class); break;
                            }
                            eventType = parser.next();
                        }

                    } catch (XmlPullParserException e) {e.printStackTrace();}
                    catch (IOException e) {e.printStackTrace();}
                    finally {
                        if(paymentResponse!=null){
                            if(paymentResponse.getIndipay().getStatus_message().equalsIgnoreCase("SUCCESS")) {
                                Toast.makeText(WebViewActivity.this, "Payment Successfull", Toast.LENGTH_SHORT).show();
                                binding.webview.setVisibility(View.GONE);
                                if (getIntent().getStringExtra("cameFrom").equalsIgnoreCase(OrderConfirmationActivity.class.getSimpleName())) orderPlaceApi(paymentResponse.getIndipay().getTracking_id());
                                else placeLifeOrderApi(paymentResponse.getIndipay().getTracking_id());
                            }else{
                                CommonUtil.setUpSnackbarMessage(binding.getRoot(),paymentResponse.getIndipay().getStatus_message(),WebViewActivity.this);
                            }
                        }else{
                            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"No Such Response Found",WebViewActivity.this);
                        }
                    }


                }
            }

            final WebView webview = findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    LoadingDialog.cancelLoading();
                    if (url.contains("https://soulahe.com/api/indipay/response")) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
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

    private void placeLifeOrderApi(String trackingId) {
        dialog.showDialog();
        Call<LifeResponseModel> call = apiInterface.placeLifeOrder(HelperClass.getCacheData(this).first,HelperClass.getCacheData(this).second,getIntent().getStringExtra("life_id"),getIntent().getStringExtra(AvenuesParams.ORDER_ID),trackingId);
        call.enqueue(new Callback<LifeResponseModel>() {
            public void onResponse(Call<LifeResponseModel> call, Response<LifeResponseModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) HelperClass.myArticlePopUp(WebViewActivity.this,response.body().getMessage());
                else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",WebViewActivity.this);
            }

            @Override
            public void onFailure(Call<LifeResponseModel> call, Throwable t) {
                dialog.hideDialog();
            }
        });

    }

    private void orderPlaceApi(String trackingId) {
        dialog.showDialog();
        Call<PlaceOrderModel> call = apiInterface.getPlaceOrderResult(HelperClass.getCacheData(this).first,
                                                                      HelperClass.getCacheData(this).second,
                                                                      SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.gift_wrapup_status).equalsIgnoreCase("Yes")?"1":"0",
                                                                      viewAddress.getAddress(), BillingHelper.getInstance().getBillingData().COUPON_CODE,
                                                                      "Online", getIntent().getStringExtra("order_number"), trackingId,
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