package com.appynew.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.appynews.adapter.NoticiasAdapter;
import com.appynews.asynctasks.DeleteNoticiaAsyncTask;
import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.asynctasks.RespuestaAsyncTask;
import com.appynews.command.actions.GetNoticiasAction;
import com.appynews.command.api.ActividadPrincipalApi;
import com.appynews.controllers.OrigenRssController;
import com.appynews.database.helper.DatabaseErrors;
import com.appynews.dialog.AlertDialogHelper;
import com.appynews.exceptions.GetOrigenesRssException;
import com.appynews.model.dto.Noticia;
import com.appynews.model.dto.OrigenNoticiaVO;
import com.appynews.utils.ConstantesDatos;
import com.appynews.utils.ConstantesPermisos;
import com.appynews.utils.LogCat;
import com.appynews.utils.LruBitmapCache;
import com.appynews.utils.MessageUtils;
import com.appynews.utils.PermissionsUtil;
import com.appynews.utils.StringUtil;
import com.appynews.utils.Utils;

import java.util.List;

import material.oscar.com.materialdesign.R;

import static material.oscar.com.materialdesign.R.drawable.ic_menu_delete;


/**
 * Clase MainActivity que lanza el Activity Principal
 * @author <a href="mailto:oscar.rodriguezbrea@gmail.com">Óscar Rodríguez</a>
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ActividadPrincipalApi {

    private RecyclerView recycler;
    private RecyclerView.LayoutManager lManager;
    private OrigenRssController origenRssController = new OrigenRssController(this);
    private ImageLoader imageLoader = null;
    private NoticiasAdapter noticiaAdapter = null;
    private Paint p = new Paint();
    private NavigationView navigationView = null;
    private boolean mostrandoNoticiasExternas = false;
    private ProgressDialog progressDialog = null;
    /** Atributo que contiene la noticia de la que se va a ver en detalle **/
    private int posicionNoticiaSeleccionada = -1;
    /**
     * Colección con los origenes de datos RSS de los que se van a leer noticias
     */
    private List<OrigenNoticiaVO> fuentesDatos = null;
    /**
     * Nombre del origen de las noticias que se están listando actualmente en el RecyclerView
     */
    private String nombreOrigenNoticiasRecuperadas = null;


    private GetNoticiasAction noticiasAction = null;

    /**
     * Método que inicializa la actividad
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Botón flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // Evento onClick sobre el botón flotante
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivityNuevaFuenteDatos();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Rellenar el menú con las fuentes de datos
        rellenarMenu();

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        /*
         * Se inicializa el listener para el swipe
         */
        initSwipe();

        /*
         * Inicializar el ImageLoader
         */
        initializeImageLoader();


        /**
         * Se inicializa el Action que permite recuperar las noticias de una determinada
         * fuente de datos RSS
         */
        this.noticiasAction = new GetNoticiasAction(this);

        /*
         * Solicitar permisos
         */
        if(PermissionsUtil.solicitarPermisosAccesoEstadoTelefono(this)) {
            noticiasAction.cargarNoticiasFavoritas();
        }
    }


    /**
     * Devuelve lel Activity
     * @return MainActivity
     */
    @Override
    public MainActivity getActivity() {
        return this;
    }


    /**
     * Devuelve el objeto NoticiasAdapter que está utilizando la actividad MainActivity para listar las noticias
     * @return NoticiasAdapter
     */
    public NoticiasAdapter getNoticiasAdapter() {
        return this.noticiaAdapter;
    }


    /**
     * Devuelve el ImageLoader para cargar las imágenes asociadas a cada noticia
     * @return ImageLoader
     */
    public ImageLoader getImageLoader() {
        return this.imageLoader;
    }


    /**
     * Devuelve el RecyclerView
     * @return RecyclerView
     */
    public RecyclerView getRecyclerView() {
        return this.recycler;
    }


    /**
     * Devuelve el Context de la Actividad
     * @return Context
     */
    @Override
    public Context getContexto() {
        return getApplicationContext();
    }


    /**
     * Comprueba la respuesta de los usuarios a la concesión de permisos. Válido de Android 6.0 en adelante
     * @param requestCode int que representa al tipo de permiso
     * @param permissions Lista de permisos
     * @param grantResults Resultado en la asignación de permisos (concedido o no concedido)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ConstantesPermisos.PERMISSIONS_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    AlertDialogHelper.crearDialogoAlertaSimple(this,getString(R.string.no_autorizado), getString(R.string.err_permiso_estado_telefono), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }
                return;
            }// case


            case ConstantesPermisos.PERMISSIONS_ACCESS_NETWORK_STATE: {
                LogCat.debug("====> Procesando respuesta para permiso de acceso al estado de conexión de red del dispositivo");
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    LogCat.debug("====> Denegado permiso de acceso al estado de conexión de red del dispositivo");
                    AlertDialogHelper.crearDialogoAlertaConfirmacion(this, "Error", "AppyNews no dispone de permiso para comprobar el estado de red de su dispositivo, por tanto, no podrá continuar ejecutándose", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    },null).show();
                }
                return;
            }

        }
    }


    /**
     * Inicializa un ImageLoader de la librería Volley, que se utiliza para cargar los thumbnails asociados a cada notifica
     */
    private void initializeImageLoader() {
        RequestQueue requestQueau = Volley.newRequestQueue(getBaseContext());
        // Se crea para la cola una caché de 10 imágenes
        this.imageLoader = new ImageLoader(requestQueau, new LruBitmapCache());
    }


    /**
     * Operación que obtiene los datos de orígenes de datos rss y configurar el menú
     * para acceder a cada origen de datos
     */
    private void rellenarMenu() {
        Menu menu = navigationView.getMenu();
        boolean continuar = false;

        try {
            // Se vacía el menú de los orígenes de datos que ya tuviese, para proceder a dar de alta los nuevos
            vaciarMenu(menu);

            // Se recupera de la BD los orígenes/fuentes de datos de los que obtener las noticias
            this.fuentesDatos = origenRssController.getOrigenes();
            continuar = true;


        } catch(GetOrigenesRssException e) {
            e.printStackTrace();
            MessageUtils.showToastDuracionLarga(getApplicationContext(),getString(R.string.err_get_fuentes_datos));
        }

        // Si se han recuperado las fuentes de datos, se rellena el menú de la aplicación con l
        if(continuar) {

            for(int i=0;fuentesDatos!=null && i<fuentesDatos.size();i++) {
                menu.add(Menu.NONE, fuentesDatos.get(i).getId(), Menu.NONE, fuentesDatos.get(i).getNombre()).setIcon(android.R.drawable.ic_menu_compass);
            }
        }

    }


    /**
     * Vacía el menú de la aplicación de los orígenes de datos que tuviese cargados
     * @param menu Menu
     */
    private void vaciarMenu(Menu menu) {
        if(menu!=null && menu.size()>0) {
            // Se eliminan los antiguos orígenes/fuentes de datos para actualizar el menú
            for(int i=0;this.fuentesDatos!=null && i<this.fuentesDatos.size();i++) {
                menu.removeItem(fuentesDatos.get(i).getId());
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
     * Permite establecer el nombr
     * @param nombre
     */
    @Override
    public void setNombreOrigenFuenteDatos(String nombre) {
        this.nombreOrigenNoticiasRecuperadas = nombre;
    }


    /**
     * Establece el título de la actividad
     * @param title String
     */
    @Override
    public void setTitulo(String title) {
        setTitle(title);
    }

    /**
     * Devuelve los Resources
     * @return Resources
     */
    @Override
    public Resources getRecursos() {
        return getResources();
    }

    /**
     * Devuelve true si se están visualizando en este momento las noticias externas, o noticias
     * almacenadas por el usuario en la base de datos
     * @return boolean
     */
    @Override
    public boolean isMostrandoNoticiasExternas() {
        return mostrandoNoticiasExternas;
    }

    /**
     * Permite indicar si se están mostrando o no noticias externas
     * @param mostrandoNoticiasExternas boolean
     */
    @Override
    public void setMostrandoNoticiasExternas(boolean mostrandoNoticiasExternas) {
        this.mostrandoNoticiasExternas = mostrandoNoticiasExternas;
    }


    /**
     * Devuelve la posición de la noticia seleccionada por el usuario en la colección de noticias
     * @return int
     */
    public int getPosicionNoticiaSeleccionada() {
        return this.posicionNoticiaSeleccionada;
    }


    /**
     * Este método pasa el control del activity actual al activity DetalleNoticiaActivity.
     * @param noticia Objeto de la clase Noticia que se pasa a la actividad DetalleNoticiaActivity
     */
    @Override
    public void cargarActivityDetalleNoticia(Noticia noticia,int posicion) {

        this.posicionNoticiaSeleccionada = posicion;

        // Se pasa la noticia seleccionada al Activity que mostrará la descripción, en este caso, ActividadDescripcionNoticia
        Intent intent = new Intent(MainActivity.this, DetalleNoticiaActivity.class);
        if(isOrigenExterno()) { // Si se cargan las noticias de un origen externo, se establece el nombre del origen en la noticia
            noticia.setOrigen(getOrigenExterno());
        }
        intent.putExtra("noticia",noticia);
        startActivityForResult(intent,ConstantesDatos.RESPONSE_GRABACION_NOTICIA);
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
     * Detecta el item de menú que ha seleccionado el usuario
     * @param item: MenuItem seleccionado por el usuario
     * @return boolean
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        switch(item.getItemId()) {
            case R.id.favoritos:
                //command.execute();
                noticiasAction.cargarNoticiasFavoritas();
                break;

            case R.id.nuevo_origen:
                // Se muestra el activity desde el que se pueden dar de alta una nueva fuente de datos
                showActivityNuevaFuenteDatos();
                break;

            case R.id.mantenimiento_origen:
                showActivityMantenimientoFuentesDatos();
                break;

            default:
                // Se recuperan las noticias del origen seleccionado por el usuario
                OrigenNoticiaVO origenSeleccionado = Utils.getFuenteDatos(fuentesDatos,id);
                noticiasAction.cargarNoticias(origenSeleccionado.getUrl(),origenSeleccionado.getNombre());
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawers();
        return true;
    }


    /**
     * initSwipe
     */
    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                LogCat.debug("onSwiped ====>");
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT && !isMostrandoNoticiasExternas()){
                    // Se recupera la noticia del adapter, y se dispone a borrarla de la BBDD
                    Noticia noticiaDelete = noticiaAdapter.getNoticia(position);

                    //if(noticiaDelete.isNoticiaFavorita()) {
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
                            AlertDialogHelper.crearDialogoAlertaAdvertencia(MainActivity.this, getString(R.string.atencion),getString(R.string.err_borrar_noticia));
                        }

                    //}

                }

                noticiaAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                LogCat.debug("onChildDraw ====>");

                // Sino se están mostrando noticias externas, se permite hacer un swipe hacia la izquierda para eliminar la noticia de la base de datos
                if(!isMostrandoNoticiasExternas()) {
                    Bitmap icon;
                    if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE ){

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
                            icon = BitmapFactory.decodeResource(getResources(), ic_menu_delete);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                            c.drawBitmap(icon,null,icon_dest,p);
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler);
    }


    /**
     * Operación que carga el activity a través de la cual se puede dar de alta una nueva
     * fuente de datos RSS
     */
    private void showActivityNuevaFuenteDatos() {
        Intent intent = new Intent(MainActivity.this, NuevaFuenteDatosActivity.class);
        startActivityForResult(intent, ConstantesDatos.RESPONSE_NUEVA_FUENTE_DATOS);
    }


    /**
     * Método que muestra el activity desde el que se realiza el mantenimiento de las fuentes
     * de dtos
     */
    private void showActivityMantenimientoFuentesDatos() {
        Intent intent = new Intent(MainActivity.this,OrigenRssMantenimientoActivity.class);
        startActivityForResult(intent,ConstantesDatos.RESPONSE_MANTENIMIENTO_FUENTE_DATOS);
    }



    /**
     * Método que es invocado cuando un activity secundaria devuelve una respuesta
     * a esta activity padre
     * @param requestCode int
     * @param resultCode int
     * @param data Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {

            switch (requestCode) {

                case ConstantesDatos.RESPONSE_NUEVA_FUENTE_DATOS:
                    // Se ha actualizada las fuentes/orígenes rss => HAy que actualizar el menú
                    rellenarMenu();
                    break;

                case ConstantesDatos.RESPONSE_MANTENIMIENTO_FUENTE_DATOS:
                    // Se ha realizado un mantenimiento de los orígenes/fuentes de datos => Hay que actualizar el menu
                    rellenarMenu();
                    break;

                case ConstantesDatos.RESPONSE_GRABACION_NOTICIA:
                    Bundle parametros = data.getExtras();
                    if(parametros!=null && parametros.size()>0) {
                        Noticia noticiaGrabada = (Noticia)parametros.get("noticiaGrabada");
                        if(noticiaGrabada!=null) {
                            noticiasAction.getNoticiasAdapter().getNoticia(getPosicionNoticiaSeleccionada()).setId(noticiaGrabada.getId());
                        }
                    }
                    break;
            }
        }
    }

}