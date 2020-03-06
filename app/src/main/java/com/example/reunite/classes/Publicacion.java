package com.example.reunite.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.Date;

public class Publicacion  implements Serializable {

    private int pub_id;
    private String Pub_Titulo; //Titulo
    private String Pub_Desc; //Descripción

    public String getRuta_imagen() {
        return ruta_imagen;
    }

    public void setRuta_imagen(String ruta_imagen) {
        this.ruta_imagen = ruta_imagen;
    }

    //private String Pub_img; //Dirección de la foto
    private String Pub_contacto;//contacto
    private String dato;
    private Bitmap Pub_img;
    private String ruta_imagen;

    public Publicacion() {
        this.pub_id = 99;
        Pub_Titulo = "Ej_Titulo";
        Pub_Desc = "Ej_Desc";
        Pub_contacto = "Ej_contacto";
    }

    public int getPub_id() {
        return pub_id;
    }

    public void setPub_id(int pub_id) {
        this.pub_id = pub_id;
    }

    public String getPub_Titulo() {
        return Pub_Titulo;
    }

    public void setPub_Titulo(String pub_Titulo) {
        Pub_Titulo = pub_Titulo;
    }

    public String getPub_Desc() {
        return Pub_Desc;
    }

    public void setPub_Desc(String pub_Desc) {
        Pub_Desc = pub_Desc;
    }


    public String getPub_contacto() {
        return Pub_contacto;
    }

    public void setPub_contacto(String pub_contacto) {
        Pub_contacto = pub_contacto;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
        try {
            byte[] byteCode = Base64.decode(dato, Base64.DEFAULT);
            this.Pub_img = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Bitmap getPub_img() {


        return Pub_img;
    }

    public void setPub_img(Bitmap pub_img) {


        Pub_img = pub_img;
    }
}