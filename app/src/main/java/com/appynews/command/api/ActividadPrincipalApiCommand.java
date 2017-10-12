package com.appynews.command.api;

import android.app.Activity;

import com.appynews.model.dto.Noticia;

import java.util.List;

/**
 * Clase ActividadPrincipalApiCommand con operaciones abstractas que deberá
 * implementar la actividad principal MainActivity
 *
 * <a href="mailto:oscar.rodriguezbrea@gmail.com">Óscar Rodríguez</a>
 */
public interface ActividadPrincipalApiCommand {

    /**
     * Devuelve el activity que implementa esta interface
     * @return Activity
     */
    public Activity getActivity();

    /**
     * Carga las noticias favoritas
     * @param noticias List<Noticia>
     */
    public void cargarFavoritas(List<Noticia> noticias);

}
