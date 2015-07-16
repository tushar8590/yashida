package com.charliechocolatefactory.quartz.scheduler.jobs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.charliechocolatefactory.quartz.scheduler.dao.JDBCConnection;

public class ShoeMatcher {

	JDBCConnection conn ;
	private String tableName = "flipkart_men_footwear ";// fkTable name for show fetching
	static StringBuffer  varname1 = new StringBuffer();
	
	//private String childTableName;
	
	public static void main(String[] args) {
		ShoeMatcher obj = new ShoeMatcher();
		
				try {
			obj.getmappedShoes("sd_fashion");
			//		obj.getmappedShoes("omg_jabong");
			//		obj.getmappedShoes("omg_fashionara");  done
			//		obj.getmappedShoes("omg_shopclues");				
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	private void getmappedShoes(String childTableName) throws Exception{
		conn = JDBCConnection.getInstance();
		conn.setAutoCommit(false);
		int count = 1;
		//tableName = varname1.toString();
		
		
		String fkShoeQuery = "Select model,product_id,brand from "+tableName+"  ";
		//System.out.println(fkShoeQuery);
		ResultSet rs = conn.executeQuery(fkShoeQuery, null);
		//String childShoeQuery = "SELECT * FROM "+childTableName+" where MATCH(model) AGAINST (?) = (SELECT MAX(MATCH(model) AGAINST (?)) FROM "+childTableName+") and brand =  ?";
		
		String childShoeQueries[] = {"SELECT child.*,MATCH(model) AGAINST (?) FROM omg_jabong child order by MATCH(model) AGAINST (?) desc LIMIT 1",
				};
		String insertQuery = "insert into shoe_mapping values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(insertQuery);
		int fkCount = 1;
		List<String> params = new ArrayList<String>();
		while(rs.next()){
		//	System.out.println("Searching fro "+rs.getString("model"));
			//System.out.println(new  java.sql.Timestamp(new java.util.Date().getTime()));
			
			
			params.add(rs.getString("model"));
			//params.add(rs.getString("brand")); 
			params.add(rs.getString("model"));
			
			
			//for array
			for(String query:childShoeQueries){
			ResultSet rs2 = conn.executeQuery(query, params);
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
				//conn.insertData(insertQuery, params, false, null, null);
				//System.out.println(rs2.getString("model"));
				/*if(params!=null){
					int i = 1;
					for(String str: params){

						pstmt.setString(i++, str);

					}
					pstmt.addBatch();
					count= count+1;
				}*/
				
				pstmt.addBatch();count++;
			
				if(count == 1000)
				{
					
					 //System.out.println(new Timestamp(date.getTime()));
					pstmt.executeBatch();
					//conn.commit();
					pstmt.clearBatch();
					//pstmt.close();
					System.out.println("Inserted ");  conn.commit();
					count=1;
					// System.out.println(new Timestamp(date.getTime()));
				System.out.println(new  java.sql.Timestamp(new java.util.Date().getTime()));
				}
				//System.out.println(new  java.sql.Timestamp(new java.util.Date().getTime()));
			}
			
			rs2.close();
			
			/*if(count%10==0)
				System.out.println("Inserted for "+count);
			count++;*/
			}//for array
			params.clear();
		}
	}
}
