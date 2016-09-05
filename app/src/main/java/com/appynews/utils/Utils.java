package com.appynews.utils;

import com.appynews.model.dto.OrigenNoticiaVO;

import java.util.Collections;
import java.util.List;

/**
 * Clase con operaciones que tienen diferentes utilidades
 * Created by oscar on 05/09/16.
 */
public class Utils {

    /**
     * Recupera una determinada fuente de datos que tenga un determinado id
     * @param fuentes: List<OrigenNoticiaVO>
     * @param id: Integer
     * @return OrigenNoticiaVO
     */
    public static OrigenNoticiaVO getFuenteDatos(List<OrigenNoticiaVO> fuentes,Integer id) {
        OrigenNoticiaVO salida = null;

        // Se ordena la colección que implementa la interfaz Comparable
        Collections.sort(fuentes);

        // Se busca en la colección de fuentes
        int posicion = Collections.binarySearch(fuentes,new OrigenNoticiaVO(id,null,null));

        LogCat.debug("Se ha encontrado la fuente en la posición: " + posicion);
        if(posicion>=0) {
            salida = fuentes.get(posicion);
        }

        return salida;
    }
}
