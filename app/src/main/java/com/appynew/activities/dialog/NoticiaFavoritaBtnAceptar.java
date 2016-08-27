package com.appynew.activities.dialog;

import android.content.Context;
import android.content.DialogInterface;

import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.asynctasks.RespuestaAsyncTask;
import com.appynews.asynctasks.SaveNoticiaAsyncTask;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.LogCat;
import com.appynews.utils.MessageUtils;

import java.util.concurrent.ExecutionException;

import material.oscar.com.materialdesign.R;

/**
 * Clase NoticiaFavoritaBtnAceptar utilizada para detectar el evento onClick
 * sobre el botón Aceptar de un AlertDialog
 * Created by oscar on 27/08/16.
 */
public class NoticiaFavoritaBtnAceptar implements DialogInterface.OnClickListener {

    private Noticia noticia = null;
    private Context context = null;

    /**
     * Constructor
     * @param context: Context
     * @param noticia: Noticia
     */
    public NoticiaFavoritaBtnAceptar(Context context,Noticia noticia) {
        this.context = context;
        this.noticia = noticia;
    }

    /**
     * onClick
     * @param var1: DialogInterface
     * @param var2:i nt
     */
    public void onClick(DialogInterface var1, int var2) {
        // Se invoca al AsynchTask a través del cual se guarda la
        // noticia como favorita

        ParametrosAsyncTask params = new ParametrosAsyncTask();
        params.setNoticia(this.noticia);
        params.setContext(this.context);

        try {
            SaveNoticiaAsyncTask task = new SaveNoticiaAsyncTask();
            task.execute(params);
            RespuestaAsyncTask res = task.get();
            if(res.getStatus()==0) {
                MessageUtils.showToastDuracionLarga(this.context,this.context.getString(R.string.noticia_grabada));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            LogCat.error("Error al ejecutar tarea asíncrona de grabación de noticia en BD: ".concat(e.getMessage()));
        } catch (ExecutionException e) {
            e.printStackTrace();
            LogCat.error("Error al ejecutar tarea asíncrona de grabación de noticia en BD: ".concat(e.getMessage()));
        }
    }

}
