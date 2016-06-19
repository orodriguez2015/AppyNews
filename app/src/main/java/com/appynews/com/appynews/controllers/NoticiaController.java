package com.appynews.com.appynews.controllers;

import android.app.Activity;

import com.appynews.asynctasks.GetInputStreamNewsConnectionTask;
import com.appynews.asynctasks.GetNewsRssSourceTask;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.MessageUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Clase NoticiaController
 * Created by oscar on 19/06/16.
 */
public class NoticiaController {

    private Activity actividad = null;

    /**
     * Constructor
     * @param actividad: Actividad desde la que se invoca al controller
     */
    public NoticiaController(Activity actividad) {
        this.actividad = actividad;
    }

    /**
     * Recupera las noticias de una determinada url
     * @param url: String
     * @return ArrayList<Noticia>
     */
    public ArrayList<Noticia> getNoticias(String url) {
        ArrayList<Noticia> noticias = new ArrayList<Noticia>();
        GetInputStreamNewsConnectionTask conIs = new GetInputStreamNewsConnectionTask();

        conIs.execute(url);
        InputStream is = null;

        try {
            is = conIs.get();

            if(is!=null) {
                GetNewsRssSourceTask getNewsTask = new GetNewsRssSourceTask();
                getNewsTask.execute(is);

                noticias = getNewsTask.get();

            } else {
                MessageUtils.showToastDuracionLarga(this.actividad.getApplicationContext(),"No se ha podido establecer conexión con ".concat(url));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return noticias;
    }

}
