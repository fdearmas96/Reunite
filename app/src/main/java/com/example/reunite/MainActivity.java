package com.example.reunite;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.reunite.classes.ConsultaUsuarioLogueado;
import com.example.reunite.classes.LoguinFragment;
import com.example.reunite.classes.Publicacion;
import com.example.reunite.classes.Utilidades;
import com.example.reunite.fragments.ListaPublicacionesFragment;
import com.example.reunite.fragments.MiListaPublicacionesFragment;
import com.example.reunite.fragments.NuevaPublicacionFragment;
import com.example.reunite.fragments.PublicacionFragment;
import com.example.reunite.fragments.RegistroUsuarioFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ImplementsFragments, Response.ErrorListener, Response.Listener<JSONObject>, IComunicaFragments {

    ProgressDialog progreso;
    Fragment miLoguin = null;
    Fragment mifragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Fragment fragment = new ChatFerFragment();
                llamar_fragment(fragment);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //Para llamar a la pantalla de loguin
        //habria que ver si ya no está loqueado
        pedirPermisos();
        Log.i("****123user pass", "antes de cargar loguin");
        cargarloguin();

    }

    private void cargarloguin() {
        //SharedPreferences preferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        //String user = preferences.getString("user","");
        //String pass = preferences.getString("pass","");
        String user;
        String pass;
        ConsultaUsuarioLogueado user1 = new ConsultaUsuarioLogueado();
        user = user1.getUser(this);
        ConsultaUsuarioLogueado pass1 = new ConsultaUsuarioLogueado();
        pass = pass1.getPass(this);
        Log.i("****123user pass", user +' ' + pass);
        //veo si hay valores y si hay veo si está registrado el usuario
        if (user.equals("") ){
            miLoguin = new LoguinFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, miLoguin).commit();
        }else{
            //ver si el usuario está registrado en la base de datos
            RequestQueue request = Volley.newRequestQueue(this);
            progreso = new ProgressDialog(this);
            progreso.setMessage("Consultando");
            progreso.show();
            //String url = Utilidades.servidor + "/Reunite/loguin.php?usuario=" + user
            //        + "&pass=" + pass;
            String url = Utilidades.WsLoguin + "usuario=" + user
                    + "&pass=" + pass;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            request.add(jsonObjectRequest);
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(this, "No se pudo conectar" + error.toString(), Toast.LENGTH_SHORT).show();
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
            if (jsonObject.optString("success").equals("0")) {
                Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                inicio();

            }else{
                Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                miLoguin = new LoguinFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, miLoguin).commit();
            }/*
            //Contraseña incorrecta
            if (jsonObject.optString("success") == "1") {
                Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                miLoguin = new LoguinFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, miLoguin).commit();
            }
            //Usuario no registrado
            if (jsonObject.optString("success") == "2") {
                Toast.makeText(this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                miLoguin = new LoguinFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, miLoguin).commit();
            }*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void inicio() {
        //voy a la pantalla de inicio
        Fragment miFragmentinicio = null;
        miFragmentinicio = new ListaPublicacionesFragment();//InicioFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,miFragmentinicio).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Fragment mifragment = null;
        boolean fragmentSeleccionado = false;
        if (id == R.id.nav_home) {
            // Handle the camera action
            mifragment = new ListaPublicacionesFragment();
            fragmentSeleccionado  = true;

        } else if (id == R.id.nav_publicaciones) {
            mifragment = new ListaPublicacionesFragment();
            fragmentSeleccionado  = true;

        } else if (id == R.id.nav_publicacion) {
            mifragment = new MiListaPublicacionesFragment();
            fragmentSeleccionado  = true;

        } else if (id == R.id.nav_nueva_publicacion) {
            mifragment = new NuevaPublicacionFragment();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_loguin) {
            mifragment = new LoguinFragment();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_nuevo_usuario) {
            mifragment = new RegistroUsuarioFragment();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_send) {

        }

        if(fragmentSeleccionado){
            llamar_fragment (mifragment);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    ////////**************************************PERMISOS***********************************/////////////////

    private boolean pedirPermisos() {
        //Estilo viejo se toman los permisos por el manifest
        //Toast.makeText(this, "Antes de los permisos", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        //& (checkSelfPermission(getContext(),WRITE_EXTERNAL_STORAGE)){
        if (
                (ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, INTERNET) == PackageManager.PERMISSION_GRANTED)
        )
        {
            //Toast.makeText(this, "ya tiene permisos", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (
                (shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))||
                (shouldShowRequestPermissionRationale(INTERNET))
        ){
            cargarDialogoRecomendacion();

        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},100);
        }

        return false;
    }
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Permisos desactivados");
        dialogo.setMessage("Debe aceptar los permisos");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA,INTERNET},100);
            }
        });
        dialogo.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100){
            if (grantResults.length==2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

            }

        }

    }

    @Override
    public void enviarPublicacion(Publicacion publicacion) {
        mifragment = new PublicacionFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto", publicacion);
        mifragment.setArguments(bundleEnvio);

        //Cargo el fragment
        llamar_fragment (mifragment);

    }
    private void llamar_fragment(Fragment mifragment) {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.content_main,mifragment).addToBackStack(null).commit();//.addToBackStack(null) el .add no estaba
    }
}
