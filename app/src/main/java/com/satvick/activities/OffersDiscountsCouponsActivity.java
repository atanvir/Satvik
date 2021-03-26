package com.satvick.activities;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.satvick.Interfaces.IHelpCenter;
import com.satvick.Interfaces.IHelpCenterImplementation;
import com.satvick.R;
import com.satvick.databinding.ActivityOffersDiscountsCouponsBinding;

public class OffersDiscountsCouponsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityOffersDiscountsCouponsBinding binding;
    private  IHelpCenter center;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_offers_discounts_coupons);
        init();

    }

    private void init() {
        binding.rlMyCoupons.setOnClickListener(this);
        binding.rlIAmUnable.setOnClickListener(this);
        binding.rlInstant.setOnClickListener(this);
        binding.rlWallet.setOnClickListener(this);
        binding.rlDiscount.setOnClickListener(this);
        binding.rlOther.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        center=new IHelpCenterImplementation();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.rlMyCoupons:
                center.showBottomSheet(this,binding.couponText.getText().toString());
                break;

            case R.id.rlIAmUnable:
                center.showBottomSheet(this,binding.iAmUnableText.getText().toString());
                break;

            case R.id.rlInstant:
                center.showBottomSheet(this,binding.instantText.getText().toString());
                break;

            case R.id.rlWallet:
                center.showBottomSheet(this,binding.wallet.getText().toString());
                break;

            case R.id.rlDiscount:
                center.showBottomSheet(this,binding.discountText.getText().toString());
                break;

            case R.id.rlOther:
                center.showBottomSheet(this,binding.OtherText.getText().toString());
                break;
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        startActivity(new Intent(this,HelpCenterActivity.class).putExtra("from","OffersDiscountsCouponsActivity"));
    }
}
