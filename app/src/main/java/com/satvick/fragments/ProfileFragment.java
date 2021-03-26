package com.satvick.fragments;

import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.satvick.R;
import com.satvick.activities.AddressActivity;
import com.satvick.activities.CouponsActivity;
import com.satvick.activities.EditProfileActivity;
import com.satvick.activities.HelpCenterActivity;
import com.satvick.activities.LoginActivity;
import com.satvick.activities.MainActivity;
import com.satvick.activities.MyOrderActivity;
import com.satvick.activities.MyWishListActivity;
import com.satvick.activities.NotificationActivity;
import com.satvick.activities.ReferAndEarnActivity;
import com.satvick.activities.SavedCardActivity;
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
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    View rootView;
    // FragmentProfileBinding binding;
    // FragmentProfileConstraintBinding binding;
    FragmentProfileNewBinding binding;
    private String mUserId = "";
    private String token = "";
    private String mUserImage = "";
    private String mFullName = "";
    private String mEmail = "";
    private String mPhone = "";
    private String mGender = "";
    private String mDob = "";
    private MyDialog myDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_new, container, false);
        rootView = binding.getRoot();
        init();

        if (HelperClass.showInternetAlert(getActivity())) {
            callViewProfileApi(binding.mainCl);//hit api
        }

        return rootView;
    }

    private void init() {
        myDialog=new MyDialog(getActivity());

        binding.tvMyOrders.setOnClickListener(this);
        binding.tvWishList.setOnClickListener(this);
        binding.tvAddress.setOnClickListener(this);
        binding.tvProfileDetails.setOnClickListener(this);
        binding.tvNotification.setOnClickListener(this);
        binding.tvHelpCenter.setOnClickListener(this);
        binding.tvLogOut.setOnClickListener(this);
        binding.tvCoupons.setOnClickListener(this);
        binding.tvSavedCard.setOnClickListener(this);
        binding.tvSettings.setOnClickListener(this);
        binding.textView82.setOnClickListener(this);

        if (SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("") ) {
            token = "1";
            mUserId = "1";
        } else if(SharedPreferenceWriter.getInstance(getActivity()).
                getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("true")){
            token = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.TOKEN);
            mUserId = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.USER_ID);
        }
    }


    private void callViewProfileApi(final View view) {
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ViewProfileModel> call = apiInterface.getViewProfileResult(token, mUserId);

        call.enqueue(new Callback<ViewProfileModel>() {
            @Override
            public void onResponse(Call<ViewProfileModel> call, Response<ViewProfileModel> response) {
                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    ViewProfileModel data = response.body();
                    if (data.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        setUI(data);//set data
                    } else if(data.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)){
                        Toast.makeText(getActivity(), "Another Device has logged-in", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                    }
                }
                else
                {

                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(getActivity(),R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callViewProfileApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }


            @Override
            public void onFailure(Call<ViewProfileModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });

    }


    private void setUI(ViewProfileModel data) {

        mUserImage = data.getViewProfileResponse().getImage();
        mFullName = data.getViewProfileResponse().getName();
        mEmail = data.getViewProfileResponse().getEmail();
        mPhone = data.getViewProfileResponse().getPhone();
        mDob =data.getViewProfileResponse().getDob();
        mGender=data.getViewProfileResponse().getGender();


//        if (!mUserImage.isEmpty() && mUserImage != null && !mUserImage.equals("null") && !mUserImage.equals("")) {
//            Picasso.with(getActivity()).load(mUserImage).into(binding.imageView15);
//        }

        if (mUserImage != null) {
            if(mUserImage.contains("https://graph.facebook.com/")) {
            mUserImage=mUserImage.replace("http://mobuloustech.com/yodapi/public", "");
            }
            Picasso.with(getActivity()).load(mUserImage).into(binding.imageView15);
        }

        if (!mFullName.isEmpty() && mFullName != null && !mFullName.equals("null") && !mUserImage.equals("")) {
            binding.textView79.setText(mFullName);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvMyOrders:
                startActivity(new Intent(getActivity(), MyOrderActivity.class).putExtra("from", "CancelOrderActivity"));
                break;

            case R.id.tvWishList:
                startActivity(new Intent(getActivity(), MyWishListActivity.class));
                break;

            case R.id.tvAddress:
                startActivity(new Intent(getActivity(), AddressActivity.class));
                break;

            case R.id.tvProfileDetails:
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", mEmail);
                bundle.putString("name", mFullName);
                bundle.putString("phone", mPhone);
                bundle.putString("image", mUserImage);
                bundle.putString("dob", mDob);
                bundle.putString("gender", mGender);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.tvNotification:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;

            case R.id.tvHelpCenter:
                startActivity(new Intent(getActivity(), HelpCenterActivity.class).putExtra("from", "ProfileFrag"));
                break;

            case R.id.tvCoupons:
                startActivity(new Intent(getActivity(), CouponsActivity.class));
                break;

            case R.id.tvSavedCard:
                startActivity(new Intent(getActivity(), SavedCardActivity.class));
                break;

            case R.id.tvSettings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;

            case R.id.tvLogOut:
                //1 way
                Intent intent1 = new Intent(getActivity(),LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);

                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "false");
                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "");
                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.TOKEN, "");
                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.USER_ID, "");
                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.BATCH_COUNT, "");
                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue("Ids","");

                getActivity().finish();

                if (MainActivity.instance != null) {
                    MainActivity.instance.finish();
                }



                break;

            case R.id.textView82:
                startActivity(new Intent(getActivity(), ReferAndEarnActivity.class));
                break;

        }
    }
}
