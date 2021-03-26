package com.satvick.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.adapters.NotificationAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityNotificationBinding;
import com.satvick.model.NotificationListResponse;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityNotificationBinding binding;
    private String userId="",token="";

    List<NotificationListResponse> notificationListResponseList = new ArrayList<>();
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        binding.ivBack.setOnClickListener(this);
        callNotificationListApi(binding.mainRl);
    }

    private void callNotificationListApi(final View view) {
        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false")||
           SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {

            userId="1";
            token="1";

        }
        else
        {
            userId=SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);
            token=SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        }


        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<NotificationListResponse> call = apiInterface.getNotificationListResult(userId,token);
        call.enqueue(new Callback<NotificationListResponse>() {
            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {
                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                    {
                        NotificationAdapter adapter=new NotificationAdapter(NotificationActivity.this,response.body().getNotficationlist());
                        binding.recyclerView.setAdapter(adapter);


                    }
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                    {
                        Toast.makeText(NotificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    }
                else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(NotificationActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callNotificationListApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(NotificationActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(NotificationActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                    }

            }
            @Override
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {
                myDialog.hideDialog();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
