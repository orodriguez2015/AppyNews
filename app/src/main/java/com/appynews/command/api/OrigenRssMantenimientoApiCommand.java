package com.appynews.command.api;

import android.support.v7.widget.RecyclerView;

import com.appynew.activities.OrigenRssMantenimientoActivity;

/**
 * Interface OrigenRssMantenimientoApiCommand
 * Created by oscar on 21/10/17.
 */
public interface OrigenRssMantenimientoApiCommand {

    /**
     * Devuelve un Activity
     * @return OrigenRssMantenimientoActivity
     */
    OrigenRssMantenimientoActivity getActivity();

    /**
     * Devuelve un RecyclerView
     * @return RecyclerView
     */
    RecyclerView getRecyclerView();

}
