package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ShareApp extends AppCompatActivity {
Button share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);

        share = findViewById(R.id.share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("Text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "This is sharing app");
                intent.putExtra(Intent.EXTRA_SUBJECT, "pthakare@2003");
                startActivity(Intent.createChooser(intent, "Share via"));
                //intent.setPackage("com.whatsapp");

                try {

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast toast = Toast.makeText(getApplicationContext(), "whatsapp not installed", Toast.LENGTH_LONG);
                    toast.setMargin(50,50);
                    toast.show();
                }
            }
        });
    }
}