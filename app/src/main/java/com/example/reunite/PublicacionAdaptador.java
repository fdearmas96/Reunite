package com.example.reunite;

import android.widget.ImageView;

public class PublicacionAdaptador {
    private String 	Pub_Titulo, Pub_Desc, 	Pub_Contacto, 	Usuario_ID;
    private int	Pub_ID;
    private ImageView foto;

    public PublicacionAdaptador(String pub_Titulo, String pub_Desc, String pub_Contacto, String usuario_ID, int pub_ID/*, ImageView foto*/) {
        Pub_Titulo = pub_Titulo;
        Pub_Desc = pub_Desc;
        Pub_Contacto = pub_Contacto;
        Usuario_ID = usuario_ID;
        Pub_ID = pub_ID;
        //this.foto = foto;
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

    public String getPub_Contacto() {
        return Pub_Contacto;
    }

    public void setPub_Contacto(String pub_Contacto) {
        Pub_Contacto = pub_Contacto;
    }

    public String getUsuario_ID() {
        return Usuario_ID;
    }

    public void setUsuario_ID(String usuario_ID) {
        Usuario_ID = usuario_ID;
    }

    public int getPub_ID() {
        return Pub_ID;
    }

    public void setPub_ID(int pub_ID) {
        Pub_ID = pub_ID;
    }

    public ImageView getFoto() {
        return foto;
    }

    public void setFoto(ImageView foto) {
        this.foto = foto;
    }
}
