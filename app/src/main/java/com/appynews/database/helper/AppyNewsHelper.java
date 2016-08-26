package com.appynews.database.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appynews.database.com.appynews.database.exception.SQLiteException;
import com.appynews.database.conversor.ModelConversorUtil;
import com.appynews.model.dto.DatosUsuarioVO;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.LogCat;
import com.com.appynews.database.columns.AppyNewsContract;

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
    public void onCreate(SQLiteDatabase sqLiteDatabase){

        try {
            LogCat.info("onCreate init()");
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
                    + AppyNewsContract.UsuarioEntry.REGION_ISO + " TEXT,"
                    + AppyNewsContract.UsuarioEntry.EMAIL + " TEXT,"
                    + AppyNewsContract.UsuarioEntry.TELEFONO + " TEXT)");
            //+ "UNIQUE (" + AppyNewsContract.NoticiaEntry._ID + "))");

            LogCat.info("onCreate end()");
        }catch(Exception e) {
            e.printStackTrace();
            LogCat.error("Se ha producido un error al crear la BD en el método onCreate: ".concat(e.getMessage()));
        }
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


    /**
     * Graba una noticia en la base de datos
     * @param noticia: Noticia
     */
    public void saveNoticia(Noticia noticia) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(AppyNewsContract.NoticiaEntry.TABLE_NAME,null, ModelConversorUtil.toContentValues(noticia));
        db.close();
    }


    /**
     * Graba una noticia en la base de datos
     * @param usuario: Objeto que contiene los datos a almacenar del dispositivo del usuario
     */
    public void saveUsuario(DatosUsuarioVO usuario) throws SQLiteException {
        SQLiteDatabase db = null;

        try {
            LogCat.info("saveUsuario init");
            db = getWritableDatabase();
            db.insert(AppyNewsContract.UsuarioEntry.TABLE_NAME, null, ModelConversorUtil.toContentValues(usuario));
            LogCat.info("saveUsuario end");

        } catch(Exception e) {
            e.printStackTrace();
            throw new SQLiteException(DatabaseErrors.ERROR_INSERTAR_USUARIO,"Error al grabar un usuario en la base de datos: ".concat(e.getMessage()));
        } finally {
            db.close();
        }
    }


    /**
     * Comprueba si existe un determinado usuario en la base de datos
     * @param usuario: DatosUsuarioVO
     * @return True si existe y false en caso contrario
     * @throws SQLiteException si se ha producido algún error
     */
    public boolean existeUsuario(DatosUsuarioVO usuario) throws SQLiteException {
        SQLiteDatabase db = null;
        Cursor rs = null;
        boolean exito = false;

        try {

            String sql = "select count(*) as num from usuario where imei=? and email=? and regionIso=?";

            System.out.println("sql: " + sql);
            db = getReadableDatabase();
            rs = db.rawQuery(sql,new String[]{usuario.getImei(),usuario.getEmail(),usuario.getRegionIso()});


            if(rs!=null && rs.getCount()>0 && rs.moveToFirst()) {
                LogCat.debug("A comprobar si existe el usuario");
                do {
                    int num = rs.getInt(0);
                    LogCat.debug("Numero " + num);
                    if(num>=1) {
                        exito = true;
                        break;
                    }

                }while(rs.moveToNext());

            } else
                LogCat.debug("No existe el usuario");




        } catch(Exception e) {
            e.printStackTrace();
            throw new SQLiteException(DatabaseErrors.ERROR_EXISTENCIA_USUARIO,"Error al comprobar la existencia del usuario: ".concat(e.getMessage()));
        } finally {
            if(rs!=null) rs.close();
            if(db!=null) db.close();
        }

        return exito;
    }


    public void getNoticias() {
        SQLiteDatabase db = getReadableDatabase();

    }
}
