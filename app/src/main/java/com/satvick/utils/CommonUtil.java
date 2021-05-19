package com.satvick.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.satvick.R;

public class CommonUtil {


    public static void setUpSnackbarMessage(View view,String message, Activity context) {
        Snackbar mSnackbar = Snackbar.make(context.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        mSnackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        mSnackbar.getView().setBackground(ContextCompat.getDrawable(context,R.drawable.drawable_gradient_line));
        mSnackbar.show();

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

}
