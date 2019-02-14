package com.nft.parser_method;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class IndividualMetricParser {

	String appPackageName;
	String uid;
	private String filePath;
	private String fileName;
	private HashMap<String,Long> totalTime;
	public String androidver;
	public int cores;

	public IndividualMetricParser(String packageName,String uid) {
		this.appPackageName=packageName;
		this.uid=uid;
		
	}

	public IndividualMetricParser(String packageName,String uid, String androidver,int cores) {
		this.appPackageName=packageName;
		this.uid=uid;
		this.androidver=androidver;
		this.cores=cores;
	}
	public static void main(String []args)
	{
		/*BufferedReader br=null;

		FileReader fr = null;
		try {
			fr = new FileReader(new File("tempfiles.json"));
		} catch (FileNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();

		}
		br = new BufferedReader(fr);


		String current = "";
		String json = "";

		try {
			while ((current = br.readLine()) != null)
			{
				json = json + current;
				//continue;
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		JSONParser jp = new JSONParser();
		JSONObject	jObj = null ;
		try {
			jObj = (JSONObject)jp.parse(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*IndividualMetricParser ts=new IndividualMetricParser("com.lenovo.anyshare.gps", "10136");
		JSONArray root=ts.listFilesForFolder(new File(jObj.get("rootFolder").toString()),"root");
		System.out.println(root.toJSONString());*/

	}
	public JSONArray collectIndividualMetrics(String filePath,String fileName,HashMap<String,Long> response)
	{
		this.filePath=filePath;
		this.fileName=fileName;
		this.totalTime=response;
		
		
		JSONArray root=this.listFilesForFolder(new File(filePath),"root");
		System.out.println(root.toJSONString());
		return root;
	}
	public  JSONArray listFilesForFolder(final File folder, String currentDirectory) {
		//JSONObject root=new JSONObject();
		JSONArray root=new JSONArray();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()&&fileEntry.getName().contains("_")) {
				root.add(this.getEachActivityDetails(fileEntry,fileEntry.getName()));
				System.out.println("directory");
			}


			else {
				continue;

			}
		}
		return root;
		//System.out.println(root);
	}

	private JSONObject getEachActivityDetails(File fileEntry, String name) {

		JSONObject singleScreen=new JSONObject();
		JSONArray singleScreenMetrics=new JSONArray();
		for(File entry:fileEntry.listFiles())
		{
			if(entry.isDirectory())
				continue;
			else if(entry.getName().contains("before.png"))
			{
				singleScreen.put("beforeScreen", entry.getAbsolutePath().replace(this.filePath, "..\\"));
			}
			else if(entry.getName().contains("after.png"))
			{
				singleScreen.put("afterScreen", entry.getAbsolutePath().replace(this.filePath, "..\\"));
			}
			else
			{
				if(this.sendToFileDetector(entry,name)==null)
					continue;
				else
				{
					//System.out.println("FileName:"+entry.getName());
					singleScreenMetrics.add(this.sendToFileDetector(entry,name));
				}
			}
		}
		singleScreen.put("activity", name);
		singleScreen.put("ResponseTime", this.totalTime.get(name));
		singleScreen.put("activitymetrics", singleScreenMetrics);
		
		
		
		return singleScreen;


	}
	private  JSONObject sendToFileDetector(File fileEntry,
			String currentDirectory) {

		//System.out.println(currentDirectory+":"+fileEntry.getName());
		JSONObject value = null;
		if(fileEntry.getName().contains("_cpu.txt"))
		{

			value=this.findDeltaCpu(fileEntry,currentDirectory);
		}
		else if(fileEntry.getName().contains("_gpu.txt"))
		{
			JSONObject gpu=this.findDeltaGPU(fileEntry,currentDirectory);
			if(gpu==null)
				return value;
			JSONObject details=new JSONObject();
			details.put("gpu", gpu);
			value=details;
		}
		else if(fileEntry.getName().contains("_memory.txt"))
		{
			value=this.findDeltaMemory(fileEntry,currentDirectory);
		}
		else if(fileEntry.getName().contains("_netstats.txt"))
		{
			value=this.findDeltaNetwork(fileEntry,currentDirectory);
		}
		else if(fileEntry.getName().contains("_thread.txt"))
		{
			//this.findDeltaThread(fileEntry,currentDirectory);
		}
		return value;
	}

	private  JSONObject findDeltaCpu(File fileEntry, String currentDirectory) {
		/*System.out.println("test1");*/
		JSONObject root=new JSONObject();
		if(this.androidver.equals("false"))
		{
		try {

			BufferedReader br=new BufferedReader(new FileReader(fileEntry));
			String currentLine=null;
			ArrayList<String> cpuValues=new ArrayList<String>();
			ArrayList<String> threadValues=new ArrayList<String>();
			boolean flag=false;
			int threadColumn=0;
			int cpuColumn=0;
			while((currentLine=br.readLine())!=null)
			{	
				if(currentLine.contains("CPU%")&&currentLine.contains("#THR")&&(!flag))
				{
					int count=1;
					StringTokenizer st=new StringTokenizer(currentLine.trim());
					while(st.hasMoreTokens())
					{
						String tem=st.nextToken();
						if(tem.equals("CPU%"))
						{

							cpuColumn=count;
						}
						if(tem.equals("#THR"))
						{

							threadColumn=count;
						}
						count++;
					}

				}
				//System.out.println(cpuColumn+":"+threadColumn);
				if(currentLine.contains(this.appPackageName))
				{
					int count=1;
					StringTokenizer st=new StringTokenizer(currentLine.trim());
					while(st.hasMoreTokens())
					{
						String temp=st.nextToken();
						//System.out.print(temp+" ");

						if(count==cpuColumn)
						{
							cpuValues.add(temp);
						}
						if(count==threadColumn)
						{
							threadValues.add(temp);
						}
						count++;
					}
					//System.out.println();



					//System.out.println(currentLine);
					//StringTokenizer st=new StringTokenizer(currentLine.trim());
					/*st.nextToken();
					st.nextToken();
					cpuValues.add(st.nextToken());
					st.nextToken();
					threadValues.add(st.nextToken());*/
					//System.out.println((st.nextToken()).nex);

				}

			}

			JSONObject cpudetails=new JSONObject();
			JSONObject threaddetails=new JSONObject();

			if(cpuValues.size()==0)
			{
				//System.out.println("no cpu found");
				cpudetails.put("cpuincrease",0);
				cpudetails.put("cpumax",0);
				//cpudetails.put("cpuavg",0);

			}
			else if(cpuValues.size()==1)
			{
				cpudetails.put("cpuincrease",cpuValues.get(0).replace("%","").trim());
				cpudetails.put("cpumax",cpuValues.get(0).replace("%","").trim());
				//cpudetails.put("cpuavg",cpuValues.get(0));
				//System.out.println("one cpu found");
			}
			else if(cpuValues.size()>1)
			{
				int sum=0,max=0;
				//System.out.println("two or more");
				int start=Integer.parseInt(cpuValues.get(0).replace("%","").trim());

				for(int i=1;i<cpuValues.size();i++)
				{
					if(Integer.parseInt(cpuValues.get(i).replace("%","").trim())>max)
					{
						max=Integer.parseInt(cpuValues.get(i).replace("%","").trim());
					}
					sum+=Integer.parseInt(cpuValues.get(i).replace("%","").trim());
				}
				cpudetails.put("cpuincrease",max-start);
				cpudetails.put("cpumax",max);
				//System.out.println(start+":::"+max);
				//cpudetails.put("cpuavg",sum/cpuValues.size());
			}




			if(threadValues.size()==0)
			{
				//System.out.println("no cpu found");
				threaddetails.put("threadincrease",0);
				//threaddetails.put("threadavg",0);

			}
			else if(threadValues.size()==1)
			{
				threaddetails.put("threadincrease",threadValues.get(0));
				//threaddetails.put("threadavg",threadValues.get(0));
				//threaddetails.out.println("one cpu found");
			}
			else if(threadValues.size()>1)
			{
				int sum=0,max=0;
				int start=Integer.parseInt(threadValues.get(0).trim());
				//System.out.println("two or more");
				for(int i=1;i<threadValues.size();i++)
				{
					if(Integer.parseInt(threadValues.get(i).trim())>max)
					{
						max=Integer.parseInt(threadValues.get(i).trim());
					}
					sum+=Integer.parseInt(threadValues.get(i).trim());
				}
				threaddetails.put("threadincrease",max-start);
				//threaddetails.put("threadavg",sum/threadValues.size());
			}
			//System.out.println(cpudetails);
			root.put("cpu", cpudetails);
			root.put("thread", threaddetails);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else{
			try {

				BufferedReader br=new BufferedReader(new FileReader(fileEntry));
				String currentLine=null;
				ArrayList<String> cpuValues=new ArrayList<String>();
				ArrayList<String> threadValues=new ArrayList<String>();
				boolean flag=false;
				int threadColumn=0;
				int cpuColumn=0;
				while((currentLine=br.readLine())!=null)
				{	
					/*if(currentLine.contains("CPU%")&&currentLine.contains("#THR")&&(!flag))
					{
						int count=1;
						StringTokenizer st=new StringTokenizer(currentLine.trim());
						while(st.hasMoreTokens())
						{
							String tem=st.nextToken();
							if(tem.equals("CPU%"))
							{

								cpuColumn=count;
							}
							if(tem.equals("#THR"))
							{

								threadColumn=count;
							}
							count++;
						}

					}*/
					//System.out.println(cpuColumn+":"+threadColumn);
					if(currentLine.contains(this.appPackageName.substring(0,12)))
					{
						int count=1;
						StringTokenizer st=new StringTokenizer(currentLine.trim());
						while(st.hasMoreTokens())
						{
							String temp=st.nextToken();
							//System.out.print(temp+" ");

							if(count==9)
							{
								float temp1=Float.parseFloat(temp)/8;
								cpuValues.add(String.format("%.2f", temp1));
							}
							/*if(count==threadColumn)
							{
								threadValues.add(temp);
							}*/
							count++;
						}
						//System.out.println();



						//System.out.println(currentLine);
						//StringTokenizer st=new StringTokenizer(currentLine.trim());
						/*st.nextToken();
						st.nextToken();
						cpuValues.add(st.nextToken());
						st.nextToken();
						threadValues.add(st.nextToken());*/
						//System.out.println((st.nextToken()).nex);

					}

				}

				JSONObject cpudetails=new JSONObject();
				//JSONObject threaddetails=new JSONObject();

				if(cpuValues.size()==0)
				{
					//System.out.println("no cpu found");
					cpudetails.put("cpuincrease",0);
					cpudetails.put("cpumax",0);
					//cpudetails.put("cpuavg",0);

				}
				else if(cpuValues.size()==1)
				{
					cpudetails.put("cpuincrease",cpuValues.get(0).trim());
					cpudetails.put("cpumax",cpuValues.get(0).trim());
					//cpudetails.put("cpuavg",cpuValues.get(0));
					//System.out.println("one cpu found");
				}
				else if(cpuValues.size()>1)
				{
					float sum=0,max=0;
					//System.out.println("two or more");
					float start=Float.parseFloat(cpuValues.get(0).trim());

					for(int i=1;i<cpuValues.size();i++)
					{
						if(Float.parseFloat(cpuValues.get(i).trim())>max)
						{
							max=Float.parseFloat(cpuValues.get(i).trim());
						}
						sum+=Float.parseFloat(cpuValues.get(i).trim());
					}
					cpudetails.put("cpuincrease",String.format("%.2f", max-start));
					cpudetails.put("cpumax",max);
					//System.out.println(start+":::"+max);
					//cpudetails.put("cpuavg",sum/cpuValues.size());
				}




				/*if(threadValues.size()==0)
				{
					//System.out.println("no cpu found");
					threaddetails.put("threadincrease",0);
					//threaddetails.put("threadavg",0);

				}
				else if(threadValues.size()==1)
				{
					threaddetails.put("threadincrease",threadValues.get(0));
					//threaddetails.put("threadavg",threadValues.get(0));
					//threaddetails.out.println("one cpu found");
				}
				else if(threadValues.size()>1)
				{
					int sum=0,max=0;
					int start=Integer.parseInt(threadValues.get(0).trim());
					//System.out.println("two or more");
					for(int i=1;i<threadValues.size();i++)
					{
						if(Integer.parseInt(threadValues.get(i).trim())>max)
						{
							max=Integer.parseInt(threadValues.get(i).trim());
						}
						sum+=Integer.parseInt(threadValues.get(i).trim());
					}
					threaddetails.put("threadincrease",max-start);
					//threaddetails.put("threadavg",sum/threadValues.size());
				}
				//System.out.println(cpudetails);*/
				root.put("cpu", cpudetails);
				//root.put("thread", threaddetails);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return root;
	}

	private  JSONObject findDeltaGPU(File fileEntry, String currentDirectory) {
		JSONObject root=new JSONObject();
		String gpuframe[]=null;
		String gpujanks[]=null;
		try {

			BufferedReader br=new BufferedReader(new FileReader(fileEntry));
			String currentLine=null;
			while((currentLine=br.readLine())!=null)
			{
				if(currentLine.contains("Total frames rendered:"))
					gpuframe=currentLine.split(":");
				if(currentLine.contains("Janky frames:"))
				{
					gpujanks=currentLine.split(":");
				}
			}
			//System.out.println(currentLine);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(gpuframe==null&&gpujanks==null)
			return null;

		root.put("gpuframes", gpuframe[1]);
		String[] temp=gpujanks[1].trim().split("\\s");
		root.put("gpujanks", temp[0]);
		return root;
	}
	private  JSONObject findDeltaMemory(File fileEntry, String currentDirectory) {
		ArrayList<String> parsedMemory = new ArrayList<String>();
		JSONObject root=new JSONObject();
		JSONObject memorydetails=new JSONObject();
		try {

			BufferedReader br1=new BufferedReader(new FileReader(fileEntry));
			String sCurrentLine=null;

			while ((sCurrentLine = br1.readLine()) != null) {
				if (sCurrentLine.contains("TOTAL"))
				{
					StringTokenizer sToken = new StringTokenizer(sCurrentLine, " ");
					ArrayList<String> orginalSequence = new ArrayList();
					while (sToken.hasMoreTokens()) {
						orginalSequence.add(sToken.nextToken());
					}
					parsedMemory.add((String)orginalSequence.get(1));
				}
			}
			if(parsedMemory.size()==0)
			{
				//System.out.println("no cpu found");
				memorydetails.put("memoryincrease",0);
				memorydetails.put("memorymax",0);
				//memorydetails.put("memoryavg",0);

			}
			else if(parsedMemory.size()==1)
			{
				memorydetails.put("memoryincrease",parsedMemory.get(0));
				memorydetails.put("memorymax",parsedMemory.get(0));
				//memorydetails.put("memoryavg",parsedMemory.get(0));
				//threaddetails.out.println("one cpu found");
			}
			else if(parsedMemory.size()>1)
			{
				int sum=0,max=0;
				//System.out.println("two or more");
				int start=Integer.parseInt(parsedMemory.get(0).trim());
				for(int i=1;i<parsedMemory.size();i++)
				{
					if(Integer.parseInt(parsedMemory.get(i).trim())>max)
					{
						max=Integer.parseInt(parsedMemory.get(i).trim());
					}
					sum+=Integer.parseInt(parsedMemory.get(i).trim());
				}
				memorydetails.put("memoryincrease",max-start);
				memorydetails.put("memorymax",max);
				//memorydetails.put("memoryavg",sum/parsedMemory.size());
			}
			//System.out.println(cpudetails);
			root.put("memory", memorydetails);
			//root.put("thread", threaddetails);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return root;
	}private  JSONObject findDeltaNetwork(File fileEntry, String currentDirectory) {
		JSONObject networkDetails=new JSONObject();
		try {

			BufferedReader br1=new BufferedReader(new FileReader(fileEntry));
			String ScurrentLine = null;

			int uidindex = 0;int rxBytesIndex = 0;int txBytesIndex = 0;
			ArrayList<String> txBytesFull = new ArrayList();
			ArrayList<String> rxBytesFull = new ArrayList();
			while ((ScurrentLine = br1.readLine()) != null)
			{
				//System.out.println(ScurrentLine);
				if (ScurrentLine.contains("uid_tag_int"))
				{
					// System.out.println(ScurrentLine);
					int counter = 0;
					StringTokenizer st = new StringTokenizer(ScurrentLine);
					while (st.hasMoreTokens())
					{
						counter++;
						String current1 = st.nextToken();
						// System.out.println("countercurrent1********:"+current1);
						if (current1.trim().equals("uid_tag_int")) {

							uidindex = counter;

						}
						if (current1.trim().equals("rx_bytes")) {
							// System.out.println("counter********:"+counter);
							rxBytesIndex = counter;
						}
						if (current1.trim().equals("tx_bytes")) {
							//System.out.println("counter********:"+counter);
							txBytesIndex = counter;
						}
					}
				}
				// System.out.println("counteruidindex********:"+uidindex);
				//System.out.println(uidindex+":"+rxBytesIndex+":"+txBytesIndex);
				if (ScurrentLine.contains(uid))
				{
					// System.out.println("uid********:"+uid);
					int counter = 0;
					StringTokenizer st = new StringTokenizer(ScurrentLine);
					boolean pointLine = false;
					while (st.hasMoreTokens())
					{
						counter++;
						String current1 = st.nextToken();
						if ((current1.trim().equals(uid)) && (counter == uidindex)) {
							pointLine = true;
						}
						if ((counter == rxBytesIndex) && (pointLine)) {
							rxBytesFull.add(current1.trim());
							//System.out.println(current1.trim()+"_-*-_");
						}
						if ((counter == txBytesIndex) && (pointLine)) {
							txBytesFull.add(current1.trim());
							//System.out.println(current1.trim()+"_-&-_");
						}
					}
				}
			}
			// System.out.println("rxBytesFull"+rxBytesFull);
			// System.out.println("txBytesFull"+txBytesFull);
			ArrayList<String> txBytesFull1 = new ArrayList();
			ArrayList<String> rxBytesFull1 = new ArrayList();
			for(int i=1;i<rxBytesFull.size();i++){
				if((rxBytesFull.get(i-1)==rxBytesFull.get(i))&&(txBytesFull.get(i-1)==txBytesFull.get(i))){
					//rxBytesFull1.add();
				}else{
					System.out.println(rxBytesFull.get(i)+":"+txBytesFull.get(i));
					rxBytesFull1.add(rxBytesFull.get(i));
					txBytesFull1.add(txBytesFull.get(i));
				}

			}



			if(txBytesFull1.size()==0)
			{
				//System.out.println("no cpu found");
				networkDetails.put("txincrease",0);
				//networkDetails.put("txavg",0);

			}
			else if(txBytesFull1.size()==1)
			{
				networkDetails.put("txincrease",txBytesFull1.get(0));
				//networkDetails.put("txavg",txBytesFull1.get(0));
				//threaddetails.out.println("one cpu found");
			}
			else if(txBytesFull1.size()>1)
			{
				int sum=0,max=0;
				int start=Integer.parseInt(txBytesFull1.get(0).trim());
				//System.out.println("two or more");
				for(int i=1;i<txBytesFull1.size();i++)
				{
					if(Integer.parseInt(txBytesFull1.get(i).trim())>max)
					{
						max=Integer.parseInt(txBytesFull1.get(i).trim());
					}
					sum+=Integer.parseInt(txBytesFull1.get(i).trim());
				}
				networkDetails.put("txincrease",max);
				//networkDetails.put("txavg",sum/txBytesFull1.size());
			}

			if(rxBytesFull1.size()==0)
			{
				//System.out.println("no cpu found");
				networkDetails.put("rxincrease",0);
				//networkDetails.put("rxavg",0);

			}
			else if(rxBytesFull1.size()==1)
			{
				networkDetails.put("rxincrease",rxBytesFull1.get(0));
				//networkDetails.put("rxavg",rxBytesFull1.get(0));
				//threaddetails.out.println("one cpu found");
			}
			else if(rxBytesFull1.size()>1)
			{
				int sum=0,max=0;
				//System.out.println("two or more");
				int start=Integer.parseInt(rxBytesFull1.get(0).trim());
				for(int i=0;i<rxBytesFull1.size();i++)
				{
					if(Integer.parseInt(rxBytesFull1.get(i).trim())>max)
					{
						max=Integer.parseInt(rxBytesFull1.get(i).trim());
					}
					sum+=Integer.parseInt(rxBytesFull1.get(i).trim());
				}
				networkDetails.put("rxincrease",max-start);
				//networkDetails.put("rxavg",sum/rxBytesFull1.size());
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject root=new JSONObject();
		root.put("network", networkDetails);
		return root;
	}




}
