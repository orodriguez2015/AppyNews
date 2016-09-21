package com.appynews.listener;

import android.view.View;

/**
 * Clase OnItemClickListener utilizado para detectar el evento onClick sobre elementos que
 * se encuentran dentro de un item de un RecyclerView, por eso se pasa la posición
 *
 * Created by oscar on 21/09/16.
 */
public class OnItemClickListener implements View.OnClickListener {
    private int position;
    private OnItemClickCallback onItemClickCallback;

    /**
     * Constructor
     * @param position int que contiene la posición del elemento seleccionado en el ReclycerView
     * @param onItemClickCallback OnItemClickCallback
     */
    public OnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
    }


    /**
     * Método onClick a implementar y que procede la interfaz View.OnClickListener
     * @param view View
     */
    @Override
    public void onClick(View view) {
        onItemClickCallback.onItemClicked(view, position);
    }

    /**
     * Interface OnItemClickCallback
     */
    public interface OnItemClickCallback {
        void onItemClicked(View view, int position);
    }
}