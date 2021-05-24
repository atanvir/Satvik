package com.satvick.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
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
import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.adapters.ProductListingAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityProductListBinding;
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
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ProductListActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityProductListBinding binding;
    HashMap<String, Object> stringObjectHashMap;


    public static Intent getIntent(Context context, HashMap<String, Object> stringObjectHashMap) {
        Intent intent = new Intent(context, ProductListActivity.class);
        intent.putExtra("kdata", stringObjectHashMap);
        return intent;
    }

    ProductListingAdapter listingAdapter;
    List<Moreproductlist> productListingResponseList = new ArrayList<>();

    String productId = "";
    String sortBy = "";
    String comeFrom = "";
    String type = "";
    String filterData = "";
    String productName = "";
    int noOfProduct;
    String option = "";
    String commaSeparatedProductId = "";
    String filter_type;
    String filter_data;

    boolean isChecked = true;


    //for social logins
    CallbackManager callbackManager;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 901;


    /////////////
    String extraFilterType = "";

    String commaSeparatedSizeNameId = "";

    String commaSeparatedColorNameId = "";
    String commaSeparatedBrandNameId = "";

    String subcatid = "", subcatname = "", userId = "",
            moreSection = "", subsubcatid = "", brandname = "",
            defaultcolor = "", minValue = "",
            maxValue = "", flashSale = "", search = "", search_key = "",
            catid = "", filter = "", theme_id = "", theme = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_list);

        getIntents();
        initViews();

        commaSeparatedProductId = SharedPreferenceWriter.getInstance(ProductListActivity.this).getString("Ids");
        callProductListApi("", binding.mainRl);
        fbRegisterCallBack();
        googleRegister();

    }

    private void getIntents() {

        comeFrom = getIntent().getStringExtra("from");
        Log.e("comeFrom", comeFrom);
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
                extraFilterType = getIntent().getStringExtra(GlobalVariables.filter_data);

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

    private void googleRegister() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(ProductListActivity.this, gso);
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
                        Toast.makeText(ProductListActivity.this, R.string.cancel, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(ProductListActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getBasicProfile(LoginResult loginResult) {
        // App code
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());

                        // Application code
                        String name = object.optString("name");
                        String fbid = object.optString("id");
                        String email = object.optString("email");
                        String profilePhoto = "https://graph.facebook.com/" + fbid + "/picture?type=large";

                        if (HelperClass.showInternetAlert(ProductListActivity.this)) {
                            callLoginApiForSocial(name, fbid, email, profilePhoto, binding.mainRl, "Facebook");
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void callProductListApi(final String sortBy, final View view) {

        final MyDialog myDialog = new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        if (minValue == null || maxValue == null) {
            minValue = "100";
            maxValue = "10000";
        } else if (minValue.equalsIgnoreCase("") || maxValue.equalsIgnoreCase("")) {

            minValue = "100";
            maxValue = "10000";
        }


        Call<ProductListingResponse> call = apiInterface.getProductListResult(userId, brandname, defaultcolor, subcatid, subsubcatid, catid, filter, commaSeparatedSizeNameId, commaSeparatedColorNameId, commaSeparatedBrandNameId, flashSale, search, search_key, minValue, maxValue, sortBy, theme, theme_id);

        Log.d("OnGetProDetaisl:", call.request().toString());
        call.enqueue(new Callback<ProductListingResponse>() {
            @Override
            public void onResponse(Call<ProductListingResponse> call, Response<ProductListingResponse> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    ProductListingResponse data = response.body();

                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        productListingResponseList = data.getMoreproductlist();
                        if (productListingResponseList.size() > 0) {
                            setProductCount(productListingResponseList);
                            setProductListingAdapter(productListingResponseList);
                        } else {
                            CommonUtil.CommonMessagePopUp(ProductListActivity.this, "New Collection Awaited");
                        }


                    } else {
                        Toast.makeText(ProductListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(ProductListActivity.this, R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callProductListApi(sortBy, view);
                            Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(ProductListActivity.this, R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(ProductListActivity.this, R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ProductListingResponse> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void setProductCount(List<Moreproductlist> productListingResponseList) {
        noOfProduct = productListingResponseList.size();
        if (noOfProduct == 0) {
            binding.tvProductCount.setText("No Item");
        } else if (noOfProduct == 1) {
            binding.tvProductCount.setText(noOfProduct + " Item");
        } else if (noOfProduct > 1) {
            binding.tvProductCount.setText(noOfProduct + " Items");
        }
    }

    private void setProductListingAdapter(final List<Moreproductlist> productListingResponseList) {


        listingAdapter = new ProductListingAdapter(ProductListActivity.this, productListingResponseList, productId);
        binding.productListRecycler.setLayoutManager(new GridLayoutManager(ProductListActivity.this, 2));
        binding.productListRecycler.setAdapter(listingAdapter);

        listingAdapter.setListener(new ProductListingAdapter.ProductListItemClickListener() {
            @Override
            public void onProductItemClick(View view, int pos) {
                startActivity(new Intent(ProductListActivity.this, ProductDetailsActivityFinal.class).putExtra("product_id", productListingResponseList.get(pos).getProductId()));
            }

            @Override
            public void onIvItemClick(View view, int pos) {
                productId = productListingResponseList.get(pos).getProductId();
                callAddToWishlistApi(binding.mainRl);
            }

        });

    }


    private void openLoginSignUpBottomSheetWhenUserNotLogedIn() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view1 = this.getLayoutInflater().inflate(R.layout.pop_up_bottom_sheet_login_when_user_not_loged_in, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

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
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductListActivity.this, LoginActivity.class));
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductListActivity.this, SignUpActivity.class));
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
                if (isChecked) {
                    isChecked = false;
                    chbSelect.setButtonDrawable(ContextCompat.getDrawable(ProductListActivity.this, R.drawable.sale_box));

                } else {
                    isChecked = true;
                    chbSelect.setButtonDrawable(ContextCompat.getDrawable(ProductListActivity.this, R.drawable.new_rect));
                }
            }
        });

        bottomSheetDialog.show();

    }

    private void initViews() {
        binding.productListBackBtn.setOnClickListener(this);
        binding.sortProduct.setOnClickListener(this);
        binding.filterproduct.setOnClickListener(this);
        binding.ivWishList.setOnClickListener(this);
        binding.ivSearch.setOnClickListener(this);
        binding.tvProductName.setText(subcatname);


        if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {

            userId = "";

        } else {
            userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);
        }
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
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Finally, in your onActivityResult method, call callbackManager.
        //onActivityResult to pass the login results to the LoginManager via callbackManager.
        callbackManager.onActivityResult(requestCode, resultCode, data);//for facebook
        super.onActivityResult(requestCode, resultCode, data);


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
            String profilePhoto = account.getPhotoUrl().toString();
            String email = account.getEmail();
            String gid = account.getId();
            String name = account.getGivenName();

            if (HelperClass.showInternetAlert(ProductListActivity.this)) {
                callLoginApiForSocial(name, gid, email, profilePhoto, binding.mainRl, "Google");
            }

            // Signed in successfully,show authenticated UI.

        } catch (ApiException e) {
            Log.e("error::", e.getMessage());

        }
    }

    private void callLoginApiForSocial(final String name, final String fbid, final String email, final String profilePhoto, final View view, final String socialType) {
        String deviceToken = SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(SharedPreferenceKey.DEVICE_TOKEN);

        final MyDialog myDialog = new MyDialog(ProductListActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<SocialLoginModel> call = apiInterface.socialLogin(fbid, socialType, deviceToken, "android", name, email, profilePhoto, "",
                SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(GlobalVariables.product_id),
                SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(GlobalVariables.color_name),
                SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(GlobalVariables.quantity),
                SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(GlobalVariables.size));


        call.enqueue(new Callback<SocialLoginModel>() {
            @Override
            public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {
                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        if (response.body().getStatus().equals("SUCCESS")) {
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(commaSeparatedProductId, "");
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).getString(SharedPreferenceKey.BATCH_COUNT, "");
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).writeStringValue(SharedPreferenceKey.FULL_NAME, response.body().getSociallogin().getName());
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).writeStringValue(SharedPreferenceKey.EMAIL, (String) response.body().getSociallogin().getEmail());
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).writeStringValue(SharedPreferenceKey.IMAGE, response.body().getSociallogin().getImage());
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).writeStringValue(SharedPreferenceKey.USER_ID, "" + response.body().getSociallogin().getId());
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).writeStringValue(SharedPreferenceKey.TOKEN, response.body().getSociallogin().getToken());
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).writeStringValue(SharedPreferenceKey.CRTEATED_AT, response.body().getSociallogin().getCreatedAt());
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).writeStringValue(SharedPreferenceKey.UPDATED_AT, response.body().getSociallogin().getUpdatedAt());
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
                            SharedPreferenceWriter.getInstance(ProductListActivity.this).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS, true);

                            Intent intent = new Intent(ProductListActivity.this, MainActivity.class).putExtra("from", "LoginButton");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                            CommonUtil.setUpSnackbarMessage(binding.mainRl, response.body().getMessage(), ProductListActivity.this);
                        }
                    } else {
                        myDialog.hideDialog();
                        final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                        mSnackbar.setActionTextColor(ContextCompat.getColor(ProductListActivity.this, R.color.colorWhite));
                        mSnackbar.setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                callLoginApiForSocial(name, fbid, email, profilePhoto, view, socialType);
                                Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                                snackbar.getView().setBackground(ContextCompat.getDrawable(ProductListActivity.this, R.drawable.drawable_gradient_line));
                                snackbar.show();
                            }
                        });
                        mSnackbar.getView().setBackground(ContextCompat.getDrawable(ProductListActivity.this, R.drawable.drawable_gradient_line));
                        mSnackbar.show();

                    }
                }
            }

            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }


    @Override
    public void onBackPressed() {

        Log.e("comeFrom", comeFrom);


        if (type.equalsIgnoreCase(GlobalVariables.flashSale)) {
            finish();
            startActivity(new Intent(this, InternalActivity.class).putExtra("filterData", SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.section_name)).putExtra("from", getIntent().getStringExtra(GlobalVariables.section_name)));
        } else if (comeFrom.equalsIgnoreCase("FilterProductListActivity")) {
            startActivity(new Intent(this, MainActivity.class).putExtra("from", "HomeFragment"));
        } else if (comeFrom.equalsIgnoreCase(GlobalVariables.ProductDetailsActivityFinal)) {
            finish();
        } else if (comeFrom.equalsIgnoreCase("ThreeLevelListAdapter")) {
            finish();
        } else if (comeFrom.equalsIgnoreCase("MenBannerAdapter")) {
            finish();
        } else if (comeFrom.equalsIgnoreCase("MenSectionShopByOccassionAdapter")) {
            finish();
        } else if (comeFrom.equalsIgnoreCase("MenActivity")) {
            finish();
        } else if (comeFrom.equalsIgnoreCase(GlobalVariables.flashSale)) {
            finish();
        } else if (comeFrom.equalsIgnoreCase("ShopByThemeAdapter")) {
            finish();
        } else if (comeFrom.equalsIgnoreCase(GlobalVariables.ProductDetailsActivityFinal)) {
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class).putExtra("from", "HomeFragment"));
        }
    }

    private void callAddToWishlistApi(final View view) {

        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);

        final MyDialog myDialog = new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<MyWishListResponse> call = apiInterface.getAddToWishListResult(token, userId, productId);
        call.enqueue(new Callback<MyWishListResponse>() {
            @Override
            public void onResponse(Call<MyWishListResponse> call, Response<MyWishListResponse> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {

                        callProductListApi("", binding.mainRl);
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(ProductListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(ProductListActivity.this, R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callAddToWishlistApi(view);
                            Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(ProductListActivity.this, R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(ProductListActivity.this, R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<MyWishListResponse> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.productListBackBtn:
                onBackPressed();
                break;

            case R.id.sortProduct:

                sortingOfProduct();

                break;

            case R.id.filterproduct:
                filterProduct();
                break;

            case R.id.ivWishList:

                if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                        SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {

                    openLoginSignUpBottomSheetWhenUserNotLogedIn();

                } else {
                    startActivity(new Intent(this, MyWishListActivity.class));
                }
                break;

            case R.id.ivSearch:
                startActivity(new Intent(ProductListActivity.this, SearchScreenActivity.class));
                break;
        }
    }

    private void sortingOfProduct() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProductListActivity.this);
        View view = ProductListActivity.this.getLayoutInflater().inflate(R.layout.item_sort_by, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.getWindow().findViewById(R.id.bottom_sheet).setBackgroundResource(android.R.color.transparent);

        TextView tvWhatsNew = view.findViewById(R.id.tvWhatsNew);
        TextView tvPopularity = view.findViewById(R.id.tvPopularity);
        TextView tvDiscount = view.findViewById(R.id.tvDiscount);
        TextView tvLowToHigh = view.findViewById(R.id.tvLowToHigh);
        TextView tvHighToLow = view.findViewById(R.id.tvHighToLow);
        TextView tvDeliveryTime = view.findViewById(R.id.tvDeliveryTime);

        tvWhatsNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sortBy = "new";
                callProductListApi(sortBy, binding.mainRl);
                bottomSheetDialog.dismiss();
            }
        });


        tvPopularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sortBy = "popularity";
                callProductListApi(sortBy, binding.mainRl);
                bottomSheetDialog.dismiss();
            }
        });


        tvDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sortBy = "discount";
                callProductListApi(sortBy, binding.mainRl);
                bottomSheetDialog.dismiss();
            }
        });


        tvLowToHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sortBy = "low_to_high";
                callProductListApi(sortBy, binding.mainRl);
                bottomSheetDialog.dismiss();
            }
        });


        tvHighToLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sortBy = "high_to_low";
                callProductListApi(sortBy, binding.mainRl);
                bottomSheetDialog.dismiss();
            }
        });


        tvDeliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sortBy = "delivery_time";
                callProductListApi(sortBy, binding.mainRl);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void filterProduct() {
        String types = "";
        if (comeFrom.equalsIgnoreCase(GlobalVariables.flashSale) || type.equalsIgnoreCase(GlobalVariables.flashSale)) {
            subcatid = "0";
            types = GlobalVariables.flashSale;
            callingIntent("ProductListActivity", binding.tvProductName.getText().toString(), subcatid, types);
        } else if (comeFrom.equalsIgnoreCase("ThreeLevelListAdapter")) {
            subcatid = subsubcatid;
            types = "subsubcat";
            callingIntent("ThreeLevelListAdapter", binding.tvProductName.getText().toString(), subcatid, types);
        } else if (comeFrom.equalsIgnoreCase("SearchAdapter")) {
            subcatid = subsubcatid;
            types = "subsubcat";
            callingIntent(comeFrom, binding.tvProductName.getText().toString(), subcatid, types);
        } else if (comeFrom.equalsIgnoreCase("BrandInFocusAdapter")) {
            subcatid = "";
            types = subcatname;
            callingIntent(comeFrom, binding.tvProductName.getText().toString(), subcatid, types);

        } else if (comeFrom.equalsIgnoreCase("FilteredBrandInFocus")) {
            subcatid = "";
            types = subcatname;
            callingIntent("BrandInFocusAdapter", binding.tvProductName.getText().toString(), subcatid, types);

        } else if (comeFrom.equalsIgnoreCase(GlobalVariables.ProductDetailsActivityFinal)) {

            types = "subcat";
            callingIntent(comeFrom, binding.tvProductName.getText().toString(), subcatid, types);
        } else if (comeFrom.equalsIgnoreCase("ShopByThemeAdapter")) {
            types = "theme";
            subcatid = theme_id;
            callingIntent(comeFrom, binding.tvProductName.getText().toString(), subcatid, types);

        } else if (comeFrom.equalsIgnoreCase("MenSectionShopByOccassionAdapter")) {
            types = "subsubcat";
            callingIntent("MenSectionShopByOccassionAdapter", binding.tvProductName.getText().toString(), subsubcatid, types);
        } else if (comeFrom.equalsIgnoreCase("AutoSlideViewPagerBannerAdapter")) {
            types = "subsubcat";
            callingIntent("AutoSlideViewPagerBannerAdapter", binding.tvProductName.getText().toString(), subsubcatid, types);
        } else if (comeFrom.equalsIgnoreCase(MyFirebaseMessagingService.class.getSimpleName())) {
            types = "subsubcat";
            callingIntent(MyFirebaseMessagingService.class.getSimpleName(), binding.tvProductName.getText().toString(), subsubcatid, types);

        } else {
            types = "subcat";
            callingIntent("ProductListActivity", binding.tvProductName.getText().toString(), subcatid, types);
        }

    }

    private void callingIntent(String from, String section_name, String subcatid, String type) {
        Intent intent = new Intent(this, FilterProductListActivity.class);
        intent.putExtra("from", from);
        intent.putExtra(GlobalVariables.section_name, section_name);
        intent.putExtra(GlobalVariables.subcatid, subcatid);
        Log.e("type:", type);
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


}
