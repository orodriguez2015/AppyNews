package com.appynews.model.dto;

import java.io.Serializable;

/**
 * Entidad Noticia cuya información será la que se muestra en la actividad principal
 * de la aplicación
 * @author oscar
 *
 */
public class Noticia implements Serializable{

    private int id;
    private String titulo;
    private String descripcion;
    private String descripcionCompleta;
    private String autor;
    private String fechaPublicacion;
    private String origen;
    private String url;

    /**
     * Constructor
     */
    public Noticia() {
    }


    /**
     * Constructor
     * @param titulo: Título de la noticia
     * @param descripcion: Descripción corta
     * @param descripcionCompleta: Descripción completa
     * @param autor: Autor
     * @param fechaPublicacion: Fecha de la publicación
     * @param origen: Origen
     * @param url: Url de la noticia en el servidor del que se ha extraído
     * @param urlThumbnail: url de la imagen/thumbnail asociada a la noticia
     */
    public Noticia(String titulo, String descripcion, String descripcionCompleta, String autor, String fechaPublicacion, String origen, String url, String urlThumbnail) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.descripcionCompleta = descripcionCompleta;
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
        this.origen = origen;
        this.url = url;
        this.urlThumbnail = urlThumbnail;
    }

    private String urlThumbnail;


    /**
     * Devuelve la descripción completa de la noticia
     * @return String
     */
    public String getDescripcionCompleta() {
        return descripcionCompleta;
    }

    /**
     * Establece la descripción completa de la noticia
     * @param descripcionCompleta: String
     */
    public void setDescripcionCompleta(String descripcionCompleta) {
        this.descripcionCompleta = descripcionCompleta;
    }


    /**
     * Devuelve la descripción completa de la noticia
     * @return String
     */
    public String getUrlThumbnail() {
        return urlThumbnail;
    }
    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }


    /**
     * Devuelve la descripción corta de la noticia
     * @return String
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción corta de la noticia
     * @param descripcion: String
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve el título de la noticia
     * @return String
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Permite establecer el título de la noticia
     * @param titulo: String
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Devuelve la url de la noticia en el medio del que
     * se ha extraído
     * @return String
     */
    public String getUrl() {
        return url;
    }


    /**
     * Devuelve la url de la noticia en el medio del que
     * se ha extraído
     * @return String
     */
    public void setUrl(String url) {
        this.url = url;
    }


    /**
     * Devuelve la fecha de publicación de la noticia
     * @return String
     */
    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Permite establecer la fecha de publicación de la noticia
     * @param fechaPublicacion: String
     */
    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Devuelve el autor de la noticia
     * @return String
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Permite establecer el autor de la noticia
     * @param autor: String
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }


    /**
     * Devuelve el origen de la noticia
     * @return String
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Establece el origen de la noticia
     * @param origen: String
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }


    /**
     * Método toString
     * @return String
     */
    public String toString() {
        String salida = "Autor: ".concat(getAutor()).concat(" <> descripcion: ").concat(getDescripcion()).concat(" <> descripcionCompleta: ").concat(getDescripcionCompleta())
                .concat(" <> titulo: ").concat(getTitulo());
        return salida;
    }

    /**
     * Devuelve el id
     * @return int
     */
    public int getId() {
        return id;
    }


    /**
     * Establece el id de la noticia
     * @param id int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve true si la noticia tiene asociado un id y false en
     * caso contrario. Si tiene id, es que la noticia se ha recuperado de la BBDD, y por tanto,
     * es una noticia favorita
     * @return boolean
     */
    public boolean isNoticiaFavorita() {
        boolean exito = false;
        
        if(this.id>=1) {
            exito = true;
        }
        return exito;
    }
}