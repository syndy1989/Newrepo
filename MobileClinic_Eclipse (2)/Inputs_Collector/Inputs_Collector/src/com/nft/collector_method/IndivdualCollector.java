package com.nft.collector_method;

import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.gson.JsonArray;
import com.nft.parser_method.GpuParser;
import com.nft.parser_method.IndividualMetricParser;
import com.nft.parser_method.MemoryParser;
import com.nft.parser_method.TopParser;

public class IndivdualCollector {

	public static String filePath;
	public static String rootPath;
	public static String gpuFileName;
	public static String cpuFileName;
	public static String memoryFileName;

	static TopParser topParser;
	static MemoryParser memoryParser;
	static Screenshot screen;
	static GpuParser gpuParser;
	static String packageName;
	static String usecaseName;
	static long start;
	static long finish;
	static long totalTime;
	static String responsetime;

	static HashMap<String, Long> response = new HashMap<String, Long>();
	
	public static String androidver;
	public static int cores;
	
	//adding it for android version >6
	public static void setVersion(String androidver,int cores)
	{
		IndivdualCollector.androidver = androidver;
		IndivdualCollector.cores = cores;
	}

	public static void start(String usecaseName, String filePath,
			String packageName) throws InterruptedException {
		IndivdualCollector.usecaseName = usecaseName;
		IndivdualCollector.packageName = packageName;
		IndivdualCollector.filePath = filePath + usecaseName + "\\";
		IndivdualCollector.rootPath = filePath;
		File f = new File(IndivdualCollector.filePath);
		if (f.mkdir()) {

			IndivdualCollector.cpuFileName = usecaseName + "_cpu.txt";
			IndivdualCollector.memoryFileName = usecaseName + "_memory.txt";
			IndivdualCollector.gpuFileName = usecaseName + "_gpu.txt";
			IndivdualCollector.responsetime = usecaseName + "_response.txt";
			gpuParser = new GpuParser(true, IndivdualCollector.filePath,
					usecaseName + "_gpu.txt", packageName);
			topParser = new TopParser(true, IndivdualCollector.filePath,
					usecaseName + "_cpu.txt", packageName,IndivdualCollector.androidver,IndivdualCollector.cores);
			Thread topThread = new Thread(topParser);
			topThread.setName("ClinicTop");

			memoryParser = new MemoryParser(true, IndivdualCollector.filePath,
					usecaseName + "_memory.txt", packageName);
			Thread memoryThread = new Thread(memoryParser);
			memoryThread.setName("ClinicMemory");

			screen = new Screenshot(true, IndivdualCollector.filePath,
					"screenshot");
			/*
			 * Thread screenShotThread = new Thread(screen);
			 * screenShotThread.setName("ClinicScreenshot");
			 */
			topParser.setInfinity(true);
			topThread.start();
			memoryThread.start();
			screen.before(IndivdualCollector.filePath, "screen_before");

		} else {

			System.out.println("cannot make directory so no metrics sorry ");
		}
		start = System.currentTimeMillis();
		
	}

	public static boolean checkVisbility(AndroidDriver<WebElement> driver,
			String element) {

		boolean visibility = (driver.findElementsById(element).size() != 0)
				|| (driver.findElements(By.xpath(element)).size() != 0)
				|| (driver.findElements(By.name(element)).size() != 0);

		return visibility;

	}

	public static boolean checkInVisbility(AndroidDriver<WebElement> driver,
			String element) {

		boolean invisibility = (driver.findElementsById(element).size() == 0)
				|| (driver.findElements(By.name(element)).size() == 0)
				|| (driver.findElements(By.xpath(element)).size() == 0);

		return invisibility;

	}

