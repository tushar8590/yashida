package com.charliechocolatefactory.quartz.scheduler.jobs;

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
			//		obj.getmappedShoes("omg_fashionara");
			//		obj.getmappedShoes("omg_shopclues");				
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	private void getmappedShoes(String childTableName) throws SQLException{
		conn = JDBCConnection.getInstance();
		int count = 1;
		//tableName = varname1.toString();
		String fkShoeQuery = "Select model,product_id,brand from "+tableName+"  ";
		//System.out.println(fkShoeQuery);
		ResultSet rs = conn.executeQuery(fkShoeQuery, null);
		while(rs.next()){
		//	System.out.println("Searching fro "+rs.getString("model"));
			String childShoeQuery = "SELECT * FROM "+childTableName+" where MATCH(model) AGAINST (?) = (SELECT MAX(MATCH(model) AGAINST (?)) FROM "+childTableName+") and brand =  ?";
			
			List<String> params = new ArrayList<String>();
			params.add(rs.getString("model"));params.add(rs.getString("model"));params.add(rs.getString("brand"));
			ResultSet rs2 = conn.executeQuery(childShoeQuery, params);
			//System.out.println(childShoeQuery);
			
			params.clear();
			String insertQuery = "insert into shoe_mapping values(?,?,?,?,?,?,?,?,?,?,?,?)";
			if(rs2.next()){
				
				params.add(rs.getString("product_id"));
				//System.out.println(rs.getString("product_id"));
				params.add(rs2.getString("product_id"));
				params.add(rs2.getString("website"));
				params.add(rs2.getString("section"));
				params.add(rs2.getString("categoryPath"));
				params.add(rs2.getString("model"));
				params.add(rs2.getString("brand"));
				params.add(rs2.getString("image"));
				params.add(rs2.getString("price"));
				params.add(rs2.getString("url"));
				params.add(rs2.getString("stock"));
				params.add(rs2.getString("size"));
				conn.insertData(insertQuery, params, false, null, null);
				//System.out.println(rs2.getString("model"));
			}
			rs2.close();
			
			if(count%1000==0)
				System.out.println("Inserted for "+count);
			count++;
		}
	}
}
