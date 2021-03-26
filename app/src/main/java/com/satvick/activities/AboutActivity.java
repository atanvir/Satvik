package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.satvick.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    WebView webView;
    ImageView ivBack;

    String url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_us);

        webView = findViewById(R.id.webview);
        ivBack = findViewById(R.id.ivBack);

        url = "http://mobuloustech.com/yodapi/about-us1";

        webView.setWebViewClient(new CustomWebViewClient());
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        webView.loadUrl(url);

        ivBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class).putExtra("from","moreFragment"));
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
