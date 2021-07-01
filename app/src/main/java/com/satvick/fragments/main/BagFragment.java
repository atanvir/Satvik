package com.satvick.fragments.main;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.satvick.activities.ApplyCouponActivity;
import com.satvick.activities.LoginActivity;
import com.satvick.activities.WishListActivity;
import com.satvick.activities.OrderConfirmationActivity;
import com.satvick.activities.ProductDetailActivity;
import com.satvick.activities.SearchScreenActivity;
import com.satvick.activities.SignUpActivity;
import com.satvick.adapters.CartListAdapter;
import com.satvick.ccavenue.WebViewActivity;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.FragmentBagBinding;
import com.satvick.databinding.PopUpBottomSheetLoginWhenUserNotLogedInBinding;
import com.satvick.model.BillingModel;
import com.satvick.model.CancelCouponModel;
import com.satvick.model.CartListModel;
import com.satvick.model.CartListModelResponse;
import com.satvick.model.MyWishListResponse;
import com.satvick.model.ProductDetails;
import com.satvick.model.SocialLoginModel;
import com.satvick.model.UpdateCartQuantity;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.utils.BillingHelper;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.GuestUserData;
import com.satvick.utils.HelperClass;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class BagFragment extends Fragment implements View.OnClickListener, FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback, CartListAdapter.CartItemClickListener {

    private FragmentBagBinding binding;
    private String productIds = "",symbol="", coponCode = "",colorNames = "", sizes = "", quantities = "",gift_wrapup_status="";
    private List<CartListModel.ProductListRetrievedSuccessfully> cartListModelList = new ArrayList<>();
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    private TextView tvBadge;
    private double convertedPrice=0.0, total = 0.0,giftWrapPrice = 0.0,couponDiscount=0.0,subTotal=0.0;
    private BottomSheetDialog bottomSheetDialog;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 901;
    public BagFragment(){ }
    public BagFragment(TextView tvBadge){
        this.tvBadge=tvBadge;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBagBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        init();
        initCtrl();
        if (HelperClass.showInternetAlert(getActivity())) { binding.progressBar.setVisibility(View.VISIBLE); callCartListApi();}
    }

    private void init() {
        callbackManager = CallbackManager.Factory.create();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build());


        symbol = SharedPreferenceWriter.getInstance(getActivity()).getString("symbol");
        convertedPrice = Double.parseDouble(SharedPreferenceWriter.getInstance(getActivity()).getString("converted_amount"));
        gift_wrapup_status = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.gift_wrapup_status);
        productIds = SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.product_id);
        colorNames = SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.color_name);
        sizes = SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.size);
        quantities = SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.quantity);

        if (gift_wrapup_status.equalsIgnoreCase("yes")) {
            binding.llgiftwrap.setBackground(getActivity().getDrawable(R.drawable.drawable_gradient_line));
            binding.tvApplyy.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
            binding.giftWrapIv.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.close));
            binding.giftWrapIv.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.drawable_circle));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) binding.giftWrapIv.setBackgroundTintList(getActivity().getColorStateList(android.R.color.white));
        }
    }
    private void initCtrl() {
        binding.tvPlaceOrder.setOnClickListener(this);
        binding.tvWishList.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.llgiftwrap.setOnClickListener(this);
        binding.llaa.setOnClickListener(this);
        binding.llgiftwrap.setOnClickListener(this);
        binding.giftWrapIv.setOnClickListener(this);
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        case R.id.ivCross: bottomSheetDialog.dismiss(); break;
        case R.id.btnLogin: bottomSheetDialog.dismiss(); CommonUtil.startNewActivity(requireActivity(),LoginActivity.class); break;
        case R.id.btnSignUp: bottomSheetDialog.dismiss(); CommonUtil.startNewActivity(requireActivity(),SignUpActivity.class); break;
        case R.id.ivFb: bottomSheetDialog.dismiss(); facebookLogin(); break;
        case R.id.ivGoogle: bottomSheetDialog.dismiss(); googleSignIn(); break;
        case R.id.ivSearch: CommonUtil.startNewActivity(requireActivity(), SearchScreenActivity.class); break;

        case R.id.tvPlaceOrder:
        if(CommonUtil.isUserLogin(requireActivity())) openLoginSignUpBottomSheetWhenUserNotLogedIn();
        else intentForOrderConfirmation();
        break;

        case R.id.tvWishList:
        if(CommonUtil.isUserLogin(requireActivity())) openLoginSignUpBottomSheetWhenUserNotLogedIn();
        else {
            Intent intent=new Intent(getActivity(),WishListActivity.class);
            intent.putExtra("isRefresh",true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        break;

        case R.id.llaa: startActivityForResult(new Intent(getActivity(), ApplyCouponActivity.class), 121); break;
        case R.id.ivBack: break;
        case R.id.llgiftwrap: removeGiftWrap(); break;
        case R.id.couponImg: if(!coponCode.equalsIgnoreCase("")) cancelCouponApi(); break;
        case R.id.giftWrapIv: changeGiftWrapStatus(); break;
        }
    }

    private void callCartListApi() {
        Call<CartListModel> call = apiInterface.getCartListResult(HelperClass.getCacheData(requireActivity()).first,
                                                                  HelperClass.getCacheData(requireActivity()).second,
                                                                  productIds, colorNames, quantities, sizes,
                                                                  gift_wrapup_status.equalsIgnoreCase("yes")?"1":"0");
        call.enqueue(new Callback<CartListModel>() {
            public void onResponse(Call<CartListModel> call, Response<CartListModel> response) {
                if(getActivity()!=null) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body());
                        else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)){
                            SharedPreferenceWriter.getInstance(getActivity()).clearPreferenceValue(SharedPreferenceKey.gift_wrapup_status, "no");
                            SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(GlobalVariables.COUPON_APPLIED, false);
                            hideSectionVisiblity();
                        }
                    } else CommonUtil.setUpSnackbarMessage(binding.getRoot(), "Internal Server Error!", requireActivity());
                }
            }

            @Override
            public void onFailure(Call<CartListModel> call, Throwable t) {
                if(getActivity()!=null) {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    private void removeToCartApi(String productId,String size){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<CartListModelResponse> call = apiInterface.removeToCart(HelperClass.getCacheData(requireActivity()).second,
                                                                     HelperClass.getCacheData(requireActivity()).first,
                                                                     productId,size);
        call.enqueue(new Callback<CartListModelResponse>() {
            @Override
            public void onResponse(Call<CartListModelResponse> call, Response<CartListModelResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
//                        cartListModelList.clear();
                        callCartListApi();
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        binding.progressBar.setVisibility(View.GONE);
                        CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),requireActivity());}
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",requireActivity());
                }
            }

            @Override
            public void onFailure(Call<CartListModelResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),requireActivity());
            }
        });
    }
    private void updateCartApi(int pos,String quantity) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<UpdateCartQuantity> call = apiInterface.updatecart(HelperClass.getCacheData(requireActivity()).first,
                                                                HelperClass.getCacheData(requireActivity()).second,
                                                                cartListModelList.get(pos).getProductId(), quantity,
                                                                cartListModelList.get(pos).getSize()!=null?cartListModelList.get(pos).getSize():"");
        call.enqueue(new Callback<UpdateCartQuantity>() {
            @Override
            public void onResponse(Call<UpdateCartQuantity> call, Response<UpdateCartQuantity> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)){ callCartListApi();}
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        binding.progressBar.setVisibility(View.GONE);
                        CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),requireActivity());
                    }
                }else{
                    binding.progressBar.setVisibility(View.GONE);
                    CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",requireActivity());
                }
            }

            @Override
            public void onFailure(Call<UpdateCartQuantity> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),requireActivity());
            }
        });
    }
    private void cancelCouponApi() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<CancelCouponModel> call=apiInterface.cancelCoupon(HelperClass.getCacheData(requireActivity()).second);
        call.enqueue(new Callback<CancelCouponModel>() {
            public void onResponse(Call<CancelCouponModel> call, Response<CancelCouponModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) { callCartListApi(); }
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        binding.progressBar.setVisibility(View.GONE);
                        CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),requireActivity());
                    }
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                    CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",requireActivity());
                }
            }

            public void onFailure(Call<CancelCouponModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),requireActivity());
            }
        });
    }
    private void callLoginApiForSocial(final String name, final String fbid, final String email, final String profilePhoto, final String socialType) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<SocialLoginModel> call = apiInterface.socialLogin(fbid, socialType,
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.DEVICE_TOKEN),
                                                                "android", name, email, profilePhoto, "",
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.product_id),
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.color_name),
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.quantity),
                                                                SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.size));

        call.enqueue(new Callback<SocialLoginModel>() {
            @Override
            public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) CommonUtil.saveData(requireActivity(),response);
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(), getActivity());
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",requireActivity());

            }

            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),requireActivity());
            }
        });
    }

    private void callAddToWishlistApi (String productId,String size){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<MyWishListResponse> call = apiInterface.getAddToWishListResult(HelperClass.getCacheData(requireActivity()).first,
                                                                            HelperClass.getCacheData(requireActivity()).second,
                                                                            productId,size);
        call.enqueue(new Callback<MyWishListResponse>() {
            @Override
            public void onResponse(Call<MyWishListResponse> call, Response<MyWishListResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) callCartListApi();
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        binding.progressBar.setVisibility(View.GONE);
                        CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),requireActivity());
                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",requireActivity());
                }
            }

            @Override
            public void onFailure(Call<MyWishListResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),requireActivity());
            }
        });
    }

    private void setDataToUI(CartListModel data) {
        cartListModelList = data.getProductListRetrievedSuccessfully();
        if (cartListModelList.isEmpty()) hideSectionVisiblity();
        else {
            binding.llBagListEmpty.setVisibility(View.GONE);
            binding.tvPlaceOrder.setVisibility(View.VISIBLE);
            binding.scrollView.setVisibility(View.VISIBLE);
            tvBadge.setVisibility(View.VISIBLE);
            tvBadge.setText(String.valueOf(cartListModelList.size()));
        }

        // Cart Items
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(new CartListAdapter(getActivity(), cartListModelList, this));

        // Billing
        total=0.0;
        couponDiscount=0.0;
        giftWrapPrice=0.0;
        for (int i = 0; i < cartListModelList.size(); i++) {
            total += Double.parseDouble(""+(cartListModelList.get(i).getActualPrice().isEmpty()?"0":cartListModelList.get(i).getActualPrice())) * Double.parseDouble(""+cartListModelList.get(i).getQuantity());
            couponDiscount += (Double.parseDouble(cartListModelList.get(i).getDiscount_coupon()));
            coponCode=cartListModelList.get(i).getCoupon_code();
            if(gift_wrapup_status.equalsIgnoreCase("yes")) giftWrapPrice += Integer.parseInt("30");
        }

        if(giftWrapPrice>0.0) binding.llGiftWrapPrice.setVisibility(View.VISIBLE);
        else binding.llGiftWrapPrice.setVisibility(View.GONE);
        if (couponDiscount > 0.0) {
            binding.llCouponApply.setVisibility(View.VISIBLE);
            binding.couponImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.close));
            binding.llaa.setBackground(getActivity().getDrawable(R.drawable.drawable_gradient_line));
            binding.couponImg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.drawable_circle));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) binding.couponImg.setBackgroundTintList(getActivity().getColorStateList(android.R.color.white));
            this.binding.tvApply.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
            binding.tvApply.setText("Applied code:" + coponCode);
            binding.couponImg.setOnClickListener(this);

        } else {
            binding.llCouponApply.setVisibility(View.GONE);
            binding.couponImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.back_buttom));
            binding.llaa.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBagFragBackground));
            binding.tvApply.setTextColor(getActivity().getResources().getColor(R.color.colorBlack));
            binding.tvApply.setText("Apply Coupon");
            binding.couponImg.setOnClickListener(null);
        }

        subTotal=total+giftWrapPrice-couponDiscount;
        binding.tvOrderTotal.setText("" + Math.round(total*convertedPrice));
        binding.tvGiftWrapPrice.setText("+" + Math.round(giftWrapPrice*convertedPrice));
        binding.tvCouponDiscount.setText("-" + Math.round(couponDiscount*convertedPrice));
        binding.tvTotalPrice.setText(symbol+" "+ Math.round(subTotal*convertedPrice));

        BillingHelper.getInstance().saveBillingData(new BillingModel(productIds, quantities, coponCode, ""+Math.round(couponDiscount), ""+Math.round(giftWrapPrice*convertedPrice), "", ""+Math.round(total*convertedPrice), ""+Math.round(subTotal*convertedPrice)));

    }
    private void hideSectionVisiblity() {
        binding.scrollView.setVisibility(View.GONE);
        binding.llBagListEmpty.setVisibility(View.VISIBLE);
        binding.tvPlaceOrder.setVisibility(View.GONE);
        tvBadge.setVisibility(View.GONE);
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.gift_wrapup_status, "no");
        SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(GlobalVariables.COUPON_APPLIED, false);
    }
    private void removeOfflineProducts(String productId, int pos,View view,String existingSize) {
        binding.progressBar.setVisibility(View.VISIBLE);
        List<ProductDetails> details = GuestUserData.getInstance().getHugeData();
        if (details!=null && details.size()>0) {
            for (int i = 0; i < details.size(); i++) {
                if (details.get(i).getProduct_id().equalsIgnoreCase(productId) && details.get(i).getSize().equalsIgnoreCase(existingSize)) {
                    details=GuestUserData.getInstance().removeData(i);
                    cartListModelList.remove(pos);
                    binding.recyclerView.getAdapter().notifyItemRemoved(pos);
                    CommonUtil.setUpSnackbarMessage(view,"Product Removed",getActivity());
                }
            }

            List<String> productList=new ArrayList<>();
            List<String> sizeList=new ArrayList<>();
            List<String> quantityList=new ArrayList<>();
            for(int i=0;i<details.size();i++){
                productList.add(details.get(i).getProduct_id());
                sizeList.add(details.get(i).getSize());
                quantityList.add(details.get(i).getQuantity());
            }
            SharedPreferenceWriter.getInstance(requireActivity()).writeIntValue(GlobalVariables.count, details.size());
            SharedPreferenceWriter.getInstance(requireActivity()).writeStringValue(GlobalVariables.product_id, TextUtils.join(",",productList));
            SharedPreferenceWriter.getInstance(requireActivity()).writeStringValue(GlobalVariables.color_name, "");
            SharedPreferenceWriter.getInstance(requireActivity()).writeStringValue(GlobalVariables.size, TextUtils.join(",",sizeList));
            SharedPreferenceWriter.getInstance(requireActivity()).writeStringValue(GlobalVariables.quantity,TextUtils.join(",",quantityList));

            if (cartListModelList.isEmpty()) {
                SharedPreferenceWriter.getInstance(getActivity()).clearPreferenceValue(SharedPreferenceKey.gift_wrapup_status, "no");
                SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(GlobalVariables.COUPON_APPLIED, false);
                hideSectionVisiblity();
            }
            else tvBadge.setText(""+details.size());
            binding.progressBar.setVisibility(View.GONE);

        }
    }
    private void openLoginSignUpBottomSheetWhenUserNotLogedIn() {
        bottomSheetDialog = new BottomSheetDialog(requireActivity());
        PopUpBottomSheetLoginWhenUserNotLogedInBinding binding= PopUpBottomSheetLoginWhenUserNotLogedInBinding.inflate(LayoutInflater.from(requireActivity()), null);
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        binding.ivCross.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
        binding.ivFb.setOnClickListener(this);
        binding.ivGoogle.setOnClickListener(this);
        bottomSheetDialog.show();
    }
    private void facebookLogin() {
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList( "public_profile", "email"));
    }
    private void googleSignIn() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void getBasicProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }
    private void changeGiftWrapStatus() {
        if(gift_wrapup_status.equalsIgnoreCase("yes"))
        {
            gift_wrapup_status="no";
            binding.llgiftwrap.setBackground(null);
            binding.llgiftwrap.setOnClickListener(this);
            binding.llgiftwrap.setBackgroundColor(getActivity().getResources().getColor(R.color.colorBagFragBackground));
            binding.tvApplyy.setTextColor(getActivity().getResources().getColor(R.color.colorBlack));
            binding.giftWrapIv.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.back_buttom));
            SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.gift_wrapup_status,"no");
            binding.giftWrapIv.setBackground(null);
            binding.giftWrapIv.setBackgroundTintList(null);
            binding.giftWrapIv.setOnClickListener(null);
            binding.progressBar.setVisibility(View.VISIBLE);
            callCartListApi();
        }
    }
    private void removeGiftWrap() {
        binding.llgiftwrap.setOnClickListener(null);
        gift_wrapup_status="yes";
        binding.llgiftwrap.setBackground(getActivity().getDrawable(R.drawable.drawable_gradient_line));
        binding.tvApplyy.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.gift_wrapup_status,"yes");
        binding.giftWrapIv.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.close));
        binding.giftWrapIv.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.drawable_circle));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) binding.giftWrapIv.setBackgroundTintList(getActivity().getColorStateList(android.R.color.white));
        binding.giftWrapIv.setOnClickListener(this);
        binding.progressBar.setVisibility(View.VISIBLE);
        callCartListApi();
    }

    private void intentForOrderConfirmation() {
        BillingHelper.getInstance().saveBillingData(new BillingModel(productIds, quantities, coponCode, ""+Math.round(couponDiscount), ""+Math.round(giftWrapPrice*convertedPrice), "", ""+Math.round(total*convertedPrice), ""+Math.round(subTotal*convertedPrice)));

        Intent intent = new Intent(getActivity(), OrderConfirmationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onSuccess(LoginResult loginResult) {
        getBasicProfile(loginResult);
    }

    @Override
    public void onCancel() {
        Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        if (HelperClass.showInternetAlert(getActivity())) callLoginApiForSocial(object.optString("name"), object.optString("id"), object.optString("email"),  "https://graph.facebook.com/" +  object.optString("id") + "/picture?type=large", "Facebook");
    }

    @Override
    public void onRemoveItemClick(View view, int pos) {
        if(CommonUtil.isUserLogin(requireActivity())) removeOfflineProducts(cartListModelList.get(pos).getProductId(), pos, binding.mainRl,cartListModelList.get(pos).getSize());
        else removeToCartApi(cartListModelList.get(pos).getProductId(),cartListModelList.get(pos).getSize()!=null?cartListModelList.get(pos).getSize():"");
    }

    @Override
    public void onMoveToWishListItemClick(View view, int pos) {
        if(CommonUtil.isUserLogin(requireActivity())) openLoginSignUpBottomSheetWhenUserNotLogedIn();
        else callAddToWishlistApi(cartListModelList.get(pos).getProductId(),cartListModelList.get(pos).getSize()!=null?cartListModelList.get(pos).getSize():"");
    }

    @Override
    public void onImageItemClick(View view, int pos) {
        startActivity(new Intent(getActivity(), ProductDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("product_id", cartListModelList.get(pos).getProductId()));
    }

    @Override
    public void setTotalPrice(View view, Map<String, String> map, String size) {
        binding.progressBar.setVisibility(View.VISIBLE);
        productIds=TextUtils.join(",",map.keySet().toArray());
        quantities=TextUtils.join(",",map.values().toArray());
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.quantity,quantities);
        callCartListApi();
    }

    @Override
    public void updateCart(int pos,String quantity) {
        updateCartApi(pos,quantity);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (HelperClass.showInternetAlert(getActivity())) {
                callLoginApiForSocial(account.getGivenName(), account.getId(), account.getEmail(),account.getPhotoUrl().toString(),"Google");
            }
        } catch (ApiException e) {
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),e.getMessage(),requireActivity());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RC_SIGN_IN: handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data)); break;
            case 121: if(resultCode==RESULT_OK){ if(HelperClass.showInternetAlert(getActivity())) { binding.progressBar.setVisibility(View.VISIBLE); callCartListApi(); }} break;
        }
    }
}
