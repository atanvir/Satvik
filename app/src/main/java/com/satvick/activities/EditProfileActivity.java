package com.satvick.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import android.os.Environment;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.satvick.BuildConfig;
import com.satvick.model.UpdateProfileModel;
import com.satvick.utils.AddRequestBody;
import com.satvick.utils.CommonUtil;
import com.satvick.utils.HelperClass;
import com.squareup.picasso.Picasso;
import com.satvick.R;
import com.satvick.databinding.ActivityEditProfleBinding;
import com.satvick.model.EditProfileModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.FilePath;
import com.satvick.utils.GlobalVariables;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private ActivityEditProfleBinding binding;
    private Date date= Calendar.getInstance().getTime();
    private boolean isValidAge;
    private String path=null;
    private MyDialog dailog;
    private ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
    private final int CAMERA_PERMISSION_REQUEST=11,CAMERA_REQUEST=12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditProfleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initCtrl();
    }

    private void init() {
        isValidAge=true;
        dailog=new MyDialog(this);
        binding.tvName.setText(getIntent().getStringExtra("name"));
        binding.edtFullName.setText(getIntent().getStringExtra("name"));
        binding.edtPhone.setText(getIntent().getStringExtra("phone"));
        binding.edtEmail.setText(getIntent().getStringExtra("email"));
        binding.edtDOB.setText(getIntent().getStringExtra("dob"));
        loadProfilePic(getIntent().getStringExtra("image"));
        CommonUtil.setAdapter(this,new String[]{"Please Select Gender","Male", "Female","Others"},binding.spnGender,binding.edtGender);
    }


    private void initCtrl() {
        binding.edtGender.setOnClickListener(this);
        binding.edtDOB.setOnClickListener(this);
        binding.tvSaveDetails.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.ivProfilePic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.edtGender: binding.spnGender.performClick(); break;

            case R.id.edtDOB:
            DatePickerDialog dialog=new DatePickerDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT,this,Integer.parseInt(new SimpleDateFormat("yyyy").format(date)), Integer.parseInt(new SimpleDateFormat("MM").format(date))-1,Integer.parseInt(new SimpleDateFormat("dd").format(date)));
            dialog.getDatePicker().setMaxDate(date.getTime());
            dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Save", dialog);
            dialog.show();
            break;

            case R.id.ivProfilePic:  if(checkPermission()) cameraIntent(); break;
            case R.id.tvSaveDetails: if(checkValidation()) editProfileApi(); break;
            case R.id.ivBack: onBackPressed(); break;
        }
    }

    private void loadProfilePic(Object object) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Glide.with(this).load(object).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                binding.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                binding.progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(binding.ivProfilePic);
    }


    private boolean checkValidation() {
        boolean ret=true;

        if(binding.edtGender.getText().toString().isEmpty())
        {
            ret=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please select gender",this);

        }else if(binding.edtGender.getText().toString().equalsIgnoreCase("Please Select Gender"))
        {
            ret=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please select gender",this);

        }else if(binding.edtFullName.getText().toString().isEmpty())
        {
            ret=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter full name",this);
        }
        else if(binding.edtEmail.getText().toString().isEmpty())
        {
            ret=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter email id",this);

        }
        else if(binding.edtPhone.getText().toString().isEmpty())
        {
            ret=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter phone number",this);
        }
        else if(binding.edtDOB.getText().toString().isEmpty())
        {
            ret=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter Date Of Birth",this);
        }
        else if(!isValidAge)
        {
            ret=false;
            CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please enter valid age more than 10 years ",this);
        }

        return  ret;
    }

    private void editProfileApi() {
        dailog.showDialog();
        Call<EditProfileModel> call = apiInterface.getEditProfileResult(geData(), getImage());
        call.enqueue(new Callback<EditProfileModel>() {
            @Override
            public void onResponse(Call<EditProfileModel> call, Response<EditProfileModel> response) {
                dailog.hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(GlobalVariables.SUCCESS)) startActivity(new Intent(EditProfileActivity.this, MainActivity.class).putExtra("edit", "EditProfileFragment"));
                    else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) CommonUtil.setUpSnackbarMessage(binding.getRoot(),response.body().getMessage(),EditProfileActivity.this);
                } else CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Internal Server Error",EditProfileActivity.this);
            }

            @Override
            public void onFailure(Call<EditProfileModel> call, Throwable t) {
                dailog.hideDialog();
                CommonUtil.setUpSnackbarMessage(binding.getRoot(),t.getMessage(),EditProfileActivity.this);
            }
        });
    }

    private boolean checkPermission() {
        boolean ret=true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ret = false;
                this.requestPermissions(new String[]{Manifest.permission.CAMERA,
                                                     Manifest.permission.READ_EXTERNAL_STORAGE,
                                                     Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST);
            }
        }
        return  ret;
    }


    private Map<String, RequestBody> geData() {
      return new AddRequestBody(new UpdateProfileModel(HelperClass.getCacheData(this).second,
                                                       binding.edtFullName.getText().toString(),
                                                       binding.edtEmail.getText().toString(),
                                                       binding.edtPhone.getText().toString(),
                                                       binding.edtDOB.getText().toString(),
                                                       binding.edtGender.getText().toString(),
                                                       HelperClass.getCacheData(this).first)).getBody();
    }
    private MultipartBody.Part getImage() {
        MultipartBody.Part part =null;
        RequestBody body= null;
        if(path!=null)
        {
            File file =new File(path);
            try {
                body = RequestBody.create(MediaType.parse("" + "image/*"),new Compressor(this).compressToFile(file));
                part=MultipartBody.Part.createFormData("image", file.getName(),body);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return part;
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        Date date= null;
        try {
            isValidAge= Calendar.getInstance().get(Calendar.YEAR) - year > 9;
            date = simpleDateFormat.parse(dayOfMonth+"/"+(month+1)+"/"+year);
            binding.edtDOB.setText(simpleDateFormat.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void cameraIntent() {
        String capture_dir= Environment.getExternalStorageDirectory() + "/Solahe/Images/";
        File file = new File(capture_dir);
        if (!file.exists()) file.mkdirs();
        path = capture_dir + System.currentTimeMillis() + ".jpg";
        Uri imageFileUri = FileProvider.getUriForFile(Objects.requireNonNull(this.getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", new File(path));
        Intent intent = new CommonUtil().getPickIntent(this,imageFileUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case CAMERA_PERMISSION_REQUEST:
            boolean isPermissionDenied=false;
            for (int grantResult : grantResults) { if (grantResult != PackageManager.PERMISSION_GRANTED) { isPermissionDenied = true; break; } }
            if(isPermissionDenied) CommonUtil.setUpSnackbarMessage(binding.getRoot(),"Please allow permission to use camera",this);
            else cameraIntent();
            break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                if (data != null) path = FilePath.getPath(this, Uri.parse(data.getDataString()));
                loadProfilePic(path);
                break;
            }
        }
    }
}
