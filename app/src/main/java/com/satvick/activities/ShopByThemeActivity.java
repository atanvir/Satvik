package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.View;

import com.satvick.R;
import com.satvick.adapters.ShopByTheme;
import com.satvick.databinding.ActivityShopByThemeBinding;
import com.satvick.model.ShopByThemeModel;
import com.satvick.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

public class ShopByThemeActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityShopByThemeBinding binding;
    String herImage="",himImage="",subcatId="";
    String productName="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_shop_by_theme);
        getIntents();
        init();
        Log.e("subcatId",subcatId);

        List<ShopByThemeModel> imgList=new ArrayList<>();
        imgList.add(new ShopByThemeModel(subcatId,himImage,productName));
        imgList.add(new ShopByThemeModel(subcatId,herImage,productName));
        binding.shopByThemeRecyclerView.setAdapter(new ShopByTheme(this,imgList));


    }

    private void getIntents() {
        Intent intent=getIntent();

        if(intent!=null){
            himImage=intent.getStringExtra("himimage");
            herImage= intent.getStringExtra("herimage");
            subcatId=getIntent().getStringExtra(GlobalVariables.subcatid);
            productName=getIntent().getStringExtra(GlobalVariables.section_name);
        }
    }

    private void init() {
        binding.tvTitle1.setText(productName);
        binding.ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class).putExtra("from","ProductListFragmentToCameAtHomeAfterLogin"));
    }
}
