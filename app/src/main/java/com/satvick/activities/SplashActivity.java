package com.satvick.activities;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        SharedPreferenceWriter.getInstance(this).writeStringValue("converted_amount","1");
        SharedPreferenceWriter.getInstance(this).writeStringValue("symbol","₹");
        getDeviceToken();
        getProductId();
    }

    private void getDeviceToken() {
        final SharedPreferenceWriter mPreference = SharedPreferenceWriter.getInstance(getApplicationContext());

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("test", "getInstanceId failed", task.getException());
                            return;
                        }

                        String device_token = task.getResult().getToken();

                        if (device_token == null) {
                            getDeviceToken();
                        } else {
                            mPreference.writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, device_token);
                            Log.e("token",mPreference.getString(SharedPreferenceKey.DEVICE_TOKEN));
                        }

                     }
                });
    }


    private void goNext() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();

            }
        }, 2000);
    }

    private void getProductId() {
        if (getIntent() != null) {
            Intent appLinkIntent = getIntent();
            Uri appLinkData = appLinkIntent.getData();
            Log.e("Deep link url :", String.valueOf(appLinkData));

            if (appLinkData != null) {
                String url = appLinkData.toString();
                String status1 = url.substring(url.lastIndexOf("/") + 1);
                Intent intent=new Intent(this,MainActivity.class);
                intent.putExtra("isDeepLink",true);
                intent.putExtra("product_id",status1);
                startActivity(intent);
                this.finish();
            }else {
                goNext();
            }
        }else {
            goNext();
        }
    }

}

