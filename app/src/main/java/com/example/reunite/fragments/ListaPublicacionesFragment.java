package com.example.reunite.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reunite.classes.Publicacion;
import com.example.reunite.R;
import com.example.reunite.adapters.AdapterItemListaPublicaciones;

import java.util.Vector;

public class ListaPublicacionesFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Vector<Publicacion> publicaciones = obtenerPublicaciones();
        View vista = inflater.inflate(R.layout.fragment_lista_publicaciones, container, false);
        recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new AdapterItemListaPublicaciones(getContext(), publicaciones));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return vista;
    }

    private  Vector<Publicacion> obtenerPublicaciones() {

        // Consumir servicio !!!
        Vector<Publicacion> publicaciones = new Vector();

        for (int i=1; i<=50; i++) {
            Publicacion publicacion = new Publicacion();
            publicacion.setPub_Titulo("Publicación Ej. " + i);
            publicaciones.addElement(publicacion);
        }
        return publicaciones;
    }
}
