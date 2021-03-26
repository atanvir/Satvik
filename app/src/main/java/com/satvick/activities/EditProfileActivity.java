package com.satvick.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityEditProfleBinding;
import com.satvick.databinding.PopUpProfileUpdatedBinding;
import com.satvick.model.CheckPhoneModel;
import com.satvick.model.EditProfileModel;
import com.satvick.model.ViewProfileModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.ChangeImageRotation;
import com.satvick.utils.FilePath;
import com.satvick.utils.GlobalVariables;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEditProfleBinding binding;

    private String mUserId = "";
    private String token = "";

    private String mUserImage = "";
    private String mFullName="";

    private String mEmail = "";
    private String mPhone = "";
    private String mGender="";
    private String mDob="";

    private final int CAMERA_REQ_CODE = 10;
    private final int GALLERY_REQ_CODE = 12;
    private String imageFilePathProfile = null;
    private Uri imageUriProfile = null;
    private String imageFilePath = null;
    private File fileFlyer=null;
    private int START_VERIFICATION = 1001;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    String path="";
    private final String capture_dir = Environment.getExternalStorageDirectory() + "/YOD/Images/";



    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode = "";
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profle);

        binding.edtGender.setOnClickListener(this);
        binding.edtDOB.setOnClickListener(this);
        binding.tvSaveDetails.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);


        token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        mUserId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);


        Bundle bundle = getIntent().getExtras();

        mEmail = bundle.getString("email");
        mFullName = bundle.getString("name");
        mPhone = bundle.getString("phone");
        mUserImage = bundle.getString("image");
        mDob=bundle.getString("dob");
        mGender=bundle.getString("gender");

        if (!mFullName.isEmpty() && mFullName != null) {
            binding.tvName.setText(mFullName);
        }

        if(!mFullName.isEmpty() && mFullName != null){
            binding.edtFullName.setText(mFullName);
        }

        if(mPhone != null && !mPhone.isEmpty() && !mPhone.equals("null")){
            binding.edtPhone.setText(mPhone);
        }

        if(mEmail!=null){
            binding.edtEmail.setText(mEmail);
        }

        if(!mDob.isEmpty() && mDob!=null){
            binding.edtDOB.setText(mDob);
        }

        if(!mGender.isEmpty() && mGender!=null){
            binding.edtGender.setText(mGender);
        }

        if (mUserImage != null) {
            Picasso.with(this).load(mUserImage).into(binding.ivProfilePic);
        }

        binding.edtGender.setOnClickListener(this);
        binding.edtDOB.setOnClickListener(this);
        binding.tvSaveDetails.setOnClickListener(this);
        binding.ivProfilePic.setOnClickListener(this);

        token = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN);
        mUserId = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID);

        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                Date date= null;
                try {
                    date = simpleDateFormat.parse(dayOfMonth+"/"+(month+1)+"/"+year);
                    StringBuilder builder=new StringBuilder();
                    builder.append(simpleDateFormat.format(date));
                    binding.edtDOB.setText(builder);


                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };


       // callViewProfileApi();
    }



    private void callViewProfileApi() {

        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ViewProfileModel> call = apiInterface.getViewProfileResult(token, mUserId);

        call.enqueue(new Callback<ViewProfileModel>() {
            @Override
            public void onResponse(Call<ViewProfileModel> call, Response<ViewProfileModel> response) {
                if (response.isSuccessful()) {
                    myDialog.hideDialog();

                    ViewProfileModel data = response.body();

                    if (data.getStatus().equals("SUCCESS")) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        setUI(data);//set data

                    } else {
                        Toast.makeText(EditProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();

                    Toast.makeText(EditProfileActivity.this, R.string.service_error, Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ViewProfileModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });

    }

    private void setUI(ViewProfileModel data) {

        mUserImage = data.getViewProfileResponse().getImage();
        mFullName = data.getViewProfileResponse().getName();
        mEmail =data.getViewProfileResponse().getEmail();
        mPhone =data.getViewProfileResponse().getPhone();
        mDob =data.getViewProfileResponse().getDob();
        mGender=data.getViewProfileResponse().getGender();

        if (!mFullName.isEmpty() && mFullName != null) {
            binding.tvName.setText(mFullName);
        }

        if (!mFullName.isEmpty() && mFullName != null) {
            binding.edtFullName.setText(mFullName);
        }

        if (!mUserImage.isEmpty() && mUserImage != null) {
            Picasso.with(this).load(mUserImage).into(binding.ivProfilePic);
        }

        if (!mEmail.isEmpty() && mEmail != null) {
            binding.edtEmail.setText(mEmail);
        }

        if (mPhone != null && !mPhone.equalsIgnoreCase("")) {
            binding.edtPhone.setText(mPhone);
        }

        if (!mDob.isEmpty() && mDob != null) {
            binding.edtDOB.setText(mDob);
        }

        if (!mGender.isEmpty() && mGender != null) {
            binding.edtGender.setText(mGender);
        }
    }

    private void showMenuList() {
        final List<String> selectedMenuList = new ArrayList<>();
        selectedMenuList.add("Male");
        selectedMenuList.add("Female");
        selectedMenuList.add("Others");

        ArrayAdapter arrayAdapter = new ArrayAdapter(EditProfileActivity.this,
                android.R.layout.simple_spinner_dropdown_item, selectedMenuList);

        final ListPopupWindow listPopupWindow = new ListPopupWindow(EditProfileActivity.this);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedMenu = selectedMenuList.get(i);
                binding.edtGender.setText(selectedMenu);

                listPopupWindow.dismiss();
            }
        });

        listPopupWindow.setAnchorView(binding.edtGender);
        listPopupWindow.setAdapter(arrayAdapter);
        listPopupWindow.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edtGender:
                showMenuList();
                break;

            case R.id.edtDOB:
                Date date= Calendar.getInstance().getTime();
                DatePickerDialog dialog=new DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT,datePickerListener,Integer.parseInt(new SimpleDateFormat("yyyy").format(date)),
                        Integer.parseInt(new SimpleDateFormat("MM").format(date))-1,Integer.parseInt(new SimpleDateFormat("dd").format(date)));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Save", dialog);
                dialog.show();
                break;

            case R.id.ivProfilePic:
                selectImage();
                break;

            case R.id.tvSaveDetails:
               // callCheckPhoneApi(binding.edtPhone.getText().toString());
                if(checkValidation())
                callEditProfileApi(binding.mainRl);
                break;

            case R.id.ivBack:
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class).putExtra("from", "EditProfileFragment"));
                break;
        }
    }

    private boolean checkValidation() {
        boolean ret=true;

        if(binding.edtGender.getText().toString().isEmpty())
        {
          showMessage("Please select gender");
            ret=false;
        }else if(binding.edtFullName.getText().toString().isEmpty())
        {
            showMessage("Please enter full name");
            ret=false;
        }
        else if(binding.edtEmail.getText().toString().isEmpty())
        {
            showMessage("Please enter email id");
            ret=false;
        }
        else if(binding.edtPhone.getText().toString().isEmpty())
        {
            showMessage("Please enter phone number");
            ret=false;
        }
        else if(binding.edtDOB.getText().toString().isEmpty())
        {
            showMessage("Please enter DOB number");
            ret=false;

        }
        else if(fileFlyer==null  && mUserImage.isEmpty())
        {
            showMessage("Please upload image");
            ret=false;

        }


        return  ret;

    }

    private void showMessage(String message) {

        Snackbar snackbar= Snackbar.make(binding.mainRl,message,Snackbar.LENGTH_LONG);
        snackbar.getView().setBackground(ContextCompat.getDrawable(this,R.drawable.drawable_gradient_line));
        snackbar.setActionTextColor(ContextCompat.getColor(this,R.color.colorWhite));
        snackbar.show();
    }

    private void selectImage() {

        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                String path=FilePath.getPath(EditProfileActivity.this,result.getUri());
                ChangeImageRotation rotation=new ChangeImageRotation(this);
                String rotatedImage=rotation.setCapturedImage(path);
                try {
                    fileFlyer= new Compressor(this).compressToFile(new File(rotatedImage));
                } catch (IOException e) {
                    e.printStackTrace();
                    fileFlyer=new File(rotatedImage);
                }

                binding.ivProfilePic.setImageURI(Uri.fromFile(fileFlyer));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }





    //start for multipart data
    private Map<String, RequestBody> setUpMapData() {

        Map<String, RequestBody> fields = new HashMap<>();

        String email = "";
        String phone = "";
        String fullName = "";
        String gender="";
        String dob="";

        RequestBody email_idbody = null;
        if (this.mEmail != null && this.mEmail.equals(binding.edtEmail.getText().toString())) {
            email_idbody = RequestBody.create(MediaType.parse("text/plain"), mEmail);//all ready existed email
            fields.put("email", email_idbody);
        } else {
            email = binding.edtEmail.getText().toString();
            email_idbody = RequestBody.create(MediaType.parse("text/plain"), email);//edited email
            fields.put("email", email_idbody);
        }


        RequestBody phone_body = null;
        if (this.mPhone != null && this.mPhone.equals(binding.edtPhone.getText().toString())) {
            phone_body = RequestBody.create(MediaType.parse("text/plain"), mPhone);
            fields.put("phone", phone_body);
        } else {
            //if phone number is different then first need to verify OTP because that number is available or not
            phone = binding.edtPhone.getText().toString();
            phone_body = RequestBody.create(MediaType.parse("text/plain"), phone);
            fields.put("phone", phone_body);
        }


        RequestBody name_body = null;
        if (this.mFullName != null && this.mFullName.equals(binding.edtFullName.getText().toString())) {
            name_body = RequestBody.create(MediaType.parse("text/plain"), mFullName);
            fields.put("fullname", name_body);
        } else {
            fullName = binding.edtFullName.getText().toString();
            name_body = RequestBody.create(MediaType.parse("text/plain"), fullName);
            fields.put("fullname", name_body);
        }


        RequestBody gender_body = null;
        if (this.mGender != null && this.mGender.equals(binding.edtGender.getText().toString())) {
            gender_body = RequestBody.create(MediaType.parse("text/plain"), mGender);
            fields.put("gender", gender_body);
        } else {
            gender = binding.edtGender.getText().toString();
            gender_body = RequestBody.create(MediaType.parse("text/plain"), gender);
            fields.put("gender", gender_body);
        }


        RequestBody dob_body = null;
        if (this.mDob != null && this.mDob.equals(binding.edtDOB.getText().toString())) {
            dob_body = RequestBody.create(MediaType.parse("text/plain"), mDob);
            fields.put("dob", dob_body);
        } else {
            dob = binding.edtDOB.getText().toString();
            dob_body = RequestBody.create(MediaType.parse("text/plain"), dob);
            fields.put("dob", dob_body);
        }


        RequestBody token_body = RequestBody.create(MediaType.parse("text/plain"), token);
        fields.put("token", token_body);

        RequestBody user_id_body = RequestBody.create(MediaType.parse("text/plain"), mUserId);
        fields.put("user_id", user_id_body);

        return fields;
    }

    private void callCheckPhoneApi(final String editedPhone) {
        final MyDialog myDialog=new MyDialog(EditProfileActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<CheckPhoneModel> call = apiInterface.getCheckPhoneResult(editedPhone);

        call.enqueue(new Callback<CheckPhoneModel>() {
            @Override
            public void onResponse(Call<CheckPhoneModel> call, Response<CheckPhoneModel> response) {
                myDialog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        myDialog.hideDialog();
                        //to get all response here,
                        Toast.makeText(EditProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        // SharedPreferenceWriter.getInstance(EditProfileActivity.this).writeStringValue(SharedPreferenceKey.PHONE,editedPhone);

                          openBottomSheetPopUpForOTP();

                          fireBaseAction(editedPhone);

                    } else {
                         Toast.makeText(EditProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                         //phone number already exist
                         callEditProfileApi(binding.mainRl);
                    }
                } else {
                    myDialog.hideDialog();
                    Toast.makeText(EditProfileActivity.this, R.string.service_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckPhoneModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void openBottomSheetPopUpForOTP() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditProfileActivity.this);
        View view2 = EditProfileActivity.this.getLayoutInflater().inflate(R.layout.pop_up_resend_otp, null);
        bottomSheetDialog.setContentView(view2);
        bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

        bottomSheetDialog.dismiss();

        Button submitButtonOtp = view2.findViewById(R.id.btnSubmitResendOtp);

        final EditText edtCode=view2.findViewById(R.id.edtCode);

        submitButtonOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!edtCode.getText().toString().isEmpty()) {
                    MyDialog myDialog = new MyDialog(EditProfileActivity.this);
                    myDialog.showDialog();

                    verifyVerificationCode(edtCode.getText().toString(),myDialog);
                }
                else {
                    Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.please_enter_otp), Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void verifyVerificationCode(String edtedCode, MyDialog myDialog) {

        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, edtedCode);
        //signing the user
        SignInWithPhone(credential,myDialog);
    }

    private void SignInWithPhone(PhoneAuthCredential credential, final MyDialog myDialog) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "Verified OTP", Toast.LENGTH_SHORT).show();
                            callEditProfileApi(binding.mainRl);

                        } else {
                            Toast.makeText(EditProfileActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                            myDialog.hideDialog();
                        }
                    }
                });
    }

    private void fireBaseAction(String editedPhone) {
        StartFireBaseLogin();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                binding.ccpSinUp.getFullNumberWithPlus()+editedPhone,// Phone number to verify
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
                Toast.makeText(EditProfileActivity.this, "Number verification failed!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                mResendToken = forceResendingToken;
                Log.e("OTP Code", s);
                Toast.makeText(EditProfileActivity.this, "Sending  otp...", Toast.LENGTH_SHORT).show();
            }
        };
    }


    //end multipart data


    private void callEditProfileApi(final View view) {




        final MyDialog myDialog=new MyDialog(EditProfileActivity.this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        //start multipart for image
        RequestBody profile_body;
        MultipartBody.Part profilePart;

        if (fileFlyer != null) {
            profile_body = RequestBody.create(MediaType.parse("image"), fileFlyer);
            profilePart = MultipartBody.Part.createFormData("image", fileFlyer.getName(), profile_body);
        } else {
            profile_body = RequestBody.create(MediaType.parse("image"), "");
            profilePart = MultipartBody.Part.createFormData("image", "", profile_body);
        }
        //end multipart image

        //calling multipart data,multipart image
        Call<EditProfileModel> call = apiInterface.getEditProfileResult(setUpMapData(), profilePart);
        call.enqueue(new Callback<EditProfileModel>() {
            @Override
            public void onResponse(Call<EditProfileModel> call, Response<EditProfileModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    EditProfileModel data = response.body();
                    if (data.getStatus().equals("SUCCESS")) {
                        setEditedData(data);//set data
                        startActivity(new Intent(EditProfileActivity.this, MainActivity.class).putExtra("from", "EditProfileFragment"));

                       // openPopUp();

                    } else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(EditProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(EditProfileActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callEditProfileApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(EditProfileActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(EditProfileActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<EditProfileModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });
    }

    private void setEditedData(EditProfileModel data) {
        String name = data.getEditProfileResponse().getFullname();
        //String gender=data.getEditProfileResponse().get
        String email = data.getEditProfileResponse().getEmail();
        String phone = data.getEditProfileResponse().getPhone();
        String image = data.getEditProfileResponse().getImage();
        //String dob = data.getEditProfileResponse().get

        binding.tvName.setText(name);
        binding.edtFullName.setText(name);
        //binding.edtGender.setText(gender);
        binding.edtEmail.setText(email);
        binding.edtPhone.setText(phone);
        //binding.edtDOB.setText(dob);

        if (!image.isEmpty()) {
            Picasso.with(this).load(image).into(binding.ivProfilePic);
        }
    }


    private void openPopUp() {
        PopUpProfileUpdatedBinding binding;
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_profile_updated, null, false);
        dialog.setContentView(binding.getRoot());

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        binding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, MainActivity.class).putExtra("from", "EditProfileFragment"));
            }
        });
        dialog.show();
    }
}
