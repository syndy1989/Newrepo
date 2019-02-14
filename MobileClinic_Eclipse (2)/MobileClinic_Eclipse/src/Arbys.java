import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.charles.POC.CharlesPing;
import com.mobile.clinic.controller.JsonfileParsing;
import com.mobile.clinic.controller.SRDetails;
import com.nft.collector_method.IndivdualCollector;
import com.nft.collector_method.MetricsCollector;

public class Arbys {
	  AndroidDriver<WebElement> driver;
	    // String filePath= "D:\\Mobileclinique\\Multiple_Usecase\\UseCase1\\";
	    // String filePath1= "D:\\Mobileclinique\\Multiple_Usecase\\UseCase2\\";
	    // IndivdualCollector ic1=new IndivdualCollector();
	    JSONObject click = new JSONObject();
	    JSONArray root = new JSONArray();
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
	    public static int count = 0;
	    static Process process;
	    static boolean isServerRunning = false;
	    static AppiumDriverLocalService service;
	    static String host;
	    static String appiumHome=System.getenv("APPIUM_HOME")+"/node_modules/appium/bin/appium.js";
	    static int port=4723;
	    static String nodeHome=System.getenv("APPIUM_HOME")+"/node.exe";
	    static SRDetails srdel=new SRDetails();
	public static void main (String[] args)
	{
		
		Arbys mc=new Arbys();
		String filename=srdel.saveTemplate("clinicconfig.properties","C:\\Mobileclinique\\NFTMobile");
		
		String status=srdel.getSRRequesttotrigger();
		Properties properties=new Properties();
		FileInputStream propertyfile;
		//String filename="clinicconfig.properties";
		
		try {
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
			    appiumUrl=properties.getProperty("appiumUrl");
			    apiUrl=properties.getProperty("apiUrl");
			    requestId=properties.getProperty("requestID");
			    filePath=properties.getProperty("filePath");
			    filePath1= properties.getProperty("filePath1");
			    filePath2=properties.getProperty("filePath2");
			 if(!status.equalsIgnoreCase("InProgress") || ! status.isEmpty())   
			 {
				 srdel.upadteSRRequest(requestId);
			    mc.setUp();
			 }
		} catch (  Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	 public void setUp() throws Exception {
		
	       System.out.println(packageName);
	        DesiredCapabilities capabilities = new DesiredCapabilities();
	        capabilities.setCapability("BROWSER_NAME", BROWSER_NAME);
	        capabilities.setCapability("VERSION", VERSION);
	        capabilities.setCapability("deviceName", deviceName);
	        capabilities.setCapability("platformName", platformName);
	        capabilities.setCapability("newCommandTimeout", newCommandTimeout);
	       // capabilities.setCapability("app",app.getAbsolutePath());

	        capabilities.setCapability("appPackage", packageName);
	        // This package name of your app (you can get it from apk info app)
	        capabilities.setCapability("appActivity", launcherActivity); // This is
	                                                                        // Launcher
	                                                                        // activity
	                                                                        // of
	                                                                        // your
	                                                                        // app
	                                                                        // (you
	                                                                        // can
	                                                                        // get
	                                                                        // it
	                                                                        // from
	                                                                        // apk
	                                                                        // info
	                                                                        // app)
	        // Create RemoteWebDriver instance and connect to the Appium server
	        // It will launch the Calculator App in Android Device using the
	        // configurations specified in Desired Capabilities
	        boolean isServerRunningcheck=checkIfServerIsRunnning(port);
	        System.out.println(isServerRunningcheck);
	        System.out.println(appiumHome);
	        
	        System.out.println(nodeHome);
	        try{
	        if(isServerRunningcheck==false){
	        
	        this.Appium_start();
	        
	        }
	        
	        else{
	            this.Appium_stop();
	            Thread.sleep(10000);
	            this.Appium_start();
	        }
	        long start = System.currentTimeMillis();

	        
	        this.start_charles();
	         CharlesPing.Startconnection();
	     CharlesPing.ClearCharles();
	     
	     
	    System.out.println("appiumUrl-------"+appiumUrl);
	        MetricsCollector.start("App-Launch", filePath, packageName, launcherActivity, false);
	        Thread.sleep(5000);
	       // driver = new AndroidDriver<WebElement>(new URL(appiumUrl), capabilities);
	        System.out.println("App Launch------------------------------------");
	        Thread.sleep(25000);
	        JSONObject application1 = MetricsCollector.stop();

	         CharlesPing.Stopconnection();
	         Thread.sleep(5000);
	     downlaodName = CharlesPing.downloadcsv(count,"App-Launch");
	      CharlesPing.finalJson("App-Launch");

	      this.testCal(application1);

	        //this.teardown();
	        }
	        catch(Exception e){
	       	 srdel.upadteSRRequestasError(requestId);
	            e.printStackTrace();
	        }
	        finally {

	            System.out.println("Final Block");
	            //process.destroy();
	            //driver.removeApp(packageName);
	            driver.quit();
	            this.stop_charles();
	            service.stop();
	            
	            System.out.println("End");
	        }
	    }
	 
	    private void testCal(JSONObject application1) {
	    	try {
	    		MetricsCollector.start("U1_Arbys", filePath1, packageName, launcherActivity, false);
	    	     Thread.sleep(15000);
	    	     CharlesPing.Startconnection();
	    	     CharlesPing.ClearCharles();
	    	    
	    	     
	    			
	    			String s1 = "Click1";
	    			s1 = s1.replaceAll("[ \"']", "");
	    			IndivdualCollector.start("U1_Arbys_01_Click_SignIn", filePath1, packageName);
	    			System.out.println("Click SignIn_SignUp:----------------------------------" + s1);
	    			Thread.sleep(30000);
	    			IndivdualCollector.stop(false, driver, "test");
	    			
	    			CharlesPing.Stopconnection();
	    			 Thread.sleep(5000);
	    		downlaodName = CharlesPing.downloadcsv(count, "U1_Arbys_01_Click_SignIn");

	    				    			
	    			//Equipment
	    			CharlesPing.Startconnection();
	    		     CharlesPing.ClearCharles();
	    			String s2 = "Click2";
	    			s2 = s2.replaceAll("[ \"']", "");
	    			IndivdualCollector.start("U1_Arbys_02_SignIntoHome", filePath1, packageName);
	    			System.out.println("Click SignIn to Home:----------------------------------" + s2);
	    			Thread.sleep(30000);
	    			IndivdualCollector.stop(false, driver, "test");
	    			CharlesPing.Stopconnection();
	    			 Thread.sleep(5000);
	    			downlaodName = CharlesPing.downloadcsv(count, "U1_Arbys_02_SignIntoHome");

	    			//Billing
	    			CharlesPing.Startconnection();
	    		     CharlesPing.ClearCharles();
	    			String s3 = "Click3";
	    			s3 = s3.replaceAll("[ \"']", "");
	    			IndivdualCollector.start("U1_Arbys_03_BurgersTab", filePath1, packageName);
	    			System.out.println("Click Burgers tab:----------------------------------" + s3);
	    			Thread.sleep(15000);
	    			IndivdualCollector.stop(false, driver, "test");
	    			CharlesPing.Stopconnection();
	    			 Thread.sleep(5000);
	    			downlaodName = CharlesPing.downloadcsv(count, "U1_Arbys_03_BurgersTab");

	    			//View Statements
	    			CharlesPing.Startconnection();
	    		     CharlesPing.ClearCharles();
	    			String s4 = "Click4";
	    			s4 = s4.replaceAll("[ \"']", "");
	    			IndivdualCollector.start("U1_Arbys_04_SelectPeroduct", filePath1, packageName);
	    			System.out.println("Select a Product:----------------------------------" + s4);
	    			Thread.sleep(15000);
	    			IndivdualCollector.stop(false, driver, "test");
	    			CharlesPing.Stopconnection();
	    			 Thread.sleep(5000);
	    			downlaodName = CharlesPing.downloadcsv(count, "U1_Arbys_04_SelectPeroduct");
	    			
	    			//View Statements
	    			CharlesPing.Startconnection();
	    		     CharlesPing.ClearCharles();
	    			String s5 = "Click5";
	    			s5 = s5.replaceAll("[ \"']", "");
	    			IndivdualCollector.start("U1_Arbys_05_BacktoBurgerstab", filePath1, packageName);
	    			System.out.println("Back to Burgers tab : ---------------------------------" + s5);
	    			Thread.sleep(15000);
	    			IndivdualCollector.stop(false, driver, "test");
	    			CharlesPing.Stopconnection();
	    			 Thread.sleep(5000);
	    			downlaodName = CharlesPing.downloadcsv(count, "U1_Arbys_05_BacktoBurgerstab");
	    			
	    			
	    			//View Statements
	    			CharlesPing.Startconnection();
	    		     CharlesPing.ClearCharles();
	    			String s6 = "Click6";
	    			s6 = s6.replaceAll("[ \"']", "");
	    			IndivdualCollector.start("U1_Arbys_06_HamburgerMenu", filePath1, packageName);
	    			System.out.println("Hamburger manut:----------------------------------" + s6);
	    			Thread.sleep(15000);
	    			IndivdualCollector.stop(false, driver, "test");
	    			CharlesPing.Stopconnection();
	    			 Thread.sleep(5000);
	    			downlaodName = CharlesPing.downloadcsv(count, "U1_Arbys_06_HamburgerMenu");
	    			
	    			//View Statements
	    			CharlesPing.Startconnection();
	    		     CharlesPing.ClearCharles();
	    			String s7 = "Click7";
	    			s7 = s7.replaceAll("[ \"']", "");
	    			IndivdualCollector.start("U1_Arbys_07_MyAccount", filePath1, packageName);
	    			System.out.println("My Account:----------------------------------" + s7);
	    			Thread.sleep(15000);
	    			IndivdualCollector.stop(false, driver, "test");
	    			CharlesPing.Stopconnection();
	    			 Thread.sleep(5000);
	    			downlaodName = CharlesPing.downloadcsv(count, "U1_Arbys_07_MyAccount");

	    			//OpenStatements
	    			
	    			
	    			CharlesPing.Startconnection();
	    		     CharlesPing.ClearCharles();
	    			String s14 = "Click8";
	    			s14 = s14.replaceAll("[ \"']", "");
	    			IndivdualCollector.start("U1_Arbys_08_LogOut", filePath1, packageName);
	    			System.out.println("LogOut:----------------------------------" + s14);
	    			Thread.sleep(15000);//Thread.sleep(10000);
	    			Thread.sleep(5000);
	    			JSONArray individual=IndivdualCollector.stop(true, driver, "test");
	    		    System.out.println("individual value is  -  " +individual);
	    		    FileWriter fw2=new FileWriter(new File(filePath2+"individual.json"));
	    		    fw2.write(individual.toJSONString());
	    		    fw2.flush();
	    		    fw2.close();
	    		     CharlesPing.Stopconnection();
	    		     Thread.sleep(5000);
	    		     downlaodName = CharlesPing.downloadcsv(count,"U1_Arbys_08_LogOut" );

	    	    CharlesPing.finalJson("U1_Arbys");
	    	    JSONObject U1_Sephora=MetricsCollector.stop();
			

			
			JSONObject hits1 = new JSONObject();
			hits1.put("_source", U1_Sephora);
			root.add(hits1);

			String reportJsn = CharlesPing.Json_Usecase();
			JSONObject hits0 = new JSONObject();
			hits0.put("_source", application1);
			root.add(hits0);
			
			JSONArray clickarray = new JSONArray();
			clickarray.add(click);
			JsonfileParsing jsparsing =new JsonfileParsing();
			jsparsing.jsonFileCreation(filePath2, root, clickarray, reportJsn, requestId, apiUrl,filePath1);
			 srdel.upadteSRRequestasCompleted(requestId);
			System.out.println(root.toJSONString());
	    	} catch (InterruptedException | IOException | ParseException e) {
				// TODO Auto-generated catch block
	    		 srdel.upadteSRRequestasError(requestId);
				e.printStackTrace();
			}
	}

		public static boolean checkIfServerIsRunnning(int port) {

	        
	        ServerSocket serverSocket;
	        try {
	            serverSocket = new ServerSocket(port);
	            serverSocket.close();
	        } catch (IOException e) {
	            //If control comes here, then it means that the port is in use
	            isServerRunning = true;
	            System.out.println("ServerRunning");
	        } finally {
	            serverSocket = null;
	            System.out.println("Server not Running");
	        }
	        return isServerRunning;
	    }

	    public static void Appium_start(){
	        try {
	        service=new AppiumServiceBuilder()
	        .usingDriverExecutable(new File(nodeHome))
	        .withAppiumJS(new File(appiumHome))
	        .withIPAddress(host.split("//")[1])
	        .usingPort(port)
	        .build();
	        service.start();
	        }
	        catch (Exception e) {
	            //If control comes here, then it means that the port is in use
	            e.printStackTrace();
	           
	        } 
	    }

	    public static void Appium_stop(){
	        try{
	        service.stop();
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	    }
	    
	    public static void start_charles(){
	        try{
	        System.out.println("Opening Charles");
	        Runtime runTime = Runtime.getRuntime();
	         process = runTime.exec(charlspath);
	         Thread.sleep(15000);
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