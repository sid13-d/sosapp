package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Sos extends AppCompatActivity {

    TextView locationText;
    FloatingActionButton addSOS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        locationText = (TextView) findViewById(R.id.locationText);
        locationText.setSelected(true);

        addSOS = (FloatingActionButton) findViewById(R.id.floatingbtn);
        addSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sos.this, AddPerson.class);
                startActivity(intent);
            }
        });
    }
}