package com.nft.parser_method;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.collect.Multiset.Entry;
//import com.immanuel.parsers.SASTParser;

public class SASTMetrics {
                //private static final Logger LOGGER=Logger.getLogger(SASTMetrics.class);
                public static void startSast() throws IOException, InterruptedException, ParseException {

      
                                Process p;
                                System.out.println("--------------------------Running NFT SAST Analyser-----------------------");
                               
                               // System.out.println("androbugs.py -f \""+jObj.get("FolderPath")+"\\"+jObj.get("APKFileName")+"\" -o \""+jObj.get("FolderPath")+"\"\\Security_DST");
                                String source="C:\\Users\\474614\\Documents\\discoverLaunch-debug.apk";
                                String destination="C:\\Users\\474614\\Documents\\";
                               
                                p = Runtime.getRuntime().exec("cmd /c start  /wait androbugs.py -f \""+ source+"\" -o \""+destination+ "  & exit");
                                p.waitFor();
                                System.out.println("Finished Running Sast");
                               
                                File f=new File(destination);

                                for(File eachEntry : f.listFiles())
                                {
                                                System.out.println("checking each files");
                                              
                                                System.out.println(eachEntry.getName()+":"+eachEntry.getAbsolutePath());
                                           

                                                SASTParser.parse(eachEntry.getAbsolutePath());


                                                System.out.println("del /f   "+eachEntry.getAbsolutePath());
                                               

                                                p = Runtime.getRuntime().exec("cmd /c start  /wait  test.bat  "+eachEntry.getAbsolutePath()+"");
                                                Thread.sleep(1000);
                                                if(p.isAlive())
                                                                p.destroyForcibly();


                                                p.waitFor();

                                }


                                System.out.println("---------------------completed---------------------------");
                              





                }

}
