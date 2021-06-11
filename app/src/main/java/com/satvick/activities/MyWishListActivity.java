package com.satvick.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.adapters.ColorProductDetailAdapter;
import com.satvick.adapters.MyWishListAdapter;
import com.satvick.adapters.SelectSizeProductDetailsAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityMyWishlistBinding;
import com.satvick.model.CartListModelResponse;
import com.satvick.model.MyWishListResponse;
import com.satvick.model.ProductDetailsResponse;
import com.satvick.model.Wishlistproduct;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyWishListActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMyWishlistBinding binding;
    MyWishListAdapter wishListAdapter;

    int[] image = {R.drawable.tshirt_yellow, R.drawable.red_tap_shoes, R.drawable.t_shirt_new_photo, R.drawable.t_shirt_new_photo};

    private Wishlistproduct wishlistproduct;
    private List<com.satvick.model.List> wishListArrayList = new ArrayList<>();

    String token = "";
    String userId = "";
    int noOfProduct;
    String mProductId = "";

    private ProductDetailsResponse data;
    List<String> getColor;
    List<String> getSize;

    ColorProductDetailAdapter colorProductDetailAdapter;
    SelectSizeProductDetailsAdapter selectSizeProductDetailsAdapter;

    String sizeText = null;
    String colorText = null;

    String sizeName = "";
    String colorName = "";

    Button btnSubmit;
    RecyclerView recyclerSize;
    RecyclerView recyclerColor;
    TextView tvSelectColor, tvSelectSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_wishlist);
        binding.ivBack.setOnClickListener(this);
        binding.ivBag.setOnClickListener(this);

        if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") ||
                SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {
            token = "1";
            userId = "1";
        } else {
            token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
            userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);
        }

        callWishListApi();

    }


    private boolean validateForm() {
        boolean isValidate = true;

        if (sizeText == null) {
            Toast.makeText(this, "Please select size", Toast.LENGTH_SHORT).show();
            return false;
        }

//        }else if(colorText==null) {
//            Toast.makeText(this, "Please select color", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return isValidate;
    }

    private void callProductDeatilsApiToSelectSizeAndColor(String mProductId) {
        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);

        final MyDialog myDialog = new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ProductDetailsResponse> call = apiInterface.getProductDetailsResult(token, userId, mProductId);
        call.enqueue(new Callback<ProductDetailsResponse>() {
            @Override
            public void onResponse(Call<ProductDetailsResponse> call, Response<ProductDetailsResponse> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    data = response.body();

                    if (response.body().getStatus().equals("SUCCESS")) {

                        // Toast.makeText(ProductDetailsActivityFinal.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        setData(data);

                    } else {
                        Toast.makeText(MyWishListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    Toast.makeText(MyWishListActivity.this, R.string.service_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductDetailsResponse> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void setData(ProductDetailsResponse data) {
        getColor = data.getProductdetails().getColor();
        getSize = data.getProductdetails().getSize();
        setSizeAdapter(getSize);
        setColorAdapter(getColor);
    }


    private void setSizeAdapter(final List<String> getSize) {
        if (getSize.size() > 0) {
            selectSizeProductDetailsAdapter = new SelectSizeProductDetailsAdapter(this, getSize);
            recyclerSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerSize.setAdapter(selectSizeProductDetailsAdapter);
            selectSizeProductDetailsAdapter.setListener(new SelectSizeProductDetailsAdapter.SelectSizeListener() {
                @Override
                public void itemClick(View view, int pos) {
                    sizeText = pos + "";
                    sizeName = getSize.get(pos);
                }
            });
        } else {
            tvSelectSize.setVisibility(View.GONE);
        }

    }

    private void setColorAdapter(final List<String> getColor) {
        if (getColor.size() > 0) {
            colorProductDetailAdapter = new ColorProductDetailAdapter(this, getColor);
            recyclerColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerColor.setAdapter(colorProductDetailAdapter);
            colorProductDetailAdapter.setListener(new ColorProductDetailAdapter.ColorsChangedListener() {
                @Override
                public void itemClick(View view, String colornm, int pos) {
                    colorText = pos + "";
                    colorName = getColor.get(pos);
                }
            });
        } else {
            tvSelectColor.setVisibility(View.GONE);
        }

    }

    private void callWishListApi() {

        final MyDialog myDialog = new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<MyWishListResponse> call = apiInterface.getWishListProductListResult(token, userId);
        call.enqueue(new Callback<MyWishListResponse>() {
            @Override
            public void onResponse(Call<MyWishListResponse> call, Response<MyWishListResponse> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    MyWishListResponse data = response.body();

                    if (response.body().getStatus().equals(GlobalVariables.SUCCESS)) {
                        wishListArrayList = data.getWishlistproduct().getList();
                        wishlistproduct = data.getWishlistproduct();
                        if (wishlistproduct.getNumOfAddtocart() == 0) {
                            binding.notificationBadge.setVisibility(View.GONE);
                        } else {
                            binding.notificationBadge.setVisibility(View.VISIBLE);
                            binding.notificationBadge.setText("" + wishlistproduct.getNumOfAddtocart());
                        }
                        if (wishListArrayList != null) {
                            wishListAdapter = new MyWishListAdapter(MyWishListActivity.this, wishListArrayList);

                            // set a GridLayoutManager with 2 number of columns , horizontal gravity and false value for reverseLayout to show the items from start to end
                            binding.recyclerView.setLayoutManager(new GridLayoutManager(MyWishListActivity.this, 2));
                            binding.recyclerView.setAdapter(wishListAdapter);

                            wishListAdapter.setListener(new MyWishListAdapter.MyWishListItemClickListener() {
                                @Override
                                public void onMoveToBagItemClick(View view, int pos) {
                                    sizeName=wishlistproduct.getList().get(pos).getSize()!=null?wishlistproduct.getList().get(pos).getSize():"";
                                    callAddToCartOrBaglistApi(wishlistproduct.getList().get(pos).getProductId(), pos, binding.mainRl);

                                   // openPopupForSelectSizeAndColor(String.valueOf(wishlistproduct.getList().get(pos).getProductId()), pos);

                                }

                                @Override
                                public void onIvCrossItemClick(View view, int pos) {
                                    sizeName=wishlistproduct.getList().get(pos).getSize();
                                    callAddToWishlistApi(pos, binding.mainRl);
                                }
                            });

                            //set product count
                            noOfProduct = wishListArrayList.size();


                            if (noOfProduct == 0) {
                                binding.tvProductCount.setText("No item");

                            } else if (noOfProduct == 1) {
                                binding.tvProductCount.setText(noOfProduct + " Item");
                            } else if (noOfProduct > 1) {
                                binding.tvProductCount.setText(noOfProduct + " Items");


                            }
                        } else {
                            binding.tvProductCount.setText("No item");
                            CommonUtil.CommonMessagePopUp(MyWishListActivity.this, "New Collection Awaited");
                        }


                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(MyWishListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyWishListResponse> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void openPopupForSelectSizeAndColor(final String mProductId, final int pos) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view1 = this.getLayoutInflater().inflate(R.layout.pop_up_select_size_and_color, null);
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

        btnSubmit = view1.findViewById(R.id.btnSubmit);
        recyclerSize = view1.findViewById(R.id.recyclerSize);
        recyclerColor = view1.findViewById(R.id.recyclerColor);
        tvSelectSize = view1.findViewById(R.id.tvSelectSize);
        tvSelectColor = view1.findViewById(R.id.tvSelectColor);

        tvSelectColor.setVisibility(View.GONE);
        recyclerColor.setVisibility(View.GONE);

//        callProductDeatilsApiToSelectSizeAndColor(mProductId);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateForm()) {
                    if (HelperClass.showInternetAlert(MyWishListActivity.this)) {

                        bottomSheetDialog.dismiss();
                    }
                }
            }
        });

        bottomSheetDialog.show();


    }

    private void callAddToCartOrBaglistApi(final String productId, final int pos, final View view) {
        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);

        final MyDialog myDialog = new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<CartListModelResponse> call = apiInterface.getAddToCartResult(token, userId, productId, colorName, sizeName, "1");
        call.enqueue(new Callback<CartListModelResponse>() {
            @Override
            public void onResponse(Call<CartListModelResponse> call, Response<CartListModelResponse> response) {
                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        wishListArrayList.remove(pos);
                        wishListAdapter.notifyDataSetChanged();

                        if (wishlistproduct.getList().size() == 0) {
                            binding.tvProductCount.setText("No Item");
                        } else if (wishlistproduct.getList().size() == 1) {
                            binding.tvProductCount.setText("1 Item");
                        } else if (wishlistproduct.getList().size() > 1) {
                            binding.tvProductCount.setText(wishlistproduct.getList().size() + " Items");
                        }

                        binding.notificationBadge.setVisibility(View.VISIBLE);
                        int count = 0;
                        if (binding.notificationBadge.getText().toString().equalsIgnoreCase("")) {

                            count = 0;
                            binding.notificationBadge.setText("" + (count + 1));
                        } else {
                            count = Integer.parseInt(binding.notificationBadge.getText().toString());
                            binding.notificationBadge.setText("" + (count + 1));

                        }


                        CommonMessagePopup(response.body().getMessage());


                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(MyWishListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(MyWishListActivity.this, R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callAddToCartOrBaglistApi(productId, pos, view);
                            Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(MyWishListActivity.this, R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(MyWishListActivity.this, R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<CartListModelResponse> call, Throwable t) {
                myDialog.hideDialog();
            }
        });

    }

    private void CommonMessagePopup(String message) {
        final Dialog dialog = new Dialog(MyWishListActivity.this, android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_mesage_popup);
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottieAnimationView);
        ImageView closeiv = dialog.findViewById(R.id.closeiv);
        TextView messagetxt = dialog.findViewById(R.id.messagetxt);
        messagetxt.setText(message);
        lottieAnimationView.setAnimation("done.json");

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


    private void callAddToWishlistApi(final int pos, final View view) {
        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);

        final MyDialog myDialog = new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<MyWishListResponse> call = apiInterface.getAddToWishListResult(token, userId, wishlistproduct.getList().get(pos).getProductId(),wishlistproduct.getList().get(pos).getSize());
        call.enqueue(new Callback<MyWishListResponse>() {
            @Override
            public void onResponse(Call<MyWishListResponse> call, Response<MyWishListResponse> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();

                    if (response.body().getStatus().equals(GlobalVariables.SUCCESS)) {
                        wishListArrayList.remove(pos);
                        wishListAdapter.notifyDataSetChanged();

                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(MyWishListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(MyWishListActivity.this, R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callAddToWishlistApi(pos, view);
                            Snackbar snackbar = Snackbar.make(view, "Please wait!", Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(MyWishListActivity.this, R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(MyWishListActivity.this, R.drawable.drawable_gradient_line));
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.ivBag:
                startActivity(new Intent(MyWishListActivity.this, MainActivity.class).putExtra("screen", "MyWishListActivity"));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
