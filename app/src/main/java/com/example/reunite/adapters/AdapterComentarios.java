
package com.example.reunite.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.reunite.R;
import com.example.reunite.classes.Comentario;
import com.example.reunite.fragments.MapsFragment;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;


public class AdapterComentarios
        extends RecyclerView.Adapter<AdapterComentarios.ViewHolder>
        implements View.OnClickListener {

    private LayoutInflater inflater;
    protected ArrayList<Comentario> listaComentarios;
    private  View.OnClickListener listener;
    RequestQueue request;
    Context context;
    MapView mapviewComentario;
    Button btnUbicacion;
    Toast toast;
    Fragment myFragment;
    public AdapterComentarios(Context context, ArrayList<Comentario> listaComentarios){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listaComentarios = listaComentarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_comentario, null);

        v.setOnClickListener(this);
        return new  ViewHolder(v);
    }

    private void mostrarMapa(Comentario comentario) {
        Log.i("selecciona2 ", comentario.getComentario());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Comentario comentario = listaComentarios.get(position);
        holder.usuarioComentario.setText(comentario.getComentarioUsuario());
        holder.comentarioBody.setText(comentario.getComentario());

        Log.i("Agrego *****", comentario.getComentario() + comentario.getLatitud());
//        mapviewComentario.setVisibility(View.GONE);

        String latitudStrM;
        String longitudStrM;
        //para el mapa
        //final Comentario comentariosel = listaComentarios.get(position);//.getLatitud();
        btnUbicacion = holder.itemView.findViewById(R.id.btnUbicacion);
        btnUbicacion.setVisibility(View.GONE);
        ImageView imgUbicacion = holder.itemView.findViewById(R.id.imgUbicacion);

        if (comentario.getLatitud().equals("")) {

           // btnUbicacion.setVisibility(View.GONE);
            imgUbicacion.setVisibility(View.GONE);
        }else  {
            Log.i("ELSE *****", "Hago el botón visible");
           // btnUbicacion.setVisibility(View.VISIBLE);
            imgUbicacion.setVisibility(View.VISIBLE);
        }

        latitudStrM =  comentario.getLatitud();
        longitudStrM =  comentario.getLongitud();

        /*btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(btnUbicacion.getContext(), "No se ingresó una ubicación", Toast.LENGTH_SHORT).show();
                Log.i("Selecciono *****", comentario.getComentario());
                mostrarMapa(comentario);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView usuarioComentario;
        public TextView comentarioBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usuarioComentario = itemView.findViewById(R.id.usuario_comentario_id);
            comentarioBody    = itemView.findViewById(R.id.comentario_body_id);
           // btnUbicacion = itemView.findViewById(R.id.btnUbicacion);


        }
    }
}
