package com.android.app;
import com.android.app.androidStandaloneapp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Toolkit;
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Color;

public class MobileClinicAndroid {

	private JFrame frmMobileClinic;
	private JTextField textField;
	JLabel lblUsecase;
	private JButton btnStartUsecase;
	private JButton btnEndUsecase;
	JButton startExecution;
	private JButton btncompleteExecution;
	private JLabel lblEnterTransactionName;
	private JTextField textField_Transaction;
	private JButton btnstartTransaction;
	private JButton btnendTransaction;

	androidStandaloneapp capture;
	private JLabel lblSelectOs;
	private JTextField packageText;
	
	static long start;
	static long finish;
	static long startlaunch;
	static long finishlaunch;
	static long totalTime;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JLabel lblNewLabel_1;
	private JTextField appNameText;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MobileClinicAndroid window = new MobileClinicAndroid();
					window.frmMobileClinic.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MobileClinicAndroid() {
		initialize();
		
	}
	
	public void CenteredFrame(javax.swing.JFrame objFrame){
        Dimension objDimension = Toolkit.getDefaultToolkit().getScreenSize();
        int iCoordX = (objDimension.width - objFrame.getWidth()) / 2;
        int iCoordY = (objDimension.height - objFrame.getHeight()) / 2;
        objFrame.setLocation(iCoordX, iCoordY); 
    } 
	
