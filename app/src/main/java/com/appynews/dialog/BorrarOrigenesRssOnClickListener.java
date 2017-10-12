package com.appynews.dialog;

import android.app.Activity;
import android.content.DialogInterface;

import com.appynews.asynctasks.DeleteOrigenesRssAsyncTask;
import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.asynctasks.RespuestaAsyncTask;
import com.appynews.database.helper.DatabaseErrors;
import com.appynews.utils.MessageUtils;

import java.util.List;

import material.oscar.com.materialdesign.R;

/**
 * Listener encargado de dar eliminar uno o más orígenes/fuentes de datos
 * Created by oscar on 20/09/16.
 */
public class BorrarOrigenesRssOnClickListener implements DialogInterface.OnClickListener {

    private List<Integer> idOrigenes = null;
    private Activity activity = null;

    /**
     * Constructor
     * @param activity   Activity
     * @param idOrigenes List<Integer> que contiene los id´s de las fuentes de datos a
     *                   eliminar
     */
    public BorrarOrigenesRssOnClickListener(Activity activity, List<Integer> idOrigenes) {
        this.idOrigenes = idOrigenes;
        this.activity   = activity;
    }


    /**
     * Método onClick que se invoca cuando el usuario selecciona un determinado elemento
     * @param dialogInterface DialogInterface
     * @param i int
     */
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        ParametrosAsyncTask params = new ParametrosAsyncTask();
        params.setContext(activity.getApplicationContext());
        params.setIdsOrigenesEliminar(idOrigenes);

        try {
            DeleteOrigenesRssAsyncTask task = new DeleteOrigenesRssAsyncTask();
            task.execute(params);


            MessageUtils.showToastDuracionCorta(this.activity.getApplicationContext(),"A borrar");
            RespuestaAsyncTask res = task.get();
            if(res.getStatus()!= DatabaseErrors.OK) {
                AlertDialogHelper.crearDialogoAlertaAdvertencia(this.activity,this.activity.getString(R.string.atencion),this.activity.getString(R.string.err_eliminar_origen_datos));
            }
        }catch(Exception e) {
            AlertDialogHelper.crearDialogoAlertaAdvertencia(this.activity,this.activity.getString(R.string.atencion),this.activity.getString(R.string.err_eliminar_origen_datos));
        }

    }

}