package com.example.reunite.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reunite.R;
import com.example.reunite.adapters.AdapterMensajes;
import com.example.reunite.classes.ConsultaUsuarioLogueado;
import com.example.reunite.classes.Mensaje;
import com.example.reunite.classes.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    RecyclerView recyclerView;
    RequestQueue request;
    private ArrayList<Mensaje> mensajes;
    ProgressDialog progreso;
    ImageButton btn_enviar;
    StringRequest stringrequest;
    EditText msg_send;


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
        recyclerView = vista.findViewById(R.id.messages_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btn_enviar = vista.findViewById(R.id.chat_btn_enviar);
        btn_enviar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llamarWebService();
                        msg_send.getText().clear();
                    }
                });

        msg_send = vista.findViewById(R.id.mensaje_enviar);
        mensajes = new ArrayList<>();
        obtener_mensajes();

        return vista;
    }

    private void llamarWebService() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();
        String url = Utilidades.WsNuevoMensaje;
        stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();
                if (response.trim().equalsIgnoreCase("registra")){
                    Log.i("****Se graba", "antes de cargar ");
                    Toast.makeText(getContext(), "Se registró", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Error", "No se pudo registrar " + response);
                    Toast.makeText(getContext(), "No se pudo registrar" + response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                Log.e("*No se pudo registra", "Algo falló ");
                Toast.makeText(getContext(), "Algo falló" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                ConsultaUsuarioLogueado user1 = new ConsultaUsuarioLogueado();
                String usersend = user1.getUser(getContext());
                String userreceive = "Pepe";
                String messagebody = msg_send.getEditableText().toString();
                Map<String,String> parametros = new HashMap<>();
                Log.d("Debug", messagebody);
                parametros.put("usersend", usersend);
                parametros.put("userreceive", userreceive);
                parametros.put("messagebody", messagebody);

                return parametros;
            }
        };
        stringrequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); //Intento = 1 sino la cargaba 2 veces
        request.add(stringrequest);
    }

    private void inicio() {
        //voy a la pantalla de inicio
        Fragment mifragmentNuvoUsuario = null;
        mifragmentNuvoUsuario = new ListaPublicacionesFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, mifragmentNuvoUsuario);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void obtener_mensajes() {
        request = Volley.newRequestQueue(getContext());
        mensajes = new ArrayList<>();
        cargarWebService(mensajes);
    }

    private void cargarWebService(ArrayList<Mensaje> mensajes) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Consultando");
        progreso.show();
        JsonObjectRequest jsonObjectRequest;
        String url = Utilidades.WsObtenerMensajes;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), "No se pudo consultar " + error.toString(), Toast.LENGTH_SHORT).show();
        Log.e("Error", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray json = response.optJSONArray("mensajes");
        JSONObject jsonObject;
        Log.d("Debug", json.toString());
        try {
            for (int i = 0; i < json.length(); i++) {
                jsonObject = json.getJSONObject(i);
                Log.d("Debug", String.valueOf(jsonObject));

                String respuestanull = jsonObject.optString("mensaje");

                if (!respuestanull.equalsIgnoreCase("No hay mensajes")) {
                    String mensaje_body = jsonObject.optString("Msg_texto");
                    String mensaje_userreceive = jsonObject.optString("Msg_userreceive");
                    String mensaje_usersend = jsonObject.optString("Msg_usersend");

                    Mensaje mensaje = new Mensaje(mensaje_body, mensaje_userreceive, mensaje_usersend);
                    mensajes.add(mensaje);
                }
            }
            progreso.hide();
            AdapterMensajes adapterMensajes = new AdapterMensajes(getContext(), mensajes);
            recyclerView.setAdapter(adapterMensajes);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            progreso.hide();
            e.printStackTrace();
        }
    }
}
