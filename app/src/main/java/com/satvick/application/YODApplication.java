package com.satvick.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.satvick.model.ProductDetails;
import com.satvick.utils.CustomFontFamily;

import java.util.ArrayList;


public class YODApplication extends Application {
    private static Context context;
    static YODApplication instance;
    private boolean activityVisible;
    CustomFontFamily customFontFamily;
    private Activity mCurrentActivity = null;
    public ArrayList<ProductDetails> newsList;
    public static Context getContext() {
        return context;
    }
    public static YODApplication getInstance() {
        YODApplication yODApplication = instance;
        if (yODApplication != null)
            return yODApplication;
        yODApplication = new YODApplication();
        instance = yODApplication;
        return yODApplication;
    }

    public Activity getCurrentActivity() {
        return this.mCurrentActivity;
    }

    public ArrayList<ProductDetails> getHugeData() {
        return this.newsList;
    }

    public boolean isVisible() {
        return this.activityVisible;
    }

    public void onCreate() {
        super.onCreate();
        instance = new YODApplication();
        context = (Context)this;
        this.customFontFamily = CustomFontFamily.getInstance();
        this.customFontFamily.addFont("lato_regular", "D-DIN.otf");
        this.customFontFamily.addFont("lato_bold", "D-DIN-Bold.otf");
    }

    public ArrayList<ProductDetails> removeData(int paramInt) {
        newsList.remove(paramInt);
        return this.newsList;
    }

    public void setCurrentActivity(Activity paramActivity) {
        this.mCurrentActivity = paramActivity;
    }

    public void setHugeData(ArrayList<ProductDetails> paramArrayList) {
        this.newsList = paramArrayList;
    }

    public void setVisible(Boolean paramBoolean) {
        this.activityVisible = paramBoolean;
    }

}
