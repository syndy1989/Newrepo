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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.JsonArray;
import com.nft.parser_method.GpuParser;
import com.nft.parser_method.IndividualMetricParser;
import com.nft.parser_method.MemoryParser;
import com.nft.parser_method.TopParser;

public class Collector {

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

	static HashMap<String,Long> response = new HashMap<String, Long>();

	public static void start(String usecaseName, String filePath,
			String packageName) throws InterruptedException {
		Collector.usecaseName = usecaseName;
		Collector.packageName = packageName;
		Collector.filePath = filePath + usecaseName + "\\";
		Collector.rootPath = filePath;
		File f = new File(Collector.filePath);
		if (f.mkdir()) {

			Collector.cpuFileName = usecaseName + "_cpu.txt";
			Collector.memoryFileName = usecaseName + "_memory.txt";
			Collector.gpuFileName = usecaseName + "_gpu.txt";
			Collector.responsetime = usecaseName + "_response.txt";
			gpuParser = new GpuParser(true, Collector.filePath,
					usecaseName + "_gpu.txt", packageName);
			topParser = new TopParser(true, Collector.filePath,
					usecaseName + "_cpu.txt", packageName);
			Thread topThread = new Thread(topParser);
			topThread.setName("ClinicTop");

			memoryParser = new MemoryParser(true, Collector.filePath,
					usecaseName + "_memory.txt", packageName);
			Thread memoryThread = new Thread(memoryParser);
			memoryThread.setName("ClinicMemory");

			screen = new Screenshot(true, Collector.filePath,
					"screenshot");
			/*
			 * Thread screenShotThread = new Thread(screen);
			 * screenShotThread.setName("ClinicScreenshot");
			 */

			topThread.start();
			memoryThread.start();
			screen.before(Collector.filePath, "screen_before");

		} else {

			System.out.println("cannot make directory so no metrics sorry ");
		}

		start = System.currentTimeMillis();
	}

	public static JSONArray stop(boolean collectIndividual,
			AndroidDriver<WebElement> driver, String element)
					throws InterruptedException {
		// public static JSONArray stop(boolean collectIndividual) {
		// Thread.sleep(5*60*1000);
		int count = 0;
		 
		
		WebDriverWait wait = new WebDriverWait(driver,20);
	      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//XCUIElementTypeStaticText[@name=\"Cancel\"]")));

		if ((driver.findElementsById(element).size() != 0)
				|| (driver.findElements(By.xpath(element)).size() != 0)
				|| (driver.findElements(By.name(element)).size() != 0)) {

			System.out.println("Element size is != 0");
			finish = System.currentTimeMillis();
			totalTime = finish - start;
			System.out.println("totalTime:" + totalTime);
			response.put(Collector.usecaseName, totalTime);

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
		
				if ((driver.findElementsById(element).size() != 0)
						|| (driver.findElements(By.xpath(element)).size() != 0)
						|| (driver.findElements(By.name(element)).size() != 0)) {
					finish = System.currentTimeMillis();
					totalTime = finish - start;
					System.out.println("totalTime:" + totalTime);
					response.put(Collector.usecaseName, totalTime);
					break;

				}
				
				if ((driver.findElementsById(element).size() == 0)
						|| (driver.findElements(By.name(element)).size() == 0)
						|| (driver.findElements(By.xpath(element)).size() == 0)) {
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

		topParser.setInfinity(false);
		memoryParser.setInfinity(false);
		screen.after(Collector.filePath, "screen_after");
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
					Collector.packageName, "10136");
			// System.out.println("individual file path:"+IndivdualCollector.rootPath);
			// ts.collectIndividualMetrics(IndivdualCollector.rootPath, "dummy",
			// response);
			return ts.collectIndividualMetrics(Collector.rootPath,
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
}
