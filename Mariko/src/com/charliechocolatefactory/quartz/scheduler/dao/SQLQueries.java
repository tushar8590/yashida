/**
 * 
 */
package com.charliechocolatefactory.quartz.scheduler.dao;

/**
 * @author jn1831
 *
 */



import java.util.*;
import java.util.List;

public class SQLQueries {

	//public static String insertProductMaster;
	public static String insertProductVendor = "insert into product_vendor values(?,?,?,?,?,?,?,?,?)";
	public static String rawProductMaster = "SELECT product_id,product_title FROM product_master WHERE product_sub_category = ? and mapped_flag = 'F'";
	public static String updateProductMaster = "update product_master set mapped_flag = 'T' where product_id = ?";
	public static String updateProductMasterFlipkart = "update product_master set mapped_fk = 'T' where product_id = ?";
	public static String updateProductMasterNaaptol = "update product_master set mapped_naaptol = 'T' where product_id = ?";
	//public static String 
	public static String updateProductInfiFLag = "update product_master set mapped_infi = 'T'  where product_id = ?";

	public static String flipkartRawProductMaseter = "SELECT product_id,product_title FROM product_master WHERE product_main_category = ? and mapped_fk = 'F'";
	//flipkart spec loader 
	public static String flipkartProductURL = "SELECT * FROM product_vendor WHERE product_vendor  = ? AND product_id LIKE ? AND product_id NOT IN (select product_id FROM product_specs)";
	public static String flipkartCamsURL = "SELECT * FROM product_vendor WHERE master_product_id IN (SELECT product_id FROM product_master WHERE product_sub_category IN (?)) AND product_vendor = ?";
	public static String insertProductSpecs = "insert into product_specs(product_id,product_spec_details) VALUES(?,?)";
	public static String infibeamRawProductMaseter = "SELECT product_id,product_title FROM product_master WHERE product_sub_category = ? and mapped_infi = 'F'";
	public static String naaptolRawProductMaseter = "SELECT product_id,product_title FROM product_master WHERE product_sub_category = ? and mapped_naaptol = 'F'";
	public static String insertPCIFeed = "insert into pci_product_feed_temp values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String getURLsPCIFeed = "select id,url,website from pci_product_feed where url_mapped = 'F' LIMIT 3000";
	public static String insertproduct_pci_url_temp = "INSERT INTO product_pci_url_temp(id,vendor,url) VALUES(?,?,?)";
	public static String updatePCIFeedForUrlMapping = "update pci_product_feed_temp set url_mapped = 'T',url = ? where id = ? and website = ?";
	public static String updatePCIFeedForUrlMappingForInvalidUrls = "update pci_product_feed_temp set url_mapped = 'X',url = ? where id = ? and website = ?";

	// PCIfeed updater queries
	public static String findProductExist = "select * from pci_product_feed_temp where id = ? and website = ?";
	public static String updatePCIFeed = "update pci_product_feed_temp set price = ? ,offer = ? , stock = ?,url_mapped = 'U' where id = ? and website = ?";
	
	
	
	public static String googleShoesData ="SELECT product_id,product_title FROM product_details_snd_top_selling WHERE product_sub_cat_1 = ? AND google_map='T' AND product_brand IN('Reebok','Bata','Nike','Adidas','Liberty','Woodland','Valentino','Converse','Lancer','Lotto','RedTape','Puma','Paragon','Relaxo','Action','Fila','Coolers','Force 10','Sharon','Italiano','Lues Alberto','Amblin','Khadims','Genius','Cyke','G Sports','Montee Cairo','Globalite','Wood Style','Alberto Torresi','Aria','Catwalk','Sole Threads','Gliders','Timberland','United Colors of Benetton','Tiptopp','Sparx') ORDER BY product_title";

	 public static String updategoogleMaster = "update product_details_snd_top_selling set google_map = 'T' where product_id = ?";
	 
	 
	 public static String getPartiallyResolvedURLS = "SELECT id,url,website FROM pci_product_feed_temp  WHERE url_mapped='F'";
	 
