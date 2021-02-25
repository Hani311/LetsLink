package com.example.androidproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotifFirebasMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented=remoteMessage.getData().get("sented");
        FirebaseUser fBU = FirebaseAuth.getInstance().getCurrentUser();

        if(fBU!=null && sented.equals(fBU.getUid())){
            sendNotifications(remoteMessage);
        }

    }

    private void sendNotifications(RemoteMessage remoteMessage) {

        String userID = remoteMessage.getData().get("userID");
        String notifIcon = remoteMessage.getData().get("notifIcon");
        String notifBody = remoteMessage.getData().get("notifBody");
        String notifTitle = remoteMessage.getData().get("notifTitle");

        RemoteMessage.Notification notif = remoteMessage.getNotification();
        int hexed = Integer.parseInt(userID.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle notifBUndle = new Bundle();
        notifBUndle.putString("userid", userID);
        intent.putExtras(notifBUndle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, hexed, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(notifIcon))
                .setContentTitle(notifTitle)
                .setContentText(notifBody)
                .setAutoCancel(true)
                .setSound(notifSound)
                .setContentIntent(pendingIntent);

        NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int a=0;

        if(hexed>0){
            a=hexed;
        }

        manager.notify(a, builder.build());
    }

}
