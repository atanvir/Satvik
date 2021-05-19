package com.satvick.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import com.bumptech.glide.Glide;
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
import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.activities.LoginActivity;
import com.satvick.activities.MainActivity;
import com.satvick.activities.MenActivity;
import com.satvick.activities.MyWishListActivity;
import com.satvick.activities.NotificationActivity;
import com.satvick.activities.ProductListActivity;
import com.satvick.activities.SearchScreenActivity;
import com.satvick.activities.SignUpActivity;
import com.satvick.adapters.AutoSlideViewPagerBannerAdapter;
import com.satvick.adapters.BrandInFocusAdapter;
import com.satvick.adapters.CategoriesAdapter;
import com.satvick.adapters.CategoryAdapter;
import com.satvick.adapters.FlashSaleAdapter;
import com.satvick.adapters.GymAdapter;
import com.satvick.adapters.HandPickedAdapter;
import com.satvick.adapters.MainCategoriesAdapter;
import com.satvick.adapters.HotDealAdapter;
import com.satvick.adapters.MainCategoryAdapter;
import com.satvick.adapters.ShopByThemeAdapter;
import com.satvick.adapters.ShopCollectionAdapter;
import com.satvick.adapters.StyleFeedAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.FragmentHomeAfterLoginBinding;
import com.satvick.fcm.MyFirebaseMessagingService;
import com.satvick.model.CategoriesBeans;
import com.satvick.model.CategoryItemResponse;
import com.satvick.model.HomeModel;
import com.satvick.model.HomeResponseModel;
import com.satvick.model.HomeSlidingProductResponse;
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

public class HomeFragment extends Fragment implements View.OnClickListener, MyFirebaseMessagingService.ShowDot, FacebookCallback<LoginResult> {

    private int lay_height = 0;
    int height = 0;
    int sc = 0;
    boolean isDialogOpen = true;
    boolean isFirstTime;

    private Dialog dialog;

    SharedPreferences sharedpreferences;
    private String token = "";
    private String userId = "";


    boolean isChecked = true;
    private String commaSeparatedProductId = "";

    CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 901;
    private  FragmentHomeAfterLoginBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_home_after_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        initCtrl();
        if(showInternetAlert(getActivity())) callHomeApi(binding.mainRl);
    }

    private void init() {
        // Facebook
        callbackManager = CallbackManager.Factory.create();

        // Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        // UI
        sharedpreferences = getActivity().getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);
        isFirstTime = sharedpreferences.getBoolean("kIsFirstTime", true);//Retrieve a boolean value from the preferences.
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("kIsFirstTime", false);
        editor.apply();
        MyFirebaseMessagingService.setListner(HomeFragment.this);
        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean("DOT", false)) binding.ivDot.setVisibility(View.VISIBLE);
        if (isFirstTime) loginBottomSheet();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        commaSeparatedProductId = SharedPreferenceWriter.getInstance(getActivity()).getString("Ids");
        if (SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") || SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
            token = "1";
            userId = "1";
        } else {
            token = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.TOKEN);
            userId = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.USER_ID);
        }
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
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        String name = object.optString("name");
                        String fbid = object.optString("id");
                        String email = object.optString("email");
                        String profilePhoto = "https://graph.facebook.com/"+fbid+"/picture?type=large";

                        if (showInternetAlert(getActivity())) {
                            callLoginApiForSocial(name, fbid, email,profilePhoto,"Facebook");
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void callLoginApiForSocial(final String name, final String fbid, final String email,final String profilePhoto,final String socialType) {
        String deviceToken = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.DEVICE_TOKEN);
        final MyDialog myDialog=new MyDialog(getActivity());
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<SocialLoginModel> call = apiInterface.socialLogin(fbid, socialType,deviceToken,"android", name, email,profilePhoto,"",
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.product_id),
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.color_name),
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.quantity),
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.size));
        call.enqueue(new Callback<SocialLoginModel>() {
            @Override
            public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        SharedPreferenceWriter.getInstance(getActivity()).getString(commaSeparatedProductId, "");
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

                        Intent intent = new Intent(getActivity(), MainActivity.class).putExtra("from", "LoginButton");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        CommonUtil.setUpSnackbarMessage(binding.mainRl,response.body().getMessage(),getActivity());
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.service_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }


    private void initCtrl() {
        // Toolbar
        binding.ivSearch.setOnClickListener(this);
        binding.ivNotification.setOnClickListener(this);
        binding.ivWishList.setOnClickListener(this);
        LoginManager.getInstance().registerCallback(callbackManager,this);
    }



    private void callHomeApi(final View view) {
        final MyDialog myDialog=new MyDialog(getActivity());
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<HomeResponseModel> call = apiInterface.getHomeResult(userId, token);
        call.enqueue(new Callback<HomeResponseModel>() {
            public void onResponse(Call<HomeResponseModel> call, Response<HomeResponseModel> response) {
                if(getActivity()!=null) {
                    myDialog.hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body());
                        else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.mainRl,response.body().getMessage(), requireActivity());
                    }
                    else CommonUtil.setUpSnackbarMessage(binding.mainRl,response.body().getMessage(), requireActivity());

                }
            }

            @Override
            public void onFailure(Call<HomeResponseModel> call, Throwable t) {
                myDialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.mainRl,t.getMessage(),requireActivity());
            }
        });
    }

    private void setDataToUI(HomeResponseModel model) {

     //     <------------ Main Category ---------------->
        if(!model.getHomescreenapi().getCatgories().isEmpty()) {
        binding.rvMainCategory.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false));
        binding.rvMainCategory.setAdapter(new MainCategoryAdapter(requireActivity(),model.getHomescreenapi().getCatgories()));
        }

     //     <------------ Sub Category ---------------->
        List<CategoriesBeans> list=new ArrayList<>();
        list.add(new CategoriesBeans("Product of the week","flashsale",false,model.getHomescreenapi().getFlashSale()));
        list.add(new CategoriesBeans("Best Seller","Best Seller",true,model.getHomescreenapi().getBestSeller()));
        list.addAll(model.getHomescreenapi().getCategoriesWithProducts());
        binding.rvCategoryWiseData.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rvCategoryWiseData.setAdapter(new CategoriesAdapter(requireActivity(),list));



    //     <------------ Top Banner ---------------->
        if(!model.getHomescreenapi().getBanners().isEmpty())
        {
            binding.vpBanner.setAdapter(new AutoSlideViewPagerBannerAdapter(getActivity(), model.getHomescreenapi().getBanners()));
            binding.tlBanner.setupWithViewPager(binding.vpBanner, true);
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new SliderTimerBanner(), 2000, 3000);
        }
        else binding.rlBanner.setVisibility(View.GONE);

    }




    private void loginBottomSheet() {
        binding.nestedScrollbar.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        int scrollX = binding.nestedScrollbar.getScrollX(); //for horizontalScrollView
                        int scrollY = binding.nestedScrollbar.getScrollY(); //for verticalScrollView
                        sc = scrollY + height;
                        Log.v("bottom", lay_height + "  Y=" + sc + "  " + scrollY + "   " + height);
                        if (sc>1000) {
                           if(dialog==null||!dialog.isShowing()) {
                               if(isDialogOpen)
                               {
                                   dialog = openLoginSignUpBottomSheet();
                                   isDialogOpen=false;
                               }
                           }
                        }
                    }
                });
            }
        });

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("kIsFirstTime", false);
        editor.apply();
    }


    private Dialog openLoginSignUpBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        View view1 = this.getLayoutInflater().inflate(R.layout.pop_up_bottom_sheet_login_when_user_not_loged_in, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);

        ImageView ivCross = view1.findViewById(R.id.ivCross);
        Button loginButton = view1.findViewById(R.id.btnLogin);
        Button signUpButton = view1.findViewById(R.id.btnSignUp);
        ImageView ivFb = view1.findViewById(R.id.ivFb);
        ImageView ivGoogle = view1.findViewById(R.id.ivGoogle);
        final CheckBox chbSelect = view1.findViewById(R.id.chbSelect);


        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                isDialogOpen=false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignUpActivity.class));
            }
        });

        ivFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLogin();
                bottomSheetDialog.dismiss();
            }
        });


        ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
                bottomSheetDialog.dismiss();
            }
        });


        chbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isChecked){
                    isChecked=false;
                    chbSelect.setButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sale_box));

                }else {
                    isChecked=true;
                    chbSelect.setButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.new_rect));
                }
            }
        });

        bottomSheetDialog.show();

        return bottomSheetDialog;
    }

    private void googleSignIn() {
        mGoogleSignInClient.signOut(); //log out
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);// Activity is started with requestCode RC_SIGN_IN which is 901
    }

    private void facebookLogin() {
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        //it will not take cache Email
        LoginManager.getInstance().logOut(); //log out
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Finally, in your onActivityResult method, call callbackManager.
        //onActivityResult to pass the login results to the LoginManager via callbackManager.
        callbackManager.onActivityResult(requestCode, resultCode, data);//for facebook
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //for camera intent
            if (requestCode == 1) {
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Log.e("data",bitmap.toString());
                } catch (Exception e) {
                }
            }
        }catch (Exception e)
        {

        }

        // for google
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }



    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String userProfile = account.getPhotoUrl().toString();
            String email = account.getEmail();
            String gid = account.getId();
            String name = account.getGivenName();

            if (showInternetAlert(getActivity())) {
                callLoginApiForSocial(name, gid, email,userProfile,"Google");
            }

            // Signed in successfully,show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private class SliderTimerBanner extends TimerTask {

        @Override
        public void run() {
            if (getActivity() == null)
                return;
                  getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (binding.vpBanner.getCurrentItem() < binding.vpBanner.getAdapter().getCount() - 1) {
                        binding.vpBanner.setCurrentItem(binding.vpBanner.getCurrentItem() + 1);
                    } else {
                        binding.vpBanner.setCurrentItem(0);
                    }
                }
            });
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSearch: getActivity().startActivity(new Intent(getActivity(), SearchScreenActivity.class)); break;

            case R.id.ivNotification:
                SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue("DOT", false);
                binding.ivDot.setVisibility(View.GONE);
                getActivity().startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;

            case R.id.ivWishList:
                if (SharedPreferenceWriter.getInstance(getActivity()).
                        getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false")||
                        SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {

                    openLoginSignUpBottomSheet();
                } else {
                    getActivity().startActivity(new Intent(getActivity(), MyWishListActivity.class));
                }
                break;

        }
    }



}
