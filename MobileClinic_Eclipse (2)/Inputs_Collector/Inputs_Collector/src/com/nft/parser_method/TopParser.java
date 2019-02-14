package com.nft.parser_method;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;

public class TopParser implements Runnable{

	public static String androidver;
	public static int cores;
	public boolean infinity=false;
	public String filePath;
	public String fileName;
	public String packageName;
	public boolean isInfinity() {
		return infinity;
	}
	public void setInfinity(boolean infinity) {
		this.infinity = infinity;
	}
	public TopParser(boolean infinity,String filePath, String fileName, String packageName)
	{
		this.infinity=infinity;
		this.filePath=filePath;
		this.fileName=fileName;
		this.packageName=packageName;
	}
	public TopParser(boolean infinity,String filePath, String fileName, String packageName, String androidver,int cores)
	{
		this.infinity=infinity;
		this.filePath=filePath;
		this.fileName=fileName;
		this.packageName=packageName;
		TopParser.androidver = androidver;
		TopParser.cores = cores;
	}
	public void init()
	{
		if(TopParser.androidver.equals("false"))
		{
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"top -n 1 | grep CPU%  \" >> \""+this.filePath+this.fileName+"\" && exit");
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
	public void runmetrics()
	{
		try
		{
			ProcessBuilder builder;
			
		//adb shell "top -n 1 | grep -v "grep" |grep com.brandin" >> topcommand.txt
			if(TopParser.androidver.equals("false"))
			{
			builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"top -n 1 | grep "+this.packageName+" \" >> \""+this.filePath+this.fileName+"\" && exit");
			}
			else
			{
				//adb shell "top -n 1 | grep -v "grep" |grep com.brandin" >> topcommand.txt
				builder = new ProcessBuilder(
						"cmd.exe", "/c", "adb shell \"top -n 1| grep -v \"grep\"  | grep "+this.packageName.substring(0,12)+" \" >> \""+this.filePath+this.fileName+"\" && exit");
			}
			builder.redirectErrorStream(true);

			Process p = builder.start();
			p.waitFor();
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

	@Override
	public void run() {
		this.init();
		long start = System.currentTimeMillis();
		while(this.isInfinity())
		{
			System.out.println("top executeddd");
			this.runmetrics();
			
		}
		long stop = System.currentTimeMillis();
		System.out.println("total time top parser:"+(stop-start));
		System.out.println( "Top Thread Closed");

	}
	/*public static void main(String args[])
	{
		System.out.println(TopParser.extractCPU());
	}*/
	public  JSONObject extractCPU(String absolutePath)
	{
		JSONObject root=new JSONObject();
		if(TopParser.androidver.equals("false"))
		{
		int cpuIndex = 0,threadIndex = 0,bgfgIndex = 0;
		JSONObject overallCPU=new JSONObject();
		JSONObject overallThread=new JSONObject();
		//JSONObject root=new JSONObject();

		try {
			BufferedReader br=new BufferedReader( new FileReader(new File(absolutePath)));
			int secoundsCounter=0;
			int sum=0;int max=0;int overallcounter=0;
			int tsum=0, tmax=0;
			while(true)
			{


				String line=br.readLine();
				if(line==null)
					break;
				if(line.trim().equals(""))
					continue;

				if(line.contains("CPU%")||line.contains("#THR")||line.contains("PCY"))
				{
					StringTokenizer st=new StringTokenizer(line, " ");
					int counter=0;
					while(st.hasMoreTokens())
					{
						++counter;
						String temp=st.nextToken();
						if(temp.equals("CPU%"))
							cpuIndex=counter;
						if(temp.equals("#THR"))
							threadIndex=counter;	

					}
					continue;
				}
				++secoundsCounter;
				StringTokenizer st=new StringTokenizer(line, " ");
				int counter=0;
				JSONObject eachSecondMetrics=new JSONObject();
				++overallcounter;
				while(st.hasMoreTokens())
				{
					++counter;
					String temp=st.nextToken();
					if(cpuIndex==counter)
					{
						if(Integer.parseInt(temp.replace("%", ""))>max)
							max=Integer.parseInt(temp.replace("%", ""));
						sum=sum+Integer.parseInt(temp.replace("%", ""));
						overallCPU.put(secoundsCounter+"s", Integer.parseInt(temp.replace("%", "")));
					}
					if(threadIndex==counter)
					{
						if(Integer.parseInt(temp)>tmax)
							tmax=Integer.parseInt(temp);
						tsum+=Integer.parseInt(temp);
						overallThread.put(secoundsCounter+"s", Integer.parseInt(temp));
					}

				}


			}
			root.put("cpudetails", overallCPU);
			root.put("averagecpu", sum/overallcounter);
			root.put("maxcpu", max);
			root.put("threaddetails", overallThread);
			root.put("maxthread", tmax);
			root.put("averagethread", tsum/overallcounter);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else{
			int cpuIndex = 0,threadIndex = 0,bgfgIndex = 0;
			JSONObject overallCPU=new JSONObject();
			JSONObject overallThread=new JSONObject();
			//JSONObject root=new JSONObject();

			try {
				BufferedReader br=new BufferedReader( new FileReader(new File(absolutePath)));
				int secoundsCounter=0;
				float sum=0;float max=0;int overallcounter=0;
				int tsum=0, tmax=0;
				while(true)
				{


					String line=br.readLine();
					if(line==null)
						break;
					if(line.trim().equals(""))
						continue;
//commenting it as we don't have any header
					/*if(line.contains("CPU%")||line.contains("#THR")||line.contains("PCY"))
					{
						StringTokenizer st=new StringTokenizer(line, " ");
						int counter=0;
						while(st.hasMoreTokens())
						{
							++counter;
							String temp=st.nextToken();
							if(temp.equals("CPU%"))
								cpuIndex=counter;
							if(temp.equals("#THR"))
								threadIndex=counter;	

						}
						continue;
					}*/
					++secoundsCounter;
					StringTokenizer st=new StringTokenizer(line, " ");
					int counter=0;
					JSONObject eachSecondMetrics=new JSONObject();
					++overallcounter;
					while(st.hasMoreTokens())
					{
						++counter;
						String temp=st.nextToken();
						//CPU stats is  available at 9th position
						if(counter==9)
						{
							float temp1 = Float.parseFloat(String.format("%.2f",Float.parseFloat(temp)/TopParser.cores));
							if(temp1>max)
								max=temp1;
							
							sum=sum+temp1;
							overallCPU.put(secoundsCounter+"s",temp1);
						}
						/*if(threadIndex==counter)
						{
							if(Integer.parseInt(temp)>tmax)
								tmax=Integer.parseInt(temp);
							tsum+=Integer.parseInt(temp);
							overallThread.put(secoundsCounter+"s", Integer.parseInt(temp));
						}*/

					}


				}
				//Float.parseFloat(String.format("%.2f",Float.parseFloat(sum/overallcounter))
				root.put("cpudetails", overallCPU);
				root.put("averagecpu", Float.parseFloat(String.format("%.2f",sum/overallcounter)));
				root.put("maxcpu", max);
				//root.put("threaddetails", overallThread);
				//root.put("maxthread", tmax);
				//root.put("averagethread", tsum/overallcounter);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return root;

	}

}
