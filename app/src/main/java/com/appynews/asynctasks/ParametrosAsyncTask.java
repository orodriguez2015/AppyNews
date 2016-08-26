package com.appynews.asynctasks;

import android.content.Context;

import com.appynews.model.dto.DatosUsuarioVO;

import java.io.Serializable;

/**
 * Clase ParametrosAsyncTask utilizada para el paso de parámetros en aquellas
 * tareas AsyncTask para las que sea necesaria.
 *
 * Created by oscar on 26/08/16.
 */
public class ParametrosAsyncTask implements Serializable {

    private Context context        = null;
    private DatosUsuarioVO usuario = null;

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
}
