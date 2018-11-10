package com.example.kritik.regularsim;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import javax.crypto.SecretKey;

public class ModifiedBroadcastReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        String encrypted_bssid = intent.getStringExtra("BSSID");
        String encrypted_ssid = intent.getStringExtra("SSID");
        String key = intent.getStringExtra("SECRET");

        String initVector = "RandomInitVector";

        Log.d("Encrypted", "SSID " + encrypted_ssid);
        Log.d("Encrypted", "BSSID " + encrypted_bssid);
        Log.d("Encrypted", "SECRET " + key);

        RegApplication.setEncrypted_bssid(encrypted_bssid);
        RegApplication.setEncrypted_ssid(encrypted_ssid);
        RegApplication.setEncryption_key(key);

        String ssid = null;
        String bssid = null;
        try
        {
            ssid = Encryption.decrypt(key, initVector, encrypted_ssid);
            bssid = Encryption.decrypt(key, initVector, encrypted_bssid);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        RegApplication.setSsid(ssid);
        RegApplication.setBssid(bssid);

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


    }
}
