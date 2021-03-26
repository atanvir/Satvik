package com.satvick.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;import androidx.databinding.DataBindingUtil;

import com.satvick.R;
import com.satvick.databinding.ActivityReturnOrderBinding;
import com.satvick.databinding.PopUpReturnOrderBinding;

import javax.annotation.Nullable;

public class ReturnOrderActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityReturnOrderBinding binding;
    boolean isChecked = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_return_order);
        binding.ivBack.setOnClickListener(this);
        binding.tvRequestReturn.setOnClickListener(this);
        binding.ivDefaultAddress.setOnClickListener(this);
       // binding.chbBankAccount.setOnClickListener(this);
        binding.chbIConfirm.setOnClickListener(this);
        binding.chbIConfirmIntact.setOnClickListener(this);

       /* binding.chbBankAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    binding.chbBankAccount.setButtonDrawable(R.drawable.sale_box);
                } else {
                    binding.chbBankAccount.setButtonDrawable(R.drawable.new_rect);
                }
            }
        });*/

        binding.chbIConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    binding.chbIConfirm.setButtonDrawable(R.drawable.sale_box);
                } else {
                    binding.chbIConfirm.setButtonDrawable(R.drawable.new_rect);
                }
            }
        });

        binding.chbIConfirmIntact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    binding.chbIConfirmIntact.setButtonDrawable(R.drawable.sale_box);
                } else {
                    binding.chbIConfirmIntact.setButtonDrawable(R.drawable.new_rect);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;

            case R.id.tvRequestReturn:
                //startActivity(new Intent(ReturnOrderActivity.this, MyOrderActivity.class));
                openReturnOrderPopup();
                break;

            case R.id.ivDefaultAddress:
                if (isChecked) {
                    isChecked = false;
                    binding.ivDefaultAddress.setBackgroundResource(R.drawable.sale_box);
                } else {
                    isChecked = true;
                    binding.ivDefaultAddress.setBackgroundResource(R.drawable.new_rect);
                }
                break;

        }

    }

    private void openReturnOrderPopup() {

        PopUpReturnOrderBinding binding;
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_return_order, null, false);
        dialog.setContentView(binding.getRoot());

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);


        binding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(ReturnOrderActivity.this, ReturnOrderCancelActivity.class));
            }
        });
        dialog.show();
    }

}
