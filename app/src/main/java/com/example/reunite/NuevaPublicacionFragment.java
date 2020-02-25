package com.example.reunite;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;
//Comentario de prueba

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NuevaPublicacionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NuevaPublicacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NuevaPublicacionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    EditText nueva_P_titulo;
    ImageView nueva_P_imagen;
    EditText nueva_P_descripcion;
    EditText nueva_P_Contacto;
    Button nueva_P_btn_Publicar;
    Button nueva_P_btn_imagen;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private final String CARPETA_RAIZ = "misImagenesReunite/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";
    final int COD_SELECCIONA = 10;
    final int COD_CAPTURA = 20;
    String path = "";


    public NuevaPublicacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NuevaPublicacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NuevaPublicacionFragment newInstance(String param1, String param2) {
        NuevaPublicacionFragment fragment = new NuevaPublicacionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_nueva_publicacion, container, false);

        /*
    EditText nueva_P_titulo;
    ImageView nueva_P_imagen;
    EditText nueva_P_descripcion;
    EditText nueva_P_Contacto;
    Button nueva_P_btn_Publicar;
    Button nueva_P_btn_imagen;

         */
        nueva_P_titulo = vista.findViewById(R.id.nueva_P_titulo);
        nueva_P_imagen = vista.findViewById(R.id.nueva_P_imagen);
        nueva_P_descripcion = vista.findViewById(R.id.nueva_P_descripcion);
        nueva_P_Contacto = vista.findViewById(R.id.nueva_P_Contacto);
        nueva_P_btn_Publicar = vista.findViewById(R.id.nueva_P_btn_Publicar);
        nueva_P_btn_imagen = vista.findViewById(R.id.nueva_P_btn_imagen);


        request = Volley.newRequestQueue(getContext());
        nueva_P_btn_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargar_imagen();
            }
        });
        nueva_P_btn_Publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarWebService();
            }
        });

        pedirPermisos();
        return vista;
    }

    private void llamarWebService() {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();
        String url = Utilidades.servidor + "/Reunite/loguin.php?usuario=
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    private void cargar_imagen() {

        final CharSequence[] opciones = {"Tomar foto", "Cargar imagen", "Cancelar"};
        final AlertDialog.Builder alterOpciones = new AlertDialog.Builder(getContext());
        alterOpciones.setTitle("Seleccione una opción.");
        alterOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar foto")) {
                    tomarFotografia();
                } else {
                    if (opciones[i].equals("Cargar imagen")) {
                        //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//trae app para cargar una imagen
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), COD_SELECCIONA);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }

            private void tomarFotografia() {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
                File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
                boolean isCreada = fileImagen.exists();
                String nombreImagen = "";

                if (isCreada == false) {
                    isCreada = fileImagen.mkdirs();

                }
                if (isCreada == true) {
                    nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
                }
                //Abre la ruta
                path = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN + File.separator + nombreImagen;

                File imagen = new File(path);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //lANZA app camara
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
                startActivityForResult(intent, COD_CAPTURA);
            }
        });
        alterOpciones.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case COD_SELECCIONA:
                    Uri miPath = data.getData();
                    nueva_P_imagen.setImageURI(miPath);
                    break;
                case COD_CAPTURA:
                    MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String s, Uri uri) {
                                    Log.i("Ruta de almacenamiento", "Path :" + path);

                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    nueva_P_imagen.setImageBitmap(bitmap);
                    break;


            }


        }
    }

    private boolean pedirPermisos() {
        //Estilo viejo se toman los permisos por el manifest
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        //& (checkSelfPermission(getContext(),WRITE_EXTERNAL_STORAGE)){
        if ((checkSelfPermission(getContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))
        {
            return true;
        }

        if ( (shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) ){
            cargarDialogoRecomendacion();

        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},100);
        }

        return false;
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos desactivados");
        dialogo.setMessage("Debe aceptar los permisos");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},100);
            }
        });
        dialogo.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100){
            if (grantResults.length==2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                nueva_P_btn_imagen.setEnabled(true);
            }

        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
