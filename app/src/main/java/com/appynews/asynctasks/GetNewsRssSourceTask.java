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
public class GetNewsRssSourceTask extends AsyncTask<ParametrosAsyncTask,Void,ArrayList<Noticia>> {

    private MainActivity mainActivity = null;
    private ProgressDialog progressDialog = null;


    public GetNewsRssSourceTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    /**
     * Método a implementar para establecer la conexión a un origen de datos que se obtiene a través
     * de una conexión a internet, y recuperar la colección de noticias a pasar al ListView a través
     * de un adapter
     * @param params: Parámetros que se pueden pasar al método necesarios para poder ejecutar la tarea que realiza
     * @return ArrayList<Noticia>: Colección con las noticias extraida de la fuente RSS
     */
    //protected ArrayList<Noticia> doInBackground(InputStream... params) {
    protected ArrayList<Noticia> doInBackground(ParametrosAsyncTask... params) {

        String url = null;
        ArrayList<Noticia> noticias = new ArrayList<Noticia>();
        //mainActivity = params[0].getMainActivity();
        InputStream is = params[0].getInputStream();

        try{

            //Thread.sleep(1000);
            LectorRssImpl lector = new LectorRssImpl();
            noticias = lector.getNoticias(is);

            LogCat.debug("RSSConnection noticias recuperadas: " + noticias.size());
        }
        catch(Exception e){

            LogCat.error("Se ha producido un error técnico al recuperar las noticias del orígen de datos RSS: " + url + ": " + e.getMessage());
        }
        return noticias;
    }


    @Override
    protected void onPostExecute(ArrayList<Noticia> noticias) {
        LogCat.debug("onPostExecute init");
        super.onPostExecute(noticias);
        progressDialog.dismiss();
        LogCat.debug("onPostExecute end");
    }


    @Override
    protected void onPreExecute() {
        //new ProgressDialog(mainActivity.getApplicationContext());


        try {
            LogCat.debug("onPreExecute init");
            super.onPreExecute();
            if(mainActivity==null) {
                LogCat.debug("onPreExecute mainActivity ==null");
            } else
                LogCat.debug("onPreExecute mainActivity !=null");

            progressDialog = new ProgressDialog(mainActivity);

            progressDialog.setMessage(mainActivity.getString(R.string.procesando));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgress(0);
            progressDialog.show();
            LogCat.debug("onPreExecute end");
        }catch(Exception e) {
            e.printStackTrace();
        }

    }


}