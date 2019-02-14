import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.math.*;

import org.json.simple.JSONObject;

public class Check {

	String appPackageName ="com.brand";
	
	/*public static void main(String a[]){
        File file = new File("C:/MyFolder/");
        String[] fileList = file.list();
        for(String name:fileList){
            System.out.println(name);
        }
    }*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*String ftxt="D:\\Development_Avecto\\platform-tools\\platform-tools\\Topcommand.txt";
		Check chk =new Check();
		System.out.print(chk.findDeltaCpu(new File(ftxt), "D:\\Development_Avecto\\platform-tools\\platform-tools\\").toJSONString());*/
		 
		File file = new File("C:/Users/303108/workspace/AndroidApp/lib/");
	        String[] fileList = file.list();
	        for(String name:fileList){
	        	File file1 = new File("C:/Users/303108/workspace/AndroidApp/lib/"+name);
	            System.out.println(file1.getAbsolutePath()+"\t"+Math.round(file1.length()/1024));
	        }

	}
	
	private  JSONObject findDeltaCpu(File fileEntry, String currentDirectory) {
		/*System.out.println("test1");*/
		JSONObject root=new JSONObject();
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
				if(currentLine.contains(this.appPackageName))
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

		return root;
	}

	

}
