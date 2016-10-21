import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class New_Account {

	private JFrame frmMakeNewAccount;
	private JFrame parentframe;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			JFrame frm1;
			public void run() {
				try {
					New_Account window = new New_Account(frm1);
					window.frmMakeNewAccount.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public New_Account(JFrame frm) {
		parentframe = frm;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMakeNewAccount = new JFrame();
		frmMakeNewAccount.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				parentframe.setVisible(true);
			}
		});
		frmMakeNewAccount.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmMakeNewAccount.setIconImage(
				Toolkit.getDefaultToolkit().getImage("C:\\Users\\Sparsh Saurabh\\Downloads\\favicon_24x24.png"));
		frmMakeNewAccount.getContentPane().setBackground(new Color(154, 205, 50));
		frmMakeNewAccount.setResizable(false);
		frmMakeNewAccount.setBounds(100, 100, 344, 389);
		frmMakeNewAccount.setVisible(true);
		textField = new JTextField();
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setColumns(10);

		JLabel lblUserName = new JLabel("User Id");

		JLabel lblPassword = new JLabel("Password");

		JLabel lblName = new JLabel("Name");

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		JLabel lblNewAccountCreated = new JLabel("");
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String Name = textField.getText();
				String userid = textField_1.getText();
				String password = textField_2.getText();
				

				JSONObject obj1 = new JSONObject();

				// Mysql query for taking the elements from the

				obj1.put("id", "0");
				obj1.put("name", Name);
				obj1.put("userid", userid);
				obj1.put("password", password);
				
				String jsonText = obj1.toString() + "\n";

				String host = "localhost";
				int port = 40000;			//change port number
				Socket socket=null;
				try
				{				InetAddress address = InetAddress.getByName(host);
								socket = new Socket(address, port);
				
								// Send the message to the server
								OutputStream os = socket.getOutputStream();
								OutputStreamWriter osw = new OutputStreamWriter(os);
								BufferedWriter bw = new BufferedWriter(osw);
				
								bw.write(jsonText);
								bw.flush();
								
								java.io.InputStream is = socket.getInputStream();
					            InputStreamReader isr = new InputStreamReader(is);
					            BufferedReader br = new BufferedReader(isr);
					            String message = br.readLine();
					            System.out.println(message);
				                JSONObject obj = (JSONObject) new JSONParser().parse(message);
				                String valid = (String) obj.get("valid");
				                
				                if(valid.compareTo("0")==0)
				                {	
				                	lblNewAccountCreated.setText("Sorry! Userid already exists");
				                }
				                else if(valid.compareTo("1")==0)
				                {	lblNewAccountCreated.setText("Welcome! Your account has been created!");
				                	
				                }
				                
				}
				catch(Exception e)
				{					
				}
				finally
				{
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		
		
		GroupLayout groupLayout = new GroupLayout(frmMakeNewAccount.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(93)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUserName)
								.addComponent(textField_1)
								.addComponent(textField, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
								.addComponent(textField_2)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(3)
									.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(144)
							.addComponent(btnSubmit)))
					.addContainerGap(84, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(75)
					.addComponent(lblNewAccountCreated, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(102))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(80)
					.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(9)
					.addComponent(lblUserName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblPassword)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblNewAccountCreated)
					.addGap(30)
					.addComponent(btnSubmit)
					.addContainerGap(67, Short.MAX_VALUE))
		);
		frmMakeNewAccount.getContentPane().setLayout(groupLayout);
	}

}
