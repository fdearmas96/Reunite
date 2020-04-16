package com.example.reunite.classes;

import android.content.Context;
import android.content.SharedPreferences;

import static java.security.AccessController.getContext;

public class BorrarUbicacion {

    public void borrarUbicacion (Context context){
        SharedPreferences preferences = context.getSharedPreferences("Ubicacion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("latitud","");
        editor.putString("longitud", "");
        editor.commit();}
}
