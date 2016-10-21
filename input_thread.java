import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.portable.InputStream;

public class input_thread{
	
	//private String message;
	private String userid;
	private JTextPane text_activities;
	private JTextPane search_text;
	private JTextPane msg_list;
	private String current_user;
	private JTextPane cnvrstn_msg;
	
	input_thread(Socket socket,JFrame frame1,String user_id,JTextPane act,JTextPane serch,JTextPane msg_lst,JTextPane cnrstn_msg)
	{	msg_list=msg_lst;
		cnvrstn_msg = cnrstn_msg; 
		text_activities = act;
		search_text = serch;
		userid = user_id;
		
		while(true)
		{
				InputStream is;
				try {
					  is = (InputStream) socket.getInputStream();
					  InputStreamReader isr = new InputStreamReader(is);
			          BufferedReader br = new BufferedReader(isr);
			          String message = br.readLine();
			          
			          JSONObject obj = (JSONObject) new JSONParser().parse(message);
		              System.out.println("From obj " + obj.get("name"));
		              
		              if(obj.get("id") == "4")	//Activity Update
		              {	    int count = Integer.parseInt((String) obj.get("count"));
			              	StyledDocument doc = text_activities.getStyledDocument();
	            	 		SimpleAttributeSet userId = new SimpleAttributeSet();
	            	 		SimpleAttributeSet pOst = new SimpleAttributeSet();
	            	 		//StyleConstants.setForeground(keyWord, Color.RED);
	            	 		//StyleConstants.setBackground(keyWord, Color.YELLOW);
	            	 		StyleConstants.setBold(userId, true);
	            	 		StyleConstants.setItalic(pOst, true);
			             int i = 1; 
			             if(count == 0) doc.insertString(0, "Sorry! No Posts :(\n", null);
			             while(count != 0)
		              	 {	String usri = "post" + i;
		              	    String dti = "date"+i;
		              		doc.insertString(0,"\n"+(String) obj.get(usri)+"\n", pOst);		              		 
		              		usri="user"+i;		              		 
		              		doc.insertString(0,"\n"+(String) obj.get(usri)+" at " + (String) obj.get(dti), userId);
		              		i = i+1;
		              		count =count - 1;
		              	 }		              
		              }
		              else if(obj.get("id") == "6")	//Search Update
		              {		if(obj.get("valid") == "1")
		            	    {	int count = Integer.parseInt((String) obj.get("count"));
				              	
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
		                    }
		              }
		              else if(obj.get("id") == "7")
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
				              		if(obj.get(usri)== current_user)
				              		{	doc1.insertString(doc1.getLength(),"\n "+ (String) obj.get(usri) +" at: "+(String) obj.get(dti), userId);
				              			doc1.insertString(doc1.getLength(),"\n "+(String) obj.get(msgi)+"\n", mSg);		              		 
				              		}
				              		
				              		//doc.insertString(0,"\n"+(String) obj.get(usri)+"\n", pOst);		              		 
				              		//usri="date"+i;		              		 
				              		doc.insertString(0,"\n " + usri +" at: \n"+(String) obj.get(usri), userId);
				              		
				              		i = i+1;
				              		count = count - 1;
				              	 }	 
				              	 
		              }
		              else if(obj.get("id") == "9")
		              {  	
		            	  if(obj.get("valid")=="1")
		            	  {	int count = Integer.parseInt((String) obj.get("count"));
		              	
		              		//StyledDocument doc = msg_list.getStyledDocument();
		              		StyledDocument doc1 = cnvrstn_msg.getStyledDocument();
		              		SimpleAttributeSet userId = new SimpleAttributeSet();
		              		SimpleAttributeSet mSg = new SimpleAttributeSet();
		              		//StyleConstants.setForeground(keyWord, Color.RED);
		              		//StyleConstants.setBackground(keyWord, Color.YELLOW);
		              		StyleConstants.setBold(userId, true);
		              		StyleConstants.setItalic(mSg, true);
		              		if(count == 0) doc1.insertString(0, "Sorry! No Messages :(\n", null);					   
		              		
		              		int i = 1;
		              		while(count != 0)
		              		{	
		              		 
			              		String usri ="user"+i;
			              		String dti="date"+i;
			              		String msgi = "msg" + i;
			              		
			              		doc1.insertString(doc1.getLength(),"\n "+ (String) obj.get(usri) +" at: "+(String) obj.get(dti), userId);
			              		doc1.insertString(doc1.getLength(),"\n "+(String) obj.get(msgi)+"\n", mSg);		              		 
			              		
			              		
			              		//doc.insertString(0,"\n"+(String) obj.get(usri)+"\n", pOst);		              		 
			              		//usri="date"+i;		              		 
			              		//doc.insertString(0,"\n " + usri +" at: \n"+(String) obj.get(usri), userId);
			              		
			              		i = i+1;
			              		count = count - 1;
		              		}	 
		            	  }
		            	  else
		            	  {		cnrstn_msg.setText("Please enter some valid user id!!\n");
		            	  		current_user = "invalid";
		            	  }
		              }
			          
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
					frame1.setVisible(false);
					e.printStackTrace();
					break;
				}
			
		}
				
	}
	
	public void change_current_user(String new_user)
	{	current_user=new_user;		
	}
	
	

}
