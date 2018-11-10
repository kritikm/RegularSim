package com.example.kritik.regularsim;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class DefaultBroadcastReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("Service", "received broadcast");
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        if (intent.getAction().equals("DEFAULT_WIFI_BROADCAST"))
//        {
        String ssid = intent.getStringExtra("SSID");
        String bssid = intent.getStringExtra("BSSID");


//        Intent wifiDataIntent = new Intent(context, MainActivity.class);
//        wifiDataIntent.putExtra("SSID", ssid);
//        wifiDataIntent.putExtra("BSSID", bssid);

        RegApplication.setBssid(bssid);
        RegApplication.setSsid(ssid);

        PendingIntent launchIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.small_wifi)
                .setContentTitle("Default Broadcast Received")
                .setContentText("SSID: " + ssid + ", BSSID: " + bssid);
        nBuilder.setContentIntent(launchIntent);
        nBuilder.setDefaults(Notification.DEFAULT_SOUND);
        nBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, nBuilder.build());


//        }
    }
}
