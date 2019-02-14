package com.android.app;

//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.service.local.AppiumDriverLocalService;
//import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.remote.DesiredCapabilities;

import com.charles.POC.CharlesPing;
import com.mobile.clinic.controller.JsonfileParsing;
import com.mobile.clinic.controller.SRDetails;
import com.nft.collector_method.IndivdualCollector;
import com.nft.collector_method.MetricsCollector;

public class androidStandaloneapp {
	JSONObject click = new JSONObject();
	JSONArray root = new JSONArray();
	static Process process;
	public static String packageName;
    public static String launcherActivity;
    public static String BROWSER_NAME;
    public static String VERSION;
    public static String deviceName;
    public static String platformName;
    public static String newCommandTimeout;
    public static String appiumUrl;
    public static String charlspath;
    public static String filePath;
    public static String filePath1;
    public static String filePath2;
    public static String apiUrl;
    public static String downlaodName;
    public static String requestId;
    public static String androidver;
    public static int cores;
    static String host;
    public static int count = 0;
    static JSONObject appLaunchdata;
    static SRDetails srdel=new SRDetails();
    public static long deviation;
    
    public Boolean startExecution(String appname,String packagename)
    {
    	
    	
    	try {
    		Properties properties1=new Properties();
        	FileInputStream propertyfile1;
        	propertyfile1= new FileInputStream("clinicconfig.properties");
        	properties1.load(propertyfile1);
        	SRDetails.filepath = properties1.getProperty("resultFolder");
//        	/responseTimeDev
        	deviation = Long.parseLong(properties1.getProperty("responseTimeDev"));
    		String filename=srdel.saveTemplateForApp("clinicconfig.properties",properties1.getProperty("templatePath"),appname,packagename);
        	String status=srdel.getSRRequesttotrigger();
        	Properties properties=new Properties();
        	FileInputStream propertyfile;
    		
    		propertyfile= new FileInputStream(filename);
    		properties.load(propertyfile);
    		packageName=properties.getProperty("packageName");
    		    launcherActivity=properties.getProperty("launcherActivity");
    		    BROWSER_NAME=properties.getProperty("browserName");
    		    VERSION=properties.getProperty("version");
    		   deviceName=properties.getProperty("deviceName");
    		    platformName=properties.getProperty("platformName");
    		    newCommandTimeout=properties.getProperty("newCommandTimeout");
    		    host=properties.getProperty("host");
    		    charlspath=properties.getProperty("charlspath");
    		    CharlesPing.charlesPath = properties.getProperty("charlesWorkingfolder");
    		   // appiumUrl=properties.getProperty("appiumUrl");
    		    apiUrl=properties.getProperty("apiUrl");
    		    requestId=properties.getProperty("requestID");
    		    filePath=properties.getProperty("filePath");
    		    filePath1= properties.getProperty("filePath1");
    		    filePath2=properties.getProperty("filePath2");
    		    androidver=properties.getProperty("AndroidVersionOver6");
    		    cores=Integer.parseInt(properties.getProperty("NoOfCores"));
    		    MetricsCollector.setVersion(androidver, cores);
    	        IndivdualCollector.setVersion(androidver, cores);
    		if(!status.equalsIgnoreCase("InProgress") || ! status.isEmpty())   
    		{
    		srdel.upadteSRRequest(requestId);
       		}
    		start_charles();
    		} catch (  Exception e) {
    		// TODO Auto-generated catch block
    			
    		e.printStackTrace();
    		return false;
    		}
    	return true;
    }
    
    
  
    public Boolean startAppLaunch()
    {
    	 try {
			CharlesPing.Startconnection();
			CharlesPing.ClearCharles();
			MetricsCollector.start("App-Launch", androidStandaloneapp.filePath, androidStandaloneapp.packageName, androidStandaloneapp.launcherActivity, false);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			srdel.upadteSRRequestasError(requestId);
			e.printStackTrace();
			return false;
		}
    	 /*finally {

             System.out.println("Final Block");
             //process.destroy();
             //driver.removeApp(packageName);
            // driver.quit();
             //this.stop_charles();
            // service.stop();
             
             System.out.println("End");
             return false;
         }*/
    	 return true;
    }
    
