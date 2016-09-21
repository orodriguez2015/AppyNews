package com.appynews.controllers;

import android.app.Activity;

import com.appynews.asynctasks.DeleteOrigenesRssAsyncTask;
import com.appynews.asynctasks.GetOrigenesRssAsyncTask;
import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.asynctasks.RespuestaAsyncTask;
import com.appynews.database.helper.DatabaseErrors;
import com.appynews.exceptions.DeleteOrigenesRssException;
import com.appynews.exceptions.GetOrigenesRssException;
import com.appynews.model.dto.OrigenNoticiaVO;

import java.util.List;

/**
 * Controlador para realizar operaciones contra la tabla origen de la base de datos
 * Created by oscar on 20/09/16.
 */
public class OrigenRssController {

    private Activity actividad = null;

    /**
     * Constructor
     * @param actividad Activity desde el que se lanza la petición contra la BBDD
     */
    public OrigenRssController(Activity actividad) {
        this.actividad = actividad;
    }

    /**
     * Recupera los orígenes de datos de tipo RSS
     * @return List<OrigenNoticiaVO>
     * @throws GetOrigenesRssException
     */
    public List<OrigenNoticiaVO> getOrigenes() throws GetOrigenesRssException {
        List<OrigenNoticiaVO> origenes = null;

        try {

            ParametrosAsyncTask params = new ParametrosAsyncTask();
            params.setContext(actividad.getApplicationContext());

            GetOrigenesRssAsyncTask task = new GetOrigenesRssAsyncTask();
            task.execute(params);
            RespuestaAsyncTask res = task.get();
            origenes = res.getOrigenes();

        }catch(Exception e) {
            throw new GetOrigenesRssException(e.getMessage());
        }

        return origenes;
    }



    /**
     * Método encargado de eliminar uno o varios orígenes RSS de la base de datos
     * @param idsOrigenesBorrar Colección de ids de orígenes/fuentes a eliminar
     * @throws GetOrigenesRssException
     */
    public void borrarOrigenesRss(List<Integer> idsOrigenesBorrar) throws DeleteOrigenesRssException {

        try {

            ParametrosAsyncTask params = new ParametrosAsyncTask();
            params.setContext(actividad.getApplicationContext());
            params.setIdsOrigenesEliminar(idsOrigenesBorrar);

            DeleteOrigenesRssAsyncTask task = new DeleteOrigenesRssAsyncTask();
            task.execute(params);

            RespuestaAsyncTask res = task.get();
            if(!(res.getStatus()== DatabaseErrors.OK)) {
                throw new DeleteOrigenesRssException("Error al borrar el/los origen/es RSS de la base de datos");
            }

        }catch(Exception e) {
            e.printStackTrace();
            throw new DeleteOrigenesRssException(e.getMessage());
        }
    }



    /**
     * Método encargado de eliminar uno o varios orígenes RSS de la base de datos
     * @param origen OrigenNoticiaVO Colección de ids de orígenes/fuentes a eliminar
     * @throws GetOrigenesRssException
     */
    public void borrarOrigenRss(OrigenNoticiaVO origen) throws DeleteOrigenesRssException {

        try {

            ParametrosAsyncTask params = new ParametrosAsyncTask();
            params.setContext(actividad.getApplicationContext());
            params.setOrigen(origen);

            DeleteOrigenesRssAsyncTask task = new DeleteOrigenesRssAsyncTask();
            task.execute(params);

            RespuestaAsyncTask res = task.get();
            if(!(res.getStatus()== DatabaseErrors.OK)) {
                throw new DeleteOrigenesRssException("Error al borrar el orígen RSS de la base de datos");
            }

        }catch(Exception e) {
            e.printStackTrace();
            throw new DeleteOrigenesRssException(e.getMessage());
        }
    }

}
