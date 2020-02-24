package com.example.reunite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import java.util.Date;

public class Publicacion {

    private int pub_id;
    private String Pub_Titulo; //Titulo
    private String Pub_Desc; //Descripción
    private Date Pub_fecha;  //fecha de publicación
    //private String Pub_img; //Dirección de la foto
    private String Pub_contacto;//contacto
    private String dato;
    private Bitmap Pub_img;

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

    public Date getPub_fecha() {
        return Pub_fecha;
    }

    public void setPub_fecha(Date pub_fecha) {
        Pub_fecha = pub_fecha;
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