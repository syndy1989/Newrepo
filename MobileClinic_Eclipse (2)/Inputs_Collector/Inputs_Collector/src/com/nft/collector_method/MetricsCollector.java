package com.nft.collector_method;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.nft.parser_method.GpuParser;
import com.nft.parser_method.LaunchTimeParser;
import com.nft.parser_method.MemoryParser;
import com.nft.parser_method.TopParser;
import com.nft.parser_method.UIDParser;
import com.nft.parser_method.NetworkParser;


public class MetricsCollector {

	public static String filePath;
	public static String gpuFileName;
	public static String cpuFileName;
	public static String memoryFileName;
	public static String usecaseName;
	public static String packageName;
	public static String activityName;
	static TopParser topParser;
	static MemoryParser memoryParser;
	static Screenshot screen;
	static GpuParser gpuParser;
	public static boolean screenshot;
	static long durationStart=0,durationEnd=0;
	static long calculateresponse=0;
	static UIDParser uidparser;
	static NetworkParser Netparser;
	static NetworkMetrics Netmetrics;
	//static UIDParser Uidparser;
	public static String netFileName;
	public static String uidFileName;
	public static String androidver;
	public static int cores;
	
	//adding it for android version >6
	public static void setVersion(String androidver,int cores)
	{
		MetricsCollector.androidver = androidver;
		MetricsCollector.cores = cores;
	}
	
	public static void start(String usecaseName, String filePath,String packageName,String activityName,boolean screenshot) throws InterruptedException {
		MetricsCollector.filePath=filePath;
		MetricsCollector.usecaseName=usecaseName;
		MetricsCollector.packageName=packageName;
		MetricsCollector.activityName=activityName;
		MetricsCollector.screenshot=screenshot;
		System.out.println(MetricsCollector.filePath);
		MetricsCollector.cpuFileName=usecaseName + "_cpu.txt";
		MetricsCollector.memoryFileName=usecaseName + "_memory.txt";
		MetricsCollector.gpuFileName=usecaseName + "_gpu.txt";
		MetricsCollector.netFileName=usecaseName + "_net.txt";
		MetricsCollector.uidFileName=usecaseName + "_uid.txt";

		gpuParser = new GpuParser(true, filePath, usecaseName + "_gpu.txt", packageName);
		topParser = new TopParser(true, filePath, usecaseName + "_cpu.txt", packageName,MetricsCollector.androidver,MetricsCollector.cores);
		Thread topThread = new Thread(topParser);
		topThread.setName("ClinicTop");
		
		uidparser =new UIDParser(true, filePath, usecaseName + "_uid.txt", packageName);       
		//uidparser.setInfinity(true);
		//Thread	uidthread=new Thread(uidparser);
		//uidthread.start();

		memoryParser = new MemoryParser(true, filePath, usecaseName + "_memory.txt",packageName);
		Thread memoryThread = new Thread(memoryParser);
		memoryThread.setName("ClinicMemory");

		System.out.println("thread config done");
		
		Netmetrics=new NetworkMetrics(true, filePath, usecaseName + "_net.txt", packageName);
		Netmetrics.setInfinity(true);
		Thread	netthread=new Thread(Netmetrics);
		netthread.start();
		
		MetricsCollector.durationStart=System.currentTimeMillis();
		topThread.start();
		memoryThread.start();
		if(MetricsCollector.screenshot)
		{
			screen = new Screenshot(true, filePath, "screenshot");
			Thread screenShotThread = new Thread(screen);
			screenShotThread.setName("ClinicScreenshot");
			screenShotThread.start();
		}
		System.out.println("started");

	}

	public static JSONObject stop() throws IOException, ParseException {
		// Thread.sleep(5*60*1000);

		topParser.setInfinity(false);
		memoryParser.setInfinity(false);
		Netmetrics.setInfinity(false);
		//uidparser.setInfinity(false);
		//dparser.setInfinity(false);
		if(MetricsCollector.screenshot)
			screen.setInfinity(false);

		gpuParser.dumpOverallGPU();
		MetricsCollector.durationEnd=System.currentTimeMillis();
		MetricsCollector.calculateresponse=durationEnd-durationStart;
		return MetricsCollector.startReportWork();
	}
	
	public static JSONObject stopInApp(long totalTime) throws IOException, ParseException {
		// Thread.sleep(5*60*1000);

		topParser.setInfinity(false);
		memoryParser.setInfinity(false);
		Netmetrics.setInfinity(false);
		//uidparser.setInfinity(false);
		//dparser.setInfinity(false);
		if(MetricsCollector.screenshot)
			screen.setInfinity(false);

		gpuParser.dumpOverallGPU();
		MetricsCollector.durationEnd=System.currentTimeMillis();
		MetricsCollector.calculateresponse=totalTime;
		return MetricsCollector.startReportWorkforapp();
	}

