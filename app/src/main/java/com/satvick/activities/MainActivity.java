package com.satvick.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityMainBinding;
import com.satvick.fragments.BagFragment;
import com.satvick.fragments.CategoriesFragment;
import com.satvick.fragments.HomeFragment;
import com.satvick.fragments.LoginSignupForProfileFragment;
import com.satvick.fragments.MoreFragment;
import com.satvick.fragments.ProfileFragment;
import com.satvick.model.MyCurrentDetailsModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.utils.GlobalVariables;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity implements BagFragment.OnDataPass,ProductDetailsActivityFinal.OnDataPass{
    ActivityMainBinding binding;
    private String mFrom = "";
    public static MainActivity instance;
    private String badgeCount="";
    public TextView tv;
    Fragment navFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        loadHomeData();
  }

    private void myCurrentDetailsApi() {
        Retrofit retrofit = ApiClient.getClient("http://ip-api.com/");
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<MyCurrentDetailsModel> call=apiInterface.getMyDeatails();
        call.enqueue(new Callback<MyCurrentDetailsModel>() {

            public void onFailure(Call<MyCurrentDetailsModel> param1Call, Throwable param1Throwable) {}

            public void onResponse(Call<MyCurrentDetailsModel> param1Call, Response<MyCurrentDetailsModel> param1Response) {
               getCurrencyByCountry(((MyCurrentDetailsModel)param1Response.body()).getCountryCode());
            }
        });
    }

    private void getCurrencyByCountry(final String countryCode) {
        Retrofit retrofit = ApiClient.getClient("https://restcountries.eu/rest/v1/");
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<MyCurrentDetailsModel> call=apiInterface.getCurrency(countryCode);
        call.enqueue(new Callback<MyCurrentDetailsModel>() {
            public void onFailure(Call<MyCurrentDetailsModel> param1Call, Throwable param1Throwable) {}

            public void onResponse(Call<MyCurrentDetailsModel> param1Call, Response<MyCurrentDetailsModel> param1Response) {
                String str = ((MyCurrentDetailsModel)param1Response.body()).getCurrencies().get(0);
                SharedPreferenceWriter.getInstance((Context)MainActivity.this).writeStringValue("country_code", countryCode);
                SharedPreferenceWriter.getInstance((Context)MainActivity.this).writeStringValue("currency", str);
                getCurrencyAmount(str);
            }
        });
    }


    private void getCurrencyAmount(final String currency) {
        Retrofit retrofit = ApiClient.getClient("https://api.exchangeratesapi.io/");
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<JsonObject> call=apiInterface.getCurrencyAmount("INR", currency);
        call.enqueue(new Callback<JsonObject>() {
            public void onFailure(Call<JsonObject> param1Call, Throwable param1Throwable) {}

            public void onResponse(Call<JsonObject> param1Call, Response<JsonObject> param1Response) {
                try {
                    String str = String.valueOf(((JsonObject)param1Response.body()).getAsJsonObject("rates").getAsJsonPrimitive(currency));
                    SharedPreferenceWriter sharedPreferenceWriter = SharedPreferenceWriter.getInstance((Context)MainActivity.this);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    stringBuilder.append(str);
                    sharedPreferenceWriter.writeStringValue("converted_amount", stringBuilder.toString());
                    getCurrencySymbol(currency);

                } catch (Exception exception) {
                    exception.printStackTrace();
                  }
            }
        });
    }


    private void getCurrencySymbol(final String currency) {
        Retrofit retrofit = ApiClient.getClient("https://www.localeplanet.com/api/auto/");
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<JsonObject> call=apiInterface.getAllSymbols();
        call.enqueue(new Callback<JsonObject>() {
            public void onFailure(Call<JsonObject> param1Call, Throwable param1Throwable) {}

            public void onResponse(Call<JsonObject> param1Call, Response<JsonObject> param1Response) {
                try {
                    JsonObject jsonObject = ((JsonObject)param1Response.body()).getAsJsonObject(currency);
                    SharedPreferenceWriter sharedPreferenceWriter = SharedPreferenceWriter.getInstance((Context)MainActivity.this);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    stringBuilder.append(String.valueOf(jsonObject.getAsJsonPrimitive("symbol")).replace("\"", ""));
                    sharedPreferenceWriter.writeStringValue("symbol", stringBuilder.toString());
                    if (SharedPreferenceWriter.getInstance(MainActivity.this).getString("symbol").equalsIgnoreCase("\u20B9") || SharedPreferenceWriter.getInstance(MainActivity.this).getString("symbol").equalsIgnoreCase("₹")) {
                        SharedPreferenceWriter.getInstance((Context) MainActivity.this).writeStringValue("symbol", "Rs.");
                    }
                    loadHomeData();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    private void callProfileFrag() {
        navFragment = new LoginSignupForProfileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();
    }
    
    private void loadHomeData()
    {
        if (getIntent().getBooleanExtra("isDeepLink", false)) {
            Intent intent = new Intent(this, ProductDetailsActivityFinal.class);
            intent.putExtra("product_id", getIntent().getStringExtra("product_id"));
            startActivity(intent);
        }
        instance = this;
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) binding.navigation.getChildAt(0);

        View v = bottomNavigationMenuView.getChildAt(4);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this).inflate(R.layout.unread_message_layout, bottomNavigationMenuView, false);
        tv = badge.findViewById(R.id.notification_badge);


        itemView.addView(badge);

        for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
            final View iconView = bottomNavigationMenuView.getChildAt(i).findViewById(R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            if (i == 1) {
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
                // set your width here
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, displayMetrics);
            } else {
                // set your height here
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
                // set your width here
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            }
            iconView.setLayoutParams(layoutParams);
        }

        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String intent = getIntent().getStringExtra("from");
        if (intent != null) {
            mFrom = getIntent().getStringExtra("from");
            Log.e("cFrom",mFrom);


            if (mFrom.equalsIgnoreCase("moreFragment")) {
                //to visible Bag Fragment tab
                binding.navigation.setSelectedItemId(R.id.more);
            }


            else if (mFrom.equalsIgnoreCase("ReferAndEarnFragment")) {
                binding.navigation.setSelectedItemId(R.id.profile);
            }

            else if (mFrom.equalsIgnoreCase("ProductDetailsActivityFinal")) {
                binding.navigation.setSelectedItemId(R.id.bag);
            }

            else if (mFrom.equalsIgnoreCase("ProductListFragment")) {
                //to visible Profile Fragment tab
                binding.navigation.setSelectedItemId(R.id.profile);
            }

            else if (mFrom.equalsIgnoreCase("HelpCenter")) {
                //to visible More Fragment tab
                binding.navigation.setSelectedItemId(R.id.more);
            }

            else if (mFrom.equalsIgnoreCase("addressMain")) {
                //to visible Bag Fragment tab
                binding.navigation.setSelectedItemId(R.id.bag);
            }

            else if (mFrom.equalsIgnoreCase("addAddress")) {
                //to visible Bag Fragment tab
                binding.navigation.setSelectedItemId(R.id.profile);
            }

            else if (mFrom.equalsIgnoreCase("EditProfileFragment")) {
                binding.navigation.setSelectedItemId(R.id.profile);
            }

            else if (mFrom.equalsIgnoreCase("MyWishListActivity")) {
                binding.navigation.setSelectedItemId(R.id.bag);
            }

            else if (mFrom.equalsIgnoreCase("RequestForExchangeProductActivity")) {
                binding.navigation.setSelectedItemId(R.id.bag);
            }

            else if (mFrom.equalsIgnoreCase("SliderAdapterHome")) {
                binding.navigation.setSelectedItemId(R.id.categories);
            }

            else if (mFrom.equalsIgnoreCase("HomeFragmentAfterLogin")) {
                binding.navigation.setSelectedItemId(R.id.categories);
            }

            else if (mFrom.equalsIgnoreCase("InternalScreens")) {
                binding.navigation.setSelectedItemId(R.id.categories);
            }

            else if (mFrom.equalsIgnoreCase("InternalScreensComeFromHomeAfterLoginSlider")) {
                binding.navigation.setSelectedItemId(R.id.home);
            }


            else if (mFrom.equalsIgnoreCase("SettingsActivity")) {
                binding.navigation.setSelectedItemId(R.id.profile);
            }

            else if (mFrom.equalsIgnoreCase("CouponsActivity")) {
                binding.navigation.setSelectedItemId(R.id.profile);
            }

            else if (mFrom.equalsIgnoreCase("SavedCardActivity")) {
                binding.navigation.setSelectedItemId(R.id.profile);
            }

            else if (mFrom.equalsIgnoreCase("onBackProfileFrag")) {
                binding.navigation.setSelectedItemId(R.id.profile);
            }

            else if (mFrom.equalsIgnoreCase("onBackMoreFrag")) {
                binding.navigation.setSelectedItemId(R.id.more);
            }

            else if (mFrom.equalsIgnoreCase("ReferAndEarnActivity")) {
                binding.navigation.setSelectedItemId(R.id.profile);
            }

            else if (mFrom.equalsIgnoreCase("HomeFragment")) {
                binding.navigation.setSelectedItemId(R.id.home);
            }


            else if (mFrom.equalsIgnoreCase("HomeFb")) {
                navFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();
            }

            else if (mFrom.equalsIgnoreCase("HomeGoogle")) {
                navFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();
            }

            else if (mFrom.equalsIgnoreCase("HomeIvFbGoogle")) {
                navFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();
            }


            else if (mFrom.equalsIgnoreCase("LoginButton")) {

                navFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();

            }

            else if (mFrom.equalsIgnoreCase("ProductListFragmentToCameAtHomeAfterLogin")) {
                navFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();
            }
            else if(mFrom.equalsIgnoreCase(GlobalVariables.flashSale))
            {
                binding.navigation.setSelectedItemId(R.id.home);
                navFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();
            }
            else
            {
                navFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();
            }
        }
        else
        {

            navFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();

        }
    }




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                //Home Fragment
                case R.id.home:

                    navFragment = new HomeFragment();

                    getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();


                    return true;


                //Categories Fragment
                case R.id.categories:

                    navFragment = new CategoriesFragment();

                    getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();

                    return true;


                //More Fragment
                case R.id.more:

                    navFragment = new MoreFragment();

                    getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();

                    return true;


                //Profile Fragment
                case R.id.profile:

                    if (SharedPreferenceWriter.getInstance(MainActivity.this).
                            getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false")||
                            SharedPreferenceWriter.getInstance(MainActivity.this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) {

                        callProfileFrag();

                        return true;
                    } else {
                        navFragment = new ProfileFragment();

                        getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();

                        return true;
                    }


                    //Bag Fragment
                case R.id.bag:

                    navFragment = new BagFragment();

                    getSupportFragmentManager().beginTransaction().replace(R.id.content, navFragment).commit();

                    return true;
            }

            return false;
        }
    };





    @Override
    public void onBackPressed() {

       if (navFragment instanceof HomeFragment) {
           final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
           dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
           dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
           dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           dialog.setContentView(R.layout.exit_popup);
           Button yesBtn=dialog.findViewById(R.id.yesbtn);
           Button nobtn=dialog.findViewById(R.id.nobtn);
           ImageView closeiv=dialog.findViewById(R.id.closeiv);
           closeiv.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.dismiss();
               }
           });

           yesBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.dismiss();
                   finishAffinity();
               }
           });

           nobtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.dismiss();

               }
           });

           dialog.setCancelable(false);
           dialog.show();

        } else {
            binding.navigation.setSelectedItemId(R.id.home);
        }

    }


    @Override
    public void onSuccess(String str) {

    }
}
