package com.satvick.activities;

import android.app.Dialog;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.satvick.databinding.ActivityAddNewAddressClickBinding;
import com.satvick.model.AddAddressModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddNewAddressClickActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAddNewAddressClickBinding binding;
    String comeFrom = "";

    String mName = "", mPhone = "", mEmail = "", mPassword = "", mConfirmPass = "", mCountry = "";
    String countryCodeAndroid = "";
    private String mAddress="";
    private String mPinCode="";
    private String mCity="";
    private String mLocality="";
    private String mState="";
    private String mAddressType="";
    private String mRemark="2";
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private LatLng latLng;
    private String country;

    String TAG=AddNewAddressClickActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_address_click);
        binding.ivBack.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvSave.setOnClickListener(this);
        binding.edtAddress.setOnClickListener(this);


        comeFrom = getIntent().getStringExtra("from");

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int c_id = radioGroup.getCheckedRadioButtonId();
                Log.v("radioGroup", "c_id: " + c_id);

                int home_id = binding.rdbHome.getId();
                int office_id = binding.rdbOffice.getId();

                if (c_id == home_id) {
                    Log.w("radioGroup", "home_id: " + home_id);
                    binding.rdbHome.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressClickActivity.this, R.drawable.radio_button_salected));
                    mAddressType="Home";

                } else {
                    binding.rdbHome.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressClickActivity.this, R.drawable.radio_button_un_salected_));
                }

                if (c_id == office_id) {
                    Log.w("radioGroup", "office_id: " + office_id);
                    binding.rdbOffice.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressClickActivity.this, R.drawable.radio_button_salected));
                    mAddressType="Office";

                } else {
                    binding.rdbOffice.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressClickActivity.this, R.drawable.radio_button_un_salected_));

                }

            }
        });


        binding.chbSelectPerson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.chbSelectPerson.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressClickActivity.this, R.drawable.sale_box));
                    mRemark="1";
                } else {
                    binding.chbSelectPerson.setButtonDrawable(ContextCompat.getDrawable(AddNewAddressClickActivity.this, R.drawable.new_rect));
                    mRemark="2";
                }
            }
        });
    }

    // validate address
    public static boolean validateAddress( String address )
    {
        return address.matches(
                "\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" );
    } // end method validateAddress


    // validate city
    public static boolean validateCity( String city )
    {
        return city.matches( "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" );
    } // end method validateCity

    // validate state
    public static boolean validateState( String state )
    {
        return state.matches( "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" ) ;
    } // end method validateState


    // validate zip
    public static boolean validateZip( String zip )
    {
        return zip.matches( "\\d{5}" );
    }

    //METHOD: check for the validation of the form fields and send OTP service call
    public boolean validateForm() {
        boolean validate = true;

        mName = binding.edtName.getText().toString().trim();
        mAddress=binding.edtAddress.getText().toString().trim();
        mPinCode=binding.edtPinCode.getText().toString().trim();
        mLocality=binding.edtLocality.getText().toString().trim();
        mCity=binding.edtCity.getText().toString().trim();
        mState=binding.edtState.getText().toString().trim();
        mPhone = binding.edtMobile.getText().toString().trim();


        if (mName.length()==0) {
            Toast.makeText(this, R.string.please_enter_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (mName.length() < 4 && mName.length() >= 40) {
            Toast.makeText(AddNewAddressClickActivity.this, R.string.name_should_not_more_than_fourty_character, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mAddress.length()==0) {
            Toast.makeText(this, R.string.please_enter_address, Toast.LENGTH_SHORT).show();
            return false;
        }

       /* else if (!validateAddress(mAddress)) {
            Toast.makeText(this, R.string.please_enter_valid_address, Toast.LENGTH_SHORT).show();
            return false;
        }*/

        if (mPinCode.length()==0) {
            Toast.makeText(this, R.string.please_enter_pincode, Toast.LENGTH_SHORT).show();
            return false;
        }

       /* else if (!validateZip(mPinCode)) {
            Toast.makeText(this, R.string.please_enter_valid_zip_code, Toast.LENGTH_SHORT).show();
            return false;
        }*/

        if (mLocality.length()==0) {
            Toast.makeText(this, R.string.please_enter_locality_or_town, Toast.LENGTH_SHORT).show();
            return false;
        }

      /*  else if (!validateAddress(mLocality)) {
            Toast.makeText(this, R.string.please_enter_valid_locality, Toast.LENGTH_SHORT).show();
            return false;
        }*/

        if (mCity.length()==0) {
            Toast.makeText(this, R.string.please_enter_city_or_district, Toast.LENGTH_SHORT).show();
            return false;
        }

       /* else if (!validateCity(mCity)) {
            Toast.makeText(this, R.string.please_enter_valid_city, Toast.LENGTH_SHORT).show();
            return false;
        }*/

        if (mState.length()==0) {
            Toast.makeText(this, R.string.please_enter_state, Toast.LENGTH_SHORT).show();
            return false;
        }

      /*   else if (!validateCity(mState)) {
            Toast.makeText(this, R.string.please_enter_valid_state, Toast.LENGTH_SHORT).show();
            return false;
        }*/
        else if (mPhone.length() == 0 && mPhone.length() >= 15) {
            Toast.makeText(AddNewAddressClickActivity.this, R.string.please_enter_phone_number, Toast.LENGTH_SHORT).show();
            return false;
        }


        return validate;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.tvCancel:
                if(comeFrom.equals("AddressActivity")){
                    startActivity(new Intent(this, AddressActivity.class).putExtra("from","AddNewAddressClickActivity"));

                }else {
                    Intent intent=new Intent(this,PlaceOrderAddressActivity.class);
                    intent.putExtra("total", getIntent().getStringExtra("total"));
                    intent.putExtra("grandTotal", getIntent().getStringExtra("grandTotal"));
                    intent.putExtra("shippingCharges", getIntent().getStringExtra("shippingCharges"));
                    intent.putExtra("product_id", getIntent().getStringExtra("product_id"));
                    intent.putExtra("product_quantity", getIntent().getStringExtra("product_quantity"));
                    intent.putExtra("gift_wrap", getIntent().getStringExtra("gift_wrap"));
                    intent.putExtra("discount", getIntent().getStringExtra("discount"));
                    intent.putExtra("coupan_code", getIntent().getStringExtra("coupan_code"));}

                break;

            case R.id.tvSave:
                if (validateForm()) {
                    if (HelperClass.showInternetAlert(AddNewAddressClickActivity.this)) {
                        callAddAddressApi("0",binding.mainRl);//hit api
                    }
                }

                break;

            case R.id.edtAddress:
//                Places.initialize(getApplicationContext(), getString(R.string.place_api_key));
//                List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG);
//                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).setTypeFilter(TypeFilter.ADDRESS).build(this);
//                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (comeFrom.equalsIgnoreCase("ReturnItemActivity")) {
            callingIntent(comeFrom);

        } else if (comeFrom.equalsIgnoreCase("RequestForExchangeProductActivity")) {
            callingIntent(comeFrom);

        }else if(comeFrom.equalsIgnoreCase("AddressActivity")){
            callingIntent(comeFrom);

        } else if(comeFrom.equals("PlaceOrderAddressActivity")){
            callingIntent(comeFrom);

        }

    }

    private void callingIntent(String comeFrom) {
        Intent intent=null;
        if(comeFrom.equalsIgnoreCase("ReturnItemActivity"))
        {
            intent=new Intent(this,ReturnItemActivity.class);
        }else if(comeFrom.equalsIgnoreCase("RequestForExchangeProductActivity"))
        {
            intent=new Intent(this,RequestForExchangeProductActivity.class);
        }
        else if(comeFrom.equalsIgnoreCase("AddressActivity"))
        {
            intent=new Intent(this,AddressActivity.class);
        }
        else if(comeFrom.equalsIgnoreCase("PlaceOrderAddressActivity"))
        {
            intent=new Intent(this,PlaceOrderAddressActivity.class);
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
        }

        startActivity(intent);
    }


    private void callAddAddressApi(final String popup, final View view) {

        String token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        String userId= SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);

        final MyDialog myDialog=new MyDialog(AddNewAddressClickActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<AddAddressModel> call = apiInterface.getAddAddressResult(userId,token,mName,mPhone,mPinCode,mAddress,mLocality,mCity,mState,mAddressType,mRemark,popup,"India",""+0.0,""+0.0);

        call.enqueue(new Callback<AddAddressModel>() {
            @Override
            public void onResponse(Call<AddAddressModel> call, Response<AddAddressModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();

                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {

                        String place_order=getIntent().getStringExtra("from");
                        if(place_order.equalsIgnoreCase(GlobalVariables.PlaceOrderAddressActivity))
                        {
                            Log.e("came","yes");
                            Intent intent=new Intent(AddNewAddressClickActivity.this,PlaceOrderAddressActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("total_price",getIntent().getStringExtra("total_price"));
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
                            finish();
                            startActivity(intent);

                        }
                        else
                        {
                            Intent intent = new Intent(AddNewAddressClickActivity.this, AddressActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)){
                        if(response.body().getMessage().equalsIgnoreCase("already saved"))
                        {
                            alreadyExistPopup();

                        }else
                        {
                            Toast.makeText(AddNewAddressClickActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(AddNewAddressClickActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callAddAddressApi(popup, view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(AddNewAddressClickActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(AddNewAddressClickActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<AddAddressModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void alreadyExistPopup() {
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.warning_pop_up);
        Button yesBtn=dialog.findViewById(R.id.yesbtn);
        Button noBtn=dialog.findViewById(R.id.nobtn);
        TextView messagetxt=dialog.findViewById(R.id.messagetxt);
        ImageView closeiv=dialog.findViewById(R.id.closeiv);

        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callAddAddressApi("1",v);
            }
        });





        dialog.show();

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
