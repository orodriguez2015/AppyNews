package com.appynews.utils;

import android.content.res.Resources;
import android.util.JsonReader;

import com.appynews.model.dto.Noticia;
import com.appynews.model.dto.OrigenNoticiaVO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import material.oscar.com.materialdesign.R;


/**
 * Clase FileOperations con operaciones sobre archivos
 * @author oscar
 */
public class FileOperations {

    /**
     * Lee el texto de un InputStream correspondiente a un fichero, y lo devuelve
     * como un String
     * @param is: InputStream
     * @return String con el texto o null sino se ha podido recuperar
     */
    public static String readTextFromInputStream(InputStream is){
        String salida = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = is.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            }catch(Exception ex){}
        }

        salida = outputStream.toString();
        return salida;
    }


    /**
     * Método que lee el html del recurso cuyo identificador se pasa por parámetro, y devuelve
     * un String con el html, y dentro del mismo el título y descripción de la noticia.
     *
     * De este modo, el html puede ser incrustrado en un WebView
     * @param resourceId: Id del recurso Android definido en la clase R
     * @param noticia: Objeto de la clase Noticia de la que se extrae la información para
     *               mostrar en el documento html
     * @param resources: Resources
     * @return String con el contenido en html
     */
    public static String readHtmlFromResource(int resourceId, Noticia noticia, Resources resources){

        String salida = null;

        // Se obtiene el InputStream correspondiente al archivo html en el que se
        // mostrarán los datos de la noticia
        InputStream is = resources.openRawResource(resourceId);

        // Se lee el contenido del archivo html y se almacena en un String
        salida = FileOperations.readTextFromInputStream(is);

        if(salida!=null){

            // Se sustituye en el HTML el título y la descripción
            if(noticia.getTitulo()!=null) {
                salida = salida.replaceAll("_TITULO_", noticia.getTitulo());
            }

            if(noticia.getDescripcionCompleta()!=null && !"".equals(noticia.getDescripcionCompleta()))
                salida = salida.replaceAll("_DESCRIPCION_", noticia.getDescripcionCompleta());
            else
                salida = salida.replaceAll("_DESCRIPCION_", noticia.getDescripcion());

            if(noticia.getAutor()!=null)
                salida = salida.replaceAll("_AUTOR_",noticia.getAutor());
            else
                salida = salida.replaceAll("_AUTOR_","");

            if(noticia.getFechaPublicacion()!=null)
                salida = salida.replaceAll("_FECHA_",noticia.getFechaPublicacion());
            else
                salida = salida.replaceAll("_FECHA_","");

            salida = salida.replaceAll("_URL_",noticia.getUrl());

            salida = salida.replaceAll("_TEXTOENLACE_",resources.getString(R.string.enlace_ir_noticia));
        }

        return salida;

    }




    /**
     * Lee el archivo de configuracion.xml que contiene los orígenes de datos RSS
     * @param resources: Objeto de la clase Resources necesario para leer el archivo de configuración con los orígenes
     *                 de datos rss
     * @return HashMap<Integer,OrigenNoticiaVO>
     */
    public static HashMap<Integer,OrigenNoticiaVO> leerArchivoConfiguracion(Resources resources){
        ArrayList<OrigenNoticiaVO> salida = new ArrayList<OrigenNoticiaVO>();
        HashMap<Integer,OrigenNoticiaVO> mapa = new HashMap<Integer,OrigenNoticiaVO>();

        try{
            InputStream fis = resources.openRawResource(R.raw.configuracion);
            LogCat.debug("FileOperations.leerArchivoConfiguracion ============>");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document dom = builder.parse(fis);
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("origen");

            for (int i=0; i<items.getLength(); i++){

                Node item = items.item(i);
                NodeList datosNoticia = item.getChildNodes();

                OrigenNoticiaVO origen = new OrigenNoticiaVO();
                for (int j=0; j<datosNoticia.getLength(); j++)
                {
                    Node dato = datosNoticia.item(j);
                    if(dato.getNodeType()==Node.ELEMENT_NODE){
                        // Se obtiene el hijo de descripcion, que es un nodo
                        Node datoContenido = dato.getFirstChild();
                        if(datoContenido!=null && datoContenido.getNodeType()==Node.TEXT_NODE){
                            String valor = datoContenido.getNodeValue();

                            if(dato.getNodeName().equals("id") && NumberOperations.isInteger(valor)) {
                                origen.setId(new Integer(valor));
                            }

                            if(dato.getNodeName().equals("descripcion")) {
                                origen.setNombre(valor);
                            }

                            if(dato.getNodeName().equals("url")) {
                                origen.setUrl(valor);
                            }
                        }
                    }
                }// for
                mapa.put(origen.getId(),origen);
            }

        }catch(Exception e){
            e.printStackTrace();
            LogCat.error("==============> Error al leer el archivo de configuración: " + e.getMessage());
        }

        LogCat.debug("FileOperations.leerArchivoConfiguracion< ============");
        return mapa;
    }


    /**
    public static void escribirXML( FileOutputStream fout,ArrayList<Origen> origenes) {

        XmlSerializer serializer = Xml.newSerializer();
        try {

            serializer.setOutput(fout, "UTF-8");
            serializer.startDocument(null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "origenes");
            for(int i=0;origenes!=null && i<origenes.size();i++){

                serializer.startTag(null, "origen");
                serializer.startTag(null, "descripcion");
                serializer.text(origenes.get(i).getNombre());
                serializer.endTag(null, "descripcion");

                serializer.startTag(null, "url");
                serializer.text(origenes.get(i).getUrl());
                serializer.endTag(null, "url");

                serializer.endTag(null, "origen");
            }

            serializer.endTag(null, "origenes");

            serializer.endDocument();
            serializer.flush();
            fout.close();

        } catch (Exception e) {
            AndroidLog.error("Error: " + e.getMessage());
        }
    }
     **/

}

