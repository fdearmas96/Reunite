package com.example.reunite;

import com.example.reunite.classes.Comentario;
import com.example.reunite.classes.Publicacion;

public interface IComunicaFragments {

    public void enviarPublicacion(Publicacion publicacion);
    public void enviarComentario(Comentario comentario);
}
