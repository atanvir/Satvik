package com.satvick.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivitySignupCustomBinding;
import com.satvick.databinding.PopUpResendOtpBinding;
import com.satvick.model.CheckPhoneModel;
import com.satvick.model.SignUpModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.satvick.utils.Utility.isValidEmail;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<AuthResult> {
    private ActivitySignupCustomBinding binding;
    private PopUpResendOtpBinding otpBinding;
    private FirebaseAuth auth;
    private String verificationCode = null;
    private PhoneAuthProvider.ForceResendingToken refreshToken;
    private BottomSheetDialog otpDailog;
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    private MyDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupCustomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
    }

    private void init() {
        dialog = new MyDialog(this);
        auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("en");
    }

    private void initCtrl() {
        binding.rlLoginSignUp.setOnClickListener(this);
        binding.buttonSignUp.setOnClickListener(this);
        binding.edtCountry.setOnClickListener(this);
        binding.ccpSinUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSubmitResendOtp:
            if(verificationCode!=null) {
                if (!otpBinding.edtCode.getText().toString().isEmpty()) auth.signInWithCredential(PhoneAuthProvider.getCredential(verificationCode, otpBinding.edtCode.getText().toString())).addOnCompleteListener(SignUpActivity.this);
                else Toast.makeText(SignUpActivity.this, getResources().getString(R.string.please_enter_otp), Toast.LENGTH_SHORT).show();
            }else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please wait for the OTP",SignUpActivity.this);
            break;

            case R.id.tvResendOtp:
            if(refreshToken!=null) resendOTP();
            else sendOtp();
            break;

            case R.id.rlLoginSignUp: CommonUtil.startNewActivity(this, LoginActivity.class); break;

            case R.id.edtCountry: selectCountry(); break;

            case R.id.buttonSignUp: if (checkValidation()) checkPhoneApi(); break;
        }
    }

    public boolean checkValidation() {
        boolean validate = true;

        if (binding.edtName.getText().toString().trim().length()==0) {
            validate= false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter name",this);
        }

        else if (binding.edtEmail.getText().toString().trim().length()==0) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter email",this);
        }

        else if (!isValidEmail(binding.edtEmail.getText().toString().trim())) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter valid email",this);
        }


        else if (binding.edtPhone.getText().toString().trim().length() == 0) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter phone number",this);
        }

        else if(binding.edtPhone.getText().toString().trim().length() !=10){
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter phone number",this);
        }

        else if(binding.edtCountry.getText().toString().length() == 0) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),getString(R.string.please_enter_country_name),this);
        }

        else if (binding.edtPass.getText().toString().trim().length()==0) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter password",this);
        }

        else if (binding.edtPass.getText().toString().trim().length()<6 && binding.edtPass.getText().toString().trim().length()>=10) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),getString(R.string.password_should_not_more_than_10_character),this);
        }

        else if (!binding.edtPass.getText().toString().matches(CommonUtil.REGX)) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please ensure that password must contain one capital , one small and numeric number",this);
        }

        else if (binding.edtConfirmPass.getText().toString().length() == 0) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(), getString(R.string.please_enter_confirm_password), this);
        }
        else if (binding.edtConfirmPass.getText().toString().length() < 6 && binding.edtConfirmPass.getText().toString().length() >= 10) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(), getString(R.string.confirm_password_should_not_more_than_10_character), this);
        }

        else if (!binding.edtConfirmPass.getText().toString().matches(CommonUtil.REGX)) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(), "Please ensure that confirm password must contain one capital , one small and numeric number ", this);
        }

        return validate;
    }

    private void selectCountry() {
        CountryPicker picker = CountryPicker.getInstance("Select Country", new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code) {
                binding.edtCountry.setText(name);
                DialogFragment dialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag("CountryPicker");
                dialogFragment.dismiss();
            }
        });

        picker.show(getSupportFragmentManager(), "CountryPicker");
    }

    private void resendOTP() {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(binding.ccpSinUp.getSelectedCountryCodeWithPlus() + binding.edtPhone.getText().toString())
                                                                    .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallback).setForceResendingToken(refreshToken).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signUpApi() {
        if(otpDailog!=null) otpDailog.dismiss();
        dialog.showDialog();
        Call<SignUpModel> call = apiInterface.getSignUpResult(binding.edtName.getText().toString(),
                                                              binding.edtEmail.getText().toString(),
                                                              binding.edtPhone.getText().toString(),
                                                              binding.ccpSinUp.getSelectedCountryCodeWithPlus(),
                                                              binding.edtPass.getText().toString(), "Android",
                                                              SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.DEVICE_TOKEN),
                                                              binding.edtRefferalCode.getText().toString(),
                                                              SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.product_id),
                                                              SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.color_name),
                                                              SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.quantity),
                                                              SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.size));

        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                dialog.showDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(GlobalVariables.SUCCESS)) CommonUtil.saveSignupData(SignUpActivity.this,response);
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),SignUpActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",SignUpActivity.this);
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),SignUpActivity.this);
            }
        });
    }
    private void checkPhoneApi() {
        dialog.showDialog();
        Call<CheckPhoneModel> call = apiInterface.verifysignup(binding.edtPhone.getText().toString(),
                                                               binding.edtEmail.getText().toString());
        call.enqueue(new Callback<CheckPhoneModel>() {
            @Override
            public void onResponse(Call<CheckPhoneModel> call, Response<CheckPhoneModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(GlobalVariables.SUCCESS)) { OTPBottomSheet(); sendOtp(); }
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),SignUpActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",SignUpActivity.this);
            }

            @Override
            public void onFailure(Call<CheckPhoneModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),SignUpActivity.this);
            }
        });
    }

    private void OTPBottomSheet() {
      otpDailog = new BottomSheetDialog(SignUpActivity.this);
      otpBinding = PopUpResendOtpBinding.inflate(LayoutInflater.from(this),null);
      otpDailog.setContentView(otpBinding.getRoot());
      otpDailog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
      otpBinding.tvResendOtp.setOnClickListener(this);
      otpBinding.btnSubmitResendOtp.setOnClickListener(this);
      otpDailog.show();
    }

    private void sendOtp() {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(binding.ccpSinUp.getSelectedCountryCodeWithPlus()+binding.edtPhone.getText().toString())
                                                                    .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallback).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuth) {
            if(phoneAuth.getSmsCode()!=null) auth.signInWithCredential(phoneAuth).addOnCompleteListener(SignUpActivity.this);
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),e.getMessage(),SignUpActivity.this);
        }


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;
            refreshToken=forceResendingToken;
        }
    };


    @Override
    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
        if (task.isSuccessful()) signUpApi();
        else CommonUtil.setUpSnackbarMessage(binding.getRoot(),task.getException().getMessage(),SignUpActivity.this);
    }
}
