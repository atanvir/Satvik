package com.satvick.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.satvick.R;
import com.satvick.activities.MyOrderActivity;
import com.satvick.activities.ProductListActivity;
import com.satvick.utils.GlobalVariables;

import java.util.Map;
import java.util.Random;

public class iFcmImp implements iFcm {

    @Override
    public Intent setIntent(Context context, Map<String,String> allData){
        String type= allData.get(GlobalVariables.fcm_type);
        Intent intent=null;

        if(type.equalsIgnoreCase(GlobalVariables.fcm_type_promo))
        {
            intent=new Intent(context, ProductListActivity.class);
            intent.putExtra(GlobalVariables.section_name,type);
            intent.putExtra(GlobalVariables.subsubcatid,allData.get(GlobalVariables.fcm_id));
        }
        else
        {
            intent=new Intent(context, MyOrderActivity.class);
        }

        intent.putExtra(GlobalVariables.from,MyFirebaseMessagingService.class.getSimpleName());

        return intent;


}






    @Override
    public void sendNotification(Context context,String title, String body, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int notificationId = 1;

        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setVibrate(new long[] {1000,1000,1000});


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(new Random().nextInt(), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());

    }



}
