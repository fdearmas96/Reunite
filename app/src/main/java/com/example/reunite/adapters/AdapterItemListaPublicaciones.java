package com.example.reunite.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reunite.classes.Publicacion;
import com.example.reunite.R;

import java.util.ArrayList;
import java.util.Vector;

public class AdapterItemListaPublicaciones extends RecyclerView.Adapter<AdapterItemListaPublicaciones.ViewHolder> {
    private LayoutInflater inflador;
    protected ArrayList<Publicacion> vectorPublicaciones; // Datos a visualizar

//    public AdapterItemListaPublicaciones(@NonNull ViewGroup parent, Vector<Publicacion> publicaciones) {
//        inflador = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.vectorPublicaciones = publicaciones;
//    }

    public AdapterItemListaPublicaciones(Context contexto, ArrayList<Publicacion> publicaciones) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.vectorPublicaciones = publicaciones;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pubTitulo;
        public ImageView pubImagen;

        public ViewHolder(View itemView) {
            super(itemView);
            pubTitulo = (TextView) itemView.findViewById(R.id.pubTitulo);
            pubImagen = (ImageView) itemView.findViewById(R.id.pubImagen);

        }
    }

    @Override // Creo la vista sin personalizar
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.item_lista_publicaciones, null);
        return new ViewHolder(v);
    }

    @Override // Personalizo el ViewHolder
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Publicacion publicacion = vectorPublicaciones.get(posicion);
        holder.pubTitulo.setText(publicacion.getPub_Titulo());
        holder.pubImagen.setImageBitmap(publicacion.getPub_img());
    }

    @Override // Indico el número de elementos de la lista
    public int getItemCount() {
        return vectorPublicaciones.size();
    }
}