	private static JSONObject startReportWork() throws IOException, ParseException {
		
		
		

		JSONObject cpuJson=topParser.extractCPU(MetricsCollector.filePath+MetricsCollector.cpuFileName);
		JSONObject memoryJson=memoryParser.extractMemory(MetricsCollector.filePath+MetricsCollector.memoryFileName,false);
		JSONObject gpuJson=gpuParser.extractGPU(MetricsCollector.filePath+MetricsCollector.gpuFileName);
		uidparser.dumpPackageDetails(MetricsCollector.packageName);
		String uid=uidparser.extractuid(MetricsCollector.packageName,MetricsCollector.usecaseName);
		System.out.println("uid"+uid);
		JSONObject netJson=Netparser.parseNetworkToJSON(MetricsCollector.filePath+MetricsCollector.netFileName,uid);
		System.out.println(netJson);
		/*System.out.println(cpuJson);
		System.out.println(memoryJson);
		System.out.println(gpuJson);*/

		JSONObject useCasedetail=new JSONObject();

		JSONObject useCaseDetails=new JSONObject();

		JSONObject appPhone=new JSONObject();

		JSONObject app=new JSONObject();

		JSONObject phone=new JSONObject();

		useCaseDetails.put("totalduration", (MetricsCollector.durationEnd-MetricsCollector.durationStart));
		useCaseDetails.put("cpudetails", cpuJson.get("cpudetails"));
		useCaseDetails.put("maxcpu", cpuJson.get("maxcpu"));
		useCaseDetails.put("averagecpu", cpuJson.get("averagecpu"));

		//commented as we don't need thread details any longer
		
		//useCaseDetails.put("threaddetails", cpuJson.get("threaddetails"));
		//useCaseDetails.put("maxthread", cpuJson.get("maxthread"));
		//useCaseDetails.put("averagethread", cpuJson.get("averagethread"));

		useCaseDetails.put("memorydetails", memoryJson.get("memorydetails"));
		useCaseDetails.put("maxmemory", memoryJson.get("maxmemory"));
		useCaseDetails.put("averagememory", memoryJson.get("averagememory"));
		useCaseDetails.put("TotalTransfered", netJson.get("TotalTransfered"));
		useCaseDetails.put("networkdetails", netJson.get("networkdetails"));
		useCaseDetails.put("TotalReceived", netJson.get("TotalReceived"));
		useCasedetail.put("usecasename", MetricsCollector.usecaseName);
		


		phone.put("[gsm$sim$operator$alpha]", "[]");
		phone.put("[persist$sys$timezone]", "[Asia/Calcutta]");
		phone.put("[dalvik$vm$heapminfree]", "[512k]");
		phone.put("[dalvik$vm$heapstartsize]", "[8m]");
		phone.put("[gsm$operator$iso-country]", "[]");
		phone.put("[ro$product$model]", "[Nexus 6P]");
		phone.put("[dalvik$vm$heapgrowthlimit]", "[192m]");
		phone.put("[ro$build$version$release]", "[6.0.1]");
		phone.put("[ro$build$version$sdk]", "[23]");
		phone.put("[dalvik$vm$heapmaxfree]", "[8m]");
		phone.put("[dalvik$vm$heapsize]", "[512m]");

		appPhone.put("app", app);
		appPhone.put("phone", phone);

		JSONObject pageWiseDetails=new JSONObject();
		if(MetricsCollector.screenshot)
			pageWiseDetails.put("launchtime", LaunchTimeParser.getLaunchTime(MetricsCollector.packageName,MetricsCollector.activityName));
		
		else
			pageWiseDetails.put("launchtime", "0");
		
		pageWiseDetails.put("page1", 0);
		pageWiseDetails.put("page2", 0);
		pageWiseDetails.put("page3", 0);

		useCaseDetails.put("pagewisedetails", pageWiseDetails);
		useCaseDetails.put("appPhone", appPhone);


		useCasedetail.put("usecasedetails", useCaseDetails);


		JSONObject runDetails=new JSONObject();
		runDetails.put("deviceType", "Android");
		runDetails.put("appVersion", "1.1");
		runDetails.put("deviceOS", "6.0");
		runDetails.put("runid", "1");
		runDetails.put("deviceName", "HTC"); 
		runDetails.put("usecasedetail", useCasedetail); 
		runDetails.put("noofusecases", 1); 

		JSONObject runs=new JSONObject();
		runs.put("rundetails", runDetails);

		JSONObject application=new JSONObject();
		application.put("runs", runs);
		application.put("applicationName", MetricsCollector.packageName);

		JSONObject _source=new JSONObject();
		_source.put("Application", application);



		return _source;

	}

private static JSONObject startReportWorkforapp() throws IOException, ParseException {
		
		
		

		JSONObject cpuJson=topParser.extractCPU(MetricsCollector.filePath+MetricsCollector.cpuFileName);
		JSONObject memoryJson=memoryParser.extractMemory(MetricsCollector.filePath+MetricsCollector.memoryFileName,false);
		JSONObject gpuJson=gpuParser.extractGPU(MetricsCollector.filePath+MetricsCollector.gpuFileName);
		uidparser.dumpPackageDetails(MetricsCollector.packageName);
		String uid=uidparser.extractuid(MetricsCollector.packageName,MetricsCollector.usecaseName);
		System.out.println("uid"+uid);
		JSONObject netJson=Netparser.parseNetworkToJSON(MetricsCollector.filePath+MetricsCollector.netFileName,uid);
		System.out.println(netJson);
		/*System.out.println(cpuJson);
		System.out.println(memoryJson);
		System.out.println(gpuJson);*/

		JSONObject useCasedetail=new JSONObject();

		JSONObject useCaseDetails=new JSONObject();

		JSONObject appPhone=new JSONObject();

		JSONObject app=new JSONObject();

		JSONObject phone=new JSONObject();

		useCaseDetails.put("totalduration", (MetricsCollector.durationEnd-MetricsCollector.durationStart));
		useCaseDetails.put("cpudetails", cpuJson.get("cpudetails"));
		useCaseDetails.put("maxcpu", cpuJson.get("maxcpu"));
		useCaseDetails.put("averagecpu", cpuJson.get("averagecpu"));

		useCaseDetails.put("threaddetails", cpuJson.get("threaddetails"));
		useCaseDetails.put("maxthread", cpuJson.get("maxthread"));
		useCaseDetails.put("averagethread", cpuJson.get("averagethread"));

		useCaseDetails.put("memorydetails", memoryJson.get("memorydetails"));
		useCaseDetails.put("maxmemory", memoryJson.get("maxmemory"));
		useCaseDetails.put("averagememory", memoryJson.get("averagememory"));
		useCaseDetails.put("TotalTransfered", netJson.get("TotalTransfered"));
		useCaseDetails.put("networkdetails", netJson.get("networkdetails"));
		useCaseDetails.put("TotalReceived", netJson.get("TotalReceived"));
		useCasedetail.put("usecasename", MetricsCollector.usecaseName);
		


		phone.put("[gsm$sim$operator$alpha]", "[]");
		phone.put("[persist$sys$timezone]", "[Asia/Calcutta]");
		phone.put("[dalvik$vm$heapminfree]", "[512k]");
		phone.put("[dalvik$vm$heapstartsize]", "[8m]");
		phone.put("[gsm$operator$iso-country]", "[]");
		phone.put("[ro$product$model]", "[Nexus 6P]");
		phone.put("[dalvik$vm$heapgrowthlimit]", "[192m]");
		phone.put("[ro$build$version$release]", "[6.0.1]");
		phone.put("[ro$build$version$sdk]", "[23]");
		phone.put("[dalvik$vm$heapmaxfree]", "[8m]");
		phone.put("[dalvik$vm$heapsize]", "[512m]");

		appPhone.put("app", app);
		appPhone.put("phone", phone);

		JSONObject pageWiseDetails=new JSONObject();
		if(MetricsCollector.screenshot)
			pageWiseDetails.put("launchtime", LaunchTimeParser.getLaunchTime(MetricsCollector.packageName,MetricsCollector.activityName));
		
		else
			pageWiseDetails.put("launchtime", Long.toString(MetricsCollector.calculateresponse));
		
		pageWiseDetails.put("page1", 0);
		pageWiseDetails.put("page2", 0);
		pageWiseDetails.put("page3", 0);

		useCaseDetails.put("pagewisedetails", pageWiseDetails);
		useCaseDetails.put("appPhone", appPhone);


		useCasedetail.put("usecasedetails", useCaseDetails);


		JSONObject runDetails=new JSONObject();
		runDetails.put("deviceType", "Android");
		runDetails.put("appVersion", "1.1");
		runDetails.put("deviceOS", "6.0");
		runDetails.put("runid", "1");
		runDetails.put("deviceName", "HTC"); 
		runDetails.put("usecasedetail", useCasedetail); 
		runDetails.put("noofusecases", 1); 

		JSONObject runs=new JSONObject();
		runs.put("rundetails", runDetails);

		JSONObject application=new JSONObject();
		application.put("runs", runs);
		application.put("applicationName", MetricsCollector.packageName);

		JSONObject _source=new JSONObject();
		_source.put("Application", application);



		return _source;

	}
}
