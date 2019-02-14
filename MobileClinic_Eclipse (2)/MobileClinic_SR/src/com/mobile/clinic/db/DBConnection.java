package com.mobile.clinic.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
	
	Properties properties = new Properties();
	
	FileInputStream propertiesFile,propertiesFile1;
	String JDBC_DRIVER = null;
	String DB_URL = null;
	String DB_REPORT_URL = null;
	String USER = null;
	String PASS = null;
	Connection con=null;
	
	public  Connection getConnection()	{
		
	try{
		String propFileName = "dbconfig.properties";
		 
		propertiesFile=new FileInputStream(propFileName);
		
		properties.load(propertiesFile);
		JDBC_DRIVER = properties.getProperty("driverName");
		DB_URL = properties.getProperty("driverUrl");
		USER = properties.getProperty("userName");
		PASS = properties.getProperty("passWord");
		System.out.println("driverurl"+DB_URL);
		propertiesFile.close();
	Class.forName(JDBC_DRIVER);  
	    con=DriverManager.getConnection(DB_URL,USER,PASS);  
	    
	    if(con!=null)
	    {
	    	System.out.println("success");
	    }
	 
}
	catch(Exception e){e.printStackTrace();}
	return con; 
}
	
	public static void main(String[] args)
	{
		DBConnection db = new DBConnection();
		db.getConnection();
	}
	
	
}
