package com.satvick.fragments.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.satvick.R;
import com.satvick.activities.LoginActivity;
import com.satvick.activities.MainActivity;
import com.satvick.activities.MyWishListActivity;
import com.satvick.activities.NotificationActivity;
import com.satvick.activities.SearchScreenActivity;
import com.satvick.activities.SignUpActivity;
import com.satvick.adapters.AutoSlideViewPagerBannerAdapter;
import com.satvick.adapters.CategoriesAdapter;
import com.satvick.adapters.MainCategoryAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.FragmentHomeAfterLoginBinding;
import com.satvick.databinding.PopUpBottomSheetLoginWhenUserNotLogedInBinding;
import com.satvick.fcm.MyFirebaseMessagingService;
import com.satvick.model.CategoriesBeans;
import com.satvick.model.HomeResponseModel;
import com.satvick.model.ProductBean;
import com.satvick.model.SocialLoginModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.satvick.utils.HelperClass.showInternetAlert;

public class HomeFragment extends Fragment implements View.OnClickListener, MyFirebaseMessagingService.ShowDot, FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback, ViewTreeObserver.OnScrollChangedListener {

    private Runnable sliderRunnable;
    private Handler sliderHandler=new Handler(Looper.myLooper());
    private TextView tvBadge;
    int sliderPostion=-1;
    private Dialog dialog;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 901;
    private  FragmentHomeAfterLoginBinding binding;
    private MyDialog dailog ;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    public HomeFragment(){

    }

    public HomeFragment(TextView tvBadge){
        this.tvBadge=tvBadge;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_home_after_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        initCtrl();
        if(showInternetAlert(getActivity())) callHomeApi();
    }

    private void init() {
        dailog=new MyDialog(requireActivity());
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        SharedPreferenceWriter.getInstance(requireActivity()).writeBooleanValue("kIsFirstTime",true);
        MyFirebaseMessagingService.setListner(HomeFragment.this);
        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean("DOT", false)) binding.ivDot.setVisibility(View.VISIBLE);
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public void show() {
        SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue("DOT", true);
        binding.ivDot.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        getBasicProfile(loginResult);
    }

