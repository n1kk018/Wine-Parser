package fr.afcepf.atod.wine.data.parser;

import fr.afcepf.atod.customer.data.api.IDaoCustomer;
import fr.afcepf.atod.vin.data.exception.WineException;
import fr.afcepf.atod.wine.data.admin.api.IDaoAdmin;

import fr.afcepf.atod.wine.data.admin.api.IDaoSpecialEvent;
import fr.afcepf.atod.wine.data.order.api.IDaoPaymentInfo;
import fr.afcepf.atod.wine.data.order.api.IDaoShippingMethode;
import fr.afcepf.atod.wine.data.product.api.IDaoAdress;
import fr.afcepf.atod.wine.data.product.api.IDaoCountry;
import fr.afcepf.atod.wine.data.product.api.IDaoProduct;
import fr.afcepf.atod.wine.data.product.api.IDaoProductFeature;
import fr.afcepf.atod.wine.data.product.api.IDaoProductType;
import fr.afcepf.atod.wine.data.product.api.IDaoProductVarietal;
import fr.afcepf.atod.wine.data.product.api.IDaoProductWine;
import fr.afcepf.atod.wine.data.product.api.IDaoSupplier;
import fr.afcepf.atod.wine.data.product.impl.DaoProductFeature;
import fr.afcepf.atod.wine.entity.Admin;
import fr.afcepf.atod.wine.entity.Adress;
import fr.afcepf.atod.wine.entity.Civility;
import fr.afcepf.atod.wine.entity.Country;
import fr.afcepf.atod.wine.entity.Customer;
import fr.afcepf.atod.wine.entity.PaymentInfo;
import fr.afcepf.atod.wine.entity.Product;
import fr.afcepf.atod.wine.entity.ProductAccessories;
import fr.afcepf.atod.wine.entity.ProductFeature;
import fr.afcepf.atod.wine.entity.ProductSupplier;
import fr.afcepf.atod.wine.entity.ProductType;
import fr.afcepf.atod.wine.entity.ProductVarietal;
import fr.afcepf.atod.wine.entity.ProductVintage;
import fr.afcepf.atod.wine.entity.ProductWine;
import fr.afcepf.atod.wine.entity.ShippingMethod;
import fr.afcepf.atod.wine.entity.SpecialEvent;
import fr.afcepf.atod.wine.entity.Supplier;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FilenameUtils;
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
    private static List<ArrayList<ProductWine>> list = new ArrayList<ArrayList<ProductWine>>();
    private static Map<String, ProductFeature> features = new HashMap<String, ProductFeature>();
    private static String apiBaseUrl = "http://services.wine.com/api/beta2/service.svc/xml/";
    private static String apikey = "37662dd9dbf72936b590e8bdec649a30";

    public static void main(String[] args) {
        log.info("\t ### debut du test ###");
        /*URL url;
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
		}*/
        Locale.setDefault(Locale.US);
        BeanFactory bf = new ClassPathXmlApplicationContext("classpath:springData.xml");
        IDaoProduct daoVin = (IDaoProduct) bf.getBean(IDaoProduct.class);
        IDaoSupplier daoSupplier = (IDaoSupplier) bf.getBean(IDaoSupplier.class);
        IDaoAdmin daoAdmin = bf.getBean(IDaoAdmin.class);
        IDaoSpecialEvent daoEvent = bf.getBean(IDaoSpecialEvent.class);
        IDaoCountry daoCountry = bf.getBean(IDaoCountry.class);
        IDaoAdress daoAdr= bf.getBean(IDaoAdress.class);
        IDaoCustomer daoCustomer =bf.getBean(IDaoCustomer.class);
        IDaoShippingMethode daoShippingMethod = bf.getBean(IDaoShippingMethode.class);
        IDaoPaymentInfo daoPayment = bf.getBean(IDaoPaymentInfo.class);
        IDaoProductFeature daoFeature = (IDaoProductFeature) bf.getBean(IDaoProductFeature.class);
        
        try {
            daoCountry.insertObj(new Country(null,"AT", "Autriche"));
            daoCountry.insertObj(new Country(null,"BE", "Belgique"));
            daoCountry.insertObj(new Country(null,"BG", "Bulgarie"));
            daoCountry.insertObj(new Country(null,"CY", "Chypre"));
            daoCountry.insertObj(new Country(null,"CZ", "République Tchèque"));
            daoCountry.insertObj(new Country(null,"DE", "Allemagne"));
            daoCountry.insertObj(new Country(null,"DK", "Danemark"));
            daoCountry.insertObj(new Country(null,"EE", "Estonie"));
            daoCountry.insertObj(new Country(null,"ES", "Espagne"));
            daoCountry.insertObj(new Country(null,"FI", "Finlande"));
            daoCountry.insertObj(new Country(null,"GB", "Royaume-Uni"));
            daoCountry.insertObj(new Country(null,"GR", "Grèce"));
            daoCountry.insertObj(new Country(null,"HU", "Hongrie"));
            daoCountry.insertObj(new Country(null,"IE", "Irlande"));
            daoCountry.insertObj(new Country(null,"IT", "Italie"));
            daoCountry.insertObj(new Country(null,"JP", "Japon"));
            daoCountry.insertObj(new Country(null,"LT", "Lituanie"));
            daoCountry.insertObj(new Country(null,"LU", "Luxembourg"));
            daoCountry.insertObj(new Country(null,"LV", "Lettonie"));
            daoCountry.insertObj(new Country(null,"MT", "Malte"));
            daoCountry.insertObj(new Country(null,"NL", "Pays-Bas"));
            daoCountry.insertObj(new Country(null,"PL", "Pologne"));
            daoCountry.insertObj(new Country(null,"PT", "Portugal"));
            daoCountry.insertObj(new Country(null,"RO", "Roumanie"));
            daoCountry.insertObj(new Country(null,"SE", "Suède"));
            daoCountry.insertObj(new Country(null,"SI", "Slovénie"));
            daoCountry.insertObj(new Country(null,"SK", "Slovaquie"));
            daoCountry.insertObj(new Country(null,"US", "Etats-Unis"));
            daoCountry.insertObj(new Country(null,"FR","France"));
		} catch (WineException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
        Admin admin=null;
        Customer customer1 = null;
        Customer customer2 = null;
        Customer customer3 = null;
        
		try {
			admin = new Admin(null, "strateur", "admini", new Date(), "nicolastorero@gmail.com", "nicolastorero@gmail.com", "test1234", "0680413240", new Date(), new Date(), Civility.MR);
			customer1 = new Customer(null, "Wang", "Fen", new Date(), "fenwang@hotmail.com", "fenwang@hotmail.com", "test1234", "0666666666", new Date(), new Date(), Civility.MISS, true);
			customer2 = new Customer(null, "Anes", "Zouheir", new Date(), "zouheir.anes@gmail.com", "zouheir.anes@gmail.com", "test1234", "0666666666", new Date(), new Date(), Civility.MR, true);
			customer3 = new Customer(null, "Storero", "Nicolas", new Date(), "nicolastorero@gmail.com", "nicolastorero@gmail.com", "test1234", "0666666666", new Date(), new Date(), Civility.MR, true);
			daoAdmin.insertObj(admin);
			daoShippingMethod.insertObj(new ShippingMethod(null,"Colissimo"));
			daoPayment.insertObj(new PaymentInfo(null,"Visa"));
			daoCustomer.insertObj(customer1);
			daoCustomer.insertObj(customer2);
			Country c = daoCountry.findObj(29);
			customer3.addAdress(new Adress(null, "rue de rivoli", "18", "75001", "Paris",
                    c, false));
			customer3.addAdress(new Adress(null, "rue de rivoli", "18", "75001", "Paris",
                    c, true));
            daoCustomer.updateObj(customer3);
		} catch (WineException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Product productRand = new Product(null, "pre", 500.0, "un produit");
        SpecialEvent se = new SpecialEvent(null,"Promotest",new Date(),new Date(),new Date(),"10% sur une sélection de produits",true,admin,10);
        Product productAccessorie = new ProductAccessories(null, "un mug",25.0, "un beau mug", new Date());
        Supplier supplier1 = new Supplier(null, "Aux bon vins de Bourgogne","05 85 74 85 69","vinsbourgogne@gmail.com", new Date());
        Supplier supplier2 = new Supplier(null, "Aux bon vins de Bordeaux","04 85 74 85 69","vinsbordeaux@gmail.com", new Date());
        Supplier supplier3 = new Supplier(null, "Aux bon vins de l'Aude","07 85 74 85 69","vinsaude@gmail.com", new Date());
        try {
	        //Les Set sont particulièrement adaptés pour manipuler une grande
	        //quantité de données. Cependant, les performances de ceux-ci peuvent
	        //être amoindries en insertion. Généralement, on opte pour un HashSet,
	        //car il est plus performant en temps d'accès 
	        ProductSupplier productSuppliers1 = new ProductSupplier();
	        ProductSupplier productSuppliers2 = new ProductSupplier();
	        productSuppliers1.setProduct(productRand);
	        productSuppliers1.setSupplier(daoSupplier.insertObj(supplier1));
	        productSuppliers1.setQuantity(30);
	        productSuppliers2.setProduct(productRand);
	        productSuppliers2.setSupplier(daoSupplier.insertObj(supplier2));
	        productSuppliers2.setQuantity(15);
	        productRand.getProductSuppliers().add(productSuppliers1);
	        productRand.getProductSuppliers().add(productSuppliers2);
	        daoVin.insertObj(productRand);
	        ProductSupplier productSuppliers3 = new ProductSupplier();
	        productSuppliers3.setProduct(productAccessorie);
	        productSuppliers3.setSupplier(daoSupplier.insertObj(supplier3));
	        productSuppliers3.setQuantity(20);
	        productAccessorie.getProductSuppliers().add(productSuppliers3);
	        daoVin.insertObj(productAccessorie);
	        for (Path filepath : Files.newDirectoryStream(Paths.get(getResourcePath()+"FilesXML/Wines/"))) {
	        	if(filepath.getFileName().toString().contains("xml")){
		        	list.add(parseSampleXml("FilesXML/Wines/"+filepath.getFileName()));
	        	}
			}
        } catch (WineException ex) {
            java.util.logging.Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
        	java.util.logging.Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, null, e);
		}
        try {
            daoEvent.insertObj(se);
            if(features.isEmpty()==false) {
                for (ProductFeature pf : features.values()) {
                    daoFeature.insertObj(pf);
                }
            }
            Integer cpt = 0;
            for (ArrayList<ProductWine> subList : list) {
                for (ProductWine productWine: subList) {
                    ProductSupplier ps = new ProductSupplier();
                    ps.setProduct(productWine);
                    ps.setSupplier(supplier1);
                    ps.setQuantity(randomWithRange(1,50));
                    productWine.getProductSuppliers().add(ps);
                    if(cpt%2==0) {
                        ProductSupplier ps2 = new ProductSupplier();
                        ps2.setProduct(productWine);
                        ps2.setSupplier(supplier2);
                        ps2.setQuantity(randomWithRange(1,50));
                        productWine.getProductSuppliers().add(ps2);
                    }else if(cpt%3==0) {
                        ProductSupplier ps3 = new ProductSupplier();
                        ps3.setProduct(productWine);
                        ps3.setSupplier(supplier3);
                        ps3.setQuantity(randomWithRange(1,50));
                        productWine.getProductSuppliers().add(ps3);
                    }
                    if(cpt<11) {
                        productWine.setSpeEvent(se);
                    }
                    daoVin.insertObj(productWine);
                    cpt++;
                }
            }
        } catch (WineException paramE) {
            // TODO Auto-generated catch block
            paramE.printStackTrace();
        }
        
        /*BeanFactory bf = new ClassPathXmlApplicationContext("classpath:springData.xml");
        IDaoProduct daoVin = (IDaoProduct) bf.getBean(IDaoProduct.class);
		try {
			BeanFactory bf = new ClassPathXmlApplicationContext("classpath:springData.xml");
	        IDaoProduct daoVin = (IDaoProduct) bf.getBean(IDaoProduct.class);
	        List<Product> list = daoVin.findAllObj();
	        for (Product product : list) {
	        	String imagesUrl = ((ProductWine)product).getImagesUrl();
	        	String xmlId = ((ProductWine)product).getApiId().toString();
	        	String [] urls = imagesUrl.split("\\|");
	        	for (int i = 0; i < urls.length; i++) {
					if(urls[i].trim()!=""){
						URL url = new URL(urls[i].trim());
						String filename = FilenameUtils.getBaseName(url.toString())+"."+FilenameUtils.getExtension(url.toString());
						if(Files.exists(Paths.get(getResourcePath() + "wine_pictures/"+xmlId+"/"+filename))==false){
				    		File file = new File(getResourcePath() + "wine_pictures/"+xmlId+"/"+filename);
				    		try {
								FileUtils.copyURLToFile(url, file);
							} catch (FileNotFoundException e) {
								
							}
				    	}
						if(filename==xmlId+"m.jpg"){
							if(Files.exists(Paths.get(getResourcePath() + "wine_pictures/"+xmlId+"/"+xmlId+"l.jpg"))==false){
					    		File file = new File(getResourcePath() + "wine_pictures/"+xmlId+"/"+xmlId+"l.jpg");
					    		URL url2 = new URL(urls[i].trim().replace("m.jpg", "l.jpg"));
					    		try {
									FileUtils.copyURLToFile(url2, file);
								} catch (FileNotFoundException e) {
									
								}
					    	}
						}
					}
				}
		    	
	        	if(xmlId.length()==6){
					URL url = new URL("http://cdn.fluidretail.net/customers/c1477/"+xmlId.substring(0, 2)+"/"+xmlId.substring(2,4)+"/"+xmlId.substring(4)+"/_s/pi/n/"+xmlId+"_spin_spin2/main_variation_na_view_01_204x400.jpg");
			    	if(Files.exists(Paths.get(getResourcePath() + "wine_pictures/"+xmlId+"/"+xmlId+"_front.jpg"))==false){
			    		File file = new File(getResourcePath() + "wine_pictures/"+xmlId+"/"+xmlId+"_front.jpg");
			    		try {
							FileUtils.copyURLToFile(url, file);
						} catch (FileNotFoundException e) {
							
						}
			    	}
			    	URL url2 = new URL("http://cdn.fluidretail.net/customers/c1477/"+xmlId.substring(0, 2)+"/"+xmlId.substring(2,4)+"/"+xmlId.substring(4)+"/_s/pi/n/"+xmlId+"_spin_spin2/main_variation_na_view_07_204x400.jpg");
			    	if(Files.exists(Paths.get(getResourcePath() + "wine_pictures/"+xmlId+"/"+xmlId+"_back.jpg"))==false){
			    		File file = new File(getResourcePath() + "wine_pictures/"+xmlId+"/"+xmlId+"_back.jpg");
			    		try {
			    			FileUtils.copyURLToFile(url2, file);
			    		} catch (FileNotFoundException e) {
							
						}
			    	}
				}
	        }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        //http://cdn.fluidretail.net/customers/c1477/13/68/80/_s/pi/n/136880_spin_spin2/main_variation_na_view_01_204x400.jpg*/
        insert_translations(bf);
        log.info("\t ### Fin du test ###");
    }
    
    private static int randomWithRange(int min, int max)
    {
	   int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
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
    
    public static ArrayList<ProductWine> parseSampleXml(String fileName) throws WineException
    {
    	ArrayList<ProductWine> wineList = new ArrayList<ProductWine>();
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
			ProductWine w = setWine(tag);
			if(Files.exists(Paths.get(getResourcePath() + "wine_pictures/"+w.getApiId()+"/"+w.getApiId()+"_front.jpg"))==true) {
				wineList.add(w);
			}
		}
		return wineList;
    }
    
    private static ProductWine setWine(Node itemNode) {
    	ProductWine p = new ProductWine();
		NodeList wineInfos = itemNode.getChildNodes();
		p.setName(extractFieldFromSubNodeList(wineInfos,"Name"));
		List<String> pics = new ArrayList<String>();
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
				getLabelsImg(wineInfos.item(i),pics);	
			}
			if(wineInfos.item(i).getNodeName().equals("Vineyard")){
				getVineyardPicImg(wineInfos.item(i),pics);	
			}
			if(wineInfos.item(i).getNodeName().equals("Varietal")){
				
				p.setProductVarietal(getWineVarietal(wineInfos.item(i)));
				p.setProductType(getWineType(wineInfos.item(i)));
				
			}
			if(wineInfos.item(i).getNodeName().equals("ProductAttributes")){
				p.setDescription(getWineDescription(wineInfos.item(i)));
				p = setWineFeatures(p,wineInfos.item(i));
			}
		}
 		p.setImages(StringUtils.join(pics.iterator(),"|"));
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
    
    private static void getVineyardPicImg(Node vineyardNode,List<String> list){
    	String url = extractFieldFromSubNodeList(vineyardNode.getChildNodes(),"ImageUrl");
    	if(url.trim()!=""){
    		list.add(FilenameUtils.getBaseName(url)+"."+FilenameUtils.getExtension(url));
    	}
    }
    
    private static void getLabelsImg(Node labelsNode,List<String> list){
    	for(int j = 0; j<labelsNode.getChildNodes().getLength();j++){
    		String url = extractFieldFromSubNodeList(labelsNode.getChildNodes().item(j).getChildNodes(),"Url");
    		if(url.trim()!=""){
    			list.add(FilenameUtils.getBaseName(url)+"."+FilenameUtils.getExtension(url));
    		}
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
    
    public static void insert_translations(BeanFactory bf)
    { 
        IDaoProductType daoProductType = (IDaoProductType) bf.getBean(IDaoProductType.class);
        IDaoProductWine daoProduct = (IDaoProductWine) bf.getBean(IDaoProductWine.class);
        IDaoProductFeature daoProductFeature = (IDaoProductFeature) bf.getBean(IDaoProductFeature.class);
        IDaoProductVarietal daoProductVarietal = (IDaoProductVarietal) bf.getBean(IDaoProductVarietal.class);
        try {
            List<ProductWine> products = daoProduct.findAllObj();
            translateProductAppellation(products,daoProduct);
            List<ProductType> types = daoProductType.findAllObj();
            translateProductTypes(types,daoProductType);
            List<ProductFeature> features = daoProductFeature.findAllObj();
            translateProductFeatures(features,daoProductFeature);
            List<ProductVarietal> varietals = daoProductVarietal.findAllObj();
            translateProductVarietals(varietals,daoProductVarietal);
        } catch (WineException paramE) {
            // TODO Auto-generated catch block
            paramE.printStackTrace();
        }
    }
    
    private static void translateProductTypes(List<ProductType> types,IDaoProductType daoProductType) {
        
        try {
            
            for (ProductType productType : types) {
                    String typefr = "";
                    if(productType.getType().contains("White Wines")){
                        typefr="Vins Blancs";
                    }else if (productType.getType().contains("Red Wines")){
                        typefr="Vins Rouges";
                    }else if (productType.getType().contains("Rosé Wine")){
                        typefr="Rosés";
                    }else{
                        typefr="Champagne";
                    }
                    Locale.setDefault(Locale.FRANCE);
                    productType.setType(typefr);
                    daoProductType.updateObj(productType); 
                    Locale.setDefault(Locale.US);
            }
            
        } catch (WineException paramE) {
            // TODO Auto-generated catch block
            paramE.printStackTrace();
        }
    }
    
    private static void translateProductAppellation(List<ProductWine> products,IDaoProductWine daoProduct) {
        try {
            Locale.setDefault(Locale.FRANCE);
            for (ProductWine product : products) {
                String appelationfr = "";
                String appellation = product.getAppellation();
                if(appellation.contains("Other")) {
                    appelationfr = "Autre appelation française";
                } else if(appellation.contains("Burgundy")) {
                    appelationfr = "Bourgogne";
                } else {
                    appelationfr = appellation;
                }
                product.setAppellation(appelationfr);
                daoProduct.updateWithEvictObj(product) ;
            }
            Locale.setDefault(Locale.US);
        } catch (WineException paramE) {
            // TODO Auto-generated catch block
            paramE.printStackTrace();
        }
    }
    
    private static void translateProductFeatures(List<ProductFeature> features, IDaoProductFeature daoProductFeature) {
        
        HashMap<String, String> featuresTrad = new HashMap<String,String>();
        try {
            Locale.setDefault(Locale.FRANCE);
            featuresTrad.put("Precious Wine","Vin rare");
            featuresTrad.put("Has Large Label","Large étiquette");
            featuresTrad.put("Bordeaux Futures","Bordeaux en primeurs");
            featuresTrad.put("Wine Gift Sets","Coffrets cadeau de vin");
            featuresTrad.put("Earthy &amp; Spicy","Vin terreux et épicé");
            featuresTrad.put("Fruity &amp; Smooth","Vin fruité et rond");
            featuresTrad.put("Birthday", "Pour anniversaire");
            featuresTrad.put("Corporate Gifts", "Cadeaux d'entreprise");
            featuresTrad.put("Sweet Wine", "Vin doux");
            featuresTrad.put("Green Wines", "Vins verts");
            featuresTrad.put("90+ Rated Wine", "Vin noté 90+");
            featuresTrad.put("94+ Rated Wine", "Vin noté 94+");
            featuresTrad.put("Light &amp; Fruity", "Vin léger et fruité");
            featuresTrad.put("Congratulations", "Pour célébrations");
            featuresTrad.put("Rich &amp; Creamy", "Vin riche et crémeux");
            featuresTrad.put("Light &amp; Crisp", "Vin sec léger");
            featuresTrad.put("Smooth &amp; Supple", "Vin rond et moelleux");
            featuresTrad.put("Older Vintages","Millésimes plus anciens");
            featuresTrad.put("94+ Rated Wine Under $75","Vin noté 94+ sous les 70€");
            featuresTrad.put("Screw Cap Wines", "Vin avec bouchon vissé");
            featuresTrad.put("90+ Rated Wine Under $20","Vin noté 90+ sous les 20€");
            featuresTrad.put("Big &amp; Bold","Vin ample et audacieux");
            featuresTrad.put("Has Video","Avec Vidéo");
            featuresTrad.put("Wedding", "Idéal pour un mariage");
            featuresTrad.put("Great Bottles to Give", "Une bouteille parfaite pour offrir");
            featuresTrad.put("Boutique Wines", "Vin apprécié en boutique");
            featuresTrad.put("Champagne Gifts", "Champagne parfait pour offrir");
            featuresTrad.put("Collectible Wines", "Vins de collection");
            for (ProductFeature pf : features) {
                pf.setLabel(featuresTrad.get(pf.getLabel()));
                daoProductFeature.updateObj(pf); 
            }
            Locale.setDefault(Locale.US);
        } catch (WineException paramE) {
            // TODO Auto-generated catch block
            paramE.printStackTrace();
        }
    }
    
    private static void translateProductVarietals(List<ProductVarietal> varietals,IDaoProductVarietal daoProductVarietal) {
        try {
            Locale.setDefault(Locale.FRANCE);
            for (ProductVarietal productVarietal : varietals) {
                    String varietalfr = "";
                    String desc = productVarietal.getDescription();
                    if(desc.contains("Other")){
                        if(desc.contains("Blends")) {
                            varietalfr = "Autres assemblages de vins rouges";
                        } else {
                            if(desc.contains("Red")) {
                                varietalfr = "Autres types de vins rouges";
                            } else {
                                varietalfr = "Autres types de vins blancs";
                            }
                        }
                    } else if(desc.contains("Rhone")){
                        varietalfr = "Assemblages de vins rouges du Rhône";
                    } else if(desc.contains("Vintage")){
                        varietalfr = "Millesimé";
                    }else {
                        varietalfr = desc;
                    }
                    productVarietal.setDescription(varietalfr);
                    daoProductVarietal.updateObj(productVarietal); 
            }
            Locale.setDefault(Locale.US);
        } catch (WineException paramE) {
            // TODO Auto-generated catch block
            paramE.printStackTrace();
        }
    }
    
    private static ProductWine setWineFeatures(ProductWine p,Node attributes) {
        for(int j = 0; j<attributes.getChildNodes().getLength();j++){
            if(attributes.getChildNodes().item(j).getNodeName().equals("ProductAttribute")){
               String feature = extractFieldFromSubNodeList(attributes.getChildNodes().item(j).getChildNodes(),"Name");
               if(features.containsKey(feature)==false) {
                   features.put(feature, new ProductFeature(null, feature));
               }
               p.getFeatures().add((ProductFeature) features.get(feature));
            }
        }          
        return p;
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
