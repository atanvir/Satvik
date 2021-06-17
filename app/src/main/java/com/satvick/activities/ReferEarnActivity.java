package com.satvick.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.satvick.R;
import com.satvick.adapters.TablyoutFragmentsAdapter;
import com.satvick.databinding.ActivityReferEarnBinding;
import com.satvick.model.ReferalListModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.text.MessageFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReferEarnActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityReferEarnBinding binding;
    private ReferalListModel.ReferalList referalListObject;
    private MyDialog dailog;
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(@javax.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferEarnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        intCtrl();
        referEarnApi();
    }

    private void init() {
        dailog=new MyDialog(this);
    }

    private void intCtrl() {
        binding.ivBack.setOnClickListener(this);
        binding.tvShare.setOnClickListener(this);
        binding.tvCopyLink.setOnClickListener(this);
    }

    private void referEarnApi() {
        dailog.showDialog();
        Call<ReferalListModel> call = apiInterface.getReferalListResult(HelperClass.getCacheData(this).second,HelperClass.getCacheData(this).first);
        call.enqueue(new Callback<ReferalListModel>() {
            @Override
            public void onResponse(Call<ReferalListModel> call, Response<ReferalListModel> response) {
                dailog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                            ReferalListModel data = response.body();
                            referalListObject=data.getReferalList();
                            String colorText= "Your Code:<font color=\"#df048b\"><bold>"+"#"+referalListObject.getReferalCode()+"</bold></font>" + "<font color=\"#df048b\"><bold>" + "</bold></font>";
                            binding.tvAnEasyss.setText(Html.fromHtml(colorText));
                            binding.viewPager.setAdapter(new TablyoutFragmentsAdapter(getSupportFragmentManager(), ReferEarnActivity.this,referalListObject));
                            binding.viewPager.setOffscreenPageLimit(2);
                            binding.tabLayout.setupWithViewPager(binding.viewPager);

                        } else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                            Toast.makeText(ReferEarnActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",ReferEarnActivity.this);

                }
            }
            @Override
            public void onFailure(Call<ReferalListModel> call, Throwable t) {
                dailog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),ReferEarnActivity.this);
            }
        });
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack: onBackPressed(); break;

            case R.id.tvShare:
            String ShareLink= MessageFormat.format("\"Sign-up using my code #{0} on the Satvik App & get discount.\\n Download the App using - https://www.playstore.com \\n\\n Hurry, it doesn’t get better than this! Download the Satvick App NOW!\"",referalListObject.getReferalCode());
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, ShareLink);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_referral_code_using)));
            break;

            case R.id.tvCopyLink:
            String text = "Download the App from - https://www.playstore.com \n\n Use my code(#"+referalListObject.getReferalCode()+") on registration and get huge discount.";
            ClipboardManager clipboard = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
            CommonUtil.setUpSnackbarMessage(binding.rlMain,"Link Copied",this);
            break;
        }
    }

}
