package com.appynews.exceptions;

/**
 * Excepción que se lanza cuando se produce un error al eliminar un determinado orígen
 * de datos
 *
 * Created by oscar on 21/09/16.
 */
public class DeleteOrigenesRssException extends Exception {

    /**
     * Constructor
     */
    public DeleteOrigenesRssException() {
        super();
    }


    /**
     * Constructor
     * @param message String
     */
    public DeleteOrigenesRssException(String message) {
        super(message);
    }


    /**
     * Constructor
     * @param message String
     * @param t Throwable
     */
    public DeleteOrigenesRssException(String message,Throwable t) {
        super(message,t);
    }
}
