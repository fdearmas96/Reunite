package com.example.reunite.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.reunite.R;
import com.example.reunite.classes.ConsultaUsuarioLogueado;
import com.example.reunite.classes.Mensaje;

import java.util.ArrayList;

public class AdapterMensajes extends RecyclerView.Adapter<AdapterMensajes.ViewHolder> {

    private LayoutInflater inflater;
    protected ArrayList<Mensaje> listaMensajes;
    RequestQueue request;
    Context context;
    String user;

    public AdapterMensajes(Context context, ArrayList<Mensaje> listaMensajes, String user){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listaMensajes = listaMensajes;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.mensaje, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mensaje mensaje = listaMensajes.get(position);
        holder.mensaje.setText(mensaje.getMensajeTexto());
        if (mensaje.getUsuarioReceive() != user) {
            holder.mensaje.setBackgroundColor(Color.BLUE);
        }
        holder.user.setText(mensaje.getUsuarioSend());
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mensaje;
        public TextView user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.message_body);
            user = itemView.findViewById(R.id.message_user);
        }
    }
}
