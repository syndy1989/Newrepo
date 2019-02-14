package com.nft.collector_method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Screenshot implements Runnable{

	public boolean infinity=false;
	public String filePath;
	public String fileName;
	public int counter=0;
	public boolean isInfinity() {
		return infinity;
	}
	public void setInfinity(boolean infinity) {
		this.infinity = infinity;
	}
	public Screenshot(boolean infinity,String filePath,String fileName)
	{
		this.infinity=infinity;
		this.filePath=filePath;
		this.fileName=fileName;
	}
	public void init()
	{
		System.out.println("adb shell \"screencap -p /data/local/tmp/"+(this.fileName+".png")+" \" && adb pull \"/data/local/tmp/"+(this.fileName+".png")+" \" "+(this.filePath)+" && adb shell \"rm /data/local/tmp/"+this.fileName+".png"+" \" && exit");
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"screencap -p /data/local/tmp/"+(this.fileName+".png")+"\" && adb pull \"/data/local/tmp/"+(this.fileName+".png")+"\" "+(this.filePath)+" && adb shell \"rm /data/local/tmp/"+this.fileName+".png"+" \" && exit");
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
	public void runmetrics()
	{
		int value=this.counter++;
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"screencap -p /data/local/tmp/"+(this.fileName+value+".png")+"\" && adb pull \"/data/local/tmp/"+(this.fileName+value+".png")+"\" "+(this.filePath+this.fileName+value+".png")+"  && adb shell \"rm /data/local/tmp/"+(this.fileName+value+".png")+" \" && exit");
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
		
		
		while(this.isInfinity())
		{
		this.runmetrics();
		}
		try {
			this.generateVideoFromScreenShots();
			System.out.println("Video Completed Sucessfully");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in Converting screenshots");
			e.printStackTrace();
		}
		System.out.println( "ScreenShot Thread Closed");

	}
	public void generateVideoFromScreenShots() throws IOException, InterruptedException
	{
		ProcessBuilder processBuilder=new ProcessBuilder("cmd.exe","/c","ffmpeg -framerate 1 -i \""+this.filePath+"screenshot%d.png\" -r 25 -pix_fmt yuv420p \""+this.filePath+"screenshotvideo.mp4\" && exit");
		processBuilder.redirectErrorStream(true);
		Process process=processBuilder.start();
		//process.waitFor();
		/*BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) { break; }

			System.out.println(line);
		}*/
	}
		public void before(String filePath,String fileName)
	{
			this.filePath=filePath;
			this.fileName=fileName;
		System.out.println("adb shell \"screencap -p /data/local/tmp/"+(this.fileName+".png")+" \" && adb pull \"/data/local/tmp/"+(this.fileName+".png")+" \" "+(this.filePath)+" && adb shell \"rm /data/local/tmp/"+this.fileName+".png"+" \" && exit");
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"screencap -p /data/local/tmp/"+(this.fileName+".png")+"\" && adb pull \"/data/local/tmp/"+(this.fileName+".png")+"\" "+(this.filePath)+" && adb shell \"rm /data/local/tmp/"+this.fileName+".png"+" \" && exit");
			builder.redirectErrorStream(true);

			Process p = builder.start();
			p.waitFor();
			/*BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) { break; }

				System.out.println(line);
			}*/
		}
		catch(Exception e)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~Error in TopParser Thread~~~~~~~~~~~~~~~~~~");
			e.printStackTrace();
			System.out.println("^^^^^^^^^^^^^^^^^^Error in TopParser Thread^^^^^^^^^^^^^^^^^^s");
		}
	}
	
	public void after(String filePath,String fileName)
	{
		this.filePath=filePath;
		this.fileName=fileName;
		System.out.println(filePath+":"+fileName+"testpathscreen");
		System.out.println("adb shell \"screencap -p /data/local/tmp/"+(this.fileName+".png")+" \" && adb pull \"/data/local/tmp/"+(this.fileName+".png")+" \" "+(this.filePath)+" && adb shell \"rm /data/local/tmp/"+this.fileName+".png"+" \" && exit");
		try
		{
			ProcessBuilder builder = new ProcessBuilder(
					"cmd.exe", "/c", "adb shell \"screencap -p /data/local/tmp/"+(this.fileName+".png")+"\" && adb pull \"/data/local/tmp/"+(this.fileName+".png")+"\" "+(this.filePath)+" && adb shell \"rm /data/local/tmp/"+this.fileName+".png"+" \" && exit");
			builder.redirectErrorStream(true);

			Process p = builder.start();
			p.waitFor();
			/*BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) { break; }

				System.out.println(line);
			}*/
		}
		catch(Exception e)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~Error in TopParser Thread~~~~~~~~~~~~~~~~~~");
			e.printStackTrace();
			System.out.println("^^^^^^^^^^^^^^^^^^Error in TopParser Thread^^^^^^^^^^^^^^^^^^s");
		}
	}

}
