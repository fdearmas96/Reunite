package com.example.reunite.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;

public class ConsultaUsuarioLogueado    {
    private String user="";
    private String pass="";
    //Context context = getActivity();


    public String getPass(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String pass = preferences.getString("pass","");
        //pass =preferences.getString("pass","");
        return pass;
    }

    public String getUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String user = preferences.getString("user","");
        //user = "Usuario";
        return user;
    }


}
