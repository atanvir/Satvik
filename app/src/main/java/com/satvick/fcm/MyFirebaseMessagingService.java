package com.satvick.fcm;


import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static ShowDot listen;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(listen!=null) {listen.show();}
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "FCMbody " + remoteMessage.getData());
        Log.e("Title",remoteMessage.getNotification().getTitle());
        Log.e("Body",remoteMessage.getNotification().getBody());

        iFcm  fcm=new iFcmImp();
        Intent intent= fcm.setIntent(this,remoteMessage.getData());
        fcm.sendNotification(this,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),intent);

    }

    public interface ShowDot{
        void show();
    }

    public static void setListner(ShowDot listner)
    {
        listen=listner;
    }


}


