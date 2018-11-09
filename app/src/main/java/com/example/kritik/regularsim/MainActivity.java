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

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
{

    String bssid = null;
    String ssid = null;
    TextView tv_bssid = null;
    TextView tv_ssid = null;
    TextView tv_location = null;
    GeoLocationData geoLocationData = null;


    String api_response = null;

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
        tv_bssid = (TextView)findViewById(R.id.tv_bssid);
        tv_ssid = (TextView)findViewById(R.id.tv_ssid);
        tv_location = (TextView)findViewById(R.id.tv_location);

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
            Log.d("HTML", e.toString());
        }
    }

    OkHttpClient client = new OkHttpClient();

    String run(String url) throws IOException
    {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response  response = client.newCall(request).execute();
        return response.body().string();
    }

    private void GetWebpage(String bssid) throws IOException
    {
        bssid = "18:64:72:23:e1:32";
        String baseUrl = "https://api.mylnikov.org/geolocation/wifi?v=1.1&data=closed&bssid=" + bssid;

        Request request = new Request.Builder()
                .url(baseUrl)
                .build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                api_response = response.body().string();
                Log.d("html", api_response);
                final GeoLocationResponse geoLocationResponse = new Gson().fromJson(api_response, GeoLocationResponse.class);
                final GeoLocationData geoLocationData = geoLocationResponse.getData();

                MainActivity.this.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (geoLocationData == null)
                            tv_location.setVisibility(View.INVISIBLE);
                        else
                        {
//                            geoLocationData = new Gson().fromJson(geoData, GeoLocationData.class);
                            tv_location.setText("Latitude " + geoLocationData.getLatitude() + "\n" + "Longitude " + geoLocationData.getLongitude());
                        }

                    }
                });

            }
        });
    }


    private BroadcastReceiver infoReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

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
