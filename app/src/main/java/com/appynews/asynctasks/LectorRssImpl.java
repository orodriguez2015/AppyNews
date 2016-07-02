package com.appynews.asynctasks;

/**
 * Created by oscar on 18/06/16.
 */
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.appynews.exceptions.RssReadException;
import com.appynews.model.dto.Noticia;
import com.appynews.utils.DateOperationsUtils;
import com.appynews.utils.LogCat;


public class LectorRssImpl{


    public ArrayList<Noticia> getNoticias(InputStream is) throws RssReadException {
        ArrayList<Noticia> salida = new ArrayList<Noticia>();

        try{

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document dom = builder.parse(is);
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("item");

            for (int i=0; i<items.getLength(); i++){

                Node item = items.item(i);
                NodeList datosNoticia = item.getChildNodes();

                Noticia noticia = new Noticia();
                for (int j=0; j<datosNoticia.getLength(); j++)
                {

                    Node dato = datosNoticia.item(j);

                    if(dato.getNodeType()==Node.ELEMENT_NODE){
                        // Se obtiene el hijo de descripcion, que es un nodo
                        Node datoContenido = dato.getFirstChild();

                        if(datoContenido!=null && datoContenido.getNodeType()==Node.CDATA_SECTION_NODE) {
                            // La descripción por ejemplo en menéame se agrupa en un node de tipo CDATA_SECTION_NODE
                            if(dato.getNodeName().equals("description")) {
                                noticia.setDescripcion(dato.getTextContent());
                            }

                        } else
                        if(datoContenido!=null && datoContenido.getNodeType()==Node.TEXT_NODE){
                            String valor = datoContenido.getNodeValue();


                            //LogCat.debug(" ****** NOMBRE NODO: " + dato.getNodeName());
                            //LogCat.debug(" ****** VALOR NODO: " + valor);

                            if(dato.getNodeName().equals("title"))
                                noticia.setTitulo(valor);
                            else
                            if(dato.getNodeName().trim().equals("description")) {
                                LogCat.debug(" ****** ESTABLECIENDO DESCRIPCION ");

                                LogCat.debug(" ****** getTextContent: " + dato.getTextContent());
                                noticia.setDescripcion(dato.getTextContent());
                            }
                            else
                            if(dato.getNodeName().equals("content:encoded"))
                                noticia.setDescripcionCompleta(valor);
                            else
                            if(dato.getNodeName().equals("dc:creator"))
                                noticia.setAutor(valor);
                            else
                            if(dato.getNodeName().equals("link"))
                                noticia.setUrl(valor);
                            else
                            if(dato.getNodeName().equals("pubDate") && valor!=null && valor.length()>0)
                                noticia.setFechaPublicacion(DateOperationsUtils.convertirFechaRss(valor));


                        }else{

                            if(dato.getNodeName().equals("media:thumbnail") ){
                                noticia.setUrlThumbnail(dato.getAttributes().getNamedItem("url").getTextContent());
                            }

                        }

                    }


                }
                salida.add(noticia);
            }

        }catch(Exception e){
            LogCat.error("========> Error al leer el archivo de configuración: " + e.getMessage());
            e.printStackTrace();
        }

        return salida;
    }




}

