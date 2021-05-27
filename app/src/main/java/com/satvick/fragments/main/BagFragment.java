package com.satvick.fragments.main;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.activities.ApplyCouponActivity;
import com.satvick.activities.LoginActivity;
import com.satvick.activities.MainActivity;
import com.satvick.activities.MyWishListActivity;
import com.satvick.activities.PlaceOrderAddressActivity;
import com.satvick.activities.ProductDetailsActivityFinal;
import com.satvick.activities.SignUpActivity;
import com.satvick.adapters.CartListAdapter;
import com.satvick.adapters.OfferAdapter;
import com.satvick.application.YODApplication;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.FragmentBagBinding;
import com.satvick.model.CancelCouponModel;
import com.satvick.model.CartListModel;
import com.satvick.model.CartListModelResponse;
import com.satvick.model.MyWishListResponse;
import com.satvick.model.ProductDetails;
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
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class BagFragment extends Fragment implements View.OnClickListener {
    View rootView;
    FragmentBagBinding binding;
    List<CartListModel.ProductListRetrievedSuccessfully> cartListModelList = new ArrayList<>();
    CartListAdapter cartListAdapter;
    String productId = "";
    String token = "";
    String userId = "";
    String commaSeparatedProductId = "";
    private Double couponDiscount = 0.0;
    boolean isChecked = true;
    double totalPrice = 0.0;
    String mTotalPrice = "";
    String grandTotal = "";
    CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 901;
    String gift_wrapup_status = "";
    boolean giftwrap = false;
    String cartProduct_id = "", cartProduct_quantity = "";
    Bundle bundle;
    String coupan_code = "";
    String percantageDiscount = "";
    String min_price_coupon = "";
    String symbol;
    private Double convertedPrice;
    private String coponCode = "";
    String productIds = "", colorNames = "", sizes = "", quantities = "", giftwrap2 = "0";
    private TextView tvBadge;
    public BagFragment(){

    }

    public BagFragment(TextView tvBadge){
        this.tvBadge=tvBadge;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bag, container, false);
        rootView = binding.getRoot();
        symbol = SharedPreferenceWriter.getInstance(getActivity()).getString("symbol");
        convertedPrice = Double.parseDouble(SharedPreferenceWriter.getInstance(getActivity()).getString("converted_amount"));
        init();
        fbRegisterCallBack();
        googleRegister();
        if (HelperClass.showInternetAlert(getActivity())) {
            callCartListApi(giftwrap2);
        }

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        binding.tvPlaceOrder.setOnClickListener(this);
        binding.tvWishList.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.llgiftwrap.setOnClickListener(this);
        binding.llaa.setOnClickListener(this);
        binding.llgiftwrap.setOnClickListener(this);


        mTotalPrice = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.TOTAL_PRICE);
        gift_wrapup_status = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.gift_wrapup_status);
        productIds = SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.product_id);
        colorNames = SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.color_name);
        sizes = SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.size);
        quantities = SharedPreferenceWriter.getInstance(getActivity()).getString(GlobalVariables.quantity);

        if (SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
              SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
            token = "1";
            userId = "1";
        } else {
            token = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.TOKEN);
            userId = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.USER_ID);
        }

        gift_wrapup_status = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.gift_wrapup_status);
        if (gift_wrapup_status.equalsIgnoreCase("yes")) {
            giftwrap2 = "1";
            binding.llgiftwrap.setBackground(getActivity().getDrawable(R.drawable.drawable_gradient_line));
            binding.tvApplyy.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
            binding.giftWrapIv.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.close));
            binding.giftWrapIv.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.drawable_circle));
            binding.giftWrapIv.setBackgroundTintList(getActivity().getColorStateList(android.R.color.white));
            binding.giftWrapIv.setOnClickListener(this);
        }
    }

    private void googleRegister() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
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

                        if (HelperClass.showInternetAlert(getActivity())) {
                            callLoginApiForSocial(name, fbid, email, profilePhoto, "Facebook");
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void callLoginApiForSocial(final String name, final String fbid, final String email, final String profilePhoto, final String socialType) {
        String deviceToken = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.DEVICE_TOKEN);

        final MyDialog myDialog = new MyDialog(getActivity());
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<SocialLoginModel> call = apiInterface.socialLogin(fbid, socialType, deviceToken, "android", name, email, profilePhoto, "",
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
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        CommonUtil.setUpSnackbarMessage(binding.mainRl, response.body().getMessage(), getActivity());
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(binding.mainRl, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            callLoginApiForSocial(name, fbid, email, profilePhoto, socialType);
                            Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }


    private void callCartListApi(String giftWrap) {
        final MyDialog myDialog = new MyDialog(getActivity());
        myDialog.showDialog();

        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<CartListModel> call = apiInterface.getCartListResult(token, userId, productIds, colorNames, quantities, sizes, giftWrap);
        call.enqueue(new Callback<CartListModel>() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<CartListModel> call, Response<CartListModel> response) {
                if (getActivity() != null) {
                    myDialog.hideDialog();
                }
                if (response.isSuccessful()) {

                    CartListModel data = response.body();

                    if (response.body().getStatus().equals("SUCCESS")) {
                        cartListModelList = data.getProductListRetrievedSuccessfully();
                        if (cartListModelList.size() == 0) {
                            binding.scrollView.setVisibility(View.GONE);
                            binding.llBagListEmpty.setVisibility(View.VISIBLE);
                            binding.tvPlaceOrder.setVisibility(View.GONE);
                            tvBadge.setVisibility(View.GONE);
                            SharedPreferenceWriter.getInstance(getActivity()).clearPreferenceValue(SharedPreferenceKey.gift_wrapup_status, "no");
                            SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(GlobalVariables.COUPON_APPLIED, false);

                        } else {
                            binding.tvPlaceOrder.setVisibility(View.VISIBLE);
                            binding.scrollView.setVisibility(View.VISIBLE);
                            tvBadge.setVisibility(View.VISIBLE);
                            tvBadge.setText(String.valueOf(cartListModelList.size()));
                        }

                        setOffersText(data.getMessage());


                        if (cartListModelList.size() == 0) {
                            if (getActivity() != null) {
                                tvBadge.setVisibility(View.GONE);
                            }
                        } else {
                            if (getActivity() != null) {
                                tvBadge.setVisibility(View.VISIBLE);
                                tvBadge.setText(String.valueOf(cartListModelList.size()));
                            }
                        }


                        if (userId.equalsIgnoreCase("1")) {

                            if (cartListModelList.size() == 0) {
                                if (getActivity() != null) {
                                    tvBadge.setVisibility(View.GONE);
                                }
                            } else {
                                if (getActivity() != null) {
                                    tvBadge.setVisibility(View.VISIBLE);
                                    tvBadge.setText(String.valueOf(cartListModelList.size()));
                                }
                            }
                        }

                        settingTotalPrice(cartListModelList);
                        setCartListAdapter(cartListModelList);//set adapter


                        setGiftWrapPrice(cartListModelList);
                        couponAppliedOrNot(cartListModelList);
                        setShippingCarges(cartListModelList);


                    } else {
                        // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    myDialog.hideDialog();
                    Toast.makeText(getActivity(), R.string.service_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartListModel> call, Throwable t) {
                myDialog.hideDialog();
                Log.e("msg", t.getMessage());
            }
        });
    }

    private void setShippingCarges(List<CartListModel.ProductListRetrievedSuccessfully> cartListModelList) {
        double shipingCharges = 0d;
        for (int i = 0; i < cartListModelList.size(); i++) {
            shipingCharges += Double.parseDouble(cartListModelList.get(i).getShipping_charges())*convertedPrice;
        }

        binding.tvShippingCharges.setText("+" + Math.round(shipingCharges));
//        Double aDouble = Double.parseDouble(binding.tvTotalPrice.getText().toString().split(symbol)[1]);
//        Double totalPrice = aDouble + shipingCharges;
//        binding.tvTotalPrice.setText(symbol + Math.round(totalPrice));
    }

    private void setGiftWrapPrice(List<CartListModel.ProductListRetrievedSuccessfully> cartListModelList) {
        binding.llGiftWrapPrice.setVisibility(View.GONE);
        String result = "";

        double price = 0d;
        if (cartListModelList.size() > 0 && cartListModelList != null) {
            for (int i = 0; i < cartListModelList.size(); i++) {

                if (!cartListModelList.get(i).getGiftwrap_price().equalsIgnoreCase("0")) {
                    binding.llGiftWrapPrice.setVisibility(View.VISIBLE);
                    price += Integer.parseInt(cartListModelList.get(i).getGiftwrap_price())*convertedPrice;
                }
            }
        }

        binding.tvGiftWrapPrice.setText("+" + Math.round(price));
        Double aDouble = Double.parseDouble(binding.tvTotalPrice.getText().toString().split(symbol)[1]);
        Double totalPrice = aDouble + price;
        binding.tvTotalPrice.setText(symbol + totalPrice);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void couponAppliedOrNot(List<CartListModel.ProductListRetrievedSuccessfully> list) {
        couponDiscount = 0.0;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getDiscount_coupon().equalsIgnoreCase("0")) {
                coponCode = list.get(i).getCoupon_code();
                couponDiscount = couponDiscount + (Double.parseDouble(list.get(i).getDiscount_coupon()) * convertedPrice);
            }
            if (couponDiscount > 0.0) {
                double d1 = Double.parseDouble(this.binding.tvTotalPrice.getText().toString().split(this.symbol)[1]);
                double d2 = this.couponDiscount.doubleValue();
                binding.tvTotalPrice.setText(symbol + " " + Math.round(d1 - d2));
                binding.llCouponApply.setVisibility(View.VISIBLE);
                binding.tvCouponDiscount.setText("-" + Math.round(couponDiscount));
                binding.couponImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.close));
                binding.couponImg.setBackground(ContextCompat.getDrawable((Context) getActivity(), R.drawable.drawable_circle));
                binding.couponImg.setBackgroundTintList(getActivity().getColorStateList(android.R.color.white));
                binding.llaa.setBackground(getActivity().getDrawable(R.drawable.drawable_gradient_line));
                binding.couponImg.setOnClickListener(this);
                binding.tvApply.setText("Applied code:" + coponCode);
                this.binding.tvApply.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
            } else {
                binding.llCouponApply.setVisibility(View.GONE);
                binding.tvApply.setTextColor(getActivity().getResources().getColor(R.color.colorBlack));
                binding.tvApply.setText("Apply Coupon");
                binding.llaa.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBagFragBackground));
                binding.couponImg.setOnClickListener(null);
                binding.couponImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.back_buttom));
            }
        }
    }
    

    private void setOffersText(List<String> message) {
        if (message.size() > 0) {
            if (message.size() == 1) {
                binding.offers.setText("Bank Offer");
            } else {
                binding.offers.setText("Bank Offers");
            }

            OfferAdapter adapter = new OfferAdapter(getActivity(), message);
            binding.OffersRV.setAdapter(adapter);

        }


    }

    private void settingTotalPrice(List<CartListModel.ProductListRetrievedSuccessfully> cartListModelList) {
        float total;
        float grandTotal = 0;
        for (int i = 0; i < cartListModelList.size(); i++) {
            total = Float.parseFloat(cartListModelList.get(i).getActualPrice()) * Integer.parseInt(cartListModelList.get(i).getQuantity());
            grandTotal = grandTotal + total;

        }
        binding.tvOrderTotal.setText("" + Math.round(grandTotal*convertedPrice));
        binding.tvTotalPrice.setText(symbol+" "+ Math.round(Double.parseDouble(binding.tvOrderTotal.getText().toString())));

    }

    private void setCartListAdapter(final List<CartListModel.ProductListRetrievedSuccessfully> cartListModelList) {

        if (cartListModelList != null && cartListModelList.size() > 0) {

            binding.scrollView.setVisibility(View.VISIBLE);
            binding.tvPlaceOrder.setVisibility(View.VISIBLE);
            binding.llBagListEmpty.setVisibility(View.GONE);

            cartListAdapter = new CartListAdapter(getActivity(), cartListModelList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

            binding.recyclerView.setLayoutManager(linearLayoutManager);
            binding.recyclerView.setAdapter(cartListAdapter);
            grandTotal = cartListAdapter.getPrice();

            cartListAdapter.setListener(new CartListAdapter.CartItemClickListener() {
                @Override
                public void onRemoveItemClick(View view, int pos) {
                    if (SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                        SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
                        productId = cartListModelList.get(pos).getProductId();
                        removeOfflineProducts(productId, pos, binding.mainRl);
                    } else {
                        productId = cartListModelList.get(pos).getProductId();
                        removeToCartApi(productId, pos, binding.mainRl);
                    }
                }

                @Override
                public void onMoveToWishListItemClick(View view, int pos) {
                    if (SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") || SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
                        openLoginSignUpBottomSheetWhenUserNotLogedIn();
                    } else {
                        productId = cartListModelList.get(pos).getProductId();
                        final MyDialog myDialog = new MyDialog(getActivity());
                        callAddToWishlistApi(productId);
                    }
                }

                @Override
                public void onImageItemClick(View view, int pos) {
                    productId = cartListModelList.get(pos).getProductId();
                    startActivity(new Intent(getActivity(), ProductDetailsActivityFinal.class).putExtra("product_id", productId));
                }

                @Override
                public void setTotalPrice(View view, Map<String, String> map) {
                    cartProduct_id = "";
                    cartProduct_quantity = "";
                    double total;
                    Double grandToal = 0.0;
                    for (int i = 0; i < map.size(); i++) {
                        String key = String.valueOf(map.keySet().toArray()[i]);
                        String value = String.valueOf(map.values().toArray()[i]);
                        cartProduct_id += key;
                        cartProduct_quantity += value;
                        if (map.size() - 1 > i) {
                            cartProduct_id += ",";
                            cartProduct_quantity += ",";
                        }
                        for (int y = 0; y < cartListModelList.size(); y++) {
                            if (cartListModelList.get(y).getProductId().equalsIgnoreCase(key)) {
                                total = Double.parseDouble(cartListModelList.get(y).getActualPrice()) * Double.parseDouble(map.get(key));
                                grandToal = grandToal + total;
                            }
                        }

                    }
                    callCartListApi(giftwrap2);
                }
            });


        } else {
            binding.llBagListEmpty.setVisibility(View.VISIBLE);
            binding.scrollView.setVisibility(View.GONE);
            binding.tvPlaceOrder.setVisibility(View.GONE);
        }

    }


    private void removeOfflineProducts(String productId, int pos,View view) {
        String product_id = "", color_name = "",sizes = "", quantity = "";
        int size= 0;
        ArrayList<ProductDetails> details = ((YODApplication) getActivity().getApplication()).getHugeData();

        if (details!=null && details.size()>0) {
            for (int i = 0; i < details.size(); i++) {

                if (details.get(i).getProduct_id().equalsIgnoreCase(productId)) {
                    details.remove(i);
                    details=YODApplication.getInstance().removeData(i);
                    cartListModelList.remove(pos);
                    cartListAdapter.notifyDataSetChanged();
                    CommonUtil.setUpSnackbarMessage(view,"Product Removed",getActivity());
                }
            }



            for(int i=0;i<details.size();i++)
            {
                product_id += details.get(i).getProduct_id();
                color_name += details.get(i).getColor_name();
                quantity += details.get(i).getQuantity();
                sizes+=details.get(i).getSize();
                if (details.size() - 1 > i) {
                    product_id += ",";
                    color_name += ",";
                    quantity += ",";
                    sizes+=",";
                }
            }

            if (cartListModelList.size() == 0) {
                binding.scrollView.setVisibility(View.GONE);
                binding.llBagListEmpty.setVisibility(View.VISIBLE);
                binding.tvPlaceOrder.setVisibility(View.GONE);
                tvBadge.setVisibility(View.GONE);

            }
            else
            {
                tvBadge.setText(""+details.size());
            }

            try{
            if(details.size()>0)
            {
                size= details.size();
            }else
            {
                size=0;
            }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            Log.e("produt",product_id);
            Log.e("color_name",color_name);
            Log.e("size",""+size);
            Log.e("quantity",""+quantity);


            SharedPreferenceWriter.getInstance(getActivity()).writeIntValue(GlobalVariables.count, size);
            SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.product_id, product_id);
            SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.color_name, color_name);
            SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.size, ""+sizes);
            SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(GlobalVariables.quantity, quantity);


        }
    }


            private void callAddToWishlistApi (String productId){
                final MyDialog myDialog = new MyDialog(getActivity());
                myDialog.showDialog();

                String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.TOKEN);
                String userId = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.USER_ID);


                Retrofit retrofit = ApiClient.getClient();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                Call<MyWishListResponse> call = apiInterface.getAddToWishListResult(token, userId, productId);
                call.enqueue(new Callback<MyWishListResponse>() {
                    @Override
                    public void onResponse(Call<MyWishListResponse> call, Response<MyWishListResponse> response) {

                        if (response.isSuccessful()) {
                            myDialog.hideDialog();

                            if (response.body().getStatus().equals("SUCCESS")) {
                                callCartListApi(giftwrap2);//hit api
                            }

                        } else {
                            myDialog.hideDialog();
                            Toast.makeText(getActivity(), R.string.service_error, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyWishListResponse> call, Throwable t) {
                        myDialog.hideDialog();
                    }
                });
            }



            private void removeToCartApi(final String productId,final int pos,final View view){
                final MyDialog myDialog = new MyDialog(getActivity());
                myDialog.showDialog();

                String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.TOKEN);
                String userId = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.USER_ID);

                Retrofit retrofit = ApiClient.getClient();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);

                Call<CartListModelResponse> call = apiInterface.removeToCart(userId, token, productId);
                call.enqueue(new Callback<CartListModelResponse>() {
                    @Override
                    public void onResponse(Call<CartListModelResponse> call, Response<CartListModelResponse> response) {

                        if (response.isSuccessful()) {
                            myDialog.hideDialog();

                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {
                                cartListModelList.clear();
                                callCartListApi(giftwrap2);

                            } else if (response.body().getStatus().equalsIgnoreCase("Failure")) {

                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            myDialog.hideDialog();
                            final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                            mSnackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                            mSnackbar.setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    removeToCartApi(productId, pos, view);
                                    Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                                    snackbar.getView().setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.drawable_gradient_line));
                                    snackbar.show();
                                }
                            });
                            mSnackbar.getView().setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.drawable_gradient_line));
                            mSnackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CartListModelResponse> call, Throwable t) {
                        myDialog.hideDialog();
                    }
                });
            }


    private void openLoginSignUpBottomSheetWhenUserNotLogedIn() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
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
                startActivity(new Intent(getActivity(), LoginActivity.class).putExtra("from","bagFragment"));
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
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList( "public_profile", "email"));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);//for facebook
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }


        if(resultCode==RESULT_OK)
        {
            if(requestCode==121 && HelperClass.showInternetAlert(getActivity())) {
                callCartListApi(giftwrap2);
            }
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

        } catch (ApiException e) {
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvPlaceOrder:
            if (SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false")|| SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
                 openLoginSignUpBottomSheetWhenUserNotLogedIn();
            } else {
                settingProductIdQuantity();
                callingIntent("PlaceOrderAddressActivity",binding.tvTotalPrice.getText().toString().split(symbol)[1],cartProduct_id,cartProduct_quantity);
            }
            break;

            case R.id.tvWishList:
            if (SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false")|| SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) openLoginSignUpBottomSheetWhenUserNotLogedIn();
            else startActivity(new Intent(getActivity(), MyWishListActivity.class));
            break;

            case R.id.llaa:
            settingProductIdQuantity();
            callingIntent("ApplyCouponActivity",binding.tvTotalPrice.getText().toString().split(symbol)[1],cartProduct_id,cartProduct_quantity);
            break;

            case R.id.ivBack:

            break;


            case R.id.llgiftwrap:
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                removeGiftWrap();
            }
            break;


            case R.id.couponImg:
            if(!coponCode.equalsIgnoreCase(""))
            {
                if(HelperClass.isInternetConnected(getActivity()))
                {
                    cancelCouponApi();
                }
            }
            break;


            case R.id.giftWrapIv:
            if(SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.gift_wrapup_status).equalsIgnoreCase("yes"))
            {
                giftwrap2="0";
                binding.llgiftwrap.setBackground(null);
                binding.llgiftwrap.setOnClickListener(this);
                binding.llgiftwrap.setBackgroundColor(getActivity().getResources().getColor(R.color.colorBagFragBackground));
                binding.tvApplyy.setTextColor(getActivity().getResources().getColor(R.color.colorBlack));
                binding.giftWrapIv.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.back_buttom));
                SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.gift_wrapup_status,"no");
                binding.giftWrapIv.setBackground(null);
                binding.giftWrapIv.setBackgroundTintList(null);
                binding.giftWrapIv.setOnClickListener(null);
                callCartListApi(giftwrap2);
            }
            break;
        }
    }



    private void cancelCouponApi() {
        final MyDialog myDialog = new MyDialog((Context)getActivity());
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<CancelCouponModel> call=apiInterface.cancelCoupon(userId);
        call.enqueue(new Callback<CancelCouponModel>() {


            public void onResponse(Call<CancelCouponModel> call, Response<CancelCouponModel> response) {
                if (getActivity() != null) myDialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        callCartListApi(giftwrap2);
                    } else {
                        Toast.makeText(getActivity(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                }
            }

            public void onFailure(Call<CancelCouponModel> param1Call, Throwable param1Throwable) {
                myDialog.hideDialog();
                Log.e("msg", param1Throwable.getMessage());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void removeGiftWrap() {
        binding.llgiftwrap.setOnClickListener(null);
        giftwrap2="1";
        binding.llgiftwrap.setBackground(getActivity().getDrawable(R.drawable.drawable_gradient_line));
        binding.tvApplyy.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.gift_wrapup_status,"yes");
        binding.giftWrapIv.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.close));
        binding.giftWrapIv.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.drawable_circle));
        binding.giftWrapIv.setBackgroundTintList(getActivity().getColorStateList(android.R.color.white));
        binding.giftWrapIv.setOnClickListener(this);
        callCartListApi(giftwrap2);
    }

    private void settingProductIdQuantity() {
        if(cartProduct_id.equalsIgnoreCase("") || cartProduct_quantity.equalsIgnoreCase(""))
        {

            for(int i=0;i<cartListModelList.size();i++)
            {
                cartProduct_id+=cartListModelList.get(i).getProductId();
                cartProduct_quantity+=cartListModelList.get(i).getQuantity();

                if(cartListModelList.size()-1>i)
                {
                    cartProduct_id+=",";
                    cartProduct_quantity+=",";

                }

            }

        }

    }

    private void callingIntent(String class_name, String total_price, String product_id, String product_quantity) {
        Intent intent = null;
        if (class_name.equalsIgnoreCase("PlaceOrderAddressActivity")) {
            intent = new Intent(getActivity(), PlaceOrderAddressActivity.class);
        } else if (class_name.equalsIgnoreCase("ApplyCouponActivity")) {
            intent = new Intent(getActivity(), ApplyCouponActivity.class);
        }
        intent.putExtra("shippingCharges",""+ binding.tvShippingCharges.getText().toString().split("\\+")[1]);
        intent.putExtra("grandTotal", total_price);
        intent.putExtra("total", binding.tvOrderTotal.getText().toString());
        intent.putExtra("product_id", product_id);
        intent.putExtra("product_quantity", product_quantity);
        intent.putExtra("discount", ""+couponDiscount);
        intent.putExtra("coupan_code", coponCode);
        gift_wrapup_status = SharedPreferenceWriter.getInstance((Context)getActivity()).getString("gift_wrapup_status");
        intent.putExtra("gift_wrap",""+ binding.tvGiftWrapPrice.getText().toString().split("\\+")[1]);
        if (class_name.equalsIgnoreCase("ApplyCouponActivity")) {
            startActivityForResult(intent, 121);
        }else {
            startActivity(intent);
        }
    }





}
