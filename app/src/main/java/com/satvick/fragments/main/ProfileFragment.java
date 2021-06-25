package com.satvick.fragments.main;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.satvick.activities.ReferEarnActivity;
import com.satvick.fragments.more.SubscriptionFragment;
import com.satvick.model.ViewProfileResponse;
import com.satvick.R;
import com.satvick.activities.SavedAddressActivity;
import com.satvick.activities.CouponsActivity;
import com.satvick.activities.EditProfileActivity;
import com.satvick.activities.HelpCenterActivity;
import com.satvick.activities.LoginActivity;
import com.satvick.activities.MyOrderActivity;
import com.satvick.activities.WishListActivity;
import com.satvick.activities.NotificationActivity;
import com.satvick.activities.SettingsActivity;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.FragmentProfileNewBinding;
import com.satvick.model.ViewProfileModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.satvick.utils.HelperClass.getCacheData;

public class ProfileFragment extends Fragment implements View.OnClickListener, Callback<ViewProfileModel> {

    private FragmentProfileNewBinding binding;
    private ViewProfileResponse data;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private SubscriptionFragment subscriptionFragment= new SubscriptionFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileNewBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        initCtrl();
        if (HelperClass.showInternetAlert(getActivity())) callViewProfileApi();
    }

    private void init() {
    }

    private void initCtrl() {
        binding.LLOrders.setOnClickListener(this);
        binding.LLWishList.setOnClickListener(this);
        binding.LLAddress.setOnClickListener(this);
        binding.LLArticles.setOnClickListener(this);
        binding.LLProfileDetails.setOnClickListener(this);
        binding.LLNotification.setOnClickListener(this);
        binding.LLHelpCenter.setOnClickListener(this);
        binding.LLCoupon.setOnClickListener(this);
        binding.LLSetting.setOnClickListener(this);
        binding.LLRefer.setOnClickListener(this);
        binding.tvLogOut.setOnClickListener(this);
    }


    private void callViewProfileApi() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ViewProfileModel> call = apiInterface.getViewProfileResult(getCacheData(requireActivity()).first,
                                                                        getCacheData(requireActivity()).second);
        call.enqueue(this);
    }

    private void setUI(ViewProfileModel data) {
        this.data=data.getViewProfileResponse();
        if(data.getViewProfileResponse().getImage()!=null) {
            if (data.getViewProfileResponse().getImage().contains("/users-photos/")) {
                data.getViewProfileResponse().setImage("https://soulahe.com//public/" + data.getViewProfileResponse().getImage());
            }
            Glide.with(requireActivity()).load(this.data.getImage()).into(binding.imageView15);
        }
        binding.tvName.setText(this.data.getName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.LLOrders: startNewActivity(MyOrderActivity.class,"CancelOrderActivity"); break;
            case R.id.LLWishList: CommonUtil.startNewActivity(requireActivity(), WishListActivity.class); break;
            case R.id.LLAddress: CommonUtil.startNewActivity(requireActivity(),SavedAddressActivity.class); break;
            case R.id.LLArticles :  requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, subscriptionFragment).addToBackStack(ProfileFragment.class.getSimpleName()).commit(); break;
            case R.id.LLProfileDetails: loadProfileDetail(); break;
            case R.id.LLNotification: CommonUtil.startNewActivity(requireActivity(),NotificationActivity.class); break;
            case R.id.LLHelpCenter: startNewActivity(HelpCenterActivity.class,"ProfileFrag"); break;
            case R.id.LLCoupon: CommonUtil.startNewActivity(requireActivity(),CouponsActivity.class); break;
            case R.id.LLSetting: CommonUtil.startNewActivity(requireActivity(),SettingsActivity.class); break;
            case R.id.tvLogOut: logout(); break;
            case R.id.LLRefer: CommonUtil.startNewActivity(requireActivity(),ReferEarnActivity.class); break;
        }
    }

    public void startNewActivity(Class className,String from){
        Intent intent=new Intent(requireActivity(),className);
        intent.putExtra("from", from);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loadProfileDetail() {
        Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
        intent.putExtra("email",  data.getEmail()!=null?data.getEmail():"");
        intent.putExtra("name",   data.getName()!=null?data.getName():"");
        intent.putExtra("phone",  data.getPhone()!=null?data.getPhone():"");
        intent.putExtra("image",  data.getImage()!=null?data.getImage():"");
        intent.putExtra("dob",    data.getDob()!=null?data.getDob():"");
        intent.putExtra("gender", data.getGender()!=null?data.getGender():"");
        startActivity(intent);
    }

    private void logout() {
        SharedPreferenceWriter.getInstance(getActivity()).writeIntValue(GlobalVariables.count, 0);
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.product_id, "");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.color_name, "");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.size, "");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.quantity, "");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "false");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.TOKEN, "");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.USER_ID, "");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.BATCH_COUNT, "");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue("Ids","");

        Intent intent1 = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent1);

        requireActivity().finish();
    }

    @Override
    public void onResponse(Call<ViewProfileModel> call, Response<ViewProfileModel> response) {
        if(getActivity()!=null) {
            binding.progressBar.setVisibility(View.GONE);
            if (response.isSuccessful()) {
                if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setUI(response.body());
                else CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(), requireActivity());
            } else CommonUtil.setUpSnackbarMessage(binding.getRoot(), "Internal Server Error", requireActivity());
        }
    }

    @Override
    public void onFailure(Call<ViewProfileModel> call, Throwable t) {
        if(getActivity()!=null) {
            binding.progressBar.setVisibility(View.GONE);
            CommonUtil.setUpSnackbarMessage(binding.getRoot(), t.getMessage(), requireActivity());
        }
    }
}
