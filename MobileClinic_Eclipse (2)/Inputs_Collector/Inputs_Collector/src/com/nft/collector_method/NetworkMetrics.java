package com.nft.collector_method;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;




import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class NetworkMetrics implements Runnable{
	public boolean infinity=false;
	public String filePath;
	public String fileName;
	public String packageName;
	public void setInfinity(boolean infinity) {
		this.infinity = infinity;
	}

	
	/*public NetworkMetrics(String usecasename,String packeagename)
	{   //this.Devicename=deviceid;
		this.usecasename=usecasename;
		//this.requestid=requestid;
		this.packageName=packeagename;
	}*/
	
	public NetworkMetrics(boolean infinity,String filePath, String fileName,String packageName)
	{
		this.infinity=infinity;
		this.filePath=filePath;
		this.fileName=fileName;
		this.packageName=packageName;
	}
	public void run() {
		String memoryname=null;
		if(this.infinity)
			runmetrics(packageName);
	}
	
	public void runmetrics(String packageName)
	{
		try
		{
			//String devicename="\""+this.Devicename+"\"";
			String netfilename=this.filePath+this.fileName;
			File f = new File(netfilename);
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"cat /proc/net/xt_qtaguid/stats \" >> \""+netfilename+"\" && exit");
			builder.redirectErrorStream(true);

			Process p = builder.start();
			//p.waitFor();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) { break; }

				System.out.println(line);
			}
		}
		catch(Exception e)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~Error in TopParser Thread~~~~~~~~~~~~~~~~~~");
			e.printStackTrace();
			System.out.println("^^^^^^^^^^^^^^^^^^Error in TopParser Thread^^^^^^^^^^^^^^^^^^s");
		}
			
  	
	}
	
}
	