    @Override
    public void onCancel() {
        Toast.makeText(requireActivity(), "Cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }


    private void getBasicProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void callLoginApiForSocial(final String name, final String fbid, final String email,final String profilePhoto,final String socialType) {
        dailog.showDialog();
        Call<SocialLoginModel> call = apiInterface.socialLogin(fbid, socialType,
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.DEVICE_TOKEN),
                                                                "android", name, email,profilePhoto,"",
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.product_id),
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.color_name),
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.quantity),
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.size));
        call.enqueue(new Callback<SocialLoginModel>() {
            @Override
            public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {
                dailog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        saveUserData(response);
                        Intent intent = new Intent(getActivity(), MainActivity.class).putExtra("from", "LoginButton");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        CommonUtil.setUpSnackbarMessage(binding.mainRl,response.body().getMessage(),getActivity());
                    }
                } else {
                    CommonUtil.setUpSnackbarMessage(binding.mainRl,"Internal Server Error",getActivity());

                }
            }

            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                dailog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.mainRl,t.getMessage(),getActivity());
            }
        });
    }

    private void saveUserData(Response<SocialLoginModel> response) {
        SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.BATCH_COUNT, "");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.FULL_NAME, response.body().getSociallogin().getName());
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.EMAIL, (String) response.body().getSociallogin().getEmail());
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.IMAGE, response.body().getSociallogin().getImage());
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.USER_ID, "" + response.body().getSociallogin().getId());
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.TOKEN, response.body().getSociallogin().getToken());
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.CRTEATED_AT, response.body().getSociallogin().getCreatedAt());
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.UPDATED_AT, response.body().getSociallogin().getUpdatedAt());
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
        SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS, true);
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.product_id,"");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.color_name,"");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.quantity,"");
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.size,"");
    }


    private void initCtrl() {
        // Toolbar
        binding.ivSearch.setOnClickListener(this);
        binding.ivNotification.setOnClickListener(this);
        binding.ivWishList.setOnClickListener(this);
        LoginManager.getInstance().registerCallback(callbackManager,this);
        binding.nestedScrollbar.getViewTreeObserver().addOnScrollChangedListener(this);
    }



    private void callHomeApi() {
        dailog.showDialog();
        Call<HomeResponseModel> call = apiInterface.getHomeResult(HelperClass.getCacheData(requireActivity()).second, HelperClass.getCacheData(requireActivity()).first);
        call.enqueue(new Callback<HomeResponseModel>() {
            public void onResponse(Call<HomeResponseModel> call, Response<HomeResponseModel> response) {
                    dailog.hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body());
                        else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.mainRl,response.body().getMessage(), requireActivity());
                    }
                    else CommonUtil.setUpSnackbarMessage(binding.mainRl,"Internal Server Error", requireActivity());
            }

            @Override
            public void onFailure(Call<HomeResponseModel> call, Throwable t) {
                dailog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.mainRl,t.getMessage(),requireActivity());
            }
        });
    }

    private void setDataToUI(HomeResponseModel model) {
        if(CommonUtil.isUserLogin(requireActivity())){
         int count=SharedPreferenceWriter.getInstance(requireActivity()).getInt(GlobalVariables.count);

         if(count>0) {
             tvBadge.setVisibility(View.VISIBLE);
             tvBadge.setText("" + count);
         }
        }else {
            if (model.getHomescreenapi().getNumOfAddtocart() > 0) {
                tvBadge.setVisibility(View.VISIBLE);
                tvBadge.setText("" + model.getHomescreenapi().getNumOfAddtocart());
            }
        }

        //     <------------ Main Category ---------------->
        List<ProductBean> categoryList = model.getHomescreenapi().getCatgories();
        categoryList.add(0, getSatvikLiveTab());
        if (!categoryList.isEmpty()) {
            binding.rvMainCategory.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
            binding.rvMainCategory.setAdapter(new MainCategoryAdapter(requireActivity(),categoryList));
        }

        //     <------------ Sub Category ---------------->
        List<CategoriesBeans> list=new ArrayList<>();
        list.add(new CategoriesBeans("Product of the week","flashsale",false,model.getHomescreenapi().getFlashSale()));
        list.add(new CategoriesBeans("Best Seller","Best Seller",true,model.getHomescreenapi().getBestSeller()));
        list.addAll(model.getHomescreenapi().getCategoriesWithProducts());
        binding.rvCategoryWiseData.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rvCategoryWiseData.setAdapter(new CategoriesAdapter(requireActivity(),list));



       //     <------------ Banner ---------------->
        if(!model.getHomescreenapi().getBanners().isEmpty())
        {
            binding.vpBanner.setAdapter(new AutoSlideViewPagerBannerAdapter(getActivity(), model.getHomescreenapi().getBanners()));
            binding.tlBanner.setupWithViewPager(binding.vpBanner, true);
//            sliderRunnable = new Runnable(){
//                @Override
//                public void run() {
//                    if (sliderPostion == model.getHomescreenapi().getBanners().size()-1) sliderPostion = 0; else sliderPostion++;
//                    binding.vpBanner.setCurrentItem(sliderPostion, true); }
//            };
//            new Timer().schedule(new TimerTask() { @Override public void run() { sliderHandler.post(sliderRunnable); }},2000,2000);
        }

        else binding.rlBanner.setVisibility(View.GONE);

    }

    private ProductBean getSatvikLiveTab() {
        return new ProductBean("","","Satvik Life","","","","","","","","","","","","","");
    }



    private Dialog openLoginSignUpBottomSheet() {
        dialog = new BottomSheetDialog(requireActivity());
        PopUpBottomSheetLoginWhenUserNotLogedInBinding binding= PopUpBottomSheetLoginWhenUserNotLogedInBinding.inflate(LayoutInflater.from(requireActivity()), null);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        binding.ivCross.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
        binding.ivFb.setOnClickListener(this);
        binding.ivGoogle.setOnClickListener(this);
        dialog.show();
        return dialog;
    }

    private void googleSignIn() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void facebookLogin() {
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RC_SIGN_IN: handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data)); break;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (showInternetAlert(getActivity())) callLoginApiForSocial(account.getGivenName(), account.getId(), account.getEmail(),account.getPhotoUrl().toString(),"Google");
        } catch (ApiException e) {
            Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivCross: dialog.dismiss(); break;
            case R.id.btnLogin: dialog.dismiss(); startActivity(new Intent(requireActivity(),LoginActivity.class)); break;
            case R.id.btnSignUp: dialog.dismiss(); startActivity(new Intent(requireActivity(),SignUpActivity.class)); break;
            case R.id.ivFb: dialog.dismiss(); facebookLogin(); break;
            case R.id.ivGoogle: dialog.dismiss(); googleSignIn(); break;
            case R.id.ivSearch: getActivity().startActivity(new Intent(getActivity(), SearchScreenActivity.class)); break;

            case R.id.ivNotification:
            if(CommonUtil.isUserLogin(requireActivity())) openLoginSignUpBottomSheet();
            else {
                SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue("DOT", false);
                binding.ivDot.setVisibility(View.GONE);
                getActivity().startActivity(new Intent(getActivity(), NotificationActivity.class));
            }
            break;

            case R.id.ivWishList:
            if(CommonUtil.isUserLogin(requireActivity())) openLoginSignUpBottomSheet();
            else getActivity().startActivity(new Intent(getActivity(), MyWishListActivity.class));
            break;
        }
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        if (showInternetAlert(getActivity())) callLoginApiForSocial(object.optString("name"), object.optString("id"), object.optString("email"),"https://graph.facebook.com/"+object.optString("id")+"/picture?type=large","Facebook");
    }

    @Override
    public void onScrollChanged() {

        if (binding.nestedScrollbar.getScrollY()>1000) {
            if (dialog == null || !dialog.isShowing()) {

                if (SharedPreferenceWriter.getInstance(requireActivity()).getBoolean("kIsFirstTime", false) && CommonUtil.isUserLogin(requireActivity())) {
                    dialog = openLoginSignUpBottomSheet();
                    SharedPreferenceWriter.getInstance(requireActivity()).writeBooleanValue("kIsFirstTime", false);
                    binding.nestedScrollbar.getViewTreeObserver().removeOnScrollChangedListener(this);
                }
            }
            binding.nestedScrollbar.getViewTreeObserver().removeOnScrollChangedListener(this);

        }
    }
}
