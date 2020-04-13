package com.example.reunite.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.reunite.adapters.AdapterChat;
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

public class ChatHubFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    RecyclerView recyclerView;
    RequestQueue request;
    ArrayList<String> chats;
    ProgressDialog progreso;
    String user = new ConsultaUsuarioLogueado().getUser(this.getContext());

    public ChatHubFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_chat_hub, container, false);
        recyclerView = vista.findViewById(R.id.messages_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        chats = new ArrayList<>();
        try {
            obtener_chats();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getActivity().setTitle("Mensajes");

        return vista;
    }

    private void obtener_chats() throws JSONException {
        request = Volley.newRequestQueue(getContext());
        chats = new ArrayList<>();
        cargarWebService(chats);
    }

    private void cargarWebService(ArrayList<String> chats) throws JSONException {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Consultando");
        progreso.show();
        String url = Utilidades.WsObtenerChats + "usuario=" + user;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
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
        Boolean hayChats = false;
        try {
            for (int i = 0; i < json.length(); i++) {
                jsonObject = json.getJSONObject(i);
                Log.d("Debug", String.valueOf(jsonObject));

                String respuestanull = jsonObject.optString("chat");
                if (!respuestanull.equalsIgnoreCase("[\"No hay chat\"]")) {
                    hayChats = true;
                    String chat_user_send = jsonObject.optString("chat_user_send");

                    String chat = chat_user_send;
                    chats.add(chat);
                }
            }

            progreso.hide();

            if (hayChats) {
                AdapterChat adapterChat = new AdapterChat(getContext(), chats, user);
                recyclerView.setAdapter(adapterChat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            progreso.hide();
            e.printStackTrace();
        }
    }
}
