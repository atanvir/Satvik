package com.satvick.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.satvick.R;
import com.satvick.adapters.SizeChartAdapter;
import com.satvick.databinding.ActivitySizeChartBinding;
import com.satvick.model.Sizechart;

import java.util.List;

public class SizeChartActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySizeChartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_size_chart);
        binding.productListBackBtn.setOnClickListener(this);

        List<Sizechart> sizeList=getIntent().getParcelableArrayListExtra("sizechart");
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(new SizeChartAdapter(this,sizeList));


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.productListBackBtn:
                onBackPressed();
                break;

        }

    }
}
