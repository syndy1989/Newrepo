package com.android.app;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;





import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.PopupFactory;

import com.mobile.clinic.controller.SRDetails;
//import com.sun.glass.ui.Timer;

import java.awt.Button;
import java.awt.TextField;
import java.awt.Label;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRadioButton;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Panel;
import java.awt.List;
import java.awt.TextArea;

public class SampleMobileCalls {

	private JFrame frame;
	static long start;
	static long finish;
	static long totalTime;
	static SRDetails srdel=new SRDetails();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SampleMobileCalls window = new SampleMobileCalls();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SampleMobileCalls() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
        
    }
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(150, 250, 500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Button btnStartUC = new Button("Start");
		Button btnStopUC = new Button("Stop");
		btnStartUC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*String filename=srdel.saveTemplate("clinicconfig.properties","C:\\Mobileclinique\\NFTMobile");
				
				String status=srdel.getSRRequesttotrigger();
				Properties properties=new Properties(); 
				FileInputStream propertyfile;*/
				
				
				//Make all the calls for Folder creation and Overall metrics collection
				//Code for sending an alert to user to Launch the app or click event
				
				try {
					Thread.sleep(0);
					
					infoBox("Click on OK and interact with your app simultaneously", "Alert");
					start=System.currentTimeMillis();
					btnStartUC.disable();
					btnStopUC.enable();					
					
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnStartUC.setBounds(274, 152, 70, 22);
		frame.getContentPane().add(btnStartUC);
		
		
		btnStopUC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				finish = System.currentTimeMillis();
				btnStopUC.disable();
				btnStartUC.enable();
				totalTime = (finish - start);
				System.out.println(totalTime);
			}
		});
		btnStopUC.setBounds(368, 151, 70, 22);
		frame.getContentPane().add(btnStopUC);
		
		TextField txtUsecaseName = new TextField();
		txtUsecaseName.setBounds(163, 152, 94, 22);
		frame.getContentPane().add(txtUsecaseName);
		
		Label lblUsecaseName = new Label("Usecase Name");
		lblUsecaseName.setBounds(20, 151, 94, 22);
		frame.getContentPane().add(lblUsecaseName);
		
		Label lblPageName = new Label("Page Name");
		lblPageName.setBounds(20, 197, 84, 22);
		frame.getContentPane().add(lblPageName);
		
		TextField txtPgName = new TextField();
		txtPgName.setBounds(163, 197, 94, 22);
		frame.getContentPane().add(txtPgName);
		
		Button btnStartPg = new Button("Start");
		btnStartPg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// show a pop up after statring individual collector
				// click proceed in popup and stasry the timer imidieatlty, do the action also and once done stop the timer.
				// 
			}
		});
		btnStartPg.setBounds(274, 197, 70, 22);
		frame.getContentPane().add(btnStartPg);
		
		Button btnStopPg = new Button("Stop");
		btnStopPg.setBounds(368, 196, 70, 22);
		frame.getContentPane().add(btnStopPg);
		
		Label lblPckgName = new Label("Package Name");
		lblPckgName.setBounds(20, 105, 94, 22);
		frame.getContentPane().add(lblPckgName);
		
		TextField txtPckgName = new TextField();
		txtPckgName.setBounds(163, 105, 94, 22);
		frame.getContentPane().add(txtPckgName);
		
		Label lblOS = new Label("Select OS");
		lblOS.setBounds(20, 28, 94, 22);
		frame.getContentPane().add(lblOS);
		
		JRadioButton rdbtnAndroid = new JRadioButton("Android");
		rdbtnAndroid.setBounds(163, 28, 77, 23);
		frame.getContentPane().add(rdbtnAndroid);
		
		JRadioButton rdbtnIos = new JRadioButton("iOS");
		rdbtnIos.setBounds(267, 28, 70, 23);
		frame.getContentPane().add(rdbtnIos);
		
		Label lblApp = new Label("App Name");
		lblApp.setBounds(20, 64, 94, 22);
		frame.getContentPane().add(lblApp);
		
		TextField textField = new TextField();
		textField.setBounds(163, 64, 94, 22);
		frame.getContentPane().add(textField);
	}
}
