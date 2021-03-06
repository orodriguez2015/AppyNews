package com.appynews.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Clase DateOperationsUtils con operaciones de manejo de fechas
 * Created by oscar on 18/06/16.
 */
public class DateOperationsUtils {

    public static final String FORMATO_FECHA_HORA = "dd/MM/yyyy HH:mm:ss";

    /**
     * Devuelve la fecha y hora actual en el formato que se indique por parámetro
     * @param formato: String con el formato de la hora
     * @return String
     */
    public static String getFechaHoraActual(String formato) {
        String salida = null;

        try {
            SimpleDateFormat sf = new SimpleDateFormat(formato);
            salida = sf.format(Calendar.getInstance().getTime());

        } catch(Exception e) {
            e.printStackTrace();
        }
        return salida;
    }


    /**
     * Convierte una fecha en formato String en un objeto Calendar
     * @param fecha: Fecha
     * @return Calendar
     */
    public static Calendar getCalendar(String fecha){
        Calendar c = Calendar.getInstance();
        c.clear();

        try{
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d = sf.parse(fecha);
            c.setTime(d);

        }catch(Exception e){
            e.printStackTrace();
        }

        return c;
    }


    /**
     * Convierte la fecha de una noticia RSS, que está en el formato EEE, dd MMM yyyy HH:mm:ss Z, en un
     * String con la fecha en formato dd/MM/yyyy HH:mm:ss
     * @param fechaRSS: Fecha
     * @return null sino se ha podido convertir la fecha, o la fecha en el formato dd/MM/yyyy HH:mm:ss
     */
    public static String convertirFechaRss(String fechaRSS){

        String salida = null;
        SimpleDateFormat formatterRss = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {

            Date date = formatterRss.parse(fechaRSS);
            salida = sf.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return salida;
    }

}