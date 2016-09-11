package fr.afcepf.atod.wine.data.parser;

import fr.afcepf.atod.vin.data.exception.WineException;
import fr.afcepf.atod.wine.data.product.api.IDaoProduct;
import fr.afcepf.atod.wine.entity.Product;
import fr.afcepf.atod.wine.entity.ProductAccessories;
import fr.afcepf.atod.wine.entity.ProductType;
import fr.afcepf.atod.wine.entity.ProductVarietal;
import fr.afcepf.atod.wine.entity.ProductVintage;
import fr.afcepf.atod.wine.entity.ProductWine;
import fr.afcepf.atod.wine.entity.Supplier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ronan
 */
public class XmlParser {

    private static Logger log = Logger.getLogger(XmlParser.class);
    private static Map<String,ProductVarietal> varietals = new HashMap<String,ProductVarietal>();
    private static Map<String,ProductType> types = new HashMap<String,ProductType>();
    private static Map<String,ProductVintage> vintages = new HashMap<String,ProductVintage>();
    private static java.util.List<ProductWine> list = new ArrayList<ProductWine>();
    private static String apiBaseUrl = "http://services.wine.com/api/beta2/service.svc/xml/";
    private static String apikey = "37662dd9dbf72936b590e8bdec649a30";

    public static void main(String[] args) {
        log.info("\t ### debut du test ###");
        URL url;
		try {
			url = new URL(apiBaseUrl+"/categorymap?filter=categories(490)&apikey="+apikey); 
        	File file = new File("/FilesXML/Wines/categoryMap.xml");
			FileUtils.copyURLToFile(url, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        BeanFactory bf = new ClassPathXmlApplicationContext("classpath:springData.xml");
        IDaoProduct daoVin = (IDaoProduct) bf.getBean(IDaoProduct.class);

        Product productRand = new Product(null, "pre", 500.0, "un produit");

        Product productAccessorie = new ProductAccessories(null, "un mug",
                25.0, "un beau mug", new Date());

        Supplier supplier1 = new Supplier(null, "Aux bon vins de Bourgogne",
                "05 85 74 85 69",
                "vinsbourgogne@gmail.com", new Date());
        Supplier supplier2 = new Supplier(null, "Aux bon vins de Bordeaux",
                "04 85 74 85 69",
                "vinsbordeaux@gmail.com", new Date());
        Supplier supplier3 = new Supplier(null, "Aux bon vins de l'Aude",
                "07 85 74 85 69",
                "vinsaude@gmail.com", new Date());
        try {
	        //Les Set sont particulièrement adaptés pour manipuler une grande
	        //quantité de données. Cependant, les performances de ceux-ci peuvent
	        //être amoindries en insertion. Généralement, on opte pour un HashSet,
	        //car il est plus performant en temps d'accès 
	        Set<Supplier> suppliersRand = new HashSet<Supplier>();
	        suppliersRand.add(supplier1);
	        suppliersRand.add(supplier2);
	        productRand.setStockSuppliers(suppliersRand);
	        daoVin.insertObj(productRand);
	        Set<Supplier> suppliersAccessorie= new HashSet<Supplier>();
	        suppliersAccessorie.add(supplier1);
	        productAccessorie.setStockSuppliers(suppliersAccessorie);
	        daoVin.insertObj(productAccessorie);
	        
	        for(int i=1;i<7;i++){
	        	list = parseSampleXml("FilesXML/wines"+i+".xml");
		        Integer cpt = 0;
		        for (ProductWine productWine: list) {
		        	Set<Supplier> supplierWine = new HashSet<>();
		        	supplierWine.add(supplier1);
		        	if(cpt%2==0) {
		        		supplierWine.add(supplier2);
		        	}else if(cpt%3==0) {
		        		supplierWine.add(supplier3);
		        	}
		        	productWine.setStockSuppliers(supplierWine);
		        	daoVin.insertObj(productWine);
		        	cpt++;
				}
	        }
        } catch (WineException ex) {
            java.util.logging.Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        log.info("\t ### Fin du test ###");
    }
    
    public static java.util.List<ProductWine> parseSampleXml(String fileName) throws WineException
    {
    	java.util.List<ProductWine> wineList = new ArrayList<ProductWine>();
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 dbf.setNamespaceAware(true);
		 Document xml=null;
		try {
			xml = dbf.newDocumentBuilder()
							 .parse(Thread.currentThread()
							 .getContextClassLoader()
							 .getResourceAsStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		NodeList subNodes = xml.getElementsByTagName("Product");
		for(int i = 0; i<subNodes.getLength();i++){
			Node node = subNodes.item(i);
			Element tag = (Element)node;
			wineList.add(setWine(tag));
		}
		return wineList;
    }
    
    private static ProductWine setWine(Node itemNode) {
    	ProductWine p = new ProductWine();
		NodeList wineInfos = itemNode.getChildNodes();
		p.setName(extractNameFromSubNodeList(wineInfos));
		for(int i = 0; i<wineInfos.getLength();i++){
			if(wineInfos.item(i).getNodeName().equals("Id")){
				p.setApiId(Integer.parseInt(wineInfos.item(i).getTextContent()));
			}
			if(wineInfos.item(i).getNodeName().equals("Vintage")){
				p.setProductVintage(getWineVintage(wineInfos.item(i)));
			}
			if(wineInfos.item(i).getNodeName().equals("PriceRetail")){
				p.setPrice(Double.valueOf(wineInfos.item(i).getTextContent()));
			}
			if(wineInfos.item(i).getNodeName().equals("Appellation")){
				p.setAppellation(extractNameFromSubNodeList(wineInfos.item(i).getChildNodes()));	
			}
			if(wineInfos.item(i).getNodeName().equals("Varietal")){
				
				p.setProductVarietal(getWineVarietal(wineInfos.item(i)));
				p.setProductType(getWineType(wineInfos.item(i)));
				
			}
			if(wineInfos.item(i).getNodeName().equals("ProductAttributes")){
				p.setDescription(getWineDescription(wineInfos.item(i)));
			}
		}
    	return p;
    }
    
    private static ProductVintage getWineVintage(Node VintageNode){
    	String vintage = VintageNode.getTextContent();
    	ProductVintage oVintage=null;
    	try{
    		if(vintage!=null) {
    			if(vintages.containsKey(vintage)==false) {
    				oVintage = new ProductVintage(null,Integer.parseInt(vintage));
    				vintages.put(vintage, oVintage);
    			} else {
    				oVintage = (ProductVintage)vintages.get(vintage);
    			}
    		}
		}catch(NumberFormatException e) {
			//
		}
    	return oVintage;
    }
    
    private static ProductVarietal getWineVarietal(Node varietalNode){
    	String varietal = extractNameFromSubNodeList(varietalNode.getChildNodes());
		ProductVarietal oVarietal=null;
		if(varietals.containsKey(varietal)==false) {
			oVarietal = new ProductVarietal(null,varietal);
			varietals.put(varietal, oVarietal);
		} else {
			oVarietal = (ProductVarietal)varietals.get(varietal);
		}
    	return oVarietal;
    }
        
    private static ProductType getWineType(Node varietal){
    	ProductType oType=null;
    	for(int j = 0; j<varietal.getChildNodes().getLength();j++){
			if(varietal.getChildNodes().item(j).getNodeName().equals("WineType")){
				String type = extractNameFromSubNodeList(varietal.getChildNodes().item(j).getChildNodes());
				if(types.containsKey(type)==false) {
					oType = new ProductType(null,type);
					types.put(type, oType);
				} else {
					oType = (ProductType)types.get(type);
				}
			}
		}
    	return oType;
    }
    
    private static String getWineDescription(Node attributes){
    	String description = "";
		for(int j = 0; j<attributes.getChildNodes().getLength();j++){
			if(attributes.getChildNodes().item(j).getNodeName().equals("ProductAttribute")){
				if(j>0){
					description=description+"|";
				}
				description=description+extractNameFromSubNodeList(attributes.getChildNodes().item(j).getChildNodes());
			}
		}
		return description;
		
    }
    
    private static String extractNameFromSubNodeList(NodeList subNodes)
    {
    	String name = null;
    	for(int i = 0; i<subNodes.getLength();i++){
    		if(subNodes.item(i).getNodeName().equals("Name")){
    			name = subNodes.item(i).getTextContent();
    		}
    	}
    	return name;
    }
    
}
