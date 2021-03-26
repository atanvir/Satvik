package com.satvick.activities;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.satvick.R;
import com.satvick.databinding.ActivityAddNewAddressPlaceOrderBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddNewAddressActivityPlaceOrderActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAddNewAddressPlaceOrderBinding binding;
    String comeFrom="";
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private LatLng latLng;
    String TAG=AddNewAddressActivityPlaceOrderActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_address_place_order);
        init();
        comeFrom=getIntent().getStringExtra("from");
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int c_id = radioGroup.getCheckedRadioButtonId();
                Log.v("radioGroup", "c_id: " + c_id);
                int game_id = binding.rdbGame.getId();
                int practice_id = binding.rdbPractice.getId();
                if (c_id == game_id) {
                    Log.w("radioGroup", "game_id: " + game_id);
                    binding.rdbGame.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressActivityPlaceOrderActivity.this, R.drawable.radio_button_salected));
                } else {
                    binding.rdbGame.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressActivityPlaceOrderActivity.this, R.drawable.radio_button_un_salected_));
                }
                if (c_id == practice_id) {
                    Log.w("radioGroup", "practice_id: " + practice_id);
                    binding.rdbPractice.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressActivityPlaceOrderActivity.this, R.drawable.radio_button_salected));

                } else {
                    binding.rdbPractice.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressActivityPlaceOrderActivity.this, R.drawable.radio_button_un_salected_));
                }

            }
        });


        binding.chbSelectPerson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chbSelectPerson.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressActivityPlaceOrderActivity.this, R.drawable.sale_box));
                } else {
                    binding.chbSelectPerson.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressActivityPlaceOrderActivity.this, R.drawable.new_rect));
                }
            }
        });
    }

    private void init() {
        binding.ivBack.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvSave.setOnClickListener(this);
        binding.edtGender.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                //startActivity(new Intent(this, MainActivity.class).putExtra("from","AddNewAddressActivityPlaceOrderActivity"));
                break;

            case R.id.tvCancel:
                startActivity(new Intent(this, PlaceOrderAddressActivity.class));
                break;

            case R.id.tvSave:
                startActivity(new Intent(this, PlaceOrderAddressActivity.class));
                break;

            case R.id.edtGender:
//                Places.initialize(getApplicationContext(), getString(R.string.place_api_key));
//                List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG);
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).setTypeFilter(TypeFilter.ADDRESS).build(this);
//                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if(comeFrom.equals("PlaceOrderAddressActivity")){
            startActivity(new Intent(AddNewAddressActivityPlaceOrderActivity.this,PlaceOrderAddressActivity.class));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                latLng = place.getLatLng();

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();

                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    binding.edtGender.setText(address+""+country);
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
