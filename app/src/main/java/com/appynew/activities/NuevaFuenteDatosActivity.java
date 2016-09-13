package com.appynew.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import material.oscar.com.materialdesign.R;


/**
 * Activity a través del cual se puede dar de alta un nuevo origen de datos
 */
public class NuevaFuenteDatosActivity extends AppCompatActivity {

    /**
     * Métoo onCreate
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_fuente_datos);

        setTitle(getString(R.string.nuevo_origen_datos));

        // Se muestra el botón de atrás en la barra de título
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
