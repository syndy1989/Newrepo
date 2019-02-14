package com.mobile.clinic.controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.json.simple.JSONObject;

import com.mobile.clinic.db.DBConnection;



public class SRDetails {
	public static String filepath;

Properties properties = new Properties();
ConfigureProperties csp=new ConfigureProperties();
	FileInputStream propertiesFile;
	Connection connection;
	DBConnection db = new DBConnection();
	CallableStatement cstmt = null;

	public String getSRdetails() {
		String srid = null;
		connection = db.getConnection();
		String SQL = "{call create_SR_number ()}";
		try {
			cstmt = connection.prepareCall(SQL);

			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {

				srid = rs.getString(1);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				cstmt.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return srid;

	}
	PreparedStatement preparedStatement;
	public void storingSRReuest(String reqId, String path, String apkfilename) {
		
		 connection =	db.getConnection();
		 
		 
		 try {
			preparedStatement = connection.prepareStatement(
						"insert into srrequestTable(srRequestId,srReqPath,status,fileName) values (?, ?, ?,?)");
			preparedStatement.setString(1, reqId);
			preparedStatement.setString(2, path);
			preparedStatement.setString(3, "Started");
			preparedStatement.setString(4, apkfilename);
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally
		 {
			 try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 }
			
	}
	ResultSet rs;
	public String getSRRequesttotrigger() {
		//public static void main(String[] args) {
			 connection =	db.getConnection();
			 String status ="";
			 if(connection != null)
			 {
				 System.out.println("Connection Successfully Established - SRDetails"); 
			 }
			 try {
				preparedStatement = connection.prepareStatement(
							"select status from srrequesttable sr where sr.status =?");
				
				preparedStatement.setString(1, "InProgress");
				rs=preparedStatement.executeQuery();
				while(rs.next())
				{
					status=rs.getString("status");
					System.out.println("Getting status"+rs.getString("status"));
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 finally
			 {
				 try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			 }
			return status;
				
		}
	public String saveTemplate(String propFileName, String template)
	{
		String reqId=getSRdetails();
		
		//String propFileName = "clinicconfig.properties";
		
		BufferedReader brdel = null;
		
		File propfile = null;
		try {
			
			//File destinationFolder=new File("/Mobile_Clinic1/"+reqId);
			File destinationFolder=new File("/Mobile_Clinic1/"+reqId);
			if (!destinationFolder.exists())
				destinationFolder.mkdirs();
		
			
			System.out.println(destinationFolder);
			
			propertiesFile=new FileInputStream(propFileName);
			properties.load(propertiesFile);
			
			 propfile=new File(destinationFolder+"/"+propFileName);
			FileWriter fileOut = new FileWriter(propfile);
			
				properties.store(fileOut, "Upadted Details");
				fileOut.close();
			
				File appLanuch =new File(destinationFolder+File.separator+"AppLanuch");
				if (!appLanuch.exists())
					appLanuch.mkdirs();
				
				File newFile=new File(destinationFolder+File.separator+"NFTmobile");
				if (!newFile.exists())
					newFile.mkdirs();
				FileWriter appfile = new FileWriter(newFile + "/" + "appsize.json");
				JSONObject appjson = new JSONObject();
				appjson.put("FileSize", 5.2);
				
				appfile.write("" + appjson.toString());
				appfile.flush();
				appfile.close();
				
				moveMobileTemplate(template, newFile.getAbsolutePath());
				
				storingSRReuest(reqId, destinationFolder.getAbsolutePath(), propfile.getAbsolutePath());
				
				csp.updatePropertites(propfile.getAbsolutePath(),reqId,destinationFolder,appLanuch,newFile);
			
		} catch ( IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propfile.getAbsolutePath();
		
		
	}
	
	public String saveTemplateForApp(String propFileName, String template,String appname,String packagename)
	{
		String reqId=getSRdetails();
		
		//String propFileName = "clinicconfig.properties";
		
		BufferedReader brdel = null;
		
		File propfile = null;
		try {
			
			//File destinationFolder=new File("/Mobile_Clinic1/"+reqId);
			File destinationFolder=new File(filepath+"//"+appname+"//"+reqId);
			if (!destinationFolder.exists())
				destinationFolder.mkdirs();
		
			
			System.out.println(destinationFolder);
			
			propertiesFile=new FileInputStream(propFileName);
			properties.load(propertiesFile);
			
			 propfile=new File(destinationFolder+"/"+propFileName);
			FileWriter fileOut = new FileWriter(propfile);
			
				properties.store(fileOut, "Upadted Details");
				fileOut.close();
			
				File appLanuch =new File(destinationFolder+File.separator+"AppLanuch");
				if (!appLanuch.exists())
					appLanuch.mkdirs();
				
				File newFile=new File(destinationFolder+File.separator+"NFTmobile");
				if (!newFile.exists())
					newFile.mkdirs();
				FileWriter appfile = new FileWriter(newFile + "/" + "appsize.json");
				JSONObject appjson = new JSONObject();
				appjson.put("FileSize", 5.2);
				
				appfile.write("" + appjson.toString());
				appfile.flush();
				appfile.close();
				
				moveMobileTemplate(template, newFile.getAbsolutePath());
				
				storingSRReuest(reqId, destinationFolder.getAbsolutePath(), propfile.getAbsolutePath());
				
				csp.updatePropertitesForApp(propfile.getAbsolutePath(),reqId,destinationFolder,appLanuch,newFile,appname,packagename);
			
		} catch ( IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propfile.getAbsolutePath();
		
		
	}
	
	public boolean moveMobileTemplate(String template, String rootFolder) throws IOException, InterruptedException
	{

		System.out.println("cmd /c start  xcopy /E /Y \""+template+"\" \""+rootFolder+"\\\""+" & exit");
		
		Process p1;
		p1 = Runtime.getRuntime().exec("cmd /c start  xcopy /E /Y \""+template+"\" \""+rootFolder+"\\\""+" & exit");
	
		
	p1.waitFor();
		
		return true;
		
	}

	public void upadteSRRequest(String reqId) {
	
			
			 connection =	db.getConnection();
			 
			 if(connection != null)
			 {
				System.out.println("Connection Successfully Established"); 
			 }
			 try {
				preparedStatement = connection.prepareStatement(
							"update srrequesttable sr set sr.status=? where sr.srRequestId=?");
				preparedStatement.setString(1, "InProgress");
				preparedStatement.setString(2, reqId);
				preparedStatement.executeUpdate();
				
				System.out.println("Completed Successfully");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 finally
			 {
				 try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			 }
				
		
		// TODO Auto-generated method stub
		
	}
	public void upadteSRRequestasCompleted(String reqId) {
		// TODO Auto-generated method stub
		
		 connection =	db.getConnection();
		 
		 if(connection != null)
		 {
			System.out.println("Connection Successfully Established"); 
		 }
		 try {
			preparedStatement = connection.prepareStatement(
						"update srrequesttable sr set sr.status=? where sr.srRequestId=?");
			preparedStatement.setString(1, "Completed");
			preparedStatement.setString(2, reqId);
			preparedStatement.executeUpdate();
			
			System.out.println("Completed Successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally
		 {
			 try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 }
	}
	
	public void upadteSRRequestasError(String reqId) {
		// TODO Auto-generated method stub
		
		 connection =	db.getConnection();
		 
		 if(connection != null)
		 {
			System.out.println("Connection Successfully Established"); 
		 }
		 try {
			preparedStatement = connection.prepareStatement(
						"update srrequesttable sr set sr.status=? where sr.srRequestId=?");
			preparedStatement.setString(1, "Error");
			preparedStatement.setString(2, reqId);
			preparedStatement.executeUpdate();
			
			System.out.println("Completed Successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally
		 {
			 try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 }
	}
	
	/*public static void main(String[] args)
	{
		SRDetails sr=new SRDetails();
		
		try {
			File fl=new File("C:\\Mobile_Clinic1\\SR0208\\");
			System.out.println(fl.getPath());
			File serverfinalpath=new File("C:/Program Files/apache-tomcat-7.0.37/webapps/SR0208");
			if(!serverfinalpath.isDirectory())
			{
				serverfinalpath.mkdir();
			}
			sr.moveMobileTemplate(fl.getPath(), serverfinalpath.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
}
