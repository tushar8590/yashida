package com.charliechocolatefactory.quartz.scheduler.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class ShopcluesFeedProcessor implements Job{

	
	final static Logger logger = Logger.getLogger(InsertFKcsvtoDB.class);
	private static JDBCConnection conn = null;
	Properties props1 = new Properties();
	private ResultSet rs;
	
	public static void main(String[] args) {
		ShopcluesFeedProcessor obj = new ShopcluesFeedProcessor();
		try {
			obj.execute(null);
		} catch (JobExecutionException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		conn = JDBCConnection.getInstance();
		logger.info("Starting Shopclues Processor");
		try {
			props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/charliechocolatefactory/resources/ApplicationResource.properties"));
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		String feedDirectory   = ((String)props1.get("shopcluesDir"));
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

				String truncateTable = SQLQueries.truncateGenericTable;
				truncateTable = truncateTable.replace("tableName",table );
			    conn.execute(truncateTable);
				
			    // create the new table 
			    

				BufferedReader br = new BufferedReader( new FileReader(completeFilePath));    
				StringTokenizer st = null;    
				int lineNumber = 0, tokenNumber = 0;    
				final int batchSize = 1000;
				int count = 0;
	
				String query = SQLQueries.insertShop;
				query =query.replaceAll("tableName+", table);
			//	System.out.println(query);
				pstmt = conn.prepareStatement(query);

				String line="";

				while( (line = br.readLine()) != null)    
				{    

					if(lineNumber++ == 0)  
						continue;                  
					
					String vendor=table;
					//String arr[] = fileName.split("\"");
					java.util.Date date= new java.util.Date();
					 
					 
					String []arr = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					
					List<String> params = new ArrayList<String>();
					try{
						for(int k=0;k<26;k++)
							params.add(arr[k]);


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
