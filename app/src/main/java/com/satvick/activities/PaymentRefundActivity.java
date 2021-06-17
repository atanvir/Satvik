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
import com.satvick.databinding.ActivityPaymentRefundBinding;

public class PaymentRefundActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityPaymentRefundBinding binding;
    private IHelpCenter center;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPaymentRefundBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
    }

    private void init() {
        center=new IHelpCenterImplementation();
    }

    private void initCtrl(){
        binding.ivBack.setOnClickListener(this);
        binding.rlnotrecvRefund.setOnClickListener(this);
        binding.rlpaymentDebited.setOnClickListener(this);
        binding.rlaccountDetail.setOnClickListener(this);
        binding.rlWallet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        case R.id.ivBack: onBackPressed(); break;
        case R.id.rlnotrecvRefund: center.showBottomSheet(this,binding.notrecvText.getText().toString()); break;
        case R.id. rlpaymentDebited: center.showBottomSheet(this,binding.paymentDebtText.getText().toString()); break;
        case R.id.rlaccountDetail: center.showBottomSheet(this,binding.accountDetailsText.getText().toString()); break;
        case R.id.rlWallet: center.showBottomSheet(this,binding.walletText.getText().toString()); break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
