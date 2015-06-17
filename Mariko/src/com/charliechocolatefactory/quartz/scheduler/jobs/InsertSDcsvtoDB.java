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

public class InsertSDcsvtoDB implements Job {
	
	
	final static Logger logger = Logger.getLogger(InsertSDcsvtoDB.class);
	private static JDBCConnection conn = null;
	Properties props1 = new Properties();
	private ResultSet rs;
		
	public static void main(String args[]){
		InsertSDcsvtoDB obje = new InsertSDcsvtoDB();
		try {
			obje.execute(null);
		} catch (JobExecutionException e) {
			
			e.printStackTrace();
		}
}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		conn = JDBCConnection.getInstance();
		logger.info("Starting SD Feed Processor");
		
		// getting the feed firectory from Resources file
		
		try {
			props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/charliechocolatefactory/resources/ApplicationResource.properties"));
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		 
		String feedDirectory   = ((String)props1.get("sdFeedsDir"));
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
			
			String table = file.getName().substring(0, file.getName().indexOf("."));
			logger.info("Table Name "+table);
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


			String query = SQLQueries.insertGenericSD;
			query =query.replaceAll("tableName+", table);
			System.out.println(query);

			
			
			pstmt = conn.prepareStatement(query);
			
			
			String line="";
			while(  (line = br.readLine()) != null)    
			{    
				
				

				if(lineNumber++ == 0)  
					continue;                  
                

				//break comma separated line using ","    
				String vendor="snapdeal";
				String arr[] = line.split("\t");
				List<String> params = new ArrayList<String>();
				try{
				
				productBean.setProductID(arr[0]);
				
				//System.out.println(productBean.getProductID());
				productBean.setProductName(arr[1]);
				productBean.setProductDescription(arr[2]);
				productBean.setBrand(arr[3]);
				productBean.setProductURL(arr[4]);
				productBean.setProductImageLargeURL(arr[5]);
				productBean.setCategoryName(arr[7]);
				productBean.setCategoryPathAsString(arr[9]);
				productBean.setDiscountedPrice(arr[10]);
				productBean.setStockAvailability(arr[12]);
				

				params.add(productBean.getProductID());
				params.add(productBean.getCategoryName());
				params.add(productBean.getBrand());
				params.add(productBean.getProductName());
				params.add(vendor);
				params.add(productBean.getDiscountedPrice());
				params.add(productBean.getProductImageLargeURL());
				params.add(productBean.getProductURL());
				params.add(productBean.getStockAvailability());
				params.add(productBean.getCategoryPathAsString());
				params.add(productBean.getProductDescription());

				}
				catch(ArrayIndexOutOfBoundsException e){
					continue;
				}
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

				if(count == 1000)
				{
					pstmt.executeBatch();
					
					
					pstmt.clearBatch();
					//pstmt.close();
					System.out.println("Inserted");  
					count=1;
				}


			}    
			pstmt.executeBatch();
			logger.info("Processing completed  for "+table + " For "+lineNumber+" Records");
		} catch (FileNotFoundException e) {    
			logger.error(e.getMessage());  
			e.printStackTrace();    
		} catch (Exception e) {    
			logger.error(e.getMessage());   
			
			e.printStackTrace();    
		}
	}
}
		logger.info("Completed SD Feed Processor");	
	}

}