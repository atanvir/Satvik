package com.satvick.retrofit;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.satvick.R;

/**
 * Created by mobua06 on 16/6/17.
 */

public class MyDialog {


    private static Context context1;
    private static Dialog dialog;


    public MyDialog(Context context)
    {
        context1=context;
    }


    public void showDialog() {
        try {
            dialog=new Dialog(context1,android.R.style.Theme_Black);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.loading_animation);
            dialog.setCancelable(false);
            dialog.show();
        }
        catch (Exception e) {
        }
    }


    public void hideDialog() {
        try {
            if(dialog.isShowing())
            {
                dialog.dismiss();
                dialog=null;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
