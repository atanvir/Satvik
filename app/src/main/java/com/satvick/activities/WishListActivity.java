package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.satvick.R;
import com.satvick.adapters.MyWishListAdapter;
import com.satvick.databinding.ActivityWishlistBinding;
import com.satvick.model.CartListModelResponse;
import com.satvick.model.MyWishListResponse;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListActivity extends AppCompatActivity implements View.OnClickListener, MyWishListAdapter.MyWishListItemClickListener {

    private ActivityWishlistBinding binding;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    private int badgeCount=0;
    private List<com.satvick.model.List> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initCtrl();
        wishListApi();
    }

    private void initCtrl(){
        binding.ivBack.setOnClickListener(this);
        binding.ivBag.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: onBackPressed(); break;
            case R.id.ivBag: startActivity(new Intent(this, MainActivity.class).putExtra("screen", "MyWishListActivity")); break;
        }
    }


    private void wishListApi() {
        Call<MyWishListResponse> call = apiInterface.getWishListProductListResult(HelperClass.getCacheData(this).first,
                                                                                  HelperClass.getCacheData(this).second);
        call.enqueue(new Callback<MyWishListResponse>() {
            @Override
            public void onResponse(Call<MyWishListResponse> call, Response<MyWishListResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) setDataToUI(response.body());
                     else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        binding.tvProductCount.setText("No item");
                        CommonUtil.CommonMessagePopUp(WishListActivity.this, "New Collection Awaited");
                    }
                }else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",WishListActivity.this);
            }

            @Override
            public void onFailure(Call<MyWishListResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),WishListActivity.this);
            }
        });
    }

    private void setDataToUI(MyWishListResponse data) {
        list = data.getWishlistproduct().getList();
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(new MyWishListAdapter(this, list,this));

        setItemCount();
        setBadgeCount(badgeCount=data.getWishlistproduct().getNumOfAddtocart());
    }

    @Override
    public void onMoveToBagItemClick(View view, int pos) { addToCartApi(pos); }

    @Override
    public void onIvCrossItemClick(View view, int pos) { addToWishlistApi(pos); }

    private void addToCartApi(int pos) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<CartListModelResponse> call = apiInterface.getAddToCartResult(HelperClass.getCacheData(this).first,
                                                                            HelperClass.getCacheData(this).second,
                                                                            list.get(pos).getProductId(), "",
                                                                            list.get(pos).getSize()!=null?list.get(pos).getSize():"",
                                                                            "1");
        call.enqueue(new Callback<CartListModelResponse>() {
            @Override
            public void onResponse(Call<CartListModelResponse> call, Response<CartListModelResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) updateDataToUI(pos);
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),WishListActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",WishListActivity.this);

            }

            @Override
            public void onFailure(Call<CartListModelResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),WishListActivity.this);
            }
        });

    }
    private void addToWishlistApi(int pos){
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<MyWishListResponse> call = apiInterface.getAddToWishListResult(HelperClass.getCacheData(this).first,
                                                                            HelperClass.getCacheData(this).second,
                                                                            list.get(pos).getProductId(),
                                                                            list.get(pos).getSize() == null ? "" : list.get(pos).getSize());
        call.enqueue(new Callback<MyWishListResponse>() {
            @Override
            public void onResponse(Call<MyWishListResponse> call, Response<MyWishListResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(GlobalVariables.SUCCESS)) updateList(pos);
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),WishListActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),WishListActivity.this);
            }

            @Override
            public void onFailure(Call<MyWishListResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),WishListActivity.this);
            }
        });
    }

    private void updateDataToUI(int pos) {
        updateList(pos);
        setItemCount();
        setBadgeCount(badgeCount++);
    }
    private void setBadgeCount(int count) {
        binding.notificationBadge.setText("" + count);
        if (count>0) binding.notificationBadge.setVisibility(View.VISIBLE);
        else binding.notificationBadge.setVisibility(View.GONE);

    }
    private void setItemCount() {
        if (list.isEmpty()) binding.tvProductCount.setText("No item");
        else if (list.size()==1) binding.tvProductCount.setText("1 Item");
        else if (list.size()>1) binding.tvProductCount.setText(list.size() + " Items");
    }
    private void updateList(int pos) {
        list.remove(pos);
        binding.recyclerView.getAdapter().notifyItemRemoved(pos);
    }
}
