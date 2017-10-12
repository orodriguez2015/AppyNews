package com.appynew.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.appynews.dialog.AlertDialogHelper;
import com.appynews.dialog.NoticiaFavoritaBtnAceptar;
import com.appynews.dialog.NoticiaFavoritaBtnCancelar;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.ConstantesDatos;
import com.appynews.utils.DateOperationsUtils;
import com.appynews.utils.FileOperations;
import com.appynews.utils.LogCat;
import com.appynews.utils.StringUtil;

import material.oscar.com.materialdesign.R;

public class DetalleNoticiaActivity extends AppCompatActivity {


    private WebView webViewNoticia = null;
    private Noticia noticia = null;
    private FloatingActionButton floatingActionButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_noticia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Botón flotante para guardar la noticia como favorita
         */
        this.floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticia.setFechaPublicacion(DateOperationsUtils.getFechaHoraActual(DateOperationsUtils.FORMATO_FECHA_HORA));
                AlertDialogHelper.crearDialogoAlertaConfirmacion(DetalleNoticiaActivity.this,getString(R.string.atencion),getString(R.string.pregunta_grabar_noticia_favorita),
                        new NoticiaFavoritaBtnAceptar(DetalleNoticiaActivity.this,noticia),new NoticiaFavoritaBtnCancelar()).show();

            }
        });


        /**
         * Se recupera el WebView de la vista
         */
        webViewNoticia = (WebView) findViewById(R.id.webviewDescripcionNoticia);


        /**
         * Se recupera la noticia que se ha pasado al activity como parámetro
         */
        Bundle parametros = getIntent().getExtras();
        this.noticia = (Noticia)parametros.get("noticia");
        this.setTitle(noticia.getOrigen());
        this.setProgressBarVisibility(true);

        String desc = null;
        if(StringUtil.isNotEmpty(noticia.getDescripcion())) {
            desc = noticia.getDescripcion();
        } else
        if(StringUtil.isNotEmpty(noticia.getDescripcionCompleta())) {
            desc = noticia.getDescripcionCompleta();
        }


        /** Si la noticia procede de la base de datos, se oculta el botón flotante para que el usuario
         * no puede almacenarla de nuevo en la base de datos
         */
        if(noticia.isNoticiaFavorita()) {
            mostrarBotonFlotante(false);
        }

        /******************************************************************************/
        /*** SE CARGA LA PLANTILLA HTML PARA MOSTRARLA LAS NOTICIAS EN LA MISMA Y   ***/
        /*** VISUALIZARLA EN EL WEBVIEW                                             ***/
        /******************************************************************************/
        String html = FileOperations.readHtmlFromResource(R.raw.plantillahtmlnoticia,noticia,getResources());

        WebSettings configuracionWebView = webViewNoticia.getSettings();
        webViewNoticia.setInitialScale(1);


        configuracionWebView.setLoadsImagesAutomatically(true);
        configuracionWebView.setUseWideViewPort(true);
        configuracionWebView.setLoadWithOverviewMode(true);
        configuracionWebView.setBuiltInZoomControls(true);
        configuracionWebView.setJavaScriptEnabled(true);
        configuracionWebView.setDisplayZoomControls(true);

        // Se carga la plantilla HTML con los datos de la noticia en el webview
        webViewNoticia.loadDataWithBaseURL(null,html, ConstantesDatos.MIMETYPE_TEXT_HTML,ConstantesDatos.UTF_8,null);

    }


    /**
     * Muestra/Oculta el botón flotante
     * @param mostrar True si se muestra y false en caso contrario
     */
    public void mostrarBotonFlotante(boolean mostrar) {
        if(mostrar) {
            this.floatingActionButton.show();
        } else {
            this.floatingActionButton.hide();
        }
    }


    /**
     * Comunica por medio deun Intent a la MainActivity que todo está OK.
     * A continuacion se vuelve hacia atrás
     */
    private void volverAtras() {
        Intent data = new Intent();
        Integer idNoticia = new Integer(-1);

        LogCat.debug("DetallenoticiaActivity noticia.getId() " + noticia.getId());

        // Si la noticia de la que se ve el detalle tiene id, es que se ha grabado en base de datos, por tanto,
        // se pasa dicho id a MainActivity para actualizar el id de la noticia
        if(this.noticia.getId()!=null && this.noticia.getId()>0) {
            idNoticia = this.noticia.getId();
            this.noticia.setId(idNoticia);
            data.putExtra("noticiaGrabada",this.noticia);
        }

        setResult(RESULT_OK, data);
        onBackPressed();
    }


    /**
     * Se sobreescribe el método onKeyDown para detectar que tecla ha pulsado el usuario, y
     * ejecutar la acción que corresponda. En este caso, si pulsa el botón atrás, se devuelve un intent
     * a la actividad MainActivity para que esta ejecute la acción que sea conveniente
     * @param keyCode int
     * @param event KeyEvent
     * @return boolean
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            volverAtras();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
