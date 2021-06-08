package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;import androidx.databinding.DataBindingUtil;
import android.view.View;

import com.satvick.Interfaces.IHelpCenter;
import com.satvick.Interfaces.IHelpCenterImplementation;
import com.satvick.R;
import com.satvick.databinding.ActivityManageYourAccountBinding;

public class ManageYourAccountActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityManageYourAccountBinding binding;
    private IHelpCenter center;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageYourAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
    }

    private void init() {
        center=new IHelpCenterImplementation();
    }

    private void initCtrl(){
        binding.rlunableLogin.setOnClickListener(this);
        binding.rlunsubscribe.setOnClickListener(this);
        binding.rlmywishList.setOnClickListener(this);
        binding.rlrefferalPoint.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: onBackPressed(); break;
            case R.id.rlunableLogin: center.showBottomSheet(this,binding.unableLoginText.getText().toString()); break;
            case R.id.rlunsubscribe: center.showBottomSheet(this,binding.unsubscribeText.getText().toString()); break;
            case R.id.rlmywishList: center.showBottomSheet(this,binding.mywishListText.getText().toString()); break;
            case R.id.rlrefferalPoint: center.showBottomSheet(this,binding.refferalPointText.getText().toString()); break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
