package com.appynew.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.appynews.model.dto.Noticia;

import material.oscar.com.materialdesign.R;

public class DetalleNoticiaActivity extends AppCompatActivity {

    private TextView descripcionNoticia = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_noticia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        descripcionNoticia = (TextView) findViewById(R.id.descripcionNoticia);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // Se recupera la noticia que se ha pasado al activity como parámetro
        Bundle parametros = getIntent().getExtras();
        Noticia noticia = (Noticia)parametros.get("noticia");
        descripcionNoticia.setText(noticia.getDescripcion());

    }
}
