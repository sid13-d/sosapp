package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Dialer extends AppCompatActivity {
EditText phone,site;
Button dial,navigate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);

        phone = findViewById(R.id.phone);
        dial = findViewById(R.id.Call);

        site = findViewById(R.id.site);
        navigate = findViewById(R.id.navigate);

        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   caller();

            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigation();
            }
        });
    }
    public void caller() {
        Toast.makeText(getApplicationContext(), "called", Toast.LENGTH_SHORT).show();
        String number = phone.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    public void navigation() {
        Toast.makeText(getApplicationContext(), "navigating...", Toast.LENGTH_SHORT).show();
        String url = site.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://"+url));
        startActivity(intent);
    }
}