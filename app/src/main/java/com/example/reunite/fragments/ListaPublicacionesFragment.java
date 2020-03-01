package com.example.reunite.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.reunite.classes.Publicacion;
import com.example.reunite.R;
import com.example.reunite.adapters.AdapterItemListaPublicaciones;
import com.example.reunite.classes.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

public class ListaPublicacionesFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    private RecyclerView recyclerView;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progreso;
    ArrayList<Publicacion> publicaciones ;//= new Vector();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Vector<Publicacion> publicaciones = obtenerPublicaciones();
        View vista = inflater.inflate(R.layout.fragment_lista_publicaciones, container, false);
        recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view);
        publicaciones = new ArrayList<>();
        //recyclerView.setAdapter(new AdapterItemListaPublicaciones(getContext(), publicaciones));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        obtenerPublicaciones();


        return vista;
    }



    private Vector<Publicacion> obtenerPublicaciones() {

        // Consumir servicio !!!
        Vector<Publicacion> publicaciones = new Vector();
        Publicacion publicacion = new Publicacion();
        /*for (int i=1; i<=50; i++) {
            Publicacion publicacion = new Publicacion();
            publicacion.setPub_Titulo("Publicación Ej. " + i);
            publicaciones.addElement(publicacion);
        }*/
        request = Volley.newRequestQueue(getContext());
        cargarWebService(publicaciones);




        return publicaciones;
    }

    private void cargarWebService(Vector<Publicacion> publicaciones) {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Consultando");
        progreso.show();
        JsonObjectRequest jsonObjectRequest;
        String url = Utilidades.servidor + "Reunite/ObtenerPublicaciones.php";//.php?Pub_ID=6";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    //Error en la llamada al webService:
    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), "No se pudo consultar" + error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error", error.toString());

    }
    //El web service responde:
    @Override
    public void onResponse(JSONObject response) {
        Publicacion publicacion = null;

        JSONArray json = response.optJSONArray("publicacion");
        JSONObject jsonObject = null;
        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();

        try {

            for (int i=0; i< json.length();i ++){
                publicacion = new Publicacion();
                jsonObject = null;
                jsonObject = json.getJSONObject(i);
                publicacion.setPub_Titulo(jsonObject.optString("Pub_Titulo"));
                //publicacion.setPub_Desc(jsonObject.optString("Pub_Desc"));
                //publicacion.setPub_contacto(jsonObject.optString("Pub_Contacto"));
                //publicacion.setRuta_imagen(jsonObject.optString("Pub_img"));
                publicaciones.add(publicacion);
                //publicaciones.addElement(publicacion);
            }

            progreso.hide();
            Toast.makeText(getContext(), publicacion.getPub_Titulo().toString(), Toast.LENGTH_SHORT).show();
            AdapterItemListaPublicaciones adapterItemListaPublicaciones = new AdapterItemListaPublicaciones(getContext(),publicaciones);
            recyclerView.setAdapter(adapterItemListaPublicaciones);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
