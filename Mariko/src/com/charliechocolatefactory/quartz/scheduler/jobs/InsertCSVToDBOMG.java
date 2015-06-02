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

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.charliechocolatefactory.quartz.scheduler.dao.JDBCConnection;
import com.charliechocolatefactory.quartz.scheduler.dao.SQLQueries;
import com.charliechocolatefactory.quartz.scheduler.model.ProductBean;
import com.google.common.base.Splitter;

public class InsertCSVToDBOMG implements Job {
	
	

		
		
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
			
			
			// getting the feed firectory from Resources file
			
			try {
				props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/charliechochlatefactory/resources/ApplicationResource.properties"));
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
					
					String table = "omg_"+file.getName().substring(0, file.getName().indexOf("."));
					// drop the existing table

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
						

						Splitter splitter = Splitter.on(',');

						//System.out.println(new Date());
						List<String> lst = splitter.splitToList(line);
					//	System.out.println(lst.toArray());
						
						
						
						
						Object arr[] = lst.toArray();
						
						List<String> params = new ArrayList<String>();
						productBean.setProductID(arr[0].toString().replace('"', ' ').trim());
						productBean.setProductSKU(arr[1].toString().replace('"', ' ').trim());
						productBean.setProductName(arr[2].toString().replace('"', ' ').trim());
						productBean.setProductDescription(arr[3].toString().replace('"', ' ').trim());
						productBean.setProductPrice(arr[4].toString().replace('"', ' ').trim());
						productBean.setProductPriceCurrency(arr[5].toString().replace('"', ' ').trim());
						productBean.setWasPrice(arr[6].toString().replace('"', ' ').trim());
						productBean.setDiscountedPrice(arr[7].toString().replace('"', ' ').trim());
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
							int i = 1;
							for(String str: params){

								pstmt.setString(i++, str);

							}
							pstmt.addBatch();
							count= count+1;
						}

						//pstmt.execute();
						//pstmt.close();
						//System.out.println("I");
						//System.out.println(params);
						if(count == 1000)
						{
							 //System.out.println(new Timestamp(date.getTime()));
							pstmt.executeBatch();
							//conn.commit();
							pstmt.clearBatch();
							//pstmt.close();
							System.out.println("Inserted OMG");  
							count=1;
							// System.out.println(new Timestamp(date.getTime()));
						}
						//System.gc();

					}    
					pstmt.executeBatch();
				} catch (FileNotFoundException e) {    
					
					e.printStackTrace();    
				} catch (Exception e) {    
					    
					e.printStackTrace();    
				}
			}
			}
			
		}
}