package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import com.satvick.R;
import com.satvick.adapters.AddressAdapter;
import com.satvick.databinding.ActivitySavedAddressBinding;
import com.satvick.model.ViewAddressModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.HelperClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedAddressActivity extends AppCompatActivity implements View.OnClickListener, AddressAdapter.setOnAddressClick {

    private ActivitySavedAddressBinding binding;
    private List<ViewAddressModel.Viewaddress> viewAddressModelList = new ArrayList<>();
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    private final int NEW_ADDRESS_REQUEST=12,UPDATE_ADDRESS_REQUEST=21;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
        addressApi();
    }

    public void init() {
    }

    public void initCtrl(){
        binding.ivBack.setOnClickListener(this);
        binding.tvAddNewAddress.setOnClickListener(this);
        binding.tvEditDefault.setOnClickListener(this);
    }

    private void addressApi() {
       binding.progressBar.setVisibility(View.VISIBLE);
       Call<ViewAddressModel> call = apiInterface.getViewAddressResult(HelperClass.getCacheData(this).second);
       call.enqueue(new Callback<ViewAddressModel>() {
            @Override
            public void onResponse(Call<ViewAddressModel> call, Response<ViewAddressModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) loadAddressUI(response.body().getViewaddress());
                    else if(response.body().getStatus().equalsIgnoreCase("FAILURE")) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),SavedAddressActivity.this);
                }
                else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",SavedAddressActivity.this);
            }

            @Override
            public void onFailure(Call<ViewAddressModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),SavedAddressActivity.this);
            }
        });
    }

    private void loadAddressUI(List<ViewAddressModel.Viewaddress> viewaddress) {
        viewAddressModelList=viewaddress;
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new AddressAdapter(this,viewaddress,this,true));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: onBackPressed(); break;
            case R.id.tvAddNewAddress: startActivityForResult(new Intent(this, AddressActivity.class),NEW_ADDRESS_REQUEST); break;

        }
    }

    private void deleteAddressApi(String addressId,int pos) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ViewAddressModel> call = apiInterface.getRemoveAddressResult(HelperClass.getCacheData(this).second, HelperClass.getCacheData(this).first, addressId);
        call.enqueue(new Callback<ViewAddressModel>() {
            @Override
            public void onResponse(Call<ViewAddressModel> call, Response<ViewAddressModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        viewAddressModelList.remove(pos);
                        binding.recyclerView.getAdapter().notifyItemRemoved(pos);
                    } else if(response.body().getStatus().equals("FAILURE")) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),SavedAddressActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",SavedAddressActivity.this);
            }

            @Override
            public void onFailure(Call<ViewAddressModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),SavedAddressActivity.this);
            }
        });
    }

    private void setAsDefaultAddressApi(String addressId,int pos) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Call<ViewAddressModel> call = apiInterface.getSetAsDefaultAddressResult(HelperClass.getCacheData(this).second, HelperClass.getCacheData(this).first, addressId,"1");
        call.enqueue(new Callback<ViewAddressModel>() {
            @Override
            public void onResponse(Call<ViewAddressModel> call, Response<ViewAddressModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        for(int i=0;i<viewAddressModelList.size();i++){
                            viewAddressModelList.get(i).setRemark("0");
                        }
                        viewAddressModelList.get(pos).setRemark("1");
                        binding.recyclerView.getAdapter().notifyDataSetChanged();
                    }
                    else if(response.body().getStatus().equals("FAILURE")) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),SavedAddressActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",SavedAddressActivity.this);
            }

            @Override
            public void onFailure(Call<ViewAddressModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),SavedAddressActivity.this);
            }
        });
    }



    @Override
    public void onAddressClick(int pos) {
    }

    @Override
    public void onAddressRemove(int pos) {
        /*if(viewAddressModelList.get(pos).getRemark().equalsIgnoreCase("0")) setAsDefaultAddressApi(""+viewAddressModelList.get(pos).getId(),pos);
        else */ deleteAddressApi(""+viewAddressModelList.get(pos).getId(),pos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
        switch (requestCode){
            case NEW_ADDRESS_REQUEST: addressApi(); break;
            case UPDATE_ADDRESS_REQUEST: addressApi(); break;
        }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
