package com.com.appynews.database.columns;

import android.provider.BaseColumns;

/**
 * Clase AppyNewsContract que contendrá la definición de las tablas de la base de datos
 * Created by oscar on 20/08/16.
 */
public class AppyNewsContract  {

    /**
     * Definición de las columnas para la tabla Noticia de la base de datos
     */
    public static abstract class NoticiaEntry implements BaseColumns {

        public static final String TABLE_NAME           ="noticia";
        public static final String ID                   = "id";
        public static final String TITULO               = "titulo";
        public static final String DESCRIPCION          = "descripcion";
        public static final String DESCRIPCION_COMPLETA = "descripcionCompleta";
        public static final String FECHA_PUBLICACION    = "fechaPublicacion";
        public static final String ORIGEN               = "origen";
        public static final String URL                  = "url";
    }

    /**
     * Definición de las columnas de la tabla Usuario de la base de datos
     */
    public static abstract class UsuarioEntry implements BaseColumns {

        public static final String TABLE_NAME           = "usuario";
        public static final String ID                   = "id";
        public static final String TELEFONO             = "telefono";
        public static final String IMEI                 = "imei";
        public static final String EMAIL                = "email";
        public static final String REGION_ISO           = "regionIso";
        public static final String NOMBRE               = "nombre";
        public static final String APELLIDO1            = "apellido1";
        public static final String APELLIDO2            = "apellido2";
    }


    /**
     * Definición de las columnas de la tabla Origen de la base de datos
     */
    public static abstract class OrigenEntry implements BaseColumns {

        public static final String TABLE_NAME           = "origen";
        public static final String DESCRIPCION          = "descripcion";
        public static final String URL                  = "url";
    }

}