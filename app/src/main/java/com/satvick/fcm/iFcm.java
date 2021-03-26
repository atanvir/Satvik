package com.satvick.fcm;

import android.content.Context;
import android.content.Intent;

import java.util.Map;

public interface iFcm {
    Intent setIntent(Context context, Map<String,String> allData);
    void sendNotification(Context context, String title, String body, Intent intent);
}
