package com.appynew.activities;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appynews.adapter.NoticiasAdapter;
import com.appynews.utils.ConnectionUtils;
import com.appynews.utils.GetInputStreamNewsConnectionTask;
import com.appynews.utils.MessageUtils;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.PermissionsUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import material.oscar.com.materialdesign.R;



/**
 * Clase MainActivity que lanza el Activity Principal
 * @author oscar
 *
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private int PERMISSION_ACCESS_STATE_PHONE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        /** carga del adapter **/

        List<Noticia> noticias = new ArrayList<Noticia>();
        Noticia n1 = new Noticia();
        n1.setAutor("Óscar");
        n1.setDescripcion("Google busca nuevos talentos");
        n1.setFechaPublicacion("12/06/2015");
        noticias.add(n1);


        Noticia n2 = new Noticia();
        n2.setAutor("Óscar");
        n2.setDescripcion("El ganador del Rallye de Naron ha sido Alberto Meira");
        n2.setFechaPublicacion("14/06/2015");
        noticias.add(n2);


        Noticia n3 = new Noticia();
        n3.setAutor("Óscar");
        n3.setDescripcion("Noticia 2");
        n3.setFechaPublicacion("14/06/2015");
        noticias.add(n3);

        Noticia n4 = new Noticia();
        n4.setAutor("Óscar");
        n4.setDescripcion("Noticia 2");
        n4.setFechaPublicacion("14/06/2015");
        noticias.add(n4);

        Noticia n5 = new Noticia();
        n5.setAutor("Óscar");
        n5.setDescripcion("El Depor ha fichado a Gaika Garitano como entrenador");
        n5.setFechaPublicacion("14/06/2015");
        noticias.add(n5);


        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);


        if(!PermissionsUtil.appTienePermiso(this, Manifest.permission.ACCESS_NETWORK_STATE)) {
            // Se comprueba si la app tiene permiso de acceso al estado de red del dispositivo, sino
            // se dispone del permiso, entonces se informa al usuario
            MessageUtils.showToastDuracionLarga(getApplicationContext(),getString(R.string.err_permisssion_network_state));
        } else {

            if (!ConnectionUtils.conexionRedHabilitada(this)) {
                // Si el dispositivo no tiene habilitado ninguna conexión de red, hay que informar al usuario
                MessageUtils.showToastDuracionLarga(getApplicationContext(),getString(R.string.err_connection_state));

            } else {

                GetInputStreamNewsConnectionTask conIs = new GetInputStreamNewsConnectionTask();
                conIs.execute("http://feeds.feedburner.com/ElLadoDelMal?format=xml");
                InputStream is = null;

                try {
                    is = conIs.get();

                    if(is!=null) {
                        MessageUtils.showToastDuracionLarga(this.getApplicationContext(),"Hay conexion con el otrolado");
                    } else {
                        MessageUtils.showToastDuracionLarga(this.getApplicationContext(),"SORRY no Hay conexion con el otrolado");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }



                // Crear un nuevo adaptador
                adapter = new NoticiasAdapter(noticias);
                recycler.setAdapter(adapter);

            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Método que comprueba si el dispositivo tiene acceso a internet, bien sea por WIFI o por Datos móviles
     * Para ello habrá que añadir un permiso extra en el AndroidManifest.xml
     * @return True si hay permiso y false en caso contrario
     */
    public boolean isOnline(){
        boolean exito = false;


        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo!=null && netInfo.isConnected()) exito = true;

        return exito;
    }


/**
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_STATE_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    */

}
