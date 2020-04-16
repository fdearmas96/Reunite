package com.example.reunite.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.reunite.R;
import com.example.reunite.classes.Comentario;
import com.example.reunite.classes.Publicacion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragmentView extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    Button btnAceptar;
    Marker marcador;
    double longitudDouble;
    double latitudDouble;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        btnAceptar = rootView.findViewById(R.id.btnAceptarubicacion);
        btnAceptar.setVisibility(View.GONE);
        Bundle objetoComentario = getArguments();
        Comentario comentario = null;
        if (objetoComentario != null) {
            //Log.i("11111Bien", "Le llega objeto");
            comentario = (Comentario) objetoComentario.getSerializable("objeto"); //ese objeto está en el mainActivity
            String latitud = comentario.getLatitud();
            String longitud = comentario.getLongitud();
            latitudDouble = Double.parseDouble(latitud);
            longitudDouble = Double.parseDouble(longitud);

        }
        final LatLng ubicacion = new LatLng(latitudDouble,longitudDouble);


        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);


        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                final GoogleMap map = mMap;

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                               // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(ubicacion).zoom(11).build();
                map.addMarker(new MarkerOptions().position(ubicacion));
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}