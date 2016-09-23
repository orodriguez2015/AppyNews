package com.appynews.exceptions;

/**
 * Excepción que se lanzará cuando se produzca un error al actualizar un orígen/fuente de datos
 * Created by oscar on 23/09/16.
 */
public class UpdateOrigenRssException extends Exception {

    /**
     * Constructor
     */
    public UpdateOrigenRssException() {
        super();
    }

    /**
     * Constructor
     * @param message String
     */
    public UpdateOrigenRssException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param message String
     * @param t Throwable
     */
    public UpdateOrigenRssException(String message,Throwable t) {
        super(message,t);
    }
}
