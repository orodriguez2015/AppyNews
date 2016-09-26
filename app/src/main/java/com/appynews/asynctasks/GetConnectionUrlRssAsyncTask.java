package com.appynews.asynctasks;

import android.os.AsyncTask;

import com.appynews.utils.LogCat;

import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Tarea asíncrona que para una url de un origen rss, comprueba si se puede establecer
 * una conexión http
 *
 * Created by oscar on 18/06/16.
 */
public class GetConnectionUrlRssAsyncTask extends AsyncTask<String,Void,Boolean> {


    /**
     * Ejecuta la tarea asíncrona en segundo plano
     * @param params String con la dirección/url rss con la que se pretende establecer conexión
     * @return Boolean
     */
    protected Boolean doInBackground(String... params){
        Boolean salida = false;

        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL(params[0]).openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500);
            urlc.connect();

            LogCat.debug(" Response code:  " + urlc.getResponseCode());
            return (urlc.getResponseCode() == 200);


        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}


