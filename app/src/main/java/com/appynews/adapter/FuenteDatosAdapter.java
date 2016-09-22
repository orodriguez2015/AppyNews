package com.appynews.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.appynew.activities.OrigenRssMantenimientoActivity;
import com.appynews.listener.OnItemClickCallbackBorrarFuente;
import com.appynews.listener.OnItemClickListener;
import com.appynews.model.dto.OrigenNoticiaVO;

import java.util.List;

import material.oscar.com.materialdesign.R;


/**
 * Adapter para los orígenes de datos
 * Implementa la interfaz View.OnClickListener para detectar el evento de selección de uno
 * de los elementos del adaptador
 * Created by oscar on 11/06/16.
 */
public class FuenteDatosAdapter extends RecyclerView.Adapter<FuenteDatosAdapter.FuenteDatosHolder> implements View.OnClickListener {

    private List<OrigenNoticiaVO> items = null;
    private String origen = null;
    private ImageLoader imageLoader = null;
    private Resources resources = null;
    private View.OnClickListener listener = null;
    private OrigenRssMantenimientoActivity actividad = null;

    /**
     * Clase NoticiaViewHolder que contiene los componentes que forman
     * parte de la vista a renderizar para cada componente
     *
     */
    public static class FuenteDatosHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre = null;
        public TextView url    = null;
        public ImageView imageEditar = null;
        public ImageView imageDelete = null;


        /**
         * Constructor
         * @param v: View
         */
        public FuenteDatosHolder(View v) {
            super(v);
            nombre  = (TextView) v.findViewById(R.id.nombreOrigen);
            url     = (TextView) v.findViewById(R.id.urlOrigen);
            imageEditar = (ImageView)v.findViewById(R.id.imgEditarOrigen);
            imageDelete = (ImageView)v.findViewById(R.id.imgBorrarOrigen);
        }
    }


    /**
     * Constructor
     * @param items List<OrigenNoticiaVO>
     * @param actividad OrigenRssMantenimientoActivity
     */
    public FuenteDatosAdapter(List<OrigenNoticiaVO> items, OrigenRssMantenimientoActivity actividad) {
        this.items  = items;
        this.actividad = actividad;
    }

    /**
     * Devuelve la colección de fuentes de datos que se muestran en el adapter
     * @return List<OrigenNoticiaVO>
     */
    public List<OrigenNoticiaVO> getFuentesDatos() {
        return this.items;
    }


    /**
     * Devuelve el número de items que se muestran
     * @return int
     */
    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * Añade una origen
     * @param origen OrigenNoticiaVO
     */
    public void addItem(OrigenNoticiaVO origen) {

    }

    /**
     * Elimina un elemento del adapter de origenes de datos
     * @param pos Posición del elemento a borrar
     */
    public void removeItem(int pos) {
        getFuentesDatos().remove(pos);
    }


    /**
     * Devuelve una determinada origen/fuente de datos
     * @param pos int que indica una posición válida de la colección de fuentes
     * @return OrigenNoticiaVO
     */
    public OrigenNoticiaVO getFuenteDatos(int pos) {
        OrigenNoticiaVO n = null;
        if(pos>=0 && pos<getFuentesDatos().size()) {
            n = getFuentesDatos().get(pos);
        }
        return n;
    }

    @Override
    public FuenteDatosHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        /** Se carga el layout noticia.xml para mostrar la información de cada noticia **/
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_detalle_origen, viewGroup, false);
        v.setOnClickListener(this);
        return new FuenteDatosHolder(v);
    }

    /**
     * onBindViewHolder
     * @param viewHolder FuenteDatosHolder
     * @param i int
     */
    @Override
    public void onBindViewHolder(final FuenteDatosHolder viewHolder, int i) {
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.url.setText(items.get(i).getUrl());

        viewHolder.imageDelete.setOnClickListener(new OnItemClickListener(i,new OnItemClickCallbackBorrarFuente(getFuentesDatos(),actividad)));

    }


    /**
     * Establecer el listener de tipo OnClickListener
     * @param listener
     */
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * Cuando el usuario ejecuta el método onClick sobre la vista
     * @param view: View
     */
    public void onClick(View view) {
        if(listener != null) {
            listener.onClick(view);
        }
    }
}