    public Boolean stopAppLaunch(long totalTime)
    {
    	 try {
    		 long time = totalTime-deviation;
    		 androidStandaloneapp.appLaunchdata = MetricsCollector.stopInApp(time);
    		 CharlesPing.Stopconnection();
             Thread.sleep(5000);
             androidStandaloneapp.downlaodName = CharlesPing.downloadcsv(androidStandaloneapp.count,"App-Launch");
          CharlesPing.finalJson("App-Launch");
       
          
          
		} catch (IOException | ParseException | InterruptedException e) {
			// TODO Auto-generated catch block
			srdel.upadteSRRequestasError(requestId);
			e.printStackTrace();
			return false;
		}
    	 /*finally {

             System.out.println("Final Block");
             //process.destroy();
             //driver.removeApp(packageName);
            // driver.quit();
             //this.stop_charles();
            // service.stop();
             
             System.out.println("End");
             return false;
         }*/
    	 return true;
    }
    
    public void startMetricsCollector(String usecasename)
    {
    	try {
			MetricsCollector.start(usecasename, androidStandaloneapp.filePath1, androidStandaloneapp.packageName, androidStandaloneapp.launcherActivity, false);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			 srdel.upadteSRRequestasError(requestId);
			e.printStackTrace();
		}
    	
    }
    
  public void stopMetricsCollector(String usecasename)
  {
	  
      try {
    	  CharlesPing.finalJson(usecasename);
		JSONObject stopmet=MetricsCollector.stop();
		JSONObject hits0 = new JSONObject();
        hits0.put("_source", stopmet);
        root.add(hits0);
        
	} catch (IOException | ParseException e) {
		// TODO Auto-generated catch block
		 srdel.upadteSRRequestasError(requestId);
		e.printStackTrace();
	} 
      
     
  }
    
  public void startIndividualCollector(String TransactionName)
  {
	  try {
		CharlesPing.Startconnection();
		 CharlesPing.ClearCharles();
		 IndivdualCollector.start(TransactionName, filePath1, packageName);
	} catch (IOException | InterruptedException e) {
		// TODO Auto-generated catch block
		srdel.upadteSRRequestasError(requestId);
		e.printStackTrace();
	}
     
      
  }
  
  
  public void stopIndividualCollector(String TransactionName,long time)
  {
	  try {
		  long tottime = time-deviation;
		  IndivdualCollector.stopInApp(false,tottime);
		    CharlesPing.Stopconnection();
		    Thread.sleep(5000);
		    downlaodName = CharlesPing.downloadcsv(count, TransactionName);
	} catch (IOException | InterruptedException e) {
		// TODO Auto-generated catch block
		srdel.upadteSRRequestasError(requestId);
		e.printStackTrace();
	}
     
      
  }
    
   public void stopExecution()
   {
	   try {
		JSONArray individual=IndivdualCollector.stopInApp(true);
		System.out.println("individual:"+individual.toJSONString());
		FileWriter fw2=new FileWriter(new File(filePath2+"individual.json"));
        fw2.write(individual.toJSONString());
        fw2.flush();
        fw2.close();
         CharlesPing.Stopconnection();
         
         JSONObject hits0 = new JSONObject();
         hits0.put("_source", androidStandaloneapp.appLaunchdata);
         root.add(hits0);
         
		String reportJsn = CharlesPing.Json_Usecase();
		JSONArray clickarray = new JSONArray();
		clickarray.add(click);
		JsonfileParsing jsparsing =new JsonfileParsing();
		jsparsing.jsonFileCreationForApp(filePath2, root, clickarray, reportJsn, requestId, apiUrl,filePath1);
		srdel.upadteSRRequestasCompleted(requestId);
		System.out.println(root.toJSONString());
		stop_charles();
	} catch (InterruptedException | IOException e) {
		// TODO Auto-generated catch block
		srdel.upadteSRRequestasError(requestId);
		e.printStackTrace();
	}
   }
   
   public static void start_charles(){
       try{
       System.out.println("Opening Charles");
       Runtime runTime = Runtime.getRuntime();
        process = runTime.exec(charlspath);
        Thread.sleep(30000);
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
       } 
   
   
   public static void stop_charles(){
       try{
       System.out.println("Closing Charles");
       process.destroy();
       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
   } 

}
