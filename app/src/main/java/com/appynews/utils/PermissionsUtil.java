package com.appynews.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Clase PermissionsUtil
 * Created by oscar on 18/06/16.
 */
public class PermissionsUtil {


    /**
     * Operación que comprueba si una actividad tiene acceso a un determinado permiso.
     * Los permisos se encuentra definidos en Manifest.permission.*
     * @param activity: Activity desde la que se hace la comprobación del permiso
     * @param permiso: Permiso a comprobar
     */
    public static boolean appTienePermiso(Activity activity,String permiso) {
        boolean exito = false;

        try {
            int permissionCheck = ContextCompat.checkSelfPermission(activity,permiso);
            if(permissionCheck== PackageManager.PERMISSION_GRANTED) {
                exito = true;
                LogCat.info("Hay permiso para leer el estado del teléfono");
            } else {
                LogCat.info("No Hay permiso para leer el estado del teléfono");
            }
        }catch(Exception e) {
            e.printStackTrace();
            exito = false;
        }

        return exito;
    }

}
