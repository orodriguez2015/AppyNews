package com.appynews.database.conversor;

import android.content.ContentValues;

import com.appynews.model.dto.DatosUsuarioVO;
import com.appynews.model.dto.Noticia;
import com.appynews.database.colums.AppyNewsContract;
import com.appynews.model.dto.OrigenNoticiaVO;

/**
 * Clase ModelConversorUtil que dispone de operaciones que convierte un objeto VO en
 * uno de tipo ContentValues necesario para persistir el objeto original en la base de
 * datos SQLite
 * Created by oscar on 22/08/16.
 */
public class ModelConversorUtil {


    /**
     * Convierte un objeto de la clase OrigenNoticiaVO en el ContentValues que se usará para
     * persistir la información en la base de datos
     * @param origen OrigenNoticiaVO
     * @return ContentValues
     */
    public static ContentValues toContentValues(OrigenNoticiaVO origen) {

        ContentValues values = new ContentValues();
        values.put(AppyNewsContract.OrigenEntry.DESCRIPCION,origen.getNombre());
        values.put(AppyNewsContract.OrigenEntry.URL,origen.getUrl());
        return values;
    }



    /**
     * Convierte un objeto de la clase Noticia en el ContentValues que se usará para
     * persistir la información en la base de datos
     * @param noticia Noticia
     * @return ContentValues
     */
    public static ContentValues toContentValues(Noticia noticia) {

        ContentValues values = new ContentValues();
        values.put(AppyNewsContract.NoticiaEntry.TITULO,noticia.getTitulo());
        values.put(AppyNewsContract.NoticiaEntry.DESCRIPCION,noticia.getDescripcion());
        values.put(AppyNewsContract.NoticiaEntry.DESCRIPCION_COMPLETA,noticia.getDescripcionCompleta());
        values.put(AppyNewsContract.NoticiaEntry.URL,noticia.getUrl());
        values.put(AppyNewsContract.NoticiaEntry.URL_IMAGEN,noticia.getUrlThumbnail());
        values.put(AppyNewsContract.NoticiaEntry.ORIGEN,noticia.getOrigen());
        values.put(AppyNewsContract.NoticiaEntry.FECHA_PUBLICACION,noticia.getFechaPublicacion());
        return values;
    }



    /**
     * Convierte un objeto de la clase Noticia en el ContentValues que se usará para
     * persistir la información en la base de datos
     * @param telefono DatosUsuarioVO
     * @return ContentValues
     */
    public static ContentValues toContentValues(DatosUsuarioVO telefono) {

        ContentValues values = new ContentValues();
        values.put(AppyNewsContract.UsuarioEntry._ID,telefono.getId());
        values.put(AppyNewsContract.UsuarioEntry.IMEI,telefono.getImei());
        values.put(AppyNewsContract.UsuarioEntry.TELEFONO,telefono.getNumeroTelefono());
        values.put(AppyNewsContract.UsuarioEntry.REGION_ISO,telefono.getRegionIso());
        values.put(AppyNewsContract.UsuarioEntry.EMAIL,telefono.getEmail());
        values.put(AppyNewsContract.UsuarioEntry.NOMBRE,telefono.getNombre());
        values.put(AppyNewsContract.UsuarioEntry.APELLIDO1,telefono.getApellido1());
        values.put(AppyNewsContract.UsuarioEntry.APELLIDO2,telefono.getApellido2());
        values.put(AppyNewsContract.UsuarioEntry.APELLIDO2,telefono.getApellido2());
        values.put(AppyNewsContract.UsuarioEntry.MARCA_DISPOSITIVO,telefono.getMarcaDispositivo());
        values.put(AppyNewsContract.UsuarioEntry.MODELO_DISPOSITIVO,telefono.getModeloDispositivo());
        values.put(AppyNewsContract.UsuarioEntry.NUMERO_SERIE_DISPOSITIVO,telefono.getNumeroSerieDispositivo());
        values.put(AppyNewsContract.UsuarioEntry.HARDWARE_DISPOSITIVO,telefono.getHardwareDispositivo());
        return values;
    }
}
