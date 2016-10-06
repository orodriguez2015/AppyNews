package com.appynews.asynctasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.appynew.activities.MainActivity;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.LogCat;

import java.io.InputStream;
import java.util.ArrayList;

import material.oscar.com.materialdesign.R;

/**
 * Created by oscar on 18/06/16.
 */
public class GetNoticiasExternasAsyncTask extends AsyncTask<ParametrosAsyncTask,Void,ArrayList<Noticia>> {

    private ArrayList<Noticia> noticias = new ArrayList<Noticia>();
    private ProgressDialog pg = null;
    private MainActivity actividad = null;

    /**
     * Constructor
     * @param mainActivity MainActivity
     */
    public GetNoticiasExternasAsyncTask(MainActivity mainActivity) {
        this.actividad = mainActivity;
    }


    /**
     * Método a implementar para establecer la conexión a un origen de datos que se obtiene a través
     * de una conexión a internet, y recuperar la colección de noticias a pasar al ListView a través
     * de un adapter
     * @param params: Parámetros que se pueden pasar al método necesarios para poder ejecutar la tarea que realiza
     * @return ArrayList<Noticia>: Colección con las noticias extraida de la fuente RSS
     */
    @Override
    protected ArrayList<Noticia> doInBackground(ParametrosAsyncTask... params) {

        InputStream is = null;
        String url = null;
        url = params[0].getUrl();

        try{

            is= new java.net.URL(url).openConnection().getInputStream();
            LectorRssImpl lector = new LectorRssImpl();
            noticias = lector.getNoticias(is);

        }
        catch(Exception e){

            LogCat.error("Se ha producido un error técnico al recuperar las noticias del orígen de datos RSS: " + url + ": " + e.getMessage());
        }
        return noticias;
    }

    @Override
    protected void onPostExecute(ArrayList<Noticia> noticias) {
        // Se pasan las noticias recuperadas al adapter y se le notifica del cambio, para que renderize la vista
        actividad.mostrarNoticias(this.noticias);
        if (pg.isShowing()) {
            pg.dismiss();
        }
    }


    @Override
    protected void onPreExecute() {
        this.pg = ProgressDialog.show(this.actividad, this.actividad.getString(R.string.procesando),this.actividad.getString(R.string.espere), true, false);
    }

}