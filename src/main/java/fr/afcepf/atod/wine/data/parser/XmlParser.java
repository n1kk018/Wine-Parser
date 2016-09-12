package fr.afcepf.atod.wine.data.parser;

import fr.afcepf.atod.vin.data.exception.WineException;
import fr.afcepf.atod.wine.data.product.api.IDaoProduct;
import fr.afcepf.atod.wine.data.product.api.IDaoSupplier;
import fr.afcepf.atod.wine.entity.Product;
import fr.afcepf.atod.wine.entity.ProductAccessories;
import fr.afcepf.atod.wine.entity.ProductSupplier;
import fr.afcepf.atod.wine.entity.ProductType;
import fr.afcepf.atod.wine.entity.ProductVarietal;
import fr.afcepf.atod.wine.entity.ProductVintage;
import fr.afcepf.atod.wine.entity.ProductWine;
import fr.afcepf.atod.wine.entity.Supplier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
        	if(Files.exists(Paths.get(getResourcePath() + "FilesXML/ategoryMap.xml"))==false){
        		File file = new File(getResourcePath() + "FilesXML/categoryMap.xml");
    			FileUtils.copyURLToFile(url, file);
        	}
        	//100 vins rouges fr au dela de 100 € 
        	url = new URL(apiBaseUrl+"/catalog?filter=categories(490+124)+price(100)&size=100&search=France&apikey="+apikey);
        	if(Files.exists(Paths.get(getResourcePath() + "FilesXML/Wines/RedWines100.xml"))==false){
        		File file = new File(getResourcePath() + "FilesXML/Wines/RedWines100.xml");
    			FileUtils.copyURLToFile(url, file);
        	}
        	//100 vins rouges fr entre 50 et 100€
        	url = new URL(apiBaseUrl+"/catalog?filter=categories(490+124)+price(50|100)&size=100&search=France&apikey="+apikey);
        	if(Files.exists(Paths.get(getResourcePath() + "FilesXML/Wines/RedWines50-100.xml"))==false){
        		File file = new File(getResourcePath() + "FilesXML/Wines/RedWines50-100.xml");
    			FileUtils.copyURLToFile(url, file);
        	}
        	//100 vins rouges fr entre 10 et 50€
        	url = new URL(apiBaseUrl+"/catalog?filter=categories(490+124)+price(10|50)&size=100&search=France&apikey="+apikey);
        	if(Files.exists(Paths.get(getResourcePath() + "FilesXML/Wines/RedWines10-50.xml"))==false){
        		File file = new File(getResourcePath() + "FilesXML/Wines/RedWines10-50.xml");
    			FileUtils.copyURLToFile(url, file);
        	}
        	//100 vins blancs fr au dela de 100 € 
        	url = new URL(apiBaseUrl+"/catalog?filter=categories(490+125)+price(100)&size=100&search=France&apikey="+apikey);
        	if(Files.exists(Paths.get(getResourcePath() + "FilesXML/Wines/WhiteWines100.xml"))==false){
        		File file = new File(getResourcePath() + "FilesXML/Wines/WhiteWines100.xml");
    			FileUtils.copyURLToFile(url, file);
        	}
        	//100 vins blancs fr entre 50 et 100€
        	url = new URL(apiBaseUrl+"/catalog?filter=categories(490+125)+price(50|100)&size=100&search=France&apikey="+apikey);
        	if(Files.exists(Paths.get(getResourcePath() + "FilesXML/Wines/WhiteWines50-100.xml"))==false){
        		File file = new File(getResourcePath() + "FilesXML/Wines/WhiteWines50-100.xml");
    			FileUtils.copyURLToFile(url, file);
        	}
        	//100 vins blancs fr entre 10 et 50€
        	url = new URL(apiBaseUrl+"/catalog?filter=categories(490+125)+price(10|50)&size=100&search=France&apikey="+apikey);
        	if(Files.exists(Paths.get(getResourcePath() + "FilesXML/Wines/WhiteWines10-50.xml"))==false){
        		File file = new File(getResourcePath() + "FilesXML/Wines/WhiteWines10-50.xml");
    			FileUtils.copyURLToFile(url, file);
        	}
        	//100 champagnes fr au dela de 100 € 
        	url = new URL(apiBaseUrl+"/catalog?filter=categories(490+123)+price(100)&size=100&search=France&apikey="+apikey);
        	if(Files.exists(Paths.get(getResourcePath() + "FilesXML/Wines/ChampagneWines100.xml"))==false){
        		File file = new File(getResourcePath() + "FilesXML/Wines/ChampagneWines100.xml");
    			FileUtils.copyURLToFile(url, file);
        	}
        	//100 champagnes fr entre 50 et 100€ 
        	url = new URL(apiBaseUrl+"/catalog?filter=categories(490+123)+price(50|100)&size=100&search=France&apikey="+apikey);
        	if(Files.exists(Paths.get(getResourcePath() + "FilesXML/Wines/ChampagneWines50-100.xml"))==false){
        		File file = new File(getResourcePath() + "FilesXML/Wines/ChampagneWines50-100.xml");
    			FileUtils.copyURLToFile(url, file);
        	}
        	//100 vins rosés fr
        	url = new URL(apiBaseUrl+"/catalog?filter=categories(490+126)&size=100&search=France&apikey="+apikey);
        	if(Files.exists(Paths.get(getResourcePath() + "FilesXML/Wines/RoseWines10-50.xml"))==false){
        		File file = new File(getResourcePath() + "FilesXML/Wines/RoseWines10-50.xml");
    			FileUtils.copyURLToFile(url, file);
        	}        	            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        BeanFactory bf = new ClassPathXmlApplicationContext("classpath:springData.xml");
        IDaoProduct daoVin = (IDaoProduct) bf.getBean(IDaoProduct.class);
        IDaoSupplier daoSupplier = (IDaoSupplier) bf.getBean(IDaoSupplier.class);

        Product productRand = new Product(null, "pre", 500.0, "un produit");

        Product productAccessorie = new ProductAccessories(null, "un mug",
                25.0, "un beau mug", new Date());
        Supplier supplier3 = new Supplier(null, "Aux bon vins de l'Aude",
                "07 85 74 85 69",
                "vinsaude@gmail.com", new Date());
        try {
	        //Les Set sont particulièrement adaptés pour manipuler une grande
	        //quantité de données. Cependant, les performances de ceux-ci peuvent
	        //être amoindries en insertion. Généralement, on opte pour un HashSet,
	        //car il est plus performant en temps d'accès 
	        ProductSupplier productSuppliers1 = new ProductSupplier();
	        ProductSupplier productSuppliers2 = new ProductSupplier();
	        productSuppliers1.setProduct(productRand);
	        productSuppliers1.setSupplier(daoSupplier.insertObj( new Supplier(null, "Aux bon vins de Bourgogne",
																	                "05 85 74 85 69",
																	                "vinsbourgogne@gmail.com", new Date())));
	        productSuppliers1.setQuantity(30);
	        
	        
	        productSuppliers2.setProduct(productRand);
	        productSuppliers2.setSupplier(daoSupplier.insertObj(new Supplier(null, "Aux bon vins de Bordeaux",
																	                "04 85 74 85 69",
																	                "vinsbordeaux@gmail.com", new Date())));
	        productSuppliers2.setQuantity(15);
	       	        
	        /*supplier1.getProductSuppliers().add(productSuppliers1);
	        supplier2.getProductSuppliers().add(productSuppliers2);*/
	        productRand.getProductSuppliers().add(productSuppliers1);
	        productRand.getProductSuppliers().add(productSuppliers2);
	        daoVin.insertObj(productRand);
	        
	        /*Set<Supplier> suppliersAccessorie= new HashSet<Supplier>();
	        suppliersAccessorie.add(supplier1);
	        productAccessorie.setStockSuppliers(suppliersAccessorie);
	        daoVin.insertObj(productAccessorie);
	        
	        for (Path filepath : Files.newDirectoryStream(Paths.get(getResourcePath()+"FilesXML/Wines/"))) {
	        	if(filepath.getFileName().toString().contains("xml")){
		        	list = parseSampleXml("FilesXML/Wines/"+filepath.getFileName());
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
			}*/
        } catch (WineException ex) {
            java.util.logging.Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } /*catch (IOException e) {
        	java.util.logging.Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, e);
		}*/
        //http://cdn.fluidretail.net/customers/c1477/13/68/80/_s/pi/n/136880_spin_spin2/main_variation_na_view_01_204x400.jpg
        log.info("\t ### Fin du test ###");
    }
    
    private static String getResourcePath() {
        try {
            URI resourcePathFile = System.class.getResource("/RESOURCE_PATH").toURI();
            String resourcePath = Files.readAllLines(Paths.get(resourcePathFile)).get(0);
            URI rootURI = new File("").toURI();
            URI resourceURI = new File(resourcePath).toURI();
            URI relativeResourceURI = rootURI.relativize(resourceURI);
            return relativeResourceURI.getPath();
        } catch (Exception e) {
            return null;
        }
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
		p.setName(extractFieldFromSubNodeList(wineInfos,"Name"));
		List<String> picsUrl = new ArrayList<String>();
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
				p.setAppellation(extractFieldFromSubNodeList(wineInfos.item(i).getChildNodes(),"Name"));	
			}
			if(wineInfos.item(i).getNodeName().equals("Labels")){
				getLabelsUrl(wineInfos.item(i),picsUrl);	
			}
			if(wineInfos.item(i).getNodeName().equals("Vineyard")){
				getVineyardPicUrl(wineInfos.item(i),picsUrl);	
			}
			if(wineInfos.item(i).getNodeName().equals("Varietal")){
				
				p.setProductVarietal(getWineVarietal(wineInfos.item(i)));
				p.setProductType(getWineType(wineInfos.item(i)));
				
			}
			if(wineInfos.item(i).getNodeName().equals("ProductAttributes")){
				p.setDescription(getWineDescription(wineInfos.item(i)));
			}
		}
 		p.setImagesUrl(StringUtils.join(picsUrl.iterator(),"|"));
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
    
    private static void getVineyardPicUrl(Node vineyardNode,List<String> list){
    	list.add(extractFieldFromSubNodeList(vineyardNode.getChildNodes(),"ImageUrl"));
    }
    
    private static void getLabelsUrl(Node labelsNode,List<String> list){
    	for(int j = 0; j<labelsNode.getChildNodes().getLength();j++){
    		list.add(extractFieldFromSubNodeList(labelsNode.getChildNodes().item(j).getChildNodes(),"Url"));
    	}
    }
    
    private static ProductVarietal getWineVarietal(Node varietalNode){
    	String varietal = extractFieldFromSubNodeList(varietalNode.getChildNodes(),"Name");
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
				String type = extractFieldFromSubNodeList(varietal.getChildNodes().item(j).getChildNodes(),"Name");
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
				description=description+extractFieldFromSubNodeList(attributes.getChildNodes().item(j).getChildNodes(),"Name");
			}
		}
		return description;
		
    }
    
    private static String extractFieldFromSubNodeList(NodeList subNodes,String fieldName)
    {
    	String name = null;
    	for(int i = 0; i<subNodes.getLength();i++){
    		if(subNodes.item(i).getNodeName().equals(fieldName)){
    			name = subNodes.item(i).getTextContent();
    		}
    	}
    	return name;
    }
    
}
