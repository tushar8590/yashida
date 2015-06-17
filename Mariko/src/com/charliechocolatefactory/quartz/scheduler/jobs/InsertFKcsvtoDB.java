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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.charliechocolatefactory.quartz.scheduler.dao.JDBCConnection;
import com.charliechocolatefactory.quartz.scheduler.dao.SQLQueries;
import com.charliechocolatefactory.quartz.scheduler.model.ProductBean;

public class InsertFKcsvtoDB implements Job {
	
	final static Logger logger = Logger.getLogger(InsertFKcsvtoDB.class);
	private static JDBCConnection conn = null;
	Properties props1 = new Properties();
	private ResultSet rs;
		
		public static void main(String args[]){
			
			InsertFKcsvtoDB obj = new InsertFKcsvtoDB();
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
			logger.info("Starting FK Feed Processor");
			try {
				props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/charliechocolatefactory/resources/ApplicationResource.properties"));
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}

			String feedDirectory   = ((String)props1.get("fkFeedsDir"));
			File directory = new File(feedDirectory);


						//get all the files from a directory
						File[] fList = directory.listFiles();

						for (File file : fList){
							String completeFilePath = feedDirectory+file.getName();
							System.out.println(completeFilePath);

							  
							ProductBean  productBean = new ProductBean();
							PreparedStatement pstmt  = null;

							try {  
								
								
								String table = file.getName().substring(0, file.getName().indexOf("."));
								logger.info("Table Name  "+table);
								// drop the existing table

								String dropTable = SQLQueries.dropGenericTable;
								dropTable = dropTable.replace("tableName",table );
							    conn.execute(dropTable);
								
							    // create the new table 
							    String createTable = SQLQueries.createGenericTable;
							    createTable = createTable.replace("tableName",table );
							    conn.execute(createTable);

								BufferedReader br = new BufferedReader( new FileReader(completeFilePath));    
								StringTokenizer st = null;    
								int lineNumber = 0, tokenNumber = 0;    
								final int batchSize = 1000;
								int count = 0;
					
								String query = SQLQueries.insertGenericFK;
								query =query.replaceAll("tableName+", table);
							//	System.out.println(query);
								pstmt = conn.prepareStatement(query);

								String line="";

								while( (line = br.readLine()) != null)    
								{    

									if(lineNumber++ == 0)  
										continue;                  
									//break comma separated line using ","    
									String vendor="flipkart";
									//String arr[] = fileName.split("\"");
									java.util.Date date= new java.util.Date();
									 
									 
									String []arr = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

									List<String> params = new ArrayList<String>();
									try{

										productBean.setProductID(arr[0].replace('"', ' '));
										//System.out.println(productBean.getProductID());
										productBean.setProductName(arr[1].replace('"', ' '));
										productBean.setProductDescription(arr[2].replace('"', ' '));
										productBean.setProductImageLargeURL(arr[3].replace('"', ' '));
										productBean.setDiscountedPrice(arr[5].replace('"', ' ').replace(",INR", ""));
										productBean.setProductURL(arr[6].replace('"', ' '));
										productBean.setCategoryName(arr[7].replace('"', ' '));
										productBean.setBrand(arr[8].replace('"', ' '));
										productBean.setStockAvailability(arr[10].replace('"', ' '));
										productBean.setOffers(arr[13]);
										productBean.setSize(arr[16]+ " "+arr[18].replace('"', ' '));
										productBean.setColour(arr[17].replace('"', ' ') );
										productBean.setSizeVariants(arr[19].replace('"', ' '));
										productBean.setColorVaraiants(arr[20].replace('"', ' '));
										productBean.setStyleCode(arr[21].replace('"', ' '));

										String []images =  productBean.getProductImageLargeURL().split(",");
										String imageFinal ="";
										for(String imagesTemp: images){
											if(imagesTemp.contains("400x400")){
												imageFinal = imagesTemp;
											}
										}
										if(imageFinal == ""){
											imageFinal = images[0];
										}
										String []catName=  productBean.getCategoryName().split(">");

										params.add(productBean.getProductID());
										params.add(catName[catName.length -1]);
										params.add(productBean.getBrand());
										params.add(productBean.getProductName());
										params.add(vendor);
										params.add(productBean.getDiscountedPrice());
										params.add(imageFinal);
										params.add(productBean.getProductURL());
										params.add(productBean.getColour());
										params.add(productBean.getOffers());
										params.add(productBean.getStockAvailability());
										params.add(productBean.getCategoryName());
										params.add(productBean.getProductDescription());
										params.add(productBean.getSize());
										params.add(productBean.getSizeVariants());
										params.add(productBean.getColorVaraiants());
										params.add(productBean.getStyleCode());


									}
									catch(ArrayIndexOutOfBoundsException e){
										continue;
									}
									

									if(params!=null){
										int i = 1;
										for(String str: params){

											pstmt.setString(i++, str);

										}
										pstmt.addBatch();
										count= count+1;
									}

									
									if(count == 1000)
									{
										//System.out.println(new Timestamp(date.getTime()));
										pstmt.executeBatch();
										
										pstmt.clearBatch();
										//pstmt.close();
										//System.out.println("Inserted Flipkart");  
										count=1;
										//System.out.println(new Timestamp(date.getTime()));
									}


								}    
								pstmt.executeBatch();
								logger.info("Processing completed  for "+table + " For "+lineNumber+" Records");
							} catch (FileNotFoundException e) {    
								// TODO Auto-generated catch block    
								e.printStackTrace();    
								logger.error(e.getMessage());
							} catch (Exception e) {    
								// TODO Auto-generated catch block    

								e.printStackTrace();   logger.error(e.getMessage()); 
							}
						}
						logger.info("Completed FK Feed Processor");
		}
		
}