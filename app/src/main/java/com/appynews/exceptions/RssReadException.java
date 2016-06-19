package com.appynews.exceptions;

/**
 * Excepción que se lanza cuando se ha producido un error
 * al leer un origen de datos RSS
 *
 * Created by oscar on 18/06/16.
 */
public class RssReadException extends Exception {

    /**
     * Constructor
     * @param message: Mensaje
     */
    public RssReadException(String message){
        super(message);
    }

    /**
     * Constructor
     * @param message: Mensaje
     * @param t: Throwable
     */
    public RssReadException(String message,Throwable t){
        super(message,t);
    }

    /**
     * Constructor
     * @param t: Throwable
     */
    public RssReadException(Throwable t){
        super(t);
    }

}
