package com.satvick.activities;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import com.satvick.R;
import com.satvick.databinding.ItemMyOrderCancelBinding;

public class ReturnOrderCancelActivity extends AppCompatActivity implements View.OnClickListener {

    ItemMyOrderCancelBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.item_my_order_cancel);
        binding.llCancelOrder.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llCancelOrder:
                startActivity(new Intent(this, OrderManageActivity.class));
                break;
        }
    }
}
