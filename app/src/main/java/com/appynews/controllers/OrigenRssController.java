package com.appynews.controllers;

import android.app.Activity;

import com.appynews.asynctasks.GetOrigenesRssAsyncTask;
import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.asynctasks.RespuestaAsyncTask;
import com.appynews.exceptions.GetOrigenesRssException;
import com.appynews.model.dto.OrigenNoticiaVO;

import java.util.List;

/**
 * Controlador para realizar operaciones contra la tabla origen de la base de datos
 * Created by oscar on 20/09/16.
 */
public class OrigenRssController {

    /**
     * Recupera los orígenes de datos de tipo RSS
     * @param activity Activity desde el que se hace la petición
     * @return List<OrigenNoticiaVO>
     * @throws GetOrigenesRssException
     */
    public List<OrigenNoticiaVO> getOrigenes(Activity activity) throws GetOrigenesRssException {
        List<OrigenNoticiaVO> origenes = null;

        try {

            ParametrosAsyncTask params = new ParametrosAsyncTask();
            params.setContext(activity.getApplicationContext());

            GetOrigenesRssAsyncTask task = new GetOrigenesRssAsyncTask();
            task.execute(params);
            RespuestaAsyncTask res = task.get();
            origenes = res.getOrigenes();

        }catch(Exception e) {
            throw new GetOrigenesRssException(e.getMessage());
        }

        return origenes;
    }

}
