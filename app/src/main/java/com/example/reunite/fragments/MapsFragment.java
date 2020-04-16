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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    Button btnAceptar;
    Marker marcador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        btnAceptar = rootView.findViewById(R.id.btnAceptarubicacion);
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
                LatLng sydney = new LatLng(-34.74562322620677,-56.16548761725426);


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(11).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                btnAceptar.setEnabled(false);
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        map.clear();
                        marcador = map.addMarker(new MarkerOptions().position(latLng));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                        Log.e("*********URL********", marcador.getPosition().toString());
                        btnAceptar.setEnabled(true);
                    }
                });
                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aceptar(marcador);
                    }
                });

            }
        });

        return rootView;
    }

    private void aceptar(Marker marker) {

        SharedPreferences preferences = getContext().getSharedPreferences("Ubicacion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        double latitud = marker.getPosition().latitude;
        double longitud = marker.getPosition().longitude;
        editor.putString("latitud", Double.toString(latitud));
        editor.putString("longitud", Double.toString(longitud));

        editor.commit();


        //hace que vuelva al anterior:
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
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