package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.satvick.R;
import com.satvick.databinding.ActivitySavedCardBinding;

public class SavedCardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySavedCardBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_card);
        binding.ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class).putExtra("from", "SavedCardActivity"));
    }

}
