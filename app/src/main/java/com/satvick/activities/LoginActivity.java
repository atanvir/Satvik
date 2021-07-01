package com.satvick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityLoginCustomBinding;
import com.satvick.databinding.PopUpEnterPhoneNumberBinding;
import com.satvick.databinding.PopUpForgotPasswordBinding;
import com.satvick.databinding.PopUpResendOtpBinding;
import com.satvick.databinding.PopUpResetPasswordBinding;
import com.satvick.model.CheckPhoneDataModel;
import com.satvick.model.CheckPhoneModel;
import com.satvick.model.ForgotPasswordModel;
import com.satvick.model.LoginModel;
import com.satvick.model.SignUpModel;
import com.satvick.model.SocialLoginModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback, OnCompleteListener<AuthResult> {

    private ActivityLoginCustomBinding binding;
    private MyDialog dialog;
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);

    // OTP
    private FirebaseAuth auth;
    private String verificationCode = null,mobileNo="";
    private PhoneAuthProvider.ForceResendingToken refreshToken;


    //Social Login
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 901;


    // Phone Number Bottom Sheet
    private BottomSheetDialog phoneNoBottomSheet;
    private PopUpEnterPhoneNumberBinding phoneNumberBinding;

    // Forgot Password Bottom Sheet
    private BottomSheetDialog forgotPasswordBottomSheet;
    private PopUpForgotPasswordBinding forgotPasswordBinding;

    // OTP Verfication Bottom Sheet
    private BottomSheetDialog otpBottomSheet;
    private PopUpResendOtpBinding otpBinding;


    // Change Password Bottom Sheet
    private PopUpResetPasswordBinding resetPasswordBinding;
    private BottomSheetDialog resetPasswordBottomSheet;

    private boolean forgotPassword=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginCustomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_custom);
        init();
        initCtrl();
    }

    private void init() {
        dialog=new MyDialog(this);
        auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("en");
        callbackManager = CallbackManager.Factory.create();

        mGoogleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build());
        LoginManager.getInstance().registerCallback(callbackManager, this);

    }

    private void initCtrl(){
        binding.loginButton.setOnClickListener(this);
        binding.tvSignUp.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);
        binding.tvForgotPass.setOnClickListener(this);
        binding.tvLoginUsingOTP.setOnClickListener(this);
        binding.ivFb.setOnClickListener(this);
        binding.ivGoogle.setOnClickListener(this);
        binding.ivHome.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // OTP Verification Bottom Sheet
            case R.id.btnSubmitResendOtp:
            if(verificationCode!=null) {
                if (!otpBinding.edtCode.getText().toString().isEmpty()){ dialog.showDialog(); auth.signInWithCredential(PhoneAuthProvider.getCredential(verificationCode, otpBinding.edtCode.getText().toString())).addOnCompleteListener(this); }
                else Toast.makeText(this, getResources().getString(R.string.please_enter_otp), Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "Please wait for the otp", Toast.LENGTH_SHORT).show();
            break;

            case R.id.tvResendOtp:
            if(refreshToken!=null) resendOTP();
            else sendOTP();
            break;


            // Login By OTP Bottom Sheet
            case R.id.btnSubmitBottomSheet : if (validationLoginByOTP()) { forgotPassword=false; checkPhoneApi();} break;

            // Forgot Password Bottom Sheet
            case R.id.btnSubmitForgotPass: if(validationForgotPassword()) { forgotPassword=true; checkPhoneApi(); } break;

            // Change Password Bottom Sheet
            case R.id.btnResetPassword : if(validationChangePassword()) changePasswordApi(); break;

            // This
            case R.id.loginButton: if (validation()) loginApi(); break;
            case R.id.llSocial: CommonUtil.startNewActivity(this,MainActivity.class); break;
            case R.id.ivHome:binding.llSocial.performClick(); break;
            case R.id.ivFb: facebookLogin(); break;
            case R.id.ivGoogle: googleSignIn(); break;
            case R.id.tvSignUp: CommonUtil.startNewActivity(this, SignUpActivity.class); break;
            case R.id.tvLoginUsingOTP : loginbyOtpBottomSheet(); break;
            case R.id.tvForgotPass: forgotPasswordBottomSheet(); break;
        }
    }


    private void getBasicProfile(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void socialLoginApi(String name, String fbid, String email, String profilePhoto,String socialType) {
        dialog.showDialog();
        Call<SocialLoginModel> call = apiInterface.socialLogin(fbid, socialType, SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.DEVICE_TOKEN),
                                                     "android", name, email, profilePhoto, "",
                                                                SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.product_id),
                                                                SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.color_name),
                                                                SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.quantity),
                                                                SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.size));


        call.enqueue(new Callback<SocialLoginModel>() {
            @Override
            public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(GlobalVariables.SUCCESS)) CommonUtil.saveData(LoginActivity.this,response);
                    else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(), response.body().getMessage(), LoginActivity.this);

                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",LoginActivity.this);
            }

            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),LoginActivity.this);
            }
        });
    }

    private void loginbyOtpBottomSheet() {
        phoneNoBottomSheet = new BottomSheetDialog(LoginActivity.this);
        phoneNumberBinding= PopUpEnterPhoneNumberBinding.inflate(getLayoutInflater(),null);
        phoneNoBottomSheet.setContentView(phoneNumberBinding.getRoot());
        phoneNumberBinding.btnSubmitBottomSheet.setOnClickListener(this);
        phoneNoBottomSheet.show();
    }
    private void forgotPasswordBottomSheet() {
        forgotPasswordBottomSheet = new BottomSheetDialog(this);
        forgotPasswordBinding=PopUpForgotPasswordBinding.inflate(getLayoutInflater(),null);
        forgotPasswordBottomSheet.setContentView(forgotPasswordBinding.getRoot());
        forgotPasswordBottomSheet.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        forgotPasswordBinding.btnSubmitForgotPass.setOnClickListener(this);
        forgotPasswordBottomSheet.show();
    }
    private void otpBottomSheet() {
        otpBottomSheet = new BottomSheetDialog(this);
        otpBinding = PopUpResendOtpBinding.inflate(LayoutInflater.from(this),null);
        otpBottomSheet.setContentView(otpBinding.getRoot());
        otpBottomSheet.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        otpBinding.tvResendOtp.setOnClickListener(this);
        otpBinding.btnSubmitResendOtp.setOnClickListener(this);
        otpBottomSheet.show();
    }
    private void changePasswordBottomSheet() {
        resetPasswordBottomSheet = new BottomSheetDialog(LoginActivity.this);
        resetPasswordBinding=PopUpResetPasswordBinding.inflate(getLayoutInflater(),null);
        resetPasswordBottomSheet.setContentView(resetPasswordBinding.getRoot());
        resetPasswordBottomSheet.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        resetPasswordBinding.btnResetPassword.setOnClickListener(this);
        resetPasswordBottomSheet.show();
    }

    private boolean validation() {
        boolean validate = true;

        if (binding.edtPhoneEmail.getText().toString().trim().length() == 0) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),getString(R.string.please_enter_email_or_phone),this);
        } else if (!CommonUtil.isValidEmail(binding.edtPhoneEmail.getText().toString().trim())) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),getString(R.string.please_enter_valid_email),this);

        } else if (binding.edtPass.getText().toString().trim().length() == 0) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),getString(R.string.please_enter_password),this);
        } else if (!binding.edtPass.getText().toString().trim().matches(CommonUtil.REGX)) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please ensure that password must contain one capital , one small and numeric number",this);

        } else if (binding.edtPass.getText().toString().trim().length() < 6 && binding.edtPass.getText().toString().trim().length() >= 10) {
            validate=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),getString(R.string.password_should_not_more_than_10_character),this);

        }
        return validate;
    }
    private boolean validationLoginByOTP() {
        boolean ret = true;

        if (phoneNumberBinding.edtPhone.getText().toString().isEmpty()) {
            ret = false;
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        } else if (phoneNumberBinding.edtPhone.getText().length() !=10) {
            ret = false;
            Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();

        }
        return ret;
    }
    private boolean validationForgotPassword() {
        boolean ret = true;

        if (forgotPasswordBinding.edtPhone.getText().toString().isEmpty()) {
            ret = false;
            Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
        } else if (forgotPasswordBinding.edtPhone.getText().length() != 10) {
            ret = false;
            Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();

        }
        return ret;
    }
    private boolean validationChangePassword() {
        boolean ret=true;
        if (resetPasswordBinding.edtNewPassword.getText().toString().equals("")) {
            ret=false;
            Toast.makeText(LoginActivity.this, R.string.please_enter_new_password, Toast.LENGTH_LONG).show();
        } else if (resetPasswordBinding.edtConfirmPassword.getText().toString().equals("")) {
            ret=false;
            Toast.makeText(LoginActivity.this, R.string.please_enter_confirm_password, Toast.LENGTH_LONG).show();
        } else if(!resetPasswordBinding.edtNewPassword.getText().toString().equals(resetPasswordBinding.edtConfirmPassword.getText().toString())) {
            ret=false;
            Toast.makeText(LoginActivity.this, "Please enter same new password and confirm password", Toast.LENGTH_LONG).show();
        }

        return ret;
    }

    private void facebookLogin() {
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void googleSignIn() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);//for facebook
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RC_SIGN_IN: handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data)); break;
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (HelperClass.showInternetAlert(LoginActivity.this)) socialLoginApi(account.getGivenName(), account.getId(),  account.getEmail(), account.getPhotoUrl().toString(), "Google");
        } catch (Exception e) {
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),e.getMessage(),this);
        }
    }


    private void checkPhoneApi() {
        dialog.showDialog();
        Call<CheckPhoneModel> call = apiInterface.getCheckPhoneResult(phoneNumberBinding!=null?phoneNumberBinding.edtPhone.getText().toString():forgotPasswordBinding.edtPhone.getText().toString());
        call.enqueue(new Callback<CheckPhoneModel>() {
            @Override
            public void onResponse(Call<CheckPhoneModel> call, Response<CheckPhoneModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        if(phoneNumberBinding!=null) mobileNo = phoneNumberBinding.ccpSinUp.getSelectedCountryCodeWithPlus() + phoneNumberBinding.edtPhone.getText().toString().trim();
                        else mobileNo = forgotPasswordBinding.ccpSinUp.getSelectedCountryCodeWithPlus() + forgotPasswordBinding.edtPhone.getText().toString().trim();
                        sendOTP();
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.PHONE, phoneNumberBinding!=null?phoneNumberBinding.edtPhone.getText().toString().trim():forgotPasswordBinding.edtPhone.getText().toString().trim());
                        if(phoneNoBottomSheet!=null) phoneNoBottomSheet.dismiss();
                        if(forgotPasswordBottomSheet!=null) forgotPasswordBottomSheet.dismiss();
                        otpBottomSheet();
                    }
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",LoginActivity.this);
            }

            @Override
            public void onFailure(Call<CheckPhoneModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),LoginActivity.this);
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback  = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            if(phoneAuthCredential.getSmsCode()!=null){
                dialog.showDialog();
                otpBinding.edtCode.setText(phoneAuthCredential.getSmsCode());
                auth.signInWithCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;
            refreshToken=forceResendingToken;
        }
    };



    private void sendOTP() {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(mobileNo).setTimeout(60L, TimeUnit.SECONDS)
                                                                    .setActivity(this).setCallbacks(mCallback).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendOTP() {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(mobileNo).setTimeout(60L, TimeUnit.SECONDS)
                                                                    .setActivity(this).setCallbacks(mCallback).setForceResendingToken(refreshToken).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    private void checkMobileApi() {
        dialog.showDialog();
        Call<CheckPhoneDataModel> call = apiInterface.getCheckPhoneDataForLoginUsingOTPResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.PHONE),
                                                                                              SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.DEVICE_TOKEN), "android");
        call.enqueue(new Callback<CheckPhoneDataModel>() {
            @Override
            public void onResponse(Call<CheckPhoneDataModel> call, Response<CheckPhoneDataModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) saveUserData(response);
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),LoginActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",LoginActivity.this);
            }

            @Override
            public void onFailure(Call<CheckPhoneDataModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),LoginActivity.this);
            }
        });
    }

    private void saveUserData(Response<CheckPhoneDataModel> response) {
        CheckPhoneDataModel checkPhoneDataModel = response.body();
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.FULL_NAME, checkPhoneDataModel.getCheckphonedata().getName());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CONTACT_NUMBER, checkPhoneDataModel.getCheckphonedata().getPhone());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.EMAIL, checkPhoneDataModel.getCheckphonedata().getEmail());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.USER_ID, checkPhoneDataModel.getCheckphonedata().getUserId());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.TOKEN, checkPhoneDataModel.getCheckphonedata().getToken());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }


    private void changePasswordApi() {
        dialog.showDialog();
        Call<ForgotPasswordModel> call = apiInterface.getChangePassByPhoneResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.PHONE),
                                                                                 resetPasswordBinding.edtNewPassword.getText().toString().trim());
        call.enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {
                if(otpBottomSheet!=null) otpBottomSheet.dismiss();
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        Toast.makeText(LoginActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    } else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),LoginActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error", LoginActivity.this);
            }

            @Override
            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                if(otpBottomSheet!=null) otpBottomSheet.dismiss();
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),LoginActivity.this);
            }
        });
    }


    private void loginApi() {
        dialog.showDialog();
        Call<LoginModel> call = apiInterface.getLoginResult(binding.edtPhoneEmail.getText().toString().trim(),
                                                            binding.edtPass.getText().toString().trim(), "android",
                                                            SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.DEVICE_TOKEN),
                                                            SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.product_id),
                                                            SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.color_name),
                                                            SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.quantity),
                                                            SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.size));
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                dialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) CommonUtil.saveLogin(LoginActivity.this,response);
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),LoginActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",LoginActivity.this);
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                dialog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),LoginActivity.this);
            }
        });
    }



    @Override
    public void onSuccess(LoginResult loginResult) {
        getBasicProfile(loginResult);
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException error) {
     CommonUtil.setUpSnackbarMessage(binding.getRoot(),error.getMessage(),this);
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        if (HelperClass.showInternetAlert(LoginActivity.this)) socialLoginApi(object.optString("name"), object.optString("id"), object.optString("email"), "https://graph.facebook.com/" + object.optString("id") + "/picture?type=large",  "Facebook");
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        dialog.hideDialog();
        if(task.isSuccessful()) {
            if(otpBottomSheet!=null) otpBottomSheet.dismiss();
            if(forgotPassword) changePasswordBottomSheet(); else checkMobileApi();
        }else Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
    }
}
