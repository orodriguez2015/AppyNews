package com.appynew.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.appynew.activities.dialog.AlertDialogHelper;
import com.appynews.adapter.FuenteDatosAdapter;
import com.appynews.controllers.OrigenRssController;
import com.appynews.exceptions.GetOrigenesRssException;
import com.appynews.model.dto.OrigenNoticiaVO;
import com.appynews.utils.LogCat;

import java.util.List;

import material.oscar.com.materialdesign.R;


/**
 * Clase OrigenRssMantenimientoActivity que representa a una actividad a través de la cual se permite el
 * mantenimiento de los orígenes de tipo RSS
 * @author oscar
 */
public class OrigenRssMantenimientoActivity extends AppCompatActivity {

    private RecyclerView recycler = null;
    private LinearLayoutManager linearLayoutManager = null;
    private OrigenRssController controller = null;

    /**
     * Método onCreate que crea la actividad y renderiza la vista
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_origen_rss_mantenimiento);

        controller = new OrigenRssController();

        // Se muestra el botón de atrás en la barra de título
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Obtener el RecyclerView que contendrá la lista de orígenes/fuentes de datos rss
        recycler = (RecyclerView) findViewById(R.id.recicladorOrigenRss);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        linearLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(linearLayoutManager);

        cargarFuentesDatos();
    }


    /**
     * Carga las noticias de la base de datos
     */
    private void cargarFuentesDatos() {

        List<OrigenNoticiaVO> origenes = null;

        try {
            origenes = controller.getOrigenes(this);

            LogCat.debug("cargarFuentesDatos origenes: " + origenes);
            FuenteDatosAdapter adapter = new FuenteDatosAdapter(origenes);
            adapter.notifyDataSetChanged();

            /**
             * Se establece el listener que se pasa al adapter para que añade
             * este Listener a cada View a mostrar en el RecyclerView
             */
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos =  recycler.getChildAdapterPosition(view);
                    LogCat.debug("Se ha seleccionado el elemento de la posición " + pos);
                }
            });

            recycler.setAdapter(adapter);

        } catch(GetOrigenesRssException e) {
            AlertDialogHelper.crearDialogoAlertaAdvertencia(this,getString(R.string.atencion),getString(R.string.err_get_fuentes_datos));
        }

    }


    /**
     * Método onOptionsItemSelected. Tiene que implementarse por ejemplo, para poder captura el evento producido al
     * pulsar sobre la fecha que permite volver hacia atrás
     * @param item MenuItem
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respuesta al pulsar el botón de atrás
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}