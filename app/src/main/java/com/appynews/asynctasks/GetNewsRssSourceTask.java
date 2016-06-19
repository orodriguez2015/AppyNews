package com.appynews.asynctasks;

import android.os.AsyncTask;

import com.appynews.model.dto.Noticia;
import com.appynews.utils.LogCat;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by oscar on 18/06/16.
 */
public class GetNewsRssSourceTask extends AsyncTask<InputStream,Void,ArrayList<Noticia>> {


    @Override
    /**
     * Método a implementar para establecer la conexión a un origen de datos que se obtiene a través
     * de una conexión a internet, y recuperar la colección de noticias a pasar al ListView a través
     * de un adapter
     * @param params: Parámetros que se pueden pasar al método necesarios para poder ejecutar la tarea que realiza
     * @return ArrayList<Noticia>: Colección con las noticias extraida de la fuente RSS
     */
    protected ArrayList<Noticia> doInBackground(InputStream... params) {

        String url = null;
        ArrayList<Noticia> noticias = new ArrayList<Noticia>();

        try{

            LectorRssImpl lector = new LectorRssImpl();
            noticias = lector.getNoticias(params[0]);

            LogCat.debug("RSSConnection noticias recuperadas: " + noticias.size());
        }
        catch(Exception e){
            LogCat.error("Se ha producido un error técnico al recuperar las noticias del orígen de datos RSS: " + url + ": " + e.getMessage());
        }
        return noticias;
    }



    protected void onPostExecute(Void result) {

        //barraDeProgreso.setVisibility(View.INVISIBLE);
       // Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG).show();
       // AndroidLog.debug("onPostExecute() ============>");
    }

    @Override
    protected void onPreExecute() {
        //barraDeProgreso.setVisibility(View.VISIBLE);
        //barraDeProgreso.setProgress(10);

    }



    protected void onProgressUpdate(Integer... values) {
        //barraDeProgreso.setProgress(values[0]);
    }


}