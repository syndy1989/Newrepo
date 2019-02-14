import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class MobileClinicAndroid {

	private JFrame frame;
	private JTextField textField;
	JLabel lblUsecase;
	private JButton btnStartUsecase;
	private JButton btnEndUsecase;
	JButton startExecution;
	private JButton btncompleteExecution;
	private JLabel lblEnterTransactionName;
	private JTextField textField_1;
	private JButton btnstartTransaction;
	private JButton btnendTransaction;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MobileClinicAndroid window = new MobileClinicAndroid();
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
	public MobileClinicAndroid() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 322);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		startExecution = new JButton("Start Execution");
		startExecution.setBounds(136, 41, 158, 23);
		frame.getContentPane().add(startExecution);
		
		textField = new JTextField();
		textField.setBounds(151, 90, 114, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setVisible(false);
		
		lblUsecase = new JLabel("Enter Usecase Name");
		lblUsecase.setBounds(10, 80, 131, 40);
		frame.getContentPane().add(lblUsecase);
		lblUsecase.setVisible(false);
		
		btnStartUsecase = new JButton("Start UseCase");
		btnStartUsecase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStartUsecase.setEnabled(false);
				textField.setEnabled(false);
				btnEndUsecase.setEnabled(true);
				textField_1.setEnabled(true);
				btnstartTransaction.setEnabled(true);
				btncompleteExecution.setEnabled(false);
			}
		});
		btnStartUsecase.setBounds(284, 73, 114, 23);
		frame.getContentPane().add(btnStartUsecase);
		btnStartUsecase.setVisible(false);
		
		btnEndUsecase = new JButton("End Usecase");
		btnEndUsecase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btncompleteExecution.setVisible(true);
				btncompleteExecution.setEnabled(true);
				btnStartUsecase.setEnabled(true);
				btnEndUsecase.setEnabled(false);
				btnstartTransaction.setEnabled(false);
				textField_1.setEnabled(false);
				btnendTransaction.setEnabled(false);
				
					textField.setEnabled(true);
				
				textField.setText("");
			}
		});
		btnEndUsecase.setBounds(284, 107, 114, 23);
		frame.getContentPane().add(btnEndUsecase);
		btnEndUsecase.setVisible(false);
		
		
		btncompleteExecution = new JButton("Complete Execution");
		btncompleteExecution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startExecution.setVisible(true);
				textField.setVisible(false);
				lblUsecase.setVisible(false);
				btnStartUsecase.setVisible(false);
				btnEndUsecase.setVisible(false);
				btncompleteExecution.setVisible(false);
				lblEnterTransactionName.setVisible(false);
				textField_1.setVisible(false);
				btnstartTransaction.setVisible(false);
				btnendTransaction.setVisible(false);
			}
		});
		btncompleteExecution.setBounds(136, 227, 158, 23);
		frame.getContentPane().add(btncompleteExecution);
		btncompleteExecution.setVisible(false);
		
		lblEnterTransactionName = new JLabel("Enter Transaction Name");
		lblEnterTransactionName.setBounds(10, 163, 129, 14);
		frame.getContentPane().add(lblEnterTransactionName);
		lblEnterTransactionName.setVisible(false);
		
		textField_1 = new JTextField();
		textField_1.setBounds(151, 160, 114, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		textField_1.setEnabled(false);
		textField_1.setVisible(false);
		
		btnstartTransaction = new JButton("Start Transaction");
		btnstartTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnstartTransaction.setEnabled(false);
				textField_1.setEnabled(false);
				btnEndUsecase.setEnabled(false);
				btnendTransaction.setEnabled(true);
				btncompleteExecution.setEnabled(false);
			}
		});
		btnstartTransaction.setBounds(284, 142, 114, 23);
		frame.getContentPane().add(btnstartTransaction);
		btnstartTransaction.setEnabled(false);
		btnstartTransaction.setVisible(false);
		
		btnendTransaction = new JButton("End Transaction");
		btnendTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnendTransaction.setEnabled(false);
				btnstartTransaction.setEnabled(true);
				btnEndUsecase.setEnabled(true);
				textField_1.setEnabled(true);
				textField_1.setText("");
			}
		});
		btnendTransaction.setBounds(284, 176, 114, 23);
		frame.getContentPane().add(btnendTransaction);
		btnendTransaction.setEnabled(false);
		btnendTransaction.setVisible(false);
		
		
		
		
		startExecution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				lblUsecase.setVisible(true);
				btnStartUsecase.setVisible(true);
				btnEndUsecase.setVisible(true);
				btnEndUsecase.setEnabled(false);
				textField.setVisible(true);
				textField.setText("App-Launch");
				textField.setEnabled(false);
				startExecution.setVisible(false);
				lblEnterTransactionName.setVisible(true);
				textField_1.setVisible(true);
				btnendTransaction.setVisible(true);
				btnstartTransaction.setVisible(true);
				btncompleteExecution.setVisible(true);
				btncompleteExecution.setEnabled(false);
				
				
			}
		});
		
		
	}
}
