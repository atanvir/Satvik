package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.adapters.ProductListAdapter;
import com.satvick.databinding.ActivityHelpCenterBinding;
import com.satvick.contracts.MyOrderHelp.MyOrderHelpContract;
import com.satvick.model.MyOrderHelp;
import com.satvick.contracts.MyOrderHelp.MyOrderHelpPresenter;
import com.satvick.retrofit.MyDialog;

public class HelpCenterActivity extends AppCompatActivity implements View.OnClickListener, MyOrderHelpContract.View {
    private ActivityHelpCenterBinding binding;
    private MyDialog dailog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHelpCenterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
    }

    private void init(){
        dailog=new MyDialog(this);
        MyOrderHelpPresenter presenter=new MyOrderHelpPresenter(this,this);
        presenter.requestMyOderHelpData();
    }

    private void initCtrl() {
        binding.ivBack.setOnClickListener(this);
        binding.llPaymentRefund.setOnClickListener(this);
        binding.llOffers.setOnClickListener(this);
        binding.llManageYourAccount.setOnClickListener(this);
        binding.llOther.setOnClickListener(this);
        binding.llPurchase.setOnClickListener(this);
        binding.tvShowMore.setOnClickListener(this);
        binding.tvMyOrders.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: onBackPressed(); break;
            case R.id.llPaymentRefund: startActivity(new Intent(this, PaymentRefundActivity.class)); break;
            case R.id.llOffers: startActivity(new Intent(this, OffersDiscountsCouponsActivity.class)); break;
            case R.id.llManageYourAccount: startActivity(new Intent(this, ManageYourAccountActivity.class)); break;
            case R.id.llOther: startActivity(new Intent(this, OtherActivity.class)); break;
            case R.id.llPurchase: startActivity(new Intent(this, MyOrderActivity.class).putExtra("from","HelpCenterActivity")); break;
            case R.id.tvShowMore: binding.llPurchase.performClick(); break;
            case R.id.tvMyOrders: startActivity(new Intent(this, MyOrderActivity.class).putExtra("from","")); break;
        }
    }


    @Override
    public void showProgress() {
        dailog.showDialog();
    }

    @Override
    public void hideProgress() {
        dailog.hideDialog();
    }

    @Override
    public void setDataToView(MyOrderHelp data) {
        if(data!=null)
        {
            binding.scrollView.setVisibility(View.VISIBLE);
            if(data.getResponse().size()>0) {
                binding.ProductRV.setVisibility(View.VISIBLE);
                binding.ProductRV.setAdapter(new ProductListAdapter(this, data.getResponse(),HelpCenterActivity.class.getSimpleName()));
            }
            else
            {
                binding.tvShowMore.setVisibility(View.GONE);
                binding.viewBelowShowMore.setVisibility(View.GONE);
                binding.viewBelowRV.setVisibility(View.VISIBLE);
            }

            if(data.getResponse().size()>2)
            {
                binding.tvShowMore.setVisibility(View.VISIBLE);
                binding.viewBelowShowMore.setVisibility(View.VISIBLE);
            }else
            {
//                binding.viewBelowShowMore.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResponseFailure(String message) {
        Snackbar.make(findViewById(R.id.rlMain),message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
