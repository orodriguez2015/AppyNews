package com.appynews.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.appynews.database.exceptions.SQLiteException;
import com.appynews.database.helper.AppyNewsHelper;
import com.appynews.model.dto.Noticia;

/**
 * Tarea asíncrona a través de la cual se puede eliminar una noticia
 * Created by oscar on 10/09/16.
 */
public class DeleteNoticiaAsyncTask extends AsyncTask<ParametrosAsyncTask,Void,RespuestaAsyncTask> {

    /**
     * Se ejecuta la tarea asíncrona en segundo plano
     * @param params: String con la url de la que se obtiene el InputStream
     * @return RespuestaAsyncTask
     */
    protected RespuestaAsyncTask doInBackground(ParametrosAsyncTask... params){

        RespuestaAsyncTask respuesta = null;
        Context context = params[0].getContext();
        Noticia noticia = params[0].getNoticia();

        AppyNewsHelper helper = new AppyNewsHelper(context);

        try {
            helper.deleteNoticia(noticia);
            respuesta = new RespuestaAsyncTask(0, "OK");

        }catch(SQLiteException e) {
            e.printStackTrace();
            respuesta = new RespuestaAsyncTask(e.getStatus(),e.getMessage());
        }

        return respuesta;
    }
}
