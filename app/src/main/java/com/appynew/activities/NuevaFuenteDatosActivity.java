package com.appynew.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.appynew.activities.dialog.AlertDialogHelper;
import com.appynew.activities.dialog.BtnAceptarCancelarDialogGenerico;
import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.asynctasks.RespuestaAsyncTask;
import com.appynews.asynctasks.SaveOrigenRssAsyncTask;
import com.appynews.database.helper.DatabaseErrors;
import com.appynews.model.dto.OrigenNoticiaVO;
import com.appynews.utils.ConnectionUtils;

import material.oscar.com.materialdesign.R;


/**
 * Activity a través del cual se puede dar de alta un nuevo origen de datos
 */
public class NuevaFuenteDatosActivity extends AppCompatActivity {

    private Button btnAceptar = null;
    private Button btnCancelar = null;
    //private EditText txtNombreOrigen = null;
    //private EditText txtUrlOrigen = null;

    private AutoCompleteTextView txtNombreOrigen;
    private EditText txtUrlOrigen;
    private MainActivity activity = null;


    /**
     * Métoo onCreate
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_fuente_datos);

        setTitle(getString(R.string.nuevo_origen_datos));

        // Se recuperan los elementos que forman la interfaz de usuario
        btnAceptar      = (Button)findViewById(R.id.btnGrabarOrigen);
        btnCancelar     = (Button)findViewById(R.id.btnCancelar);
        txtNombreOrigen = (AutoCompleteTextView)findViewById(R.id.txtNombreOrigen);
        txtUrlOrigen    = (EditText)findViewById(R.id.txtUrlOrigen);


        // Se muestra el botón de atrás en la barra de título
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /**
         * Evento onClickListener sobre el botón Aceptar
         */
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            validarCamposFormulario();

                /**
                String nombre = txtNombreOrigen.getText().toString();
                String url    = txtUrlOrigen.getText().toString();

                if(TextUtils.isEmpty(nombre)) {
                    AlertDialogHelper.crearDialogoAlertaSimple(NuevaFuenteDatosActivity.this,getString(R.string.atencion),getString(R.string.error_nombre_origen_obligatorio),new BtnAceptarCancelarDialogGenerico()).show();
                }
                else
                if(TextUtils.isEmpty(url)) {
                    AlertDialogHelper.crearDialogoAlertaSimple(NuevaFuenteDatosActivity.this,getString(R.string.atencion),getString(R.string.error_url_origen_obligatorio),new BtnAceptarCancelarDialogGenerico()).show();
                } else {

                    boolean esUrlValida = Patterns.WEB_URL.matcher(url).matches();

                    if(!esUrlValida) {
                        // Si la url no es válida
                        AlertDialogHelper.crearDialogoAlertaSimple(NuevaFuenteDatosActivity.this,getString(R.string.atencion),getString(R.string.error_url_no_valida),new BtnAceptarCancelarDialogGenerico()).show();
                    } else {
                        LogCat.debug("La url es valida " + url);
                        grabarOrigenDatos(new OrigenNoticiaVO(nombre,url));
                    }
                }
                 **/

            }
        });


        /**
         * Evento onClickListener sobre el botón Cancelar
         */
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    /**
     * Se validan los campos del formulario
     */
    private void  validarCamposFormulario() {
        boolean cancel = false;
        View focusView = null;
        String nombre  = this.txtNombreOrigen.getText().toString();
        String url     = this.txtUrlOrigen.getText().toString();

        if(TextUtils.isEmpty(nombre)) {
            this.txtNombreOrigen.setError(getString(R.string.campo_obligatorio));
            focusView = txtNombreOrigen;
            cancel    = true;
        } else
        if(TextUtils.isEmpty(url)) {
            this.txtUrlOrigen.setError(getString(R.string.campo_obligatorio));
            focusView = txtUrlOrigen;
            cancel    = true;
        } else
        if (!ConnectionUtils.isUrlFormatoValida(url)) {
            this.txtUrlOrigen.setError(getString(R.string.err_formato_url_origen));
            focusView = txtUrlOrigen;
            cancel    = true;
        } else {

            int isOnline = ConnectionUtils.isOnline(this,url);

            switch(isOnline) {
                case 0: // Todo OK
                    cancel = false;
                    break;

                case 1: // No se ha podido establecer conexión con la url del recurso RSS
                    this.txtUrlOrigen.setError(getString(R.string.err_conexion_url_origen));
                    focusView = txtUrlOrigen;
                    cancel    = true;
                    break;

                case 2: // El dispositivo no tiene habilitadas sus conexiones de red
                    this.txtUrlOrigen.setError(getString(R.string.err_connection_state));
                    focusView = txtUrlOrigen;
                    cancel    = true;
                    break;

            }// switch

        }





        if(cancel) {
            focusView.requestFocus();
        } else {
            grabarOrigenDatos(new OrigenNoticiaVO(nombre,url));
        }

    }


    /**
     * Operación que invoca a la tarea asíncrona que se encarga
     * de grabar un origen de datos en la base de datos
     * @param origen OrigenNoticiaVO
     */
    private void grabarOrigenDatos(OrigenNoticiaVO origen) {
        ParametrosAsyncTask params = new ParametrosAsyncTask();
        params.setContext(getApplicationContext());
        params.setOrigen(origen);

        SaveOrigenRssAsyncTask task = new SaveOrigenRssAsyncTask();
        task.execute(params);

        try {
            RespuestaAsyncTask res = task.get();
            if(res.getStatus().equals(DatabaseErrors.OK)) {
                // Se pasa a la actividad MainActivity padre el resultado en el intent
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                // Se finaliza el activity
                finish();

            } else {
                // Se muestra un error
                AlertDialogHelper.crearDialogoAlertaSimple(NuevaFuenteDatosActivity.this,getString(R.string.atencion),getString(R.string.error_grabar_origen_bbdd),new BtnAceptarCancelarDialogGenerico()).show();
            }

        }catch(Exception e) {

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