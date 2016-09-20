package com.appynews.exceptions;

/**
 * Excepción que se lanza cuando se ha producido un error al recuperar origenes de datos
 * de la base de datos
 * Created by oscar on 20/09/16.
 */
public class GetOrigenesRssException extends Exception {

    /**
     * Constructor
     * @param message String
     */
    public GetOrigenesRssException(String message) {
        super(message);
    }


    /**
     * Constructor
     * @param message String
     * @param t Throwable
     */
    public GetOrigenesRssException(String message,Throwable t) {
        super(message,t);
    }

}
