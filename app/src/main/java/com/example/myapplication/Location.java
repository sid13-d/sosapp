package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Location extends AppCompatActivity {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView textView1, textView2, textView3, textView4, textView5;
    Button button,button1;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        button = findViewById(R.id.button);
        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        textView3 = findViewById(R.id.textview3);
        textView4 = findViewById(R.id.textview4);
        textView5 = findViewById(R.id.textview5);
        button1 = findViewById(R.id.button1);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Location.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(Location.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String geoUri = "http://maps.google.com/maps?q=loc:" + textView1.getText().toString() + "," + textView2.getText().toString();
                String geoUri = "http://maps.google.com/maps?q=loc:"+textView5.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
            @Override
            public void onComplete(@NonNull Task<android.location.Location> task) {
                android.location.Location location = task.getResult();
                if (location != null) {

                    Geocoder geocoder = new Geocoder(Location.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );

                        textView1.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude :<b><br></font>"+addressList.get(0).getLatitude()));

                        textView2.setText(Html.fromHtml("<font color='#6200EE'><b>Laongitude :<b><br></font>"+addressList.get(0).getLongitude()));

                        textView3.setText(Html.fromHtml("<font color='#6200EE'><b>Country :<b><br></font>"+addressList.get(0).getCountryName()));

                        textView4.setText(Html.fromHtml("<font color='#6200EE'><b>Locality :<b><br></font>"+addressList.get(0).getLocality()));

                        textView5.setText(Html.fromHtml("<font color='#6200EE'><b>Address :<b><br></font>"+addressList.get(0).getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}