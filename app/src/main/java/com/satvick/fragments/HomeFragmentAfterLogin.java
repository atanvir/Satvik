package com.satvick.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
import com.satvick.adapters.CategoryAdapter;
import com.satvick.adapters.FlashSaleAdapter;
import com.satvick.adapters.GymAdapter;
import com.satvick.adapters.HandPickedAdapter;
import com.satvick.adapters.HomeSlidingProductAdapter;
import com.satvick.adapters.HotDealAdapter;
import com.satvick.adapters.MainCategoryAdapter;
import com.satvick.adapters.ShopByThemeAdapter;
import com.satvick.adapters.ShopCollectionAdapter;
import com.satvick.adapters.StyleFeedAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.FragmentHomeAfterLoginBinding;
import com.satvick.fcm.MyFirebaseMessagingService;
import com.satvick.model.CategoryItemResponse;
import com.satvick.model.HomeModel;
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

public class HomeFragmentAfterLogin extends Fragment implements View.OnClickListener, MyFirebaseMessagingService.ShowDot {
    View rootView;
    FragmentHomeAfterLoginBinding binding;
    List<HomeSlidingProductResponse> homeSlidingProductResponseList = new ArrayList<>();
    List<CategoryItemResponse> categoryList = new ArrayList<>();

    HomeSlidingProductAdapter homeSlidingProductAdapter;
    CategoryAdapter categoryAdapter;
    HotDealAdapter hotDealAdapter;
    FlashSaleAdapter FlashSaleAdapter;
    StyleFeedAdapter styleFeedAdapter;
    BrandInFocusAdapter brandInFocusAdapter;
    GymAdapter gymAdapter;
    HandPickedAdapter handPickedAdapter;
    ShopByThemeAdapter shopByThemeAdapter;
    ShopCollectionAdapter shopCollectionAdapter;
    private int lay_height = 0;
    int height = 0;
    int sc = 0;
    boolean isDialogOpen = true;
    boolean isFirstTime;

    private Dialog dialog;

    SharedPreferences sharedpreferences;
    private String token = "";
    private String userId = "";

    List<HomeModel.Stylefeed> stylefeedList = new ArrayList<>();
    List<HomeModel.Brandinfocu> brandinfocusList = new ArrayList<>();
    List<HomeModel.Handpicked> handpickedList = new ArrayList<>();
    List<HomeModel.Banner1> banner1List = new ArrayList<>();
    List<HomeModel.Banner2> banner2List = new ArrayList<>();
    List<HomeModel.Slider1> slider1List = new ArrayList<>();
    List<HomeModel.Slider2> slider2List = new ArrayList<>();
    List<HomeModel.Banner> bannersList = new ArrayList<>();
    List<HomeModel.Themedetail> themeDetailsList = new ArrayList<>();
    List<HomeModel.FlashSale> flashSaleList = new ArrayList<>();
    List<HomeModel.Hotdeal> hotDealList = new ArrayList<>();
    List<HomeModel.AnnouncementBar> announcementBarList = new ArrayList<>();

    boolean isChecked = true;
    private String commaSeparatedProductId = "";

    CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 901;
    int tempMin ;
    int tempSec=60;
    int hours=0;
    boolean ishours=false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_after_login, container, false);
        rootView = binding.getRoot();
        MyFirebaseMessagingService.setListner(HomeFragmentAfterLogin.this);
        if(SharedPreferenceWriter.getInstance(getActivity()).getBoolean("DOT", false))
        {
            binding.ivDot.setVisibility(View.VISIBLE);
        }
        viewClickListener();
        setHomeSliderAdapter();
        sharedpreferences = getActivity().getSharedPreferences("kAppPreferences", Context.MODE_PRIVATE);
        isFirstTime = sharedpreferences.getBoolean("kIsFirstTime", true);//Retrieve a boolean value from the preferences.
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("kIsFirstTime", false);
        editor.apply();

        if (isFirstTime) {
            callLoginSignupBottomSheetMethod();
        }
        commaSeparatedProductId = SharedPreferenceWriter.getInstance(getActivity()).getString("Ids");
        //[fb register]
        fbRegisterCallBack();

        //[google register]
        googleRegister();

        if (SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
            SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase(""))
        {
            token = "1";
            userId = "1";
        }
        else {
            token = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.TOKEN);
            userId = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.USER_ID);
        }

        if (HelperClass.showInternetAlert(getActivity())) {
            callHomeApi(binding.mainRl);//hit api
        }

        return rootView;

    }

    @Override
    public void show() {
        SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue("DOT", true);
        binding.ivDot.setVisibility(View.VISIBLE);
    }


    public class BoundedClass<T>
    {
        public void onClickListner(T obj)
        {
            Log.e("obj",obj.getClass().getSimpleName());
            if(obj instanceof HomeModel.Man)
            {
                callingIntent("HomeFragmentAfterLogin", String.valueOf(((HomeModel.Man) obj).getId()),"",((HomeModel.Man) obj).getName());
            }
            else if(obj instanceof HomeModel.Woman)
            {
                callingIntent("HomeFragmentAfterLogin", String.valueOf(((HomeModel.Woman) obj).getId()),"",((HomeModel.Woman) obj).getName());
            }
            else if(obj instanceof HomeModel.Kid)
            {
                callingIntent("HomeFragmentAfterLogin", String.valueOf(((HomeModel.Kid) obj).getId()),"",((HomeModel.Kid) obj).getName());
            }
        }


    }

    private void callingIntent(String from, String subcatid,String filterType,String category_name) {
        Intent intent=new Intent(getActivity(),ProductListActivity.class);
        intent.putExtra("from",from);
        intent.putExtra(GlobalVariables.subcatid,subcatid);
        intent.putExtra(GlobalVariables.subcatname,category_name);
        getActivity().startActivity(intent);
    }


    private void googleRegister() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void fbRegisterCallBack() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getBasicProfile(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), R.string.cancel, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                    }
                });
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

                        if (HelperClass.showInternetAlert(getActivity())) {
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


    private void viewClickListener() {
        binding.ivSearch.setOnClickListener(this);
        binding.ivNotification.setOnClickListener(this);
        binding.ivWishList.setOnClickListener(this);
        binding.categoryRecyclerView.setOnClickListener(this);
        binding.hotDealRecyclerView.setOnClickListener(this);
        binding.flashSaleRecyclerView.setOnClickListener(this);
        binding.styleFeedRecyclerView.setOnClickListener(this);
        binding.brandInFocusRecyclerView.setOnClickListener(this);
        binding.gymRecyclerView.setOnClickListener(this);
        binding.handPickedRecyclerView.setOnClickListener(this);
        binding.shopByThemeRecyclerView.setOnClickListener(this);
        binding.nestedScrollbar.setOnClickListener(this);
        binding.ivBanner2.setOnClickListener(this);
        binding.ivBanner3.setOnClickListener(this);
        binding.rlExclusive.setOnClickListener(this);
//        binding.rlAppExclusive.setOnClickListener(this);
        binding.image1.setOnClickListener(this);
        binding.image2.setOnClickListener(this);
        binding.image3.setOnClickListener(this);
        binding.ll1ShopAll.setOnClickListener(this);
        binding.ll2ShopAll.setOnClickListener(this);
        binding.ll3ShopAll.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void callHomeApi(final View view) {
        final MyDialog myDialog=new MyDialog(getActivity());
        myDialog.showDialog();


        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<HomeModel> call = apiInterface.getHomeResult(userId, token);
        call.enqueue(new Callback<HomeModel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {

                 if(getActivity()!=null) {
                     myDialog.hideDialog();
                 }

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {

                        settingArraylists(response.body());

//                        <--------------------------- Announcement Bar ------------------------->
                        if(announcementBarList.size()>0)
                        {
                        binding.exclusiveOffer.setText(announcementBarList.get(0).getContent());
                            if(announcementBarList.size()>1) {
                                binding.tvAppExclusiveOffer.setText(announcementBarList.get(1).getContent());
                            }
                            else
                            {
                                binding.tvAppExclusiveOffer.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            binding.exclusiveOffer.setVisibility(View.GONE);
                        }

                        settingRecyclerView(response.body());
                    }


                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(getActivity(),R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callHomeApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                    return;
                }
            }

            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
                Log.e("errrrrr:---------->",t.getMessage());
                myDialog.hideDialog();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void settingRecyclerView(HomeModel data) {

//     <----------------- Main Categories ------------------------>
        setMainCategory(data.getHomeapi().getMen());



//     <----------------- Top Banner ------------------------>
        setViewPagerBannerAdapter();

//     <----------------- Featured Products ------------------------>
        setFeaturedProducts(data.getHomeapi().getFeatured());


//     <----------------- Satvik Category ------------------------>
        setCategory(data.getHomeapi().getWomen());

//
////     <----------------- SubCategory Section --------------->
//        if(getActivity()!=null) {
//            setSubCategoryTextView(data.getHomeapi());
//        }
//
////     <----------------- HotDeal Section ------------------->
//        setHotDealRecyclerView();

//     <----------------- FlashSale Section ----------------->
        setFlashSaleRecyclerView();
        String flashTime=String.valueOf(data.getHomeapi().getFlashSaleTime());
        if(!flashTime.equalsIgnoreCase("")){
            displaySplashTime(flashTime);
        }

//
////     <----------------- StyleFeed Section ----------------->
//        setStyleFeedRecyclerView();
//
////     <----------------- BrandInFocus Section -------------->
//        setBrandInFocusRecyclerView();
//
////     <----------------- Banner Section 1 ------------------>
//        if(getActivity()!=null)
//        {
//            setBanner1();
//        }
//
////     <----------------- GymWear's Section ----------------->
//        setGymRecyclerView();
//
////     <----------------- Banner Section 1 ------------------>
//        if(getActivity()!=null)
//        {
//            setBanner2();
//        }
//
////     <----------------- ShopCollection Section------------>
//        setShopCollectionRecyclerview();
//
////     <----------------- HandPicked Section --------------->
//        setHandPickedRecyclerView();
//
////     <----------------- ShopByTheme Section -------------->
//        setShopByThemeRecyclerView();
//
    }

    private void setMainCategory(List<HomeModel.Man> data) {
        if(data!=null) {
            if(data.size()>0) {
                binding.recyclerView.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                binding.recyclerView.setLayoutManager(linearLayoutManager);
                binding.recyclerView.setAdapter(new MainCategoryAdapter(getActivity(), data));
            }else{
                binding.recyclerView.setVisibility(View.GONE);

            }
        }else{
            binding.recyclerView.setVisibility(View.GONE);

        }

    }

    private void setCategory(List<HomeModel.Woman> data) {
        if(data!=null) {
            if(data.size()>0) {

                binding.tvCategory.setVisibility(View.VISIBLE);
LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                binding.hotDealRecyclerView.setLayoutManager(linearLayoutManager);
                binding.hotDealRecyclerView.setAdapter(new HomeSlidingProductAdapter(getActivity(), data));
            }else{
                binding.hotDealRecyclerView.setVisibility(View.GONE);
                binding.tvCategory.setVisibility(View.GONE);
            }
        }else{
            binding.hotDealRecyclerView.setVisibility(View.GONE);
            binding.tvCategory.setVisibility(View.GONE);
        }

    }

    private void setFeaturedProducts(List<HomeModel.FlashSale> data) {
        if(data!=null){
            if(data.size()>0){

                Log.e("aaya ","yes nnanananananananan");
                binding.tvFeatured.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                binding.categoryRecyclerView.setLayoutManager(linearLayoutManager);
                binding.categoryRecyclerView.setAdapter(new FlashSaleAdapter(getActivity(), data));
            }else{
                binding.tvFeatured.setVisibility(View.GONE);
                binding.categoryRecyclerView.setVisibility(View.GONE);
            }
        }else{
            binding.tvFeatured.setVisibility(View.GONE);
            binding.categoryRecyclerView.setVisibility(View.GONE);
        }
    }

    private void settingArraylists(HomeModel data) {
//        announcementBarList=data.getHomeapi().getAnnouncementBar();
        bannersList = data.getHomeapi().getBanners();
//        stylefeedList = data.getHomeapi().getStylefeed();
//        brandinfocusList = data.getHomeapi().getBrandinfocus();
//        handpickedList = data.getHomeapi().getHandpicked();
//        banner1List = data.getHomeapi().getBanner1();
//        banner2List = data.getHomeapi().getBanner2();
//        slider1List = data.getHomeapi().getSlider1();
//        slider2List = data.getHomeapi().getSlider2();
//        themeDetailsList = data.getHomeapi().getThemedetails();
        flashSaleList = data.getHomeapi().getFlashSale();
//        hotDealList = data.getHomeapi().getHotdeal();
    }

    private void setSubCategoryTextView(final HomeModel.Homeapi response) {
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");
        for (int i = 0; i < 5; i++) {
            //men
            TextView textView = new TextView(getActivity());
            textView.setSingleLine();
            textView.setPadding(5, 10, 5, 10);
            textView.setTextSize(11);
            textView.setTypeface(face);
            if (response.getMen().size() > i) {
                Log.e("count", String.valueOf(i));
                textView.setText(response.getMen().get(i).getName());
            }
            final int finalI = i;
            final TextView finalTextView = textView;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!finalTextView.getText().toString().equalsIgnoreCase("")) {
                        new BoundedClass<>().onClickListner(response.getMen().get(finalI));
                    }
                }
            });
            binding.ll1Inner.addView(textView);

        }
        for (int i = 0; i < 5; i++) {

            TextView textView = new TextView(getActivity());
            textView.setSingleLine();
            textView.setPadding(5, 10, 5, 10);
            textView.setTextSize(11);
            textView.setTypeface(face);
            if (response.getWomen().size() > i) {
                Log.e("count", String.valueOf(i));
                textView.setText(response.getWomen().get(i).getName());
            }
            final int finalI = i;
            final TextView finalTextView1 = textView;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!finalTextView1.getText().toString().equalsIgnoreCase("")) {
                        new BoundedClass<>().onClickListner(response.getWomen().get(finalI));
                    }
                }
            });
            binding.ll2Inner.addView(textView);
        }
        for (int i = 0; i < 5; i++) {

            TextView textView = new TextView(getActivity());
            textView.setSingleLine();
            textView.setPadding(5, 10, 5, 10);
            textView.setTextSize(11);
            textView.setTypeface(face);
            if (response.getKids().size() > i) {
                Log.e("count", String.valueOf(i));
                textView.setText(response.getKids().get(i).getName());
            }
            final int finalI = i;
            final TextView finalTextView2 = textView;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!finalTextView2.getText().toString().equalsIgnoreCase("")) {
                        new BoundedClass<>().onClickListner(response.getKids().get(finalI));
                    }
                }
            });
            binding.ll3Inner.addView(textView);
        }
    }

    private void setBanner1() {
        if(banner1List.size()>0 && banner1List!=null)
        {
            Glide.with(getActivity()).load(banner1List.get(0).getImage()).into(binding.ivBanner2);
        }
        else
        {
            binding.ivBanner2.setVisibility(View.GONE);

        }
    }

    private void setBanner2() {
        if(banner2List.size()>0 && banner2List!=null)
        {

            Glide.with(getActivity()).load(banner2List.get(0).getImage()).into(binding.ivBanner3);
        }
        else {
            binding.ivBanner3.setVisibility(View.GONE);
        }

    }


    private void callLoginSignupBottomSheetMethod() {

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

            if (HelperClass.showInternetAlert(getActivity())) {
                callLoginApiForSocial(name, gid, email,userProfile,"Google");
            }

            // Signed in successfully,show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void setViewPagerBannerAdapter() {
        if(bannersList.size()>0 && bannersList!=null)
        {
            binding.viewPagerr.setAdapter(new AutoSlideViewPagerBannerAdapter(getActivity(), bannersList));
            binding.indicator.setupWithViewPager(binding.viewPagerr, true);
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new SliderTimerBanner(), 2000, 3000);
        }
        else
        {

            binding.rlViewPager.setVisibility(View.GONE);
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
                    if (binding.viewPagerr.getCurrentItem() < bannersList.size() - 1) {
                        binding.viewPagerr.setCurrentItem(binding.viewPagerr.getCurrentItem() + 1);
                    } else {
                        binding.viewPagerr.setCurrentItem(0);
                    }
                }
            });
        }
    }


    private void setShopCollectionRecyclerview() {
        if(slider2List.size()>0 && slider2List!=null) {
            shopCollectionAdapter = new ShopCollectionAdapter(getActivity(), slider2List);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.shopCollectionRecyclerView.setLayoutManager(linearLayoutManager);
            binding.shopCollectionRecyclerView.setAdapter(shopCollectionAdapter);
        }
        else
        {
            binding.slider2ll.setVisibility(View.GONE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displaySplashTime(String flashtime) {
        ishours=false;

        new CountDownTimer(Long.parseLong(flashtime), 1000) {
            public void onTick(long millisUntilFinished) {
                if(!ishours)
                {
                    hours = (int) ((millisUntilFinished /  (3600)));
                    Log.e("hours:",""+hours);
                    ishours=true;
                    long hours = millisUntilFinished / 3600;
                    long secondsLeft = millisUntilFinished - hours * 3600;
                    long minutes = secondsLeft / 60;
                    long seconds = secondsLeft - minutes * 60;
                    tempMin=(int)minutes;
                    tempSec= (int) seconds;

                }
                binding.hourtxt.setText("" + hours);
                binding.mintxt.setText("" + tempMin);
                binding.sectxt.setText("" + tempSec);

                tempSec--;
                if(tempSec==1)
                {
                    tempMin--;
                    tempSec=60;
                }

                if(tempMin<=1)
                    tempMin=60;

            }

            public void onFinish() {
                binding.countdownLL.setVisibility(View.GONE);
                binding.flashSalell.setVisibility(View.GONE);
                binding.flashSaleRecyclerView.setVisibility(View.GONE);

            }
        }.start();
    }

    private void setHomeSliderAdapter() {
//        homeSlidingProductResponseList.add(new HomeSlidingProductResponse("Organic", R.drawable.image_a));
//        homeSlidingProductResponseList.add(new HomeSlidingProductResponse("Ayurvedic", R.drawable.image_b));
//        homeSlidingProductResponseList.add(new HomeSlidingProductResponse("Home Made & Natural", R.drawable.image_c));
//        homeSlidingProductAdapter = new HomeSlidingProductAdapter(getActivity(), homeSlidingProductResponseList);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        binding.recyclerView.setLayoutManager(linearLayoutManager);
//        binding.recyclerView.setAdapter(homeSlidingProductAdapter);
//
//        homeSlidingProductAdapter.setSelectClassroomListClickListener(new HomeSlidingProductAdapter.OnSlidingImageListItemClickListener() {
//            @Override
//            public void onItemClick(View view, int pos) {
//                String section_name="";
//                if (pos == 0) {
//                    section_name="ORGANIC";
//                    startActivity(new Intent(getActivity(), MenActivity.class)
//                            .putExtra("from", "HomeFragmentAfterLoginMen")
//                            .putExtra("filterData", section_name)
//                            .putExtra("type","Category"));
//
//                   // getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//
//                } else if (pos == 1) {
//                    section_name="AYURVEDIC";
//                    startActivity(new Intent(getActivity(), MenActivity.class)
//                            .putExtra("from", "HomeFragmentAfterLoginWomen")
//                            .putExtra("filterData", section_name)
//                            .putExtra("type","Category"));
//
//                  //  getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//
//                } else if (pos == 2) {
//                    section_name="HOME MADE";
//                    startActivity(new Intent(getActivity(), MenActivity.class)
//                            .putExtra("from", "HomeFragmentAfterLoginWomen")
//                            .putExtra("filterData", section_name)
//                            .putExtra("type","Category"));
//
//                } else if (pos == 3) {
//                    section_name="ACCESSORIES";
//                    startActivity(new Intent(getActivity(), MenActivity.class)
//                            .putExtra("from", "HomeFragmentAfterLoginWomen")
//                            .putExtra("filterData", section_name)
//                            .putExtra("type","Category"));
//
//                } else if (pos == 4) {
//                    section_name="ESSENTIAL";
//                    startActivity(new Intent(getActivity(), MenActivity.class)
//                            .putExtra("from", "HomeFragmentAfterLoginWomen")
//                            .putExtra("filterData", section_name)
//                            .putExtra("type","Category"));
//
//                } else if (pos == 5) {
//                    section_name="OTHERS";
//                    startActivity(new Intent(getActivity(), MenActivity.class)
//                            .putExtra("from", "HomeFragmentAfterLoginWomen")
//                            .putExtra("filterData", section_name)
//                            .putExtra("type","Category"));
//                }
//
//                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.section_name,section_name);
//
//            }
//        });
    }

    private void setHotDealRecyclerView() {
        if(hotDealList.size()>0 && hotDealList!=null)
        {
            hotDealAdapter = new HotDealAdapter(getActivity(), hotDealList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.hotDealRecyclerView.setLayoutManager(linearLayoutManager);
            binding.hotDealRecyclerView.setAdapter(hotDealAdapter);
        }
        else
        {
            binding.styleFeedll.setVisibility(View.GONE);
        }
    }

    private void setFlashSaleRecyclerView() {
        if(flashSaleList.size()>0 && flashSaleList!=null) {

            binding.flashSalell.setVisibility(View.VISIBLE);
            FlashSaleAdapter = new FlashSaleAdapter(getActivity(), flashSaleList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.flashSaleRecyclerView.setLayoutManager(linearLayoutManager);
            binding.flashSaleRecyclerView.setAdapter(FlashSaleAdapter);

        }else
        {
            binding.flashSalell.setVisibility(View.GONE);
        }
    }

    private void setStyleFeedRecyclerView() {

        if (stylefeedList != null && stylefeedList.size() > 0) {

            styleFeedAdapter = new StyleFeedAdapter(getActivity(), stylefeedList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.styleFeedRecyclerView.setLayoutManager(linearLayoutManager);
            binding.styleFeedRecyclerView.setAdapter(styleFeedAdapter);
        }

    }

    private void setBrandInFocusRecyclerView() {

        if (brandinfocusList != null && brandinfocusList.size() > 0) {

            brandInFocusAdapter = new BrandInFocusAdapter(getActivity(), brandinfocusList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.brandInFocusRecyclerView.setLayoutManager(linearLayoutManager);
            binding.brandInFocusRecyclerView.setAdapter(brandInFocusAdapter);
        }
        else {
            binding.brandInFocusLL.setVisibility(View.GONE);
        }
    }

    private void setGymRecyclerView() {

        if (slider1List != null && slider1List.size() > 0) {

            gymAdapter = new GymAdapter(getActivity(), slider1List);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.gymRecyclerView.setLayoutManager(linearLayoutManager);
            binding.gymRecyclerView.setAdapter(gymAdapter);
        }
        else
        {
            binding.shopCollectionRecyclerView.setVisibility(View.GONE);
        }
    }

    private void setHandPickedRecyclerView() {

        if (handpickedList != null && handpickedList.size() > 0) {
            handPickedAdapter = new HandPickedAdapter(getActivity(), handpickedList);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            binding.handPickedRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
            binding.handPickedRecyclerView.setAdapter(handPickedAdapter);
        }
    }

    private void setShopByThemeRecyclerView() {

        if (themeDetailsList != null && themeDetailsList.size() > 0) {
            shopByThemeAdapter = new ShopByThemeAdapter(getActivity(), themeDetailsList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.shopByThemeRecyclerView.setLayoutManager(linearLayoutManager);
            binding.shopByThemeRecyclerView.setAdapter(shopByThemeAdapter);
        }

        else {
            binding.shopByThemeLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSearch:
                getActivity().startActivity(new Intent(getActivity(), SearchScreenActivity.class));
                break;

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


            case R.id.ivBanner2:
                Intent intent2=new Intent(getActivity(),ProductListActivity.class);
                intent2.putExtra("from","HomeFragmentAfterLogin");
                intent2.putExtra(GlobalVariables.subcatid,banner1List.get(0).getFilterData());
                intent2.putExtra(GlobalVariables.subcatname,"Items");
                getActivity().startActivity(intent2);
                break;

            case R.id.ivBanner3:
                Intent intent3=new Intent(getActivity(),ProductListActivity.class);
                intent3.putExtra("from","HomeFragmentAfterLogin");
                intent3.putExtra(GlobalVariables.subcatid,banner2List.get(0).getFilterData());
                intent3.putExtra(GlobalVariables.subcatname,"Items");
                getActivity().startActivity(intent3);
                break;

            case R.id.rl1:

                //getActivity().startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra("from", "HomeFragmentAfterLogin"));
                break;

            case R.id.rl2:

                //getActivity().startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra("from", "HomeFragmentAfterLogin"));
                break;

            case R.id.image1:
                callingIntent2("HomeFragmentAfterLogin","ORGANIC","Category");
                break;


            case R.id.image2:
                callingIntent2("HomeFragmentAfterLogin","AYURVEDIC","Category");
                break;


            case R.id.image3:
                callingIntent2("HomeFragmentAfterLogin","HOME MADE AND NATURAL","Category");
                break;

            case R.id.ll1Inner:


                getActivity().startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra("from", "HomeFragmentAfterLogin"));
                break;

            case R.id.ll2Inner:
                getActivity().startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra("from", "HomeFragmentAfterLogin"));
                break;

            case R.id.ll3Inner:
                getActivity().startActivity(new Intent(getActivity(), ProductListActivity.class).putExtra("from", "HomeFragmentAfterLogin"));
                break;

            case R.id.ll1ShopAll:
                callingIntent2("HomeFragmentAfterLogin","ORGANIC","Category");
                break;

            case R.id.ll2ShopAll:
                callingIntent2("HomeFragmentAfterLogin","AYURVEDIC","Category");
                break;

            case R.id.ll3ShopAll:
                callingIntent2("HomeFragmentAfterLogin","HOME MADE AND NATURAL","Category");
                break;

            case R.id.rlExclusive:
            Intent rlOfferIntent=new Intent(getActivity(),ProductListActivity.class);
            rlOfferIntent.putExtra("from","HomeFragmentAfterLogin");
            rlOfferIntent.putExtra(GlobalVariables.subcatid,""+announcementBarList.get(0).getFilterData());
            rlOfferIntent.putExtra(GlobalVariables.subcatname,"Exclusive Offer");
            getActivity().startActivity(rlOfferIntent);
            break;

            case R.id.rlAppExclusive:
            Intent rlAppExclusiveIntent=new Intent(getActivity(),ProductListActivity.class);
            rlAppExclusiveIntent.putExtra("from","HomeFragmentAfterLogin");
            rlAppExclusiveIntent.putExtra(GlobalVariables.subcatid,""+announcementBarList.get(1).getFilterData());
            rlAppExclusiveIntent.putExtra(GlobalVariables.subcatname,announcementBarList.get(1).getContent().split("-")[0]);
            getActivity().startActivity(rlAppExclusiveIntent);
            break;


        }
    }

    private void callingIntent2(String from, String filterData, String type) {
        Intent intent=new Intent(getActivity(),MenActivity.class);
        intent.putExtra("from",from);
        intent.putExtra("filterData", filterData);
        intent.putExtra("type",type);
        startActivity(intent);

    }




}
