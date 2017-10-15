package com.appynews.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
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


    /**
     * Solicita permiso de acceso al teléfono para poder ejecutar la aplicación
     * Válido de Android 6.0 en adelante
     * @param activity Activity para el que se solicitan los permisos
     * @return True si tiene permiso y false en caso contrario. Al devolver false el dispositivo queda a la
     * espera de que el usuario conceda permiso
     */
    public static boolean solicitarPermisosAccesoEstadoTelefono(Activity activity) {
        boolean permissions  =true;

        LogCat.info("Device: " + Build.DEVICE);
        LogCat.info("Board: " + Build.BOARD);
        LogCat.info("BOOTLOADER: " + Build.BOOTLOADER);
        LogCat.info("Brand: " + Build.BRAND);
        LogCat.info("display: " + Build.DISPLAY);
        LogCat.info("fingerprint: " + Build.FINGERPRINT);
        LogCat.info("MANUFACTURER: " + Build.MANUFACTURER);
        LogCat.info("MODEL: " + Build.MODEL);
        LogCat.info("PRODUCT: " + Build.PRODUCT);
        LogCat.info("SERIAL: " + Build.SERIAL);

        LogCat.info("TYPE: " + Build.TYPE);
        LogCat.info("TAGS: " + Build.TAGS);
        LogCat.info("SHARDWARE: " + Build.HARDWARE);

        /*
        LogCat.info("SERIAL: " + Build.SERIAL);
        LogCat.info("SERIAL: " + Build.SERIAL);
        LogCat.info("SERIAL: " + Build.SERIAL);
        LogCat.info("SERIAL: " + Build.SERIAL);
        LogCat.info("SERIAL: " + Build.SERIAL);
        LogCat.info("SERIAL: " + Build.SERIAL);
        */


        /**
         * Si la versión de api de Android es superior a la 23 (Android 6), hay que solicitar permisos al usuario, puesto que no se conceden
         * al instalar la aplicación por primera vez, sino en la primera ejecución
         */
        if (Build.VERSION.SDK_INT >= ConstantesDatos.API_VERSION_ANDROID_REQUEST_PERMISSIONS) {

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_PHONE_STATE}, ConstantesPermisos.PERMISSIONS_READ_PHONE_STATE);
                permissions = false;
            }

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, ConstantesPermisos.PERMISSIONS_ACCESS_NETWORK_STATE);
                permissions = false;
            }
        }

        return permissions;
    }



}
