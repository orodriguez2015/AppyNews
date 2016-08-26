package com.appynews.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.appynews.database.com.appynews.database.exception.SQLiteException;
import com.appynews.database.helper.AppyNewsHelper;
import com.appynews.model.dto.DatosUsuarioVO;

/**
 * Tarea asíncrona a través de la cual se graba un usuario en la base de datos
 * en la base de datos
 *
 * Created by oscar on 25/08/16.
 */
public class SaveUsuarioAsyncTask extends AsyncTask<ParametrosAsyncTask,Void,RespuestaAsyncTask> {

    /**
     * Se ejecuta
     * @param params: String con la url de la que se obtiene el InputStream
     * @return RespuestaAsyncTask
     */
    protected RespuestaAsyncTask doInBackground(ParametrosAsyncTask... params){

        RespuestaAsyncTask respuesta = null;
        Context context        = params[0].getContext();
        DatosUsuarioVO usuario = params[0].getUsuario();

        AppyNewsHelper helper = new AppyNewsHelper(context);

        try {
            if(!helper.existeUsuario(usuario)) {
                helper.saveUsuario(usuario);
            }

            respuesta = new RespuestaAsyncTask(0, "OK");

        }catch(SQLiteException e) {
            e.printStackTrace();
            respuesta = new RespuestaAsyncTask(e.getStatus(),e.getMessage());
        }

        return respuesta;
    }
}

