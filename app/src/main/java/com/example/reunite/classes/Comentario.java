package com.example.reunite.classes;

public class Comentario {
    private String comentarioUsuario;
    private String comentario;
    private int comentarioId;
    private int comentarioPublicación;

    public Comentario(String comentarioUsuario, String comentario, int comentarioId, int comentarioPublicación) {
        this.comentarioUsuario = comentarioUsuario;
        this.comentario = comentario;
        this.comentarioId = comentarioId;
        this.comentarioPublicación = comentarioPublicación;
    }


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
}
