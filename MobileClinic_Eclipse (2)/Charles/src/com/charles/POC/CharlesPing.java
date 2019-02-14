package com.charles.POC;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.immanuel.entrypoint.EntryPoint;
import com.immanuel.entrypoint.PropertyLoader;

public class CharlesPing {
	/*
	 * public static void main(String[] args) throws IOException{
	 * Startconnection(); Stopconnection(); String filaname=downloadcsv();
	 * Csvconversion csv=new Csvconversion(); csv.jsonconversion(filaname);
	 * 
	 * 
	 * 
	 * 
	 * }
	 */
	static Process process = null;
	static NetworkRecommend netRec = new NetworkRecommend();
	private static final Logger LOGGER = Logger.getLogger(CharlesPing.class);
	static String startTime;
	static String endTime;
	public static String charlesPath;
	public static String charlesappPath;

	public static void startCharles() {
		try {
			System.out.println("Opening Charles");
			LOGGER.info("Opening Charles");
			LOGGER.debug("Opening Charles");
			Runtime runTime = Runtime.getRuntime();
			process = runTime.exec("\"" + PropertyLoader.getInstance().charleslocation + "\"");

		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("error", e);

		}
	}

	public static void stopCharles() {
		System.out.println("Closing Charles");
		LOGGER.info("Closing Charles");
		LOGGER.debug("Closing Charles");

		process.destroy();
	}

	public static void Startconnection() throws IOException {
		URL url = new URL("http://control.charles/recording/start");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setFollowRedirects(true);

		System.getProperties().put("http.proxyHost", "127.0.0.1");
		System.getProperties().put("http.proxyPort", "8888");
		System.setProperty("http.proxyUser", "admin");
		System.setProperty("http.proxyPassword", "admin");
		System.getProperties().put("http.proxySet", "true");
		System.out.println(connection.getResponseCode());
		LOGGER.info(connection.getResponseCode());
		LOGGER.debug(connection.getResponseCode());

		if (connection.getResponseCode() == 200) {
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
			String line8;
			String message8 = new String();
			final StringBuffer buffer = new StringBuffer(2048);
			while ((line8 = br.readLine()) != null) {
				// buffer.append(line);
				message8 += line8;
			}
		} else {
			connection.disconnect();
		}

	}
	
	public static void ClearCharles() throws IOException {
		URL url = new URL("http://control.charles/session/clear");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setFollowRedirects(true);

		System.getProperties().put("http.proxyHost", "127.0.0.1");
		System.getProperties().put("http.proxyPort", "8888");
		System.setProperty("http.proxyUser", "admin");
		System.setProperty("http.proxyPassword", "admin");
		System.getProperties().put("http.proxySet", "true");
		System.out.println(connection.getResponseCode());
		LOGGER.info(connection.getResponseCode());
		LOGGER.debug(connection.getResponseCode());

		if (connection.getResponseCode() == 200) {
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
			String line8;
			String message8 = new String();
			final StringBuffer buffer = new StringBuffer(2048);
			while ((line8 = br.readLine()) != null) {
				// buffer.append(line);
				message8 += line8;
			}
		} else {
			connection.disconnect();
		}

	}


	public static void Stopconnection() throws IOException {
		URL url = new URL("http://control.charles/recording/stop");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setFollowRedirects(true);

		System.getProperties().put("http.proxyHost", "127.0.0.1");
		System.getProperties().put("http.proxyPort", "8888");
		System.setProperty("http.proxyUser", "admin");
		System.setProperty("http.proxyPassword", "admin");
		System.getProperties().put("http.proxySet", "true");
		System.out.println(connection.getResponseCode());
		LOGGER.info(connection.getResponseCode());
		LOGGER.debug(connection.getResponseCode());

		if (connection.getResponseCode() == 200) {
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
			String line8;
			String message8 = new String();
			final StringBuffer buffer = new StringBuffer(2048);
			while ((line8 = br.readLine()) != null) {
				// buffer.append(line);
				message8 += line8;
			}
		} else {
			connection.disconnect();
		}
		System.out.println("Downloading Charles Json");
		LOGGER.info("Downloading Charles Json");
		LOGGER.debug("Downloading Charles Json");

		//String fileName = CharlesPing.downloadcsv();
		//CharlesPing.renameFile();
		// System.out.println(fileName);

	}

