package com.nft.parser_method;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LaunchTimeParser {
	
	/*public static void main(String args[])
	{
		System.out.println(getLaunchTime("com.lenovo.anyshare.gps","com.lenovo.anyshare.activity.MainActivity"));
	}
*/
	public static String getLaunchTime(String packageName,String activityName)
	{
		String launchTime=null;
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell  am start -S -W  "+packageName+"/"+activityName+" && exit");
			builder.redirectErrorStream(true);

			Process p = builder.start();
			p.waitFor();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) { break; }
				
				if(line.contains("ThisTime"))
				{
					String[] temp=line.split(":");
					launchTime= temp[1].trim();
				}
				//System.out.println(line);
			}
		}
		catch(Exception e)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~Error in TopParser Thread~~~~~~~~~~~~~~~~~~");
			e.printStackTrace();
			System.out.println("^^^^^^^^^^^^^^^^^^Error in TopParser Thread^^^^^^^^^^^^^^^^^^s");
		}
		return launchTime;
		
	}
}
