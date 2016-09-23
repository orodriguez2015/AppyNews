package com.appynews.listener;

import android.app.Activity;
import android.view.View;

import com.appynew.activities.OrigenRssMantenimientoActivity;
import com.appynews.model.dto.OrigenNoticiaVO;

import java.util.List;

/**
 * OnItemClickCallbackEditarFuente que es invocado cuando un usuario pretende
 * editar una determinada fuente de datos
 * Created by oscar on 21/09/16.
 */
public class OnItemClickCallbackEditarFuente implements OnItemClickListener.OnItemClickCallback {

    private List<OrigenNoticiaVO> fuentesDatos = null;
    private Activity actividad = null;


    /**
     * Constructor
     * @param fuentesDatos List<OrigenNoticiaVO>
     * @param actividad Activity desde el que llega la petición de edición
     */
    public OnItemClickCallbackEditarFuente(List<OrigenNoticiaVO> fuentesDatos,Activity actividad) {
        this.fuentesDatos = fuentesDatos;
        this.actividad    = actividad;
    }


    /**
     * onItemClicked
     * @param view View
     * @param position posición del item del RecyclerView seleccionado por el usuario
     */
    @Override
    public void onItemClicked(View view, int position) {
        ((OrigenRssMantenimientoActivity)actividad).showActivityEditarOrigenRss(fuentesDatos.get(position));
    }



}
