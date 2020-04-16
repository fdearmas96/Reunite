package com.example.reunite.classes;

import java.io.Serializable;

public class Comentario implements Serializable {
    private String comentarioUsuario;
    private String comentario;
    private int comentarioId;
    private int comentarioPublicación;
    private String latitud, longitud;

    /*public Comentario(String comentarioUsuario, String comentario, int comentarioId, int comentarioPublicación) {
        this.comentarioUsuario = comentarioUsuario;
        this.comentario = comentario;
        this.comentarioId = comentarioId;
        this.comentarioPublicación = comentarioPublicación;
    }*/


    public String getComentarioUsuario() {
        return comentarioUsuario;
    }

    public void setComentarioUsuario(String comentarioUsuario) {
        this.comentarioUsuario = comentarioUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getComentarioId() {
        return comentarioId;
    }

    public void setComentarioId(int comentarioId) {
        this.comentarioId = comentarioId;
    }

    public int getComentarioPublicación() {
        return comentarioPublicación;
    }

    public void setComentarioPublicación(int comentarioPublicación) {
        this.comentarioPublicación = comentarioPublicación;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
