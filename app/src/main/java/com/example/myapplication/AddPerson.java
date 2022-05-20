package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.TaskInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddPerson extends AppCompatActivity {
    private static final int PICK_CONTACT = 1;
    Button show;
    RecyclerView recyclerView;
    ContactAdapter contactAdapter;
    LinearLayout linearLayout;
    Button delete;
//    String[] name={"Prathamesh","Somesh","Ajinkya","Siddhesh"};
    ArrayList<String> namee = new ArrayList<String>();
    ArrayList<String> phone = new ArrayList<String>();
//    String[] phone = {"Prathamesh","Somesh","Ajinkya","Siddhesh"};
    private String[] numbers;
    private String[] user_id;
    int[] id={1,2,3,4};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        show = findViewById(R.id.showList);
        delete = findViewById(R.id.del);
        namee.add("Title");
        phone.add("Number");

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddPerson.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    showList();
                }else{
                    ActivityCompat.requestPermissions(AddPerson.this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
                }
            }
        });
        getUsers();
        recyclerView = (RecyclerView) findViewById(R.id.list);
        contactAdapter = new ContactAdapter(getApplicationContext(), namee, phone, id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(contactAdapter);


    }

    private void getUsers() {
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.url) + "get_users", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                        JSONArray phnnumber = response.getJSONArray("phone_numbers");
                        JSONArray id = response.getJSONArray("ids");
                       numbers = new String[phnnumber.length()];
                       user_id = new String[id.length()];

                        for (int i=0; i<numbers.length; i++) {
                            numbers[i] = phnnumber.optString(i);
                            user_id[i] = id.optString(i);
                        }
                      Log.d("array", Arrays.toString(numbers));

                    Log.d("users", Arrays.toString(user_id));
//                    Log.d("users", ids);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("users", "onResponse: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(jsonObjectRequest);
    }

    public void showList(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case (PICK_CONTACT):{
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1"))
                        {
                            String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                            phones.moveToFirst();
                            String contactNumber = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String contactName = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                            Toast.makeText(getApplicationContext(), contactNumber, Toast.LENGTH_SHORT).show();

                            checkUserExistence(contactNumber,contactName);
                        }
                        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();


                        // TODO Whatever you want to do with the selected contact name.
                    }

                }
            }
            break;
        }
    }
    public void checkUserExistence(String number, String name){
        number = number.replace(" ","");
        number = number.replace("+91","");
       for (int i=0; i<numbers.length; i++) {
           Log.d("numbersi", numbers[i]);
           if (numbers[i].equals(number)) {
               Log.d("userNumber", ""+i);

               addUserinArray(number, name);
               Toast.makeText(getApplicationContext(), "User exists", Toast.LENGTH_SHORT).show();

           }else{
               Toast.makeText(getApplicationContext(), "No user found", Toast.LENGTH_SHORT).show();
           }
       }
    }

    private void addUserinArray(String number, String name) {
        if (!namee.contains(name) && number.contains(number)){
            namee.add(name);
            phone.add(number);
        }else{
            Toast.makeText(getApplicationContext(), "The user has been added already", Toast.LENGTH_SHORT).show();
        }

    }


}