package com.example.reunite.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.reunite.R;
import com.example.reunite.classes.Comentario;

import java.util.ArrayList;

public class AdapterComentarios extends RecyclerView.Adapter<AdapterComentarios.ViewHolder> {

    private LayoutInflater inflater;
    protected ArrayList<Comentario> listaComentarios;
    RequestQueue request;
    Context context;

    public AdapterComentarios(Context context, ArrayList<Comentario> listaComentarios){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listaComentarios = listaComentarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_comentario, null);

        return new  ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comentario comentario = listaComentarios.get(position);
        holder.usuarioComentario.setText(comentario.getComentarioUsuario());
        holder.comentarioBody.setText(comentario.getComentario());
        Log.i("Agrego ", comentario.getComentario());
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView usuarioComentario;
        public TextView comentarioBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usuarioComentario = itemView.findViewById(R.id.usuario_comentario_id);
            comentarioBody    = itemView.findViewById(R.id.comentario_body_id);

        }
    }
}
