package com.example.reunite.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.reunite.IComunicaFragments;
import com.example.reunite.adapters.AdapterComentarios;
import com.example.reunite.classes.BorrarUbicacion;
import com.example.reunite.classes.Comentario;
import com.example.reunite.classes.ConsultaUsuarioLogueado;
import com.example.reunite.classes.Publicacion;
import com.example.reunite.R;
import com.example.reunite.classes.Utilidades;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


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
    int IdentidadDePub;

    //Para el mapa
    MapView mapViewpub;

    ///**************Para los comentarios:
    ArrayList<Comentario> listaComentarios ;
    RecyclerView recyclerComentarios= null;
    Button btn_comentar, btnMapaComentario;
    EditText comentario_body_id;
    Comentario comentario = null;

    String url = null;
    String urlComparar = null;
    BorrarUbicacion borrarUbicacion = new BorrarUbicacion();
    String latitud1 ;
    String longitud1;

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
        btn_comentar = vista.findViewById(R.id.btn_comentar);
        comentario_body_id = vista.findViewById(R.id.comentario_body_id);
        btnMapaComentario  = vista.findViewById(R.id.btnMapaComentario);
        //btnMapaComentario.setVisibility(View.GONE); //Lo dejo invisible por ahora se muestra una alerta que te pregunta que si queres agregar una ubicación

        request = Volley.newRequestQueue(getContext());
        Bundle objetoPublicaicon = getArguments();
        Publicacion publicacion = null;

        if (objetoPublicaicon != null) {
            //Log.i("11111Bien", "Le llega objeto");
            publicacion = (Publicacion) objetoPublicaicon.getSerializable("objeto"); //ese objeto está en el mainActivity
            int pub_id = publicacion.getPub_id();




            this.IdentidadDePub = pub_id;
            publicacionTit.setText( publicacion.getPub_Titulo());
            publicacionDescri.setText(publicacion.getPub_Desc());
            publicacionContacto.setText(publicacion.getPub_contacto());
            String url_imagen = Utilidades.servidor +  publicacion.getRuta_imagen();
            cargarWebServiceImagen(url_imagen, pub_id);

            if (publicacion.getPub_img()!=null){
                ImagenPublicacion.setImageBitmap(publicacion.getPub_img());
            }else{//Log.i("Mal", "No Le llega imagen");

            }

            //para el mapa
            String latitudStr = publicacion.getLatitud();
            String longitudStr = publicacion.getLongitud();
            //Log.e("latitud", "antes");
            //Log.e("latitud", latitudStr);
            //Log.e("longitud", longitudStr);
            if (latitudStr == null || latitudStr.equals("")){
                // Log.e("latitud", "entro");
                mapViewpub = (MapView) vista.findViewById(R.id.mapViewpub);
                mapViewpub.setVisibility(View.GONE);
            }else{

                mapViewpub = (MapView) vista.findViewById(R.id.mapViewpub);
                mapViewpub.onCreate(savedInstanceState);
                mapViewpub.onResume(); // needed to get the map to display immediately

                final Double latitud=Double.parseDouble(latitudStr);
                final Double longitud=Double.parseDouble(longitudStr);
                try {
                    MapsInitializer.initialize(getActivity().getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mapViewpub.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap mMap) {
                        final GoogleMap map = mMap;

                        // For showing a move to my location button
                        //googleMap.setMyLocationEnabled(true);

                        // For dropping a marker at a point on the Map
                        //Log.e("latitud", latitud.toString());
//                        Log.e("longitud", longitud.toString());
                        LatLng ultimavez = new LatLng(latitud,longitud);


                        // For zooming automatically to the location of the marker
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(ultimavez).zoom(11).build();
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        map.addMarker(new MarkerOptions().position(ultimavez));
                    }
                });
            }


        }else {

        }


        ///***********************Se Carga el recycler de los comentarios*********************************************//listaComentarios

        recyclerComentarios = (RecyclerView) vista.findViewById(R.id.recyclerComentarios);
        listaComentarios = new ArrayList<>();
        recyclerComentarios.setLayoutManager(new LinearLayoutManager(getContext()));
        cargarComentarios(publicacion.getPub_id());


        ////**************Para agregar los comentarios *********//////////////////
        //btn_comentar.setOnClickListener(agregarComentario(publicacion.getPub_id()));
        btn_comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarComentario();
            }
        });
        btnMapaComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarUbiPublicacion();
            }
        });

        //JsonObjectRequest jsonObjectRequest;

        getActivity().setTitle("Publicación");

        return vista;
    }

    private void agregarUbiPublicacion() {
        borrarUbicacion.borrarUbicacion(getContext());
        Fragment mifragmentNuvoUsuario = null;
        mifragmentNuvoUsuario = new MapsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, mifragmentNuvoUsuario);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private  void agregarComentario() {

        SharedPreferences preferences = getContext().getSharedPreferences("Ubicacion", Context.MODE_PRIVATE);
        latitud1 = preferences.getString("latitud","");
        longitud1 = preferences.getString("longitud","");
        Log.e("*****latitud", latitud1 );
        Log.e("****longitud", longitud1 );
        Log.e("****Antes de llamar", "webserv");
        llamarwsCargarComentario();
        comentario_body_id.setText("");
        borrarUbicacion.borrarUbicacion(getContext());


    }

    private void llamarwsCargarComentario() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Agregando comentarios");
        progreso.show();
        String usuarioLogueado = new ConsultaUsuarioLogueado().getUser(getContext());
        //http://localhost:8080/Reunite/comentarios.php?accion=INS&usuario=Fernando&publicacion=1&comentario=magui2
        this.url = Utilidades.WsComentarios+"accion=INS&usuario="+usuarioLogueado+"&publicacion="+
                this.IdentidadDePub+"&comentario=" + comentario_body_id.getText()+"&latitud=" + latitud1+"&longitud=" + longitud1;
        this.urlComparar = this.url;
        Log.i("*****URL_Agregar", url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,this.url,null,this,this);
        request.add(jsonObjectRequest);
        borrarUbicacion.borrarUbicacion(getContext()); //para que se limpie
    }



    private void cargarComentarios(int pub_id) {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando comentarios");
        progreso.show();
        this.url = Utilidades.WsComentarios+"accion=DSP&publicacion="+pub_id;
        this.urlComparar ="";
        //Log.i("*********URL********", url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,this.url,null,this,this);
        request.add(jsonObjectRequest);
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


    @Override
    public void onErrorResponse(VolleyError error) {

        progreso.hide();
        Toast.makeText(getContext(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
        //Log.i("Error", error.toString()+url);
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        //Toast.makeText(getContext(),"Mensaje: " +response, Toast.LENGTH_SHORT).show();
        //Publicacion miPublicacion = new Publicacion();
        if (url.equals(Utilidades.WsComentarios+"accion=DSP&publicacion="+this.IdentidadDePub) ){
            JSONArray  json = response.optJSONArray("comentario");
            JSONObject jsonObject = null;
            try {
                listaComentarios.clear();
                AdapterComentarios adapterComentarios = new AdapterComentarios(getContext(),listaComentarios);
                recyclerComentarios.setAdapter(adapterComentarios);
                for (int i=0; i< json.length();i ++){
                    jsonObject = null;
                    jsonObject = json.getJSONObject(i);
                    if (jsonObject.optString("success").equals("0") ){
                        comentario = new Comentario();
                        comentario.setComentarioId(Integer.parseInt(jsonObject.optString("comentario_Id")));
                        comentario.setComentarioUsuario(jsonObject.optString("Usuario_ID"));
                        comentario.setComentarioPublicación(Integer.parseInt(jsonObject.optString("Pub_ID")));
                        comentario.setComentario(jsonObject.optString("comentario_body"));
                        comentario.setLatitud(jsonObject.optString("latitud"));
                        comentario.setLongitud(jsonObject.optString("longitud"));
                        Log.e("PubFragment ", jsonObject.optString("latitud")+jsonObject.optString("comentario_body"));
                        listaComentarios.add(comentario);
                        //Log.i("Error", "agrego comentario " + comentario.getComentario());
                       // adapterComentarios = new AdapterComentarios(getContext(),listaComentarios);
                       // recyclerComentarios.setAdapter(adapterComentarios);
                    }


                }
                adapterComentarios = new AdapterComentarios(getContext(),listaComentarios);
                //Para seleccionar el comentario:
                adapterComentarios.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getContext(),
                          //      "Selecciona:" + listaComentarios.get
                            //            (recyclerComentarios.getChildAdapterPosition(v)).getComentario(),Toast.LENGTH_SHORT).show();
                        if (listaComentarios.get(recyclerComentarios.getChildAdapterPosition(v)).getLatitud().equals(""))
                        {

                        }else{
                            String latitud =listaComentarios.get(recyclerComentarios.getChildAdapterPosition(v)).getLatitud();
                            String longitud =listaComentarios.get(recyclerComentarios.getChildAdapterPosition(v)).getLatitud();
                            IComunicaFragments interfaceComunicaFragment = null;// = null;
                            Activity activity = (Activity) getContext();
                            interfaceComunicaFragment = (IComunicaFragments) activity;

                            interfaceComunicaFragment.enviarComentario(listaComentarios.get(recyclerComentarios.getChildAdapterPosition(v)));//getChildAdapterPosition(getView())

                        }

                    }

                });

                recyclerComentarios.setAdapter(adapterComentarios);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(urlComparar.equals(url)){
            //Por ahora nada
            cargarComentarios(IdentidadDePub);
            comentario_body_id.setText("");
        }


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
