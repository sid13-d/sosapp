package com.example.myapplication;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    View view;
    LayoutInflater layoutInflater;
    List<String> title, desc;
    int[] id;
    TextView notificationDesc;
    Button del;
    public ContactAdapter(Context context, ArrayList<String> title, ArrayList<String> desc, int[] id) {
        this.title = title;
        this.desc = desc;
        this.id = id;
    }
    @NonNull
    @Override
    public ContactAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.contact_layout, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactHolder holder, int position) {
        holder.notiTitle.setText(title.get(position));
        holder.notiDesc.setText(desc.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationDesc = (TextView) v.findViewById(R.id.NotificationDesc);
                del = v.findViewById(R.id.del);
                int noti =  (notificationDesc.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
                //int delnoti =  (del.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;

                TransitionManager.beginDelayedTransition(holder.cardView, new AutoTransition());
                notificationDesc.setVisibility(noti);
                //del.setVisibility(delnoti);
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
            notiTitle = (TextView) itemView.findViewById(R.id.notificationTitle);
            notiDesc = (TextView) itemView.findViewById(R.id.NotificationDesc);
            cardView = (CardView) itemView.findViewById(R.id.CardviewNotify);
        }
    }
}