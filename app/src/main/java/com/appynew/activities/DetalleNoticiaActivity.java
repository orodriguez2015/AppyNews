package com.appynew.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.appynew.activities.dialog.AlertDialogHelper;
import com.appynew.activities.dialog.NoticiaFavoritaBtnAceptar;
import com.appynew.activities.dialog.NoticiaFavoritaBtnCancelar;
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
    private FloatingActionButton fab = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_noticia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Botón flotante para guardar la noticia como favorita
         */
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                noticia.setFechaPublicacion(DateOperationsUtils.getFechaHoraActual(DateOperationsUtils.FORMATO_FECHA_HORA));

                AlertDialogHelper.crearDialogoAlertaSimple(DetalleNoticiaActivity.this,getString(R.string.atencion),getString(R.string.pregunta_grabar_noticia_favorita),
                        new NoticiaFavoritaBtnAceptar(DetalleNoticiaActivity.this,noticia),new NoticiaFavoritaBtnCancelar()).show();
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        **/
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

        LogCat.debug(" =====> titulo: " + noticia.getTitulo());
        LogCat.debug(" =====> descripcion: " + noticia.getDescripcion());
        LogCat.debug(" =====> descripcionCompleta: " + noticia.getDescripcionCompleta());
        LogCat.debug(" =====> urlThumbnail: " + noticia.getUrlThumbnail());

        // Se lee la plantilla html de noticias, se incluyen los datos de la noticia en la misma, y se devuelve el +
        // html en un String
        String html = FileOperations.readHtmlFromResource(R.raw.plantillahtmlnoticia,noticia,getResources());

        WebSettings configuracionWebView = webViewNoticia.getSettings();
        webViewNoticia.setInitialScale(1);
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
     * @param mostrar: True si se muestra y false en caso contrario
     */
    public void mostrarBotonFlotante(boolean mostrar) {
        if(mostrar) {
            this.fab.show();
        } else {
            this.fab.hide();
        }
    }
}
