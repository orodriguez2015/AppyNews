package com.appynews.command.actions;

import android.view.View;

import com.appynews.adapter.NoticiasAdapter;
import com.appynews.asynctasks.GetNoticiasExternasAsyncTask;
import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.command.api.ActividadPrincipalApi;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.LogCat;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que permite recuperar las noticias de una fuente de datos RSS
 * Created by oscar on 22/10/17.
 */
public class GetNoticiasCommandAction {
    private ActividadPrincipalApi api = null;
    private NoticiasAdapter noticiasAdapter = null;

    public GetNoticiasCommandAction(ActividadPrincipalApi api) {
        this.api = api;
    }

    /**
     * Devuelve el NoticiasAdapter utilizado para mostrar las noticias
     * @return NoticiasAdapter
     */
    public NoticiasAdapter getNoticiasAdapter() {
        return this.noticiasAdapter;
    }


    /**
     * Recupera la noticias de una determinada url para mostrarselas al usuario
     * @param url String con la url
     * @param origen Nombre de la fuente de datos (Menéame, GenBeta, ... )
     */
    public void cargarNoticias(String url,String origen) {
        api.setNombreOrigenFuenteDatos(origen);
        api.setMostrandoNoticiasExternas(true);

        // Se recupera la lista de noticias a través
        List<Noticia> noticias = new ArrayList<Noticia>();

        this.noticiasAdapter =  new NoticiasAdapter(noticias,origen,api.getImageLoader(),api.getRecursos());
        api.getRecyclerView().setAdapter(noticiasAdapter);

        try {
            // Se recuperan las noticias
            ParametrosAsyncTask params = new ParametrosAsyncTask();
            params.setUrl(url);
            GetNoticiasExternasAsyncTask tarea = new GetNoticiasExternasAsyncTask(api.getContexto());
            tarea.execute(params);

            api.setTitulo(origen);
                /*
                 * Se muestra las noticias a través del NoticiasAdapter
                 */
            mostrarNoticias(tarea.get());

        }catch(Exception e) {
            LogCat.error("Se ha producido un error al recuperar las noticias de la url " + url + ": " + e.getMessage());
        }


        /**
         * Se establece el listener que se pasa al adapter para que añade
         * este Listener a cada View a mostrar en el RecyclerView
         */
        this.noticiasAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos =  api.getRecyclerView().getChildAdapterPosition(view);
                api.cargarActivityDetalleNoticia(noticiasAdapter.getNoticias().get(pos),pos);
            }
        });
    }


    /**
     * Muestra las noticias en el Adapter correspondiente
     * @param noticias List<Noticia>
     */
    private void mostrarNoticias(List<Noticia> noticias) {
        noticiasAdapter.setNoticias(noticias);
        noticiasAdapter.notifyDataSetChanged();
    }

}
