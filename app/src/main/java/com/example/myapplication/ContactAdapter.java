package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    View view;
    Context context;
    LayoutInflater layoutInflater;
    List<String> title, desc;
    List<String> id;
    TextView notificationDesc;
    Button del;
    AddPerson ap;
    public ContactAdapter(Context context, ArrayList<String> title, ArrayList<String> desc, ArrayList<String> id) {
        this.title = title;
        this.desc = desc;
        this.id = id;
        this.context = context;
        this.ap = new AddPerson();
    }
    @NonNull
    @Override
    public ContactAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.contact_layout, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.notiTitle.setText(title.get(position));
        holder.notiDesc.setText(desc.get(position));
        del = view.findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                JSONObject param = new JSONObject();
                try {
                    param.put("user_id", "6287fdbe90bd466058fb34e8");
                    param.put("sos_id", id.get(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest delete = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.url) + "remove_from_sos_list", param, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(v.getContext(), "User Removed", Toast.LENGTH_SHORT).show();
                        ap.getSosList();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(v.getContext(), "There was problem removing user", Toast.LENGTH_SHORT).show();

                    }
                });
                requestQueue.add(delete);

//                Intent intent = new Intent(context, AddPerson.class);
//                ((Activity) context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        TextView notiTitle, notiDesc;
        CardView cardView;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            notiTitle = (TextView) itemView.findViewById(R.id.personName);
            notiDesc = (TextView) itemView.findViewById(R.id.personPhone);
            cardView = (CardView) itemView.findViewById(R.id.CardviewNotify);

        }
    }


}