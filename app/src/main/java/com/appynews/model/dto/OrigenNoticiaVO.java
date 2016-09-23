package com.appynews.model.dto;

import java.io.Serializable;

/**
 * Clase OrigenNoticiaVO que representa un orígen de datos RSS
 *
 * Created by oscar on 03/07/16.
 */
public class OrigenNoticiaVO implements Comparable<OrigenNoticiaVO>,Serializable {

    private Integer id = null;
    /**
     * Nombre del sitio
     */
    private String nombre = null;
    /**
     * Url de los recursos RSS
     */
    private String url = null;


    /**
     * Constructor
     */
    public OrigenNoticiaVO() {

    }


    /**
     * Constructor
     * @param nombre: Nombre del sitio
     * @param url: Url de acceso al rss del sitio
     */
    public OrigenNoticiaVO(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }


    /**
     * Constructor
     * @param id: Id
     * @param nombre: Nombre del sitio
     * @param url: Url de acceso al rss del sitio
     */
    public OrigenNoticiaVO(Integer id, String nombre, String url) {
        this.id = id;
        this.nombre = nombre;
        this.url = url;
    }

    /**
     * Id
     */ /**
     * Devuelve el id
     * @return Integer
     */
    public Integer getId() {
        return id;
    }


    /**
     * Establece el id
     * @param id: Integer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del sitio que contiene la información rss
     * @return String
     */
    public String getNombre() {
        return nombre;
    }


    /**
     * Permite establecer el nombre del sitio que contiene la información rss
     * @param nombre: String
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    /**
     * Devuelve la url del orígen rss
     * @return: String
     */
    public String getUrl() {
        return url;
    }


    /**
     * Establece la url del orígen rss
     * @param url: String
     */
    public void setUrl(String url) {
        this.url = url;
    }


    /**
     * hashCode
     * @return: int
     */
    public int hashCode() {
        return this.getId().hashCode();
    }

    /**
     * equals
     * @param objeto: Object
     * @return: boolean
     */
    public boolean equals( Object objeto ) {
        if (objeto == null) return false;
        OrigenNoticiaVO origen = (OrigenNoticiaVO)objeto;
        if (this.getId() == origen.getId()) {
            return true;
        }

        return false;
    }


    /**
     * Método  compareTo
     // Devuelve +1 si this es > que objeto
     // Devuelve -1 si this es < que objeto
     // Devuelve 0 si son iguales
     * @param objeto: Object
     * @return int
     */
    public int compareTo(OrigenNoticiaVO objeto) {

        int salida = -1; // Valor de salida por defecto

        if (this.equals(objeto)) {
            salida = 0;
        } else if (this.getId() > objeto.getId()) {
            salida = 1;
        } else if (this.getId() < objeto.getId()) {
            salida = -1;
        }

        return salida;
    }




}
