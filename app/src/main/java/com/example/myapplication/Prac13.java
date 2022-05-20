package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Prac13 extends AppCompatActivity {
Button on;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prac13);

        on = findViewById(R.id.on);
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

// ARE WE CONNECTED TO THE NET
                if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                        connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
                    // ...
                    TextView text = findViewById(R.id.tv);
                }
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(false);
            }
        });
    }
}