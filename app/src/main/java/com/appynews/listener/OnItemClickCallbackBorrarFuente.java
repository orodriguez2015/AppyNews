package com.appynews.listener;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import com.appynew.activities.OrigenRssMantenimientoActivity;
import com.appynews.dialog.AlertDialogHelper;
import com.appynews.dialog.BtnAceptarCancelarDialogGenerico;
import com.appynews.controllers.OrigenRssController;
import com.appynews.exceptions.DeleteOrigenesRssException;
import com.appynews.model.dto.OrigenNoticiaVO;
import com.appynews.utils.ConstantesDatos;
import com.appynews.utils.LogCat;

import java.util.List;

import material.oscar.com.materialdesign.R;


/**
 * OnItemClickCallbackBorrarFuente que es invocado cuando un usuario pretende
 * eliminar una determinada fuente de datos
 * Created by oscar on 21/09/16.
 */
public class OnItemClickCallbackBorrarFuente  implements OnItemClickListener.OnItemClickCallback {

    private List<OrigenNoticiaVO> fuentesDatos = null;
    private Activity origenRssMantenimientoActivity = null;

    /**
     * Constructor
     * @param fuentesDatos List<OrigenNoticiaVO>
     * @param actividad Activity
     */
    public OnItemClickCallbackBorrarFuente(List<OrigenNoticiaVO> fuentesDatos, Activity actividad) {
        this.fuentesDatos = fuentesDatos;
        this.origenRssMantenimientoActivity = actividad;
    }


    /**
     * onItemClicked
     * @param view View
     * @param position posición del item del RecyclerView seleccionado por el usuario
     */
    @Override
    public void onItemClicked(View view, int position) {

        final OrigenNoticiaVO fuente = fuentesDatos.get(position);
        LogCat.debug("Pretende eliminar la fuente: " + fuente.getNombre() + " y url: " + fuente.getUrl());

        String mensaje  = origenRssMantenimientoActivity.getString(R.string.pregunta_eliminar_fuente_datos).concat(ConstantesDatos.ESPACIO_BLANCO).concat(fuente.getNombre()).concat(ConstantesDatos.INTERROGANTE);
        String atencion = origenRssMantenimientoActivity.getString(R.string.atencion);

        AlertDialogHelper.crearDialogoAlertaConfirmacion(origenRssMantenimientoActivity,atencion,mensaje, new DialogInterface.OnClickListener(){

            /**
             * onClick
             * @param dialog DialogInterface
             * @param which int
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {

                OrigenRssController controller = new OrigenRssController(origenRssMantenimientoActivity);
                try {
                    controller.borrarOrigenRss(fuente);
                    // Se ha eliminado el origen rss, entonces se comunica a MainActivity
                    if(origenRssMantenimientoActivity instanceof OrigenRssMantenimientoActivity) {
                        ((OrigenRssMantenimientoActivity) origenRssMantenimientoActivity).recargarFuenteDatos();
                    }


                }catch(DeleteOrigenesRssException e) {
                    AlertDialogHelper.crearDialogoAlertaAdvertencia(origenRssMantenimientoActivity,origenRssMantenimientoActivity.getString(R.string.atencion),origenRssMantenimientoActivity.getString(R.string.err_eliminar_origen_dato)).show();
                }
            }

        },new BtnAceptarCancelarDialogGenerico()).show();

    }
}
