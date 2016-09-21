package com.appynews.listener;

import android.view.View;

import com.appynews.model.dto.OrigenNoticiaVO;

import java.util.List;

/**
 * OnItemClickCallbackEditarFuente que es invocado cuando un usuario pretende
 * editar una determinada fuente de datos
 * Created by oscar on 21/09/16.
 */
public class OnItemClickCallbackEditarFuente implements OnItemClickListener.OnItemClickCallback {

    private List<OrigenNoticiaVO> fuentesDatos = null;

    /**
     * Constructor
     * @param fuentesDatos List<OrigenNoticiaVO>
     */
    public OnItemClickCallbackEditarFuente(List<OrigenNoticiaVO> fuentesDatos) {
        this.fuentesDatos = fuentesDatos;
    }


    /**
     * onItemClicked
     * @param view View
     * @param position posición del item del RecyclerView seleccionado por el usuario
     */
    @Override
    public void onItemClicked(View view, int position) {

    }



}
