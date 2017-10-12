package com.appynews.command.actions;

import android.app.Activity;

import com.appynews.asynctasks.ParametrosAsyncTask;
import com.appynews.asynctasks.RespuestaAsyncTask;
import com.appynews.asynctasks.SaveUsuarioAsyncTask;
import com.appynews.command.api.ActividadPrincipalApiCommand;
import com.appynews.controllers.NoticiaController;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.ConnectionUtils;
import com.appynews.utils.LogCat;
import com.appynews.utils.MessageUtils;
import com.appynews.utils.PermissionsUtil;
import com.appynews.utils.TelephoneUtil;

import java.util.List;
import java.util.concurrent.ExecutionException;

import material.oscar.com.materialdesign.R;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

/**
 * Clase RecuperarNoticiasFavoritasCommandAction
 * <a href="mailto:oscar.rodriguezbrea@gmail.com">Óscar Rodríguez</a>
 */
public class RecuperarNoticiasFavoritasCommandAction implements CommandAction {

    private ActividadPrincipalApiCommand command =null;

    /**
     * Constructor
     * @param command ActividadPrincipalApiCommand a la que está asociada el comando
     */
    public RecuperarNoticiasFavoritasCommandAction(ActividadPrincipalApiCommand command) {
        this.command = command;
    }

    /**
     * Método que ejecutar la acción
     * @return List<Noticia>
     */
    public Object execute() {
        List<Noticia> favoritas = null;

        Activity activity = command.getActivity();

        if(!PermissionsUtil.appTienePermiso(activity, ACCESS_NETWORK_STATE)) {
            // Se comprueba si la app tiene permiso de acceso al estado de red del dispositivo, sino
            // se dispone del permiso, entonces se informa al usuario
            MessageUtils.showToastDuracionLarga(activity,activity.getString(R.string.err_permisssion_network_state));
        } else {

            if (!ConnectionUtils.conexionRedHabilitada(activity)) {
                // Si el dispositivo no tiene habilitado ninguna conexión de red, hay que informar al usuario
                MessageUtils.showToastDuracionLarga(activity,activity.getString(R.string.err_connection_state));

            } else {

                /************************************************************/
                /**** Se almacenan los datos del dispositivo en la BBDD *****/
                /************************************************************/
                try {
                    ParametrosAsyncTask params = new ParametrosAsyncTask();
                    params.setContext(activity.getApplicationContext());
                    params.setUsuario(TelephoneUtil.getInfoDispositivo(activity.getApplicationContext()));
                    SaveUsuarioAsyncTask asyncTask = new SaveUsuarioAsyncTask();
                    asyncTask.execute(params);

                    RespuestaAsyncTask res = asyncTask.get();
                    if(res.getStatus()==0) {
                        LogCat.debug("Datos del teléfono/usuario grabados en BBDD");
                        NoticiaController noticiaController = new NoticiaController(activity);
                        favoritas = noticiaController.getNoticiasFavoritas();
                        // Carga las noticias favoritas en el MainActivity
                        command.cargarFavoritas(favoritas);

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

        return favoritas;
    }

}
