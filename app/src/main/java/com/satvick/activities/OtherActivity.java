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
import com.satvick.databinding.ActivityOtherBinding;

public class OtherActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityOtherBinding binding;
    private IHelpCenter center;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other);
        init();

    }

    private void init() {

        binding.rlMobileApp.setOnClickListener(this);
        binding.rlQuery.setOnClickListener(this);
        binding.rlOther.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        center=new IHelpCenterImplementation();



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.rlMobileApp:
                center.showBottomSheet(this,binding.MobileAppText.getText().toString());
                break;

            case R.id.rlQuery:
                center.showBottomSheet(this,binding.QueryText.getText().toString());
                break;

            case R.id.rlOther:
                center.showBottomSheet(this,binding.OtherText.getText().toString());
                break;


        }
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,HelpCenterActivity.class).putExtra("from","OtherActivity"));
    }
}
