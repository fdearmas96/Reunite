package com.example.reunite.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.reunite.classes.GuardarUsuario;
import com.example.reunite.R;
import com.example.reunite.classes.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//Para probar

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistroUsuarioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistroUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroUsuarioFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText registro_usuario, registro_contrasena, registro_nombre, registro_apellido, registro_correo;
    Button registro_btn_registrar;

    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public RegistroUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroUsuarioFragment newInstance(String param1, String param2) {
        RegistroUsuarioFragment fragment = new RegistroUsuarioFragment();
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
        View vista = inflater.inflate(R.layout.fragment_registro_usuario, container, false);

        registro_usuario = vista.findViewById(R.id.registro_usuario);
        registro_contrasena = vista.findViewById(R.id.registro_contrasena);
        registro_apellido = vista.findViewById(R.id.registro_apellido);
        registro_nombre = vista.findViewById(R.id.registro_nombre);
        registro_correo = vista.findViewById(R.id.registro_correo);
        registro_btn_registrar = vista.findViewById(R.id.registro_btn_registrar);
        request = Volley.newRequestQueue(getContext());
        registro_btn_registrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });



        return vista;
    }

    private void registrarUsuario() {

        //ver si el usuario está registrado en la base de datos
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Consultando");
        progreso.show();
        //String url = Utilidades.servidor + "/Reunite/RegistroUsuario.php?usuario=" + registro_usuario.getText().toString()
        String url = Utilidades.WsRegistroUsuario + "usuario=" + registro_usuario.getText().toString()
                + "&pass=" + registro_contrasena.getText().toString()+"&usuario_nombre="+registro_nombre.getText().toString()+
                "&usuario_apellido=" + registro_apellido.getText().toString() + "&usuario_correo="+registro_correo.getText().toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), "No se pudo conectar" + error.toString(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        //esto muestra la respuesta entera:
        Toast.makeText(getContext(),"Mensaje: " +response, Toast.LENGTH_SHORT).show();

        JSONArray json = response.optJSONArray("registro");
        JSONObject jsonObject = null;
        try {
            jsonObject = json.getJSONObject(0);
            //todo correcto

            //Toast.makeText(getContext(),"Antes de hacer validaciones: " +jsonObject.optString("success"), Toast.LENGTH_SHORT).show();
            String success = jsonObject.optString("success");
            String mensaje = jsonObject.optString("message");



            // Toast.makeText(getContext(), "Antes de validaciones", Toast.LENGTH_SHORT).show();

            if (success.equals("0")) {

                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                dialogo.setTitle("Registro");
                dialogo.setMessage(mensaje);
                progreso.hide();
                dialogo.show();
                //Toast.makeText(getContext(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                //guardarLoguin();
                Context context = getContext();
                String userGuar = registro_usuario.getText().toString();
                String passGuard = registro_contrasena.getText().toString();
               GuardarUsuario guardarUsuario = new GuardarUsuario();
               guardarUsuario.GuardarUsuario(userGuar,passGuard,context);



            }
            //Contraseña incorrecta
            if (success.equals("1")) {
                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                dialogo.setTitle("Atención");
                dialogo.setMessage(mensaje);
                progreso.hide();
                dialogo.show();


                //Toast.makeText(getContext(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
            }

            //Toast.makeText(getContext(), "despues de validaciones" + entre +"-"+success, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
