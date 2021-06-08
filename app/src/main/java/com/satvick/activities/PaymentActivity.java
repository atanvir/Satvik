package com.satvick.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.satvick.R;
import com.satvick.databinding.ActivityPaymentBinding;
import com.satvick.databinding.PopUpOrderPlacesSuccessfullyBinding;

public class PaymentActivity extends Activity implements View.OnClickListener {

    ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);

        binding.tvCuttedText.setPaintFlags(binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        binding.ivBack.setOnClickListener(this);
        binding.btnPayNow.setOnClickListener(this);
        binding.llCreditDebitCard.setOnClickListener(this);


       /* binding.rdb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {
                    binding.rdb.setButtonDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.radio_button_salected));

                } else {
                    binding.rdb.setButtonDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.radio_button_un_salected_));
                }
            }

        });*/


        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int c_id = radioGroup.getCheckedRadioButtonId();
                Log.v("radioGroup", "c_id: " + c_id);

                int bank_id = binding.rdb.getId();

                if (c_id == bank_id) {
                    binding.rdb.setButtonDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.radio_button_salected));

                } else {
                    binding.rdb.setButtonDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.radio_button_un_salected_));
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPayNow:
                openPopup();
                break;

            case R.id.llCreditDebitCard:
                startActivity(new Intent(this, PaymentActivityAfterSelect.class));
                break;

            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, OrderConfirmationActivity.class).putExtra("from","PaymentActivity"));
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
                startActivity(new Intent(PaymentActivity.this, MainActivity.class));
            }
        });
        dialog.show();

    }
}
