package com.satvick.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivitySplashBinding;
import com.satvick.model.ProductDetails;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.GuestUserData;
import com.satvick.utils.HelperClass;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
   private ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        SharedPreferenceWriter.getInstance(this).writeStringValue("converted_amount", "1");
        SharedPreferenceWriter.getInstance(this).writeStringValue("symbol", "₹");
        HelperClass.printKeyHash(this);
        getDeviceToken();


        ArrayList<ProductDetails> productDetails=new ArrayList<>();
        String product[]=SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.product_id).split(",");
        String sizess[]=SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.size).split(",");
        String quantiy[]=SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.quantity).split(",");
        Log.e("product",""+product.length);
        for(int i=0;i<product.length;i++) {
            if (!product[i].equalsIgnoreCase("")){
                try {
                    productDetails.add(new ProductDetails(product[i], "", sizess[i], quantiy[i]));
                } catch (Exception e) {
                    productDetails.add(new ProductDetails(product[i], "", "", quantiy[i]));
                }
            }
        }
        GuestUserData.getInstance().setHugeData(productDetails);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
//             this.requestPermissions(new String[]{Manifest.permission.SEND_SMS},101);
//        }else getProductId();
//        }
//        else getProductId();
        getProductId();
    }

    private void getDeviceToken() {
        final SharedPreferenceWriter mPreference = SharedPreferenceWriter.getInstance(getApplicationContext());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>(){

            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                String device_token = task.getResult();
                if (device_token == null) {
                    getDeviceToken();
                } else {
                    mPreference.writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, device_token);
                    Log.e("token", mPreference.getString(SharedPreferenceKey.DEVICE_TOKEN));
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
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("isDeepLink", true);
                intent.putExtra("product_id", status1);
                startActivity(intent);
                this.finish();
            } else {
                goNext();
            }
        } else {
            goNext();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 101:
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getProductId();
            }else{
                Toast.makeText(this, "Please allow permission..", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
                        this.requestPermissions(new String[]{Manifest.permission.SEND_SMS},101);
                    }else getProductId();
                }
                else getProductId();
            }
           break;
        }
    }
}