	 public static String getURLFromPCIFeed = "select product_id,url from cat_search_product_data where website = ? and  spec_resolved = 'F' LIMIT 5000";
	 public static String insertPciSpecMaster = "insert into pci_spec_master(product_id,product_spec_details) VALUES(?,?)";
	 public static String updatePCISpecFlag = "update cat_search_product_data set spec_resolved = 'T' where product_id = ?";
	
	 /*adding new queries for the updater   */
	 public static String getMasterFeedDataForupdate = "select * from elec_product_master where section = ? and updated_flag = 'N' and multi_vendor ='N'";
	 // insert query for the multiVendor product data
	 public static String insertElecMultiVendorData = "insert into elec_multi_vendor(id,model,url,website,offer,price,stock,color,rating) values(?,?,?,?,?,?,?,?,?)";
	 public static String  updateElecProductMaster = "update elec_product_master set updated_flag = 'Y',multi_vendor ='Y' where product_id = ?";
	 public static String logElecUnmapped = "insert into elec_multi_vendor_unmapped values(?,?,?,?)";
	 public static String deferUpdateElecMultiVendor = "update elec_product_master set multi_vendor = 'D', updated_flag = 'D' where product_id = ?";
	 
	 
	 
	 // for test
	 public static String getDesc  = "Select  description from catproduct_omg_all where section = 'Root' and  website ='zovi'";
	 public static String getDataUpdaterFromURLTemp  = "SELECT  id,url,vendor FROM product_pci_url_temp where flag = 'F' LIMIT 5000";
	 public static String updateURL = "update pci_product_feed_temp set url = ? where id = ? and website = ?";
	 public static String updateTempURLFlag = "update product_pci_url_temp set flag = 'T' where url = ? and id = ? and vendor = ?";
	 
	 
	 // for New Aparel Feed Loader 201506
	 // drop table
	 public static String dropGenericTable = "drop table IF EXISTS tableName";
	 public static String truncateGenericTable = "truncate table  tableName";
	 public static String createGenericTable = "CREATE TABLE tableName (    product_id TINYTEXT CHARACTER SET latin1,    section TEXT CHARACTER SET latin1,    brand TEXT CHARACTER SET latin1,    model TEXT CHARACTER SET latin1,    website TEXT CHARACTER SET latin1,    price TEXT CHARACTER SET latin1,    image TEXT CHARACTER SET latin1,    image_medium TEXT,    image_zoom TEXT,    url TEXT CHARACTER SET latin1,    color TEXT CHARACTER SET latin1,    offers TEXT CHARACTER SET latin1,    stock TEXT CHARACTER SET latin1,    CategoryPath TEXT CHARACTER SET latin1,    custom1 TEXT CHARACTER SET latin1,    custom2 TEXT CHARACTER SET latin1,    custom3 TEXT CHARACTER SET latin1,    custom4 TEXT CHARACTER SET latin1,    custom5 TEXT CHARACTER SET latin1,    description TEXT CHARACTER SET latin1,    size TEXT,    size_variants TEXT,    color_variants TEXT,    menu_link TEXT,    style_code TEXT,FULLTEXT KEY `searchIndex` (`brand`,`model`,`website`)  ) ENGINE=MYISAM DEFAULT CHARSET=utf8";
	 
	 public static String insertGenericOMG  = "INSERT INTO tableName(product_id, section, brand, model, website, price, image, image_medium, image_zoom, url, color, stock,CategoryPath,custom1,custom2,custom3, custom4, custom5,description ) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
	 public static String insertGenericFK  = "INSERT INTO tableName(product_id, section, brand, model, website, price, image_zoom, url,color,offers, stock,CategoryPath,description, size, size_variants, color_variants,style_code ) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
	 
	 public static String insertGenericSD = "INSERT INTO tableName(product_id, section, brand, model, website, price, image_zoom, url, stock,CategoryPath,description ) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,? )";
	 
	 public static String insertShop = "insert into tableName values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
	 
	 // get the fk Images for Verification
	 public static String fkImgages = "select CASE WHEN image IS NOT NULL AND TRIM(image) <> '' THEN TRIM(image)  WHEN image_medium IS NOT NULL AND TRIM(image_medium) <> '' THEN TRIM(image_medium)  ELSE TRIM(image_zoom) END AS image FROM flipkart_men_apparel f WHERE section = 'Shirts' LIMIT 5 ";
}
