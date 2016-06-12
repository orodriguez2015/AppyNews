package com.appynews.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appynews.model.dto.Noticia;

import java.util.List;

import material.oscar.com.materialdesign.R;

/**
 * Adapter para las noticias
 * Created by oscar on 11/06/16.
 */
public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.NoticiaViewHolder> {
    private List<Noticia> items;

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView descripcion;
        public TextView fechaPublicacion;

        public NoticiaViewHolder(View v) {
            super(v);
            //imagen = (ImageView) v.findViewById(R.id.imagen);
            descripcion = (TextView) v.findViewById(R.id.descripcion);
            fechaPublicacion = (TextView) v.findViewById(R.id.fechaPublicacion);
        }
    }


    /**
     * Constructor
     * @param items: List<Noticia>
     */
    public NoticiasAdapter(List<Noticia> items) {
        this.items = items;
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



        return new NoticiaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NoticiaViewHolder viewHolder, int i) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());

        viewHolder.descripcion.setText(items.get(i).getDescripcion());
        viewHolder.fechaPublicacion.setText("Visitas:"+String.valueOf(items.get(i).getFechaPublicacion()));
    }
}