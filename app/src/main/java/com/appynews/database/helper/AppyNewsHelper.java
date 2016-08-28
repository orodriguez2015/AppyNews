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

import java.util.ArrayList;
import java.util.List;

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
                    + AppyNewsContract.NoticiaEntry.DESCRIPCION + " TEXT,"
                    + AppyNewsContract.NoticiaEntry.TITULO + " TEXT,"
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
    public void saveNoticia(Noticia noticia) throws SQLiteException {
        SQLiteDatabase db = getWritableDatabase();

        try {
            LogCat.info("saveNoticia init");
            Long id = db.insert(AppyNewsContract.NoticiaEntry.TABLE_NAME, null, ModelConversorUtil.toContentValues(noticia));
            noticia.setId(id.intValue());
            LogCat.info("saveNoticia end");


        } catch(Exception e) {
            e.printStackTrace();
            throw new SQLiteException(DatabaseErrors.ERROR_INSERTAR_NOTICIA,"Error al grabar una noticia en la base de datos: ".concat(e.getMessage()));
        } finally {
            if(db!=null) {
                db.close();
            }
        }
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
            Long id = db.insert(AppyNewsContract.UsuarioEntry.TABLE_NAME, null, ModelConversorUtil.toContentValues(usuario));
            usuario.setId(id.intValue());
            LogCat.info("saveUsuario end");

        } catch(Exception e) {
            e.printStackTrace();
            throw new SQLiteException(DatabaseErrors.ERROR_INSERTAR_USUARIO,"Error al grabar un usuario en la base de datos: ".concat(e.getMessage()));
        } finally {
            if(db!=null) {
                db.close();
            }
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

            LogCat.debug("sql: " + sql);
            db = getReadableDatabase();
            rs = db.rawQuery(sql,new String[]{usuario.getImei(),usuario.getEmail(),usuario.getRegionIso()});


            if(rs!=null && rs.getCount()>0 && rs.moveToFirst()) {
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

            LogCat.debug("Existe el usuario/telefono en base de datos: " + exito);

        } catch(Exception e) {
            e.printStackTrace();
            throw new SQLiteException(DatabaseErrors.ERROR_EXISTENCIA_USUARIO,"Error al comprobar la existencia del usuario: ".concat(e.getMessage()));
        } finally {
            if(rs!=null) rs.close();
            if(db!=null) db.close();
        }

        return exito;
    }


    /**
     * Recupera las noticias de la base de datos
     * @return List<Noticia>
     * @throws SQLiteException
     */
    public List<Noticia> getNoticias() throws SQLiteException {
        List<Noticia> noticias = new ArrayList<Noticia>();
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            LogCat.info("getNoticias() init");
            String sql = "select _id,titulo,descripcion,descripcionCompleta,fechaPublicacion,origen,url from noticia order by fechaPublicacion desc";
            LogCat.debug("sql: " + sql);

            db = getReadableDatabase();
            rs = db.rawQuery(sql,null);



            if(rs!=null && rs.getCount()>0 && rs.moveToFirst()) {
                LogCat.debug("Numero noticias recuperadas: " + rs.getCount());

                do {
                    Noticia noticia = new Noticia();
                    noticia.setId(rs.getInt(0));
                    noticia.setTitulo(rs.getString(1));
                    noticia.setDescripcion(rs.getString(2));
                    noticia.setDescripcionCompleta(rs.getString(3));
                    noticia.setFechaPublicacion(rs.getString(4));
                    noticia.setOrigen(rs.getString(5));
                    noticia.setUrl(rs.getString(6));
                    noticias.add(noticia);

                } while(rs.moveToNext());

            } else
                LogCat.debug("No existe el usuario");

            LogCat.info("getNoticias() end");

        } catch(Exception e) {
            e.printStackTrace();
            throw new SQLiteException(DatabaseErrors.ERROR_RECUPERAR_NOTICIAS,"Error al recuperar las noticias de la base de datos: ".concat(e.getMessage()));
        } finally {
            if(rs!=null) rs.close();
            if(db!=null) db.close();
        }
        return noticias;
    }
}
