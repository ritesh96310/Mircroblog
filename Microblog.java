import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.SystemColor;
import javax.swing.JTextArea;
import java.awt.Window.Type;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.JFormattedTextField;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.portable.InputStream;

import javax.swing.JEditorPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Microblog {

	private JFrame frame;
	private JTextField search_tab_txt;
	private String post1; 
	private String current_user;
	private JTabbedPane tabbedPane;
	private JPanel panel_2;
	private JPanel panel;
	private JPanel panel_1;
	private JTextPane text_activities;
	private JButton post_btn;
	private JTextField search_msg;
	private JButton msg_send_btn;
	private JTextPane cnvrstn_msg;
	private JTextPane msg_list;
	private JTextPane search_text;
	private Socket socket;
	private String user_id;
	private String followers;
	private JButton btnFollow;
	private String followed;
	private JEditorPane send_msg1;
	private String usr;
	private JFrame parentfrm;
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
		
	}

	/**
	 * Create the application.
	 *@wbp.parser.entryPoint
	 */
	public Microblog(JFrame frm,Socket socket1,String user_name,String nfollowers) {
		//search_user = "invalid";
		parentfrm = frm;
		socket = socket1;
		user_id = user_name;
		followers = nfollowers;
		initialize();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//frame.setVisible(true);
			Thread t= new  Thread(new input_thread());
        // if(t!=null)
         t.start();
//         System.out.println("got a connection");

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Sparsh Saurabh\\Downloads\\favicon_24x24.png"));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
			parentfrm.setVisible(true);
			}
		});
  		current_user = "invalid";
		frame.setResizable(false);
		frame.setBounds(100, 100, 644, 421);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		menubar();
		tabs();
	}
	
	private void menubar()
	{	JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu logout_menu = new JMenu("Log Out");
		menuBar.add(logout_menu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Sure?");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				frame.setVisible(false); //you can't see me!
				frame.dispose(); //Destroy the JFrame object
			}
		});
		logout_menu.add(mntmNewMenuItem);			
		
	}
	
	private void tabs()
	{	tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	tabbedPane.setBorder(null);
		frame.getContentPane().add(tabbedPane, BorderLayout.NORTH);
		Activities_tab();
		Search_tab();
		Messages_tab();	
	}
	
	private void Activities_tab()
	{	panel_2 = new JPanel();
	panel_2.setBorder(null);
		tabbedPane.addTab("Activities", null, panel_2, null);
		
		JPanel panel_3 = new JPanel();		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		JScrollPane scrollPane = new JScrollPane();

		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 618, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 618, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 618, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(6)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
					.addGap(9))
		);
		
		text_activities = new JTextPane();		
		text_activities.setEditable(false);
		scrollPane.setViewportView(text_activities);

		
		post_btn = new JButton("Post");
		JEditorPane text_Post = new JEditorPane();
		text_Post.setDocument(new limitchar(140));
		
		post_btn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
					 try{ 
							post1 = text_Post.getText();
							//JOptionPane.showMessageDialog(null, post1);
							post_thread(post1);
						}
						catch(Exception e)
						{	JOptionPane.showMessageDialog(null, "Try Again");						
						}
					}
				});
				
				
				
				GroupLayout gl_panel_4 = new GroupLayout(panel_4);
				gl_panel_4.setHorizontalGroup(
					gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel_4.createSequentialGroup()
							.addGap(6)
							.addComponent(text_Post, GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(post_btn, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
				);
				gl_panel_4.setVerticalGroup(
					gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addContainerGap()
							.addComponent(post_btn)
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(text_Post, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
				);
				panel_4.setLayout(gl_panel_4);
				
				JLabel username = new JLabel("Name");
				username.setText(user_id);
				JLabel num_of_followers = new JLabel(followers);
				num_of_followers.setText(followers);
				GroupLayout gl_panel_3 = new GroupLayout(panel_3);
				gl_panel_3.setHorizontalGroup(
					gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addContainerGap()
							.addComponent(username)
							.addPreferredGap(ComponentPlacement.RELATED, 525, Short.MAX_VALUE)
							.addComponent(num_of_followers)
							.addContainerGap())
				);
				gl_panel_3.setVerticalGroup(
					gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
								.addComponent(username)
								.addComponent(num_of_followers))
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
				panel_3.setLayout(gl_panel_3);
				panel_2.setLayout(gl_panel_2);
				//StyledDocument doc = text_activities.getStyledDocument();
				//SimpleAttributeSet keyWord = new SimpleAttributeSet();
				//StyleConstants.setForeground(keyWord, Color.RED);
				//StyleConstants.setBackground(keyWord, Color.YELLOW);
				//StyleConstants.setBold(keyWord, true);
				
				
				//try
				//{
				//   doc.insertString(0, "Start of text\n", null );
				//    doc.insertString(0, "\nEnd of text", keyWord );
				//}
				//catch(Exception e) { System.out.println(e); }		
	}


	//Post
	private void post_thread(String post)
	{		// Send the post to server
			//OutputStream os;
			try {
							
				JSONObject obj = new JSONObject();
				obj.put("id", "3");
	            obj.put("post", post);
	            
	            sendtoserver(obj.toString()+"\n");
	            System.out.println("POSTED");
			}
			catch(Exception e)
			{
				
			}
				  //java.io.InputStream is = socket.getInputStream();
				  //InputStreamReader isr = new InputStreamReader(is);
		          //BufferedReader br = new BufferedReader(isr);
		          //String message = br.readLine();
		          //System.out.println(message);
		          
				
				
			// Put the new post in text box
			//int count;
/*			StyledDocument doc = text_activities.getStyledDocument();
	 		SimpleAttributeSet userId = new SimpleAttributeSet();
	 		SimpleAttributeSet pOst = new SimpleAttributeSet();
	 		//StyleConstants.setForeground(keyWord, Color.RED);
	 		//StyleConstants.setBackground(keyWord, Color.YELLOW);
	 		StyleConstants.setBold(userId, true);
	 		StyleConstants.setItalic(pOst, true);
	         //if(count == 0) doc.insertString(0, "Sorry! No Posts :(\n", null);
	 		 	
	 			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	      		doc.insertString(0,"\n"+post+"\n", pOst);		              		 
	      		Calendar cal = Calendar.getInstance();
	     	    //System.out.println(dateFormat.format(cal.getTime()));
	      		doc.insertString(0,"\n"+user_id+" at " + (String) dateFormat.format(cal.getTime()), userId);
			} 
			catch(Exception e)
			{	
				JOptionPane.showMessageDialog(null, "Can't Post, Try Again!!");
			}
*/	}
	
	private void Search_tab()
	{
		panel = new JPanel();
		panel.setBorder(null);
		tabbedPane.addTab("Search", null, panel, null);
		
		search_tab_txt = new JTextField();
		search_tab_txt.setColumns(10);
		
		JButton search_tab_btn = new JButton("Search");
		search_tab_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//close old thread and open new thread
				usr = search_tab_txt.getText();
				search_Thread(usr);
				search_text.setText("");
			}
		});
		
		JScrollPane scrollPane_3 = new JScrollPane();
		
		btnFollow = new JButton("Follow");
		btnFollow.setEnabled(false);
		btnFollow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(followed == "1")
				{	//OutputStream os;
					try {
						//os = socket.getOutputStream();
						//OutputStreamWriter osw = new OutputStreamWriter(os);
						//BufferedWriter bw = new BufferedWriter(osw);
						
						JSONObject obj = new JSONObject();
						obj.put("id", "6a");
			            obj.put("userid", usr);
			            obj.put("follow", "0");
			            sendtoserver(obj.toString()+"\n");
			            followed = "0";
			            btnFollow.setText("Follow");
			            
						//bw.flush();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
				else if(followed == "0")
				{	//OutputStream os;
					try {
						//os = socket.getOutputStream();
						//OutputStreamWriter osw = new OutputStreamWriter(os);
						//BufferedWriter bw = new BufferedWriter(osw);
						
						JSONObject obj = new JSONObject();
						obj.put("id", "6a");
			            obj.put("userid", usr);
			            obj.put("follow", "1");
			            followed = "1";
			            btnFollow.setText("Unfollow");
			            
			            sendtoserver(obj.toString()+"\n");
						//bw.flush();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}										
				}
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(search_tab_txt, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(search_tab_btn)
							.addPreferredGap(ComponentPlacement.RELATED, 335, Short.MAX_VALUE)
							.addComponent(btnFollow))
						.addComponent(scrollPane_3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(search_tab_txt, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(search_tab_btn)
						.addComponent(btnFollow))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		search_text = new JTextPane();
		search_text.setEditable(false);
		scrollPane_3.setViewportView(search_text);
		panel.setLayout(gl_panel);
	}

	private void search_Thread(String user)
	{	//open and then regularly update searched_profile
		//OutputStream os;
		try {
			//os = socket.getOutputStream();
			//OutputStreamWriter osw = new OutputStreamWriter(os);
			//BufferedWriter bw = new BufferedWriter(osw);
			
			JSONObject obj = new JSONObject();
			obj.put("id", "5");
            obj.put("userid", user);
            
            sendtoserver(obj.toString()+"\n");
			//bw.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void Messages_tab()
	{	panel_1 = new JPanel();
	panel_1.setBorder(null);
		tabbedPane.addTab("Message", null, panel_1, null);
		
		JPanel panel_5 = new JPanel();		
		JPanel panel_6 = new JPanel();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);

		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 483, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_6, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))
					.addGap(4))
		);
		
		JPanel panel_7 = new JPanel();		
		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 463, GroupLayout.PREFERRED_SIZE)
					.addGap(20))
				.addGroup(gl_panel_6.createSequentialGroup()
					.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_6.createSequentialGroup()
					.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 272, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		msg_send_btn = new JButton("New button");
		msg_send_btn.setEnabled(false);
		msg_send_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg = send_msg1.getText();
				send_msg_funct(msg);
			}
		});
		
		send_msg1 = new JEditorPane();
		send_msg1.setDocument(new limitchar(140));
		
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8.setHorizontalGroup(
			gl_panel_8.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_8.createSequentialGroup()
					.addComponent(send_msg1, GroupLayout.PREFERRED_SIZE, 416, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(msg_send_btn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(25, Short.MAX_VALUE))
		);
		gl_panel_8.setVerticalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addComponent(send_msg1, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addContainerGap()
					.addComponent(msg_send_btn, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_8.setLayout(gl_panel_8);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(
			gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addComponent(scrollPane_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
		);
		gl_panel_7.setVerticalGroup(
			gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
		);
		
		cnvrstn_msg = new JTextPane();
		cnvrstn_msg.setEditable(false);
		scrollPane_2.setViewportView(cnvrstn_msg);
		panel_7.setLayout(gl_panel_7);
		panel_6.setLayout(gl_panel_6);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JPanel panel_3 = new JPanel();
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		msg_list = new JTextPane();
		msg_list.setEditable(false);
		scrollPane_1.setViewportView(msg_list);
		panel_3.setLayout(null);
		
		search_msg = new JTextField();
		search_msg.setBounds(0, 3, 112, 23);
		panel_3.add(search_msg);
		search_msg.setColumns(10);
		
		JButton search_msg_btn = new JButton("New button");
		search_msg_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String usr = search_msg.getText();
				cnvrstn_msg.setText("");
				search_acc_msg(usr);
			}
		});
		search_msg_btn.setBounds(113, 3, 32, 23);
		panel_3.add(search_msg_btn);
		panel_5.setLayout(gl_panel_5);
		panel_1.setLayout(gl_panel_1);

		//all_msg_thread();
		
	}
	
	private void search_acc_msg(String user)
	{	//Open the acc, last 5 msg, make input area accesible,start current_msg_thread
		//else keep input area sealed, and ask for valid username
		current_user=user;
		//OutputStream os;
		try {
			//os = socket.getOutputStream();
			//OutputStreamWriter osw = new OutputStreamWriter(os);
			//BufferedWriter bw = new BufferedWriter(osw);
			
			JSONObject obj = new JSONObject();
			obj.put("id", "8");
            obj.put("userid", user);
            
            sendtoserver(obj.toString()+"\n");
			//bw.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//private void all_msg_thread()
	//{	//Update regularly msg_list


	//}

	//private void current_msg_thread()
	//{	//Update regularly cnvrstn_msg

	//}

	private void send_msg_funct(String msg)
	{	// send msg from current_msg
		//current_user=user;
		//OutputStream os;
		try {
			//os = socket.getOutputStream();
			//OutputStreamWriter osw = new OutputStreamWriter(os);
			//BufferedWriter bw = new BufferedWriter(osw);
			System.out.println(msg);
			JSONObject obj = new JSONObject();
			obj.put("id", "10");
            obj.put("currentuser", current_user);
            obj.put("msg",msg);
            sendtoserver(obj.toString()+"\n");
            
            //StyledDocument doc = msg_list.getStyledDocument();
  	 		StyledDocument doc1 = cnvrstn_msg.getStyledDocument();
	        SimpleAttributeSet userId = new SimpleAttributeSet();
  	 		SimpleAttributeSet mSg = new SimpleAttributeSet();
  	 		//StyleConstants.setForeground(keyWord, Color.RED);
  	 		//StyleConstants.setBackground(keyWord, Color.YELLOW);
  	 		StyleConstants.setBold(userId, true);
  	 		StyleConstants.setItalic(mSg, true);
             //if(count == 0) doc.insertString(0, "Sorry! No Posts :(\n", null);	
  	 		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      		Calendar cal = Calendar.getInstance();
      		
      		doc1.insertString(0,"\n "+msg+"\n", mSg);	
  	 		doc1.insertString(0,"\n "+ user_id +" at: "+(String) dateFormat.format(cal.getTime()) , userId);
  			
  			
      		
     	    //System.out.println(dateFormat.format(cal.getTime()));
  			
			//bw.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public class input_thread implements Runnable
	{	public void run()
		{		
		
			while(true)
			{
					java.io.InputStream is;
					try {
						  is = socket.getInputStream();
						  InputStreamReader isr = new InputStreamReader(is);
				          BufferedReader br = new BufferedReader(isr);
				          String message = br.readLine();
				          System.out.println("Message: " + message);
				          JSONObject obj = (JSONObject) new JSONParser().parse(message);
			              //System.out.println("Obj " + obj.get("name"));
			              System.out.println("Here in input thread");
			              String id = (String) obj.get("id");
			              //System.out.println(obj);
			              int prev_count=0;
			              if(id.compareTo("4") == 0)	//Activity Update
			              {	    
			            	  	int count = Integer.parseInt((String) obj.get("count"));
			            	  	StyledDocument doc = text_activities.getStyledDocument();
		            	 		SimpleAttributeSet userId = new SimpleAttributeSet();
		            	 		SimpleAttributeSet pOst = new SimpleAttributeSet();
		            	 		//StyleConstants.setForeground(keyWord, Color.RED);
		            	 		//StyleConstants.setBackground(keyWord, Color.YELLOW);
		            	 		StyleConstants.setBold(userId, true);
		            	 		StyleConstants.setItalic(pOst, true);
				                int i = 1; 
				                if(count == 0) doc.insertString(0, "Sorry! No Posts :(\n", null);
				             while(i <= count)
			              	 {	String usri = "post" + i;
			              	    String dti = "date"+i;
			              		doc.insertString(0,"\n"+(String) obj.get(usri)+"\n", pOst);		              		 
			              		usri="user"+i;		              		 
			              		doc.insertString(0,"\n"+(String) obj.get(usri)+" at " + (String) obj.get(dti), userId);
			              		i = i+1;
			              	 }	
				             
				             System.out.println("i:"+i);
			              }
			              else if(id.compareTo("6") == 0)	//Search Update
			              {		String valid = (String) obj.get("valid");
			            	  	if(valid.compareTo("1") == 0)
			            	    {	System.out.println(obj);
			            	  		int count = Integer.parseInt((String) obj.get("count"));
					              	
			            	    	btnFollow.setEnabled(true);
			            	    	String follow=((String)obj.get("follow"));
			            	    	if(follow.compareTo("1")==0)	
			            	    	{	btnFollow.setText("Unfollow");
			            	    		followed = "1";
			            	    	}
			            	    	else if(follow.compareTo("0")==0) 
			            	    	{	btnFollow.setText("Follow");
			            	    		followed = "0";
			            	    	}
			            	    	
			            	        StyledDocument doc = search_text.getStyledDocument();
				          	 		SimpleAttributeSet userId = new SimpleAttributeSet();
				          	 		SimpleAttributeSet pOst = new SimpleAttributeSet();
				          	 		//StyleConstants.setForeground(keyWord, Color.RED);
				          	 		//StyleConstants.setBackground(keyWord, Color.YELLOW);
				          	 		StyleConstants.setBold(userId, true);
				          	 		StyleConstants.setItalic(pOst, true);
						             if(count == 0) doc.insertString(0, "Sorry! No Posts :(\n", null);					   
				          	 		
				          	 		 int i = 1;
					              	 while(count != 0)
					              	 {	String usri = "post" + i;
					              		doc.insertString(0,"\n"+(String) obj.get(usri)+"\n", pOst);		              		 
					              		usri="date"+i;		              		 
					              		doc.insertString(0,"\n at: "+(String) obj.get(usri), userId);
					              		i = i+1;
					              		count = count - 1;
					              	 }	
			            	    }
			                    else
			                    {	search_text.setText("Please enter some valid user id!!\n");   
			                    	btnFollow.setEnabled(false);
			                    }
			              }
			              else if(id.compareTo("7") == 0)//All Messages
			              {		
			            	  		int count = Integer.parseInt((String) obj.get("count"));
					              	
			            	        StyledDocument doc = msg_list.getStyledDocument();
				          	 		StyledDocument doc1 = cnvrstn_msg.getStyledDocument();
			            	        SimpleAttributeSet userId = new SimpleAttributeSet();
				          	 		SimpleAttributeSet mSg = new SimpleAttributeSet();
				          	 		//StyleConstants.setForeground(keyWord, Color.RED);
				          	 		//StyleConstants.setBackground(keyWord, Color.YELLOW);
				          	 		StyleConstants.setBold(userId, true);
				          	 		StyleConstants.setItalic(mSg, true);
						             //if(count == 0) doc.insertString(0, "Sorry! No Posts :(\n", null);					   
				          	 		
				          	 		 int i = 1;
					              	 while(count != 0)
					              	 {	
					              		 
					              		String usri ="user"+i;
					              		String dti="date"+i;
					              		String msgi = "msg" + i;
					              		String str = (String) obj.get(usri);
					              		if(str.compareTo(current_user)==0)
					              		{	
					              			doc1.insertString(0,"\n "+(String) obj.get(msgi)+"\n", mSg);	
					              			doc1.insertString(0,"\n "+ (String) obj.get(usri) +" at: "+(String) obj.get(dti), userId);
					              		}
					              		
					              		//doc.insertString(0,"\n"+(String) obj.get(usri)+"\n", pOst);		              		 
					              		//usri="date"+i;		              		 
					              		doc.insertString(0,"\n " + (String)obj.get(usri) +" at: \n"+(String) obj.get(dti)+"\n"+(String) obj.get(msgi), userId);
					              		
					              		i = i+1;
					              		count = count - 1;
					              	 }	 
					              	 
			              }
			              else if(id.compareTo("9") == 0)//Search Reply
			              {   String valid = (String) obj.get("valid");
			            	  if(valid.compareTo("1") == 0)
			            	  {	int count = Integer.parseInt((String) obj.get("count"));
			              	
			              		//StyledDocument doc = msg_list.getStyledDocument();
			              		StyledDocument doc1 = cnvrstn_msg.getStyledDocument();
			              		SimpleAttributeSet userId = new SimpleAttributeSet();
			              		SimpleAttributeSet mSg = new SimpleAttributeSet();
			              		//StyleConstants.setForeground(keyWord, Color.RED);
			              		//StyleConstants.setBackground(keyWord, Color.YELLOW);
			              		StyleConstants.setBold(userId, true);
			              		StyleConstants.setItalic(mSg, true);
			              		if(count == 0)
			              		{	doc1.insertString(0, "Sorry! No Messages :(\n", null);
			              			msg_send_btn.setEnabled(true);
			              		}
			              		
			              		int i = 1;
			              		while(count != 0)
			              		{	
			              		 
				              		String usri ="user"+i;
				              		String dti="date"+i;
				              		String msgi = "msg" + i;
				              		
				              		doc1.insertString(doc1.getLength(),"\n "+ (String) obj.get(usri) +" at: "+(String) obj.get(dti), userId);
				              		doc1.insertString(doc1.getLength(),"\n "+(String) obj.get(msgi)+"\n", mSg);		              		 
				              		msg_send_btn.setEnabled(true);
				              		System.out.println("Here");
				              		//doc.insertString(0,"\n"+(String) obj.get(usri)+"\n", pOst);		              		 
				              		//usri="date"+i;		              		 
				              		//doc.insertString(0,"\n " + usri +" at: \n"+(String) obj.get(usri), userId);
				              		
				              		i = i+1;
				              		count = count - 1;
			              		}	 
			            	  }
			            	  else
			            	  {		cnvrstn_msg.setText("Please enter some valid user id!!\n");
			            	  		current_user = "invalid";
			            	  		msg_send_btn.setEnabled(false);
			            	  }
			              }
				          
					} catch (Exception e) {
						//  Auto-generated catch block
						frame.setVisible(false);
						e.printStackTrace();
						break;
					}
			
		}
				
	}
	}
	
	private synchronized void sendtoserver(String msg)
	{	try
		{	OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			
	        bw.write(msg);
			bw.flush();
		}
		catch(Exception e)
		{				
		}
	}
}
