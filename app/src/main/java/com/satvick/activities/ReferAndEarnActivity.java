package com.satvick.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;import androidx.databinding.DataBindingUtil;

import com.satvick.R;
import com.satvick.databinding.ActivityReferAndEarnNewBinding;
import com.satvick.fragments.more.ReferEarnFragment;

import javax.annotation.Nullable;

public class ReferAndEarnActivity extends AppCompatActivity {

    ActivityReferAndEarnNewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_refer_and_earn_new);

        ReferEarnFragment fragment=new ReferEarnFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerRefer,fragment).commit();

    }

}
