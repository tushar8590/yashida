package com.charliechocolatefactory.quartz.scheduler.jobs;



import java.io.BufferedReader;
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
import java.util.StringTokenizer;




import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.charliechocolatefactory.quartz.scheduler.dao.JDBCConnection;
import com.charliechocolatefactory.quartz.scheduler.dao.SQLQueries;
import com.charliechocolatefactory.quartz.scheduler.model.ProductBean;


public class InsertSDcsvtoDB implements Job {
	
	
	// old Db name    aapcorjr_aapdb9
	/*	private static String host = "jdbc:mysql://103.21.58.156:3306/aapcorjr_aapdb9";
		private static String userName = "aapcorjr_adbuser";
		private static String password = "adbuseraccess1@34";
		private static Connection con;*/
		
		
		
		private static JDBCConnection conn = null;
		private ResultSet rs;
		
		
	public static void main(String args[]){
		
		 		}


	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String fileName="C:/Users/jt3411/Documents/topFashion.csv";  

		//String fileName="C:/Users/jt3411/Downloads/277-29042015-120845.csv";  
		ProductBean  productBean = new ProductBean();
		PreparedStatement pstmt  = null;
		
		try {    
			
			BufferedReader br = new BufferedReader( new FileReader(fileName));    
			StringTokenizer st = null;    
			int lineNumber = 0, tokenNumber = 0;    
			final int batchSize = 1000;
			int count = 0;
			//String query ="INSERT INTO catproduct_omg_all(product_id, section, brand, model, website, price, image_zoom, url, stock,CategoryPath,description ) "
			//		+ "VALUES(?,?,?,?,?,?,?,?,?,?,? )";


			//pstmt = con.prepareStatement(query);
			
			int records = 0;
			
			while( (fileName = br.readLine()) != null)    
			{    
				
				

				if(lineNumber++ == 0)  
					continue;                  

				//break comma separated line using ","    
				String vendor="snapdeal";
				String arr[] = fileName.split("\t");
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
				
				
				if(productBean.getCategoryPathAsString().toLowerCase().contains("women") || productBean.getCategoryName().toLowerCase().contains("women") || productBean.getProductName().toLowerCase().contains("women")  )
				{
					count = ++count;
				}
				
				}
				catch(ArrayIndexOutOfBoundsException e){
					continue;
				}
				
			}    
			System.out.println("count:"+ count);
			//pstmt.executeBatch();
		} catch (FileNotFoundException e) {    
			// TODO Auto-generated catch block    
			e.printStackTrace();    
		} catch (Exception e) {    
			// TODO Auto-generated catch block    
			
			e.printStackTrace();    
		}

		
	}

}