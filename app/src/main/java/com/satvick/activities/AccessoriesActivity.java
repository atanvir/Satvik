package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;import androidx.databinding.DataBindingUtil;

import com.satvick.R;
import com.satvick.databinding.ActivityAccessoriesBinding;

public class AccessoriesActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAccessoriesBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_accessories);

        binding.ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:

                startActivity(new Intent(this, MainActivity.class).putExtra("from", "HomeFragment"));

                break;
        }
    }
}
