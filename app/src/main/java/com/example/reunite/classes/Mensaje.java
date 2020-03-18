package com.example.reunite.classes;

public class Mensaje {
    private String texto;
    private int id;
    private int user_send;
    private int user_receive;

    public Mensaje(String texto) {
        this.texto = texto;
    }

    public String getMensajeTexto() {
        return this.texto;
    }
}
