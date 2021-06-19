package com.satvick.fragments.main;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.satvick.activities.MyWishListActivity;
import com.satvick.activities.NotificationActivity;
import com.satvick.activities.SettingsActivity;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.FragmentProfileNewBinding;
import com.satvick.model.ViewProfileModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.satvick.utils.HelperClass.getCacheData;

public class ProfileFragment extends Fragment implements View.OnClickListener, Callback<ViewProfileModel> {

    private FragmentProfileNewBinding binding;
    private ViewProfileResponse data;
    private String token,userId;
    private MyDialog dialog;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private SubscriptionFragment subscriptionFragment= new SubscriptionFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        initCtrl();
        if (HelperClass.showInternetAlert(getActivity())) callViewProfileApi();
    }

    private void init() {
        dialog=new MyDialog(requireActivity());
        token= getCacheData(requireActivity()).first;
        userId=getCacheData(requireActivity()).second;
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
        dialog.showDialog();
        Call<ViewProfileModel> call = apiInterface.getViewProfileResult(token, userId);
        call.enqueue(this);
    }


    private void setUI(ViewProfileModel data) {
        this.data=data.getViewProfileResponse();
        if(data.getViewProfileResponse().getImage().contains("/users-photos/")){
            data.getViewProfileResponse().setImage("https://soulahe.com//public/"+data.getViewProfileResponse().getImage());
        }
        Glide.with(requireActivity()).load(this.data.getImage()).into(binding.imageView15);
        binding.tvName.setText(this.data.getName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.LLOrders: startNewActivity(MyOrderActivity.class,"CancelOrderActivity"); break;
            case R.id.LLWishList: startNewActivity(MyWishListActivity.class); break;
            case R.id.LLAddress: startNewActivity(SavedAddressActivity.class); break;
            case R.id.LLArticles :  requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, subscriptionFragment).addToBackStack(ProfileFragment.class.getSimpleName()).commit(); break;
            case R.id.LLProfileDetails: loadProfileDetail(); break;
            case R.id.LLNotification: startNewActivity(NotificationActivity.class); break;
            case R.id.LLHelpCenter: startNewActivity(HelpCenterActivity.class,"ProfileFrag"); break;
            case R.id.LLCoupon: startNewActivity(CouponsActivity.class); break;
            case R.id.LLSetting: startNewActivity(SettingsActivity.class); break;
            case R.id.tvLogOut: logout(); break;
            case R.id.LLRefer: startNewActivity(ReferEarnActivity.class); break;
        }
    }

    public void startNewActivity(Class className){
        Intent intent=new Intent(requireActivity(),className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void startNewActivity(Class className,String from){
        Intent intent=new Intent(requireActivity(),className);
        intent.putExtra("from", from);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void loadProfileDetail() {
        if(data!=null) {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("email", data.getEmail());
            bundle.putString("name", data.getName());
            bundle.putString("phone", data.getPhone());
            bundle.putString("image", data.getImage());
            bundle.putString("dob", data.getDob());
            bundle.putString("gender", data.getGender());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void logout() {
        Intent intent1 = new Intent(getActivity(),LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
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
        getActivity().finish();
    }

    @Override
    public void onResponse(Call<ViewProfileModel> call, Response<ViewProfileModel> response) {
        dialog.hideDialog();
        if (response.isSuccessful()) {
            ViewProfileModel data = response.body();
            if (data.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) { setUI(data); }
            else Toast.makeText(requireActivity(), data.getMessage(), Toast.LENGTH_SHORT).show();
        }
        else { Toast.makeText(requireActivity(), "Internal Server Error", Toast.LENGTH_SHORT).show(); }
    }

    @Override
    public void onFailure(Call<ViewProfileModel> call, Throwable t) {
        dialog.hideDialog();
        Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
