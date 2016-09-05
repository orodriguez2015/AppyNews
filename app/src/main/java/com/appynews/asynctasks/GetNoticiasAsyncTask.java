package com.appynews.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.appynews.database.exceptions.SQLiteException;
import com.appynews.database.helper.AppyNewsHelper;
import com.appynews.model.dto.Noticia;

import java.util.List;

/**
 * Tarea asíncrona que recupera las noticias grabadas por el usuario en la base
 * de datos
 *
 * Created by oscar on 28/08/16.
 */
public class GetNoticiasAsyncTask extends AsyncTask<ParametrosAsyncTask,Void,RespuestaAsyncTask> {

    /**
     * Se ejecuta la tarea asíncrona en segundo plano
     * @param params: String con la url de la que se obtiene el InputStream
     * @return RespuestaAsyncTask
     */
    protected RespuestaAsyncTask doInBackground(ParametrosAsyncTask... params){

        RespuestaAsyncTask respuesta = null;
        Context context = params[0].getContext();

        AppyNewsHelper helper = new AppyNewsHelper(context);

        try {
            List<Noticia> noticias = helper.getNoticias();
            respuesta = new RespuestaAsyncTask(0, "OK");
            respuesta.setNoticias(noticias);

        }catch(SQLiteException e) {
            e.printStackTrace();
            respuesta = new RespuestaAsyncTask(e.getStatus(),e.getMessage());
        }

        return respuesta;
    }

}
