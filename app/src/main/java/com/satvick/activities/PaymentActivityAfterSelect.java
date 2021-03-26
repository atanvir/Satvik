package com.satvick.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.satvick.R;
import com.satvick.databinding.ActivityPaymentAfterSelectBinding;
import com.satvick.databinding.PopUpOrderPlacesSuccessfullyBinding;

public class PaymentActivityAfterSelect extends AppCompatActivity implements View.OnClickListener {

    ActivityPaymentAfterSelectBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_after_select);
        binding.ivBack.setOnClickListener(this);
        binding.btnPayNow.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                startActivity(new Intent(this, PaymentActivity.class));
                break;

            case R.id.btnPayNow:
                openPopup();
                break;
        }
    }

    private void openPopup() {

        PopUpOrderPlacesSuccessfullyBinding binding;
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_order_places_successfully, null, false);
        dialog.setContentView(binding.getRoot());

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);


        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivityAfterSelect.this, MainActivity.class));
            }
        });
        dialog.show();

    }
}
