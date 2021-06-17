package com.satvick.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.satvick.R;
import com.satvick.adapters.ProductDescriptionRecycler1Adapter;
import com.satvick.adapters.ProductDescriptionRecycler2Adapter;
import com.satvick.adapters.SelectSizeProductDetailsAdapter;
import com.satvick.adapters.SliderAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityProductDetailBinding;
import com.satvick.databinding.PopUpBottomSheetLoginWhenUserNotLogedInBinding;
import com.satvick.model.CartListModel2;
import com.satvick.model.CommonModel;
import com.satvick.model.MyWishListResponse;
import com.satvick.model.ProductDetails;
import com.satvick.model.ProductDetailsResponse;
import com.satvick.model.SignUpModel;
import com.satvick.model.SocialLoginModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.GuestUserData;
import com.satvick.utils.HelperClass;
import org.json.JSONObject;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener, ViewTreeObserver.OnScrollChangedListener, SelectSizeProductDetailsAdapter.SelectSizeListener, FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback, AdapterView.OnItemClickListener {

    private ActivityProductDetailBinding binding;
    private MyDialog dailog ;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private Runnable sliderRunnable;
    private Handler sliderHandler=new Handler(Looper.myLooper());
    private List<String> selectedMenuList = new ArrayList<>(),color,sizeList;
    private Double convertedAmout;
    private CallbackManager callbackManager;
    private BottomSheetDialog bottomSheetDialog;
    private static final int RC_SIGN_IN = 901;
    private int clickCount = 0,counts,height = 0, sliderPostion=-1;
    private ProductDetailsResponse data;
    private ListPopupWindow listPopupWindow;
    private GoogleSignInClient mGoogleSignInClient;
    private boolean outOfStock = false,isValidVariant = true;
    private ProductDescriptionRecycler1Adapter productDescriptionRecycler1Adapter;
    private ProductDescriptionRecycler2Adapter productDescriptionRecycler2Adapter;
    private SelectSizeProductDetailsAdapter selectSizeProductDetailsAdapter;
    private String sizeName = "" ,sizeText=null,symbol,selectedQuantity = "1",productId = "",commaSeparatedProductId = "";
    private List<ProductDetails> savedProductsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        callProductDetailsApi();
    }

    private void init() {
        dailog=new MyDialog(this);
        listPopupWindow = new ListPopupWindow(this);
        convertedAmout = Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString("converted_amount"));
        symbol = SharedPreferenceWriter.getInstance(this).getString("symbol");
        counts = SharedPreferenceWriter.getInstance(ProductDetailActivity.this).getInt(GlobalVariables.count);
        savedProductsList=GuestUserData.getInstance().getHugeData();
        mGoogleSignInClient = GoogleSignIn.getClient(ProductDetailActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build());
        callbackManager=CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,this);
        if (getIntent() != null) productId = getIntent().getStringExtra(GlobalVariables.product_id);
    }

    private void initCtrl() {
        binding.ivWishlist.setOnClickListener(this);
        binding.llAddToWishList.setOnClickListener(this);
        binding.llAddToWishList2.setOnClickListener(this);
        binding.llAddToBagOrCart.setOnClickListener(this);
        binding.llAddToBagOrCart2.setOnClickListener(this);
        binding.ivAddToBagOrCart.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.ivShare.setOnClickListener(this);
        binding.rl.setOnClickListener(this);
        binding.llTerms.setOnClickListener(this);
        binding.sizeChartText.setOnClickListener(this);
        binding.tvSelectQty.setOnClickListener(this);
        binding.layoutProductDetail7.setOnClickListener(this);
        binding.layoutProductDetail8.setOnClickListener(this);
        binding.layoutProductDetail9.setOnClickListener(this);
        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(this);
        listPopupWindow.setOnItemClickListener(this);
    }

    private void callAddToWishlistApi() {
        dailog.showDialog();
        Call<MyWishListResponse> call = apiInterface.getAddToWishListResult(HelperClass.getCacheData(this).first,
                                                                            HelperClass.getCacheData(this).second,
                                                                            productId,sizeName==null?"":sizeName);
        call.enqueue(new Callback<MyWishListResponse>() {
            @Override
            public void onResponse(Call<MyWishListResponse> call, Response<MyWishListResponse> response) {
                dailog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        if (response.body().getMessage().equalsIgnoreCase("Product removed from wishlist successfully")) {
                            binding.wishListIV2.setImageResource(R.drawable.savedicon8);
                            binding.whisListIV1.setImageResource(R.drawable.savedicon8);
                            binding.ivWishlist.setImageResource(R.drawable.savedicon8);
                        } else {
                            binding.whisListIV1.setImageResource(R.drawable.bitmap_saved_salected);
                            binding.wishListIV2.setImageResource(R.drawable.bitmap_saved_salected);
                            binding.ivWishlist.setImageResource(R.drawable.bitmap_saved_salected);
                        }


                    } else if(response.body().getStatus().equalsIgnoreCase("FAILURE")) {
                        CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),ProductDetailActivity.this);
                    }
                } else {
                    CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",ProductDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Call<MyWishListResponse> call, Throwable t) {
                dailog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),ProductDetailActivity.this);
            }
        });

    }
    private void callAddToCartOrBaglistApi() {
        dailog.showDialog();
        Call<CartListModel2> call = apiInterface.getAddToCartResult3(HelperClass.getCacheData(this).first,
                                                                     HelperClass.getCacheData(this).second,
                                                                     productId, "", sizeName==null?"":sizeName, selectedQuantity);
        call.enqueue(new Callback<CartListModel2>() {
            @Override
            public void onResponse(Call<CartListModel2> call, Response<CartListModel2> response) {
                dailog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        CommonUtil.commonMessagePopup(ProductDetailActivity.this,response.body().getMessage(), GlobalVariables.SUCCESS);
                        if (response.body().getAddtocart().getTotalItemsInCart() > 0) {
                            binding.notificationBadge.setVisibility(View.VISIBLE);
                            binding.notificationBadge.setText(String.valueOf(response.body().getAddtocart().getTotalItemsInCart()));
                        } else binding.notificationBadge.setVisibility(View.GONE);
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),ProductDetailActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",ProductDetailActivity.this);
            }

            @Override
            public void onFailure(Call<CartListModel2> call, Throwable t) {
                dailog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),ProductDetailActivity.this);
            }
        });
    }
    private void callLoginApiForSocial(final String name, final String fbid, final String email, final String profilePhoto, final String socialType) {
        dailog.showDialog();
        Call<SocialLoginModel> call = apiInterface.socialLogin(fbid, socialType,
                SharedPreferenceWriter.getInstance(ProductDetailActivity.this).getString(SharedPreferenceKey.DEVICE_TOKEN), "android", name, email, profilePhoto, "",
                SharedPreferenceWriter.getInstance(ProductDetailActivity.this).getString(GlobalVariables.product_id),
                SharedPreferenceWriter.getInstance(ProductDetailActivity.this).getString(GlobalVariables.color_name),
                SharedPreferenceWriter.getInstance(ProductDetailActivity.this).getString(GlobalVariables.quantity),
                SharedPreferenceWriter.getInstance(ProductDetailActivity.this).getString(GlobalVariables.size));

        call.enqueue(new Callback<SocialLoginModel>() {
            @Override
            public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {
                dailog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) saveData(response);
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.mainRl, response.body().getMessage(), ProductDetailActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",ProductDetailActivity.this);
            }
            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                dailog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),ProductDetailActivity.this);
            }
        });
    }
    private void callProductDetailsApi() {
        dailog.showDialog();
        Call<ProductDetailsResponse> call = apiInterface.getProductDetailsResult(HelperClass.getCacheData(this).first,
                HelperClass.getCacheData(this).second, productId);
        call.enqueue(new Callback<ProductDetailsResponse>() {
            @Override
            public void onResponse(Call<ProductDetailsResponse> call, Response<ProductDetailsResponse> response) {
                dailog.hideDialog();
                if (response.isSuccessful()) {
                    binding.scrollView.setVisibility(View.VISIBLE);
                    binding.llButtonStart.setVisibility(View.VISIBLE);
                    data = response.body();
                    if (response.body().getStatus().equals("SUCCESS")) setData(data);
                    else if(response.body().getStatus().equals("FAILURE")) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),ProductDetailActivity.this);
                } else { CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Product is not added properly",ProductDetailActivity.this); }
            }

            @Override
            public void onFailure(Call<ProductDetailsResponse> call, Throwable t) {
                dailog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),ProductDetailActivity.this);
            }
        });
    }
    private void getImageByColor(String colorName, String sizeName) {
        dailog.showDialog();
        Call<CommonModel> call = apiInterface.getImageByColor(colorName, productId, sizeName);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                dailog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                        color = response.body().getResponse().getImageList();
                        if (response.body().getResponse().getPriceDetail().getPercentage().equalsIgnoreCase("") || response.body().getResponse().getPriceDetail().getSp().equalsIgnoreCase("") || response.body().getResponse().getPriceDetail().getMrp().equalsIgnoreCase("")) isValidVariant = false;
                        else isValidVariant = true;

                        if (!response.body().getResponse().getPriceDetail().getPercentage().equalsIgnoreCase(""))
                            if (response.body().getResponse().getPriceDetail().getPercentage().equalsIgnoreCase("0")) {
                                binding.tvCuttedText.setVisibility(View.GONE);
                                binding.tvOff.setVisibility(View.GONE);
                            } else {
                                binding.tvOff.setText(MessageFormat.format("({0}% OFF)", response.body().getResponse().getPriceDetail().getPercentage()));
                                binding.tvCuttedText.setText(symbol + " " + Math.round(Double.parseDouble(response.body().getResponse().getPriceDetail().getMrp()) * convertedAmout));
                                binding.tvCuttedText.setPaintFlags(ProductDetailActivity.this.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            }
                        if (!response.body().getResponse().getPriceDetail().getSp().equalsIgnoreCase("")) {
                            binding.tvPrice.setText(symbol + " " + Math.round(Double.parseDouble(response.body().getResponse().getPriceDetail().getSp()) * convertedAmout));
                        }
                        if (color.size() > 0) {
                            ((SliderAdapter)binding.viewPager.getAdapter()).setData(ProductDetailActivity.this.color);
                            binding.scrollView.fullScroll(View.FOCUS_UP);
                        }
                    } else if (response.body().getStatus().equalsIgnoreCase("FAILURE")) {
                        isValidVariant = false;
                        CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(), ProductDetailActivity.this);
                    }

                } else {
                    CommonUtil.setUpSnackbarMessage(binding.getRoot(), "Internal Server Error!", ProductDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                dailog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(), t.getMessage(), ProductDetailActivity.this);
            }
        });
    }

    private void getBasicProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void setData(ProductDetailsResponse data) {

        // Slider
        sliderRunnable = new Runnable(){
            @Override
            public void run() {
                if (sliderPostion == data.getProductdetails().getImageList().size()-1) sliderPostion = 0; else sliderPostion++;
                binding.viewPager.setCurrentItem(sliderPostion, true); }
        };

        binding.viewPager.setAdapter(new SliderAdapter(this, data.getProductdetails().getImageList()));
        binding.indicator.setupWithViewPager(binding.viewPager, true);
        new Timer().schedule(new TimerTask() { @Override public void run() { sliderHandler.post(sliderRunnable); }},2000);

        // Product Detail
        binding.tvName.setText(data.getProductdetails().getName());
        if (data.getProductdetails().getPercentage() == 0) {
            binding.tvCuttedText.setVisibility(View.GONE);
            binding.tvOff.setVisibility(View.GONE);
        } else {
            binding.tvOff.setText(MessageFormat.format("({0}% OFF)", data.getProductdetails().getPercentage()));
            binding.tvCuttedText.setText(symbol + " " + Math.round(Double.parseDouble(data.getProductdetails().getMrp()) * convertedAmout));
            binding.tvCuttedText.setPaintFlags(binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        binding.tvPrice.setText(symbol + " " + Math.round(Double.parseDouble(data.getProductdetails().getSp()) * convertedAmout));
        binding.tvBrand.setText(data.getProductdetails().getBrand());
        binding.tvSoldBy.setText(data.getProductdetails().getSoldBy());
        binding.tvHsnCode.setText(data.getProductdetails().getHsnCode());
        binding.tvMoreDescription.setText(data.getProductdetails().getDescription());

        //  Badge Count
        if(!CommonUtil.isUserLogin(this)) counts = data.getProductdetails().getNumOfAddtocart();
        binding.notificationBadge.setText(String.valueOf(counts));
        if (counts == 0) binding.notificationBadge.setVisibility(View.GONE);
        else { binding.notificationBadge.setVisibility(View.VISIBLE);}


        // Size
        binding.tvLabelSize.setText(data.getProductdetails().getSize_label());
        sizeList = data.getProductdetails().getSize();
        if (!data.getProductdetails().getSize().isEmpty()) {
            selectSizeProductDetailsAdapter = new SelectSizeProductDetailsAdapter(this, sizeList);
            binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerSize.setAdapter(selectSizeProductDetailsAdapter);
            selectSizeProductDetailsAdapter.setListener(this);
        } else {
            binding.sizeChartText.setVisibility(View.GONE);
            binding.tvLabelSize.setVisibility(View.GONE);
            binding.recyclerSize.setVisibility(View.GONE);
        }

        // Wish List
        if (data.getProductdetails().getWishHave().equalsIgnoreCase("0")) {
            binding.wishListIV2.setImageResource(R.drawable.savedicon8);
            binding.whisListIV1.setImageResource(R.drawable.savedicon8);
            binding.ivWishlist.setImageResource(R.drawable.savedicon8);
        } else {
            binding.wishListIV2.setImageResource(R.drawable.bitmap_saved_salected);
            binding.whisListIV1.setImageResource(R.drawable.bitmap_saved_salected);
            binding.ivWishlist.setImageResource(R.drawable.bitmap_saved_salected);
        }

        // Add To Bag
        if (data.getProductdetails().getQuantity().equalsIgnoreCase("0") ||
            !(Integer.parseInt(data.getProductdetails().getQuantity()) > 0)) {
            outOfStock = true;
            binding.addToBagText.setText("OUT OF STOCK!");
            binding.addToBagText2.setText("OUT OF STOCK!");
            binding.selectQtyText.setVisibility(View.GONE);
            binding.llqty.setVisibility(View.GONE);
        }

        // Expected Delivery
        if (binding.tvExpectedDelv.getText().equals("1")) binding.tvExpectedDelv.setText("Expected Delivery In " + data.getProductdetails().getDeliveryTime() + " day");
        else binding.tvExpectedDelv.setText("Expected Delivery In " + data.getProductdetails().getDeliveryTime() + " days");


        //View similar
        if (data.getProductdetails().getSimilarProduct().size() > 0) {
            binding.textProductdetail6.setVisibility(View.VISIBLE);
            binding.layoutProductDetail6.setVisibility(View.VISIBLE);
            binding.view21.setVisibility(View.VISIBLE);
            productDescriptionRecycler1Adapter = new ProductDescriptionRecycler1Adapter(this, data.getProductdetails().getSimilarProduct());
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
            linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.recyclerProductDetail.setLayoutManager(linearLayoutManager1);
            binding.recyclerProductDetail.setAdapter(productDescriptionRecycler1Adapter);
        }


        //More Sections
        if(!data.getProductdetails().getMorebrand().isEmpty()){
            if(data.getProductdetails().getMorebrand().get(0)!=null){
                binding.layoutProductDetail7.setVisibility(View.VISIBLE);
                binding.moreTextOne.setText(data.getProductdetails().getMorebrand().get(0));
            }
            if(data.getProductdetails().getMorebrand().get(1)!=null){
                binding.layoutProductDetail8.setVisibility(View.VISIBLE);
                binding.moreTextTwo.setText(data.getProductdetails().getMorebrand().get(1));
            }

            if(data.getProductdetails().getMorebrand().get(2)!=null){
                binding.layoutProductDetail9.setVisibility(View.VISIBLE);
                binding.moreTextThree.setText(data.getProductdetails().getMorebrand().get(2));
            }
        }


        //Customer also like
        if (data.getProductdetails().getCustomerAlsoLiked().size() > 0) {
            binding.alsoLikeTxt.setVisibility(View.VISIBLE);
            productDescriptionRecycler2Adapter = new ProductDescriptionRecycler2Adapter(this, data.getProductdetails().getCustomerAlsoLiked());
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
            linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.recyclerProductDetail2.setLayoutManager(linearLayoutManager2);
            binding.recyclerProductDetail2.setAdapter(productDescriptionRecycler2Adapter);
        }
    }



    private void openLoginSignUpBottomSheetWhenUserNotLogedIn() {
        bottomSheetDialog = new BottomSheetDialog(this);
        PopUpBottomSheetLoginWhenUserNotLogedInBinding binding= PopUpBottomSheetLoginWhenUserNotLogedInBinding.inflate(LayoutInflater.from(this), null);
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        binding.ivCross.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
        binding.ivFb.setOnClickListener(this);
        binding.ivGoogle.setOnClickListener(this);
        bottomSheetDialog.show();
    }

    private void googleSignIn() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void facebookLogin() {
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (HelperClass.showInternetAlert(ProductDetailActivity.this)) {
                callLoginApiForSocial(account.getGivenName(), account.getId(), account.getEmail(), account.getPhotoUrl().toString(), "Google");
            }
        } catch (ApiException e) {
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),e.getMessage(),ProductDetailActivity.this);
        }
    }


    private void saveData(Response<SocialLoginModel> response) {
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).getString(commaSeparatedProductId, "");
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).getString(SharedPreferenceKey.BATCH_COUNT, "");
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(SharedPreferenceKey.FULL_NAME, response.body().getSociallogin().getName());
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(SharedPreferenceKey.EMAIL, (String) response.body().getSociallogin().getEmail());
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(SharedPreferenceKey.IMAGE, response.body().getSociallogin().getImage());
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(SharedPreferenceKey.USER_ID, "" + response.body().getSociallogin().getId());
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(SharedPreferenceKey.TOKEN, response.body().getSociallogin().getToken());
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(SharedPreferenceKey.CRTEATED_AT, response.body().getSociallogin().getCreatedAt());
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(SharedPreferenceKey.UPDATED_AT, response.body().getSociallogin().getUpdatedAt());
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS, true);
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(GlobalVariables.color_name,"");
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(GlobalVariables.quantity,"");
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(GlobalVariables.size,"");
        SharedPreferenceWriter.getInstance(ProductDetailActivity.this).writeStringValue(GlobalVariables.product_id,"");

        Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class).putExtra("from", "LoginButton");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Bottom Sheet
            case R.id.ivCross: bottomSheetDialog.dismiss(); break;
            case R.id.btnLogin: bottomSheetDialog.dismiss(); startActivity(new Intent(this,LoginActivity.class)); break;
            case R.id.btnSignUp: bottomSheetDialog.dismiss(); startActivity(new Intent(this,SignUpActivity.class)); break;
            case R.id.ivFb: bottomSheetDialog.dismiss(); facebookLogin(); break;
            case R.id.ivGoogle: bottomSheetDialog.dismiss(); googleSignIn(); break;

            // Main Screen
            case R.id.ivWishlist:
            if(CommonUtil.isUserLogin(this)) openLoginSignUpBottomSheetWhenUserNotLogedIn();
            else startActivity(new Intent(this, MyWishListActivity.class));
            break;

            case R.id.llAddToWishList:
            if(CommonUtil.isUserLogin(this)) openLoginSignUpBottomSheetWhenUserNotLogedIn();
            else {
            if (sizeText == null && sizeList.size() > 0 && sizeName.equals("")) CommonUtil.commonMessagePopup(this,"Please select any size", "");
            else if (!isValidVariant) CommonUtil.commonMessagePopup(this,"Not valid product variant", "FAILURE");
            else callAddToWishlistApi(); }
            break;

            case R.id.llAddToWishList2: binding.llAddToWishList.performClick(); break;
            case R.id.llAddToBagOrCart: addToBag(); break;
            case R.id.llAddToBagOrCart2: binding.llAddToBagOrCart.performClick(); break;
            case R.id.ivAddToBagOrCart:
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("screen", ProductDetailActivity.class.getSimpleName());
            finish();
            startActivity(intent);
            break;

            case R.id.ivBack: onBackPressed(); break;
            case R.id.ivShare: shareApp(productId); break;
            case R.id.rl: binding.ivShare.performClick(); break;
            case R.id.llTerms: startActivity(new Intent(this, TermsOfUseActivity.class)); break;

            case R.id.tvSelectQty:
            if (clickCount % 2 == 0) openDropDownList();
            else listPopupWindow.dismiss();
            clickCount = clickCount + 1;
            break;

            case R.id.layout_product_detail7: callingIntent( "moreTextOne", binding.moreTextOne.getText().toString(), data.getProductdetails().getSubsubcatid()); break;
            case R.id.layout_product_detail8: callingIntent( "moreTextTwo", binding.moreTextTwo.getText().toString(), data.getProductdetails().getSubsubcatid()); break;
            case R.id.layout_product_detail9:  callingIntent( "moreTextThree", binding.moreTextThree.getText().toString(), data.getProductdetails().getSubsubcatid()); break;
        }
    }

    private void callingIntent(String section, String section_name, int subsubcatid) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("from", GlobalVariables.ProductDetailsActivityFinal);
        intent.putExtra(GlobalVariables.section, section);
        intent.putExtra(GlobalVariables.section_name, section_name);
        intent.putExtra(GlobalVariables.subsubcatid, subsubcatid);
        startActivity(intent);
    }


    @Override
    public void itemClick(View view, int pos) {
        sizeText = pos + "";
        if(sizeList.get(pos)!=null) sizeName = sizeList.get(pos);
        else sizeName="";
        getImageByColor("", sizeName);
    }

    private void addToBag() {
        boolean isProductExists = false;
        Boolean isProductMatch = false;
        if (outOfStock) CommonUtil.commonMessagePopup(this, getString(R.string.out_of_stock), "");
        else if (sizeText == null && sizeList.size() > 0) CommonUtil.commonMessagePopup(this, "Please select any " + binding.tvLabelSize.getText().toString().trim(), "");
        else if (!isValidVariant) CommonUtil.commonMessagePopup(this, "Not valid product variant", "FAILURE");
        else {
            if (CommonUtil.isUserLogin(this)) {
                if (!savedProductsList.isEmpty()) {
                    isProductMatch = false;
                    for (int i = 0; i < savedProductsList.size(); i++) {
                        if (savedProductsList.get(i).getProduct_id().equalsIgnoreCase(productId)) {
                            if(!checkSizeContains(savedProductsList,productId,sizeName)) {
                                isProductExists = false;
                                isProductMatch = false;
                                break;
                            }
                             else {
                                isProductMatch = true;
                                isProductExists = true;
                                break;
                            }
                        } else {
                            isProductMatch = false;
                        }
                    }
                }

                if (!isProductExists) {
                    if (!isProductMatch) {

                        savedProductsList.add(new ProductDetails(productId, "", sizeName, selectedQuantity));
                        GuestUserData.getInstance().setHugeData(savedProductsList);
                        List<String> productList=new ArrayList<>();
                        List<String> sizeList=new ArrayList<>();
                        List<String> quantityList=new ArrayList<>();
                        for(int i=0;i<savedProductsList.size();i++){
                         productList.add(savedProductsList.get(i).getProduct_id());
                         sizeList.add(savedProductsList.get(i).getSize());
                         quantityList.add(savedProductsList.get(i).getQuantity());
                       }
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeIntValue(GlobalVariables.count, savedProductsList.size());
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(GlobalVariables.product_id, TextUtils.join(",",productList));
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(GlobalVariables.color_name, "");
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(GlobalVariables.size, TextUtils.join(",",sizeList));
                        SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(GlobalVariables.quantity,TextUtils.join(",",quantityList));

                        CommonUtil.commonMessagePopup(this,getString(R.string.added_to_cart),"SUCCESS");
                        binding.notificationBadge.setText(String.valueOf(savedProductsList.size()));
                        if (savedProductsList.size() == 0) binding.notificationBadge.setVisibility(View.GONE);
                        else { binding.notificationBadge.setVisibility(View.VISIBLE);}

                    } else CommonUtil.commonMessagePopup(this, getString(R.string.added_to_cart_already), "");
                } else CommonUtil.commonMessagePopup(this, getString(R.string.added_to_cart_already), "");

            } else callAddToCartOrBaglistApi();
        }
    }

    private boolean checkSizeContains(List<ProductDetails> savedProductsList, String productId, String sizeName) {
        boolean ret=false;
        for(int i=0;i<savedProductsList.size();i++){
            if(savedProductsList.get(i).getProduct_id().equalsIgnoreCase(productId)){
                if(savedProductsList.get(i).getSize().equalsIgnoreCase(sizeName)){
                    ret=true;
                    break;
                }
            }
        }
        return ret;
    }

    private void openDropDownList() {
        selectedMenuList.clear();
        int loopsize = 0;
        if (Integer.parseInt(data.getProductdetails().getQuantity()) > 5)  loopsize = 5;
        else loopsize = Integer.parseInt(data.getProductdetails().getQuantity());
        for (int i = 0; i < loopsize; i++) selectedMenuList.add("" + (i + 1));
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, selectedMenuList);
        listPopupWindow.setAnchorView(binding.tvSelectQty);
        listPopupWindow.setAdapter(arrayAdapter);
        listPopupWindow.show();
    }

    private void shareApp(String productId) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        String shareLing = "https://soulahe.page.link/?link=https://soulahe.com/" + productId + "&apn=com.satvick&isi=1481825964&ibi=com.mobulous.YOD";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareLing);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    @Override
    public void onScrollChanged() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if (binding.scrollView.getScrollY() <= 340) binding.llButtonStart.setVisibility(View.VISIBLE);
                else binding.llButtonStart.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        getBasicProfile(loginResult);
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        if (HelperClass.showInternetAlert(ProductDetailActivity.this)) callLoginApiForSocial(object.optString("name"),  object.optString("id"), object.optString("email"), "https://graph.facebook.com/" + object.optString("id") + "/picture?type=large", "Facebook");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedQuantity = selectedMenuList.get(position);
        binding.tvSelectQty.setText(selectedQuantity);
        clickCount = clickCount - 1;
        listPopupWindow.dismiss();
    }
}

