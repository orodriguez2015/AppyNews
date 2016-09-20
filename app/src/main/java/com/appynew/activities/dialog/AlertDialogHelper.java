package com.appynew.activities.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.appynews.model.dto.OrigenNoticiaVO;
import com.appynews.utils.LogCat;
import com.appynews.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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




    /**
     * Operación que crea un AlertDialog de Android para mostrar únicamente un mensaje de advertencia al usuario.
     * Sólo muestra un botón de [Aceptar] al cual no se le puede asociar ningún listener
     * @param activity: Actividad padre sobre la que se mostrará el AlertDialog
     * @param titulo: Título del activity
     * @param mensaje: Mensaje a mostrar al usuario
     * @return AlertDialog
     */
    public static AlertDialog crearDialogoAlertaAdvertencia(final Activity activity, String titulo, String mensaje) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        // Acción para el botón de "Aceptar"
        builder.setPositiveButton(activity.getString(R.string.aceptar),new BtnAceptarCancelarDialogGenerico());

        return builder.create();
    }



    /**
     * Operación que crea un AlertDialog de Android simple con un determinado mensaje
     * @param activity: Actividad padre sobre la que se mostrará el AlertDialog
     * @param titulo: Título del activity
     * @param mensaje: Mensaje a mostrar al usuario
     * @param aceptar: Clase que implementa la interfaz DialogInterface.OnClickListener para la acción asociada
     *                 al botón de Aceptar
     * @return AlertDialog
     */
    public static AlertDialog crearDialogoAlertaSimple(final Activity activity, String titulo, String mensaje, DialogInterface.OnClickListener aceptar) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        // Acción para el botón de "Aceptar"
        builder.setPositiveButton(activity.getString(R.string.aceptar),aceptar);

        return builder.create();

    }




    /**
     * Operación que crea un AlertDialog de Android que permite al usuario seleccionar entre varios origenes de datos, y a continuación, proceder
     * a la eliminación física de los mismos de la BBDD
     * @param activity Actividad padre sobre la que se mostrará el AlertDialog
     * @param mensaje Título del AlertDialog
     * @param fuentesDatos List<OrigenNoticiaVO>
     * @param mensaje DialogInterface.OnClickListener que contiene la acción
     * @return AlertDialog
     */
    public static AlertDialog crearDialogoAlertaSeleccionMultipleFuenteDatos(final Activity activity, String mensaje, final List<OrigenNoticiaVO> fuentesDatos, DialogInterface.OnClickListener aceptar) {
        // Array con los nombres de los orígenes/fuentes de datos
        final String[] datos                      = Utils.toStringArray(fuentesDatos);
        // Colección con los id´ de los orígenes/fuentes de datos seleccionados por le usuario
        final List<Integer> idsSeleccionados = new ArrayList<Integer>();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(mensaje);

        builder.setMultiChoiceItems(datos,null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface var1, int posicion, boolean seleccionado) {
                LogCat.debug("Seleccionado elemento var2 : " + posicion + " con valor: " +  datos[posicion] + ", var3: " + seleccionado);

                if(seleccionado) {
                    idsSeleccionados.add(fuentesDatos.get(posicion).getId());
                } else {
                    idsSeleccionados.remove(fuentesDatos.get(posicion).getId());
                }

                LogCat.debug("idsSeleccionados " + idsSeleccionados);
            }
        });


        // Acción asociada al botón "Cancelar"
        builder.setNegativeButton(activity.getString(R.string.cancelar),new BtnAceptarCancelarDialogGenerico());

        // Acción para el botón de "Aceptar"
        builder.setPositiveButton(activity.getString(R.string.aceptar),new BorrarOrigenesRssOnClickListener(activity,idsSeleccionados));


        return builder.create();
    }

}
