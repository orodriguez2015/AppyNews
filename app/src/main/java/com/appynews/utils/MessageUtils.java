package com.appynews.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Clase MessageUtils con operaciones de utilidad para mostrar mensajes
 * al usuario.
 *
 * Created by oscar on 18/06/16.
 */
public class MessageUtils {

    public static final int DURACION_CORTA = Toast.LENGTH_SHORT;
    public static final int DURACION_LARGA = Toast.LENGTH_LONG;


    /**
     * Muestra un mensaje de tipo Toast
     * @param context: Context
     * @param message: String con el mensaje a mostrar
     * @param duracion: Puede ser de dos tipos:
     *                  Toast.LENGTH_SHORT para una duración corta
     *                  Toast.LENGTH_LONG para una duración larga
     */
    public static void showToast(Context context,String message,int duracion) {
        Toast.makeText(context,message,duracion).show();
    }


    /**
     * Muestra un mensaje de tipo Toast de duración corta
     * @param context: Context
     * @param message: String con el mensaje a mostrar
     */
    public static void showToastDuracionCorta(Context context,String message) {
        Toast.makeText(context,message,DURACION_CORTA).show();
    }

    /**
     * Muestra un mensaje de tipo Toast de duración larga
     * @param context: Context
     * @param message: String con el mensaje a mostrar
     */
    public static void showToastDuracionLarga(Context context,String message) {
        Toast.makeText(context,message,DURACION_LARGA).show();
    }


}
