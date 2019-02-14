package com.nft.collector_method;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;

public class MobileProperties {

	/*public static void main(String[] args) {
		System.out.println(MobileProperties.getMobileProperties());
	}*/
	public static JSONObject getMobileProperties()
	{
		JSONObject mobileProperties=new JSONObject();
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell getprop  && exit");
			builder.redirectErrorStream(true);

			Process p = builder.start();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;

			while (true) {
				line = r.readLine();
				if (line == null) { break; }

				if(line.contains("[ro.product.model]:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				else if(line.contains("[ro.product.manufacturer]:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				else if(line.contains("[ro.product.brand]:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				else if(line.contains("[ro.hardware]:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				else if(line.contains("[ro.build.version.sdk]:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				else if(line.contains("[ro.build.version.release]:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				else if(line.contains("[ro.boot.hardware]:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				else if(line.contains("[ro.board.platform]:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				else if(line.contains("[ro.product.cpu.abi]"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				else if(line.contains("[gsm.sim.operator.alpha]: "))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				else if(line.contains("[gsm.sim.operator.iso-country]:"))
				{
					StringTokenizer st=new StringTokenizer(line, ":");
					mobileProperties.put(st.nextToken(), st.nextToken());
				}
				
				
			}
		}
		catch(Exception e)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~Error in MobileProperty ~~~~~~~~~~~~~~~~~~");
			e.printStackTrace();
			System.out.println("^^^^^^^^^^^^^^^^^^Error in MobileProperty ^^^^^^^^^^^^^^^^^^s");
		}


		return mobileProperties;
		/*[ro.product.model]:
			[ro.product.manufacturer]: 
			[ro.product.brand]:
			[ro.hardware]:
			[ro.build.version.sdk]:
			[ro.build.version.release]:
			[ro.boot.hardware]:
			[ro.board.platform]:
			[ro.product.cpu.abi]
			[gsm.sim.operator.alpha]: 
			[gsm.sim.operator.iso-country]:*/
	}

}
