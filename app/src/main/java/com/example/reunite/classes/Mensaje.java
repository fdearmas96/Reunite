package com.example.reunite.classes;

public class Mensaje {
    private String texto;
    private int id;
    private String user_send;
    private String user_receive;

    public Mensaje(String texto, String user_receive, String user_send) {
        this.texto = texto;
        this.user_receive = user_receive;
        this.user_send = user_send;
    }

    public String getMensajeTexto() {
        return this.texto;
    }
}
