package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.entity.StringEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClientBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Sos extends AppCompatActivity {

    TextView locationText;
    FloatingActionButton addSOS;
    LottieAnimationView sosBtn;
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String ID = "id_key";
    public static final String NAME = "user_name";
    public static final String PHONE = "user_phone";

    SharedPreferences sharedpreferences;
    String id,name,phone;
    ArrayList<String> tokens = new ArrayList<String>();
    TextView currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addresses;
    ConstraintLayout cardViewConstraintLayout;
    Location location;
    String language;
    Locale myLocale;
    String currentLanguage = "en", currentLang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        Intent intent = getIntent();
        language = intent.getStringExtra("lan");
        Log.d("checklan", "onCreate: "+language);
        setLocale(language);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(ID, null);
        name = sharedpreferences.getString(NAME, null);
        Log.d("namecheck", "onCreate: "+name);

        currentLocation = (TextView) findViewById(R.id.locationText);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Sos.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(Sos.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        locationText = (TextView) findViewById(R.id.locationText);
        locationText.setSelected(true);

        sosBtn = (LottieAnimationView) findViewById(R.id.sosLottie);
        sosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();
                getSosList();
                if (getSosList()){
                    try {
                        sendAlert();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        addSOS = (FloatingActionButton) findViewById(R.id.floatingbtn);
        addSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sos.this, AddPerson.class);
                startActivity(intent);
            }
        });

    }
    public void setLocale( String localeName) {
        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, Sos.class);
        refresh.putExtra(currentLang, localeName);
        startActivity(refresh);

    }
    private void getLocation() {

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
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(Sos.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        currentLocation.setText(addresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        cardViewConstraintLayout = (ConstraintLayout) findViewById(R.id.CardViewConstraintLayout);
        cardViewConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geoUri = "http://maps.google.com/maps?q=loc:"+currentLocation.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });
    }

    public boolean getSosList() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest fetch = new JsonObjectRequest(Request.Method.GET, getString(R.string.url) + "get_sos_list?user_id="+id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                tokens.clear();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
//                    user_id = new String[jsonArray.length()];
                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        tokens.add(jsonObject.getString("_id"));
                    }

                    Log.d("arrayCheck", "onResponse: "+Arrays.toString(tokens.toArray()));
//                    sendAlert();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("response", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(fetch);
        return true;
    }

    public void sendAlert() throws JSONException, IOException {

        if (tokens.toArray().length == 0){
            Toast.makeText(getApplicationContext(), "You don't have anyone added to your list!!", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < tokens.toArray().length; i++) {
            Toast.makeText(getApplicationContext(), "Alert sent!!!", Toast.LENGTH_SHORT).show();

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", "key="+getString(R.string.fcm_key));

            JSONObject message = new JSONObject();
            message.put("to", "/topics/"+tokens.get(i));
            message.put("priority", "high");


            JSONObject notification = new JSONObject();
            notification.put("title", "SOS");
            notification.put("body", "Hurry "+name+" needs your help at "+addresses.get(0).getAddressLine(0));

            message.put("notification", notification);

            JSONObject data = new JSONObject();
            data.put("location", addresses.get(0).getAddressLine(0));
            data.put("lat", location.getLatitude());
            data.put("long", location.getLongitude());
            Log.d("location", "sendAlert: "+location.getLatitude());
            Log.d("location", "sendAlert: "+location.getLongitude());

            message.put("data", data);

            post.setEntity(new StringEntity(message.toString(), "UTF-8"));
            HttpResponse response = client.execute(post);
            System.out.println(response);
            Log.d("sendAlert", "sendAlert: "+response);
            Log.d("sendAlert", "sendAlert: "+message);
            System.out.println(message);
        }
    }
}