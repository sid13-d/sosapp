package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

public class Sos extends AppCompatActivity {

    TextView locationText;
    FloatingActionButton addSOS;
    LottieAnimationView sosBtn;
    ArrayList<String> tokens = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
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
                try {
                    sendAlert();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
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

    public void getSosList() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest fetch = new JsonObjectRequest(Request.Method.GET, getString(R.string.url) + "get_sos_list?user_id=6287fdbe90bd466058fb34e8", null, new Response.Listener<JSONObject>() {
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
    }

    public void sendAlert() throws JSONException, IOException {

        Toast.makeText(getApplicationContext(), "hiii", Toast.LENGTH_SHORT).show();

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
        post.setHeader("Content-type", "application/json");
        post.setHeader("Authorization", "key="+getString(R.string.fcm_key));

        JSONObject message = new JSONObject();
        message.put("to", "/topics/6288b825972a8ce499fdd195");
        message.put("priority", "high");



        JSONObject notification = new JSONObject();
        notification.put("title", "SOS");
        notification.put("body", "Emergency!!!!");

        message.put("notification", notification);

        JSONObject data = new JSONObject();
        data.put("key1", "Hello");

        message.put("data", data);

        post.setEntity(new StringEntity(message.toString(), "UTF-8"));
        HttpResponse response = client.execute(post);
        System.out.println(response);
        Log.d("sendAlert", "sendAlert: "+response);
        Log.d("sendAlert", "sendAlert: "+message);
        System.out.println(message);
    }
}