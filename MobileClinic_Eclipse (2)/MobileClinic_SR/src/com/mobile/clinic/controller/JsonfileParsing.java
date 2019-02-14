package com.mobile.clinic.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JsonfileParsing {

	SRDetails srdel=new SRDetails();
	
	public void jsonFileCreation(String filePath2,JSONArray root,JSONArray clickarray,String reportJsn, String RequestId,String apiUrl, String filePath1)
	{
		FileWriter fw;
		try {
			fw = new FileWriter(new File(filePath2 + "mobile.json"));
	
		fw.write(root.toJSONString());
		fw.flush();
		fw.close();

		FileWriter fw3 = new FileWriter(new File(filePath2 + "click.json"));
		fw3.write(clickarray.toJSONString());
		fw3.flush();
		fw3.close();

		FileWriter finalfile = new FileWriter(new File(filePath2 + "Result.json"));
		finalfile.write(reportJsn);
		finalfile.flush();
		finalfile.close();


		  FileReader reader = new FileReader(filePath2 + "mobile.json");
        FileReader reader1 = new FileReader(filePath2 + "appsize.json");
        FileReader reader2 = new FileReader(filePath2 + "individual.json");
        FileReader reader3 = new FileReader(filePath2 + "Result.json");
        
        JSONParser parser = new JSONParser();
        // JSONParser parser1=new JSONParser();
        Object object = parser.parse(reader);
        Object object1 = parser.parse(reader1);
        Object object2 = parser.parse(reader2);
        Object object3 = parser.parse(reader3);
        // System.out.println(object2);

        JSONArray main = new JSONArray();
        main = (JSONArray) object;
        JSONObject appsize = new JSONObject();
        JSONArray respOBJ = new JSONArray();
        respOBJ = (JSONArray) object2;
        // System.out.println(respOBJ.size());
        appsize = (JSONObject) object1;
        JSONArray ResultJson = new JSONArray();
        ResultJson = (JSONArray) object3;
        
        
        int avgcpusum = 0, maxcpusum = 0, maxtrdsum = 0, avgtrdsum = 0, length = 0, launchtime = 0;
        long maxmemsum = 0, avgmemsum = 0;
        String packagename = "", deviceos = "", osversion = "";

        JSONObject launchtimeobj = new JSONObject();
        JSONObject jsonindividual = new JSONObject();
        JSONObject jsonResult = new JSONObject();

        JSONObject MainOBJECT = new JSONObject();
        JSONArray usecasedetails = new JSONArray();
        JSONObject phonedetails = new JSONObject();
        JSONObject hits = new JSONObject();
        
        int l=0;
        for(int i=1;i<ResultJson.size();i++)
        {
      	  
      	  JSONObject resPages = (JSONObject) ResultJson.get(i); 
      //	  ResUseCase = (JSONObject) ResultJson.get(i);
      //	  System.out.println("ResUseCase length - "+"  "+resPages.size()+"  , "+resPages.toString());
      	  ArrayList<String> al = new ArrayList<String>();
      	  al.addAll(resPages.keySet());
      	  JSONArray resPageClicks = (JSONArray) resPages.get(al.get(0));
     // 	  System.out.println("jsonarr_1 length - "+resPageClicks.size()+"  "+al.get(0));
      	  
      	  for(int j=0;j<resPageClicks.size();j++)
      	  {
      		  JSONArray activityArray = new JSONArray();
      		  JSONObject Responses = new JSONObject();
          	  
      		  JSONObject eachPageClick = (JSONObject) resPageClicks.get(j); 
      		  ArrayList<String> al1 = new ArrayList<String>();
          	  al1.addAll(eachPageClick.keySet());
      	//	  System.out.println("al1 length and value    - " + al1.size()+"  ,  "+ al1);
      		  JSONObject pageClick = (JSONObject) eachPageClick.get(al1.get(0));
      		 
      		  JSONArray networkDetails = (JSONArray) pageClick.get("NetworkDetails");
      		  
      		  long pageSize=0;
      		  for(int k=0;k<networkDetails.size();k++)
      		  {
      			  JSONObject responseSize = (JSONObject) networkDetails.get(k);
      			  pageSize = pageSize + (long) responseSize.get("ResponseSize");
      		  }
      //		  System.out.println("jsonarr_1 length - "+pageSize+"    "+networkDetails.size());
      		  Responses.put("activity", al1.get(0));
      		  Responses.put("No_Of_Networkcall", networkDetails.size());
      		  Responses.put("PageSize", pageSize);
      		  activityArray.add(Responses);
                // activityArray.add(Activities);
      		  jsonResult.put("Activity" + l, activityArray);
      		  l++;
     // 		  System.out.println("jsonResult inside loop  -  "+jsonResult.toString());
      		  
      	  }
      	  
      //	  System.out.println("jsonResult   -  "+jsonResult.toString());
      	  
        }
        
        

        for (int i = 0; i < respOBJ.size(); i++) {
                        JSONArray activityArray = new JSONArray();
                        JSONObject Activities = new JSONObject();
                        JSONObject Responses = new JSONObject();
                        JSONObject response = new JSONObject();
                    
                        response = (JSONObject) respOBJ.get(i);
                        JSONArray activitymetrics = (JSONArray) response.get("activitymetrics");
                        for(int j=0;j<activitymetrics.size();j++)
                        {
                      	  JSONObject activitymetricsObject = (JSONObject) activitymetrics.get(j); 
                      	  JSONObject activitymetricsEachObject = new JSONObject();
                      	  if(activitymetricsObject.toJSONString().contains("cpuincrease"))
                      	  {
                      		  activitymetricsEachObject = (JSONObject) activitymetricsObject.get("cpu");
                      		  String cpuincrease =  activitymetricsEachObject.get("cpuincrease").toString();
                      		  Responses.put("cpuincrease", cpuincrease);
                      		  System.out.println("cpuincrease - "+ cpuincrease);
                      	  }
                      	  if(activitymetricsObject.toJSONString().contains("memoryincrease"))
                      	  {
                      		  activitymetricsEachObject = (JSONObject) activitymetricsObject.get("memory");
                      		  long memoryincrease = (Long) activitymetricsEachObject.get("memoryincrease");
                      		  Responses.put("memoryincrease", memoryincrease);
                      		  
                      		  System.out.println("memoryincrease - "+ memoryincrease);
                      	  }
                        }
                        
                        
                        System.out.println("activitymetrics length - "+ activitymetrics.size());
              //          System.out.println("response length - "+response.size());
                        long responsetime = (Long) response.get("ResponseTime");
                        Responses.put("ResponseTime", responsetime);
                        String activityname = (String) response.get("activity");
                        Responses.put("activity", activityname);
                        activityArray.add(Responses);
                        // activityArray.add(Activities);
                        jsonindividual.put("Activity" + i, activityArray);
        }
        // System.out.println("individual"+individual+individual.size());

        for (int i = 0; i < main.size(); i++) {
                        JSONObject mainobj = (JSONObject) main.get(i);

                        // System.out.println("packagename"+packagename);
                        JSONObject src = (JSONObject) mainobj.get("_source");
                        JSONObject Application = (JSONObject) src.get("Application");
                        JSONObject runs = (JSONObject) Application.get("runs");
                        packagename = (String) Application.get("applicationName");
                        JSONObject rundets = (JSONObject) runs.get("rundetails");
                        deviceos = (String) rundets.get("deviceType");
                        osversion = (String) rundets.get("deviceOS");
                        JSONObject usecases = (JSONObject) rundets.get("usecasedetail");

                        JSONObject usecasewise = new JSONObject();
                        JSONObject usecasedetail = new JSONObject();
                        usecasedetail = (JSONObject) usecases.get("usecasedetails");
                        String usecasename = (String) usecases.get("usecasename");
                        usecasewise.put("usecasename", usecases.get("usecasename"));
                        usecasewise.put("usecasedetails_refernceId", RequestId);
                        usecasewise.put("averagecpu", usecasedetail.get("averagecpu"));
                        usecasewise.put("maxcpu", usecasedetail.get("maxcpu"));
                        usecasewise.put("maxthread", usecasedetail.get("maxthread"));
                        usecasewise.put("averagethread", usecasedetail.get("averagethread"));
                        usecasewise.put("maxmemory", usecasedetail.get("maxmemory"));
                        usecasewise.put("averagememory", usecasedetail.get("averagememory"));
                        launchtimeobj = (JSONObject) usecasedetail.get("pagewisedetails");
                        // Responsetime=(Long) launchtimeobj.get("LauchResponseTime");
                        // System.out.println("Responsetime"+Responsetime);
                        // usecasename.put("Responsetime", Responsetime); //wrong response
                        // time

                        phonedetails = (JSONObject) usecasedetail.get("appPhone");

                        usecasedetails.add(usecasewise);
                        String launchtimetemp = (String) launchtimeobj.get("launchtime");
                        launchtime = launchtime + Integer.parseInt(launchtimetemp);

                        Long averagecpu = (Long) usecasedetail.get("averagecpu");
                        avgcpusum = (int) ((avgcpusum) + (averagecpu));

                        Long maxcpu = (Long) usecasedetail.get("maxcpu");
                        maxcpusum = (int) (maxcpusum + maxcpu);

                        Long maxthread = (Long) usecasedetail.get("maxthread");
                        maxtrdsum = (int) (maxtrdsum + maxthread);

                        Long averagethread = (Long) usecasedetail.get("averagethread");
                        avgtrdsum = (int) (avgtrdsum + averagethread);

                        Long maxmemory = (Long) usecasedetail.get("maxmemory");
                        maxmemsum = (long) (maxmemsum + maxmemory);

                        Long averagememory = (Long) usecasedetail.get("averagememory");
                        avgmemsum = (long) (avgmemsum + averagememory);
                        JSONObject eachResponse = new JSONObject();
                        for (int j = 0; j < jsonindividual.size(); j++) {
                                        JSONArray eachindi = new JSONArray();
                                        eachindi = (JSONArray) jsonindividual.get("Activity" + j);
                                        // System.out.println("eachindi"+eachindi);
                                        JSONObject eachobject = new JSONObject();
                                        eachobject = (JSONObject) eachindi.get(0);
                                        // System.out.println("eachactivity"+eachobject);
                                        String eachactivity = "";
                                        eachactivity = (String) eachobject.get("activity");

                                        if (eachactivity.contains(usecasename)) {
                                                        long Responsetime;
                                                        Responsetime = (Long) eachobject.get("ResponseTime");
                                                        eachResponse.put(eachactivity, Responsetime);

                                        }
                                        if (usecasename.contains("Launch")) {
                                                        // long Responsetime;
                                                        /* Responsetime=(Long) eachobject.get("ResponseTime"); */
                                                        eachResponse.put(usecasename, launchtime);
                                //                        System.out.println("launchtime Firstloop - " + launchtime);
                                        }

                                        usecasewise.put("pageresponses", eachResponse);
                           //            System.out.println("eachactivity" + eachResponse);
                         //               System.out.println("launchtime  - " + launchtime);
                                        JSONObject eachresponse = new JSONObject();

                        }
                        
                 //       System.out.println("launchtime1  - " + launchtime);
                        JSONObject pageResponses = new JSONObject();
               //         System.out.println("jsonResult.size() -  "+jsonResult.size());
                        for (int j = 0; j < jsonResult.size(); j++) {
                      	  
                      	  JSONObject eachPageResponses = new JSONObject();
                      	  Long PageSize; int No_Of_Networkcall =0;
                            JSONArray eachRes = new JSONArray();
                            eachRes = (JSONArray) jsonResult.get("Activity" + j);
                            JSONArray eachindi = new JSONArray();
                            eachindi = (JSONArray) jsonindividual.get("Activity" + j);
                            
                            // System.out.println("eachindi"+eachindi);
                            JSONObject eachResObject = new JSONObject();
                            eachResObject = (JSONObject) eachRes.get(0);
                            JSONObject eachIndiObject = new JSONObject();
                            eachIndiObject = (JSONObject) eachindi.get(0);
           //                 System.out.println("eachResObject  - "+ eachResObject);
                            String eachactivity = "";
                            eachactivity = (String) eachIndiObject.get("activity");

                            if (eachactivity.contains(usecasename)) {
                          	
                                            long Responsetime;
                                            Responsetime = (Long) eachIndiObject.get("ResponseTime");
                                            eachPageResponses.put("ResponseTime", Responsetime);
                                            
                                            String cpuincrease;
                                            cpuincrease =  eachIndiObject.get("cpuincrease").toString();
                                            eachPageResponses.put("cpuincrease", cpuincrease);
                                            
                                            long memoryincrease;
                                            memoryincrease = (Long) eachIndiObject.get("memoryincrease");
                                            eachPageResponses.put("memoryincrease", memoryincrease);
                                            
                                            No_Of_Networkcall = (int) eachResObject.get("No_Of_Networkcall");
                                            eachPageResponses.put("No_Of_Networkcall", No_Of_Networkcall);
                                            
                                            PageSize = (Long) eachResObject.get("PageSize");
                                            eachPageResponses.put("PageSize", PageSize);
                                            pageResponses.put(eachactivity,eachPageResponses);

                            }
                           
                            if (usecasename.contains("Launch")) {
                          	 
                                            
                          	  				eachPageResponses.put("ResponseTime", launchtime);
                          	  				pageResponses.put(usecasename,eachPageResponses);
                            }
                            
                            
                            
                            
                           
                            usecasewise.put("clickWisePageResponse", pageResponses);
                    //        System.out.println("eachactivity     " + pageResponses);
                            

                        }
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        // usecasewise.put("Responsetime", Responsetime);

                        length++;
        }

        phonedetails = (JSONObject) phonedetails.get("phone");
        // System.out.println("phonedetails"+phonedetails);

        // System.out.println(avgcpusum+" "+maxcpusum+" "+maxtrdsum+"
        // "+avgtrdsum+" "+maxmemsum+" "+maxmemsum+" "+length);
        int app_averagecpu = avgcpusum / length;
        int app_maxcpu = maxcpusum / length;
        int app_avethread = avgtrdsum / length;
        int app_maxthread = maxtrdsum / length;
        int app_maxmemory = avgcpusum / length;
        int app_averagememory = avgcpusum / length;
        MainOBJECT.put("packagename", packagename);
        MainOBJECT.put("DeviceType", deviceos);
        MainOBJECT.put("RequestId", RequestId);
        MainOBJECT.put("DeviceName", phonedetails.get("[ro$product$model]"));
        MainOBJECT.put("DeviceOS", osversion);
        MainOBJECT.put("Networktype", "WIFI");

        JSONArray Overallsummaryarr = new JSONArray();
        JSONObject overallobj = new JSONObject();

        overallobj.put("app_averagecpu", app_averagecpu);
        overallobj.put("app_maxcpu", app_maxcpu);
        overallobj.put("app_avethread", app_avethread);
        overallobj.put("app_maxthread", app_maxthread);
        overallobj.put("app_maxmemory", app_maxmemory);
        overallobj.put("app_averagememory", app_averagememory);
        overallobj.put("appsize", appsize.get("FileSize"));
        overallobj.put("launchtime", launchtime + "ms");
        Overallsummaryarr.add(overallobj);

        MainOBJECT.put("Overallsummary", Overallsummaryarr);
        MainOBJECT.put("usecasedetails", usecasedetails);
        hits.put("hits", MainOBJECT);
        System.out.println("Overallsummary" + hits);
        File file = new File(filePath2 + "Final.json");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        // System.out.println("Writing JSON object to file");
        // System.out.println("-----------------------");
        // System.out.print(hits);

        fileWriter.write(hits.toJSONString());
        fileWriter.flush();
        fileWriter.close();

        Properties properties = new Properties();
		
		FileInputStream propertiesFile;
		propertiesFile=new FileInputStream("dbconfig.properties");
		
		properties.load(propertiesFile);
		String serverpath = properties.getProperty("serverpath")+RequestId;
		File serverfinalpath=new File(serverpath);
		if(!serverfinalpath.isDirectory())
		{
			serverfinalpath.mkdir();
		}
		try{
			File serversrpath=new File(filePath1);
		srdel.moveMobileTemplate(serversrpath.getPath(), serverfinalpath.getPath());
		
		File jsFile = new File(filePath2 + "Final.json");
		HttpClient httpClientDefault1 = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://localhost:8082/mobClinApi/ScriptConversion/servicedetails/scriptupload");
		FileBody uploadFilePart = new FileBody(jsFile.getAbsoluteFile());
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("jsonFile", uploadFilePart);
		post.setEntity(reqEntity);
		HttpResponse httpRespnse = httpClientDefault1.execute(post);

		System.out.println("Output: " + httpRespnse);
       
		}catch(Exception e)
  		{
  			e.printStackTrace();
  		}
          System.out.println("Finished Successfully");	
		
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void jsonFileCreationForApp(String filePath2,JSONArray root,JSONArray clickarray,String reportJsn, String RequestId,String apiUrl, String filePath1)
	{
		FileWriter fw;
		try {
			fw = new FileWriter(new File(filePath2 + "mobile.json"));
	
		fw.write(root.toJSONString());
		fw.flush();
		fw.close();

		FileWriter fw3 = new FileWriter(new File(filePath2 + "click.json"));
		fw3.write(clickarray.toJSONString());
		fw3.flush();
		fw3.close();

		FileWriter finalfile = new FileWriter(new File(filePath2 + "Result.json"));
		finalfile.write(reportJsn);
		finalfile.flush();
		finalfile.close();


		  FileReader reader = new FileReader(filePath2 + "mobile.json");
        FileReader reader1 = new FileReader(filePath2 + "appsize.json");
        FileReader reader2 = new FileReader(filePath2 + "individual.json");
        FileReader reader3 = new FileReader(filePath2 + "Result.json");
        
        JSONParser parser = new JSONParser();
        // JSONParser parser1=new JSONParser();
        Object object = parser.parse(reader);
        Object object1 = parser.parse(reader1);
        Object object2 = parser.parse(reader2);
        Object object3 = parser.parse(reader3);
        // System.out.println(object2);

        JSONArray main = new JSONArray();
        main = (JSONArray) object;
        JSONObject appsize = new JSONObject();
        JSONArray respOBJ = new JSONArray();
        respOBJ = (JSONArray) object2;
        // System.out.println(respOBJ.size());
        appsize = (JSONObject) object1;
        JSONArray ResultJson = new JSONArray();
        ResultJson = (JSONArray) object3;
        
        
        int avgcpusum = 0, maxcpusum = 0, maxtrdsum = 0, avgtrdsum = 0, length = 0, launchtime = 0;
        long maxmemsum = 0, avgmemsum = 0;
        String packagename = "", deviceos = "", osversion = "";

        JSONObject launchtimeobj = new JSONObject();
        JSONObject jsonindividual = new JSONObject();
        JSONObject jsonResult = new JSONObject();

        JSONObject MainOBJECT = new JSONObject();
        JSONArray usecasedetails = new JSONArray();
        JSONObject phonedetails = new JSONObject();
        JSONObject hits = new JSONObject();
        
        int l=0;
        for(int i=1;i<ResultJson.size();i++)
        {
      	  
      	  JSONObject resPages = (JSONObject) ResultJson.get(i); 
      //	  ResUseCase = (JSONObject) ResultJson.get(i);
      //	  System.out.println("ResUseCase length - "+"  "+resPages.size()+"  , "+resPages.toString());
      	  ArrayList<String> al = new ArrayList<String>();
      	  al.addAll(resPages.keySet());
      	  JSONArray resPageClicks = (JSONArray) resPages.get(al.get(0));
     // 	  System.out.println("jsonarr_1 length - "+resPageClicks.size()+"  "+al.get(0));
      	  
      	  for(int j=0;j<resPageClicks.size();j++)
      	  {
      		  JSONArray activityArray = new JSONArray();
      		  JSONObject Responses = new JSONObject();
          	  
      		  JSONObject eachPageClick = (JSONObject) resPageClicks.get(j); 
      		  ArrayList<String> al1 = new ArrayList<String>();
          	  al1.addAll(eachPageClick.keySet());
      	//	  System.out.println("al1 length and value    - " + al1.size()+"  ,  "+ al1);
      		  JSONObject pageClick = (JSONObject) eachPageClick.get(al1.get(0));
      		 
      		  JSONArray networkDetails = (JSONArray) pageClick.get("NetworkDetails");
      		  
      		  long pageSize=0;
      		  for(int k=0;k<networkDetails.size();k++)
      		  {
      			  JSONObject responseSize = (JSONObject) networkDetails.get(k);
      			  pageSize = pageSize + (long) responseSize.get("ResponseSize");
      		  }
      //		  System.out.println("jsonarr_1 length - "+pageSize+"    "+networkDetails.size());
      		  Responses.put("activity", al1.get(0));
      		  Responses.put("No_Of_Networkcall", networkDetails.size());
      		  Responses.put("PageSize", pageSize);
      		  activityArray.add(Responses);
                // activityArray.add(Activities);
      		  jsonResult.put("Activity" + l, activityArray);
      		  l++;
     // 		  System.out.println("jsonResult inside loop  -  "+jsonResult.toString());
      		  
      	  }
      	  
      //	  System.out.println("jsonResult   -  "+jsonResult.toString());
      	  
        }
        
        

        for (int i = 0; i < respOBJ.size(); i++) {
                        JSONArray activityArray = new JSONArray();
                        JSONObject Activities = new JSONObject();
                        JSONObject Responses = new JSONObject();
                        JSONObject response = new JSONObject();
                    
                        response = (JSONObject) respOBJ.get(i);
                        JSONArray activitymetrics = (JSONArray) response.get("activitymetrics");
                        for(int j=0;j<activitymetrics.size();j++)
                        {
                      	  JSONObject activitymetricsObject = (JSONObject) activitymetrics.get(j); 
                      	  JSONObject activitymetricsEachObject = new JSONObject();
                      	  if(activitymetricsObject.toJSONString().contains("cpuincrease"))
                      	  {
                      		  activitymetricsEachObject = (JSONObject) activitymetricsObject.get("cpu");
                      		  String cpuincrease =  activitymetricsEachObject.get("cpuincrease").toString();
                      		  Responses.put("cpuincrease", cpuincrease);
                      		  System.out.println("cpuincrease - "+ cpuincrease);
                      	  }
                      	  if(activitymetricsObject.toJSONString().contains("memoryincrease"))
                      	  {
                      		  activitymetricsEachObject = (JSONObject) activitymetricsObject.get("memory");
                      		  long memoryincrease = (Long) activitymetricsEachObject.get("memoryincrease");
                      		  Responses.put("memoryincrease", memoryincrease);
                      		  
                      		  System.out.println("memoryincrease - "+ memoryincrease);
                      	  }
                        }
                        
                        
                        System.out.println("activitymetrics length - "+ activitymetrics.size());
              //          System.out.println("response length - "+response.size());
                        long responsetime = (Long) response.get("ResponseTime");
                        Responses.put("ResponseTime", responsetime);
                        String activityname = (String) response.get("activity");
                        Responses.put("activity", activityname);
                        activityArray.add(Responses);
                        // activityArray.add(Activities);
                        jsonindividual.put("Activity" + i, activityArray);
        }
        // System.out.println("individual"+individual+individual.size());

        for (int i = 0; i < main.size(); i++) {
                        JSONObject mainobj = (JSONObject) main.get(i);

                        // System.out.println("packagename"+packagename);
                        JSONObject src = (JSONObject) mainobj.get("_source");
                        JSONObject Application = (JSONObject) src.get("Application");
                        JSONObject runs = (JSONObject) Application.get("runs");
                        packagename = (String) Application.get("applicationName");
                        JSONObject rundets = (JSONObject) runs.get("rundetails");
                        deviceos = (String) rundets.get("deviceType");
                        osversion = (String) rundets.get("deviceOS");
                        JSONObject usecases = (JSONObject) rundets.get("usecasedetail");

                        JSONObject usecasewise = new JSONObject();
                        JSONObject usecasedetail = new JSONObject();
                        usecasedetail = (JSONObject) usecases.get("usecasedetails");
                        String usecasename = (String) usecases.get("usecasename");
                        usecasewise.put("usecasename", usecases.get("usecasename"));
                        usecasewise.put("usecasedetails_refernceId", RequestId);
                        usecasewise.put("averagecpu", usecasedetail.get("averagecpu"));
                        usecasewise.put("maxcpu", usecasedetail.get("maxcpu"));
                        usecasewise.put("maxthread", usecasedetail.get("maxthread"));
                        usecasewise.put("averagethread", usecasedetail.get("averagethread"));
                        usecasewise.put("maxmemory", usecasedetail.get("maxmemory"));
                        usecasewise.put("averagememory", usecasedetail.get("averagememory"));
                        launchtimeobj = (JSONObject) usecasedetail.get("pagewisedetails");
                        // Responsetime=(Long) launchtimeobj.get("LauchResponseTime");
                        // System.out.println("Responsetime"+Responsetime);
                        // usecasename.put("Responsetime", Responsetime); //wrong response
                        // time

                        phonedetails = (JSONObject) usecasedetail.get("appPhone");

                        usecasedetails.add(usecasewise);
                        String launchtimetemp = (String) launchtimeobj.get("launchtime");
                        launchtime = launchtime + Integer.parseInt(launchtimetemp);

                        Long averagecpu = (Long) usecasedetail.get("averagecpu");
                        avgcpusum = (int) ((avgcpusum) + (averagecpu));

                        Long maxcpu = (Long) usecasedetail.get("maxcpu");
                        maxcpusum = (int) (maxcpusum + maxcpu);

                        Long maxthread = (Long) usecasedetail.get("maxthread");
                        maxtrdsum = (int) (maxtrdsum + maxthread);

                        Long averagethread = (Long) usecasedetail.get("averagethread");
                        avgtrdsum = (int) (avgtrdsum + averagethread);

                        Long maxmemory = (Long) usecasedetail.get("maxmemory");
                        maxmemsum = (long) (maxmemsum + maxmemory);

                        Long averagememory = (Long) usecasedetail.get("averagememory");
                        avgmemsum = (long) (avgmemsum + averagememory);
                        JSONObject eachResponse = new JSONObject();
                        for (int j = 0; j < jsonindividual.size(); j++) {
                                        JSONArray eachindi = new JSONArray();
                                        eachindi = (JSONArray) jsonindividual.get("Activity" + j);
                                        // System.out.println("eachindi"+eachindi);
                                        JSONObject eachobject = new JSONObject();
                                        eachobject = (JSONObject) eachindi.get(0);
                                        // System.out.println("eachactivity"+eachobject);
                                        String eachactivity = "";
                                        eachactivity = (String) eachobject.get("activity");

                                        if (eachactivity.contains(usecasename)) {
                                                        long Responsetime;
                                                        Responsetime = (Long) eachobject.get("ResponseTime");
                                                        eachResponse.put(eachactivity, Responsetime);

                                        }
                                        if (usecasename.contains("Launch")) {
                                                        // long Responsetime;
                                                        /* Responsetime=(Long) eachobject.get("ResponseTime"); */
                                                        eachResponse.put(usecasename, launchtime);
                                //                        System.out.println("launchtime Firstloop - " + launchtime);
                                        }

                                        usecasewise.put("pageresponses", eachResponse);
                           //            System.out.println("eachactivity" + eachResponse);
                         //               System.out.println("launchtime  - " + launchtime);
                                        JSONObject eachresponse = new JSONObject();

                        }
                        
                 //       System.out.println("launchtime1  - " + launchtime);
                        JSONObject pageResponses = new JSONObject();
               //         System.out.println("jsonResult.size() -  "+jsonResult.size());
                        for (int j = 0; j < jsonResult.size(); j++) {
                      	  
                      	  JSONObject eachPageResponses = new JSONObject();
                      	  Long PageSize; int No_Of_Networkcall =0;
                            JSONArray eachRes = new JSONArray();
                            eachRes = (JSONArray) jsonResult.get("Activity" + j);
                            JSONArray eachindi = new JSONArray();
                            eachindi = (JSONArray) jsonindividual.get("Activity" + j);
                            
                            // System.out.println("eachindi"+eachindi);
                            JSONObject eachResObject = new JSONObject();
                            eachResObject = (JSONObject) eachRes.get(0);
                            JSONObject eachIndiObject = new JSONObject();
                            eachIndiObject = (JSONObject) eachindi.get(0);
           //                 System.out.println("eachResObject  - "+ eachResObject);
                            String eachactivity = "";
                            eachactivity = (String) eachIndiObject.get("activity");

                            if (eachactivity.contains(usecasename)) {
                          	
                                            long Responsetime;
                                            Responsetime = (Long) eachIndiObject.get("ResponseTime");
                                            eachPageResponses.put("ResponseTime", Responsetime);
                                            
                                            String cpuincrease;
                                            cpuincrease =  eachIndiObject.get("cpuincrease").toString();
                                            eachPageResponses.put("cpuincrease", cpuincrease);
                                            
                                            long memoryincrease;
                                            memoryincrease = (Long) eachIndiObject.get("memoryincrease");
                                            eachPageResponses.put("memoryincrease", memoryincrease);
                                            
                                            No_Of_Networkcall = (int) eachResObject.get("No_Of_Networkcall");
                                            eachPageResponses.put("No_Of_Networkcall", No_Of_Networkcall);
                                            
                                            PageSize = (Long) eachResObject.get("PageSize");
                                            eachPageResponses.put("PageSize", PageSize);
                                            pageResponses.put(eachactivity,eachPageResponses);

                            }
                           
                            if (usecasename.contains("Launch")) {
                          	 
                                            
                          	  				eachPageResponses.put("ResponseTime", launchtime);
                          	  				pageResponses.put(usecasename,eachPageResponses);
                            }
                            
                            
                            
                            
                           
                            usecasewise.put("clickWisePageResponse", pageResponses);
                    //        System.out.println("eachactivity     " + pageResponses);
                            

                        }
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        // usecasewise.put("Responsetime", Responsetime);

                        length++;
        }

        phonedetails = (JSONObject) phonedetails.get("phone");
        // System.out.println("phonedetails"+phonedetails);

        // System.out.println(avgcpusum+" "+maxcpusum+" "+maxtrdsum+"
        // "+avgtrdsum+" "+maxmemsum+" "+maxmemsum+" "+length);
        int app_averagecpu = avgcpusum / length;
        int app_maxcpu = maxcpusum / length;
        int app_avethread = avgtrdsum / length;
        int app_maxthread = maxtrdsum / length;
        int app_maxmemory = avgcpusum / length;
        int app_averagememory = avgcpusum / length;
        MainOBJECT.put("packagename", packagename);
        MainOBJECT.put("DeviceType", deviceos);
        MainOBJECT.put("RequestId", RequestId);
        MainOBJECT.put("DeviceName", phonedetails.get("[ro$product$model]"));
        MainOBJECT.put("DeviceOS", osversion);
        MainOBJECT.put("Networktype", "WIFI");

        JSONArray Overallsummaryarr = new JSONArray();
        JSONObject overallobj = new JSONObject();

        overallobj.put("app_averagecpu", app_averagecpu);
        overallobj.put("app_maxcpu", app_maxcpu);
        overallobj.put("app_avethread", app_avethread);
        overallobj.put("app_maxthread", app_maxthread);
        overallobj.put("app_maxmemory", app_maxmemory);
        overallobj.put("app_averagememory", app_averagememory);
        overallobj.put("appsize", appsize.get("FileSize"));
        overallobj.put("launchtime", launchtime + "ms");
        Overallsummaryarr.add(overallobj);

        MainOBJECT.put("Overallsummary", Overallsummaryarr);
        MainOBJECT.put("usecasedetails", usecasedetails);
        hits.put("hits", MainOBJECT);
        System.out.println("Overallsummary" + hits);
        File file = new File(filePath2 + "Final.json");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        // System.out.println("Writing JSON object to file");
        // System.out.println("-----------------------");
        // System.out.print(hits);

        fileWriter.write(hits.toJSONString());
        fileWriter.flush();
        fileWriter.close();

        /*Properties properties = new Properties();
		
		FileInputStream propertiesFile;
		propertiesFile=new FileInputStream("dbconfig.properties");
		
		properties.load(propertiesFile);
		String serverpath = properties.getProperty("serverpath")+RequestId;
		File serverfinalpath=new File(serverpath);
		if(!serverfinalpath.isDirectory())
		{
			serverfinalpath.mkdir();
		}
		try{
			File serversrpath=new File(filePath1);
		srdel.moveMobileTemplate(serversrpath.getPath(), serverfinalpath.getPath());
		
		File jsFile = new File(filePath2 + "Final.json");
		HttpClient httpClientDefault1 = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://localhost:8082/mobClinApi/ScriptConversion/servicedetails/scriptupload");
		FileBody uploadFilePart = new FileBody(jsFile.getAbsoluteFile());
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("jsonFile", uploadFilePart);
		post.setEntity(reqEntity);
		HttpResponse httpRespnse = httpClientDefault1.execute(post);

		System.out.println("Output: " + httpRespnse);
       
		}catch(Exception e)
  		{
  			e.printStackTrace();
  		}*/
          System.out.println("Finished Successfully");	
		
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
