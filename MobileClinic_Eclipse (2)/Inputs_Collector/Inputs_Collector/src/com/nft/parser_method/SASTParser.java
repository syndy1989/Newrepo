package com.nft.parser_method;
import java.io.*;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/*import jdk.nashorn.internal.runtime.JSONListAdapter;*/
public class SASTParser
{
	
	public static void main(String args[]) throws Exception{
		String fileName="C:\\Users\\474614\\Desktop\\discover.txt";
		parse(fileName);
	}
                //private static final Logger LOGGER=Logger.getLogger(SASTParser.class);
                public static void parse(String fileName) throws IOException, ParseException
                {        

                                int readAheadLimit = 100000;
                                
                                File SrcFile1=new File(fileName);
                                
                                System.out.println(SrcFile1.exists());
                                //LOGGER.info(SrcFile1.exists());
                                //LOGGER.debug(SrcFile1.exists());
                                JSONArray rootobj=new JSONArray();

                                JSONArray newarray= null;
                                FileReader fr=new FileReader(SrcFile1);
                                BufferedReader br=new BufferedReader(fr);
                                String st;

                                String versvalue="";
                                String[] parts;
                                String part1 = "", part2 = "";
                                String packval="";
                                int count=0;
                                int next=0;
                                FileWriter dstfile = null;
                                JSONArray perm_arr=null;
                                JSONArray activity_arr=null;
                                while((st=br.readLine())!=null)
                                {
                                                JSONObject obj=new JSONObject();
                                                count = 0;
                                                /*perm_arr=new JSONArray();
                              activity_arr=new JSONArray();*/

                                                if(st.contains("[Critical]")||st.contains("[Warning]")||st.contains("[Notice]")||st.contains("[Info]"))
                                                { 
                                                                //System.out.println();
                                                                StringTokenizer strtkn=new StringTokenizer(st,":");
                                                                versvalue=strtkn.nextToken();
                                                                parts = versvalue.split("]");
                                                                part1 = parts[0];
                                                                part2 = parts[1];
                                                                part1 = part1.replace("[", "");
                                                                part2 = part2.replaceAll("<.*?>", "");
                                                                count = 1;   

                                                                //            System.out.println("version name::::"+versvalue);

                                                                //System.out.println("versnameversnameversname"+versname);
                                                }




                                                if(count==1)
                                                {
                                                                
                                                                newarray = new JSONArray();


                                                obj.put("Severity", part1);


                                                obj.put("Type", part2);


                                                while((st=br.readLine())!=null)
                                                {
                                                                next=1;
                                                                //System.out.println(st);

                                                                if(!(st.contains("[Critical]")||st.contains("[Warning]")||st.contains("[Notice]")||st.contains("[Info]")))
                                                                {

                                                                                
                                                                                StringTokenizer strtkn1=new StringTokenizer(st,"");
                                                                                if(strtkn1.countTokens()==0)
                                                                                                continue;
                                                                                packval = strtkn1.nextToken();
                                                                                
                                                                                packval = packval.trim();

                                                                                newarray.add(packval);


                                                                                br.mark(readAheadLimit);

                                                                                next=0;
                                                                }

                                                                if(next==1)
                                                                {
                                                                                br.reset();
                                                                                break;
                                                                }


                                                }
                                                obj.put("Reason", newarray);
                                                //System.out.println("obj"+obj);  
                                                rootobj.add(obj);
                                                System.out.println(rootobj);
                                                
                                                }
                                                


                                }
                             /*   BufferedReader br1=null;

                                FileReader fr1 = null;
                                try {
                                                fr1 = new FileReader(new File("tempfiles.json"));
                                } catch (FileNotFoundException e3) {
                                                // TODO Auto-generated catch block
                                                e3.printStackTrace();
                                                //LOGGER.info("error",e3);
                                                                                                                
                                }
                                br1 = new BufferedReader(fr1);


                                String current = "";
                                String json = "";

                                try {
                                                while ((current = br1.readLine()) != null)
                                                {
                                                                json = json + current;
                                                                //continue;
                                                }
                                } catch (IOException e2) {
                                                // TODO Auto-generated catch block
                                                e2.printStackTrace();
                                                //LOGGER.info("error",e2);
                                }
                                JSONParser jp = new JSONParser();
                                JSONObject        jObj = null ;
                                try {
                                                jObj = (JSONObject)jp.parse(json);
                                } catch (ParseException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                                //LOGGER.info("error",e);
                                }*/
                                dstfile=new FileWriter("D:\\testing\\NFTMobile\\security.json");
                                dstfile.write(((rootobj).toJSONString()));


                                dstfile.flush();
                                dstfile.close();

                                System.out.println("rootobj"+rootobj);
                                
                                fr.close();





                }
}
