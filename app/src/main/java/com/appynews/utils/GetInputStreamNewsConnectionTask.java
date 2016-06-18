package com.appynews.utils;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;

/**
 * Tarea asíncrona que para una url determinada, establece una conexión y se devuelve
 * el InputStream, a partir del cual se puede obtener las noticias de un origen de datos
 * de tipo RSS.
 *
 * Created by oscar on 18/06/16.
 */
public class GetInputStreamNewsConnectionTask extends AsyncTask<String,Void,InputStream> {

    /**
     * Este método establece conexión con una determinada URL correspondiente a una fuente de
     * datos RSS y devuelve el InputStream a partir del cual se obtiene las noticias.
     * @param sUrl: URL de origen de datos al que conectarse para establecer conexión y obtener el InputStream
     * @return Un InputStream
     */
    public InputStream getInputStream(String sUrl) {
        InputStream is = null;

        try {

            is= new java.net.URL(sUrl).openConnection().getInputStream();

        } catch (IOException e) {
            LogCat.error("Error al intentar abrir conexión con: " + e.getMessage());
            return null;
        }

        return is;
    }


    protected InputStream doInBackground(String... params) {
        return getInputStream(params[0]);
    }
}
