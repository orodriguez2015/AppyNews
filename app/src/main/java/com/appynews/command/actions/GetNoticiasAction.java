package com.appynews.command.actions;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appynews.adapter.NoticiasAdapter;
import com.appynews.asynctasks.GetNoticiasExternasAsyncTask;
import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.asynctasks.RespuestaAsyncTask;
import com.appynews.asynctasks.SaveUsuarioAsyncTask;
import com.appynews.command.api.ActividadPrincipalApi;
import com.appynews.controllers.NoticiaController;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.ConnectionUtils;
import com.appynews.utils.LogCat;
import com.appynews.utils.MessageUtils;
import com.appynews.utils.PermissionsUtil;
import com.appynews.utils.TelephoneUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import material.oscar.com.materialdesign.R;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

/**
 * Clase que permite recuperar las noticias de una fuente de datos RSS, así como las noticias favoritas
 * almacenadas por el usuario en la base de datos
 *
 * Created by oscar on 22/10/17.
 */
public class GetNoticiasAction {
    private ActividadPrincipalApi api = null;
    private NoticiasAdapter noticiasAdapter = null;

    public GetNoticiasAction(ActividadPrincipalApi api) {
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
            GetNoticiasExternasAsyncTask tarea = new GetNoticiasExternasAsyncTask(api.getActivity());
            tarea.execute(params);

            api.setTitulo(origen);
                /*
                 * Se muestra las noticias a través del NoticiasAdapter
                 */
            mostrarNoticias(tarea.get());

        }catch(Exception e) {
            e.printStackTrace();
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


    /**
     * Carga las
     * @return List<Noticia>
     */
    public void cargarNoticiasFavoritas() {
        List<Noticia> favoritas = new ArrayList<Noticia>();

        Activity actividad = api.getActivity();

        if(!PermissionsUtil.appTienePermiso(actividad, ACCESS_NETWORK_STATE)) {
            // Se comprueba si la app tiene permiso de acceso al estado de red del dispositivo, sino
            // se dispone del permiso, entonces se informa al usuario
            MessageUtils.showToastDuracionLarga(actividad,actividad.getString(R.string.err_permisssion_network_state));
        } else {

            if (!ConnectionUtils.conexionRedHabilitada(actividad)) {
                // Si el dispositivo no tiene habilitado ninguna conexión de red, hay que informar al usuario
                MessageUtils.showToastDuracionLarga(actividad,actividad.getString(R.string.err_connection_state));

            } else {

                /************************************************************/
                /**** Se almacenan los datos del dispositivo en la BBDD *****/
                /************************************************************/
                try {

                    final RecyclerView recyclerView = this.api.getRecyclerView();

                    ParametrosAsyncTask params = new ParametrosAsyncTask();
                    params.setContext(actividad.getApplicationContext());
                    params.setUsuario(TelephoneUtil.getInfoDispositivo(actividad.getApplicationContext()));
                    SaveUsuarioAsyncTask asyncTask = new SaveUsuarioAsyncTask();
                    asyncTask.execute(params);

                    RespuestaAsyncTask res = asyncTask.get();
                    if(res.getStatus()==0) {
                        LogCat.debug("Datos del teléfono/usuario grabados en BBDD");
                        NoticiaController noticiaController = new NoticiaController(actividad);
                        favoritas = noticiaController.getNoticiasFavoritas();
                        final List<Noticia> listadoFavoritas = favoritas;

                        NoticiasAdapter noticiaAdapter =  new NoticiasAdapter(favoritas,null,this.api.getImageLoader(),this.api.getRecursos());
                        noticiaAdapter.notifyDataSetChanged();
                        this.api.setMostrandoNoticiasExternas(false);

                        if(favoritas==null || favoritas.size()==0) {
                            MessageUtils.showToastDuracionCorta(actividad,actividad.getString(R.string.msg_no_hay_noticias_favoritas));
                        }

                        /**
                         * Se establece el listener que se pasa al adapter para que añade
                         * este Listener a cada View a mostrar en el RecyclerView
                         */
                        noticiaAdapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int pos =  recyclerView.getChildAdapterPosition(view);
                                GetNoticiasAction.this.api.cargarActivityDetalleNoticia(listadoFavoritas.get(pos),pos);
                            }
                        });

                        recyclerView.setAdapter(noticiaAdapter);
                        actividad.setTitle(actividad.getString(R.string.favoritos));

                        /******/

                    } else {
                        LogCat.error("Se ha producido un error al grabar el teléfono/usuario grabados en BBDD ".concat(res.getDescStatus()));
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LogCat.error("Error al ejecutar tarea asíncrona de grabación de usuario en BD: ".concat(e.getMessage()));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    LogCat.error("Error al ejecutar tarea asíncrona de grabación de usuario en BD: ".concat(e.getMessage()));
                }
            }
        }
    }

}
