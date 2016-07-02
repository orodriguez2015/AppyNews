package com.appynew.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
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
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.appynews.adapter.NoticiasAdapter;
import com.appynews.asynctasks.GetNewsRssSourceTask;
import com.appynews.com.appynews.controllers.NoticiaController;
import com.appynews.utils.ConnectionUtils;
import com.appynews.asynctasks.GetInputStreamNewsConnectionTask;
import com.appynews.utils.LogCat;
import com.appynews.utils.LruBitmapCache;
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
    //private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private int PERMISSION_ACCESS_STATE_PHONE = 1;
    private NoticiaController noticiaController = new NoticiaController(this);
    private ImageLoader imageLoader = null;


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


        /** Añadir elementos al menú de forma dinámica
        Menu menu = navigationView.getMenu();

        menu.add(Menu.CATEGORY_SYSTEM, 4, Menu.CATEGORY_SYSTEM, "Opcion1")
                .setIcon(android.R.drawable.ic_menu_preferences);

        menu.add(Menu.NONE, 5, Menu.NONE, "Opcion2")
                .setIcon(android.R.drawable.ic_menu_compass);


        SubMenu submenu = menu.a
         ddSubMenu(Menu.CATEGORY_SYSTEM);
        submenu.add(Menu.NONE, 5, Menu.NONE, "Opcion3").setIcon(android.R.drawable.ic_menu_compass);
        */
        ArrayList<Noticia> noticias = new ArrayList<Noticia>();


        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);


        /**
        recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                MessageUtils.showToastDuracionLarga(getApplicationContext(),"onInterceptTouchEvent");
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                MessageUtils.showToastDuracionLarga(getApplicationContext(),"ontTouchEvent");
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                MessageUtils.showToastDuracionLarga(getApplicationContext(),"onRequestDisallowIntercept");
            }
        });
        **/

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

                RequestQueue requestQueau = Volley.newRequestQueue(this);
                // Se crea para la cola una caché de 10 imágenes
                this.imageLoader = new ImageLoader(requestQueau,new LruBitmapCache());


                // Carga inicial de noticias de un determinado origen
                //cargarNoticias("http://feeds.feedburner.com/ElLadoDelMal?format=xml","El otro lado del mal");
                cargarNoticias("https://www.meneame.net/rss","Menéame");
            }
        }
    }


    /**
     * Método que carga las noticias de una determinada url
     * @param url: String que contiene la url del orígen de datos RSS
     * @param origen: String que contiene el nombre del orígen de datos RSS
     */
    private void cargarNoticias(String url,String origen) {

        // Se recupera la lista de noticias a través
        final List<Noticia> noticias = noticiaController.getNoticias(url);
        NoticiasAdapter adapter =  new NoticiasAdapter(noticias,origen,imageLoader,getResources());

        /**
         * Se establece el listener que se pasa al adapter para que añade
         * este Listener a cada View a mostrar en el RecyclerView
         */
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos =  recycler.getChildAdapterPosition(view);
                cargarActivityDetalleNoticia(noticias.get(pos));
            }
        });

        recycler.setAdapter(adapter);
    }


    private void cargarActivityDetalleNoticia(Noticia noticia) {

        // Se pasa la noticia seleccionada al Activity que mostrará la descripción, en este caso, ActividadDescripcionNoticia
        Intent intent = new Intent(MainActivity.this, DetalleNoticiaActivity.class);

        // Se recupera la noticia seleccionada de la colección de noticias, en base al parámetro con la posición

        // Origen de la noticia que está en el atributo origenNoticias

        intent.putExtra("noticia",noticia);
        startActivity(intent);

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


        LogCat.debug(" ================> Se ha selecciona el elemento del menú con id: " + id);

        if (id == R.id.meneame) {
            cargarNoticias("https://www.meneame.net/rss","Menéame");

        } else if (id == R.id.elotrolado) {
            cargarNoticias("http://feeds.feedburner.com/ElLadoDelMal?format=xml","El otro lado del mal");

        } else if (id == R.id.seguridadapple) {
            cargarNoticias("http://feeds.feedburner.com/Seguridadapple?format=xml","Seguridad Apple");

        } else if(id==R.id.expansion) {
            cargarNoticias("http://estaticos.expansion.com/rss/portada.xml","Expansión");
        }


        /*
        else if(id==R.id.applesfera) {
            cargarNoticias("http://feeds.weblogssl.com/genbetadev?format=xml");
        }
        /*else if (id == R.id.applesfera) {
            cargarNoticias("http://feeds.weblogssl.com/applesfera");
        }*/

        /*
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
