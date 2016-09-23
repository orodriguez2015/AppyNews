package com.appynews.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.appynews.database.exceptions.SQLiteException;
import com.appynews.database.helper.AppyNewsHelper;
import com.appynews.model.dto.OrigenNoticiaVO;


/**
 * Tarea asíncrona a través de la cual se puede grabar un orígen de datos
 * Created by oscar on 18/09/16.
 */
public class UpdateOrigenRssAsynchTask extends AsyncTask<ParametrosAsyncTask,Void,RespuestaAsyncTask> {

    /**
     * Se ejecuta la tarea asíncrona en segundo plano
     * @param params Objeto con los parámetros necesarios que necesita la tarea para poder grabar el origen de datos
     * @return RespuestaAsyncTask
     */
    protected RespuestaAsyncTask doInBackground(ParametrosAsyncTask... params){

        RespuestaAsyncTask respuesta = null;
        Context context = params[0].getContext();
        OrigenNoticiaVO origen = params[0].getOrigen();

        AppyNewsHelper helper = new AppyNewsHelper(context);

        try {
            helper.updateOrigen(origen);
            respuesta = new RespuestaAsyncTask(0, "OK");

        }catch(SQLiteException e) {
            e.printStackTrace();
            respuesta = new RespuestaAsyncTask(e.getStatus(),e.getMessage());
        }

        return respuesta;
    }
}
