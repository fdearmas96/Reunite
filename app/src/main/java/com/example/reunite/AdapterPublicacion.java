package com.example.reunite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterPublicacion extends RecyclerView.Adapter<AdapterPublicacion.ViewHolderPublicacion> {

    ArrayList<String> listPublicaciones;

    public AdapterPublicacion(ArrayList<String> listPublicaciones) {
        this.listPublicaciones = listPublicaciones;
    }

    @Override
    public ViewHolderPublicacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);

        return new ViewHolderPublicacion(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPublicacion holder, int position) {
        holder.asignarPublicaciones(listPublicaciones.get(position));
    }

    @Override
    public int getItemCount() {
        return listPublicaciones.size();
    }

    public class ViewHolderPublicacion extends RecyclerView.ViewHolder {
        //Referencio lo que quiero mostrar
        TextView publicacionTit, PublicacionDescri, PublicacionContacto;
        ImageView ImagenPublicacion;


        public ViewHolderPublicacion(@NonNull View itemView) {
            super(itemView);
            publicacionTit = itemView.findViewById(R.id.publicacionTit);
            PublicacionDescri = itemView.findViewById(R.id.PublicacionDescri);
            PublicacionContacto = itemView.findViewById(R.id.PublicacionContacto);
            ImagenPublicacion = itemView.findViewById(R.id.ImagenPublicacion);
        }

        public void asignarPublicaciones(String s) {

        }
    }
}
