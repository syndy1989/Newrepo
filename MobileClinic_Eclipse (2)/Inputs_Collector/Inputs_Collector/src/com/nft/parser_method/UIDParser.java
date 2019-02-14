package com.nft.parser_method;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.StringTokenizer;






import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.nft.collector_method.MetricsCollector;



public class UIDParser implements Runnable
{
	public boolean infinity=false;
	public String filePath;
	public String fileName;
	public String packageName;

	public void setInfinity(boolean infinity) {
		this.infinity = infinity;
	}
	
	public UIDParser(boolean infinity,String filePath, String fileName,String packageName)
	{
		this.infinity=infinity;
		this.filePath=filePath;
		this.fileName=fileName;
		this.packageName=packageName;
	}

	/*public UIDParser(String deviceid,String usecasename,String requestid,String packeagename)
	{   
	this.Devicename=deviceid;
	this.usecasename=usecasename;
	this.requestid=requestid;
	this.packageName=packeagename;
	}*/
	public void dumpPackageDetails(String packageName)
			throws ParseException, IOException
	{
		String uid = null;
		//String devicename="\""+this.Devicename+"\"";
		String uidfilename=this.filePath+this.fileName;


		File f = new File(uidfilename);
		try {
			f.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{

			ProcessBuilder builder = new ProcessBuilder(
					
			//"cmd.exe", "/c", "adb shell \"dumpsys "+packageName+ "\" > \""+uidfilename+"\" && exit");
			"cmd.exe", "/c", "adb shell \"dumpsys package "+this.packageName+" \" > \""+uidfilename+"\" && exit");
			System.out.println("cmd.exe"+ "/c"+ "adb shell \"dumpsys package \" > \""+uidfilename+"\" && exit");
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


		return;
	}

	public String extractuid(String packageName,String usecasename)
			throws IOException
	{
		//System.out.println("Batch file1 done." + requestid);
		//String uidfilename=requestid+"\\"+usecasename+"\\"+usecasename+"uidstat.txt";
		String uidfilename=MetricsCollector.filePath+MetricsCollector.uidFileName;
		System.out.println("uidfilename" +uidfilename);
		File packageFile = new File(uidfilename);

		BufferedReader br = new BufferedReader(new FileReader(packageFile));
		String sCurrentLine = null;
		boolean userId = false;
		String uid = null;
		while ((sCurrentLine = br.readLine()) != null)
		{
			if ((sCurrentLine.contains("Package [" + this.packageName + "]")) && (!userId)) {
				userId = true;
			}
			if ((userId) && (sCurrentLine.contains("userId=")))
			{
				StringTokenizer st = new StringTokenizer(sCurrentLine, " ");
				sCurrentLine = st.nextToken();
				userId = false;
				uid = sCurrentLine.trim().replace("userId=", "");
			}
		}
		//System.out.println("uid file done." + uid);


		return uid;
	}
	public void run() {
		while(this.infinity)
		{
			try {
				this.dumpPackageDetails(packageName);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
