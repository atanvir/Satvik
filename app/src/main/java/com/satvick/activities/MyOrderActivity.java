package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.adapters.MyOrderAdapter;
import com.satvick.adapters.ProductListAdapter;
import com.satvick.contracts.MyOrderHelp.MyOrderHelpContract;
import com.satvick.contracts.MyOrderHelp.MyOrderHelpPresenter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityMyOrderBinding;

import com.satvick.fcm.MyFirebaseMessagingService;
import com.satvick.model.MyOrderDetailsModel;
import com.satvick.model.MyOrderHelp;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener, MyOrderHelpContract.View {
    ActivityMyOrderBinding binding;
    MyOrderAdapter myOrderAdapter;

    String comeFrom = "";
    List<MyOrderDetailsModel.Response> myOrderDetailsModelList = new Vector<>();
    private MyDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order);
        comeFrom = getIntent().getStringExtra("from");
        binding.ivBack.setOnClickListener(this);
        if (comeFrom != null) {
            if (comeFrom.equalsIgnoreCase("HelpCenterActivity")) {
                MyOrderHelpContract.Presenter presenter = new MyOrderHelpPresenter(this, this);
                presenter.requestMyOderHelpData();
            } else {
                myOrderApi();
            }
        } else {
            myOrderApi();//hit api
        }


    }

    private void myOrderApi() {
        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);

        final MyDialog myDialog = new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<MyOrderDetailsModel> call = apiInterface.getMyOrderResult(token, userId);
        call.enqueue(new Callback<MyOrderDetailsModel>() {
            @Override
            public void onResponse(Call<MyOrderDetailsModel> call, Response<MyOrderDetailsModel> response) {
                Log.d("OnResOrder:", response.body().toString());
                if (response.isSuccessful()) {
                    myDialog.hideDialog();

                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        MyOrderDetailsModel data = response.body();
                        if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                            myOrderDetailsModelList = data.getResponse();
                            List<MyOrderDetailsModel.Response> copiedData = new Vector<>();
                            setMyOrderDetailsAdapter(myOrderDetailsModelList);
                        }


                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(MyOrderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyOrderDetailsModel> call, Throwable t) {
                Log.e("message", t.getMessage());
                myDialog.hideDialog();
            }
        });
    }

    private void setMyOrderDetailsAdapter(List<MyOrderDetailsModel.Response> response) {

        myOrderAdapter = new MyOrderAdapter(this, response);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.myOrdersRecycler.setLayoutManager(linearLayoutManager);
        binding.myOrdersRecycler.setAdapter(myOrderAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (comeFrom != null) {
            if (comeFrom.equals("HelpCenterActivity")) {
                startActivity(new Intent(MyOrderActivity.this, HelpCenterActivity.class).putExtra("from", "MyOrderActivity"));
            } else if (comeFrom.equals("CancelOrderActivity")) {
                startActivity(new Intent(MyOrderActivity.this, MainActivity.class).putExtra("from", "ProductListFragment"));
            } else if (comeFrom.equalsIgnoreCase(MyFirebaseMessagingService.class.getSimpleName())) {
                startActivity(new Intent(MyOrderActivity.this, MainActivity.class).putExtra("from", ""));
            } else if (comeFrom.equalsIgnoreCase("")) {
                startActivity(new Intent(MyOrderActivity.this, HelpCenterActivity.class).putExtra("from", "MyOrderActivity"));
            } else {
                startActivity(new Intent(MyOrderActivity.this, MainActivity.class).putExtra("from", "ProductListFragment"));
            }
        } else {

            startActivity(new Intent(MyOrderActivity.this, MainActivity.class));
        }
    }

    @Override
    public void showProgress() {
        myDialog = new MyDialog(this);
        myDialog.showDialog();
    }

    @Override
    public void hideProgress() {
        myDialog.hideDialog();

    }

    @Override
    public void setDataToView(MyOrderHelp data) {
        if (data != null) {
            if (data.getResponse().size() > 0) {
                binding.tvTitle1.setText("Recent Purchased");
                binding.view.setVisibility(View.GONE);
                binding.myOrdersRecycler.setAdapter(new ProductListAdapter(this, data.getResponse(), MyOrderActivity.class.getSimpleName()));

            }
        } else {
            Snackbar.make(findViewById(R.id.rlMain), "No Data", Snackbar.LENGTH_LONG).show();

        }

    }

    @Override
    public void onResponseFailure(String message) {

    }
}
