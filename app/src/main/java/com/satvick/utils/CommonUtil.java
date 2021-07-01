package com.satvick.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.satvick.R;
import com.satvick.activities.EditProfileActivity;
import com.satvick.activities.MainActivity;
import com.satvick.activities.MyOrderActivity;
import com.satvick.activities.OrderManageActivity;
import com.satvick.activities.ProductDetailActivity;
import com.satvick.activities.ProductListActivity;
import com.satvick.activities.SignUpActivity;
import com.satvick.activities.SplashActivity;
import com.satvick.database.SharedPreferenceKey;
import com.satvick.database.SharedPreferenceWriter;
import com.satvick.databinding.ActivityEditProfleBinding;
import com.satvick.databinding.PopUpCancellationRequestBinding;
import com.satvick.model.LoginModel;
import com.satvick.model.ProductDetails;
import com.satvick.model.SignUpModel;
import com.satvick.model.SocialLoginModel;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class CommonUtil {


    public static final String REGX  = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*()\\-     _=+{}|?>.<,:;~`’]{6,}$";

    public static void setUpSnackbarMessage(View view, String message, Activity context) {
        if(message.equalsIgnoreCase("already saved")){
            message="Address Already Saved";
        }
        Snackbar mSnackbar = Snackbar.make(context.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        mSnackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        mSnackbar.getView().setBackground(ContextCompat.getDrawable(context,R.drawable.drawable_gradient_line));
        mSnackbar.show();

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static List<ProductDetails> setCartData(Context context){
        List<ProductDetails> productDetails=new ArrayList<>();
        String product[]=SharedPreferenceWriter.getInstance(context).getString(GlobalVariables.product_id).split(",");
        String sizess[]=SharedPreferenceWriter.getInstance(context).getString(GlobalVariables.size).split(",");
        String quantiy[]=SharedPreferenceWriter.getInstance(context).getString(GlobalVariables.quantity).split(",");
        Log.e("product",""+product.length);
        for(int i=0;i<product.length;i++) {
            if (!product[i].equalsIgnoreCase("")){
                try {
                    productDetails.add(new ProductDetails(product[i], "", sizess[i], quantiy[i]));
                } catch (Exception e) {
                    productDetails.add(new ProductDetails(product[i], "", "", quantiy[i]));
                }
            }
        }

        return productDetails;
    }


    public static void CommonMessagePopUp(final Context context, String message)
    {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_mesage_popup2);
        LottieAnimationView lottieAnimationView=dialog.findViewById(R.id.lottieAnimationView);
        ImageView closeiv=dialog.findViewById(R.id.closeiv);
        TextView messagetxt=dialog.findViewById(R.id.messagetxt);
        messagetxt.setText(message);
        lottieAnimationView.setAnimation("warning.json");
        Button okbtn=dialog.findViewById(R.id.okbtn);

        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity)context).finish();

            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity)context).finish();


            }
        });
        dialog.show();
    }

    public static void orderStatusPopUp(Context context, String message) {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        PopUpCancellationRequestBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pop_up_cancellation_request, null, false);
        dialog.setContentView(binding.getRoot());
        binding.tvCancellationRequest.setText(message);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.startActivity(new Intent(context, MyOrderActivity.class));
            }
        });
        dialog.show();
    }

    public static void commonMessagePopup(Context context,String message, String status) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_mesage_popup);
        ImageView closeiv = dialog.findViewById(R.id.closeiv);
        TextView messagetxt = dialog.findViewById(R.id.messagetxt);
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottieAnimationView);
        if(message.contains("Please select any ")){
            if (message.equalsIgnoreCase("Please select any "+message.split("Please select any ")[1])) {
                lottieAnimationView.setAnimation("warning.json");
                messagetxt.setText(message);

            }
        } else if (message.equalsIgnoreCase(context.getString(R.string.please_select_color))) {
            lottieAnimationView.setAnimation("warning.json");
            messagetxt.setText(message);

        } else if (message.equalsIgnoreCase(context.getString(R.string.added_to_cart))) {
            lottieAnimationView.setAnimation("done.json");
            messagetxt.setText(message);
        } else if (message.equalsIgnoreCase(context.getString(R.string.added_to_cart_already))) {
            lottieAnimationView.setAnimation("warning.json");
            messagetxt.setText(message);
        } else if (message.equalsIgnoreCase(context.getString(R.string.out_of_stock))) {
            lottieAnimationView.setAnimation("warning.json");
            messagetxt.setText(message);
        } else {
            if (status.equalsIgnoreCase(GlobalVariables.SUCCESS)) {
                lottieAnimationView.setAnimation("done.json");
                messagetxt.setText(message);
            } else if (status.equalsIgnoreCase(GlobalVariables.FAILURE)) {
                lottieAnimationView.setAnimation("error.json");
                messagetxt.setText(message);
            }

        }

        Button okbtn = dialog.findViewById(R.id.okbtn);

        closeiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static Boolean isUserLogin(Context context) {
       return  (SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("false") || SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.CURRENT_LOGIN).equalsIgnoreCase("")); }

    public static void saveData(Context context,Response<SocialLoginModel> response) {
        SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.BATCH_COUNT, "");
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.FULL_NAME, response.body().getSociallogin().getName());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.EMAIL, (String) response.body().getSociallogin().getEmail());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.IMAGE, response.body().getSociallogin().getImage());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.USER_ID, "" + response.body().getSociallogin().getId());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.TOKEN, response.body().getSociallogin().getToken());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.CRTEATED_AT, response.body().getSociallogin().getCreatedAt());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.UPDATED_AT, response.body().getSociallogin().getUpdatedAt());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
        SharedPreferenceWriter.getInstance(context).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS, true);
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.product_id,"");
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.color_name,"");
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.quantity,"");
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.size,"");

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    public static void saveLogin(Context context,Response<LoginModel> response) {
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.FULL_NAME, response.body().getLoginResponse().getName());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.CONTACT_NUMBER, response.body().getLoginResponse().getPhone());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.EMAIL, response.body().getLoginResponse().getEmail());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.USER_ID, response.body().getLoginResponse().getId());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.TOKEN, response.body().getLoginResponse().getToken());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
        SharedPreferenceWriter.getInstance(context).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS, true);
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.product_id, "");
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.quantity, "");
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.size, "");
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.color_name, "");


        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    public static void setAdapter(Context context, String [] list, Spinner spinner, View textView) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,list){
            @Override
            public boolean isEnabled(int position) {
                return position!=0;
            }

            @Override
            public View getDropDownView(int position, @Nullable  View convertView, @NonNull  ViewGroup parent) {
                View view=super.getDropDownView(position, convertView, parent);
                ((TextView)view).setTextSize(15);
                if(position==0) ((TextView)view).setTextColor(context.getResources().getColor(R.color.quantum_grey));
                else ((TextView)view).setTextColor(context.getResources().getColor(R.color.black));
                view.setPadding(view.getPaddingLeft(),18,view.getPaddingRight(),18);
                return view;
            }
        };


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) if(textView instanceof TextInputEditText) ((TextInputEditText)textView).setText(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);
    }

    public static void startNewActivity(Context context, Class className){
        Intent intent=new Intent(context,className);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);


    }

    public static void saveSignupData(Context context, Response<SignUpModel> response) {

        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.FULL_NAME, response.body().getSignup().getName());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.CONTACT_NUMBER, response.body().getSignup().getPhone());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.EMAIL, response.body().getSignup().getEmail());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.USER_ID, response.body().getSignup().getUser_id());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.TOKEN, response.body().getSignup().getToken());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.COUNTRY, response.body().getSignup().getCountry());
        SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.CURRENT_LOGIN, "true");
        SharedPreferenceWriter.getInstance(context).writeBooleanValue(SharedPreferenceKey.NOTIFICATION_STATUS, true);
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.product_id, "");
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.quantity, "");
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.size, "");
        SharedPreferenceWriter.getInstance(context).writeStringValue(GlobalVariables.color_name, "");

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);

    }

    public static void getDeviceToken(Context context) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>(){
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    if (task.getResult() == null) getDeviceToken(context);
                    else SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, task.getResult());
                }
            }
        });
    }

    public static String  getDeepLinkProductId(String url) {
       return ""+(url.substring(url.lastIndexOf("/") + 1));
    }


    public Intent getPickIntent(Context context,Uri cameraOutputUri) {
        final List<Intent> intents = new ArrayList();
        intents.add(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        setCameraIntents(context,intents, cameraOutputUri);
        if (intents.isEmpty()) return null;
        Intent result = Intent.createChooser(intents.remove(0), null);
        if (!intents.isEmpty()) result.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[] {}));
        return result;
    }

    public  void setCameraIntents(Context context,List<Intent> cameraIntents, Uri output) {
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
            intent.putExtra("uri",output);
            cameraIntents.add(intent);
        }
    }
}
