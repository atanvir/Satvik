package com.satvick.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import com.satvick.adapters.BankOfferAdapter;
import com.satvick.adapters.ColorProductDetailAdapter;
import com.satvick.adapters.ProductDescriptionRecycler1Adapter;
import com.satvick.adapters.ProductDescriptionRecycler2Adapter;
import com.satvick.adapters.SelectSizeProductDetailsAdapter;
import com.satvick.adapters.SliderAdapter;
import com.satvick.application.YODApplication;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityProductDetailsFinalBinding;
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
import com.satvick.utils.HelperClass;
import com.satvick.utils.TouchImageView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductDetailsActivityFinal extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 901;
    ActivityProductDetailsFinalBinding binding;
    CallbackManager callbackManager;
    int clickCount = 0;
    List<String> color;
    private List<String> colorList;
    String colorName = "";
    String colorNames = "";
    ColorProductDetailAdapter colorProductDetailAdapter;
    String colorText = null;
    String commaSeparatedProductId = "";
    Double convertedAmout;
    int counts;
    private ProductDetailsResponse data;
    int height = 0;
    boolean isAddToBagClicked = true;
    boolean isChecked = true;
    boolean isValidVariant = true;
    private int lay_height = 0;
    ListPopupWindow listPopupWindow;
    OnDataPass listener;
    private GoogleSignInClient mGoogleSignInClient;
    boolean outOfStock = false;
    ProductDescriptionRecycler1Adapter productDescriptionRecycler1Adapter;
    ProductDescriptionRecycler2Adapter productDescriptionRecycler2Adapter;
    String productId = "";
    String productIds = "";
    String quantities = "";
    SelectSizeProductDetailsAdapter selectSizeProductDetailsAdapter;
    String selectedQuantity = "";
    private List<String> sizeList;
    String sizeName = "";
    String sizeText = null;
    Bitmap sizechart;
    String sizes = "";
    private SliderAdapter sliderAdapter;
    private ArrayList<ProductDetails> strings;
    String symbol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details_final);
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


        listPopupWindow = new ListPopupWindow(this);

        Intent intent = getIntent();
        if (intent != null) {
            productId = getIntent().getStringExtra(GlobalVariables.product_id);
        }

        productIds = SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(GlobalVariables.product_id);
        colorNames = SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(GlobalVariables.color_name);
        sizes = SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(GlobalVariables.size);
        quantities = SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(GlobalVariables.quantity);
        counts = SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getInt(GlobalVariables.count);


        Log.e("product_id", productIds);
        Log.e("colorNames", colorNames);
        Log.e("sizes", sizes);
        Log.e("quantity", quantities);
        Log.e("count", "" + counts);

        callProductDetailsApi(binding.mainRl); //hit api


        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        int scrollX = binding.scrollView.getScrollX(); //for horizontalScrollView
                        int scrollY = binding.scrollView.getScrollY(); //for verticalScrollView

                        int sc = scrollY + height;


                        if (sc <= 898) {
                            binding.llButtonStart.setVisibility(View.VISIBLE);
                        } else {
                            binding.llButtonStart.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        //[fb register]
        fbRegisterCallBack();

        //[google register]
        googleRegister();

    }

    private void googleRegister() {
// Configure sign-in to request the user's ID, email address, and basic profile.
// ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(ProductDetailsActivityFinal.this, gso);
    }

    private void fbRegisterCallBack() {

        //create a callbackManager to handle login responses by calling
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        //Toast.makeText(ProductDetailsActivityFinal.this, R.string.success, Toast.LENGTH_SHORT).show();
                        getBasicProfile(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(ProductDetailsActivityFinal.this, R.string.cancel, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(ProductDetailsActivityFinal.this, R.string.error, Toast.LENGTH_SHORT).show();
                        //Log.e(TAG, "onError: ", exception);
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

                        if (HelperClass.showInternetAlert(ProductDetailsActivityFinal.this)) {
                            callLoginApiForSocial(name, fbid, email, profilePhoto, binding.mainRl, "Facebook");
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void callProductDetailsApi(final View view) {

        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);


        final MyDialog myDialog = new MyDialog(ProductDetailsActivityFinal.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Log.e("gotId", productId);

        Call<ProductDetailsResponse> call = apiInterface.getProductDetailsResult(token, userId, productId);
        call.enqueue(new Callback<ProductDetailsResponse>() {
            @Override
            public void onResponse(Call<ProductDetailsResponse> call, Response<ProductDetailsResponse> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    binding.scrollView.setVisibility(View.VISIBLE);
                    binding.llButtonStart.setVisibility(View.VISIBLE);
                    data = response.body();

                    if (response.body().getStatus().equals("SUCCESS")) {
                        setData(data);
                    } else {
                        Toast.makeText(ProductDetailsActivityFinal.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(ProductDetailsActivityFinal.this, R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callProductDetailsApi(view);
                            Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(ProductDetailsActivityFinal.this, R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(ProductDetailsActivityFinal.this, R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ProductDetailsResponse> call, Throwable t) {

                Log.e("Failure", t.getMessage());
                myDialog.hideDialog();
            }
        });
    }

    private void setData(ProductDetailsResponse data) {
        binding.tvLabelSize.setText(data.getProductdetails().getSize_label());
        convertedAmout = Double.parseDouble(SharedPreferenceWriter.getInstance(this).getString("converted_amount"));
        symbol = SharedPreferenceWriter.getInstance(this).getString("symbol");
        binding.tvBrand.setText(data.getProductdetails().getBrand());
        binding.tvName.setText(data.getProductdetails().getName());
        binding.tvPrice.setText(symbol + " " + Math.round(Double.parseDouble(data.getProductdetails().getSp()) * convertedAmout));
        binding.tvSoldBy.setText(data.getProductdetails().getSoldBy());
        binding.tvHsnCode.setText(data.getProductdetails().getHsnCode());
        List<String> arrayList = new ArrayList();
        arrayList.addAll(data.getProductdetails().getOffers());
        arrayList.addAll(data.getProductdetails().getDetailsOffer());
        ArrayList<String> offerNameList = new ArrayList();
        int i;
        for (i = 0; i < data.getProductdetails().getOffers().size(); i++) {
            offerNameList.add("Bank Offer");
        }
        for (i = 0; i < data.getProductdetails().getDetailsOffer().size(); i++) {
            offerNameList.add("Offer");
        }

        if(!(arrayList.size()>0))
        {
            binding.rlOffer.setVisibility(View.GONE);
        }

        BankOfferAdapter adapter = new BankOfferAdapter(this, arrayList, offerNameList);
        binding.offerRV.setAdapter(adapter);

        ProductDetailsResponse.Attribute attribute = new ProductDetailsResponse.Attribute();
        attribute.setKeyName("Product Details");
        attribute.setKeyValue(data.getProductdetails().getDescription());
        data.getProductdetails().getAttributes().add(0, attribute);

        //prouduct description
        binding.productDesRv.setVisibility(View.GONE);
//        if (data.getProductdetails().getAttributes().size() > 0) {
//            binding.productDesRv.setAdapter(new ProductDescAdapter(this, data.getProductdetails().getAttributes()));
//        } else {
//            binding.productDesRv.setVisibility(View.GONE);
//        }

        if (binding.tvExpectedDelv.getText().equals("1")) {
            binding.tvExpectedDelv.setText("Expected Delivery In " + data.getProductdetails().getDeliveryTime() + " day");
        } else {
            binding.tvExpectedDelv.setText("Expected Delivery In " + data.getProductdetails().getDeliveryTime() + " days");
        }

        if (data.getProductdetails().getPercentage() == 0) {
            binding.tvCuttedText.setVisibility(View.GONE);
            binding.tvOff.setVisibility(View.GONE);
        } else {
            binding.tvOff.setText(MessageFormat.format("({0}% OFF)", data.getProductdetails().getPercentage()));
            binding.tvCuttedText.setText(symbol + " " + Math.round(Double.parseDouble(data.getProductdetails().getMrp()) * convertedAmout));
            binding.tvCuttedText.setPaintFlags(binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
            if (counts == 0) {
                binding.notificationBadge.setVisibility(View.GONE);
            } else {
                binding.notificationBadge.setVisibility(View.VISIBLE);
                binding.notificationBadge.setText(String.valueOf(counts));
            }


        } else {
            int count = data.getProductdetails().getNumOfAddtocart();
            if (count == 0) {
                binding.notificationBadge.setVisibility(View.GONE);
            } else {
                binding.notificationBadge.setVisibility(View.VISIBLE);
                binding.notificationBadge.setText(String.valueOf(count));
            }
        }


        color = data.getProductdetails().getImageList();
        Log.e("color", String.valueOf(color));
        sliderAdapter = new SliderAdapter(this, color);
        binding.viewPager.setAdapter(sliderAdapter);
        binding.indicator.setupWithViewPager(binding.viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ProductDetailsActivityFinal.SliderTimer(), 2000, 3000);


        //in case of out of stock
        if (data.getProductdetails().getQuantity().equalsIgnoreCase("0") || !(Integer.parseInt(data.getProductdetails().getQuantity()) > 0)) {
            outOfStock = true;
            binding.addToBagText.setText("OUT OF STOCK!");
            binding.addToBagText2.setText("OUT OF STOCK!");
            binding.selectQtyText.setVisibility(View.GONE);
            binding.llqty.setVisibility(View.GONE);
        } else {
            binding.addToBagText.setText("ADD TO BAG");
            binding.addToBagText2.setText("ADD TO BAG");
            binding.selectQtyText.setVisibility(View.VISIBLE);
            binding.llqty.setVisibility(View.VISIBLE);
        }

        //wishlist
        if (data.getProductdetails().getWishHave().equalsIgnoreCase("0")) {
            binding.wishListIV2.setImageResource(R.drawable.savedicon8);
            binding.whisListIV1.setImageResource(R.drawable.savedicon8);
        } else {
            binding.wishListIV2.setImageResource(R.drawable._saved_salected);
            binding.whisListIV1.setImageResource(R.drawable._saved_salected);
        }


        colorName = data.getProductdetails().getDefaultcolor();
        colorList = data.getProductdetails().getColor();
        setColorAdapter(colorList);
        sizeList = data.getProductdetails().getSize();
        setSelectSizeAdapter(sizeList);

        //size chart
        // sizechart=DownloadImageFromPath(data.getProductdetails().getSizechart());


        //view similar
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


        //more text section
//
//        binding.moreTextOne.setText(data.getProductdetails().getMorebrand().get(0));
//        binding.moreTextTwo.setText(data.getProductdetails().getMorebrand().get(1));
//        binding.moreTextThree.setText(data.getProductdetails().getMorebrand().get(2));


        //customer also like
        if (data.getProductdetails().getCustomerAlsoLiked().size() > 0) {
            binding.alsoLikeTxt.setVisibility(View.VISIBLE);
            productDescriptionRecycler2Adapter = new ProductDescriptionRecycler2Adapter(this, data.getProductdetails().getCustomerAlsoLiked());
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
            linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.recyclerProductDetail2.setLayoutManager(linearLayoutManager2);
            binding.recyclerProductDetail2.setAdapter(productDescriptionRecycler2Adapter);

        }

    }


    private void setSelectSizeAdapter(final List<String> size) {
        if (size.size() <= 0) {
            this.binding.sizeChartText.setVisibility(View.GONE);
        }

        if (size.size() > 0) {
            selectSizeProductDetailsAdapter = new SelectSizeProductDetailsAdapter(this, size);
            binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerSize.setAdapter(selectSizeProductDetailsAdapter);
            selectSizeProductDetailsAdapter.setListener(new SelectSizeProductDetailsAdapter.SelectSizeListener() {
                @Override
                public void itemClick(View view, int pos) {
                    sizeText = pos + "";
                    sizeName = size.get(pos);
                    getImageByColor(colorName, sizeName);
                }
            });
        } else {
            binding.tvLabelSize.setVisibility(View.GONE);
            binding.recyclerSize.setVisibility(View.GONE);
        }
    }

    private void setColorAdapter(final List<String> colorList) {
//        if (colorList.size() > 0) {
//            colorProductDetailAdapter = new ColorProductDetailAdapter(this, colorList);
//            binding.recyclerColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//            binding.recyclerColor.setAdapter(colorProductDetailAdapter);
//            colorProductDetailAdapter.setListener(new ColorProductDetailAdapter.ColorsChangedListener() {
//                @Override
//                public void itemClick(View view, String colornm, int pos) {
//                    colorText = colornm + "";
//                    colorName = colorList.get(pos);
//                    getImageByColor(colorName, sizeName);
//                }
//            });
//        } else {
//            binding.labelColor.setVisibility(View.GONE);
//            binding.recyclerColor.setVisibility(View.GONE);
//
//        }

        binding.labelColor.setVisibility(View.GONE);
        binding.recyclerColor.setVisibility(View.GONE);

    }

    private void getImageByColor(String colorName, String sizeName) {


        final MyDialog myDialog = new MyDialog(ProductDetailsActivityFinal.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<CommonModel> call = apiInterface.getImageByColor(colorName, productId, sizeName);
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                        myDialog.hideDialog();
                        color = response.body().getResponse().getImageList();
                        if (response.body().getResponse().getPriceDetail().getPercentage().equalsIgnoreCase("") || response.body().getResponse().getPriceDetail().getSp().equalsIgnoreCase("") || response.body().getResponse().getPriceDetail().getMrp().equalsIgnoreCase("")) {
                            isValidVariant = false;
                        } else {
                            isValidVariant = true;
                        }
                        if (!response.body().getResponse().getPriceDetail().getPercentage().equalsIgnoreCase(""))
                            if (response.body().getResponse().getPriceDetail().getPercentage().equalsIgnoreCase("0")) {
                                binding.tvCuttedText.setVisibility(View.GONE);
                                binding.tvOff.setVisibility(View.GONE);
                            } else {
                                binding.tvOff.setText(MessageFormat.format("({0}% OFF)", response.body().getResponse().getPriceDetail().getPercentage()));
                                binding.tvCuttedText.setText(symbol + " " + Math.round(Double.parseDouble(response.body().getResponse().getPriceDetail().getMrp()) * convertedAmout));
                                binding.tvCuttedText.setPaintFlags(ProductDetailsActivityFinal.this.binding.tvCuttedText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            }
                        if (!response.body().getResponse().getPriceDetail().getSp().equalsIgnoreCase("")) {
                            binding.tvPrice.setText(symbol + " " + Math.round(Double.parseDouble(response.body().getResponse().getPriceDetail().getSp()) * convertedAmout));
                        }
                        if (color.size() > 0) {
                            sliderAdapter.setData(ProductDetailsActivityFinal.this.color);
                            binding.scrollView.fullScroll(View.FOCUS_UP);
                        }
                    } else if (response.body().getStatus().equalsIgnoreCase("FAILURE")) {
                        isValidVariant = false;
                        myDialog.hideDialog();
                        CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(), ProductDetailsActivityFinal.this);
                    }

                } else {
                    myDialog.hideDialog();

                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                Log.e("error", t.getMessage());
                myDialog.hideDialog();
            }
        });

    }


    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            if (ProductDetailsActivityFinal.this == null)
                return;
            ProductDetailsActivityFinal.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (binding.viewPager.getCurrentItem() < color.size() - 1) {
                        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                    } else {
                        binding.viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    private void openLoginSignUpBottomSheetWhenUserNotLogedIn() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view1 = this.getLayoutInflater().inflate(R.layout.pop_up_bottom_sheet_login_when_user_not_loged_in, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        // bottomSheetDialog.setCancelable(false);
        // bottomSheetDialog.setCanceledOnTouchOutside(true);

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
                startActivity(new Intent(ProductDetailsActivityFinal.this, LoginActivity.class));
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailsActivityFinal.this, SignUpActivity.class));
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
                    chbSelect.setButtonDrawable(ContextCompat.getDrawable(ProductDetailsActivityFinal.this, R.drawable.sale_box));

                } else {
                    isChecked = true;
                    chbSelect.setButtonDrawable(ContextCompat.getDrawable(ProductDetailsActivityFinal.this, R.drawable.new_rect));
                }
            }
        });


        bottomSheetDialog.show();
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
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
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

            if (HelperClass.showInternetAlert(ProductDetailsActivityFinal.this)) {
                callLoginApiForSocial(name, gid, email, profilePhoto, binding.mainRl, "Google");
            }

            // Signed in successfully,show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private void callLoginApiForSocial(final String name, final String fbid, final String email, final String profilePhoto, final View view, final String socialType) {
        String deviceToken = SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(SharedPreferenceKey.DEVICE_TOKEN);

        final MyDialog myDialog = new MyDialog(ProductDetailsActivityFinal.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<SocialLoginModel> call = apiInterface.socialLogin(fbid, socialType, deviceToken, "android", name, email, profilePhoto, "",
                SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(GlobalVariables.product_id),
                SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(GlobalVariables.color_name),
                SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(GlobalVariables.quantity),
                SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(GlobalVariables.size));

        call.enqueue(new Callback<SocialLoginModel>() {
            @Override
            public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(commaSeparatedProductId, "");
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(SharedPreferenceKey.BATCH_COUNT, "");
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.FULL_NAME, response.body().getSociallogin().getName());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.EMAIL, (String) response.body().getSociallogin().getEmail());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.IMAGE, response.body().getSociallogin().getImage());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.USER_ID, "" + response.body().getSociallogin().getId());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.TOKEN, response.body().getSociallogin().getToken());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.CRTEATED_AT, response.body().getSociallogin().getCreatedAt());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.UPDATED_AT, response.body().getSociallogin().getUpdatedAt());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS, true);

                        Intent intent = new Intent(ProductDetailsActivityFinal.this, MainActivity.class).putExtra("from", "LoginButton");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        CommonUtil.setUpSnackbarMessage(binding.mainRl, response.body().getMessage(), ProductDetailsActivityFinal.this);
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(ProductDetailsActivityFinal.this, R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callLoginApiForSocial(name, fbid, email, profilePhoto, view, socialType);
                            Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(ProductDetailsActivityFinal.this, R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(ProductDetailsActivityFinal.this, R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void callSignupApi(final String name, final String email, final String fbid, final View view) {
        final MyDialog myDialog = new MyDialog(ProductDetailsActivityFinal.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<SignUpModel> call = apiInterface.getSignUpResult(name, email, "", "", "", "Android", SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).getString(SharedPreferenceKey.DEVICE_TOKEN), "",
                SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.product_id),
                SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.color_name),
                SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.quantity),
                SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.size));

        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {


                        //to get all response here,
                        Toast.makeText(ProductDetailsActivityFinal.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        SignUpModel signUpModel = response.body();
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.FULL_NAME, signUpModel.getSignup().getName());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.CONTACT_NUMBER, signUpModel.getSignup().getPhone());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.EMAIL, signUpModel.getSignup().getEmail());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.USER_ID, signUpModel.getSignup().getUser_id());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.TOKEN, signUpModel.getSignup().getToken());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.PASSWORD, signUpModel.getSignup().getPassword());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.CONFIRM_PASSWORD, signUpModel.getSignup().getPassword_confirmation());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.CONFIRM_PASSWORD, signUpModel.getSignup().getCountry());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, signUpModel.getSignup().getDeviceToken());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.DEVICE_TYPE, signUpModel.getSignup().getDeviceType());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.SOCIAL_ID, signUpModel.getSignup().getSocial_id());
                        SharedPreferenceWriter.getInstance(ProductDetailsActivityFinal.this).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");

                        //send at profile fragment,profile fragment is loaded at MainActivity
                        Intent intent = new Intent(ProductDetailsActivityFinal.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        ProductDetailsActivityFinal.this.finishAffinity();
                    } else {
                        Toast.makeText(ProductDetailsActivityFinal.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(ProductDetailsActivityFinal.this, R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callSignupApi(name, fbid, email, view);
                            Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(ProductDetailsActivityFinal.this, R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(ProductDetailsActivityFinal.this, R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivWishlist:
                if (SharedPreferenceWriter.getInstance(this).
                        getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                        SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
                    // Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                    openLoginSignUpBottomSheetWhenUserNotLogedIn();
                } else {
                    startActivity(new Intent(this, MyWishListActivity.class));
                }
                break;

            case R.id.llAddToWishList:
                if (SharedPreferenceWriter.getInstance(this).
                        getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                        SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
                    //  Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                    openLoginSignUpBottomSheetWhenUserNotLogedIn();
                } else {
                    callAddToWishlistApi(binding.mainRl);//hit api
                }
                break;

            case R.id.llAddToWishList2:
                if (SharedPreferenceWriter.getInstance(this).
                        getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                        SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
                    // Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                    openLoginSignUpBottomSheetWhenUserNotLogedIn();
                } else {
                    callAddToWishlistApi(binding.mainRl);//hit api
                }
                break;

            case R.id.llAddToBagOrCart:
                addToBag();

                break;

            case R.id.llAddToBagOrCart2:
                addToBag();


                break;

            case R.id.ivAddToBagOrCart:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("from", "ProductDetailsActivityFinal");
                finish();
                startActivity(intent);

                break;

            case R.id.ivBack:
                finish();

                break;

            case R.id.ivShare:
                shareApp(productId);//Deep linking
                break;

            case R.id.rl:
                shareApp(productId);
                break;

            case R.id.llTerms:
                startActivity(new Intent(this, TermsOfUseActivity.class));
                break;

            case R.id.tvSelectQty:
                Log.e("count", String.valueOf(clickCount));
                if (clickCount % 2 == 0) {
                    clickCount = clickCount + 1;
                    openDropDownList();
                } else {
                    clickCount = clickCount + 1;
                    listPopupWindow.dismiss();

                }

                break;

            case R.id.sizeChartText:
                Intent chartIntent = new Intent(this, SizeChartActivity.class);
                chartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                chartIntent.putParcelableArrayListExtra("sizechart", (ArrayList<? extends Parcelable>) data.getProductdetails().getSizechart());
                startActivity(chartIntent);
                break;

            case R.id.layout_product_detail7:
                callingIntent(GlobalVariables.ProductDetailsActivityFinal, "moreTextOne", binding.moreTextOne.getText().toString(), data.getProductdetails().getSubsubcatid());
                break;

            case R.id.layout_product_detail8:
                callingIntent(GlobalVariables.ProductDetailsActivityFinal, "moreTextTwo", binding.moreTextTwo.getText().toString(), data.getProductdetails().getSubsubcatid());
                break;

            case R.id.layout_product_detail9:
                callingIntent(GlobalVariables.ProductDetailsActivityFinal, "moreTextThree", binding.moreTextThree.getText().toString(), data.getProductdetails().getSubsubcatid());

                break;
        }
    }

    private void callingIntent(String from, String section, String section_name, int subsubcatid) {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("from", from);
        intent.putExtra(GlobalVariables.section, section);
        intent.putExtra(GlobalVariables.section_name, section_name);

        if (section.equalsIgnoreCase("moreTextOne")) {
            intent.putExtra(GlobalVariables.subsubcatid, subsubcatid);
            intent.putExtra(GlobalVariables.brandname, data.getProductdetails().getBrand());
        } else if (section.equalsIgnoreCase("moreTextTwo")) {
            intent.putExtra(GlobalVariables.subsubcatid, subsubcatid);
            intent.putExtra(GlobalVariables.defaultcolor, data.getProductdetails().getDefaultcolor());
        } else if (section.equalsIgnoreCase("moreTextThree")) {
            intent.putExtra(GlobalVariables.subsubcatid, subsubcatid);
        }


        startActivity(intent);

    }

    private void sizeChartPopup(String url_image) {

        new DownloadImage().execute(url_image);

    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        MyDialog myDialog;

        @Override
        protected void onPreExecute() {
            myDialog = new MyDialog(ProductDetailsActivityFinal.this);
            myDialog.showDialog();

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return DownloadImageFromPath(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            sizechart = bitmap;
            myDialog.hideDialog();
            final Dialog dialog = new Dialog(ProductDetailsActivityFinal.this, android.R.style.Theme_Black);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_zoom_image_pop_up);
            TouchImageView imageView = dialog.findViewById(R.id.imageView);

            ImageView ivClose = dialog.findViewById(R.id.ivClose);

            imageView.setImageBitmap(sizechart);

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

    public Bitmap DownloadImageFromPath(String path) {
        InputStream in = null;
        Bitmap bmp = null;

        int responseCode = -1;
        try {

            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();

            }

        } catch (Exception ex) {

        }
        return bmp;
    }


    private void CommonMessagePopup(String message, String status) {
        final Dialog dialog = new Dialog(ProductDetailsActivityFinal.this, android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_mesage_popup);
        ImageView closeiv = dialog.findViewById(R.id.closeiv);
        TextView messagetxt = dialog.findViewById(R.id.messagetxt);
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottieAnimationView);
        if (message.equalsIgnoreCase("Please select "+binding.tvLabelSize.getText().toString().trim())) {
            lottieAnimationView.setAnimation("warning.json");
            messagetxt.setText(message);

        } else if (message.equalsIgnoreCase(getString(R.string.please_select_color))) {
            lottieAnimationView.setAnimation("warning.json");
            messagetxt.setText(message);

        } else if (message.equalsIgnoreCase(getString(R.string.added_to_cart))) {
            lottieAnimationView.setAnimation("done.json");
            messagetxt.setText(message);
        } else if (message.equalsIgnoreCase(getString(R.string.added_to_cart_already))) {
            lottieAnimationView.setAnimation("warning.json");
            messagetxt.setText(message);
        } else if (message.equalsIgnoreCase(getString(R.string.out_of_stock))) {
            lottieAnimationView.setAnimation("warning.json");
            messagetxt.setText(message);
        } else {
            if (status.equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                lottieAnimationView.setAnimation("done.json");
                messagetxt.setText(message);
            } else if (status.equalsIgnoreCase(GlobalVariables.FAILURE)) {
                lottieAnimationView.setAnimation("error.json");
                messagetxt.setText(message);
            }

        }

        Button okbtn = dialog.findViewById(R.id.okbtn);

        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void addToBag() {
        if (outOfStock) {
            CommonMessagePopup(getString(R.string.out_of_stock), "");
        } else if (sizeText == null && sizeList.size() > 0) {
            CommonMessagePopup("Please select " + binding.tvLabelSize.getText().toString().trim(), "");
        } else if (!isValidVariant) {
            CommonMessagePopup("Not valid product variant", "FAILURE");
        } else {
            if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                    SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
                boolean isProductExists = false;

                if (isAddToBagClicked) {
                    isAddToBagClicked = false;

                    strings = new ArrayList<>();
                    if (selectedQuantity.equalsIgnoreCase("")) {
                        selectedQuantity = "1";
                    }
                    if(colorName!=null) {
                        if (colorName.equalsIgnoreCase("")) {
                            colorName = data.getProductdetails().getDefaultcolor();
                        }
                    }

                    strings.add(new ProductDetails(productId, colorName, sizeName, selectedQuantity));

                    if (((YODApplication) getApplication()).getHugeData() != null && ((YODApplication) getApplication()).getHugeData().size() >= 0) {

                        //if product is already in bag

                        ArrayList<ProductDetails> details = ((YODApplication) getApplication()).getHugeData();
                        for (int i = 0; i < details.size(); i++) {
                            if (!strings.get(0).getProduct_id().equalsIgnoreCase(details.get(i).getProduct_id())) {
                                strings.add(((YODApplication) getApplication()).getHugeData().get(i));
                            } else {
                                isProductExists = true;
                            }

                        }
                    }

                    String product_id = "", color_name = "", size = "", quantity = "";
                    int count = strings.size();
                    for (int i = 0; i < strings.size(); i++) {
                        product_id += strings.get(i).getProduct_id();
                        color_name += strings.get(i).getColor_name();
                        size += strings.get(i).getSize();
                        quantity += strings.get(i).getQuantity();
                        if (strings.size() - 1 > i) {
                            product_id += ",";
                            color_name += ",";
                            size += ",";
                            quantity += ",";

                        }

                    }
                    SharedPreferenceWriter.getInstance(getApplicationContext()).writeIntValue(GlobalVariables.count, count);
                    SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(GlobalVariables.product_id, product_id);
                    SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(GlobalVariables.color_name, color_name);
                    SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(GlobalVariables.size, size);
                    SharedPreferenceWriter.getInstance(getApplicationContext()).writeStringValue(GlobalVariables.quantity, quantity);


                    //save data using setter Method


                    ((YODApplication) getApplication()).setHugeData(strings);
                    if (isProductExists) {
                        CommonMessagePopup(getString(R.string.added_to_cart_already), "");
                    } else {
                        CommonMessagePopup(getResources().getString(R.string.added_to_cart), "");
                    }


                    if (count == 0) {
                        binding.notificationBadge.setVisibility(View.GONE);
                    } else {
                        binding.notificationBadge.setVisibility(View.VISIBLE);
                        binding.notificationBadge.setText("" + count);
                    }

                } else {
                    CommonMessagePopup(getString(R.string.added_to_cart_already), "");
                }

            } else {
                if (isAddToBagClicked) {
                    isAddToBagClicked = false;
                    final MyDialog myDialog = new MyDialog(this);
                    callAddToCartOrBaglistApi();//hit api
                } else {
                    CommonMessagePopup(getString(R.string.added_to_cart_already), "");
                }
            }
        }
    }


    private void openDropDownList() {
        int loopsize = 0;
        if (Integer.parseInt(data.getProductdetails().getQuantity()) > 5) {
            loopsize = 5;

        } else {
            loopsize = Integer.parseInt(data.getProductdetails().getQuantity());

        }

        final List<String> selectedMenuList = new ArrayList<>();
        for (int i = 0; i < loopsize; i++) {
            selectedMenuList.add("" + (i + 1));
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, selectedMenuList);


        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedQuantity = selectedMenuList.get(i);
                binding.tvSelectQty.setText(selectedQuantity);
                clickCount = clickCount - 1;
                listPopupWindow.dismiss();
            }
        });


        listPopupWindow.setAnchorView(binding.tvSelectQty);
        listPopupWindow.setAdapter(arrayAdapter);
        listPopupWindow.show();


    }


    private void shareApp(String productId) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        // String shareLing="https://jobyoda.page.link/?link=http://mobuloustech.com/jobyoda/api/"+JobPostId+"&apn=com.jobyodamo&isi=1471619860&ibi=com.Jobyoda.app";
        // String shareLing="Job Description : "+"\n"+"https://jobyoda.page.link/?link=https://www.jobyoda.com/api/"+productId+"&apn=com.jobyodamo&isi=1471619860&ibi=com.Jobyoda.app";

        // String shareLing="Product Description : "+"\n"+"https://yod.page.link/?link=http://mobuloustech.com/yodapi/api/&apn=com.yod&isi=1481825964&ibi=com.mobulous.YOD/"+productId;
        // String shareLing="https://yod.page.link/?link=http://mobuloustech.com/yodapi/api/id&apn=com.yod&isi=1481825964&ibi=com.mobulous.YOD/"+productId;
        // String shareLing="https://yod2.page.link/?link=http://mobuloustech.com/yodapi/api/&apn=com.yod&isi=1481825964&ibi=com.mobulous.YOD/"+productId;
        String shareLing = "https://yod.page.link/?link=http://mobuloustech.com/yodapi/api/" + productId + "&apn=com.satvick&isi=1481825964&ibi=com.mobulous.YOD";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareLing);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
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
                        if (response.body().getMessage().equalsIgnoreCase("Product removed from wishlist successfully")) {
                            binding.whisListIV1.setImageResource(R.drawable.savedicon8);
                            binding.wishListIV2.setImageResource(R.drawable.savedicon8);
                        } else {
                            binding.whisListIV1.setImageResource(R.drawable._saved_salected);
                            binding.wishListIV2.setImageResource(R.drawable._saved_salected);
                        }


                    } else {

                        Toast.makeText(ProductDetailsActivityFinal.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(ProductDetailsActivityFinal.this, R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callAddToWishlistApi(view);
                            Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(ProductDetailsActivityFinal.this, R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(ProductDetailsActivityFinal.this, R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                }
            }

            @Override
            public void onFailure(Call<MyWishListResponse> call, Throwable t) {
                myDialog.hideDialog();
            }
        });

    }

    private void callAddToCartOrBaglistApi() {

        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);
        final MyDialog myDialog = new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        if (selectedQuantity.equalsIgnoreCase("")) {
            selectedQuantity = "1";
        }

        if(colorName!=null){
            if (colorName.equalsIgnoreCase("")) {
                if(data!=null) {
                    if (data.getProductdetails() != null) {
                        colorName = data.getProductdetails().getDefaultcolor();
                    }
                }
            }
        }

        Call<CartListModel2> call = apiInterface.getAddToCartResult3(token, userId, productId, colorName, sizeName, selectedQuantity);
        call.enqueue(new Callback<CartListModel2>() {
            @Override
            public void onResponse(Call<CartListModel2> call, Response<CartListModel2> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();


                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        CommonMessagePopup(response.body().getMessage(), GlobalVariables.SUCCESS);
                        if (response.body().getAddtocart().getTotalItemsInCart() > 0) {
                            binding.notificationBadge.setVisibility(View.VISIBLE);
                            binding.notificationBadge.setText(String.valueOf(response.body().getAddtocart().getTotalItemsInCart()));

                        } else {
                            binding.notificationBadge.setVisibility(View.GONE);
                        }

                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        CommonMessagePopup(response.body().getMessage(), GlobalVariables.FAILURE);
                    }
                } else {
                    myDialog.hideDialog();
                }
            }

            @Override
            public void onFailure(Call<CartListModel2> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }


    public interface OnDataPass {
        void onSuccess(String str);
    }
}

