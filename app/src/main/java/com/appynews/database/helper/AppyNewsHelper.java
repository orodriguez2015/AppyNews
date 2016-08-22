package com.appynews.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appynews.db.columns.AppyNewsContract;

/**
 * Clase AppyNewsHelper encargada de crear la base de datos SQLite y de actualizarla
 * cuando sea preciso
 * Created by oscar on 20/08/16.
 */
public class AppyNewsHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AppyNews.db";


    /**
     * Constructor
     * @param context: Objeto de la clase Context
     */
    public AppyNewsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Método onCreate que se encarga de crear la base de datos
     * @param sqLiteDatabase: Manejador de la base de datos
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Se crea la tabla Noticia
        sqLiteDatabase.execSQL("CREATE TABLE " + AppyNewsContract.NoticiaEntry.TABLE_NAME + " ("
                + AppyNewsContract.NoticiaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + AppyNewsContract.NoticiaEntry.DESCRIPCION + " TEXT NOT NULL,"
                + AppyNewsContract.NoticiaEntry.TITULO + " TEXT NOT NULL,"
                + AppyNewsContract.NoticiaEntry.DESCRIPCION_COMPLETA + " TEXT,"
                + AppyNewsContract.NoticiaEntry.ORIGEN + " TEXT NOT NULL,"
                + AppyNewsContract.NoticiaEntry.FECHA_PUBLICACION + " DATE,"
                + AppyNewsContract.NoticiaEntry.URL + " TEXT)");
                //+ "UNIQUE (" + AppyNewsContract.NoticiaEntry._ID + "))");


        // Se crea la tabla Usuario
        sqLiteDatabase.execSQL("CREATE TABLE " + AppyNewsContract.UsuarioEntry.TABLE_NAME + " ("
                + AppyNewsContract.UsuarioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + AppyNewsContract.UsuarioEntry.NOMBRE + " TEXT,"
                + AppyNewsContract.UsuarioEntry.APELLIDO1 + " TEXT,"
                + AppyNewsContract.UsuarioEntry.APELLIDO2 + " TEXT,"
                + AppyNewsContract.UsuarioEntry.IMEI + " TEXT,"
                + AppyNewsContract.UsuarioEntry.TELEFONO + " TEXT)");
                //+ "UNIQUE (" + AppyNewsContract.NoticiaEntry._ID + "))");
    }


    /**
     * Método onUpgrade que se encarga de actualizar la base de datos
     * @param sqLiteDatabase: Manejador de la base de datos
     * @param i: Versión antigua de la base de datos
     * @param i1: Nueva versión de la base de datos
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
