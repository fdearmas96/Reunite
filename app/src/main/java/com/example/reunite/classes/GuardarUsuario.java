package com.example.reunite.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class GuardarUsuario {
    private String user;
    private String pass;
    private Context context;

    public void GuardarUsuario(String user, String pass, Context context) {
        this.user = user;
        this.pass = pass;
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String usuario = user.toString().trim();
        String contrasena = pass.toString().trim();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", usuario);
        editor.putString("pass", contrasena);
        editor.commit();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
