package com.charliechocolatefactory.quartz.scheduler.jobs;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.charliechocolatefactory.quartz.scheduler.dao.JDBCConnection;
import com.charliechocolatefactory.quartz.scheduler.dao.SQLQueries;
import com.charliechocolatefactory.quartz.scheduler.model.ProductBean;



public class ShoeMatcher {

	
	private JDBCConnection  conn;
	private String categoryPath = "Footwear>Men&s Footwear>Shoes>Sports Shoes";
	public static void main(String[] args) {

	}
	
	private void getMatchedRecords() throws Exception{
		// get the matched recorfs from 
		String query = "SELECT * FROM flipkart_men_footwear WHERE categorypath = ?" ;
		
		conn = JDBCConnection.getInstance();
		List<String> params = new ArrayList<String>();
		params.add("Footwear>Men&s Footwear>Shoes>Sports Shoes");
		ResultSet rs  = conn.executeQuery(query,params);
		
		while(rs.next()){
			// build the full text search query for gettin the data from multiple tables
			String model = rs.getString("model");
			query = "Select * from sd_fashion  WHERE MATCH(model) AGAINST (?) = (SELECT MAX(MATCH(model) AGAINST (?)) FROM `sd_fashion` WHERE MATCH(model) AGAINST (?) ORDER BY MATCH(model) AGAINST (?)  DESC)";
			List<String> paramsInner = new ArrayList<String>();
			paramsInner.add("Puma Cricket Shoes");paramsInner.add("Puma Cricket Shoes");paramsInner.add("Puma Cricket Shoes");paramsInner.add("Puma Cricket Shoes");
			ResultSet rs2 = conn.executeQuery(query, paramsInner);
			while(rs2.next()){
				System.out.println(rs2.getString("model"));
			}
		}
		
		
	}

}
