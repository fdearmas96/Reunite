package com.example.reunite.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.reunite.classes.Comentario;
import com.example.reunite.classes.Publicacion;
import com.example.reunite.R;
import com.example.reunite.classes.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicacionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicacionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView publicacionTit;
    ImageView ImagenPublicacion;
    TextView publicacionDescri;
    TextView publicacionContacto;
    ProgressDialog progreso;
    Publicacion miPublicacion = new Publicacion();
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    ///**************Para los comentarios:
    ArrayList<Comentario> listaComentarios ;
    RecyclerView recyclerComentarios= null;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicacionFragment newInstance(String param1, String param2) {

        PublicacionFragment fragment = new PublicacionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_publicacion, container, false);

        publicacionTit = (TextView) vista.findViewById(R.id.publicacionTit);
        ImagenPublicacion = (ImageView) vista.findViewById(R.id.ImagenPublicacion);
        publicacionDescri = (TextView) vista.findViewById(R.id.PublicacionDescri);
        publicacionContacto = (TextView) vista.findViewById(R.id.PublicacionContacto);
        request = Volley.newRequestQueue(getContext());
        Bundle objetoPublicaicon = getArguments();
        Publicacion publicacion = null;

        if (objetoPublicaicon != null) {
            Log.i("11111Bien", "Le llega objeto");
            publicacion = (Publicacion) objetoPublicaicon.getSerializable("objeto"); //ese objeto está en el mainActivity
            int pub_id = publicacion.getPub_id();
            publicacionTit.setText( publicacion.getPub_Titulo());
            publicacionDescri.setText(publicacion.getPub_Desc());
            publicacionContacto.setText(publicacion.getPub_contacto());
            String url_imagen = Utilidades.servidor +  publicacion.getRuta_imagen();
            cargarWebServiceImagen(url_imagen, pub_id);

            if (publicacion.getPub_img()!=null){
                Log.i("11111Bien", "Le llega imagen");
                ImagenPublicacion.setImageBitmap(publicacion.getPub_img());
            }else{Log.i("Mal", "No Le llega imagen");

            }

        }else {

        }


        ///***********************Se Carga el recycler de los comentarios*********************************************//listaComentarios

        recyclerComentarios = (RecyclerView) vista.findViewById(R.id.recyclerComentarios);
        listaComentarios = new ArrayList<>();
        recyclerComentarios.setLayoutManager(new LinearLayoutManager(getContext()));
        for (int i=0; i< 10;i ++) {
            Comentario comentario = new Comentario("usuario", "comentario", i, 2);
            listaComentarios.add(comentario);
        }


        //cargarWebService();

        JsonObjectRequest jsonObjectRequest;


        return vista;
    }

    private void cargarWebServiceImagen(String url_imagen, int pub_id) {
        url_imagen = url_imagen.replace(" ", "%20");//para quitar espacios
        //Toast.makeText(context,  " Busco imagen " + url_imagen , Toast.LENGTH_SHORT).show();
        ImageRequest imageRequest = new ImageRequest(url_imagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                response = Bitmap.createScaledBitmap(response,600,800,true);
                ImagenPublicacion.setImageBitmap(response);
            }
        },

                0, 0, ImageView.ScaleType.CENTER, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context,  "Error al cargar la imagen "  , Toast.LENGTH_SHORT).show();
                    }
                });
        request.add(imageRequest);
    }

    private void cargarWebService() {
        //this.seleccionada = seleccionada;
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Consultando");
        progreso.show();

        //String url = Utilidades.servidor + "Reunite/ConsultarPublicacion.php?Pub_ID=6";
        String url = Utilidades.WsConsultarPublicacion +"Pub_ID=" ;
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
        progreso.hide();
        Toast.makeText(getContext(),"Mensaje: " +response, Toast.LENGTH_SHORT).show();

        //Publicacion miPublicacion = new Publicacion();
        JSONArray  json = response.optJSONArray("publicacion");
        JSONObject jsonObject = null;
        try {
            jsonObject = json.getJSONObject(0);
            miPublicacion.setPub_Titulo(jsonObject.optString("Pub_Titulo"));
            miPublicacion.setPub_Desc(jsonObject.optString("Pub_Desc"));
            miPublicacion.setPub_contacto(jsonObject.optString("Pub_Contacto"));
            miPublicacion.setRuta_imagen(jsonObject.optString("Pub_img"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        publicacionTit.setText( miPublicacion.getPub_Titulo());
        publicacionDescri.setText(miPublicacion.getPub_Desc());
        publicacionContacto.setText(miPublicacion.getPub_contacto());
        String url_imagen = Utilidades.servidor + "Reunite/" + miPublicacion.getRuta_imagen();
        Toast.makeText(getContext(), "No se pudo consultar" + url_imagen , Toast.LENGTH_SHORT).show();
        cargarWebServiceImagen(url_imagen);
        //ImagenPublicacion.setImageBitmap(miPublicacion.getPub_img());


    }

    private void cargarWebServiceImagen(String url_imagen) {
        url_imagen = url_imagen.replace(" ", "%20");//para quitar espacios
        ImageRequest imageRequest = new ImageRequest(url_imagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ImagenPublicacion.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al cargar la imagen " , Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
