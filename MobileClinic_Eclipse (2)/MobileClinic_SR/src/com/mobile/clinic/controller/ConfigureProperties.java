package com.mobile.clinic.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ConfigureProperties {

	public void updatePropertites(String propFileName, String reqId,
			File destinationFolder, File appLanuch, File template) {
		Properties properties = new Properties();
		FileInputStream propertiesFile, propertiesFile1;
		try {

			propertiesFile = new FileInputStream(propFileName);
			properties.load(propertiesFile);
			propertiesFile.close();

			properties.setProperty("requestID", reqId);

			properties.setProperty("filePath", appLanuch.getAbsolutePath()
					+ "\\");

			properties.setProperty("filePath1",
					destinationFolder.getAbsolutePath() + "\\");
			properties.setProperty("filePath2", template.getAbsolutePath()
					+ "\\");
			FileWriter fileOut = new FileWriter(propFileName);
			// fileOut.write(properties.);
			properties.store(fileOut, "Upadted Details");
			fileOut.close();

			propertiesFile1 = new FileInputStream(propFileName);
			properties.load(propertiesFile1);

			propertiesFile1.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void updatePropertitesForApp(String propFileName, String reqId,
			File destinationFolder, File appLanuch, File template,String appname, String packagename) {
		Properties properties = new Properties();
		FileInputStream propertiesFile, propertiesFile1;
		try {

			propertiesFile = new FileInputStream(propFileName);
			properties.load(propertiesFile);
			propertiesFile.close();

			properties.setProperty("packageName", packagename);
			properties.setProperty("appName", appname);
			properties.setProperty("requestID", reqId);

			properties.setProperty("filePath", appLanuch.getAbsolutePath()
					+ "\\");

			properties.setProperty("filePath1",
					destinationFolder.getAbsolutePath() + "\\");
			properties.setProperty("filePath2", template.getAbsolutePath()
					+ "\\");
			FileWriter fileOut = new FileWriter(propFileName);
			// fileOut.write(properties.);
			properties.store(fileOut, "Upadted Details");
			fileOut.close();

			propertiesFile1 = new FileInputStream(propFileName);
			properties.load(propertiesFile1);

			propertiesFile1.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/*
	 * public static void main(String[] args) { ConfigureProperties cs=new
	 * ConfigureProperties();
	 * 
	 * cs.updatePropertites(); }
	 */
}
