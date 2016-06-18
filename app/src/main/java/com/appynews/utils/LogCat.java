package com.appynews.utils;

import android.util.Log;

/**
 * Clase LogCat con operaciones que permitan mostrar
 * mensajes de log
 * @author oscar
 */
public class LogCat {

    private static final String TAG = "AppyNews";

    /**
     * Muestra mensajes de tipo debug
     * @param message: String
     */
    public static void debug(String message){
        Log.d(TAG,message);
    }

    /**
     * Muestra mensajes de tipo error
     * @param message: String
     */
    public static void error(String message){
        Log.e(TAG,message);
    }

    /**
     * Muestra mensajes de tipo info
     * @param message: String
     */
    public static void info(String message){
        Log.i(TAG,message);
    }
}