	/*private static void renameFile() {
		File file = new File("D:\\Charles_Proxy\\Result");
		File[] list = file.listFiles();

		for (File f : list) {
			if (f.getName().startsWith("charles") && f.getName().endsWith(".csv")) {

				String name = f.getName();
				System.out.println(name);
				LOGGER.info(name);
				LOGGER.debug(name);

				String newName = "testnet" + "" + ".json";
				String newPath = file + "\\" + newName;
				f.renameTo(new File(newPath));
				System.out.println(name + " changed to " + newName);
				LOGGER.info(name + " changed to " + newName);
				LOGGER.debug(name + " changed to " + newName);

			}
		}

	}*/

	public static String downloadcsv(int Count, String list) throws IOException {
		
		URL url = new URL("http://control.charles/session/export-json");
		URL url2 = new URL("http://control.charles/session/export-har");
		String saveFilePath = null;
		String saveFilePath2 = null;
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");

		connection.setFollowRedirects(true);
		
		HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
		connection2.setDoInput(true);
		connection2.setDoOutput(true);
		connection2.setRequestMethod("POST");
		
		
		System.getProperties().put("http.proxyHost", "127.0.0.1");
		System.getProperties().put("http.proxyPort", "8888");
		System.setProperty("http.proxyUser", "admin");
		System.setProperty("http.proxyPassword", "admin");
		System.getProperties().put("http.proxySet", "true");
		System.out.println(connection.getContent());
		LOGGER.info(connection.getContent());
		LOGGER.debug(connection.getContent());
		
		System.out.println(connection2.getContent());
		LOGGER.info(connection2.getContent());
		LOGGER.debug(connection2.getContent());

		String lastSlash = null;
		String harname = null;
		
		if (connection.getResponseCode() == 200) {
			String message8 = connection.getHeaderField("Content-Disposition");
			lastSlash = message8.substring(21).replace(".chlsj","");
			System.out.println(lastSlash);
			
			//String jsonnamesplit=lastSlash.substring(0, 19);
			String jsonfile = lastSlash.replaceAll(lastSlash,list);
			LOGGER.info(jsonfile);
			LOGGER.debug(jsonfile);
			
			
			String message9 = connection2.getHeaderField("Content-Disposition");
			String[] harsplit =message9.split("=");
			harname = harsplit[1];
			String harnamesplit=harname.substring(0, 19);
			String harfile = harname.replaceAll(harnamesplit,list);
			LOGGER.info(harfile);
			LOGGER.debug(harfile);

			InputStream inputStream = connection.getInputStream();
			InputStream inputStream2 = connection2.getInputStream();
			//charlesPath
			//File f = new File("C:\\Charles\\Mobileclinique\\NFTMobile\\dump\\");
			File f = new File(charlesPath+"\\dump\\");

			if (!f.exists()) {
				f.mkdirs();

			} 
			
			//charlesPath+"
			//saveFilePath = "C:\\Charles\\Mobileclinique\\NFTMobile\\dump\\" + jsonfile+".json";
			saveFilePath = charlesPath+"\\dump\\" + jsonfile+".json";
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);
			System.out.println("Harfile-------------------------");
			//saveFilePath2 = "C:\\Charles\\Mobileclinique\\NFTMobile\\dump\\" + harfile;
			saveFilePath2 = charlesPath+"\\dump\\" + harfile;
			FileOutputStream outputStream2 = new FileOutputStream(saveFilePath2);
			int bytesRead = -1;
			byte[] buffer1 = new byte[4096];
			while ((bytesRead = inputStream.read(buffer1)) != -1) {
				outputStream.write(buffer1, 0, bytesRead);
			}
			System.out.println("Harfile1111111-------------------------");
			outputStream.close();
			inputStream.close();
			
			int bytesRead2 = -1;
			byte[] buffer2 = new byte[4096];
			while ((bytesRead2 = inputStream2.read(buffer2)) != -1) {
				outputStream2.write(buffer2, 0, bytesRead2);
			}
			System.out.println("Harfile222222-------------------------");
			outputStream2.close();
			inputStream2.close();
			
			
			JSONArray getRecommend = netRec.NetRecommend(saveFilePath2);
			JSONObject Recommendation = new JSONObject();
			JSONObject NetworkDetails = new JSONObject();
			JSONArray lastary = new JSONArray();
			JSONObject finaljson = new JSONObject();
			System.out.println("Harfile333333-------------------------");
			JSONParser parser = new JSONParser();
	        try
	        {
	        	JSONArray finalary = new JSONArray();
	        	JSONObject json = new JSONObject();
	        	
	        	
	        	Object object = parser.parse(new FileReader(saveFilePath));
	        	
	            //convert Object to JSONObject
	        	JSONArray jsonary = (JSONArray)object;
	            JSONObject jsonObject = new JSONObject();
	            
	            jsonObject.put("Sample", jsonary);
	        	
	            //Reading the String
	            
	            
	            JSONArray jsonMainArr = null;
	            JSONObject jsonobject1 = jsonObject;
	            //Reading the array
	            jsonMainArr = (JSONArray) jsonobject1.get("Sample");
	            
	            //For loop
	            
	            for (int i = 0; i < jsonMainArr.size(); i++) {
	            	JSONObject resultobj = new JSONObject();
	            JSONObject newobj = (JSONObject) jsonMainArr.get(i);
	            String status = (String) newobj.get("status");
	            String host = (String) newobj.get("host");
	            JSONObject jsonResp = (JSONObject) newobj.get("response");
	            Long statusCode = (Long) jsonResp.get("status");
	            
	            JSONObject jsonrespSize = (JSONObject) jsonResp.get("sizes");
	            Long headerSize = (Long) jsonrespSize.get("headers");
	            Long bodySize = (Long) jsonrespSize.get("body");
	            Long respSize = headerSize + bodySize;
	            
	            JSONObject jsonReq = (JSONObject) newobj.get("request");
	            JSONObject jsonreqSize = (JSONObject) jsonReq.get("sizes");
	            Long reqheaderSize = (Long) jsonreqSize.get("headers");
	            Long reqbodySize = (Long) jsonreqSize.get("body");
	            Long reqSize = reqheaderSize + reqbodySize;
	            JSONObject jsonPage = (JSONObject) jsonReq.get("header");
	            String reqPage = (String) jsonPage.get("firstLine");
	            
	            JSONObject jsonlatency = (JSONObject) newobj.get("durations");
	            Long latency = (Long) jsonlatency.get("latency");
	            
	            
	            JSONObject time = (JSONObject) newobj.get("times");
	           /* JSONArray time = (JSONArray) newobj.get("times");
	            JSONArray duration = (JSONArray) newobj.get("durations");*/
	            
	             startTime =  (String) time.get("start");
	             endTime =  (String) time.get("end");
	            
	            if(startTime !=null && endTime !=null)
	            {
	            String[] stimeTSplit = startTime.split("T");
	            String[] etimeTSplit = endTime.split("T");
	            
	            String[] stimeSplit = stimeTSplit[1].split("\\+");
	            String[] etimeSplit = etimeTSplit[1].split("\\+");
	            
	            String finalStarttime = stimeSplit[0];
	            String finalEndtime = etimeSplit[0];
	            
	            String[] splitStartms = finalStarttime.split("\\.");
	            String[] splitEndms = finalEndtime.split("\\.");
	            
	            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	            Date time1 = format.parse(splitStartms[0]);
	            Date time2 = format.parse(splitEndms[0]);
	            long difference = time2.getTime() - time1.getTime(); 
	            
	            long timeDiff = difference - (Long.parseLong(splitStartms[1]) - Long.parseLong(splitEndms[1]));
	            
	            resultobj.put("ResponseTime", timeDiff);
	            }
	            else
	            {
	            	resultobj.put("ResponseTime", null);
	            }
	            
	            System.out.println("output = "+ status);
	            System.out.println("host = "+ host);
	            System.out.println("statusCode = "+ statusCode);
	            System.out.println("Request Size = "+ reqSize);
	            System.out.println("Response Size = "+ respSize);
	            System.out.println("latency = "+ latency);
	            System.out.println("Page = "+ reqPage);
	            System.out.println("latency = "+ time);
	            System.out.println("Page = "+ jsonlatency);
	            
	            
	            resultobj.put("Status", status);
	            resultobj.put("Domain", host);
	            resultobj.put("STATUS", statusCode);
	            resultobj.put("RequestSize", reqSize);
	            resultobj.put("ResponseSize", respSize);
	            resultobj.put("Latency", latency);
	            
	            resultobj.put("RequestedPage", reqPage);
	            resultobj.put("Time", time);
	            resultobj.put("Duration", jsonlatency);
	            
	            
	            
	            
	          
	            
	            
	            finalary.add(resultobj);
	            System.out.println(finalary);
	            		}
	           
	            json.put("NetworkDetails", finalary);
	            json.put("Recommendation", getRecommend);
	            //lastary.add(json);
	            
	            
	            finaljson.put(list, json);
	            System.out.println("Json" + finaljson.toString());
	           //charlesPath+"
	            
	            //File f2 = new File("C:\\Charles\\Mobileclinique\\NFTMobile\\JSON_files\\");
	            File f2 = new File(charlesPath+"\\JSON_files\\");

				if (!f2.exists()) {
					f2.mkdirs();

				} 
	            
	            //try (FileWriter file = new FileWriter("C:\\Charles\\Mobileclinique\\NFTMobile\\JSON_files\\"+list+".json")) {
				try (FileWriter file = new FileWriter(charlesPath+"\\JSON_files\\"+list+".json")) {
	    			file.write(finaljson.toJSONString());
	    			System.out.println("Successfully Copied JSON Object to File...");
	    			System.out.println("\nJSON Object: " + finaljson);
	    		}
	            
	        	
	            //Printing all the values
	           /* System.out.println("Name: " + name);
	            System.out.println("Age: " + age);
	            System.out.println("Countries:");
	            for(Object country : countries)
	            {
	                System.out.println("\t"+country.toString());
	            }*/
	        	
	        }
	        catch(Exception fe)
	        {
	            fe.printStackTrace();
	        }
	        
			

		} else {
			connection.disconnect();
		}
		return lastSlash;

	}
	
	public static void finalJson(String UsecaseName)
	{
		
		JSONObject jobj = new JSONObject();
		JSONArray finalary = new JSONArray();
		//File file = new File("C:\\Charles\\Mobileclinique\\NFTMobile\\JSON_files\\");
		File file = new File(charlesPath+"\\JSON_files\\");
        File[] files = file.listFiles();
        for(File f: files){
            System.out.println(f.getName());
            
            
            BufferedReader br = null;
            FileReader fr = null;
    		try {
    			//fr = new FileReader(new File("C:\\Charles\\Mobileclinique\\NFTMobile\\JSON_files\\"+f.getName()));
    			fr = new FileReader(new File(charlesPath+"\\JSON_files\\"+f.getName()));
    		
    		} catch (FileNotFoundException e3) {
    			// TODO Auto-generated catch block
    			e3.printStackTrace();
    			//LOGGER.error("error", e3);
    		}
    		br = new BufferedReader(fr);

    		String current = "";
    		String json = "";

    		try {
    			while ((current = br.readLine()) != null) {
    				json = json + current;
    				// continue;
    			}
    		} catch (IOException e2) {
    			// TODO Auto-generated catch block
    			e2.printStackTrace();
    			//LOGGER.error("error", e2);
    		}
    		JSONParser jp = new JSONParser();
    		JSONObject jObj = null;
    		try {
    			jObj = (JSONObject) jp.parse(json);
    			System.out.println("JSON : " + jObj);
    			
    			finalary.add(jObj);
    			br.close();
    			fr.close();
    			
    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			//LOGGER.error("error", e);
    		}            
        }	
        
        jobj.put(UsecaseName, finalary);
        System.out.println("Final : " + jobj);
        //File f3 = new File("C:\\Charles\\Mobileclinique\\NFTMobile\\final_JSONfile\\");
        File f3 = new File(charlesPath+"\\final_JSONfile\\");

		if (!f3.exists()) {
			f3.mkdirs();

		} 
        //try (FileWriter finalfile = new FileWriter("C:\\Charles\\Mobileclinique\\NFTMobile\\final_JSONfile\\"+UsecaseName+".json")) {
		try (FileWriter finalfile = new FileWriter(charlesPath+"\\final_JSONfile\\"+UsecaseName+".json")) {
        	finalfile.write(jobj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + jobj);
			finalfile.close();
			
		}
        catch (Exception e) {
			e.printStackTrace();
		}
        for(File f: files){
            f.delete();
        }
	//return temp = 0;
	}
	
	public static String Json_Usecase()
	{
		
		JSONObject jobj = new JSONObject();
		JSONArray finalary = new JSONArray();;
		//File file = new File("C:\\Charles\\Mobileclinique\\NFTMobile\\final_JSONfile\\");
		File file = new File(charlesPath+"\\final_JSONfile\\");
        File[] files = file.listFiles();
        for(File f: files){
            System.out.println(f.getName());
            
            
            BufferedReader br = null;
            FileReader fr = null;
    		try {
    			//fr = new FileReader(new File("C:\\Charles\\Mobileclinique\\NFTMobile\\final_JSONfile\\"+f.getName()));
    			fr = new FileReader(new File(charlesPath+"\\final_JSONfile\\"+f.getName()));
    		
    		} catch (FileNotFoundException e3) {
    			// TODO Auto-generated catch block
    			e3.printStackTrace();
    			//LOGGER.error("error", e3);
    		}
    		br = new BufferedReader(fr);

    		String current = "";
    		String json = "";

    		try {
    			while ((current = br.readLine()) != null) {
    				json = json + current;
    				// continue;
    			}
    		} catch (IOException e2) {
    			// TODO Auto-generated catch block
    			e2.printStackTrace();
    			//LOGGER.error("error", e2);
    		}
    		JSONParser jp = new JSONParser();
    		JSONObject jObj = null;
    		try {
    			jObj = (JSONObject) jp.parse(json);
    			System.out.println("JSON : " + jObj);
    			
    			finalary.add(jObj);
    			br.close();
    			fr.close();
    			
    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			//LOGGER.error("error", e);
    		}            
        }	
        for(File f: files){
           f.delete();
        }
        return finalary.toJSONString();
        /*jobj.put("Result", finalary);
        JSONArray jArray = new JSONArray();
        jArray.add(jobj);
        System.out.println("Final : " + jobj);*/
        /*try (FileWriter finalfile = new FileWriter("C:\\Charles\\Mobileclinique\\NFTMobile\\Result.json")) {
        	finalfile.write(finalary.toJSONString());
			System.out.println("Successfully Copied Combined JSON Object to File...");
			System.out.println("\nJSON Object: " + finalary);
			finalfile.close();
			
		}
        catch (Exception e) {
			e.printStackTrace();
		}*/
        
	
	}
	
}
