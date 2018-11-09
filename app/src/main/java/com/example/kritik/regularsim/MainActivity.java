package com.example.kritik.regularsim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{

    String bssid = null;
    String ssid = null;

    private IntentFilter defaultFilter = new IntentFilter("DEFAULT_WIFI_BROADCAST");
    private IntentFilter modifiedFilter = new IntentFilter("MODIFIED_WIFI_BROADCAST");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.registerReceiver(infoReceiver, defaultFilter);
        this.registerReceiver(infoReceiver, modifiedFilter);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.unregisterReceiver(infoReceiver);
    }

    public void GetLocation(View view)
    {
        // get location here and set textview
        try
        {
            GetWebpage(bssid);
        }
        catch (Exception e)
        {
            Log.d("HTML", e.getMessage());
        }
    }

    private void GetWebpage(String bssid) throws IOException
    {
        bssid = "18:64:72:23:e1:32";
        String baseUrl = "https://api.mylnikov.org/geolocation/wifi?v=1.1&data=closed&bssid=" + bssid;
        String html = null;

        URL url = new URL(baseUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            html = in.toString();
            Log.d("HTML", html);
        } finally {
            urlConnection.disconnect();
        }

    }


    private BroadcastReceiver infoReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            TextView tv_bssid = (TextView)findViewById(R.id.tv_bssid);
            TextView tv_ssid = (TextView)findViewById(R.id.tv_ssid);

            WifiInfo stuff = (WifiInfo) intent.getExtras().get("wifiInfo");
            if (intent.getAction().equals("DEFAULT_WIFI_BROADCAST"))
            {
//                WifiInfo stuff = ((WifiInfo) intent.getExtras());
                bssid = stuff.getBSSID();
                ssid = stuff.getSSID();
            }
            else if (intent.getAction().equals("MODIFIED_WIFI_BROADCAST"))
            {

            }

            tv_bssid.setText(bssid);
            tv_ssid.setText(ssid);
        }
    };
}
