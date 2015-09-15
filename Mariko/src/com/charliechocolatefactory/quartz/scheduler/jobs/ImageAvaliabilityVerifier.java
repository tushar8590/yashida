package com.charliechocolatefactory.quartz.scheduler.jobs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.charliechocolatefactory.quartz.scheduler.dao.JDBCConnection;
import com.charliechocolatefactory.quartz.scheduler.dao.SQLQueries;

public class ImageAvaliabilityVerifier {

	
	private static JDBCConnection conn = null;
	Properties props1 = new Properties();
	private ResultSet rs;
	final static Logger logger = Logger.getLogger(ImageAvaliabilityVerifier.class);

	
	public static void main(String[] args) {
		
		System.out.println(new  java.sql.Timestamp(new java.util.Date().getTime()));
		ImageAvaliabilityVerifier iav = new ImageAvaliabilityVerifier();
		iav.checkIfImageExists();
		System.out.println(new  java.sql.Timestamp(new java.util.Date().getTime()));
	}
	
	public  void checkIfImageExists(){
	
		
		
		conn = JDBCConnection.getInstance();
		
		logger.info("Starting OMG Feed Processor");
		// getting the feed firectory from Resources file
		
		try {
			props1.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("com/charliechocolatefactory/resources/ApplicationResource.properties"));
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		String query = SQLQueries.fkImgages;
		ResultSet rs = conn.executeQuery(query, null);
		try {
			while(rs.next()){
				String imageURL = rs.getString("image");
				System.out.println(imageURL);
				saveImage(imageURL,"C:\\TMP\\imgs\\images.jpg");	
			}
		} catch (SQLException | IOException e) {
	
			e.printStackTrace();
		}
		System.out.println("No Problem");
		
	}
	
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
}
