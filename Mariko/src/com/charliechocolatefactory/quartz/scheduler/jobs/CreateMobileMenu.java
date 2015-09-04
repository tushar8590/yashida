package com.charliechocolatefactory.quartz.scheduler.jobs;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateMobileMenu {
	
	
	// old Db name    aapcorjr_aapdb9
		private static String host = "jdbc:mysql://103.21.58.156:3306/aapcorjr_dbaapcompare9";
		private static String userName = "aapcorjr_adbuser";
		private static String password = "adbuseraccess1@34";
		
	/*	private static String host = "jdbc:mysql://localhost:3306/aapcorjr_aapdb9";
		private static String userName = "root";
		private static String password = "";
		*/
		
		private static Connection con;
		
		
	
		private ResultSet rs;
		
	public static void main(String args[]){
		try{
			
			// Load the Driver class. 
		Class.forName("com.mysql.jdbc.Driver");
		// If you are using any other database then load the right driver here.

		//Create the connection using the static getConnection method
		con = DriverManager.getConnection (host,userName,password);
		con.setAutoCommit(false);

		}catch(Exception e){
			e.printStackTrace();
		}
		 //		String fileName="C:/Users/jt3411/Documents/topFashion.csv";  

		//String fileName="C:/Users/jt3411/Downloads/277-29042015-120845.csv";  
	
		//Statement stmt  = null;
		Set cat = new HashSet();
		Set level1Set = new HashSet();

		List catLevel1 = new ArrayList();
		List catLevel2 = new ArrayList();
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append("<nav id='off-canvas-menu' >");
		strBuilder.append("<div id='off-canvas-menu-title'>MENU<span class='icon icon-cancel-3' id='off-canvas-menu-close'></span></div>");
		strBuilder.append("<ul class='expander-list'>"); // top leve ul
		
		try {
			String query ="select  distinct main_menu from menu";

			Statement stmt = con.createStatement();

			ResultSet rs;
			
				rs = stmt.executeQuery(query);
			
				while(rs.next())
				{
					String mainMenu=rs.getString(1);
					
					strBuilder.append("	<li><span class='name'> <span class='expander'>-</span> "+mainMenu+"</a> </span>");
				//	strBuilder.append("<dd class='item-content'><div class='navbar-main-submenu'><div class='row wrapper'>");
				  
				    String queryMenu1 ="select distinct menu_level1,main_menu_image from menu where main_menu = '"+ mainMenu +"' and includedtop ='1'";
				    strBuilder.append("<ul>"); // second level ul
					Statement stmt1 = con.createStatement();

					ResultSet rs1;
					
						rs1 = stmt1.executeQuery(queryMenu1);
					int count = 1;
						while(rs1.next())
						{
							 String menuLevel1=rs1.getString(1);
							 String mainMenuImage=rs1.getString(2);
							 /*if(count != 1){
							 strBuilder.append("<ul>");
							 
							 }*/
							 count++;
								strBuilder.append("<li> <span class='name'> <span class='expander'>-</span> <a href='listing.html'><span class='fa fa-mobile'></span>"+menuLevel1+"</a> </span>");
								
							 
							    String queryMenu2 ="select menu_level2,href from menu where main_menu = '"+ mainMenu +"' and menu_level1 = '"+ menuLevel1 +"' and includedtop ='1'";

								Statement stmt2 = con.createStatement();

								ResultSet rs2;
								strBuilder.append("<ul>"); // third level ul
									rs2 = stmt2.executeQuery(queryMenu2);
								
									while(rs2.next())
									{
										 String menuLevel2 = rs2.getString(1);
										 String href = rs2.getString(2);
										 
										strBuilder.append("<li><span class='name'><a href='"+href+"'>"+menuLevel2+"</a></li>");
									}
							  strBuilder.append("</ul></li>");
						}
						strBuilder.append("	</ul>");

					
				   
				}
				strBuilder.append("	</li></ul>");
				
				strBuilder.append("</ul></nav>");
				
				System.out.println(strBuilder);
			


			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	}}