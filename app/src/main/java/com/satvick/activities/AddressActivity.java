package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.adapters.AddNewAddressAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityAddNewAddressBinding;
import com.satvick.model.ViewAddressModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAddNewAddressBinding binding;
    List<ViewAddressModel.Viewaddress> viewAddressModelList = new ArrayList<>();
    AddNewAddressAdapter addNewAddressAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_address);

        binding.ivBack.setOnClickListener(this);
        binding.tvAddNewAddress.setOnClickListener(this);
        binding.tvEditDefault.setOnClickListener(this);

        callViewAddressApi(binding.mainRl);
    }

    private void callViewAddressApi(final  View view) {

        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);

        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ViewAddressModel> call = apiInterface.getViewAddressResult(userId);
        call.enqueue(new Callback<ViewAddressModel>() {
            @Override
            public void onResponse(Call<ViewAddressModel> call, Response<ViewAddressModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    ViewAddressModel data = response.body();

                    if (response.body().getStatus().equals("SUCCESS")) {
                        viewAddressModelList = data.getViewaddress();
                        if(viewAddressModelList.size()==2)
                        {
                            binding.tvAddNewAddress.setEnabled(false);
                        }
                        else
                        {
                            binding.tvAddNewAddress.setEnabled(true);
                        }
                        setAdapter(viewAddressModelList);

                    } else {
                        Toast.makeText(AddressActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(AddressActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callViewAddressApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(AddressActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(AddressActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                    Toast.makeText(AddressActivity.this, R.string.service_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViewAddressModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void setAdapter(final List<ViewAddressModel.Viewaddress> viewAddressModelList) {
        if (viewAddressModelList != null && viewAddressModelList.size() > 0) {
            addNewAddressAdapter = new AddNewAddressAdapter(this, viewAddressModelList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            binding.recyclerView.setLayoutManager(linearLayoutManager);
            binding.recyclerView.setAdapter(addNewAddressAdapter);


            addNewAddressAdapter.setListener(new AddNewAddressAdapter.AddressItemClickListener() {
                @Override
                public void onRemoveItemClick(View view, int pos) {
                    if(viewAddressModelList.get(pos).getRemark().equalsIgnoreCase("2")){
                         callSetAsDefaultAddressApi(String.valueOf(viewAddressModelList.get(pos).getId()),binding.mainRl);
                    }else {
                        callRemoveAddressApi(String.valueOf(viewAddressModelList.get(pos).getId()),binding.mainRl);
                        addNewAddressAdapter.deleteItem(pos);
                    }
                }

                @Override
                public void onEditItemClick(View view, int pos) {

                    Intent intent2=new Intent(AddressActivity.this, EditAddressActivity.class);

                    Bundle bundle2 = new Bundle();
                    bundle2.putString("from", "AddressActivityEditOther");
                    bundle2.putString("name", viewAddressModelList.get(pos).getName());
                    bundle2.putString("mobile_number", viewAddressModelList.get(pos).getPhone());
                    bundle2.putString("pin_code", viewAddressModelList.get(pos).getPincode());
                    bundle2.putString("state", viewAddressModelList.get(pos).getState());
                    bundle2.putString("address", viewAddressModelList.get(pos).getAddress());
                    bundle2.putString("locality_or_town", viewAddressModelList.get(pos).getTown());
                    bundle2.putString("city_or_district", viewAddressModelList.get(pos).getCity());
                    bundle2.putString("address_id", String.valueOf(viewAddressModelList.get(pos).getId()));
                    bundle2.putString("remark", String.valueOf(viewAddressModelList.get(pos).getRemark()));
                    bundle2.putString("country", String.valueOf(viewAddressModelList.get(pos).getCountry()));
                    bundle2.putString("latitude", String.valueOf(viewAddressModelList.get(pos).getLatitude()));
                    bundle2.putString("longitude", String.valueOf(viewAddressModelList.get(pos).getLongitude()));


                    intent2.putExtras(bundle2);
                    startActivity(intent2);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.tvAddNewAddress:
                if(viewAddressModelList.size()>=2)
                    return;
                startActivity(new Intent(this, AddNewAddressClickActivity.class).putExtra("from", "AddressActivity"));
                break;

        }
    }

    private void callRemoveAddressApi(final String addressId,final View view) {
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);
        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);

        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ViewAddressModel> call = apiInterface.getRemoveAddressResult(userId, token, addressId);
        call.enqueue(new Callback<ViewAddressModel>() {
            @Override
            public void onResponse(Call<ViewAddressModel> call, Response<ViewAddressModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        callViewAddressApi(view);

                    } else {
                        Toast.makeText(AddressActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(AddressActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callRemoveAddressApi(addressId,view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(AddressActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(AddressActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ViewAddressModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void callSetAsDefaultAddressApi(final String addressId, final View view) {
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);
        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);

        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ViewAddressModel> call = apiInterface.getSetAsDefaultAddressResult(userId, token, addressId,"1");
        call.enqueue(new Callback<ViewAddressModel>() {
            @Override
            public void onResponse(Call<ViewAddressModel> call, Response<ViewAddressModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();

                    if (response.body().getStatus().equals("SUCCESS")) {
                        callViewAddressApi(view);

                    } else {
                        Toast.makeText(AddressActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(AddressActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callRemoveAddressApi(addressId,view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(AddressActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(AddressActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ViewAddressModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
    }
}
