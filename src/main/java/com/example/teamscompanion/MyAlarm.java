package com.example.teamscompanion;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.teamscompanion.App.CHANNEL_1_ID;


public class MyAlarm extends BroadcastReceiver {

    // onReceive called alarm timeout

    NotificationManagerCompat notificationManagerCompat;
    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
    //    Log.i("Info", "Alarm!");

        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        boolean deadLock = sharedPreferences.getBoolean("deadLock", false);

        if(deadLock) {


            wifiManager.setWifiEnabled(true);

            sharedPreferences.edit().putBoolean("deadLock", false).apply();


        } else {


            AudioManager mobilemode = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

            mobilemode.setStreamVolume(AudioManager.STREAM_MUSIC, mobilemode.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2,0);

            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.beep);
            mediaPlayer.start();


            sharedPreferences.edit().putBoolean("isFinished", true).apply();

            try {
//                Log.i("MyAlarm", "wifi : OFF");
                wifiManager.setWifiEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Intent myIntent = new Intent(context, MainActivity.class);

            // add new flags to just clear all the top stack intents

            myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    myIntent,
                    0);

            // create notification
            notificationManagerCompat = NotificationManagerCompat.from(context);

            Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_baseline_people_alt_24)
                    .setContentTitle("Teams Meeting left")
                    .setContentText("Tap for more bunks")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker("Notification!")
                    .build();
            notificationManagerCompat.notify(1, notification);

            sharedPreferences.edit().putBoolean("deadLock", true).apply();

        }

    }
}

