package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.juanpabloprado.countrypicker.CountryPicker;
import com.juanpabloprado.countrypicker.CountryPickerListener;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivitySignupCustomBinding;
import com.satvick.model.CheckPhoneModel;
import com.satvick.model.SignUpModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.satvick.utils.Utility.isValidEmail;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySignupCustomBinding binding;

    String mName = "", mPhone = "", mEmail = "", mPassword = "", mConfirmPass = "", mCountry = "";
    String countryCodeAndroid = "";
    String countryName="";
    String otpValue="";
    String regex ="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*()\\-     _=+{}|?>.<,:;~`’]{6,}$";

    private FirebaseAuth mAuth;
    private String verificationCode = "";
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup_custom);
        binding.rlLoginSignUp.setOnClickListener(this);
        binding.buttonSignUp.setOnClickListener(this);
        binding.edtCountry.setOnClickListener(this);
        binding.ccpSinUp.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        binding.ccpSinUp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeAndroid = binding.ccpSinUp.getSelectedCountryCodeWithPlus();
                //String countryName= binding.ccpSinUp.getSelectedCountryName();
                //binding.edtCountry.setText(countryName);
            }
        });
    }


    //METHOD: check for the validation of the form fields and send OTP service call
    public boolean validateForm() {
        boolean validate = true;

        mName = binding.edtName.getText().toString().trim();
        mPhone = binding.edtPhone.getText().toString().trim();
        mEmail = binding.edtEmail.getText().toString().trim();
        mPassword = binding.edtPass.getText().toString().trim();
        mConfirmPass = binding.edtConfirmPass.getText().toString().trim();
        mCountry = binding.edtCountry.getText().toString().trim();


        if (mName.length()==0) {
            Toast.makeText(this, R.string.please_enter_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (mName.length() < 4 && mName.length() >= 40) {
            Toast.makeText(SignUpActivity.this, R.string.name_should_not_more_than_fourty_character, Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (mPhone.length() == 0 && mPhone.length() >= 15) {
            Toast.makeText(SignUpActivity.this, R.string.please_enter_phone_number, Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (mEmail.length()==0) {
            Toast.makeText(this, "Please enter email id", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (!isValidEmail(mEmail)) {
            Toast.makeText(SignUpActivity.this, R.string.please_enter_valid_email, Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (mPassword.length()==0) {
            Toast.makeText(this, R.string.please_enter_password, Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (mPassword.length() < 6 && mPassword.length() >= 10) {
            Toast.makeText(SignUpActivity.this, R.string.password_should_not_more_than_10_character, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!mPassword.matches(regex)) {
            Toast.makeText(this, "Please ensure that password must contain one capital , one small and numeric number", Toast.LENGTH_SHORT).show();
            return false;
        }


        else if (mConfirmPass.length() == 0) {
            Toast.makeText(this, R.string.please_enter_confirm_password, Toast.LENGTH_SHORT).show();
            return false;

        }
        else if (mConfirmPass.length() < 6 && mConfirmPass.length() >= 10) {
            Toast.makeText(SignUpActivity.this, R.string.confirm_password_should_not_more_than_10_character, Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (!mConfirmPass.matches(regex)) {
            Toast.makeText(this, "Please ensure that confirm password must contain one capital , one small and numeric number", Toast.LENGTH_SHORT).show();
            return false;
        }


        else if(mCountry.length() == 0) {
            Toast.makeText(this, R.string.please_enter_country_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        return validate;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlLoginSignUp:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

           case R.id.edtCountry:
                    final CountryPicker picker = CountryPicker.getInstance("Select Country", new CountryPickerListener() {
                        @Override
                        public void onSelectCountry(String name, String code) {
                            // Toast.makeText(SignUpAct.this, "Name: " + name, Toast.LENGTH_SHORT).show();
                            binding.edtCountry.setText(name);
                            countryName = name;
                            DialogFragment dialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag("CountryPicker");
                            dialogFragment.dismiss();
                        }
                    });
                    picker.show(getSupportFragmentManager(), "CountryPicker");

            case R.id.buttonSignUp:
                if (validateForm()) {
                    if (HelperClass.showInternetAlert(SignUpActivity.this)) {
                        callCheckPhoneApi(binding.mainRl);
//                        openBottomSheetPopUpForOTP();
//                        fireBaseAction();
                    }
                }

                break;
        }
    }

    private void callCheckPhoneApi(final View view) {
        final MyDialog myDialog=new MyDialog(SignUpActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<CheckPhoneModel> call = apiInterface.verifysignup(mPhone,mEmail);

        call.enqueue(new Callback<CheckPhoneModel>() {
            @Override
            public void onResponse(Call<CheckPhoneModel> call, Response<CheckPhoneModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                         openBottomSheetPopUpForOTP();
                         fireBaseAction();

                    } else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        setUpSnackbarMessage(view,response.body().getMessage());
                    }
                } else {

                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callCheckPhoneApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<CheckPhoneModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void setUpSnackbarMessage(View view,String message) {
       Snackbar mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        mSnackbar.setActionTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.colorWhite));
        mSnackbar.getView().setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.drawable_gradient_line));
        mSnackbar.show();

    }

    private void openBottomSheetPopUpForOTP() {
        final BottomSheetDialog bottomSheetDialogg = new BottomSheetDialog(SignUpActivity.this);
        View vieww = SignUpActivity.this.getLayoutInflater().inflate(R.layout.pop_up_resend_otp, null);
        bottomSheetDialogg.setContentView(vieww);
        bottomSheetDialogg.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        bottomSheetDialogg.dismiss();

        final EditText edtCode=vieww.findViewById(R.id.edtCode);

        Button submitButtonOtp = vieww.findViewById(R.id.btnSubmitResendOtp);

        submitButtonOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogg.dismiss();

               if(!edtCode.getText().toString().isEmpty())
                    verifyVerificationCode(edtCode.getText().toString());
                else
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.please_enter_otp), Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialogg.show();
    }

    private void verifyVerificationCode(String edtCode) {
        final MyDialog myDialog=new MyDialog(SignUpActivity.this);
        myDialog.showDialog();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, edtCode);
        SignInWithPhone(credential,myDialog);
    }

    private void SignInWithPhone(PhoneAuthCredential credential, final MyDialog myDialog) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            myDialog.hideDialog();
                            callSignUpApi(binding.mainRl);
                        } else {
                            setUpSnackbarMessage(binding.mainRl,"Incorrect OTP");

                            myDialog.hideDialog();
                        }
                    }
                });
    }

    private void fireBaseAction() {


        Log.e("phone_number",binding.ccpSinUp.getFullNumberWithPlus()+binding.edtPhone.getText().toString());

        PhoneAuthProvider.getInstance().verifyPhoneNumber(binding.ccpSinUp.getFullNumberWithPlus()+binding.edtPhone.getText().toString(),// Phone number to verify
                                                            60, TimeUnit.SECONDS, this, mCallback);


    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String otp = phoneAuthCredential.getSmsCode();
            Log.d("", "" + otp);
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {

            Log.w("Failed", "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Log.e("invalid_request",e.getMessage());
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {

                Log.e("sms_limit",e.getMessage());
                // The SMS quota for the project has been exceeded
                // ...
            }
        }


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;
            mResendToken = forceResendingToken;
            Log.e("OTP Code", s);
            //Toast.makeText(SignUpActivity.this, "Sending  otp...", Toast.LENGTH_SHORT).show();
        }
    };



    private void callSignUpApi(final View view) {

        final MyDialog myDialog=new MyDialog(SignUpActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<SignUpModel> call = apiInterface.getSignUpResult(mName, mEmail, mPhone,mCountry,mPassword, "Android",
                                                                SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.DEVICE_TOKEN),
                                                                binding.edtRefferalCode.getText().toString(),
                                                                SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.product_id),
                                                                SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.color_name),
                                                                SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.quantity),
                                                                SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.size));

       call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        Toast.makeText(SignUpActivity.this, "User sign up successfully", Toast.LENGTH_SHORT).show();

                        SignUpModel signUpModel=response.body();

                        SharedPreferenceWriter.getInstance(SignUpActivity.this).writeStringValue(SharedPreferenceKey.FULL_NAME, signUpModel.getSignup().getName());
                        SharedPreferenceWriter.getInstance(SignUpActivity.this).writeStringValue(SharedPreferenceKey.CONTACT_NUMBER, signUpModel.getSignup().getPhone());
                        SharedPreferenceWriter.getInstance(SignUpActivity.this).writeStringValue(SharedPreferenceKey.EMAIL, signUpModel.getSignup().getEmail());
                        SharedPreferenceWriter.getInstance(SignUpActivity.this).writeStringValue(SharedPreferenceKey.USER_ID, signUpModel.getSignup().getUser_id());
                        SharedPreferenceWriter.getInstance(SignUpActivity.this).writeStringValue(SharedPreferenceKey.TOKEN, signUpModel.getSignup().getToken());
                        SharedPreferenceWriter.getInstance(SignUpActivity.this).writeStringValue(SharedPreferenceKey.COUNTRY, signUpModel.getSignup().getCountry());
                        SharedPreferenceWriter.getInstance(SignUpActivity.this).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
                        SharedPreferenceWriter.getInstance(SignUpActivity.this).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS, true);

                        //send at profile fragment,profile fragment is loaded at MainActivity
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(SignUpActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callCheckPhoneApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
       // finishAffinity();
        startActivity(new Intent(this,MainActivity.class));
    }
}
