package com.appynews.model.dto;

import java.io.Serializable;

/**
 * Clase para alojar datos del dispositivo del usuario
 * Created by oscar on 22/08/16.
 */
public class DatosUsuarioVO implements Serializable {

    private Integer id            = null;
    private String nombre         = null;
    private String apellido1      = null;
    private String apellido2      = null;
    private String numeroTelefono = null;
    private String imei           = null;
    private String regionIso      = null;
    private String email          = null;


    /**
     * Devuelve el imei del teléfono
     * @return String
     */
    public String getImei() {
        return imei;
    }

    /**
     * Establece el imei del teléfono
     * @param imei: String
     */
    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * Devuelve el código de la región del operador
     * @return String
     */
    public String getRegionIso() {
        return regionIso;
    }

    /**
     * Establece el código de la región del operador
     * @param regionIso: String
     */
    public void setRegionIso(String regionIso) {
        this.regionIso = regionIso;
    }

    /**
     * Devuelve el número de teléfono
     * @return String
     */
    public String getNumeroTelefono() {
        return numeroTelefono;
    }


    /**
     * Permite establecer el número de teléfono
     * @param numeroTelefono: String
     */
    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }


    /**
     * Devuelve el email
     * @return: String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Permite establecer el email de la cuenta de google
     * @param email: String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * toString
     * @return String
     */
    public String toString() {
        return "numeroTelefono: ".concat(numeroTelefono).concat(",imei: ").concat(imei).concat(",regionIso: ").concat(regionIso).concat(",email: ").concat(email);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
