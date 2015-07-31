package com.charliechocolatefactory.quartz.scheduler.jobs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.charliechocolatefactory.quartz.scheduler.dao.JDBCConnection;

public class CustomMatcherImpl implements CustomMatcher {
	

	JDBCConnection conn ;
	String mainTable;
	List<String> childTableNames;
	String targetTable;
	String[] childQueries;
	
	public static void main(String[] args) {

		CustomMatcherImpl obj = new  CustomMatcherImpl("flipkart_men_apparel", "men_apparel_mapping");
		try {
			obj.initiate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public   void initiate() throws Exception{
		
		//for(String childTableName:this.childTableNames){
			getMappedData(childTableNames);
		//}
		
	}
	
	public CustomMatcherImpl (String mainTable,String targetTable){
		this.setMainTable(mainTable);
		this.setTargetTable(targetTable);
		childTableNames = new ArrayList<String>();
		 childTableNames.add("sd_fashion");
		 childTableNames.add("omg_fashionara");
		 childTableNames.add("omg_jabong");
		 childTableNames.add("omg_zovi");
		 childTableNames.add("omg_yepme");
		 childTableNames.add("omg_shopclues");
		 childTableNames.add("omg_indiatimes");
		 childTableNames.add("omg_paytm");
		 
	}

	@Override
	public void getMappedData(List<String> childTableNames) throws Exception {
		conn = JDBCConnection.getInstance(); 
		conn.setAutoCommit(false);
		int count = 1;
		//tableName = varname1.toString();
		
		
		String fkShoeQuery = "Select distinct model,product_id,brand from "+this.mainTable+" where mapped_flag IS NULL";
		//System.out.println(fkShoeQuery);
		ResultSet rs = conn.executeQuery(fkShoeQuery, null);

		

		int fkCount = 1;
		List<String> params = new ArrayList<String>();
		PreparedStatement pstmt = null;
		String insertQuery = "insert into "+this.targetTable+" values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(insertQuery);
		while(rs.next()){
		//	System.out.println("Searching fro "+rs.getString("model"));
			//System.out.println(new  java.sql.Timestamp(new java.util.Date().getTime()));
			for(String childTableName:childTableNames){		
				String childQuery = "SELECT child.* FROM "+ childTableName+" child where brand = ? order by MATCH(model) AGAINST (?) desc LIMIT 1";

			
			//params.add(rs.getString("model"));
			params.add(rs.getString("brand")); 
			params.add(rs.getString("model"));
			
			
			
			ResultSet rs2 = conn.executeQuery(childQuery, params);
			//System.out.println(childShoeQuery);
			
			//params.clear();
			
			if(rs2.next()){
				//System.out.println(new  java.sql.Timestamp(new java.util.Date().getTime()));
				//params.clear();
				pstmt.setString(1,rs.getString("product_id"));
				//System.out.println(rs.getString("product_id"));
				pstmt.setString(2,rs2.getString("product_id"));
				pstmt.setString(3,rs2.getString("website"));
				pstmt.setString(4,rs2.getString("section"));
				pstmt.setString(5,rs2.getString("categoryPath"));
				pstmt.setString(6,rs2.getString("model"));
				pstmt.setString(7,rs2.getString("brand"));
				pstmt.setString(8,rs2.getString("image"));
				pstmt.setString(9,rs2.getString("price"));
				pstmt.setString(10,rs2.getString("url"));
				pstmt.setString(11,rs2.getString("stock"));
				pstmt.setString(12,rs2.getString("size"));
				pstmt.setString(13,"T");
				
				
				pstmt.addBatch();count++;
				
				
				if(count == 10)
				{
					
					 //System.out.println(new Timestamp(date.getTime()));
					pstmt.executeBatch();
					conn.commit();
					pstmt.clearBatch();
					//pstmt.close();
					System.out.println("Inserted ");  conn.commit();
					count=1;
					// System.out.println(new Timestamp(date.getTime()));
				System.out.println(new  java.sql.Timestamp(new java.util.Date().getTime()));
				//System.gc();
				}
				//System.out.println(new  java.sql.Timestamp(new java.util.Date().getTime()));
			}
			
			rs2.close();
			
			/*if(count%10==0)
				System.out.println("Inserted for "+count);
			count++;*/
			
			params.clear();
		}

		// update FK flag
		String updateQuery = "Update "+this.mainTable+" set mapped_flag = 'T' where product_id = '"+rs.getString("product_id")+"'";
		conn.executeUpdate(updateQuery);
		
	
		}
		pstmt.executeBatch();
		conn.commit();
	}

	@Override
	public void setMainTable(String mainTable) {
		this.mainTable = mainTable;
		
	}

	@Override
	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
		
	}
	@Override
	public void setChildTables(List<String> children) {
		
		this.childTableNames = children;
	}

}
