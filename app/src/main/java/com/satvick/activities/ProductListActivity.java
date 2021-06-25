package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.satvick.adapters.ProductListingAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityProductListBinding;
import com.satvick.databinding.ItemSortByBinding;
import com.satvick.databinding.PopUpBottomSheetLoginWhenUserNotLogedInBinding;
import com.satvick.fcm.MyFirebaseMessagingService;
import com.satvick.model.Moreproductlist;
import com.satvick.model.MyWishListResponse;
import com.satvick.model.ProductListingResponse;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener, FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback, ProductListingAdapter.ProductListItemClickListener {

    private ActivityProductListBinding binding;
    private final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private BottomSheetDialog bottomSheetDialog,sortByDailog;
    private List<Moreproductlist> productListingResponseList = new ArrayList<>();

    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 901;

    private String subcatid = "",type="",comeFrom="",sortBy="", subcatname = "",
                   moreSection = "", subsubcatid = "", brandname = "", defaultcolor = "",
                   minValue = "100", maxValue = "10000", flashSale = "", search = "",
                   search_key = "", catid = "", filter = "", theme_id = "", theme = "",
                   commaSeparatedSizeNameId="",commaSeparatedColorNameId="",commaSeparatedBrandNameId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        getIntents();
        callProductListApi();
    }

    private void init() {
        binding.progressBar.setVisibility(View.VISIBLE);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,this);
        mGoogleSignInClient = GoogleSignIn.getClient(ProductListActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build());
    }
    private void initCtrl() {
        binding.productListBackBtn.setOnClickListener(this);
        binding.sortProduct.setOnClickListener(this);
        binding.filterproduct.setOnClickListener(this);
        binding.ivWishList.setOnClickListener(this);
        binding.ivSearch.setOnClickListener(this);
        binding.tvProductName.setText(subcatname);
    }
    private void getIntents() {
        comeFrom = getIntent().getStringExtra("from");
        if (getIntent() != null) {
            if (comeFrom.equalsIgnoreCase("FilterProductListActivity")) {
                filter = "Filter";
                commaSeparatedSizeNameId = getIntent().getStringExtra("commaSeparatedSizeNameId");
                commaSeparatedColorNameId = getIntent().getStringExtra("commaSeparatedColorNameId");
                commaSeparatedBrandNameId = getIntent().getStringExtra("commaSeparatedBrandNameId");

                minValue = getIntent().getStringExtra(GlobalVariables.minValue);
                maxValue = getIntent().getStringExtra(GlobalVariables.maxValue);
                type = getIntent().getStringExtra(GlobalVariables.type);
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
                subcatid = getIntent().getStringExtra(GlobalVariables.subcatid);
                subsubcatid = getIntent().getStringExtra(GlobalVariables.subsubcatid);
                catid = getIntent().getStringExtra(GlobalVariables.catid);

            } else if (comeFrom.equalsIgnoreCase("ThreeLevelListAdapter")) {
                filter = "Filter";
                commaSeparatedSizeNameId = getIntent().getStringExtra("commaSeparatedSizeNameId");
                commaSeparatedColorNameId = getIntent().getStringExtra("commaSeparatedColorNameId");
                commaSeparatedBrandNameId = getIntent().getStringExtra("commaSeparatedBrandNameId");
                minValue = getIntent().getStringExtra(GlobalVariables.minValue);
                maxValue = getIntent().getStringExtra(GlobalVariables.maxValue);
                type = getIntent().getStringExtra(GlobalVariables.type);
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
                catid = getIntent().getStringExtra(GlobalVariables.catid);
                subsubcatid = getIntent().getStringExtra(GlobalVariables.subsubcatid);

            } else if (comeFrom.equalsIgnoreCase("HomeFragmentAfterLogin")) {

                subcatid = getIntent().getStringExtra(GlobalVariables.subcatid);
                subcatname = getIntent().getStringExtra(GlobalVariables.subcatname);

            } else if (comeFrom.equalsIgnoreCase(GlobalVariables.ProductDetailsActivityFinal)) {
                moreSection = getIntent().getStringExtra(GlobalVariables.section);
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name).split("More")[1];
                if (moreSection.equalsIgnoreCase("moreTextOne")) {
                    subsubcatid = String.valueOf(getIntent().getIntExtra(GlobalVariables.subsubcatid, 0));
                    brandname = getIntent().getStringExtra(GlobalVariables.brandname);

                } else if (moreSection.equalsIgnoreCase("moreTextTwo")) {
                    subsubcatid = String.valueOf(getIntent().getIntExtra(GlobalVariables.subsubcatid, 0));
                    defaultcolor = getIntent().getStringExtra(GlobalVariables.defaultcolor);
                } else if (moreSection.equalsIgnoreCase("moreTextThree")) {
                    subsubcatid = String.valueOf(getIntent().getIntExtra(GlobalVariables.subsubcatid, 0));
                }

            } else if (comeFrom.equalsIgnoreCase(GlobalVariables.flashSale)) {
                flashSale = GlobalVariables.flashSale;
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
            } else if (comeFrom.equalsIgnoreCase("SearchAdapter")) {
                filter = "Filter";
                commaSeparatedSizeNameId = getIntent().getStringExtra("commaSeparatedSizeNameId");
                commaSeparatedColorNameId = getIntent().getStringExtra("commaSeparatedColorNameId");
                commaSeparatedBrandNameId = getIntent().getStringExtra("commaSeparatedBrandNameId");

                minValue = getIntent().getStringExtra(GlobalVariables.minValue);
                maxValue = getIntent().getStringExtra(GlobalVariables.maxValue);
                type = getIntent().getStringExtra(GlobalVariables.type);
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
                subcatid = getIntent().getStringExtra(GlobalVariables.subcatid);
                catid = getIntent().getStringExtra(GlobalVariables.catid);
                subsubcatid = getIntent().getStringExtra(GlobalVariables.subsubcatid);

            } else if (comeFrom.equalsIgnoreCase("BrandInFocusAdapter")) {
                filter = "filter";
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
                commaSeparatedBrandNameId = subcatname;

            } else if (comeFrom.equalsIgnoreCase("FilteredBrandInFocus")) {
                filter = "filter";
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
                commaSeparatedColorNameId = getIntent().getStringExtra("commaSeparatedColorNameId");
                commaSeparatedBrandNameId = getIntent().getStringExtra("commaSeparatedBrandNameId");
                minValue = getIntent().getStringExtra(GlobalVariables.minValue);
                maxValue = getIntent().getStringExtra(GlobalVariables.maxValue);
            } else if (comeFrom.equalsIgnoreCase("ShopByThemeAdapter")) {
                minValue = getIntent().getStringExtra(GlobalVariables.minValue);
                maxValue = getIntent().getStringExtra(GlobalVariables.maxValue);
                commaSeparatedSizeNameId = getIntent().getStringExtra("commaSeparatedSizeNameId");
                commaSeparatedColorNameId = getIntent().getStringExtra("commaSeparatedColorNameId");
                commaSeparatedBrandNameId = getIntent().getStringExtra("commaSeparatedBrandNameId");
                theme_id = getIntent().getStringExtra(GlobalVariables.subcatid);
                theme = getIntent().getStringExtra(GlobalVariables.theme);
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
                filter = getIntent().getStringExtra(GlobalVariables.filter_data);
            } else if (comeFrom.equalsIgnoreCase("MenSectionShopByOccassionAdapter")) {

                subsubcatid = getIntent().getStringExtra(GlobalVariables.subsubcatid);
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
                filter = getIntent().getStringExtra(GlobalVariables.filter_data);
                commaSeparatedSizeNameId = getIntent().getStringExtra("commaSeparatedSizeNameId");
                commaSeparatedColorNameId = getIntent().getStringExtra("commaSeparatedColorNameId");
                commaSeparatedBrandNameId = getIntent().getStringExtra("commaSeparatedBrandNameId");
                minValue = getIntent().getStringExtra(GlobalVariables.minValue);
                maxValue = getIntent().getStringExtra(GlobalVariables.maxValue);

            } else if (comeFrom.equalsIgnoreCase("AutoSlideViewPagerBannerAdapter")) {
                subsubcatid = getIntent().getStringExtra(GlobalVariables.subsubcatid);
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
                filter = getIntent().getStringExtra(GlobalVariables.filter_data);
                commaSeparatedSizeNameId = getIntent().getStringExtra("commaSeparatedSizeNameId");
                commaSeparatedColorNameId = getIntent().getStringExtra("commaSeparatedColorNameId");
                commaSeparatedBrandNameId = getIntent().getStringExtra("commaSeparatedBrandNameId");
                minValue = getIntent().getStringExtra(GlobalVariables.minValue);
                maxValue = getIntent().getStringExtra(GlobalVariables.maxValue);

            } else if (comeFrom.equalsIgnoreCase(MyFirebaseMessagingService.class.getSimpleName())) {
                subsubcatid = getIntent().getStringExtra(GlobalVariables.subsubcatid);
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
                filter = getIntent().getStringExtra(GlobalVariables.filter_data);
                commaSeparatedSizeNameId = getIntent().getStringExtra("commaSeparatedSizeNameId");
                commaSeparatedColorNameId = getIntent().getStringExtra("commaSeparatedColorNameId");
                commaSeparatedBrandNameId = getIntent().getStringExtra("commaSeparatedBrandNameId");
                minValue = getIntent().getStringExtra(GlobalVariables.minValue);
                maxValue = getIntent().getStringExtra(GlobalVariables.maxValue);

            } else {
                subsubcatid = getIntent().getStringExtra(GlobalVariables.subsubcatid);
                subcatid = getIntent().getStringExtra(GlobalVariables.subcatid);
                subcatname = getIntent().getStringExtra(GlobalVariables.section_name);
            }

        }
    }
    private void callProductListApi() {
        Call<ProductListingResponse> call = apiInterface.getProductListResult(HelperClass.getCacheData(this).second,
                                                                              brandname, defaultcolor, subcatid, subsubcatid,
                                                                              catid, filter, commaSeparatedSizeNameId,
                                                                              commaSeparatedColorNameId, commaSeparatedBrandNameId,
                                                                              flashSale, search, search_key, minValue,
                                                                              maxValue, sortBy, theme, theme_id);
        call.enqueue(new Callback<ProductListingResponse>() {
            @Override
            public void onResponse(Call<ProductListingResponse> call, Response<ProductListingResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body().getMoreproductlist());
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(),ProductListActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",ProductListActivity.this);
            }

            @Override
            public void onFailure(Call<ProductListingResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),ProductListActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // Sort By Bottom Sheet
            case R.id.tvDeliveryTime : sortData("delivery_time");  break;
            case R.id.tvHighToLow : sortData("high_to_low"); break;
            case R.id.tvLowToHigh :  sortData("low_to_high"); break;
            case R.id.tvDiscount :  sortData("discount"); break;
            case R.id.tvPopularity :  sortData("popularity"); break;
            case R.id.tvWhatsNew :  sortData("new");  break;

            // Login Bottom Sheet
            case R.id.ivCross: bottomSheetDialog.dismiss(); break;
            case R.id.btnLogin: bottomSheetDialog.dismiss(); startActivity(new Intent(this,LoginActivity.class)); break;
            case R.id.btnSignUp: bottomSheetDialog.dismiss(); startActivity(new Intent(this,SignUpActivity.class)); break;
            case R.id.ivFb: bottomSheetDialog.dismiss(); facebookLogin(); break;
            case R.id.ivGoogle: bottomSheetDialog.dismiss(); googleSignIn(); break;

            // Main
            case R.id.ivSearch: startActivity(new Intent(this, SearchScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)); break;
            case R.id.productListBackBtn: onBackPressed(); break;
            case R.id.sortProduct: sortingOfProduct(); break;
            case R.id.filterproduct: filterProduct(); break;
            case R.id.ivWishList:
            if(CommonUtil.isUserLogin(this)) openLoginSignUpBottomSheetWhenUserNotLogedIn();
            else startActivity(new Intent(this, MyWishListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            break;

        }
    }

    private void sortData(String type) {
        sortByDailog.dismiss();
        sortBy=type;
        binding.progressBar.setVisibility(View.VISIBLE);
        callProductListApi();
    }


    private void getBasicProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }
    private void setDataToUI(List<Moreproductlist> list) {
        productListingResponseList = list;
        if(productListingResponseList!=null) {

            if (productListingResponseList.isEmpty()) binding.tvProductCount.setText("No Item");
            else if (productListingResponseList.size() == 1) binding.tvProductCount.setText(1 + " Item");
            else binding.tvProductCount.setText(productListingResponseList.size() + " Items");

            binding.productListRecycler.setLayoutManager(new GridLayoutManager(ProductListActivity.this, 2));
            binding.productListRecycler.setAdapter(new ProductListingAdapter(ProductListActivity.this, productListingResponseList, this));
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
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RC_SIGN_IN: handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data)); break;
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (HelperClass.showInternetAlert(this)) {
                callLoginApiForSocial(account.getGivenName(), account.getId(), account.getEmail(),account.getPhotoUrl().toString(),"Google");
            }
        } catch (ApiException e) {
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),e.getMessage(),this);
        }
    }


    private void callLoginApiForSocial(final String name, final String fbid,
                                       final String email, final String profilePhoto, final String socialType) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<SocialLoginModel> call = apiInterface.socialLogin(fbid, socialType,
                                                               SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(SharedPreferenceKey.DEVICE_TOKEN),
                                                               "android", name, email, profilePhoto, "",
                                                               SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(GlobalVariables.product_id),
                                                               SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(GlobalVariables.color_name),
                                                               SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(GlobalVariables.quantity),
                                                               SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(GlobalVariables.size));


        call.enqueue(new Callback<SocialLoginModel>() {
            @Override
            public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) CommonUtil.saveData(ProductListActivity.this,response);
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(), ProductListActivity.this);
                    else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!", ProductListActivity.this);
                }
            }

            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(), ProductListActivity.this);
            }
        });
    }


    private void callAddToWishlistApi(int pos) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<MyWishListResponse> call = apiInterface.getAddToWishListResult(HelperClass.getCacheData(this).first,
                                                                            HelperClass.getCacheData(this).second,
                                                                            productListingResponseList.get(pos).getProductId(),
                                                                            productListingResponseList!=null?""+productListingResponseList.size():"0");
        call.enqueue(new Callback<MyWishListResponse>() {
            @Override
            public void onResponse(Call<MyWishListResponse> call, Response<MyWishListResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")){ sortBy=""; callProductListApi(); }
                     else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        binding.progressBar.setVisibility(View.GONE);
                        CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),ProductListActivity.this);
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",ProductListActivity.this);
                }
            }

            @Override
            public void onFailure(Call<MyWishListResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),ProductListActivity.this);
            }
        });
    }



    private void sortingOfProduct() {
        sortByDailog = new BottomSheetDialog(ProductListActivity.this);
        ItemSortByBinding binding=ItemSortByBinding.inflate(LayoutInflater.from(this),null);
        sortByDailog.setContentView(binding.getRoot());
        sortByDailog.getWindow().findViewById(R.id.bottom_sheet).setBackgroundResource(android.R.color.transparent);
        binding.tvWhatsNew.setOnClickListener(this);
        binding.tvPopularity.setOnClickListener(this);
        binding.tvLowToHigh.setOnClickListener(this);
        binding.tvDiscount.setOnClickListener(this);
        binding.tvHighToLow.setOnClickListener(this);
        binding.tvDeliveryTime.setOnClickListener(this);
        sortByDailog.show();
    }

    private void filterProduct() {
        String types = "",from="";
        if (comeFrom.equalsIgnoreCase(GlobalVariables.flashSale) || type.equalsIgnoreCase(GlobalVariables.flashSale)) {
            subcatid = "0";
            types = GlobalVariables.flashSale;
            from="ProductListActivity";
        } else if (comeFrom.equalsIgnoreCase("ThreeLevelListAdapter")) {
            subcatid = subsubcatid;
            types = "subsubcat";
            from="ThreeLevelListAdapter";
        } else if (comeFrom.equalsIgnoreCase("SearchAdapter")) {
            subcatid = subsubcatid;
            types = "subsubcat";
            from=comeFrom;
        } else if (comeFrom.equalsIgnoreCase("BrandInFocusAdapter")) {
            subcatid = "";
            types = subcatname;
            from=comeFrom;
        } else if (comeFrom.equalsIgnoreCase("FilteredBrandInFocus")) {
            subcatid = "";
            types = subcatname;
            from="BrandInFocusAdapter";
        } else if (comeFrom.equalsIgnoreCase(GlobalVariables.ProductDetailsActivityFinal)) {
            types = "subcat";
            from=comeFrom;
        } else if (comeFrom.equalsIgnoreCase("ShopByThemeAdapter")) {
            types = "theme";
            subcatid = theme_id;
            from=comeFrom;
        } else if (comeFrom.equalsIgnoreCase("MenSectionShopByOccassionAdapter")) {
            types = "subsubcat";
            from="MenSectionShopByOccassionAdapter";
        } else if (comeFrom.equalsIgnoreCase("AutoSlideViewPagerBannerAdapter")) {
            types = "subsubcat";
            from="AutoSlideViewPagerBannerAdapter";
        } else if (comeFrom.equalsIgnoreCase(MyFirebaseMessagingService.class.getSimpleName())) {
            types = "subsubcat";
            from=MyFirebaseMessagingService.class.getSimpleName();
        } else {
            types = "subcat";
            from="ProductListActivity";
        }

        callingIntent(from, binding.tvProductName.getText().toString(), subcatid, types);
    }

    private void callingIntent(String from, String section_name, String subcatid, String type) {
        Intent intent = new Intent(this, FilterProductListActivity.class);
        intent.putExtra("from", from);
        intent.putExtra(GlobalVariables.section_name, section_name);
        intent.putExtra(GlobalVariables.subcatid, subcatid);
        intent.putExtra(GlobalVariables.type, type);
        if (from.equalsIgnoreCase("SearchAdapter")) {
            intent.putExtra(GlobalVariables.catid, catid);
            intent.putExtra(GlobalVariables.filter_data, getIntent().getStringExtra(GlobalVariables.filter_data));

        } else if (comeFrom.equalsIgnoreCase("ShopByThemeAdapter")) {
            intent.putExtra(GlobalVariables.theme, theme);

        } else if (comeFrom.equalsIgnoreCase("MenSectionShopByOccassionAdapter")) {
            intent.putExtra(GlobalVariables.subsubcatid, subsubcatid);
        } else if (comeFrom.equalsIgnoreCase("AutoSlideViewPagerBannerAdapter")) {
            intent.putExtra(GlobalVariables.subsubcatid, subsubcatid);
        } else if (comeFrom.equalsIgnoreCase(MyFirebaseMessagingService.class.getSimpleName())) {
            intent.putExtra(GlobalVariables.subsubcatid, subcatid);
        }
        startActivity(intent);

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
        if (HelperClass.showInternetAlert(ProductListActivity.this)) {
            callLoginApiForSocial(object.optString("name"), object.optString("id"), object.optString("email"), "https://graph.facebook.com/" + object.optString("id") + "/picture?type=large", "Facebook");
        }
    }

    @Override
    public void onProductItemClick(View view, int pos) {
        startActivity(new Intent(ProductListActivity.this, ProductDetailActivity.class)
                           .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK )
                           .putExtra("product_id", productListingResponseList.get(pos).getProductId()));
    }

    @Override
    public void onIvItemClick(View view, int pos) {
        callAddToWishlistApi(pos);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
