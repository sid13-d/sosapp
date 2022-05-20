package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class GetStarted extends AppCompatActivity {
EditText name, number;
Button getStarted;
private String token="";
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String ID = "id_key";

    SharedPreferences sharedpreferences;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started);
        name = findViewById(R.id.name);
        number = findViewById(R.id.phone);
        getStarted = findViewById(R.id.submit);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = sharedpreferences.getString(ID, null);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length() !=0 && number.getText().length() == 10) {
                        generateToken();

                }else{
                    Toast.makeText(getApplicationContext(), "Please fill correct detials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void generateToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("tokenfail", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                         token = task.getResult();
                        try {
                            addData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                       Log.d("token", token);
                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addData() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(GetStarted.this);
        JSONObject param = new JSONObject();
        param.put("token", token);
        param.put("name", name.getText().toString().trim());
        param.put("phone", number.getText().toString().trim());
        JsonObjectRequest put = new JsonObjectRequest(Request.Method.POST, getString(R.string.url) + "add_user", param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SharedPreferences.Editor editor = sharedpreferences.edit();

                // below two lines will put values for
                // email and password in shared preferences.
                try {
                    editor.putString(ID, response.getString("insertedId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.apply();
                Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(put);
    }
}
