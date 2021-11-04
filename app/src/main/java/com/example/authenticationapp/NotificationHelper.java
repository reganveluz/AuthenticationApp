package com.example.authenticationapp;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.authenticationapp.DriverInterface.QRReceipt;

public class NotificationHelper {

    public static void displayNotification(Context context, String title, String body){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, QRReceipt.CHANNEL_ID)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
        mNotificationMgr.notify(1, mBuilder.build());
    }

}