	public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
        
    }
	
	
	public void reset()
	{
		capture = null;
		startExecution.setEnabled(true);
		
		textField.setVisible(false);
		lblUsecase.setVisible(false);
		btnStartUsecase.setVisible(false);
		btnEndUsecase.setVisible(false);
		btncompleteExecution.setVisible(false);
		lblEnterTransactionName.setVisible(false);
		textField_Transaction.setVisible(false);
		btnstartTransaction.setVisible(false);
		btnendTransaction.setVisible(false);
		packageText.setEnabled(true);
		packageText.setText("");
		appNameText.setEnabled(true);
		appNameText.setText("");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMobileClinic = new JFrame();
		frmMobileClinic.setTitle("Mobile Clinic");
		frmMobileClinic.setForeground(Color.RED);
		frmMobileClinic.setIconImage(Toolkit.getDefaultToolkit().getImage(MobileClinicAndroid.class.getResource("/com/android/app/mobile-icon-2.png")));
		frmMobileClinic.setBounds(100, 100, 474, 366);
		frmMobileClinic.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMobileClinic.getContentPane().setLayout(null);
		
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frmMobileClinic.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frmMobileClinic.getHeight()) / 2);
		frmMobileClinic.setLocation(x, y);
		
		startExecution = new JButton("Start Recording");
		startExecution.setBounds(136, 106, 158, 23);
		frmMobileClinic.getContentPane().add(startExecution);
		
		textField = new JTextField();
		textField.setBounds(136, 154, 114, 20);
		frmMobileClinic.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setVisible(false);
		
		lblUsecase = new JLabel("Enter Usecase Name");
		lblUsecase.setBounds(10, 144, 131, 40);
		frmMobileClinic.getContentPane().add(lblUsecase);
		lblUsecase.setVisible(false);
		
		btnStartUsecase = new JButton("Start");
		btnStartUsecase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(textField.getText().equals("App-Launch"))
				{
					capture.startAppLaunch();
					System.out.println("Check launch"+System.currentTimeMillis());
					
					infoBox("Click on OK and interact with your app simultaneously", "Alert");
					startlaunch = System.currentTimeMillis();
					btnStartUsecase.setEnabled(false);
					textField.setEnabled(false);
					btnEndUsecase.setEnabled(true);
				}
				else
				{
					if(textField.getText().contains(" ")||textField.getText().isEmpty())
					{
						infoBox("Enter Valid Usecase Name", "Alert");
					}
					else{
						
					
					capture.startMetricsCollector(textField.getText());
					textField_Transaction.setEnabled(true);
					textField_Transaction.setText(textField.getText()+"_");
					btnstartTransaction.setEnabled(true);
					btncompleteExecution.setEnabled(false);
					btnStartUsecase.setEnabled(false);
					textField.setEnabled(false);
					btnEndUsecase.setEnabled(true);
				}
				}
				
				
				
				
			}
		});
		btnStartUsecase.setBounds(284, 140, 114, 23);
		frmMobileClinic.getContentPane().add(btnStartUsecase);
		btnStartUsecase.setVisible(false);
		
		btnEndUsecase = new JButton("End");
		btnEndUsecase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(textField.getText().equals("App-Launch"))
				{
					finishlaunch = System.currentTimeMillis();
					totalTime = (finishlaunch - startlaunch);
					System.out.println("Start Launch:"+startlaunch);
					System.out.println("Finish Launch:"+finishlaunch);
					System.out.println("Launch Time:"+totalTime);					
					capture.stopAppLaunch(totalTime);
					lblUsecase.setText("Enter Usecase Name");
				}
				else
				{
					capture.stopMetricsCollector(textField.getText());
				}
				btncompleteExecution.setVisible(true);
				btncompleteExecution.setEnabled(true);
				btnStartUsecase.setEnabled(true);
				btnEndUsecase.setEnabled(false);
				btnstartTransaction.setEnabled(false);
				textField_Transaction.setEnabled(false);
				btnendTransaction.setEnabled(false);
				
					textField.setEnabled(true);
				
				textField.setText("");
			}
		});
		btnEndUsecase.setBounds(284, 174, 114, 23);
		frmMobileClinic.getContentPane().add(btnEndUsecase);
		btnEndUsecase.setVisible(false);
		
		
		btncompleteExecution = new JButton("Complete Recording");
		btncompleteExecution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				capture.stopExecution();
				reset();
			}
		});
		btncompleteExecution.setBounds(136, 293, 158, 23);
		frmMobileClinic.getContentPane().add(btncompleteExecution);
		btncompleteExecution.setVisible(false);
		
		lblEnterTransactionName = new JLabel("Enter Transaction Name");
		lblEnterTransactionName.setBounds(10, 249, 129, 14);
		frmMobileClinic.getContentPane().add(lblEnterTransactionName);
		lblEnterTransactionName.setVisible(false);
		
		textField_Transaction = new JTextField();
		textField_Transaction.setBounds(136, 246, 114, 20);
		frmMobileClinic.getContentPane().add(textField_Transaction);
		textField_Transaction.setColumns(10);
		textField_Transaction.setEnabled(false);
		textField_Transaction.setVisible(false);
		
		btnstartTransaction = new JButton("Start");
		btnstartTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField_Transaction.getText().contains(" ")||textField_Transaction.getText().isEmpty())
				{
					infoBox("Enter Valid Transaction Name", "Alert");
				}
				else
				{
				capture.startIndividualCollector(textField_Transaction.getText());
				btnstartTransaction.setEnabled(false);
				textField_Transaction.setEnabled(false);
				btnEndUsecase.setEnabled(false);
				btnendTransaction.setEnabled(true);
				btncompleteExecution.setEnabled(false);
				System.out.println("Check"+System.currentTimeMillis());
				infoBox("Click on OK and interact with your app simultaneously", "Alert");
				start = System.currentTimeMillis();
				}
			}
		});
		btnstartTransaction.setBounds(284, 225, 114, 23);
		frmMobileClinic.getContentPane().add(btnstartTransaction);
		btnstartTransaction.setEnabled(false);
		btnstartTransaction.setVisible(false);
		
		btnendTransaction = new JButton("End");
		btnendTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				finish = System.currentTimeMillis();
				totalTime = (finish - start);
				
				System.out.println("Start"+textField_Transaction.getText()+":"+start);
				System.out.println("Finish"+textField_Transaction.getText()+":"+finish);
				System.out.println("Total"+textField_Transaction.getText()+":"+totalTime);
				
				capture.stopIndividualCollector(textField_Transaction.getText(),totalTime);
				btnendTransaction.setEnabled(false);
				btnstartTransaction.setEnabled(true);
				btnEndUsecase.setEnabled(true);
				textField_Transaction.setEnabled(true);
				textField_Transaction.setText(textField.getText()+"_");
			}
		});
		btnendTransaction.setBounds(284, 259, 114, 23);
		frmMobileClinic.getContentPane().add(btnendTransaction);
		btnendTransaction.setEnabled(false);
		
		lblSelectOs = new JLabel("Select OS");
		lblSelectOs.setBounds(10, 20, 46, 14);
		frmMobileClinic.getContentPane().add(lblSelectOs);
		
		JRadioButton rdbtnAndroid = new JRadioButton("Android");
		buttonGroup.add(rdbtnAndroid);
		rdbtnAndroid.setSelected(true);
		rdbtnAndroid.setBounds(134, 16, 81, 23);
		frmMobileClinic.getContentPane().add(rdbtnAndroid);
		
		JRadioButton rdbtnIos = new JRadioButton("iOS");
		rdbtnIos.setEnabled(false);
		buttonGroup.add(rdbtnIos);
		rdbtnIos.setBounds(252, 16, 56, 23);
		frmMobileClinic.getContentPane().add(rdbtnIos);
		
		JLabel lblNewLabel = new JLabel("Package Name");
		lblNewLabel.setBounds(10, 78, 90, 14);
		frmMobileClinic.getContentPane().add(lblNewLabel);
		
		packageText = new JTextField();
		packageText.setBounds(136, 75, 114, 20);
		frmMobileClinic.getContentPane().add(packageText);
		packageText.setColumns(10);
		
		lblNewLabel_1 = new JLabel("App Name");
		lblNewLabel_1.setBounds(10, 45, 74, 14);
		frmMobileClinic.getContentPane().add(lblNewLabel_1);
		
		appNameText = new JTextField();
		appNameText.setBounds(136, 46, 114, 20);
		frmMobileClinic.getContentPane().add(appNameText);
		appNameText.setColumns(10);
		btnendTransaction.setVisible(false);
		
		
		
		
		startExecution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if(packageText.getText().contains(" ")||packageText.getText().isEmpty()||appNameText.getText().isEmpty())
				{
					infoBox("Enter Valid Package/App Name", "Alert");
				}
				else
				{
				capture = new androidStandaloneapp();
				
				if(capture.startExecution(appNameText.getText(),packageText.getText()))
				{
					lblUsecase.setText("Record App Launch");
					lblUsecase.setVisible(true);
					
					btnStartUsecase.setVisible(true);
					
					btnEndUsecase.setVisible(true);
					btnEndUsecase.setEnabled(false);
					
					textField.setVisible(true);
					textField.setText("App-Launch");
					textField.setEnabled(false);
					startExecution.setEnabled(false);
					lblEnterTransactionName.setVisible(true);
					textField_Transaction.setVisible(true);
					btnendTransaction.setVisible(true);
					btnstartTransaction.setVisible(true);
					btncompleteExecution.setVisible(true);
					btncompleteExecution.setEnabled(false);
					packageText.setEnabled(false);
					appNameText.setEnabled(false);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"An error has occured");
					reset();
				}
				
				}	
				
			}
		});
		
		
	}
}
