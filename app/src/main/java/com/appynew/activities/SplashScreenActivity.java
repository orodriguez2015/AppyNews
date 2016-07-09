package com.appynew.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import material.oscar.com.materialdesign.R;

/**
 * Clase SplashScreenActivity que renderiza la Splash Screen de la aplicación
 * @author oscar
 */
public class SplashScreenActivity extends AppCompatActivity {

    /**
     * Temporizador para la pantalla de bienvenida
     */
    private static int SPLASH_TIEMPO = 1500;


    /**
     * Métod onCreate que se encarga de crear la interfaz de usuario
     * @param savedInstanceState: Bundlle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {

			/*
			* Mostramos la pantalla de bienvenida con un temporizador.
			* De esta forma se puede mostrar el logo de la app o
			* compañia durante unos segundos.
			*/

            @Override
            public void run() {
                // Este método se ejecuta cuando se consume el tiempo del temporizador.
                // Se pasa a la activity principal
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);

                // Cerramos esta activity
                finish();
            }
        }, SPLASH_TIEMPO);


    }

}
