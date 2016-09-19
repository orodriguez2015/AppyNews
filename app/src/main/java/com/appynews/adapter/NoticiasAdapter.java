package com.appynews.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.LogCat;
import com.appynews.utils.StringUtil;

import java.util.List;

import material.oscar.com.materialdesign.R;

/**
 * Adapter para las noticias.
 * Implementa la interfaz View.OnClickListener para detectar el evento de selección de uno
 * de los elementos del adaptador
 * Created by oscar on 11/06/16.
 */
public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.NoticiaViewHolder> implements View.OnClickListener {

    private List<Noticia> items = null;
    private String origen = null;
    private ImageLoader imageLoader = null;
    private Resources resources = null;
    private View.OnClickListener listener = null;


    /**
     * Clase NoticiaViewHolder que contiene los componentes que forman
     * parte de la vista a renderizar para cada componente
     *
     */
    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView descripcion;
        public TextView fechaPublicacion;
        public TextView origen;
        public NetworkImageView imagen = null;


        /**
         * Constructor
         * @param v: View
         */
        public NoticiaViewHolder(View v) {
            super(v);
            imagen           = (NetworkImageView) v.findViewById(R.id.imagen);
            descripcion      = (TextView) v.findViewById(R.id.descripcion);
            fechaPublicacion = (TextView) v.findViewById(R.id.fechaPublicacion);
            origen           = (TextView) v.findViewById(R.id.origen);
        }
    }


    /**
     * Constructor
     * @param items: List<Noticia>
     */
    public NoticiasAdapter(List<Noticia> items, String origen,ImageLoader imageLoader, Resources resources) {
        this.items  = items;
        this.origen = origen;
        this.imageLoader = imageLoader;
        this.resources   = resources;
    }

    /**
     * Devuelve la colección de noticias que se muestran en el adapter
     * @return List<Noticia>
     */
    public List<Noticia> getNoticias() {
        return this.items;
    }


    /**
     * Devuelve el número de noticias que se muestran
     * @return int
     */
    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * Añade una noticia
     * @param noticia Noticia
     */
    public void addItem(Noticia noticia) {

    }

    /**
     * Elimina un elemento del adapter de noticia
     * @param pos Posición del elemento a borrar
     */
    public void removeItem(int pos) {
        getNoticias().remove(pos);
    }


    /**
     * Devuelve una determinada noticia
     * @param pos int que indica una posición válida de la colección de noticias
     * @return Noticia
     */
    public Noticia getNoticia(int pos) {
        Noticia noticia = null;
        if(pos>=0 && pos<getNoticias().size()) {
            noticia = getNoticias().get(pos);
        }
        return noticia;
    }

    @Override
    public NoticiaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        /** Se carga el layout noticia.xml para mostrar la información de cada noticia **/
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.noticia, viewGroup, false);

        v.setOnClickListener(this);
        return new NoticiaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoticiaViewHolder viewHolder, int i) {

        if(items.get(i).getUrlThumbnail()!=null && !"".equals(items.get(i).getUrlThumbnail())) {

            // Se indica al NetWorkImage la url de la imagen a mostrar, en caso de que no haya imagen.
            // Se muestra el logo de la aplicación como background del NetworkImageView
            viewHolder.imagen.setAdjustViewBounds(true);
            viewHolder.imagen.setImageUrl(items.get(i).getUrlThumbnail(), this.imageLoader);
            //BitmapDrawable bd = (BitmapDrawable)viewHolder.imagen.getDrawable();
            //RoundedBitmapDrawableFactory.create(this.resources,null);
        }

        viewHolder.descripcion.setText(items.get(i).getTitulo());
        viewHolder.fechaPublicacion.setText("");
        if(!TextUtils.isEmpty(items.get(i).getFechaPublicacion())) {
            viewHolder.fechaPublicacion.setText(String.valueOf(items.get(i).getFechaPublicacion()));
        }

        String origenFinal = this.origen;
        if(StringUtil.isNotEmpty(items.get(i).getOrigen())) {
            origenFinal = items.get(i).getOrigen();
        }
        viewHolder.origen.setText(origenFinal);
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
        LogCat.debug("NoticiasAdapter.onClick ====>");
        if(listener != null) {
            listener.onClick(view);
        }

    }
}