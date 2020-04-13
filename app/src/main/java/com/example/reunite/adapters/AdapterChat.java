package com.example.reunite.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.reunite.R;

import java.util.ArrayList;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ViewHolder> {

    private LayoutInflater inflater;
    protected ArrayList<String> listaChats;
    RequestQueue request;
    Context context;
    String user;

    public AdapterChat(Context context, ArrayList<String> listaChats, String user){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listaChats = listaChats;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_mensaje, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String chat = listaChats.get(position);
        holder.user.setText(chat);
    }

    @Override
    public int getItemCount() {
        return listaChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user;
        public RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.mensaje_layout);
            user = itemView.findViewById(R.id.message_user);
        }
    }
}
