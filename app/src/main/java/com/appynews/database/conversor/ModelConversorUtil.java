package com.appynews.database.conversor;

import android.content.ContentValues;

import com.appynews.model.dto.DatosUsuarioVO;
import com.appynews.model.dto.Noticia;
import com.appynews.database.colums.AppyNewsContract;

/**
 * Created by oscar on 22/08/16.
 */
public class ModelConversorUtil {


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
        return values;
    }
}
