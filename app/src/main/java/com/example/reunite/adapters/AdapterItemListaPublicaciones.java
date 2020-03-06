package com.example.reunite.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.reunite.classes.Publicacion;
import com.example.reunite.R;
import com.example.reunite.classes.Utilidades;
import com.example.reunite.fragments.ListaPublicacionesFragment;

import java.util.ArrayList;
import java.util.Vector;

public class AdapterItemListaPublicaciones
        extends RecyclerView.Adapter<AdapterItemListaPublicaciones.ViewHolder>
        implements View.OnClickListener
{
    private LayoutInflater inflador;
    protected ArrayList<Publicacion> vectorPublicaciones; // Datos a visualizar
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

//    public AdapterItemListaPublicaciones(@NonNull ViewGroup parent, Vector<Publicacion> publicaciones) {
//        inflador = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.vectorPublicaciones = publicaciones;
//    }

    public AdapterItemListaPublicaciones(Context context, ArrayList<Publicacion> publicaciones) {
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.vectorPublicaciones = publicaciones;
        this.context=context;
        request = Volley.newRequestQueue(context);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pubTitulo;
        public ImageView pubImagen;

        public ViewHolder(View itemView) {
            super(itemView);
            pubTitulo = (TextView) itemView.findViewById(R.id.pubTitulo);
            pubImagen = (ImageView) itemView.findViewById(R.id.pubImagen);

        }
    }

    @Override // Creo la vista sin personalizar
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.item_lista_publicaciones, null);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override // Personalizo el ViewHolder
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Publicacion publicacion = vectorPublicaciones.get(posicion);
        holder.pubTitulo.setText(publicacion.getPub_Titulo());
        if (publicacion.getRuta_imagen()!=null) {//Si hay una ruta
            String url_imagen = Utilidades.servidor +  publicacion.getRuta_imagen();
            cargarWebServiceImagen(url_imagen, holder);

        }

    }
    private void cargarWebServiceImagen(String url_imagen, final ViewHolder holder) {
        url_imagen = url_imagen.replace(" ", "%20");//para quitar espacios
        //Toast.makeText(context,  " Busco imagen " + url_imagen , Toast.LENGTH_SHORT).show();
        ImageRequest imageRequest = new ImageRequest(url_imagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {


                response = Bitmap.createScaledBitmap(response,600,800,true);
                holder.pubImagen.setImageBitmap(response);
                //ListaPublicacionesFragment.this.publicacion.setPub_img(response);
                //Toast.makeText(context,  "agrego imagen "  , Toast.LENGTH_SHORT).show();

            }
        },


                0, 0, ImageView.ScaleType.CENTER, null,


                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context,  "Error al cargar la imagen "  , Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);

    }

    @Override // Indico el número de elementos de la lista
    public int getItemCount() {

        return vectorPublicaciones.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

}

