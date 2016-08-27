package com.appynew.activities.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import material.oscar.com.materialdesign.R;

/**
 * Created by oscar on 27/08/16.
 */
public class AlertDialogHelper  {


    /**
     * Operación que crea un AlertDialog de Android simple con un determinado mensaje
     * @param activity: Actividad padre sobre la que se mostrará el AlertDialog
     * @param titulo: Título del activity
     * @param mensaje: Mensaje a mostrar al usuario
     * @param aceptar: Clase que implementa la interfaz DialogInterface.OnClickListener para la acción asociada
     *                 al botón de Aceptar
     * @param cancelar: Clase que implementa la interfaz DialogInterface.OnClickListener para la acción asociada
     *                 al botón de Cancelar
     * @return AlertDialog
     */
    public static AlertDialog crearDialogoAlertaSimple(final Activity activity, String titulo, String mensaje, DialogInterface.OnClickListener aceptar,DialogInterface.OnClickListener cancelar) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        // Acción para el botón de "Aceptar"
        builder.setPositiveButton(activity.getString(R.string.aceptar),aceptar);
        // Acción para el botón de "Cancelar"
        builder.setNegativeButton(activity.getString(R.string.cancelar),cancelar);

        return builder.create();
    }

}
