/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.afcepf.atod.wine.data.parser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author ronan
 */
public class WineParser {

    private static Logger log = Logger.getLogger(WineParser.class);

    public static void main(String[] args) {
        log.info("\t #### Debut du parsing avec JAXP #### ");

        String nomTable = JOptionPane.showInputDialog("Nom de la table @ remplir: ");
        int nbAttributs = Integer.parseInt(
                JOptionPane.showInputDialog("Nombre attributs: "));

        List<String> elementsToParse = new ArrayList<>();
        for (int i = 0; i < nbAttributs; i++) {
            String attribut = JOptionPane.showInputDialog("Attribut" + i + ": ");
            elementsToParse.add(attribut);
        }

        List<ArrayList<String>> resultsParsing = parsing(elementsToParse, "Regions.xml");

        log.info("\t" + "#### ecriture file ####");
        writeFile(nomTable, elementsToParse, resultsParsing);
        // check the values
        visualizeElements(resultsParsing);

        log.info("\t #### Fin du parsing avec JAXP #### ");
    }

    /**
     *
     * @param attributsParses
     * @param nomFile
     * @return
     */
    private static List<ArrayList<String>> parsing(List<String> attributsParses, String nomFile) {
        List<ArrayList<String>> resultsParsing = new ArrayList<ArrayList<String>>();

        try {
            InputStream in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("FilesXML/" + nomFile);

            DocumentBuilderFactory docBuilderFactory
                    = DocumentBuilderFactory.newInstance();

            DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
            Document doc = parser.parse(in);

            Element rootElement = doc.getDocumentElement();
            NodeList nodes = rootElement.getChildNodes();

            // ou acces directe via getElementsByTagName();
            if (!attributsParses.isEmpty()
                    && !attributsParses.get(0).equalsIgnoreCase("")) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    ArrayList<String> listAttributs = new ArrayList<>();
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        for (String str : attributsParses) {
                            listAttributs.add(element
                                    .getElementsByTagName(str)
                                    .item(0)
                                    .getTextContent());
                        }
                        resultsParsing.add(listAttributs);
                    }
                }
            }
        } catch (SAXException ex) {
            java.util.logging.Logger
                    .getLogger(WineParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger
                    .getLogger(WineParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            java.util.logging.Logger
                    .getLogger(WineParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return resultsParsing;
        }
    }

    private static void visualizeElements(List<ArrayList<String>> resultsParsing) {
        for (ArrayList<String> listStr : resultsParsing) {
            log.info("#" + listStr.toString() + "\n");
            for (String str : listStr) {
                log.info("#" + str + "\n");
            }
        }
    }

    private static void writeFile(String nomTable, List<String> elementsToParse,
            List<ArrayList<String>> resultsParsing) {
        FileOutputStream out = null;
        PrintStream ps = null;
        try {
            out = new FileOutputStream(nomTable + ".sql");
            ps = new PrintStream(out);
            
            for (ArrayList<String> listAttributs : resultsParsing) {                
                ps.print("INSERT INTO `" + nomTable + "` (`id`,`");                
                for (String str : elementsToParse) {
                    ps.print(str + "`");
                }
                ps.print(") VALUES (" + resultsParsing.indexOf(listAttributs));
                for(String attribut : listAttributs) {
                    ps.print("," + attribut);
                }
                ps.print(");");
                ps.println();
            }

        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(WineParser.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            ps.close();
            try {
                out.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(WineParser.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
}
