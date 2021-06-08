package com.satvick.activities;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityMainBinding;
import com.satvick.fragments.main.BagFragment;
import com.satvick.fragments.main.CategoriesFragment;
import com.satvick.fragments.main.HomeFragment;
import com.satvick.fragments.main.LoginFragment;
import com.satvick.fragments.main.MoreFragment;
import com.satvick.fragments.main.ProfileFragment;

import static com.satvick.utils.HelperClass.findNotificationBadge;

public class MainActivity extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener {

    //Binding
    private ActivityMainBinding binding;

    //Variables
    private TextView tvBadge;
    private boolean doubleBackToExitPressedOnce,isHomeSet = false;

    // Fragment's
    private HomeFragment homeFragment ;
    private BagFragment bagFragment ;
    private CategoriesFragment categoriesFragment = new CategoriesFragment();
    private MoreFragment moreFragment = new MoreFragment();
    private LoginFragment loginSingup = new LoginFragment();
    private ProfileFragment profileFragment = new ProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
        initCtrl();
  }


    private void init() {
        tvBadge=findNotificationBadge(MainActivity.this,binding.navigation);
        homeFragment= new HomeFragment(tvBadge);
        bagFragment= new BagFragment(tvBadge);

        // Deep Linked clicked
        if (getIntent().getBooleanExtra("isDeepLink", false)) {
            Intent intent = new Intent(this, ProductDetailsActivityFinal.class);
            intent.putExtra("product_id", getIntent().getStringExtra("product_id"));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(getIntent().getStringExtra("screen")!=null){
            binding.navigation.setSelectedItemId(R.id.bag);
            loadFragment(bagFragment);
        }else{
            loadFragment(homeFragment);
        }
    }

    private void initCtrl() {
        binding.navigation.setOnNavigationItemSelectedListener(this);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.home:
            loadFragment(homeFragment);
            return true;

            case R.id.categories:
            loadFragment(categoriesFragment);
            return true;


            case R.id.more:
            loadFragment(moreFragment);
            return true;

            case R.id.profile:
            if (SharedPreferenceWriter.getInstance(MainActivity.this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false")|| SharedPreferenceWriter.getInstance(MainActivity.this).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")) { loadFragment(loginSingup);return true; }
            else { loadFragment(profileFragment);return true; }

            case R.id.bag:
            loadFragment(bagFragment);
            return true;
        }
        return false;
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        else if(!isHomeSet) {
            loadFragment(homeFragment);
            binding.navigation.setSelectedItemId(R.id.home);
            isHomeSet = true;
            return;
        }else if(doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

}
