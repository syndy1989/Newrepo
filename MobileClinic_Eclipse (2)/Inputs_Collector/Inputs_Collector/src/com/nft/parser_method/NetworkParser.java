package com.nft.parser_method;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class NetworkParser
{
	
	public static void main (String args[]) throws IOException, ParseException{
		
		NetworkParser tt=new NetworkParser();
		String filename="D:\\testing\\UseCase1\\App-Launch_uid.txt";
		String uid="10287";
		tt.parseNetworkToJSON(filename, uid);
		
	}
	
	
	public static JSONObject parseNetworkToJSON(String filename,String uid)
			throws IOException, ParseException
	{

		
						
		String sCurrentLine = null;


		ArrayList<JSONObject> parselist = new ArrayList();

		File cpuFile = new File(filename);
		System.out.println("netFile"+cpuFile);
		BufferedReader br1 = new BufferedReader(new FileReader(cpuFile));
		//  System.out.println("**uid:" + uid + "**");
		ArrayList<Long> listdata = new ArrayList();
		String ScurrentLine = null;

		int uidindex = 0;int rxBytesIndex = 0;int txBytesIndex = 0;
		ArrayList<String> txBytesFull = new ArrayList();
		ArrayList<String> rxBytesFull = new ArrayList();
		while ((ScurrentLine = br1.readLine()) != null)
		{

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
				rxBytesFull1.add(rxBytesFull.get(i));
				txBytesFull1.add(txBytesFull.get(i));
			}
		}

		System.out.println("rxBytesFull1"+rxBytesFull1);
		
		
		System.out.println("txBytesFull1"+txBytesFull1);
		
		JSONObject root = new JSONObject();
		JSONObject rootavgmax = new JSONObject();
		JSONObject finalval = new JSONObject();
		JSONObject networkdetails = new JSONObject();
		JSONObject networkdetails1 = new JSONObject();

		Long rxavg1 = Long.valueOf(0L);
		Long txavg1 = Long.valueOf(0L);
		for (int i = 0; i < rxBytesFull.size(); i++)
		{
			JSONObject current1 = new JSONObject();
			current1.put("rx", Long.parseLong((String)rxBytesFull.get(i)));

			current1.put("tx", Long.parseLong((String)txBytesFull.get(i)));

			root.put((i + 1) + "s", current1);
		}
		// System.out.print("averagevalue rootArray.sizes" + root.size());
		JSONObject avergajson = averagevalue(root);
		networkdetails.put("TotalTransfered", avergajson.get("totaltx"));
		networkdetails.put("TotalReceived", avergajson.get("totalrx"));
		finalval.put("Networkbytes",root);
		finalval.put("Averagentbytes",avergajson);
		finalval.put("TotalTransfered",avergajson.get("totaltx"));
		finalval.put("TotalReceived",avergajson.get("totalrx"));
		finalval.put("networkdetails",networkdetails);
		finalval.put("networkdetails1",networkdetails1);
		System.out.println("networkdetails::"+networkdetails);
		System.out.println("finalval::"+finalval);
		return finalval;
	}

	public static JSONObject averagevalue(JSONObject rootArray)
	{
		Long average = Long.valueOf(0L);
		Long txavg = Long.valueOf(0L);
		Long rxavg = Long.valueOf(0L);
		Long rxavg1 = Long.valueOf(0L);
		Long txavg1 = Long.valueOf(0L);
		JSONObject rootavgmax = new JSONObject();
		ArrayList<Long> listdatarx = new ArrayList();
		ArrayList<Long> listdatatx = new ArrayList();

		ArrayList<Long> listdata = new ArrayList();
		for (int i = 0; i < rootArray.size(); i++)
		{
			JSONObject st311 = (JSONObject)rootArray.get((i + 1) + "s");
			Long val = (Long)st311.get("rx");
			Long val1 = (Long)st311.get("tx");

			listdatarx.add(val);
			listdatatx.add(val1);
			rxavg = val+ rxavg;
			txavg = val1 + txavg;
		}
		//System.out.print("averagevalue rootArray.size" + rootArray.size());
		Long rxmax=0L;
		Long txmax =0L;
		if(rootArray.size()==0){
			rxavg1=0L;
			txavg1=0L;

		}else{
			rxavg1 = rxavg/ rootArray.size();
			txavg1 = txavg/ rootArray.size();
			rxmax = (Long)Collections.max(listdatarx);
			txmax = (Long)Collections.max(listdatatx);
		}

		rootavgmax.put("averagerx", rxavg1);
		rootavgmax.put("averagetx", txavg1);
		rootavgmax.put("maxrx", rxmax);
		rootavgmax.put("maxtx", txmax);
		rootavgmax.put("totalrx", rxavg);
		rootavgmax.put("totaltx", txavg);

		return rootavgmax;
	}
}

