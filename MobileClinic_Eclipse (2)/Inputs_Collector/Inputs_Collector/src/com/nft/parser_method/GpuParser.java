package com.nft.parser_method;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;

public class GpuParser {
	public boolean infinity=false;
	public String filePath;
	public String fileName;
	private String packageName;
	public boolean isInfinity() {
		return infinity;
	}
	public void setInfinity(boolean infinity) {
		this.infinity = infinity;
	}
	public GpuParser(boolean infinity,String filePath, String fileName,String packageName)
	{
		this.infinity=infinity;
		this.filePath=filePath;
		this.fileName=fileName;
		this.packageName=packageName;
		this.resetGPU(packageName);
	}
	private void resetGPU(String packageName) {
		//System.out.println("reset gpu");
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"dumpsys gfxinfo "+packageName+" reset\" && adb shell \"dumpsys gfxinfo "+packageName+" reset\" && exit");
			builder.redirectErrorStream(true);

			Process p = builder.start();
			System.out.println("reset gpu");
			//p.waitFor();
			System.out.println("reset gpu");
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) { break; }

				//System.out.println("testing if gpu works fine"+line);
			}
		}
		catch(Exception e)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~Error in TopParser Thread~~~~~~~~~~~~~~~~~~");
			e.printStackTrace();
			System.out.println("^^^^^^^^^^^^^^^^^^Error in TopParser Thread^^^^^^^^^^^^^^^^^^s");
		}
	}
	public void dumpOverallGPU()
	{
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"dumpsys gfxinfo "+this.packageName+"\" >> \""+this.filePath+this.fileName+"\" && exit");
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
	public JSONObject extractGPU(String absolutePath) {
		/*Total frames rendered: 1812

		Janky frames: 1633 (90.12%)*/
		
		JSONObject overallGPU=new JSONObject();
		try {
			BufferedReader br=new BufferedReader( new FileReader(new File(absolutePath)));
			while(true)
			{
				
				
				String line=br.readLine();
				if(line==null)
					break;
				if(line.trim().equals(""))
					continue;
				int gpuIndex=2;
				
				if(line.contains("Total frames rendered:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					int counter=0;
					while(st.hasMoreTokens())
					{
						++counter;
						String temp=st.nextToken();
						if(counter==gpuIndex)
							overallGPU.put("FramesLoaded",temp);
					}
				}
				if(line.contains("Janky frames:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					int counter=0;
					while(st.hasMoreTokens())
					{
						++counter;
						String temp=st.nextToken();
						if(counter==gpuIndex)
							overallGPU.put("FramesLost",temp);
					}
				}
				
			}
		}
		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return overallGPU;
		
	}
	

}
