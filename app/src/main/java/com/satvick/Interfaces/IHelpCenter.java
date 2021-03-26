package com.satvick.Interfaces;

import android.content.Context;

public interface IHelpCenter {

    void callus(Context context);

    void mailUs(Context context,String subject);

    void showBottomSheet(Context context,String subject);

    boolean showPermission(Context context);
}
