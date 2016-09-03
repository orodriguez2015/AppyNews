package com.appynew.activities.dialog;

import android.content.DialogInterface;

import com.appynews.utils.LogCat;

/**
 * Clase BtnAceptarDialogGenerico que se asocia a la acción de hacer click sobre un botón de un AlertDialog
 * @author oscar
 */
public class BtnAceptarDialogGenerico implements DialogInterface.OnClickListener {

    /**
     * onClick
     * @param var1: DialogInterface
     * @param var2:i nt
     */
    public void onClick(DialogInterface var1, int var2) {
        LogCat.debug("Ha pulsado el botón Aceptar");
    }

}
