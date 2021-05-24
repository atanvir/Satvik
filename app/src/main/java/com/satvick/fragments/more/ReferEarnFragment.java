package com.satvick.fragments.more;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.satvick.R;
import com.satvick.activities.MainActivity;
import com.satvick.adapters.TablyoutFragmentsAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.FragmentReferAndEarnBinding;
import com.satvick.model.ReferalListModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;

import java.text.MessageFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ReferEarnFragment extends Fragment implements View.OnClickListener {

    View rootView;
    FragmentReferAndEarnBinding binding;
    String userId="",token="";
    ReferalListModel.ReferalList referalListObject;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_refer_and_earn, container, false);
        rootView = binding.getRoot();
        init();
        myReferralListApi();
        return rootView;
    }

    private void myReferralListApi() {

        final MyDialog myDialog=new MyDialog(getActivity());
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ReferalListModel> call = apiInterface.getReferalListResult(userId,token);
        call.enqueue(new Callback<ReferalListModel>() {
            @Override
            public void onResponse(Call<ReferalListModel> call, Response<ReferalListModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.isSuccessful()) {
                        ReferalListModel data = response.body();
                        if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                            binding.scrollView2.setVisibility(View.VISIBLE);
                            referalListObject=data.getReferalList();
                            String colorText= "Your Code:<font color=\"#df048b\"><bold>"+"#"+referalListObject.getReferalCode()+"</bold></font>" + "<font color=\"#df048b\"><bold>" + "</bold></font>";
                            binding.tvAnEasyss.setText(Html.fromHtml(colorText));
                            binding.viewPager.setAdapter(new TablyoutFragmentsAdapter(getFragmentManager(), getActivity(),referalListObject));
                            binding.viewPager.setOffscreenPageLimit(2);
                            binding.tabLayout.setupWithViewPager(binding.viewPager);


                        } else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        myDialog.hideDialog();

                    }
                }
            }
            @Override
            public void onFailure(Call<ReferalListModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }



    private void init() {
        binding.ivBack.setOnClickListener(this);
        binding.tvShare.setOnClickListener(this);
        binding.tvCopyLink.setOnClickListener(this);
        if (SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false")||
                SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
            userId="1";
            token="1";
        }
        else
        {
            userId=SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.USER_ID);
            token=SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.TOKEN);

        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
              startActivity(new Intent(getActivity(), MainActivity.class).putExtra("from","ReferAndEarnFragment"));
              break;

            case R.id.tvShare:
                String ShareLink= MessageFormat.format("\"Sign-up using my code #{0} on the YOD App & get discount.\\n Download the App using - https://www.playstore.com \\n\\n Hurry, it doesn’t get better than this! Download the YOD App NOW!\"",referalListObject.getReferalCode());
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, ShareLink);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_referral_code_using)));
                break;

            case R.id.tvCopyLink:
                String text = "Download the App from - https://www.playstore.com \n\n Use my code(#"+referalListObject.getReferalCode()+") on registration and get huge discount.";
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", text);
                clipboard.setPrimaryClip(clip);
                CommonUtil.setUpSnackbarMessage(binding.rlMain,"Link Copied",getActivity());

                break;
        }
    }

}
