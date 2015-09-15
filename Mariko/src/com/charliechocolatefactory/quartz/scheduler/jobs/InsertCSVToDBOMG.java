package com.charliechocolatefactory.quartz.scheduler.jobs;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.charliechocolatefactory.quartz.scheduler.dao.JDBCConnection;
import com.charliechocolatefactory.quartz.scheduler.dao.SQLQueries;
import com.charliechocolatefactory.quartz.scheduler.model.ProductBean;
import com.google.common.base.Splitter;

public class InsertCSVToDBOMG implements Job {
	
	

	final static Logger logger = Logger.getLogger(InsertCSVToDBOMG.class);
		
		private static JDBCConnection conn = null;
		Properties props1 = new Properties();
		private ResultSet rs;
		
		public static void main(String args[]){
			InsertCSVToDBOMG obj = new InsertCSVToDBOMG();
			try {
				obj.execute(null);
			} catch (JobExecutionException e) {
				
				e.printStackTrace();
			}
		}

		@Override
		public void execute(JobExecutionContext arg0)
				throws JobExecutionException {
			conn = JDBCConnection.getInstance();
			
			logger.info("Starting OMG Feed Processor");
			// getting the feed firectory from Resources file
			
			try {
				props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/charliechocolatefactory/resources/ApplicationResource.properties"));
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
			String feedDirectory   = ((String)props1.get("omgFeedsDir"));
			File directory = new File(feedDirectory);

			//get all the files from a directory
			File[] fList = directory.listFiles();

			for (File file : fList){
				if(!file.isDirectory()){
					
				String completeFilePath = feedDirectory+file.getName();
				System.out.println(completeFilePath);
				  
				ProductBean  productBean = new ProductBean();
				PreparedStatement pstmt  = null;

				try {    
					logger.info("Processing Starting for "+file.getName().substring(0, file.getName().indexOf(".")));
					String table = "omg_"+file.getName().substring(0, file.getName().indexOf("."));
					// drop the existing table  (it should be OMG_sth)

					String dropTable = SQLQueries.dropGenericTable;
					dropTable = dropTable.replace("tableName",table );
				    conn.execute(dropTable);
					
				    // create the new table 
				    String createTable = SQLQueries.createGenericTable;
				    createTable = createTable.replace("tableName",table );
				    conn.execute(createTable);
				    
				    
				    // reading the feed
					BufferedReader br = new BufferedReader( new FileReader(completeFilePath));    
					StringTokenizer st = null;    
					int lineNumber = 0, tokenNumber = 0;    
					final int batchSize = 1000;
					int count = 0;
					

					String query = SQLQueries.insertGenericOMG;
					query =query.replaceAll("tableName+", table);
					System.out.println(query);
					pstmt = conn.prepareStatement(query);

					String line="";

					while( (line = br.readLine()) != null)    
					{    

						if(lineNumber++ == 0)  
							continue;                  

						//break comma separated line using ","    
						String vendor=table;
						

					//	Splitter splitter = Splitter.on(',');

						//System.out.println(new Date());
					//	List<String> lst = splitter.splitToList(line);
					//	System.out.println(lst.toArray());
						
						
						
						
					
						List<String> params = new ArrayList<String>();
					//	if(table.equalsIgnoreCase("omg_yepme")){
							Object arr[] = new Object[26];
							Pattern p = Pattern.compile("\"([^\"]*)\"");
							Matcher m = p.matcher(line);
							int i = 0;
							while (m.find()) {
							  arr[i++] =  m.group(1);
							}
							
						
						productBean.setProductID(arr[0].toString().replace('"', ' ').trim());
						productBean.setProductSKU(arr[1].toString().replace('"', ' ').trim());
						productBean.setProductName(arr[2].toString().replace('"', ' ').trim());
						productBean.setProductDescription(arr[3].toString().replace('"', ' ').trim());
						productBean.setProductPrice(arr[4].toString().replace('"', ' ').trim());
						productBean.setProductPriceCurrency(arr[5].toString().replace('"', ' ').trim());
						productBean.setWasPrice(arr[6].toString().replace('"', ' ').trim());
						productBean.setDiscountedPrice(arr[7].toString().replace('"', ' ').trim());
						if(productBean.getDiscountedPrice().equalsIgnoreCase("0.00") || productBean.getDiscountedPrice().equalsIgnoreCase("0")){
							productBean.setDiscountedPrice(productBean.getProductPrice());
						}
						productBean.setProductURL(arr[8].toString().replace('"', ' ').trim());
						productBean.setPID(arr[9].toString().replace('"', ' ').trim());
						productBean.setMID(arr[10].toString().replace('"', ' ').trim());
						productBean.setProductImageSmallURL(arr[11].toString().replace('"', ' ').trim());
						productBean.setProductImageMediumURL(arr[12].toString().replace('"', ' ').trim());
						productBean.setProductImageLargeURL(arr[13].toString().replace('"', ' ').trim());
						productBean.setMPN(arr[14].toString().replace('"', ' ').trim());
						productBean.setStockAvailability(arr[15].toString().replace('"', ' ').trim());
						productBean.setBrand(arr[16].toString().replace('"', ' ').trim());
						productBean.setLocation(arr[17].toString().replace('"', ' ').trim());
						productBean.setColour(arr[18].toString().replace('"', ' ').trim());
						productBean.setCustom1(arr[19].toString().replace('"', ' ').trim());
						productBean.setCustom2(arr[20].toString().replace('"', ' ').trim());
						productBean.setCustom3(arr[21].toString().replace('"', ' ').trim());
						productBean.setCustom4(arr[22].toString().replace('"', ' ').trim());
						productBean.setCustom5(arr[23].toString().replace('"', ' ').trim());
						productBean.setCategoryName(arr[24].toString().replace('"', ' ').trim());
						productBean.setCategoryPathAsString(arr[25].toString().replace('"', ' ').trim());
					//	System.out.println(productBean.toString());
						/*}else{
							Object arr[] = lst.toArray();
							productBean.setProductID(arr[0].toString().replace('"', ' ').trim());
							productBean.setProductSKU(arr[1].toString().replace('"', ' ').trim());
							productBean.setProductName(arr[2].toString().replace('"', ' ').trim());
							productBean.setProductDescription(arr[3].toString().replace('"', ' ').trim());
							productBean.setProductPrice(arr[4].toString().replace('"', ' ').trim());
							productBean.setProductPriceCurrency(arr[5].toString().replace('"', ' ').trim());
							productBean.setWasPrice(arr[6].toString().replace('"', ' ').trim());
							productBean.setDiscountedPrice(arr[7].toString().replace('"', ' ').trim());
							if(productBean.getDiscountedPrice().equalsIgnoreCase("0.00") || productBean.getDiscountedPrice().equalsIgnoreCase("0")){
								productBean.setDiscountedPrice(productBean.getProductPrice());
							}
							productBean.setProductURL(arr[8].toString().replace('"', ' ').trim());
							productBean.setPID(arr[9].toString().replace('"', ' ').trim());
							productBean.setMID(arr[10].toString().replace('"', ' ').trim());
							productBean.setProductImageSmallURL(arr[11].toString().replace('"', ' ').trim());
							productBean.setProductImageMediumURL(arr[12].toString().replace('"', ' ').trim());
							productBean.setProductImageLargeURL(arr[13].toString().replace('"', ' ').trim());
							productBean.setMPN(arr[14].toString().replace('"', ' ').trim());
							productBean.setStockAvailability(arr[15].toString().replace('"', ' ').trim());
							productBean.setBrand(arr[16].toString().replace('"', ' ').trim());
							productBean.setLocation(arr[17].toString().replace('"', ' ').trim());
							productBean.setColour(arr[18].toString().replace('"', ' ').trim());
							productBean.setCustom1(arr[19].toString().replace('"', ' ').trim());
							productBean.setCustom2(arr[20].toString().replace('"', ' ').trim());
							productBean.setCustom3(arr[21].toString().replace('"', ' ').trim());
							productBean.setCustom4(arr[22].toString().replace('"', ' ').trim());
							productBean.setCustom5(arr[23].toString().replace('"', ' ').trim());
							productBean.setCategoryName(arr[24].toString().replace('"', ' ').trim());
							productBean.setCategoryPathAsString(arr[25].toString().replace('"', ' ').trim());
						}*/
						
					//	System.out.println(productBean.toString());
						params.add(productBean.getProductID());
						params.add(productBean.getCategoryName());
						params.add(productBean.getBrand());
						params.add(productBean.getProductName());
						params.add(vendor);
						params.add(productBean.getDiscountedPrice());
						params.add(productBean.getProductImageSmallURL());
						params.add(productBean.getProductImageMediumURL());
						params.add(productBean.getProductImageLargeURL());
						params.add(productBean.getProductURL());
						params.add(productBean.getColour());
						params.add(productBean.getStockAvailability());
						params.add(productBean.getCategoryPathAsString());
						params.add(productBean.getCustom1());
						params.add(productBean.getCustom2());
						params.add(productBean.getCustom3());
						params.add(productBean.getCustom4());
						params.add(productBean.getCustom5());
						params.add(productBean.getProductDescription());
						

						/*for(String s:arr){
				     System.out.println(s.replace('"', ' ').trim());
				    }*/


						if(params!=null){
							int j = 1;
							for(String str: params){

								pstmt.setString(j++, str);

							}
							pstmt.addBatch();
							count= count+1;
						}

						//pstmt.execute();
						//pstmt.close();
						//System.out.println("I");
						//System.out.println(params);
						if(count == 10)
						{
							 //System.out.println(new Timestamp(date.getTime()));
							pstmt.executeBatch();
							//conn.commit();
							pstmt.clearBatch();
							//pstmt.close();
							//System.out.println("Inserted OMG");  
							count=1;
							// System.out.println(new Timestamp(date.getTime()));
						}
						//System.gc();

					}    
					pstmt.executeBatch();
					logger.info("Processing completed  for "+file.getName().substring(0, file.getName().indexOf(".")) + " For "+lineNumber+" Records");
				} catch (FileNotFoundException e) {    
					
					e.printStackTrace();   
					logger.error(e.getMessage());
				} catch (Exception e) {    
					    
					e.printStackTrace(); logger.error(e.getMessage());   
				}
			}
			}
			
			logger.info("Completed OMG Feed Processor");
			
		}
}