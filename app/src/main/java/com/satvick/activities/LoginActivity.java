package com.satvick.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.annotation.NonNull;
import javax.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityLoginCustomBinding;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginCustomBinding binding;

    String mEmailPhone = "", mPassword = "";
    String regex ="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*()\\-     _=+{}|?>.<,:;~`’]{6,}$";

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode = "";
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private String comeFrom="";
    private String commaSeparatedProductId="";

    //for social login
    CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 901;
    String name="",fbid="",email="";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_custom);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.satvick", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        binding.loginButton.setOnClickListener(this);
        binding.tvSignUp.setOnClickListener(this);
        binding.llSocial.setOnClickListener(this);
        binding.tvForgotPass.setOnClickListener(this);
        binding.tvLoginUsingOTP.setOnClickListener(this);
        binding.ivFb.setOnClickListener(this);
        binding.ivGoogle.setOnClickListener(this);
        binding.ivHome.setOnClickListener(this);

        getHashKey();

        //[fb register]
        fbRegisterCallBack();

        //[google register]
        googleRegister();

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            comeFrom=getIntent().getStringExtra("from");
        }
    }

    private void googleRegister() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void fbRegisterCallBack() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getBasicProfile(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(LoginActivity.this, R.string.cancel, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(LoginActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                        //Log.e(TAG, "onError: ", exception);
                    }
                });
    }

    private void getBasicProfile(LoginResult loginResult) {
        // App code
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("Facebook_Data", object.toString());
                        Log.v("response", response.toString());
                        // Application code

                         name = object.optString("name");
                         fbid = object.optString("id");
                         email = object.optString("email");

                         String profilePhoto = "https://graph.facebook.com/"+fbid+"/picture?type=large";

                        if (HelperClass.showInternetAlert(LoginActivity.this)) {
                            callLoginApiForSocial(name,fbid,email,profilePhoto,binding.mainSv,"Facebook");
                        }
                    }
                });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void callLoginApiForSocial(final String name, final String fbid, final String email,final String profilePhoto,final View view,final String socialType) {

        String deviceToken = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.DEVICE_TOKEN);

        final MyDialog myDialog=new MyDialog(LoginActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<SocialLoginModel> call = apiInterface.socialLogin(fbid, socialType,deviceToken,"android", name, email,profilePhoto,"",
                                                                SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.product_id),
                                                                SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.color_name),
                                                                SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.quantity),
                                                                SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.size));


        call.enqueue(new Callback<SocialLoginModel>() {
            @Override
            public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        SharedPreferenceWriter.getInstance(LoginActivity.this).getString(commaSeparatedProductId, "");
                        SharedPreferenceWriter.getInstance(LoginActivity.this).getString(SharedPreferenceKey.BATCH_COUNT, "");
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.FULL_NAME, response.body().getSociallogin().getName());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.EMAIL, (String) response.body().getSociallogin().getEmail());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.IMAGE, response.body().getSociallogin().getImage());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.USER_ID, "" + response.body().getSociallogin().getId());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.TOKEN, response.body().getSociallogin().getToken());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CRTEATED_AT, response.body().getSociallogin().getCreatedAt());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.UPDATED_AT, response.body().getSociallogin().getUpdatedAt());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS, true);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("from", "LoginButton");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                     else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        CommonUtil.setUpSnackbarMessage(binding.mainSv,response.body().getMessage(),LoginActivity.this);
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(LoginActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callLoginApiForSocial(name, fbid, email, profilePhoto,view,socialType);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                }
            }

            @Override
            public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void callSignupApi(final String name,final String email,final String fbid,final View view) {
        final MyDialog myDialog=new MyDialog(LoginActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<SignUpModel> call = apiInterface.getSignUpResult(name, email, "","","", "Android",SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.DEVICE_TOKEN),"",
                                                            SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.product_id),
                                                            SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.color_name),
                                                            SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.quantity),
                                                            SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.size));



        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                myDialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        //to get all response here,
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        SignUpModel signUpModel=response.body();

                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.FULL_NAME, signUpModel.getSignup().getName());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CONTACT_NUMBER, signUpModel.getSignup().getPhone());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.EMAIL, signUpModel.getSignup().getEmail());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.USER_ID, signUpModel.getSignup().getUser_id());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.TOKEN, signUpModel.getSignup().getToken());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.PASSWORD, signUpModel.getSignup().getPassword());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CONFIRM_PASSWORD, signUpModel.getSignup().getPassword_confirmation());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CONFIRM_PASSWORD, signUpModel.getSignup().getCountry());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, signUpModel.getSignup().getDeviceToken());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.DEVICE_TYPE, signUpModel.getSignup().getDeviceType());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.SOCIAL_ID, signUpModel.getSignup().getSocial_id());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");

                        //send at profile fragment,profile fragment is loaded at MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(LoginActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callSignupApi(name, fbid, email, view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.satvick", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                // Toast.makeText(getApplicationContext(),sign,Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    private boolean validateForm() {
        boolean validate = true;

        mEmailPhone = binding.edtPhoneEmail.getText().toString().trim();
        mPassword = binding.edtPass.getText().toString().trim();

        if (mEmailPhone.length() == 0) {
            Toast.makeText(this, R.string.please_enter_email_or_phone, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(mEmailPhone)) {
            Toast.makeText(LoginActivity.this, R.string.please_enter_valid_email, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPassword.length() == 0) {
            Toast.makeText(this, R.string.please_enter_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!mPassword.matches(regex)) {
            Toast.makeText(LoginActivity.this, "Please ensure that password must contain one capital , one small and numeric number", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (mPassword.length() < 6 && mPassword.length() >= 10) {
            Toast.makeText(LoginActivity.this, R.string.password_should_not_more_than_10_character, Toast.LENGTH_SHORT).show();
            return false;
        }
        return validate;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:

                if (validateForm()) {
                    if (HelperClass.showInternetAlert(LoginActivity.this)) {
                        commaSeparatedProductId= SharedPreferenceWriter.getInstance(this).getString("Ids");
                        callLoginApi(binding.mainSv);//hit api
                    }
                }
                break;

            case R.id.llSocial:
                startActivity(new Intent(this, MainActivity.class).putExtra("from", "LoginButton"));
                finish();
                break;

            case R.id.ivHome:
                startActivity(new Intent(this, MainActivity.class).putExtra("from", "LoginButton"));
                break;

            case R.id.ivFb:
                facebookLogin();
                break;

            case R.id.ivGoogle:
                googleSignIn();
                break;

            case R.id.tvSignUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.tvLoginUsingOTP:

                comeFrom="LoginUsingOTP";

                final BottomSheetDialog bottomSheetDialogg = new BottomSheetDialog(LoginActivity.this);
                final View vieww = LoginActivity.this.getLayoutInflater().inflate(R.layout.pop_up_enter_phone_number, null);
                bottomSheetDialogg.setContentView(vieww);
                bottomSheetDialogg.setCancelable(false);

                Button submitButtonOtp = vieww.findViewById(R.id.btnSubmitResendOtp);
                final EditText edtPhonee = vieww.findViewById(R.id.edtPhone);
                final CountryCodePicker ccpSinUpp=vieww.findViewById(R.id.ccpSinUp);

                submitButtonOtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(checkValidation(vieww.findViewById(R.id.mainRl),edtPhonee,vieww.getContext())){
                            if (HelperClass.showInternetAlert(LoginActivity.this)) {
                                callCheckPhoneApi(edtPhonee.getText().toString(),ccpSinUpp,vieww.findViewById(R.id.mainRl),bottomSheetDialogg);
                            }
                        }
                    }
                });

                bottomSheetDialogg.show();

                break;


            case R.id.tvForgotPass:
                comeFrom="ForgotPass";
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
                final View view1 = this.getLayoutInflater().inflate(R.layout.pop_up_forgot_password, null);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

                Button submitButton = view1.findViewById(R.id.btnSubmitForgotPass);
                final EditText edtPhone = view1.findViewById(R.id.edtPhone);
                final CountryCodePicker ccpSinUp=view1.findViewById(R.id.ccpSinUp);

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkValidation(view1.findViewById(R.id.mainRl),edtPhone,view1.getContext())){
                            if (HelperClass.showInternetAlert(LoginActivity.this)) {
                                callCheckPhoneApi(edtPhone.getText().toString(),ccpSinUp,binding.mainSv,bottomSheetDialog);
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                bottomSheetDialog.show();
                break;
        }
    }

    private boolean checkValidation(View viewById, EditText edtPhonee, Context context) {
        boolean  ret=true;

        if(edtPhonee.getText().toString().isEmpty())
        {
            ret=false;
            edtPhonee.setError("Please enter phone number");
            edtPhonee.setFocusable(true);
            edtPhonee.requestFocus();

        }else if(edtPhonee.getText().length()>10)
        {
            ret=false;
            edtPhonee.setError("Please enter valid phone number");
            edtPhonee.setFocusable(true);
            edtPhonee.requestFocus();

        }else if(edtPhonee.getText().toString().contains("+"))
        {
            ret=false;
            edtPhonee.setError("Please enter valid phone number");
            edtPhonee.setFocusable(true);
            edtPhonee.requestFocus();
        }

        return  ret;
    }

    private void googleSignIn() {
        mGoogleSignInClient.signOut(); //log out
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);// Activity is started with requestCode RC_SIGN_IN which is 901
    }


    private void facebookLogin() {
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        LoginManager.getInstance().logOut(); //log out
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList( "public_profile", "email"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);//for facebook
        super.onActivityResult(requestCode, resultCode, data);


        Log.e("aa","yes");
        // for google
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);



            String userProfile = account.getPhotoUrl().toString();
            String email = account.getEmail();
            String gid = account.getId();
            String name = account.getGivenName();


            if (HelperClass.showInternetAlert(LoginActivity.this)) {
                callLoginApiForSocial(name, gid, email,userProfile,binding.mainSv,"Google");
            }



            // Signed in successfully,show authenticated UI.
        } catch (ApiException e) {
            Log.e("errpr",e.getMessage());

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            // Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private void callCheckPhoneApi(final String phone, final CountryCodePicker ccpSinUp,final View view,final BottomSheetDialog bottomSheetDialogg) {
        final MyDialog myDialog=new MyDialog(LoginActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<CheckPhoneModel> call = apiInterface.getCheckPhoneResult(phone);

        call.enqueue(new Callback<CheckPhoneModel>() {
            @Override
            public void onResponse(Call<CheckPhoneModel> call, Response<CheckPhoneModel> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                        myDialog.hideDialog();
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)){
                        myDialog.hideDialog();
                        bottomSheetDialogg.dismiss();
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.PHONE,phone);
                        fireBaseAction(phone,ccpSinUp);
                        openBottomSheetPopUpForOTP();

                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(LoginActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callCheckPhoneApi(phone, ccpSinUp, view,bottomSheetDialogg);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<CheckPhoneModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }


    private void fireBaseAction(String phone, CountryCodePicker ccpSinUp) {
        StartFireBaseLogin();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ccpSinUp.getFullNumberWithPlus()+phone,// Phone number to verify
                60,                // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,      // Activity (for callback binding)
                mCallback);// OnVerificationStateChangedCallback
    }

    private void StartFireBaseLogin() {
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String otp = phoneAuthCredential.getSmsCode();
                Log.d("", "" + otp);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(LoginActivity.this, "Number verification failed!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                mResendToken = forceResendingToken;
                Log.e("OTP Code", s);

            }
        };
    }

    private void openBottomSheetPopUpForOTP() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginActivity.this);
        View view2 = LoginActivity.this.getLayoutInflater().inflate(R.layout.pop_up_resend_otp, null);
        bottomSheetDialog.setContentView(view2);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.dismiss();

        Button submitButtonOtp = view2.findViewById(R.id.btnSubmitResendOtp);

        final EditText edtCode=view2.findViewById(R.id.edtCode);

        submitButtonOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!edtCode.getText().toString().isEmpty()) {
                    MyDialog myDialog = new MyDialog(LoginActivity.this);
                    myDialog.showDialog();
                    verifyVerificationCode(edtCode.getText().toString(),myDialog,bottomSheetDialog);

                }else {
                    Toast.makeText(LoginActivity.this, "Please enter otp", Toast.LENGTH_SHORT).show();

                }
            }
        });

        bottomSheetDialog.show();
    }


    private void verifyVerificationCode(String edtCode, MyDialog myDialog,BottomSheetDialog dialog) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, edtCode);
        SignInWithPhone(credential,myDialog,dialog);
    }

        private void SignInWithPhone(PhoneAuthCredential credential, final MyDialog myDialog,final BottomSheetDialog dialog) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                myDialog.hideDialog();
                                if(comeFrom.equalsIgnoreCase("ForgotPass")){
                                    openPopUpChangePass();
                                }else {
                                    callCheckPhoneDataApiForLoginUsingOTP(binding.mainSv);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                                myDialog.hideDialog();
                            }
                        }
                    });
        }

    private void callCheckPhoneDataApiForLoginUsingOTP(final View view) {
        final MyDialog myDialog=new MyDialog(LoginActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<CheckPhoneDataModel> call = apiInterface.getCheckPhoneDataForLoginUsingOTPResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.PHONE),
                SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.DEVICE_TOKEN),"android");

        call.enqueue(new Callback<CheckPhoneDataModel>() {
            @Override
            public void onResponse(Call<CheckPhoneDataModel> call, Response<CheckPhoneDataModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {

                        CheckPhoneDataModel checkPhoneDataModel=response.body();

                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.FULL_NAME, checkPhoneDataModel.getCheckphonedata().getName());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CONTACT_NUMBER, checkPhoneDataModel.getCheckphonedata().getPhone());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.EMAIL, checkPhoneDataModel.getCheckphonedata().getEmail());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.USER_ID, checkPhoneDataModel.getCheckphonedata().getUserId());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.TOKEN, checkPhoneDataModel.getCheckphonedata().getToken());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");

                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("from","LoginButton");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(LoginActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callSignupApi(name, fbid, email, view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<CheckPhoneDataModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void openPopUpChangePass() {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginActivity.this);
                View view3 = LoginActivity.this.getLayoutInflater().inflate(R.layout.pop_up_reset_password, null);
                bottomSheetDialog.setContentView(view3);
                bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

                bottomSheetDialog.dismiss();

                Button btnResetPassword = view3.findViewById(R.id.btnResetPassword);

                final EditText edtNewPass = view3.findViewById(R.id.edtNewPassword);

                final EditText edtConfirmPass = view3.findViewById(R.id.edtConfirmPassword);

                btnResetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (edtNewPass.getText().toString().equals("")) {
                            Toast.makeText(LoginActivity.this, R.string.please_enter_new_password, Toast.LENGTH_LONG).show();
                        } else if (edtConfirmPass.getText().toString().equals("")) {
                            Toast.makeText(LoginActivity.this, R.string.please_enter_confirm_password, Toast.LENGTH_LONG).show();
                        }else {
                            if (edtNewPass.getText().toString().equals(edtNewPass.getText().toString())){
                                  callChangePasswordByPhoneApi(edtNewPass.getText().toString(),binding.mainSv);
                                } else {
                                   Toast.makeText(LoginActivity.this, "Please enter same new password and confirm password", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

        bottomSheetDialog.show();
    }

    private void callChangePasswordByPhoneApi(String edtNewPass,final View view) {

        final MyDialog myDialog=new MyDialog(LoginActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ForgotPasswordModel> call = apiInterface.getChangePassByPhoneResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.PHONE),edtNewPass);

        call.enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        Toast.makeText(LoginActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(LoginActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callSignupApi(name, fbid, email, view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();

                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }


    private void callLoginApi(final View view) {
        String deviceToken = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.DEVICE_TOKEN);

        final MyDialog myDialog=new MyDialog(LoginActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<LoginModel> call = apiInterface.getLoginResult(mEmailPhone, mPassword, "android", deviceToken,
                                                            SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.product_id),
                                                            SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.color_name),
                                                            SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.quantity),
                                                            SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.size));
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {


                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //blank cart items
                        // SharedPreferenceWriter.getInstance(LoginActivity.this).getString(SharedPreferenceKey.COMMA_SEPARATED_PRODUCT_ID, "");
                        SharedPreferenceWriter.getInstance(LoginActivity.this).getString("commaSeparatedProductId","");
                        SharedPreferenceWriter.getInstance(LoginActivity.this).getString(SharedPreferenceKey.BATCH_COUNT,"");

                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.FULL_NAME, response.body().getLoginResponse().getName());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CONTACT_NUMBER, response.body().getLoginResponse().getPhone());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.EMAIL, response.body().getLoginResponse().getEmail());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.IMAGE, response.body().getLoginResponse().getImage());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.USER_ID, response.body().getLoginResponse().getId());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.TOKEN, response.body().getLoginResponse().getToken());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.PASSWORD, response.body().getLoginResponse().getPassword());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CRTEATED_AT, response.body().getLoginResponse().getCreated_at());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.UPDATED_AT, response.body().getLoginResponse().getUpdated_at());
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
                        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS, true);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("from", "LoginButton");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(LoginActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callSignupApi(name, fbid, email, view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(LoginActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
       // startActivity(new Intent(this,MainActivity.class));
    }


}
