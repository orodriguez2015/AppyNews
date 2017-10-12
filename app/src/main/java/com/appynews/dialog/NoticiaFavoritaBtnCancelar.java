package com.appynews.dialog;

import android.content.DialogInterface;

/**
 * Acción asociada al botón "Cancelar" del cuadro de diálogo que permite
 * grabar una noticia en la base de datos
 * Created by oscar on 27/08/16.
 */
public class NoticiaFavoritaBtnCancelar implements DialogInterface.OnClickListener {

    /**
     * onClick
     * @param var1: DialogInterface
     * @param var2:i nt
     */
    public void onClick(DialogInterface var1, int var2) {
        // Se invoca al AsynchTask a través del cual se guarda la
        // noticia como favorita
    }

}
