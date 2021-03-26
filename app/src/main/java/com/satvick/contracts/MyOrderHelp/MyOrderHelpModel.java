package com.satvick.contracts.MyOrderHelp;

import android.content.Context;

import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.model.MyOrderHelp;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.utils.GlobalVariables;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderHelpModel implements MyOrderHelpContract.Model {


    @Override
    public void getMyOrderHelp(Context context, final OnFinishedListener listner) {

        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<MyOrderHelp> call= apiInterface.getMyOrderHelp(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.TOKEN),
                                                            SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.USER_ID));

        call.enqueue(new Callback<MyOrderHelp>() {
            @Override
            public void onResponse(Call<MyOrderHelp> call, Response<MyOrderHelp> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                    {
                        listner.onSucess(response.body());
                    }
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                    {
                        listner.onFailure(response.body().getStatus());
                    }

                }
            }

            @Override
            public void onFailure(Call<MyOrderHelp> call, Throwable t) {
                listner.onFailure(t.getMessage());
            }
        });

    }
}
