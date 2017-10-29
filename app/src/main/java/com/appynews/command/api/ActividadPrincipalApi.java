package com.appynews.command.api;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.appynew.activities.MainActivity;
import com.appynews.model.dto.Noticia;

/**
 * Clase ActividadPrincipalApi con operaciones abstractas que deberá
 * implementar la actividad principal MainActivity
 *
 * <a href="mailto:oscar.rodriguezbrea@gmail.com">Óscar Rodríguez</a>
 */
public interface ActividadPrincipalApi {

    /**
     * Devuelve el activity que implementa esta interface
     * @return MainActivity
     */
    public MainActivity getActivity();

    /**
     * Establece el título de la actividad
     * @param title String
     */
    public void setTitulo(String title);


    /**
     * Devuelve los Resources
     * @return Resources
     */
    public Resources getRecursos();

    /**
     * Devuelve el Context del Activity
     * @return Context
     */
    public Context getContexto();

    /**
     * Devuelve el ImageLoader para cargar las imágenes asociadas a cada noticia
     * @return ImageLoader
     */
    public ImageLoader getImageLoader();

    /**
     * Devuelve true si se están visualizando en este momento las noticias externas, o noticias
     * almacenadas por el usuario en la base de datos
     * @return boolean
     */
    public boolean isMostrandoNoticiasExternas();

    /**
     * Permite indicar si se están mostrando o no noticias externas
     * @param mostrandoNoticiasExternas boolean
     */
    public void setMostrandoNoticiasExternas(boolean mostrandoNoticiasExternas);

    /**
     * Devuelve el RecyclerView
     * @return
     */
    public RecyclerView getRecyclerView();

    /**
     * Este método pasa el control de la actividad MainActivity a la actividad DetalleNoticiaActivity.
     * @param noticia Objeto de la clase Noticia que se pasa a la actividad DetalleNoticiaActivity
     * @param posicion int
     */
    public void cargarActivityDetalleNoticia(Noticia noticia,int posicion);


    /**
     * Permite establecer el nombre de la fuente de datos RSS de la que se muestran las noticias
     * @param nombre String
     */
    public void setNombreOrigenFuenteDatos(String nombre);

}
