package com.example.shreyas.hola;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashSet;
import java.util.Set;

public class NotificationChannelHelper {

    private static NotificationManagerCompat notificationManager;
    private static Set<String> channels;

//    private static final String CHANNEL_NAME="Shreyas";
    private static final String CHANNEL_DESC = "Notification example";
//    public static final String CHANNEL_ID="shreyas_dobhal";


    public static void sendOnChannel(Context context, String channelId, String title, String text) {

        if (channels==null) {
            Log.i("NOTIFY","New set created");
            channels = new HashSet<>();
        }

        if (!channels.contains(channelId)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.i("NOTIFY","Creating channel "+channelId);
                String channelName = channelId;
                NotificationChannel channel = new NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(CHANNEL_DESC);
                NotificationManager manager = context.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }
        }

        if (notificationManager==null)
            notificationManager = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.drawable.face)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1,notification);
    }
}
