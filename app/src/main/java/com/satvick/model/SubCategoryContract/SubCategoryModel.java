package com.satvick.model.SubCategoryContract;

import android.content.Context;
import android.widget.Toast;

import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.model.HomeModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryModel implements SubCategoryContract.Model {
    private Context context;

    public SubCategoryModel(Context context)
    {
        this.context=context;
    }

    @Override
    public void getSubCategoryList(final OnFinishedListener listener) {


       ApiInterface api_service= ApiClient.getClient().create(ApiInterface.class);
       Call<HomeModel> call=api_service.getHomeResult(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.USER_ID), SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.TOKEN));
       call.enqueue(new Callback<HomeModel>() {
           @Override
           public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
               if(response.isSuccessful())
               {
                   Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                   if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {


                      listener.onSucess(response.body().getHomeapi());

                   }
                   else if(response.body().getStatus().equalsIgnoreCase("Failure"))
                   {


                   }
               }
           }

           @Override
           public void onFailure(Call<HomeModel> call, Throwable t) {

               listener.onFailure(t.getMessage());

           }
       });

    }
}
