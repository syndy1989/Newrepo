package com.nft.parser_method;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;

public class MemoryParser implements Runnable{
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
	public MemoryParser(boolean infinity,String filePath, String fileName,String packageName)
	{
		this.infinity=infinity;
		this.filePath=filePath;
		this.fileName=fileName;
		this.packageName=packageName;
	}
	/*public void init()
	{
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"top -n 1 | grep CPU% \" && exit");
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
	}*/
	public void runmetrics(String packageName)
	{
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"dumpsys meminfo "+packageName+" | grep TOTAL \" >> \""+this.filePath+this.fileName+"\" && exit");
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
	
	@Override
	public void run() {
		while(this.isInfinity())
		{
		this.runmetrics(this.packageName);
		}
		System.out.println( "Memory Thread Closed");

	}
	/*public static void main(String args[])
	{
		System.out.println(TopParser.extractCPU());
	}*/
	public JSONObject extractMemory(String absolutePath,boolean fullMemorySplitUp) {
		JSONObject overallMemory=new JSONObject();
		JSONObject root=new JSONObject();
		try {
			BufferedReader br=new BufferedReader( new FileReader(new File(absolutePath)));
			int secoundsCounter=0;
			int max=0, sum=0,overallcounter=0;
			while(true)
			{
				
				
				String line=br.readLine();
				if(line==null)
					break;
				if(line.trim().equals(""))
					continue;
				++secoundsCounter;
				int memoryIndex=2;
				if(fullMemorySplitUp)
				{
					//have to implement parse code for native dalvik pss
				}
				if(line.contains("TOTAL"))
				{
					++overallcounter;
					StringTokenizer st=new StringTokenizer(line, " ");
					int counter=0;
					while(st.hasMoreTokens())
					{
						++counter;
						String temp=st.nextToken();
						
						if(counter==memoryIndex)
						{
							sum+=Integer.parseInt(temp);
							if(Integer.parseInt(temp)>max)
								max=Integer.parseInt(temp);
							overallMemory.put(secoundsCounter+"s", Integer.parseInt(temp));
						}
					}
				}
				
			}
			root.put("memorydetails", overallMemory);
			root.put("maxmemory", max);
			root.put("averagememory", sum/overallcounter);
			
		}
		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return root;
	}

}
