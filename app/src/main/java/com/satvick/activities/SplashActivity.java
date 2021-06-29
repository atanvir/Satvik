package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivitySplashBinding;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GuestUserData;

public class SplashActivity extends AppCompatActivity {

   private ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        startIntent();
    }

    private void init() {
        SharedPreferenceWriter.getInstance(this).writeStringValue("converted_amount", "1");
        SharedPreferenceWriter.getInstance(this).writeStringValue("symbol", "₹");
        CommonUtil.getDeviceToken(this);
        GuestUserData.getInstance().setHugeData(CommonUtil.setCartData(this));
    }


    private void startIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getIntent() != null ? getIntent().getData() != null ? true : false : false) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("isDeepLink", true);
                    intent.putExtra("product_id", CommonUtil.getDeepLinkProductId(getIntent().getData().toString()));
                    startActivity(intent);
                }
                else startActivity(new Intent(SplashActivity.this, MainActivity.class));

            }
        }, 2000);
    }
}

