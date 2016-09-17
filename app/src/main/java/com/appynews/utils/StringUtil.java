package com.appynews.utils;

/**
 * Created by oscar on 26/06/16.
 */
public class StringUtil {


    /**
     * Comprueba si un String es nulo o contiene la cadena vacía
     * @param dato: String
     * @return boolean
     */
    public static boolean isEmpty(String dato) {
        boolean exito = false;

        if(dato==null || "".equalsIgnoreCase(dato.trim()) || dato.trim().length()==0) {
            exito = true;
        }

        return exito;
    }


    /**
     * Comprueba si un String es no nulo y no contiene cadena vacía
     * @param dato: String
     * @return boolean
     */
    public static boolean isNotEmpty(String dato) {
        return !isEmpty(dato);
    }


    /**
     * Comprueba si un String es nulo
     * @param dato: String
     * @return boolean
     */
    public static boolean isNull(String dato) {
        boolean exito = false;

        if(dato==null) {
            exito = false;
        }
        return exito;
    }
}
