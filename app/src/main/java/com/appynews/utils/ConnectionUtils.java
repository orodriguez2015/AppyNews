package com.appynews.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;

import com.appynews.asynctasks.GetConnectionUrlRssAsyncTask;

/**
 * Clase ConnectionUtils
 * Created by oscar on 18/06/16.
 */
public class ConnectionUtils {


    /**
     * Comprueba si el dispositivo tiene habilitado alguna conexión de red, sean datos
     * móviles, wifi, ...
     * @param application: Activity desde la que se hace la comprobación
     * @return True si hay conexión y false en caso contrario
     */
    public static boolean conexionRedHabilitada(Activity application){
        boolean exito = false;

        try {
            ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                exito = true;
            }

        }catch(Exception e) {
            exito = false;
            e.printStackTrace();
        }
        return exito;
    }


    /**
     * Comprueba si se trata de una url válida
     * @param url String
     * @return boolean
     */
    public static boolean isUrlFormatoValida(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }


    /**
     * Comprueba que desde el dispositivo se pueda establecer conexión HTTP con una determinada url
     * @param actividad Activity desde el que se hace la petición
     * @param url String
     * @return int 0 --> Si la url es correcta y se puede establecer conexión
     *             1 --> No se ha podido establecer conexión con la url
     *             2 --> Si las conexiones de red del dispositivo no están habilitadas
     */
    public static int isOnline(Activity actividad, String url) {
        int salida = 1;

        try {

            if(conexionRedHabilitada(actividad)) {
                GetConnectionUrlRssAsyncTask task = new GetConnectionUrlRssAsyncTask();
                task.execute(url);

                boolean exito = task.get();
                if (exito) {
                    salida = 0;
                }
            } else salida = 2;

        }catch(Exception e) {
            e.printStackTrace();
        }

        return salida;
    }



}
