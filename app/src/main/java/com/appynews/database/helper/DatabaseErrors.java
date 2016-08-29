package com.appynews.database.helper;

/**
 * Códigos de error para las diferentes operaciones ejecutadas
 * contra la base de datos
 *
 * Created by oscar on 25/08/16.
 */
public class DatabaseErrors {

    public static final int OK                         = 0;
    public static final int ERROR_CREAR_BASEDATOS      = 1;
    public static final int ERROR_ACTUALIZAR_BASEDATOS = 2;
    public static final int ERROR_INSERTAR_USUARIO     = 3;
    public static final int ERROR_ACTUALIZAR_USUARIO   = 4;
    public static final int ERROR_ELIMINAR_USUARIO     = 5;
    public static final int ERROR_EXISTENCIA_USUARIO   = 6;
    public static final int ERROR_RECUPERAR_USUARIOS   = 7;
    public static final int ERROR_INSERTAR_NOTICIA     = 8;
    public static final int ERROR_RECUPERAR_NOTICIAS   = 9;

}
