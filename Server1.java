import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;


public class Server1 {

	private static Socket clientSocket;
	static ServerSocket serverSocket;

	public static void main(String[] args) {
		try {

			int port = 40000;
			serverSocket = new ServerSocket(port);
			System.out.println("Server Started and listening to the port 40000");

			// Server is running always. This is done using this while(true)
			// loop

			while (true) {

				clientSocket = serverSocket.accept();
				System.out.println(clientSocket);

				InputStream is = clientSocket.getInputStream();
				InputStreamReader isReader = new InputStreamReader(is);
				BufferedReader bReader = new BufferedReader(isReader);

				OutputStream os = clientSocket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os);
				BufferedWriter bWriter = new BufferedWriter(osw);

				String url = "jdbc:mysql://localhost:3306/";
				String user = "root";
				String password = "";

				// Making the connection with the database

				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection con;
				con = null;
				con = DriverManager.getConnection(url, user, password);
				if (con != null)
					System.out.println("connection build");
				
				Statement stt = con.createStatement();

				String clientStr = null;

				clientStr = bReader.readLine();
				// System.out.println(clientStr);

				int valid = 0;
				
				
				// System.out.println(clientStr);
				JSONObject jsonObject = (JSONObject) new JSONParser().parse(clientStr);
				// JSONObject jsonObject = (JSONObject) obj;
				System.out.println(jsonObject.get("id"));

				
				
				
				String loginid;
				String password1;
				String name;

				String id0 = (String) jsonObject.get("id");
				if (id0.compareTo("0") == 0) {
					
					loginid = (String) jsonObject.get("userid");
					password1 = (String) jsonObject.get("password");
					name = (String) jsonObject.get("name");

					
					ResultSet res = null;
					

					stt.execute("USE microblogging");
					System.out.println("enter before res.next loop");

					res = stt.executeQuery("SELECT * FROM users WHERE user_id = '" + loginid + "'");

					if (res.next()) {
						
						// System.out.println("enter into res.next loop");
						valid = 0;

					} else {
						valid = 1;
					}

					if (valid == 1) {
						
						JSONObject obj1 = new JSONObject();

						// Mysql query for putting the elements from the
					
						
						stt.execute("INSERT INTO users (user_id, password, name, follower_no) VALUES" + 
				                    "('"+loginid+"', '"+password1+"','"+name +"', '0')");

						obj1.put("id", "0a");
						obj1.put("valid", "1");
						
						StringWriter out = new StringWriter();
						obj1.writeJSONString(out);

						String jsonText = out.toString() + "\n";
						
						bWriter.write(jsonText);
						bWriter.flush();

					}

					else {
						JSONObject obj1 = new JSONObject();

						obj1.put("id", "0a");
						obj1.put("valid", "0");
				
						StringWriter out = new StringWriter();

						obj1.writeJSONString(out);
						String jsonText = out.toString() + "\n";
						

						bWriter.write(jsonText);
						bWriter.flush();

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} 
		
		finally {
				try {
					serverSocket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

	}
}