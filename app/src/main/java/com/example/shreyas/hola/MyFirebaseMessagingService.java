package com.example.shreyas.hola;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("NOTIFY","Notification received");
        if (remoteMessage!=null && remoteMessage.getNotification()!=null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

//            remoteMessage.getNotification().

            NotificationHelper.displayNotification(getApplicationContext(),title,body);

//            String channelId = (new String[]{"Shreyas","Shreya"})[(int)Math.random()*2];
//            Log.i("NOTIFY","Channel Id : "+channelId);
//            NotificationChannelHelper.sendOnChannel(getApplicationContext(),"",title,body);
        }
    }
}