	public static JSONArray stop(boolean collectIndividual,
			AndroidDriver<WebElement> driver, String element)
			throws InterruptedException {
		// public static JSONArray stop(boolean collectIndividual) {
		// Thread.sleep(5*60*1000);
		int count = 0;
		long timeWaitedChecking=0;
		
		long visbilityStart = System.currentTimeMillis();
		//boolean visibility=checkVisbility(driver, element);
		//boolean invisibility=checkInVisbility(driver, element);
		
	boolean visibility=false;
	boolean invisibility=true;

		long visbilityStop = System.currentTimeMillis();

		if (visibility) {

			System.out.println("Element size is != 0");

			finish = System.currentTimeMillis();

			System.out.println(start + "::" + finish);
			totalTime = (finish - start) - (visbilityStop - visbilityStart);
			System.out.println("totalTime:" + totalTime);
			response.put(IndivdualCollector.usecaseName, totalTime);

			// response.add(totalTime);
			// JSONArraycad individual=IndivdualCollector.stop(true, totalTime,
			// driver, element);
		}
		/*
		 * else { for(int i=0;i<=30;i++){ System.out.println("Loading...");
		 * count++; Thread.sleep(500);
		 * if((driver.findElementsById(element).size()==0)||
		 * (driver.findElements(By.xpath(element)).size()==0) ||
		 * (driver.findElements(By.name(element)).size()==0)){
		 * System.out.println("Still trying to find element"); if(count==2){
		 * System
		 * .out.println("Waited for 15 seconds !!! Unable to find the element");
		 * break;
		 * 
		 * } } break; }
		 * 
		 * if((driver.findElementsById(element).size()!=0)||
		 * (driver.findElements(By.xpath(element)).size()!=0) ||
		 * (driver.findElements(By.name(element)).size()!=0)){ finish =
		 * System.currentTimeMillis(); totalTime = finish - start;
		 * System.out.println("totalTime:" +totalTime);
		 * response.put(IndivdualCollector.usecaseName,totalTime);
		 * 
		 * }
		 */

		else {
			for (;;) {
				
				visbilityStart = System.currentTimeMillis();
				//visibility=checkVisbility(driver, element);
				 //invisibility=checkInVisbility(driver, element);
				visibility=false;
				 invisibility=true;
				 visbilityStop = System.currentTimeMillis();
				 timeWaitedChecking+=(visbilityStop - visbilityStart);
				if (visibility) {
					finish = System.currentTimeMillis();
					totalTime = (finish - start)
							- (timeWaitedChecking);
					System.out.println("totalTime:" + totalTime);
					response.put(IndivdualCollector.usecaseName, totalTime);
					break;

				}

				if (invisibility) {
					System.out.println("Waited for 1 seconds !!!");
					Thread.sleep(500);

				}
				if (count == 20) {
					System.out
							.println("Waited for 15 seconds !!! Unable to find the element");
					break;

				}
				count++;

			}

		}
		// response.put(IndivdualCollector.usecaseName,-17L);
		// System.out.println("unable to get response time");

		// finish = System.currentTimeMillis();
		// totalTime = finish - start;
		// System.out.println("totalTime:" +totalTime);
		// response.put(IndivdualCollector.usecaseName,totalTime);
		// }
		Thread.sleep(5000);
		topParser.setInfinity(false);
		memoryParser.setInfinity(false);
		screen.after(IndivdualCollector.filePath, "screen_after");
		// screen.setInfinity(false);
		gpuParser.dumpOverallGPU();

		start = 0;
		finish = 0;
		/*
		 * if(ResponseTime) {
		 * 
		 * IndividualMetricParser ts=new
		 * IndividualMetricParser(IndivdualCollector.packageName, "10136");
		 * //System
		 * .out.println("individual file path:"+IndivdualCollector.rootPath);
		 * //System.out.println("individualdetails"+ts.collectIndividualMetrics(
		 * IndivdualCollector.rootPath, "dummy").toJSONString()); return
		 * ts.collectIndividualMetrics(IndivdualCollector.rootPath, "dummy",
		 * response);
		 * 
		 * }
		 */

		if (collectIndividual) {
			IndividualMetricParser ts = new IndividualMetricParser(
					IndivdualCollector.packageName, "10136",IndivdualCollector.androidver,IndivdualCollector.cores);
			// System.out.println("individual file path:"+IndivdualCollector.rootPath);
			// ts.collectIndividualMetrics(IndivdualCollector.rootPath, "dummy",
			// response);
			return ts.collectIndividualMetrics(IndivdualCollector.rootPath,
					"dummy", response);

		}

		// IndivdualCollector.startReportWork();
		return null;

		/*
		 * private static JSONObject startReportWork() {
		 * 
		 * JSONObject
		 * cpuJson=topParser.extractCPU(MetricsCollector.filePath+MetricsCollector
		 * .cpuFileName); JSONObject
		 * memoryJson=memoryParser.extractMemory(MetricsCollector
		 * .filePath+MetricsCollector.memoryFileName,false); JSONObject
		 * gpuJson=gpuParser
		 * .extractGPU(MetricsCollector.filePath+MetricsCollector.gpuFileName);
		 * 
		 * System.out.println(cpuJson); System.out.println(memoryJson);
		 * System.out.println(gpuJson); return null;
		 * 
		 * }
		 */
	}

	public static JSONArray stopInApp(boolean collectIndividual,long time)
			throws InterruptedException {
				


		if (collectIndividual) {
			IndividualMetricParser ts = new IndividualMetricParser(
					IndivdualCollector.packageName, "10136",IndivdualCollector.androidver,IndivdualCollector.cores);
			// System.out.println("individual file path:"+IndivdualCollector.rootPath);
			// ts.collectIndividualMetrics(IndivdualCollector.rootPath, "dummy",
			// response);
			return ts.collectIndividualMetrics(IndivdualCollector.rootPath,
					"dummy", response);

		}
		else
		{
		
			finish = System.currentTimeMillis();

		System.out.println(start + "::" + finish);
		totalTime = (finish - start);
		System.out.println("totalTime:" + totalTime);
		response.put(IndivdualCollector.usecaseName, time);

	Thread.sleep(5000);
	topParser.setInfinity(false);
	memoryParser.setInfinity(false);
	screen.after(IndivdualCollector.filePath, "screen_after");
	// screen.setInfinity(false);
	gpuParser.dumpOverallGPU();
		}

		
		return null;

	}
	
	public static JSONArray stopInApp(boolean collectIndividual)
			throws InterruptedException {
				


		if (collectIndividual) {
			IndividualMetricParser ts = new IndividualMetricParser(
					IndivdualCollector.packageName, "10136",IndivdualCollector.androidver,IndivdualCollector.cores);
			// System.out.println("individual file path:"+IndivdualCollector.rootPath);
			// ts.collectIndividualMetrics(IndivdualCollector.rootPath, "dummy",
			// response);
			return ts.collectIndividualMetrics(IndivdualCollector.rootPath,
					"dummy", response);

		}
		else
		{
		
			finish = System.currentTimeMillis();

		System.out.println(start + "::" + finish);
		totalTime = (finish - start);
		System.out.println("totalTime:" + totalTime);
		response.put(IndivdualCollector.usecaseName, totalTime);

	Thread.sleep(5000);
	topParser.setInfinity(false);
	memoryParser.setInfinity(false);
	screen.after(IndivdualCollector.filePath, "screen_after");
	// screen.setInfinity(false);
	gpuParser.dumpOverallGPU();
		}

		
		return null;

	}
}
