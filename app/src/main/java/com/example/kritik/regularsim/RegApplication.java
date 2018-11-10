package com.example.kritik.regularsim;

import android.app.Application;
import android.content.Intent;

public class RegApplication extends Application
{
    private static String bssid = null;
    private static String ssid = null;


    @Override
    public void onCreate()
    {
        super.onCreate();

        startService(new Intent(this, ReceiverService.class));
    }

    public static String getSsid()
    {
        return ssid;
    }

    public static void setSsid(String ssid)
    {
        RegApplication.ssid = ssid;
    }

    public static String getBssid()
    {
        return bssid;
    }

    public static void setBssid(String bssid)
    {
        RegApplication.bssid = bssid;
    }
}
