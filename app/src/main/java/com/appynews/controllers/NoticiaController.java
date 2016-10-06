package com.appynews.controllers;

import android.app.Activity;
import android.content.Context;

import com.appynews.asynctasks.GetNoticiasAsyncTask;
import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.asynctasks.RespuestaAsyncTask;
import com.appynews.model.dto.Noticia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Clase NoticiaController
 * Created by oscar on 19/06/16.
 */
public class NoticiaController {
    private Activity actividad = null;

    /**
     * Constructor
     * @param actividad: Actividad desde la que se invoca al controller
     */
    public NoticiaController(Activity actividad) {
        this.actividad = actividad;
    }


    /**
     * Recupera las noticias grabadas por el usuario en la base de datos
     * @param context: Context
     * @return List<Noticia>
     */
    public List<Noticia> getNoticiasFavoritas(Context context) {
        List<Noticia> noticias = new ArrayList<Noticia>();


        try {
            ParametrosAsyncTask params = new ParametrosAsyncTask();
            params.setContext(context);

            GetNoticiasAsyncTask task = new GetNoticiasAsyncTask();
            task.execute(params);
            RespuestaAsyncTask res = task.get();
            if(res.getStatus()==0) {
                noticias = res.getNoticias();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return noticias;
    }

}
