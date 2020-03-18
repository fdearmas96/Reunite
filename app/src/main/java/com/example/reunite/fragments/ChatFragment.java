package com.example.reunite.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.example.reunite.R;
import com.example.reunite.classes.Mensaje;
import com.example.reunite.classes.Utilidades;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    ListView listview;
    RequestQueue request;
    private ArrayList<Mensaje> mensajes;
    ProgressDialog progreso;


    public ChatFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_chat, container, false);
        listview = vista.findViewById(R.id.messages_view);
        mensajes = new ArrayList<>();

        obtener_mensajes();

        return vista;
    }

    private void obtener_mensajes() {
        request = Volley.newRequestQueue(getContext());
        mensajes = new ArrayList<>();
        cargarWebService(mensajes);
    }

    private void cargarWebService(ArrayList<Mensaje> mensajes) {
        JsonObjectRequest jsonObjectRequest;
        String url = Utilidades.WsObtenerPublicaciones;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), "No se pudo consultar" + error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
