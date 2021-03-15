package com.example.androidproject.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class OreoNotification extends ContextWrapper {

    private static final String ID="ID";
    private static final String NAME="FirebaseAPP";

    private NotificationManager manager;

    public OreoNotification(Context base) {
        super(base);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            creatNotifChannel();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void creatNotifChannel(){

        NotificationChannel channel=new NotificationChannel( ID, NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if(manager==null){
            manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getOreoNotifications(String title, String body, PendingIntent pIntent, Uri soundUri, String icon){

        return new Notification.Builder(getApplicationContext(), ID).setContentIntent(pIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setSmallIcon(Integer.parseInt(icon));
    }
}
