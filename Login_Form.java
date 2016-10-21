import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.print.attribute.AttributeSet;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.portable.InputStream;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class Login_Form {

	private JFrame frame;
	private JPasswordField passwordField;
	Microblog window1;
	private static Socket socket;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login_Form window = new Login_Form();
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
	public Login_Form() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Sparsh Saurabh\\Downloads\\favicon_24x24.png"));
		frame.setResizable(false);
		frame.getContentPane().setEnabled(false);
		
		JLabel user_name = new JLabel("User Name");
		
		JLabel pass = new JLabel("Password");
		
		JButton btnLogin = new JButton("Login");
		
		JFormattedTextField UserName = new JFormattedTextField();
		passwordField = new JPasswordField();

		JLabel cantconnect = new JLabel("Can't Connect");
		cantconnect.setEnabled(false);
		cantconnect.setVisible(false);
		
		JLabel wronginfo = new JLabel("Wrong User Name and/or Password");
		wronginfo.setEnabled(false);
		wronginfo.setVisible(false);
			
		btnLogin.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")

/*------------------------------------------------------------------*/
/*      On login try, login and password is sent to the server 		*/

			
			
			public void actionPerformed(ActionEvent arg0) {
				 try{ 
					 
					 	String user;
						String password;
						user = UserName.getText();
						password = passwordField.getText();
						JOptionPane.showMessageDialog(null, user + password);
						//window1 = new Microblog(socket,user,"100");
						try
				        {
				            String host = "localhost";
				            int port = 30000;
				            InetAddress address = InetAddress.getByName(host);
				            socket = new Socket(address, port);
				 
				            //Send the message to the server
				            OutputStream os = socket.getOutputStream();
				            OutputStreamWriter osw = new OutputStreamWriter(os);
				            BufferedWriter bw = new BufferedWriter(osw);
				            
				            JSONObject login_obj = new JSONObject();
				            login_obj.put("id", "1");
				            login_obj.put("loginid", user);
				            login_obj.put("password", password);	
				            String sendMessage = login_obj.toString() + "\n";
				            bw.write(sendMessage);
				            bw.flush();
				            System.out.println(sendMessage);
				            //Get the return message from the server
				            java.io.InputStream is = socket.getInputStream();
				            InputStreamReader isr = new InputStreamReader(is);
				            BufferedReader br = new BufferedReader(isr);
				            String message = br.readLine();
				            System.out.println(message);
			                JSONObject obj = (JSONObject) new JSONParser().parse(message);
			                
			                System.out.println(obj);
			                String id = (String) obj.get("id");
			                String valid = (String) obj.get("valid");
			                
			                System.out.println("From obj " + id+" "+valid);
			                
			                if((id.compareTo("2")==0) && (valid.compareTo("1") == 0))
			                {		frame.setVisible(false);
									window1 = new Microblog(frame,socket,(String)obj.get("name"),(String)obj.get("follower"));
			                }
			                else
			                {	
			                	wronginfo.setVisible(true);
			                }
				        }
				        catch (Exception exception)
				        {
				        	cantconnect.setVisible(true);
				            exception.printStackTrace();
				        }
/*				        finally
				        {
				            //Closing the socket
				            try
				            {
				                socket.close();
				            }
				            catch(Exception e)
				            {
				                e.printStackTrace();
				            }
				        }
*/							
						
						
						
						//if(login(user,password) == 1)
						//{	frame.setVisible(false);
						//	window1 = new Microblog();							
						//}
						
					}
					catch(Exception e)
					{	JOptionPane.showMessageDialog(null, "Try Again");						
					}
				}
		
		});
		
		UserName.setDocument(new limitchar(15));
		
		passwordField.setDocument(new limitchar(15));
		
		JButton btnCreateNewAccount = new JButton("Create New Account");
		btnCreateNewAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);	
				New_Account newaccform = new New_Account(frame);
					
			}
		});
		
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(118)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(pass)
								.addComponent(wronginfo)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(UserName, Alignment.LEADING)
									.addComponent(passwordField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
								.addComponent(user_name)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(164)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(cantconnect)
								.addComponent(btnLogin)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(19)
							.addComponent(btnCreateNewAccount)))
					.addContainerGap(88, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(64)
					.addComponent(user_name)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(UserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pass)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(15)
					.addComponent(btnLogin)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cantconnect)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(wronginfo)
					.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
					.addComponent(btnCreateNewAccount)
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
		frame.setBounds(100, 100, 383, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

