package com.charliechocolatefactory.quartz.scheduler.dao;

import java.sql.*;
import java.util.List;
import java.util.Properties;


public class JDBCConnection {

	Properties props1 = new Properties();
	
	
	
	//private String host = "jdbc:mysql://103.21.58.156:3306/aapcorjr_aapdb9";
	private String host = "";
	//private String host = "jdbc:mysql://103.21.58.156:3306/aapcorjr_dbaapcompare9";
	/*private String userName = "aapcorjr_adbuser";
	private String password = "adbuseraccess1@34";*/
	

	private static Connection con;
	
	private static JDBCConnection conn = null;
	private ResultSet rs;
	private static int counter = 1;
	
	private JDBCConnection() {
		try{
			
		props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/charliechocolatefactory/resources/DBConfig.properties"));
		 String userName = props1.getProperty("DB_USERNAME"); 
		// String password = props1.getProperty("DB_PASSWORD");
		 String password = "";
		 String host = props1.getProperty("DB_SERVER");
		 
			// Load the Driver class. 
		Class.forName("com.mysql.jdbc.Driver");
		// If you are using any other database then load the right driver here.

		//Create the connection using the static getConnection method
		con = DriverManager.getConnection (host,userName,password);

		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void setAutoCommit(boolean state){
		try {
			con.setAutoCommit(state);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	public static void  commit(){
		try {
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static JDBCConnection getInstance(){
		 
		try {
			if(conn == null || con.isClosed()){
				synchronized(JDBCConnection.class){
					if(conn == null || con.isClosed()){
						conn = new JDBCConnection();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
		//System.out.println("JDBC Count for "+counter++);
		// new JDBCConnection();
	}
	
	public ResultSet executeQuery(String query,List<String> params){
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
		if(params!=null){
			int i = 1;
			for(String str: params){
			
				pstmt.setString(i++, str);
				
			}
			}
		
			rs = pstmt.executeQuery();
			
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
		return rs;
	}
	
	public boolean insertData(String query,List<String> params,boolean isUpdate,String parentProductId,String vendor){
		PreparedStatement pstmt  = null;
		PreparedStatement pstmt1 = null;
		
		boolean flag = false;
		
		try {
			pstmt = con.prepareStatement(query);
		
		
		
		if(params!=null){
			int i = 1;
			for(String str: params){
			
				pstmt.setString(i++, str);
				
			}
		}
		
			//System.out.println(query);
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
			
			// this boolean condition is for the data like clothing, home 
			//and furnishing for which compare feature is not req
			
			if(isUpdate){
				
				
			if(vendor.equals("flipkart"))
				pstmt1 = con.prepareStatement(SQLQueries.updateProductMasterFlipkart);
			else if(vendor.equals("infi"))
				pstmt1 = con.prepareStatement(SQLQueries.updateProductInfiFLag);
			else if(vendor.equals("google"))
			    pstmt1 = con.prepareStatement(SQLQueries.updategoogleMaster);
			else
		    	pstmt1 = con.prepareStatement(SQLQueries.updateProductMaster);
			
			
			pstmt1.setString(1,parentProductId);
				if(pstmt1.executeUpdate()>0){
				flag = true;
				}
			}
			
		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
		}finally{
			/*try {
				//pstmt.close();
				//pstmt1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
		}
		return flag;
	}
	
	
	public boolean upsertData(String query,List<String> params){
		PreparedStatement pstmt  = null;
		boolean flag = false;
		
		try {
		conn = getInstance();
			pstmt = con.prepareStatement(query);
			if(params!=null){
			int i = 1;
			for(String str: params){
				pstmt.setString(i++, str);
			}
		}
		
			//System.out.println(query);
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
			
		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
		}finally{
			/*try {
				//pstmt.close();
				//pstmt1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
		}
		return flag;
	}
	
	public boolean insertBatch(String query,List<String> params){
		boolean flag = false;
		
		
		
		
		return flag;
	}
    
	public PreparedStatement prepareStatement(String query) throws Exception{
		return con.prepareStatement(query);
	}
	
	
	
	public boolean execute(String query){
		boolean flag = false;
		try {
			Statement stmt = con.createStatement();
		
		flag = stmt.execute(query);
			
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
		return flag;
	}
	public boolean executeUpdate(String query){
		int flag = 0;
		Statement stmt =null;
		try {
			stmt = con.createStatement();
		
		flag = stmt.executeUpdate(query);
			
		} catch (SQLException e) {
	
			e.printStackTrace();
		}finally{
			try {
				if(stmt!=null && !stmt.isClosed())
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag == 0?true:false;
	}
	
	
	public void closeConnection(){
		try {
			if(this.con!=null)
			this.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
