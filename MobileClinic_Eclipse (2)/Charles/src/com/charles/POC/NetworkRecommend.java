package com.charles.POC;

import java.io.FileReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkRecommend {
	public int totalrequests;

	public Map<String, List<String>> condition1 = new HashMap<String, List<String>>();

	public List<String> condition2 = new ArrayList<String>();

	public List<String> condition3 = new ArrayList<String>();

	public Map<String, List<String>> errorurls = new HashMap<String, List<String>>();

	public Map<String, List<String>> cssurls = new HashMap<String, List<String>>();
	public Map<String, List<String>> jsurls = new HashMap<String, List<String>>();

	public List<String> timetakingurls = new ArrayList<String>();

	public static JSONArray NetRecommend(String harfile) {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		JSONArray array = new JSONArray();
		try {

			Object obj = parser.parse(new FileReader(harfile));

			JSONObject jsonObject = (JSONObject) obj;

			// //String name = (String) jsonObject.get("Name");
			// String author = (String) jsonObject.get("Author");
			// //JSONArray companyList = (JSONArray)
			// jsonObject.get("Company List");

			JSONObject log = (JSONObject) jsonObject.get("log");
			JSONArray entries = (JSONArray) log.get("entries");

			NetworkRecommend t1 = new NetworkRecommend();
			t1.totalrequests = entries.size();

			JSONObject rule1 = new JSONObject();
			JSONObject recommendation = new JSONObject();
			rule1.put("ruleHeader", "Validate no. of requests in a page");
			if (t1.totalrequests >= 10) {
				rule1.put(
						"Message",
						"Total no. of requests in the page:"
								+ t1.totalrequests
								+ ".,Consider reducing total no. of resources getting downloaded");
				rule1.put(
						"Recommendation",
						"If possible combine multiple js/css files from same domain to single js/css. CSS spriting for images also reduces the no. of network calls");
			} else {
				rule1.put(
						"Message",
						"Total no. of requests in the page:"
								+ t1.totalrequests
								+ ".,No. of requests per page is within the industry standard");
				rule1.put("Recommendation", "none");
			}
			array.add(rule1);

			System.out.println(array);
			JSONObject rule2 = new JSONObject();

			t1.condition1 = t1.checkcachecontrol(entries);
			System.out.println("Total No. Of Requests in the page:"
					+ t1.totalrequests);

			System.out.println("Rule 1 Cache Control:");

			rule2.put("ruleHeader", "Leverage Browsing Cache");
			int chk = 0;
			String message;
			message = "";
			if (!t1.condition1.get("Expiry").isEmpty()) {
				System.out
						.println("Expires Header is not mentioned for the below resources"
								+ t1.condition1.get("Expiry"));
				// rule2.put("Expiries url", t1.condition1.get("Expiry"));
				message = "Url's without any expiry header:,"
						+ t1.condition1.get("Expiry").toString().substring(1)
								.replaceFirst("]", "") + ",";
				chk = 1;
			}

			if (!t1.condition1.get("CacheControl").isEmpty()) {
				System.out
						.println("Cache Control Header is not mentioned for the below resources"
								+ t1.condition1.get("CacheControl"));
				// rule2.put("CacheControl url",
				// t1.condition1.get("CacheControl"));
				message = message
						+ "Url's without cache control header:,"
						+ t1.condition1.get("CacheControl").toString()
								.substring(1).replaceFirst("]", "") + ",";
				chk = 1;
			}
			if (!t1.condition1.get("CacheStatus").isEmpty()) {
				System.out
						.println("Below resources are having 304 as status code"
								+ t1.condition1.get("CacheStatus"));
				message += "Url's 304 status:,"
						+ t1.condition1.get("CacheStatus").toString()
								.substring(1).replaceFirst("]", "") + "\n";
				chk = 1;
			}
			if (chk == 0) {
				rule2.put("Message", "none");
				rule2.put("Recommendation", "none");
			} else {
				System.out.println("Check" + message);
				rule2.put("Message", message);
				rule2.put(
						"Recommendation",
						"For having a good caching startegy it is recoomended to have cache control and expires header for all the resources, Also as a best practice it is recommended that no resources to get 304 status");
			}
			array.add(rule2);

			t1.condition2 = t1.findCompression(entries);

			JSONObject rule3 = new JSONObject();

			rule3.put("ruleHeader", "Apply Compression Technique");

			System.out.println("Rule 2 Compression Check:");
			if (!t1.condition2.isEmpty()) {
				System.out
						.println("Compression is not applied to below resources:"
								+ t1.condition2);
				rule3.put("Message",
						"No compression methodologies has been applied for the below URL's:,"
								+ t1.condition2.toString().substring(1)
										.replaceFirst("]", ""));
				rule3.put(
						"Recommendation",
						"It is recommended to apply gzip/deflate/br compression techniques to the resources by which we can minimize the amount of data getting transferred");

			} else {
				rule3.put("Message", "none");
				rule3.put("Recommendation", "none");

			}
			array.add(rule3);

			t1.condition3 = t1.findDuplicates(entries);

			JSONObject rule4 = new JSONObject();

			rule4.put("ruleHeader", "Avoid Duplicate calls");
			message = "";
			System.out.println("Rule 3 Duplicate calls in the page:");
			if (!t1.condition3.isEmpty()) {
				System.out
						.println("Below duplicate calls are observed in the page:"
								+ t1.condition3);
				rule4.put("Message",
						"Below duplicate calls were observed:,"
								+ t1.condition3.toString().substring(1)
										.replaceFirst("]", ""));
				rule4.put("Recommendation",
						"Duplicate call needs to be avoided. Remove unnecessary network calls");
			} else {
				rule4.put("Message", "none");
				rule4.put("Recommendation", "none");
			}

			array.add(rule4);

			t1.errorurls = t1.errorenousurls(entries);
			JSONObject rule5 = new JSONObject();

			rule5.put("ruleHeader", "Errorneous Requests");

			List<String> list400 = new ArrayList<String>();
			List<String> list302 = new ArrayList<String>();

			list400 = t1.errorurls.get("404");
			list302 = t1.errorurls.get("302");

			System.out.println("Rule 4 Errorneous requests:");
			if (!list400.isEmpty()) {
				System.out
						.println("Below errorenous requests(400/404) were observed:,"
								+ list400);
				rule5.put(
						"Message",
						"Below resources have status code 400/404:,"
								+ list400.toString().substring(1)
										.replaceFirst("]", ""));
				rule5.put("Recommendation",
						"Resolve 400/404 resources else remove the unwanted calls");
			} else {
				rule5.put("Message", "none");
				rule5.put("Recommendation", "none");
				System.out.println("No errorenous requests were observed");
			}

			array.add(rule5);

			System.out.println("Rule 5 Avoid redirects:");

			JSONObject rule6 = new JSONObject();

			rule6.put("ruleHeader", "Avoid Redirects");

			if (!list302.isEmpty()) {
				System.out
						.println("Below requests with 302 status code were observed:"
								+ list302);
				rule6.put(
						"Message",
						"Status code 302 was observed for the url's:, "
								+ list302.toString().substring(1)
										.replaceFirst("]", ""));
				rule6.put(
						"Recommendation",
						"Provide direct url to the resource which will reduce the unwanted roundtrip of network call");
			} else {
				System.out.println("No redirects were observed in the page");
				rule6.put("Message", "none");
				rule6.put("Recommendation", "none");
			}
			array.add(rule6);

			t1.timetakingurls = t1.timeconsuming(entries);

			JSONObject rule7 = new JSONObject();

			rule7.put("ruleHeader", "Server time consuming");

			if (!t1.timetakingurls.isEmpty()) {
				rule7.put("Message",
						"Response time for the below individual request is over 500ms:, "
								+ t1.timetakingurls.toString().substring(1)
										.replaceFirst("]", ""));
				rule7.put(
						"Recommendation",
						"The requests seems to be time consuming from server/network side. This needs to be profiled");
			} else {
				rule7.put("Message", "none");
				rule7.put("Recommendation", "none");
			}
			array.add(rule7);

			t1.cssurls = t1.getDomainurls(entries, ".css");

			JSONObject rule8 = new JSONObject();

			rule8.put("ruleHeader", "Combine CSS and JS");

			message = "";
			chk = 0;
			for (String key : t1.cssurls.keySet()) {
				if (t1.cssurls.get(key).size() > 1) {
					chk = 1;
					message += "Below urls from the domain-"
							+ key
							+ "are the candidates for merging css:,"
							+ t1.cssurls.get(key).toString().substring(1)
									.replaceFirst("]", "") + ",";
				}
			}

			t1.jsurls = t1.getDomainurls(entries, ".js");

			int chk1 = 0;
			for (String key : t1.jsurls.keySet()) {
				if (t1.jsurls.get(key).size() > 1) {
					chk1 = 1;
					message += "Below urls from the domain-"
							+ key
							+ "are the candidates for merging js:,"
							+ t1.jsurls.get(key).toString().substring(1)
									.replaceFirst("]", "") + ",";
				}
			}

			if (chk == 1 || chk1 == 1) {
				rule8.put("Message", message);
				rule8.put(
						"Recommendation",
						"Combine the candidate files into a single file or lesser multiple files which would reduce the no. of network calls in the page");
			} else {
				rule8.put("Message", "none");
				rule8.put("Recommendation", "none");
			}
			array.add(rule8);
			recommendation.put("recommendation", array);
			System.out.println(recommendation);

			// JSONObject chk = (JSONObject) entries.get(0);

			// System.out.println(chk.get("request"));
			// System.out.println("Author: " + author);
			// System.out.println("\nCompany List:");
			// Iterator<String> iterator = companyList.iterator();
			// while (iterator.hasNext()) {
			// System.out.println(iterator.next());
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;

	}

	public Map<String, List<String>> checkcachecontrol(JSONArray b1) {
		int size = b1.size();
		List<String> cacheContorlUrl = new ArrayList<String>();
		List<String> expiryUrl = new ArrayList<String>();
		List<String> wrongcachestatus = new ArrayList<String>();

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		String url;

		for (int i = 0; i < size; i++) {
			JSONObject chk = (JSONObject) b1.get(i);

			JSONObject request = (JSONObject) chk.get("request");

			url = request.get("url").toString();

			JSONObject response = (JSONObject) chk.get("response");

			// System.out.println(response.get("status").toString());

			if (response.get("status").toString().contains("304")) {
				wrongcachestatus.add(url);
			}

			JSONArray headers = (JSONArray) response.get("headers");

			// System.out.println(headers);

			int c1 = 0;

			for (int j = 0; j < headers.size(); j++) {
				if (headers.get(j).toString().contains("Cache-Control")) {
					c1 = 1;
					break;
				}
			}

			if (c1 == 0) {
				cacheContorlUrl.add(url);
			}

			int c2 = 0;

			for (int j = 0; j < headers.size(); j++) {
				if (headers.get(j).toString().contains("Expires")) {
					c2 = 1;
					break;
				}
			}

			if (c2 == 0) {
				expiryUrl.add(url);
			}

		}

		// System.out.println(expiryUrl);
		// System.out.println(cacheContorlUrl);
		map.put("Expiry", expiryUrl);
		map.put("CacheControl", cacheContorlUrl);
		map.put("CacheStatus", wrongcachestatus);
		return map;
	}

	public List<String> findCompression(JSONArray b1) {
		int size = b1.size();
		List<String> compressionUrl = new ArrayList<String>();

		String url;

		for (int i = 0; i < size; i++) {
			JSONObject chk = (JSONObject) b1.get(i);

			JSONObject request = (JSONObject) chk.get("request");

			url = request.get("url").toString();

			if (!url.contains(".png") && !url.contains(".gif")
					&& !url.contains(".jpeg") && !url.contains(".jpg")) {
				JSONObject response = (JSONObject) chk.get("response");

				// System.out.println(response.get("status").toString());

				JSONArray headers = (JSONArray) response.get("headers");

				// System.out.println(headers);

				int c1 = 0;

				for (int j = 0; j < headers.size(); j++) {
					if (headers.get(j).toString().contains("Content-Encoding")) {
						c1 = 1;
						break;
					}
				}

				if (c1 == 0) {
					compressionUrl.add(url);
				}

			}
		}

		return compressionUrl;
	}

	public List<String> findDuplicates(JSONArray b1) {
		int size = b1.size();
		List<String> duplicateUrl = new ArrayList<String>();

		Map<String, String> urlwithsize = new HashMap<String, String>();

		String url;

		for (int i = 0; i < size; i++) {
			JSONObject chk = (JSONObject) b1.get(i);

			JSONObject request = (JSONObject) chk.get("request");

			url = request.get("url").toString();

			JSONObject response = (JSONObject) chk.get("response");

			// System.out.println(response.get("bodySize").toString());

			if (urlwithsize.containsKey(url)) {
				if (response.get("bodySize").toString()
						.equalsIgnoreCase(urlwithsize.get(url))) {
					duplicateUrl.add(url);
				}
			} else {
				urlwithsize.put(url, response.get("bodySize").toString());
			}

		}

		return duplicateUrl;
	}

	public Map<String, List<String>> errorenousurls(JSONArray b1) {
		int size = b1.size();
		List<String> url302 = new ArrayList<String>();
		List<String> url404 = new ArrayList<String>();

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		String url;

		for (int i = 0; i < size; i++) {
			JSONObject chk = (JSONObject) b1.get(i);

			JSONObject request = (JSONObject) chk.get("request");

			url = request.get("url").toString();

			JSONObject response = (JSONObject) chk.get("response");

			// System.out.println(response.get("status").toString());

			if (response.get("status").toString().contains("302")) {
				url302.add(url);
			}

			if (response.get("status").toString().contains("400")
					|| response.get("status").toString().contains("404")) {
				url404.add(url);
			}

		}
		map.put("302", url302);
		map.put("404", url404);
		return map;

	}

	public List<String> timeconsuming(JSONArray b1) {
		int size = b1.size();
		List<String> timeconurl = new ArrayList<String>();

		String url;

		for (int i = 0; i < size; i++) {
			JSONObject chk = (JSONObject) b1.get(i);

			JSONObject request = (JSONObject) chk.get("request");

			url = request.get("url").toString();

			float f = Float.parseFloat(chk.get("time").toString());
			// System.out.println(f);
			if (f > 500) {
				timeconurl.add(url);
				// System.out.println(url);

			}

		}

		return timeconurl;
	}

	public Map<String, List<String>> getDomainurls(JSONArray b1, String str) {
		int size = b1.size();

		// List<String> expiryUrl = new ArrayList<String>();
		// List<String> wrongcachestatus = new ArrayList<String>();

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		String url;

		for (int i = 0; i < size; i++) {
			JSONObject chk = (JSONObject) b1.get(i);

			JSONObject request = (JSONObject) chk.get("request");

			url = request.get("url").toString();

			if (url.endsWith(str)) {
				String domain;
				String[] split = url.split("/");
				domain = split[2];
				if (map.containsKey(domain)) {
					List<String> content = new ArrayList<String>();
					content = map.get(domain);
					content.add(url);
					map.put(domain, content);
				} else {
					List<String> content = new ArrayList<String>();
					content.add(url);
					map.put(domain, content);

				}

			}
		}

		for (String key : map.keySet()) {
			System.out.println("Key:" + key + ";Value:" + map.get(key));
		}
		// System.out.println(expiryUrl);
		// System.out.println(cacheContorlUrl);

		return map;

	}

}
