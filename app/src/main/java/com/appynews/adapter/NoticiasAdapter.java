package com.appynews.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.LogCat;
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

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView descripcion;
        public TextView fechaPublicacion;
        public TextView origen;
        public NetworkImageView imagen = null;


        public NoticiaViewHolder(View v) {
            super(v);
            imagen           = (NetworkImageView) v.findViewById(R.id.imagen);
            descripcion      = (TextView) v.findViewById(R.id.descripcion);
            fechaPublicacion = (TextView) v.findViewById(R.id.fechaPublicacion);
            origen           = (TextView) v.findViewById(R.id.origenRss);
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


    @Override
    public int getItemCount() {
        return items.size();
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
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());

        if(items.get(i).getUrlThumbnail()!=null && !"".equals(items.get(i).getUrlThumbnail())) {

            LogCat.debug("NoticiasAdapter <> obtenerImagen de la url: " + items.get(i).getUrlThumbnail());


            viewHolder.imagen.setDefaultImageResId(android.R.drawable.sym_def_app_icon);
            //viewHolder.imagen.setErrorImageResId(R.drawable.ic_launcher);
            viewHolder.imagen.setAdjustViewBounds(true);
            //viewHolder.imagen.setImageResource(R.drawable.ic_launcher);


            if(items.get(i).getUrlThumbnail()!=null && !"".equals(items.get(i).getUrlThumbnail())){

                LogCat.debug("============> Estableciendo imagen " + items.get(i).getUrlThumbnail());
                viewHolder.imagen.setImageUrl(items.get(i).getUrlThumbnail(), this.imageLoader);

                //BitmapDrawable bd = (BitmapDrawable)viewHolder.imagen.getDrawable();

                //RoundedBitmapDrawableFactory.create(this.resources,null);


            }else{
                LogCat.debug("============> No hay imagen ");
                viewHolder.imagen.setDefaultImageResId(android.R.drawable.sym_def_app_icon);
            }
        }

        viewHolder.descripcion.setText(items.get(i).getTitulo());
        viewHolder.fechaPublicacion.setText(String.valueOf(items.get(i).getFechaPublicacion()));
        viewHolder.origen.setText(origen);
    }


    /**
     * Establecer el listener de tipo OnClickListener
     * @param listener
     */
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * Cuando el usuario seleccione Invoca al método onClick del
     * @param view
     */
    public void onClick(View view) {

        if(listener != null) {
            listener.onClick(view);
        }

    }
}