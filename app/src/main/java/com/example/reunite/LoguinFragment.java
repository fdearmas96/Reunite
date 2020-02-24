package com.example.reunite;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoguinFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoguinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoguinFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText loguin_user, loguin_pass;
    Button loguin_ingresar;

    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public LoguinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoguinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoguinFragment newInstance(String param1, String param2) {
        LoguinFragment fragment = new LoguinFragment();
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
        View vista = inflater.inflate(R.layout.fragment_loguin, container, false);

        loguin_user = vista.findViewById(R.id.loguin_user);
        loguin_pass = vista.findViewById(R.id.loguin_pass);
        loguin_ingresar = vista.findViewById(R.id.loguin_ingresar);
        request = Volley.newRequestQueue(getContext());
        loguin_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresar();
            }
        } );
        return vista;
    }


    private View.OnClickListener ingresar() {
        //ver si el usuario está registrado en la base de datos
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Consultando");
        progreso.show();
        String url = Utilidades.servidor + "/Reunite/loguin.php?usuario=" + loguin_user.getText().toString()
                + "&pass=" + loguin_pass.getText().toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
        return null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), "No se pudo conectar" + error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("Error", error.toString());
    }
    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        //esto muestra la respuesta entera:
        //Toast.makeText(getContext(),"Mensaje: " +response, Toast.LENGTH_SHORT).show();

        JSONArray json = response.optJSONArray("loguin");
        JSONObject jsonObject = null;
        try {
            jsonObject = json.getJSONObject(0);
            //todo correcto

            //Toast.makeText(getContext(),"Antes de hacer validaciones: " +jsonObject.optString("success"), Toast.LENGTH_SHORT).show();
            String success = jsonObject.optString("success");
            Integer succesInt = 2;
            String mensaje = jsonObject.optString("message");
            String entre = "nada";
            

           // Toast.makeText(getContext(), "Antes de validaciones", Toast.LENGTH_SHORT).show();

            if (success.equals("0")) {
                entre ="0";
                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                dialogo.setTitle("Atención");
                dialogo.setMessage(mensaje);
                progreso.hide();
                dialogo.show();
                //Toast.makeText(getContext(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                //guardarLoguin();

            }
            //Contraseña incorrecta
            if (success.equals("1")) {
                entre ="1";
                entre ="0";
                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                dialogo.setTitle("Atención");
                dialogo.setMessage(mensaje);
                progreso.hide();
                dialogo.show();


                //Toast.makeText(getContext(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
            }
            //Usuario no registrado
            if (success.equals("2")) {
                entre ="2";


                entre ="0";
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

    private void guardarLoguin() {
        SharedPreferences preferences = getContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String usuario = loguin_user.getText().toString().trim();
        String contrasena = loguin_pass.getText().toString().trim();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", usuario);
        editor.putString("pass", contrasena);
        editor.commit();

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
