package com.appynews.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appynews.model.dto.Noticia;
import com.appynews.utils.ImageUtils;

import org.w3c.dom.Text;

import java.util.List;

import material.oscar.com.materialdesign.R;

/**
 * Adapter para las noticias
 * Created by oscar on 11/06/16.
 */
public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.NoticiaViewHolder> {
    private List<Noticia> items = null;
    private String origen = null;

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView descripcion;
        public TextView fechaPublicacion;
        public TextView origen;

        public NoticiaViewHolder(View v) {
            super(v);
            imagen           = (ImageView) v.findViewById(R.id.imagen);
            descripcion      = (TextView) v.findViewById(R.id.descripcion);
            fechaPublicacion = (TextView) v.findViewById(R.id.fechaPublicacion);
            origen           = (TextView) v.findViewById(R.id.origenRss);
        }
    }


    /**
     * Constructor
     * @param items: List<Noticia>
     */
    public NoticiasAdapter(List<Noticia> items, String origen) {
        this.items  = items;
        this.origen = origen;
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

        if(items.get(i).getUrlThumbnail()!=null && !"".equals(items.get(i).getUrlThumbnail())) {
            //Bitmap bitmap = BitmapFactory.decodeFile(items.get(i).getUrlThumbnail());

            //viewHolder.imagen.setImageBitmap(ImageUtils.getImageBitmap(items.get(i).getUrlThumbnail()));
            //viewHolder.imagen.setImageURI(new Uri());
            //viewHolder.imagen.setImageResource(R.drawable.ic_menu_gallery);
        }

        viewHolder.descripcion.setText(items.get(i).getTitulo());
        viewHolder.fechaPublicacion.setText(String.valueOf(items.get(i).getFechaPublicacion()));
        viewHolder.origen.setText(origen);
    }
}