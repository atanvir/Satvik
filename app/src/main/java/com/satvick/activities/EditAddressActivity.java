package com.satvick.activities;

import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityEditAddressBinding;
import com.satvick.model.EditAddressModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEditAddressBinding binding;
    String comeFrom = "";
    String name = "";
    String mobile_number = "";
    String pin_code = "";
    String state = "";
    String address = "";
    String locality_or_town = "";
    String city_or_district = "";
    String address_id="";
    String remark="";
    String longitude,latitude;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
     LatLng latLng;
    String TAG=EditAddressActivity.class.getSimpleName();
    private String country;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_address);

        binding.ivBack.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvSave.setOnClickListener(this);
        binding.edtAddress.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        comeFrom = bundle.getString("from");
        name = bundle.getString("name");
        mobile_number = bundle.getString("mobile_number");
        pin_code = bundle.getString("pin_code");
        state = bundle.getString("state");
        address = bundle.getString("address");
        locality_or_town = bundle.getString("locality_or_town");
        city_or_district = bundle.getString("city_or_district");
        address_id = bundle.getString("address_id");
        remark=bundle.getString("remark");
        country=bundle.getString("country");
        latitude= bundle.getString("latitude");
        longitude= bundle.getString("longitude");

        if(bundle!=null) {
            if (comeFrom.equalsIgnoreCase("AddressActivityEditDefault")) {
                binding.edtFullName.setText(name);
                binding.edtMobile.setText(mobile_number);
                binding.edtPinCode.setText(pin_code);
                binding.edtState.setText(state);
                binding.edtAddress.setText(address);
                binding.edtLocality.setText(locality_or_town);
                binding.edtCity.setText(city_or_district);
            } else if (comeFrom.equalsIgnoreCase("AddressActivityEditOther")) {
                binding.edtFullName.setText(name);
                binding.edtMobile.setText(mobile_number);
                binding.edtPinCode.setText(pin_code);
                binding.edtState.setText(state);
                binding.edtAddress.setText(address);
                binding.edtLocality.setText(locality_or_town);
                binding.edtCity.setText(city_or_district);
            } else if(comeFrom.equalsIgnoreCase("PlaceOrderAddressActivity")){
                binding.edtFullName.setText(name);
                binding.edtMobile.setText(mobile_number);
                binding.edtPinCode.setText(pin_code);
                binding.edtState.setText(state);
                binding.edtAddress.setText(address);
                binding.edtLocality.setText(locality_or_town);
                binding.edtCity.setText(city_or_district);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                if (comeFrom.equalsIgnoreCase("PlaceOrderAddressActivity")) {
                    Intent intent=new Intent(EditAddressActivity.this, OrderConfirmationActivity.class);
                    intent.putExtra("product_id",getIntent().getStringExtra("product_id"));
                    intent.putExtra("product_quantity",getIntent().getStringExtra("product_quantity"));
                    intent.putExtra("total", getIntent().getStringExtra("total"));
                    intent.putExtra("grandTotal", getIntent().getStringExtra("grandTotal"));
                    intent.putExtra("shippingCharges", getIntent().getStringExtra("shippingCharges"));
                    intent.putExtra("product_id", getIntent().getStringExtra("product_id"));
                    intent.putExtra("product_quantity", getIntent().getStringExtra("product_quantity"));
                    intent.putExtra("gift_wrap", getIntent().getStringExtra("gift_wrap"));
                    intent.putExtra("discount", getIntent().getStringExtra("discount"));
                    intent.putExtra("coupan_code", getIntent().getStringExtra("coupan_code"));
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, SavedAddressActivity.class));
                }
                break;

            case R.id.tvCancel:

                if (comeFrom.equals("PlaceOrderAddressActivity")) {
                    startActivity(new Intent(this, OrderConfirmationActivity.class));
                } else {
                    startActivity(new Intent(this, SavedAddressActivity.class));
                }
                break;

            case R.id.tvSave:
            callEditApi(binding.mainRl);
            break;

            case R.id.edtAddress:
//             Places.initialize(getApplicationContext(), getString(R.string.place_api_key));
//             List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG);
//             Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).setTypeFilter(TypeFilter.ADDRESS).build(this);
//             startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
             break;
        }

    }

    @Override
    public void onBackPressed() {

        if (comeFrom.equalsIgnoreCase("PlaceOrderAddressActivity")) {
            Intent intent=new Intent(EditAddressActivity.this, OrderConfirmationActivity.class);
            intent.putExtra("product_id",getIntent().getStringExtra("product_id"));
            intent.putExtra("product_quantity",getIntent().getStringExtra("product_quantity"));
            intent.putExtra("total", getIntent().getStringExtra("total"));
            intent.putExtra("grandTotal", getIntent().getStringExtra("grandTotal"));
            intent.putExtra("shippingCharges", getIntent().getStringExtra("shippingCharges"));
            intent.putExtra("product_id", getIntent().getStringExtra("product_id"));
            intent.putExtra("product_quantity", getIntent().getStringExtra("product_quantity"));
            intent.putExtra("gift_wrap", getIntent().getStringExtra("gift_wrap"));
            intent.putExtra("discount", getIntent().getStringExtra("discount"));
            intent.putExtra("coupan_code", getIntent().getStringExtra("coupan_code"));
            startActivity(intent);
        } else { startActivity(new Intent(this, SavedAddressActivity.class)); }
    }

    private void callEditApi(final View view){
        String userId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);
        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);

        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<EditAddressModel> call = apiInterface.getEditAddressResult(userId, token,
                binding.edtFullName.getText().toString(),
                binding.edtMobile.getText().toString(),
                binding.edtPinCode.getText().toString(),
                binding.edtAddress.getText().toString(),
                binding.edtLocality.getText().toString(),
                binding.edtCity.getText().toString(),
                binding.edtAddress.getText().toString(),
                remark,"",
                address_id,"India",""+0.0,""+0.0);
        call.enqueue(new Callback<EditAddressModel>() {
            @Override
            public void onResponse(Call<EditAddressModel> call, Response<EditAddressModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {

                        Toast.makeText(EditAddressActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (comeFrom.equals("PlaceOrderAddressActivity")) {
                            Intent intent=new Intent(EditAddressActivity.this, OrderConfirmationActivity.class);
                            intent.putExtra("product_id",getIntent().getStringExtra("product_id"));
                            intent.putExtra("product_quantity",getIntent().getStringExtra("product_quantity"));
                            intent.putExtra("total", getIntent().getStringExtra("total"));
                            intent.putExtra("grandTotal", getIntent().getStringExtra("grandTotal"));
                            intent.putExtra("shippingCharges", getIntent().getStringExtra("shippingCharges"));
                            intent.putExtra("gift_wrap", getIntent().getStringExtra("gift_wrap"));
                            intent.putExtra("discount", getIntent().getStringExtra("discount"));
                            intent.putExtra("coupan_code", getIntent().getStringExtra("coupan_code"));
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(EditAddressActivity.this, SavedAddressActivity.class));
                        }

                    } else {
                        Toast.makeText(EditAddressActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(EditAddressActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callEditApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(EditAddressActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(EditAddressActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                }
            }

            @Override
            public void onFailure(Call<EditAddressModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                latLng = place.getLatLng();
                latitude= String.valueOf(latLng.latitude);
                longitude= String.valueOf(latLng.longitude);

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String countryName = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    country=countryName;
                    binding.edtAddress.setText(address+""+countryName);
                    binding.edtPinCode.setText(postalCode);
                    binding.edtLocality.setText(knownName);
                    binding.edtCity.setText(city);
                    binding.edtState.setText(state);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
