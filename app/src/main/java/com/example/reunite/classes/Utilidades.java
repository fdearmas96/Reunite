package com.example.reunite.classes;

import java.util.Date;

public class Utilidades {
    //Ip de api
    //public static String servidor = "http://192.168.1.106:8080/"; //"http://192.168.1.108:8080/";
    public static String servidor = "http://reunite.hol.es/";

    public static String WsConsultarPublicacion = servidor +"consultarPublicacion.php?";
    public static String WsLoguin = servidor + "loguin.php?";
    public static String WsNuevaPublicacion = servidor + "nuevaPublicacion.php?";
    public static String WsObtenerPublicaciones = servidor + "obtenerPublicaciones.php?";




    public static String Tabla_Pub = "publicacion";
    public static String Publicacion_Id = "Pub_ID";
    public static String Pub_Titulo = "Pub_Titulo"; //Titulo
    public static String Pub_Desc = "Pub_Desc"; //Descripción
    public static String Pub_fecha = "Pub_fecha";  //fecha de publicación
    public static String Pub_img = "Pub_img"; //Dirección de la foto
    public static String Pub_contacto = "Pub_Contacto";//contacto

    public static float AnchoImagen = 600;
    public static float AltoImagen = 800;

}
