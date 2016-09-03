package com.appynews.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.appynews.database.com.appynews.database.exception.SQLiteException;
import com.appynews.database.helper.AppyNewsHelper;
import com.appynews.model.dto.OrigenNoticiaVO;

import java.util.List;

/**
 * Clase GetOrigenesRssAsyncTask que representa una tarea asíncrona a través de la cual
 * hay que recuperar los orígenes de datos RSS de la base de datos
 *
 * @author
 */
public class GetOrigenesRssAsyncTask extends AsyncTask<ParametrosAsyncTask,Void,RespuestaAsyncTask> {


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
            List<OrigenNoticiaVO> origenes = helper.getOrigenes();
            respuesta = new RespuestaAsyncTask(0, "OK");
            respuesta.setOrigenes(origenes);

        }catch(SQLiteException e) {
            e.printStackTrace();
            respuesta = new RespuestaAsyncTask(e.getStatus(),e.getMessage());
        }

        return respuesta;
    }


}
