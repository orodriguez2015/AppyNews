package com.appynew.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.appynew.activities.dialog.AlertDialogHelper;
import com.appynew.activities.dialog.BtnAceptarDialogGenerico;
import com.appynews.adapter.NoticiasAdapter;
import com.appynews.asynctasks.DeleteNoticiaAsyncTask;
import com.appynews.asynctasks.GetOrigenesRssAsyncTask;
import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.asynctasks.RespuestaAsyncTask;
import com.appynews.asynctasks.SaveUsuarioAsyncTask;
import com.appynews.controllers.NoticiaController;
import com.appynews.database.helper.DatabaseErrors;
import com.appynews.model.dto.DatosUsuarioVO;
import com.appynews.model.dto.Noticia;
import com.appynews.model.dto.OrigenNoticiaVO;
import com.appynews.utils.ConnectionUtils;
import com.appynews.utils.LogCat;
import com.appynews.utils.LruBitmapCache;
import com.appynews.utils.MessageUtils;
import com.appynews.utils.PermissionsUtil;
import com.appynews.utils.StringUtil;
import com.appynews.utils.TelephoneUtil;
import com.appynews.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import material.oscar.com.materialdesign.R;

import static material.oscar.com.materialdesign.R.drawable.ic_menu_delete2;



/**
 * Clase MainActivity que lanza el Activity Principal
 * @author oscar
 *
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recycler;
    private RecyclerView.LayoutManager lManager;
    private NoticiaController noticiaController = new NoticiaController(this);
    private ImageLoader imageLoader = null;
    private NoticiasAdapter noticiaAdapter = null;
    private Paint p = new Paint();


    /**
     * Colección con los origenes de datos RSS de los que se van a leer noticias
     */
    private List<OrigenNoticiaVO> fuentesDatos = null;


    /**
     * Contiene los datos y url de un origen RSS del que se obtendrá sus noticia
     */
    private HashMap<Integer,OrigenNoticiaVO> origenes = new HashMap<Integer,OrigenNoticiaVO>();

    /**
     * Nombre del origen de las noticias que se están listando actualmente en el RecyclerView
     */
    private String nombreOrigenNoticiasRecuperadas = null;


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

        // Rellenar el menú con las fuentes de datos
        rellenarMenu(navigationView);

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);


        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);



        // Se inicializa el listener para el swipe
        initSwipe();


        // Si se dispone de permiso para leer el estado del teléfono, se obtiene datos como el número, imei, etc ...
        if(PermissionsUtil.appTienePermiso(this,Manifest.permission.READ_PHONE_STATE)) {
            // Recopilación de datos del dispositivo
            DatosUsuarioVO datosTelefono = TelephoneUtil.getInfoDispositivo(getBaseContext());
            LogCat.debug(" datos del telefono: " + datosTelefono.toString());
        }


        if(!PermissionsUtil.appTienePermiso(this, Manifest.permission.ACCESS_NETWORK_STATE)) {
            // Se comprueba si la app tiene permiso de acceso al estado de red del dispositivo, sino
            // se dispone del permiso, entonces se informa al usuario
            MessageUtils.showToastDuracionLarga(getApplicationContext(),getString(R.string.err_permisssion_network_state));
        } else {

            if (!ConnectionUtils.conexionRedHabilitada(this)) {
                // Si el dispositivo no tiene habilitado ninguna conexión de red, hay que informar al usuario
                MessageUtils.showToastDuracionLarga(getApplicationContext(),getString(R.string.err_connection_state));

            } else {



                /************************************************************/
                /**** Se almacenan los datos del dispositivo en la BBDD *****/
                /************************************************************/
                try {
                    ParametrosAsyncTask params = new ParametrosAsyncTask();
                    params.setContext(this.getApplicationContext());
                    params.setUsuario(TelephoneUtil.getInfoDispositivo(getApplicationContext()));
                    SaveUsuarioAsyncTask asyncTask = new SaveUsuarioAsyncTask();
                    asyncTask.execute(params);

                    RespuestaAsyncTask res = asyncTask.get();
                    if(res.getStatus()==0) {
                        LogCat.debug("Datos del teléfono/usuario grabados en BBDD");
                    } else {
                        LogCat.error("Se ha producido un error al grabar el teléfono/usuario grabados en BBDD ".concat(res.getDescStatus()));
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LogCat.error("Error al ejecutar tarea asíncrona de grabación de usuario en BD: ".concat(e.getMessage()));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    LogCat.error("Error al ejecutar tarea asíncrona de grabación de usuario en BD: ".concat(e.getMessage()));
                }


                RequestQueue requestQueau = Volley.newRequestQueue(this);
                // Se crea para la cola una caché de 10 imágenes
                this.imageLoader = new ImageLoader(requestQueau,new LruBitmapCache());

                // Carga inicial de noticias de un determinado origen
                cargarFavoritas();
            }
        }
    }


    /**
     * Operación que obtiene los datos de orígenes de datos rss y configurar el menú
     * para acceder a cada origen de datos
     * @param navigationView: NavigationView en el que se añaden los menús
     */
    private void rellenarMenu(NavigationView navigationView) {
        boolean continuar = false;

        try {

            ParametrosAsyncTask params = new ParametrosAsyncTask();
            params.setContext(getBaseContext());

            GetOrigenesRssAsyncTask task = new GetOrigenesRssAsyncTask();
            task.execute(params);
            RespuestaAsyncTask res = task.get();

            if(res.getStatus().equals(DatabaseErrors.OK)) {
                this.fuentesDatos = res.getOrigenes();
                continuar = true;

            } else {
                MessageUtils.showToastDuracionLarga(getApplicationContext(),getString(R.string.err_get_fuentes_datos));
            }

        } catch(Exception e) {
            e.printStackTrace();
            MessageUtils.showToastDuracionLarga(getApplicationContext(),getString(R.string.err_get_fuentes_datos));
        }


        // Si se han recuperado las fuentes de datos, se rellena el menú de la aplicación con l
        if(continuar) {
            Menu menu = navigationView.getMenu();

            for(int i=0;fuentesDatos!=null && i<fuentesDatos.size();i++) {
                MenuItem menuItem = menu.add(Menu.NONE, fuentesDatos.get(i).getId(), Menu.NONE, fuentesDatos.get(i).getNombre()).setIcon(android.R.drawable.ic_menu_compass);
            }
        }

    }


    /**
     * Devuelve true si las noticias que se están visualizando proceden de un origen externo
     * @return boolean
     */
    private boolean isOrigenExterno() {
        boolean exito = false;

        if(StringUtil.isNotEmpty(this.nombreOrigenNoticiasRecuperadas)) {
            exito = true;
        }
        return exito;
    }


    /**
     * Devuelve el nombre del origen del cual se están cargando noticias
     * @return String
     */
    private String getOrigenExterno() {
        return this.nombreOrigenNoticiasRecuperadas;
    }




    /**
     * Método que carga las noticias de una determinada url
     * @param url String que contiene la url del orígen de datos RSS
     * @param origen String que contiene el nombre del orígen de datos RSS
     */
    private void cargarNoticias(String url,String origen) {

        this.nombreOrigenNoticiasRecuperadas = origen;

        // Se recupera la lista de noticias a través
        final List<Noticia> noticias = noticiaController.getNoticias(url);
        noticiaAdapter =  new NoticiasAdapter(noticias,origen,imageLoader,getResources());
        noticiaAdapter.notifyDataSetChanged();
        /**
         * Se establece el listener que se pasa al adapter para que añade
         * este Listener a cada View a mostrar en el RecyclerView
         */
        noticiaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos =  recycler.getChildAdapterPosition(view);
                cargarActivityDetalleNoticia(noticias.get(pos));
            }
        });

        recycler.setAdapter(noticiaAdapter);
    }


    /**
     * Carga las noticias de la base de datos
     */
    private void cargarFavoritas() {

        final List<Noticia> favoritas = noticiaController.getNoticiasFavoritas(getApplicationContext());
        noticiaAdapter =  new NoticiasAdapter(favoritas,null,imageLoader,getResources());
        noticiaAdapter.notifyDataSetChanged();

        if(favoritas==null || favoritas.size()==0) {
            MessageUtils.showToastDuracionCorta(this,getString(R.string.msg_no_hay_noticias_favoritas));
        }


        for(int i=0;favoritas!=null && i<favoritas.size();i++) {

            LogCat.debug("autor: " + favoritas.get(i).getAutor());
            LogCat.debug("titulo: " + favoritas.get(i).getTitulo());
            LogCat.debug("url: " + favoritas.get(i).getUrl());
            LogCat.debug("url imagen: " + favoritas.get(i).getUrlThumbnail());
            LogCat.debug("");
        }



        /**
         * Se establece el listener que se pasa al adapter para que añade
         * este Listener a cada View a mostrar en el RecyclerView
         */
        noticiaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos =  recycler.getChildAdapterPosition(view);
                cargarActivityDetalleNoticia(favoritas.get(pos));
            }
        });

        recycler.setAdapter(noticiaAdapter);
    }

    /**
     * Este método pasa el control del activity actual al activity DetalleNoticiaActivity.
     * @param noticia Objeto de la clase Noticia que se pasa a la actividad DetalleNoticiaActivity
     */
    private void cargarActivityDetalleNoticia(Noticia noticia) {

        // Se pasa la noticia seleccionada al Activity que mostrará la descripción, en este caso, ActividadDescripcionNoticia
        Intent intent = new Intent(MainActivity.this, DetalleNoticiaActivity.class);
        if(isOrigenExterno()) { // Si se cargan las noticias de un origen externo, se establece el nombre del origen en la noticia
            noticia.setOrigen(getOrigenExterno());
        }
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


    /**
     * Operación que es invocada cuando el usuario selecciona un determinado ítem del menú
     * @param item: MenuItem
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Id del item de menú seleccionado por el usuario
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Detecta el item de menú que ha seleccionado el usuario
     * @param item: MenuItem seleccionado por el usuario
     * @return boolean
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        LogCat.debug(" ================> Se ha selecciona el elemento del menú con id: " + id);

        switch(item.getItemId()) {
            case R.id.favoritos:
                // Se recuperan las noticias grabadas en la base de datos
                cargarFavoritas();
                break;

            case R.id.nuevo_origen:
                // Dar de alta un nuevo origen
                MessageUtils.showToastDuracionCorta(getApplicationContext(),getString(R.string.err_accion_no_implementada));
                break;

            default:
                // Se recuperan las noticias del origen seleccionado por el usuario
                OrigenNoticiaVO origenSeleccionado = Utils.getFuenteDatos(fuentesDatos,id);
                cargarNoticias(origenSeleccionado.getUrl(),origenSeleccionado.getNombre());
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /*** prueba ***/

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    // Se recupera la noticia del adapter, y se dispone a borrarla de la BBDD
                    Noticia noticiaDelete = noticiaAdapter.getNoticia(position);

                    if(noticiaDelete.isNoticiaFavorita()) {
                        // Si la noticia procede de la BBDD, se procede a eliminarla
                        ParametrosAsyncTask params = new ParametrosAsyncTask();
                        params.setContext(getApplicationContext());
                        params.setNoticia(noticiaDelete);

                        try {
                            DeleteNoticiaAsyncTask task = new DeleteNoticiaAsyncTask();
                            task.execute(params);
                            RespuestaAsyncTask res = task.get();
                            if (res.getStatus() == DatabaseErrors.OK) {
                                noticiaAdapter.removeItem(position);
                            }

                        } catch (Exception e) {
                            AlertDialogHelper.crearDialogoAlertaSimple(MainActivity.this, getString(R.string.atencion),getString(R.string.err_borrar_noticia),new BtnAceptarDialogGenerico(),null);
                        }

                    }

                } else {

                    /*
                    removeView();
                    edit_position = position;
                    alertDialog.setTitle("Edit Country");
                    et_country.setText(countries.get(position));
                    alertDialog.show();
                    */
                }
                noticiaAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;


                    /** Si se hace swipe hacia la derecha
                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getBaseContext().getResources(), ic_menu_gallery);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                     **/
                    if(dX<=0) {
                        // El usuario ha hecho swipe hacia la izquierda
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), ic_menu_delete2);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                        c.drawText("Eliminar",500,1000,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
    }





}