package com.appynews.asynctasks;

import android.content.Context;

import com.appynews.model.dto.DatosUsuarioVO;
import com.appynews.model.dto.Noticia;
import com.appynews.model.dto.OrigenNoticiaVO;

import java.io.Serializable;
import java.util.List;

/**
 * Clase ParametrosAsyncTask utilizada para el paso de parámetros en aquellas
 * tareas AsyncTask para las que sea necesaria.
 *
 * Created by oscar on 26/08/16.
 */
public class ParametrosAsyncTask implements Serializable {

    private Context context        = null;
    private DatosUsuarioVO usuario = null;
    private Noticia noticia        = null;
    private OrigenNoticiaVO origen = null;
    private List<Integer> idsOrigenesEliminar = null;

    /**
     * Establece la lista con los ids de los orígenes de datos a eliminar
     * @param idsOrigenesEliminar List<Integer>
     */
    public void setIdsOrigenesEliminar(List<Integer> idsOrigenesEliminar) {
        this.idsOrigenesEliminar = idsOrigenesEliminar;
    }



    /**
     * Devuelve la lista con los ids de los orígenes de datos a eliminar
     * @return List<Integer>
     */
    public List<Integer> getIdsOrigenesEliminar() {
        return idsOrigenesEliminar;
    }


    /**
     * Devuelve el Context
     * @return Context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Establece el Context
     * @param context: Context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Devuelve los datos del usuario
     * @return DatosUsuarioVO
     */
    public DatosUsuarioVO getUsuario() {
        return usuario;
    }

    /**
     * Establece los datos del usuario
     * @param usuario: Context
     */
    public void setUsuario(DatosUsuarioVO usuario) {
        this.usuario = usuario;
    }


    /**
     * Devuelve la noticia que se va a almacenar en base de datos
     * @return Noticia
     */
    public Noticia getNoticia() {
        return noticia;
    }

    /**
     * Establece la noticia que se va a almacenar en base de datos
     * @return Noticia
     */
    public void setNoticia(Noticia noticia) {
        this.noticia = noticia;
    }

    /**
     * Devuelve el origen/fuente de datos
     * @return OrigenNoticiaVO
     */
    public OrigenNoticiaVO getOrigen() {
        return origen;
    }


    /**
     * Establece el  origen/fuente de datos
     * @param origen OrigenNoticiaVO
     */
    public void setOrigen(OrigenNoticiaVO origen) {
        this.origen = origen;
    }
}
