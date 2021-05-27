package com.satvick.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivitySettingBinding;
import com.satvick.databinding.PopUpPasswordChangedBinding;
import com.satvick.model.ChangePasswordModel;
import com.satvick.model.SettingNotificationModel;
import com.satvick.retrofit.ApiClient;
import com.satvick.retrofit.ApiInterface;
import com.satvick.retrofit.MyDialog;
import com.satvick.utils.GlobalVariables;
import com.satvick.utils.HelperClass;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {


    ActivitySettingBinding binding;

    Button submitButton;
    EditText edtOldPassword;
    EditText edtNewPassword;
    EditText edtConfirmPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        init();
    }



    private void init() {
        binding.ivBack.setOnClickListener(this);
        binding.llChangePass.setOnClickListener(this);
        binding.toggleButton.setOnClickListener(this);

        if(SharedPreferenceWriter.getInstance(SettingsActivity.this).getBoolean(SharedPreferenceKey.NOTIFICATION_STATUS,false))
        {
            binding.toggleButton.setChecked(true);
        }else
        {
            binding.toggleButton.setChecked(false);
        }


    }


    private boolean validateForm() {
        boolean flag = true;
        if (edtOldPassword.getText().toString().equals("")) {
            Toast.makeText(SettingsActivity.this, R.string.please_enter_old_password, Toast.LENGTH_LONG).show();
            flag = false;
        } else if (edtNewPassword.getText().toString().equals("")) {
            Toast.makeText(SettingsActivity.this, R.string.please_enter_new_password, Toast.LENGTH_LONG).show();
            flag = false;
        } else if (edtConfirmPassword.getText().toString().equals("")) {
            Toast.makeText(SettingsActivity.this, R.string.Please_enter_confirm_password, Toast.LENGTH_LONG).show();
            flag = false;
        }
        return flag;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.llChangePass:

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
                View view1 = this.getLayoutInflater().inflate(R.layout.pop_up_change_password, null);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);

                 submitButton = view1.findViewById(R.id.btnSubmitChangePass);
                 edtOldPassword =view1.findViewById(R.id.edtOldPassword);
                 edtNewPassword =view1.findViewById(R.id.edtNewPassword);
                 edtConfirmPassword =view1.findViewById(R.id.edtConfirmPassword);

                 submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       // openSuccessPopup();

                        if (validateForm()) {
                            if (HelperClass.showInternetAlert(SettingsActivity.this)) {
                                callChangePasswordService(binding.mainRl);
                            }
                        }
                    }
                });

                bottomSheetDialog.show();
                break;


            case R.id.toggleButton:
                callPAVRNotificationsApi(binding.mainRl);
                break;
        }
    }

    private void openSuccessPopup() {
        final PopUpPasswordChangedBinding binding;
        final Dialog dialog=new Dialog(this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_password_changed, null, false);
        dialog.setContentView(binding.getRoot());

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        binding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateForm()) {
                    if (HelperClass.showInternetAlert(SettingsActivity.this)) {
                        callChangePasswordService(v);
                    }
                }

            }
        });
        dialog.show();
    }

    private void callChangePasswordService(final View view) {

        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ChangePasswordModel> call = apiInterface.getChangePasswordResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN),
                SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID),
                edtOldPassword.getText().toString(),
                edtNewPassword.getText().toString());

        call.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equals("SUCCESS")) {
                        Toast.makeText(SettingsActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(SettingsActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callChangePasswordService(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(SettingsActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(SettingsActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                myDialog.hideDialog();
            }
        });

    }


    private void callPAVRNotificationsApi(final View view) {
        final MyDialog myDialog=new MyDialog(this);
        myDialog.showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<SettingNotificationModel> call = apiInterface.getSettingNotificationtResult(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.TOKEN),
                                                                                         SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.USER_ID));

        call.enqueue(new Callback<SettingNotificationModel>() {
            @Override
            public void onResponse(Call<SettingNotificationModel> call, final Response<SettingNotificationModel> response) {

                if (response.isSuccessful()) {
                    myDialog.hideDialog();
                    if (response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS)) {

                        if (response.body().getNotifystatus().getStatus().equalsIgnoreCase("1")) {
                            SharedPreferenceWriter.getInstance(SettingsActivity.this).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS,false);

                        } else if(response.body().getNotifystatus().getStatus().equalsIgnoreCase("0")) {
                            SharedPreferenceWriter.getInstance(SettingsActivity.this).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS,true);
                        }
                    } else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE)) {
                        Toast.makeText(SettingsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {


                    myDialog.hideDialog();
                    final Snackbar mSnackbar = Snackbar.make(view, R.string.service_error, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setActionTextColor(ContextCompat.getColor(SettingsActivity.this,R.color.colorWhite));
                    mSnackbar.setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            callPAVRNotificationsApi(view);
                            Snackbar snackbar=Snackbar.make(view,"Please wait!",Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackground(ContextCompat.getDrawable(SettingsActivity.this,R.drawable.drawable_gradient_line));
                            snackbar.show();
                        }
                    });
                    mSnackbar.getView().setBackground(ContextCompat.getDrawable(SettingsActivity.this,R.drawable.drawable_gradient_line));
                    mSnackbar.show();
                }

            }

            @Override
            public void onFailure(Call<SettingNotificationModel> call, Throwable t) {
                Log.e("error",t.getMessage());
                myDialog.hideDialog();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
