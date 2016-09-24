package com.appynews.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;

import com.appynews.asynctasks.GetInputStreamNewsConnectionTask;

import java.io.InputStream;

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
     * Comprueba si se puede establecer conexión con la url
     * @param url String
     * @return true si se ha establecido conexión y false en caso contrario
     */
    public static boolean connectUrl(String url) {

        boolean connect = false;
        InputStream is  = null;
        try {
            GetInputStreamNewsConnectionTask task = new GetInputStreamNewsConnectionTask();
            task.execute(url);
            is = task.get();

            if(is!=null) {
                connect = true;
            }

        } catch(Exception e) {
            try {
                e.printStackTrace();
                connect = false;

                if (is != null) {
                    is.close();
                }
            }catch(Exception ex) {

            }
        }

        return connect;
    }



}
