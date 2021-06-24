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

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.satvick.adapters.AddressAdapter;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityAddressBinding;
import com.satvick.model.AddAddressModel;
import com.satvick.model.AddressValidModel;
import com.satvick.model.EditAddressModel;
import com.satvick.model.PinCodeModel;
import com.satvick.model.ViewAddressModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.satvick.retrofit.ApiClient.BASE_URL;
import static com.satvick.retrofit.ApiClient.PIN_CODE_URL;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, TextWatcher {
    private ActivityAddressBinding binding;
    private MyDialog dialog;
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    private String addressType="";
    private ViewAddressModel.Viewaddress address;
    private Boolean isValidPincode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
    }

    private void init() {
        dialog = new MyDialog(this);
        address=getIntent().getParcelableExtra("data");

        if(address!=null) {
            isValidPincode=true;
            binding.chbSelectPerson.setVisibility(View.GONE);
            binding.tvLabelAddressType.setVisibility(View.GONE);
            binding.radioGroup.setVisibility(View.GONE);
            binding.toolbar.tvTitle.setText("Update Address");
            binding.edtName.setText(address.getName());
            binding.edtAddress.setText(address.getAddress());
            binding.edtPinCode.setText(address.getPincode());
            binding.edtLocality.setText(address.getTown());
            binding.edtCity.setText(address.getCity());
            binding.edtState.setText(address.getState());
            binding.edtMobile.setText(address.getPhone());
            addressType=address.getType();
            if(address.getType().equalsIgnoreCase("Home")) binding.rbHome.setChecked(true);
            else if(address.getType().equalsIgnoreCase("Office")) binding.rbOffice.setChecked(true);
            if(address.getRemark().equalsIgnoreCase("0")) binding.chbSelectPerson.setChecked(true);
        }else{
            binding.toolbar.tvTitle.setText("Add Address");
        }
    }
    private void initCtrl() {
        binding.toolbar.ivBack.setOnClickListener(this);
        binding.radioGroup.setOnCheckedChangeListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.tvSave.setOnClickListener(this);
        binding.edtAddress.setOnClickListener(this);
        binding.edtPinCode.addTextChangedListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack: onBackPressed(); break;
            case R.id.tvCancel: finish(); break;
            case R.id.tvSave: if (checkValidation()) pinCodeApi(); break;
        }
        }

    private void pinCodeApi() {
        try {
            dialog = new MyDialog(this);
            dialog.showDialog();
        }catch (Exception e){
            e.printStackTrace();
        }
        Call<PinCodeModel> call = apiInterface.getPinCodeResult(binding.edtPinCode.getText().toString());
        call.enqueue(new Callback<PinCodeModel>() {
            @Override
            public void onResponse(Call<PinCodeModel> call, Response<PinCodeModel> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        if(response.body().getCheckpincodeseller().getStatus().equalsIgnoreCase("0")) {
                            dialog.hideDialog();
                            CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), AddressActivity.this);
                        }
                        else if(address==null) saveAddress(); else editAddress();
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), AddressActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!", AddressActivity.this);

            }

            @Override
            public void onFailure(Call<PinCodeModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(), AddressActivity.this);
            }
        });


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()){
            case R.id.rbHome : addressType=binding.rbHome.getText().toString();  break;
            case R.id.rbOffice : addressType=binding.rbOffice.getText().toString(); break;
        }
    }

    public boolean checkValidation() {
        boolean validate = true;

        if(TextUtils.isEmpty(binding.edtName.getText().toString())){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Add Name",AddressActivity.this);
        }

        else if(TextUtils.isEmpty(binding.edtAddress.getText().toString())){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Add Address",AddressActivity.this);
        }

        else if(TextUtils.isEmpty(binding.edtPinCode.getText().toString())){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Add Pin Code",AddressActivity.this);
        }

        else if(binding.edtPinCode.getText().toString().length()!=6){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Enter Valid Pin Code",AddressActivity.this);
        }else if(!isValidPincode){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Enter Valid Pin Code",AddressActivity.this);
        }


        else if(TextUtils.isEmpty(binding.edtLocality.getText().toString())){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Add Locality",AddressActivity.this);
        }

        else if(TextUtils.isEmpty(binding.edtCity.getText().toString())){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Add City",AddressActivity.this);
        }

        else if(TextUtils.isEmpty(binding.edtState.getText().toString())){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Add State",AddressActivity.this);
        }
        else if(TextUtils.isEmpty(binding.edtMobile.getText().toString())){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Add Mobile Number",AddressActivity.this);
        }
        else if(binding.edtMobile.getText().toString().length()!=10){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Enter Valid Mobile Number",AddressActivity.this);
        }
        else if(TextUtils.isEmpty(addressType)){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please Select Address Type",AddressActivity.this);
        }

        return validate;
    }



    private void saveAddress() {
        Call<AddAddressModel> call = apiInterface.getAddAddressResult(HelperClass.getCacheData(this).second,
                                                                      HelperClass.getCacheData(this).first,
                                                                      binding.edtName.getText().toString(),
                                                                      binding.edtMobile.getText().toString(),
                                                                      binding.edtPinCode.getText().toString(),
                                                                      binding.edtAddress.getText().toString(),
                                                                      binding.edtLocality.getText().toString(),
                                                                      binding.edtCity.getText().toString(),
                                                                      binding.edtState.getText().toString(),
                                                                      addressType,binding.chbSelectPerson.isChecked()==true?"0":"1",
                                                                      "0","India","0.0","0.0");
        call.enqueue(new Callback<AddAddressModel>() {
            @Override
            public void onResponse(Call<AddAddressModel> call, Response<AddAddressModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        setResult(RESULT_OK);
                        finish();
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), AddressActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!", AddressActivity.this);

            }

            @Override
            public void onFailure(Call<AddAddressModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(), AddressActivity.this);
            }
        });
    }

    private void editAddress() {
        dialog.showDialog();
        Call<EditAddressModel> call = apiInterface.getEditAddressResult(HelperClass.getCacheData(this).second,
                                                                        HelperClass.getCacheData(this).first,
                                                                        binding.edtName.getText().toString(),
                                                                        binding.edtMobile.getText().toString(),
                                                                        binding.edtPinCode.getText().toString(),
                                                                        binding.edtAddress.getText().toString(),
                                                                        binding.edtLocality.getText().toString(),
                                                                        binding.edtCity.getText().toString(),
                                                                        binding.edtState.getText().toString(),
                                                                        addressType,binding.chbSelectPerson.isChecked()==true?"0":"1",
                                                                        ""+address.getId(),address.getCountry(),address.getLatitude(),address.getLongitude());
        call.enqueue(new Callback<EditAddressModel>() {
            @Override
            public void onResponse(Call<EditAddressModel> call, Response<EditAddressModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        setResult(RESULT_OK);
                        finish();
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(), AddressActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!", AddressActivity.this);

            }

            @Override
            public void onFailure(Call<EditAddressModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(), AddressActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        isValidPincode=false;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.toString().length()==6){
            getCurrentStateApi();
        }
    }

    private void getCurrentStateApi() {
        dialog.showDialog();
        Call<AddressValidModel> call=ApiClient.getClient(PIN_CODE_URL).create(ApiInterface.class).getValidAddress(binding.edtPinCode.getText().toString());
        call.enqueue(new Callback<AddressValidModel>() {
            @Override
            public void onResponse(Call<AddressValidModel> call, Response<AddressValidModel> response) {
                dialog.hideDialog();
                if(response.isSuccessful()){
                    if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)){
                        isValidPincode=true;
                        binding.edtLocality.setText(response.body().getPostOffice().get(response.body().getPostOffice().size()-1).getName());
                        binding.edtCity.setText(response.body().getPostOffice().get(response.body().getPostOffice().size()-1).getRegion());
                        binding.edtState.setText(response.body().getPostOffice().get(response.body().getPostOffice().size()-1).getState());
                    }else{
                        isValidPincode=false;
                    }

                }else{
                 CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error!",AddressActivity.this);
                }
            }

            @Override
            public void onFailure(Call<AddressValidModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),AddressActivity.this);

            }
        });
    }
}
