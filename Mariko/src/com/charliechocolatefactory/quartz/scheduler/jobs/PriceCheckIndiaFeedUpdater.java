package com.charliechocolatefactory.quartz.scheduler.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;










import com.charliechocolatefactory.quartz.scheduler.MainScheduler;
import com.charliechocolatefactory.quartz.scheduler.dao.JDBCConnection;
import com.charliechocolatefactory.quartz.scheduler.dao.SQLQueries;
import com.charliechocolatefactory.quartz.scheduler.model.Product;
import com.charliechocolatefactory.quartz.scheduler.model.Store;
import com.gargoylesoftware.htmlunit.BrowserVersion;


import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;






import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;



public class PriceCheckIndiaFeedUpdater implements Job {
static JDBCConnection conn;
static int Mycount = 1;
	public PriceCheckIndiaFeedUpdater() {
	}


	public static void main(String[] args) throws JsonProcessingException, IOException,Exception {
		PriceCheckIndiaFeedUpdater feedUpdater = new PriceCheckIndiaFeedUpdater();
		feedUpdater.execute(null);
	}
	
	 static boolean saveResults(List<Product> list ){
		 int i = 0;
		  boolean flag = false;
		  conn = JDBCConnection.getInstance();
		  try{
			  Iterator<Product> productListIterator = list.iterator();
			  
	            while (productListIterator.hasNext()) {
	                Product p = productListIterator.next();
	             
	                String productId = p.getId();
	                
	                
	                List<String> params = new ArrayList<String>();
	                
	               // params.add(p.getSharelink());
	                     
	                List<Store> storesList = p.getStores(); 
	                if(storesList.size() ==0)
	                	continue;
	                Iterator<Store> storeListIterator = storesList.iterator();
	                while(storeListIterator.hasNext()){
	                	/***
	                	 * check if the item exist in the database on the basis of id and website
	                	 */
	                	
	                	//params.add(productId); params.add(storeListIterator.next().getWebsite());
	                	
	                	// get the store for analysis
	                	Store store = storeListIterator.next();
	                	params.add(productId); params.add(store.getWebsite());
	                	ResultSet rs  = conn.executeQuery(SQLQueries.findProductExist, params);
	                	
	                	if(rs.next()){
	                		// update for that id - website combo
	                		params.clear();
	                		List<String> storeItems = store.getItemsForUpdates();
		                	params.addAll(storeItems);
		                	params.add(p.getId());
			                params.add(store.getWebsite());
			                flag = conn.upsertData(SQLQueries.updatePCIFeed, params);
		                	params.clear();
		                	if(flag)
		                		i++;
		                	rs.close();
	                	}else{
	                		// insert for that id - website combo
	                		params.clear();
	    	                params.add(p.getId());
	    	                params.add(p.getSection());
	    	                params.add(p.getBrand());
	    	                params.add(p.getModel());
	    	                params.add(p.getSharelink());
	                		List<String> storeItems = store.getItems();
	    	                params.addAll(storeItems);
	    	                params.add("F");  // setting the url_mapped flag to F for the newly inserted entries
	    	                params.add("F");  // added a temp flag
	    	                flag = conn.upsertData(SQLQueries.insertPCIFeed, params);
	    	              	params.clear();
		                	if(flag)
		                		i++;
	                	}
	                	
	                	
	                	
	                }
	                if(i % 10 == 0)
	                {
	                	System.out.println("Executed for 10 rows");
	                }
	                System.gc();
	            }
	            System.out.println(i+" records inserted");
	            conn.closeConnection();
		  }catch(Exception e){
			  e.printStackTrace();
			
		  }
		  
		  return flag;
		  
	  }


	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		System.out.println("PCI Updater");
		/*Mycount ++;
		if(Mycount == 3){
			System.out.println("m here");
			try {
			
				MainScheduler.getScheduler().pauseTrigger(TriggerKey.triggerKey("class com.charliechocolatefactory.quartz.scheduler.jobs.PriceCheckIndiaFeedUpdaterTrigger",MainScheduler.getScheduler().DEFAULT_GROUP));
				System.out.println("coundnt shutdown");
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}*/
		List<String> urlList = new ArrayList<String>();
		JSONParser parser = new JSONParser();
		 
        try {
        	
        	HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
			//WebDriver driver = new FirefoxDriver();
		    driver.get("http://api.pricecheckindia.com");
		    
		 driver.findElement(By.cssSelector("body > div.container > div.row > div:nth-child(2) > form > fieldset > input.input-large")).sendKeys("tushar8590@gmail.com");;
		 driver.findElement(By.cssSelector("body > div.container > div.row > div:nth-child(2) > form > fieldset > input.span2")).sendKeys("12345678");;
		driver.findElement(By.cssSelector("body > div.container > div.row > div:nth-child(2) > form > fieldset > button")).click(); 
		driver.findElement(By.cssSelector("#catalogs-link > a")).click();   
		 List<Product> list = null;
		    for(int x =2;x<=2;x++){
		    String path = "#stores > form > fieldset > table > tbody > tr:nth-child("+x+") > td:nth-child(4) > a";
		    	WebElement urlElem = driver.findElement(By.cssSelector(path));
		    //	System.out.println(url.getAttribute("href").toString());
		    	urlList.add(urlElem.getAttribute("href").toString());
			   
		    }
		       System.out.println("List Created.....");	
        	for(String url : urlList){
        	//String url = "http://api.pricecheckindia.com/feed/product/mobile_memory.json?user=tushar85&key=FGZRKLAUZVRWGHFQ"; 
        	  InputStream is = new URL(url).openStream();
        	  System.out.println("Running for "+url);
            Object obj = parser.parse(new BufferedReader(new InputStreamReader(is, "UTF-8")));
 
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray productList = (JSONArray) jsonObject.get("product");
             list = new ObjectMapper().readValue(productList.toJSONString(), new org.codehaus.jackson.type.TypeReference<List<Product>>() { });
            saveResults(list);
            list.clear();
        	}
            
        	
        	
        	driver.close();driver.quit();  
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	
	public String toString(){
		return "PCI";
		
	}
}