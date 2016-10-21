import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.sql.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class ClientHandler implements Runnable {

	BufferedReader bReader;
	BufferedWriter bWriter;
	Socket socket;

	// Statement stt;
	String loginid;
	String password1;
	String message_user;

	public ClientHandler(Socket clientSocket) {
		try {

			socket = clientSocket;
			// Making the input and outputstream.

			InputStream is = socket.getInputStream();
			InputStreamReader isReader = new InputStreamReader(is);
			bReader = new BufferedReader(isReader);

			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			bWriter = new BufferedWriter(osw);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void run() {
		try {
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
			// Statement stt = null;
			Statement stt = con.createStatement();

			// while(true)
			// {
			String clientStr = null;

			clientStr = bReader.readLine();
			// System.out.println(clientStr);

			int valid = 0;
			// System.out.println(clientStr);
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(clientStr);
			// JSONObject jsonObject = (JSONObject) obj;
			System.out.println(jsonObject.get("id"));

			String id0 = (String) jsonObject.get("id");
			if (id0.compareTo("1") == 0) {
				loginid = (String) jsonObject.get("loginid");
				password1 = (String) jsonObject.get("password");

				// System.out.println("value from client="+ loginid +password1);
				ResultSet res = null;
				// check from database;

				stt.execute("USE microblogging");
				System.out.println("enter before res.next loop");

				// res = stt.executeQuery("SELECT * FROM users WHERE user_id =
				// "+"'"+loginid+"'"+" AND password="+"'"+password1+"'");
				res = queryClient(1, stt,
						"SELECT * FROM users WHERE user_id = '" + loginid + "'" + " AND password='" + password1 + "'");
				if (res.next()) {
					// System.out.println("enter into res.next loop");
					valid = 1;

				}

				// make some variable above and then check for that whether it
				// is present or not .
				if (valid == 1) {
					JSONObject obj1 = new JSONObject();

					// Mysql query for taking the elements from the

					obj1.put("id", "2");
					obj1.put("valid", "1");
					obj1.put("name", (String) res.getString("name"));
					obj1.put("follower", (String) res.getString("follower_no"));

					// System.out.println("Message send from the server : "+
					// obj);
					StringWriter out = new StringWriter();
					obj1.writeJSONString(out);

					String jsonText = out.toString() + "\n";
					// System.out.println("Message received from the server : "
					// +jsonText);

					bWriter.write(jsonText);
					bWriter.flush();

					// do stuff mainly start the three thread.
					// 1.input thread, 2.output thread 3.activity thread.
					Thread t3 = new Thread(new Input());
					t3.start();

					Thread t1 = new Thread(new Activity());
					t1.start();

					Thread t2 = new Thread(new Message());
					t2.start();

				}

				else {
					JSONObject obj1 = new JSONObject();

					obj1.put("id", "2");
					obj1.put("valid", "0");
					obj1.put("name", "null");
					obj1.put("follower", "0");

					// System.out.println("Message send from the server : "+
					// obj);
					StringWriter out = new StringWriter();

					obj1.writeJSONString(out);
					String jsonText = out.toString() + "\n";
					// System.out.println("Message received from the server : "
					// +jsonText);

					bWriter.write(jsonText);
					bWriter.flush();

				}
			}
			// }//while loop ends
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// run ends

	/**************************
	 * @@@@@@@@@@@@@@@@@@@ INPUT THREAD IMPLEMENTATION @@@@@@@@@@@@@@@@@@@@@@@@
	 *********************************/
	public class Input implements Runnable {

		public Input() {

		}

		public void run() {

			try {

				while (true) {
					String input = null;
					// System.out.println("New Input thread has started");
					input = bReader.readLine();
					// System.out.println(clientStr);
					// System.out.println(input);
					String url = "jdbc:mysql://localhost:3306/";
					String user = "root";
					String password = "";

					// Making the connection with the database

					Class.forName("com.mysql.jdbc.Driver").newInstance();

					Connection con = null;
					con = DriverManager.getConnection(url, user, password);
					// if(con!=null)
					// System.out.println("connection build");
					// Statement stt = null;
					Statement stt = con.createStatement();
					stt.execute("USE microblogging");

					JSONObject jsonObject1 = (JSONObject) new JSONParser().parse(input);
					// JSONObject jsonObject = (JSONObject) obj;
					// System.out.println("value of id in new input thread
					// "+jsonObject1.get("id"));

					String id1 = (String) jsonObject1.get("id");

					if (id1.compareTo("3") == 0) {
						// take the post and Insert into database
						String post1 = (String) jsonObject1.get("post");
						// System.out.println(post1);
						// String date1 = (String) jsonObject1.get("date");

						// System.out.println("for posting the values are
						// loginid="+loginid+"post="+post1);//+"date="+date1);

						// stt.execute("INSERT INTO people (fname, lname)
						// VALUES" +
						// "('Joe', 'Bloggs'), ('Mary', 'Bloggs'), ('Jill',
						// 'Hill')");

						// stt.execute("INSERT INTO posts (user_id,user_post)
						// VALUES" +"('"+loginid+"','"+/*date1
						// +"','"+*/post1+"')");
						queryClient(2, stt, "INSERT INTO posts (user_id,user_post) VALUES" + "('" + loginid + "','"
								+ /* date1 +"','"+ */post1 + "')");

					} else if (id1.compareTo("5") == 0) {
						// take the search input and check from database and
						// return the 5(searched user posts)information using id
						// 6.

						ResultSet res;
						res = queryClient(1, stt,
								"SELECT * FROM users WHERE user_id = '" + jsonObject1.get("userid").toString() + "'");
						JSONObject obj1 = new JSONObject();

						if (res.next()) {
							obj1.put("id", "6");
							obj1.put("valid", "1");

							res = queryClient(1, stt, "SELECT  * FROM posts where user_id= '"
									+ jsonObject1.get("userid").toString() + " '");
							int i = 1;

							while (res.next()) {

								obj1.put("user" + i, res.getString("user_id"));
								obj1.put("post" + i, res.getString("user_post"));
								obj1.put("date" + i, res.getString("date"));

								i++;
							}

							obj1.put("count", Integer.toString(i - 1));
							res = queryClient(1, stt, "SELECT * FROM follower WHERE (user1 = '" + loginid
									+ "') AND (user2 = '" + jsonObject1.get("userid").toString() + "')");
							if (res.next())
								obj1.put("follow", "1");
							else
								obj1.put("follow", "0");
						} else {
							obj1.put("id", "6");
							obj1.put("valid", "0");
							obj1.put("count", "0");
						}

						// System.out.println("Message send from the server : "+
						// obj);
						// StringWriter out = new StringWriter();

						// obj1.writeJSONString(out);
						String jsonText = obj1.toString() + "\n";
						// System.out.println("Message received from the server
						// : " +jsonText);
						sendtoClient(jsonText);
						// bWriter.write(jsonText);
						// bWriter.flush();

					} else if (id1.compareTo("6a") == 0) {
						ResultSet res;
						res = queryClient(1, stt, "SELECT * FROM follower WHERE user1 = '" + loginid + "' AND user2 = '"
								+ jsonObject1.get("userid").toString() + "'");
						JSONObject obj1 = new JSONObject();

						if (res.next()) {
							queryClient(3, stt, "DELETE FROM follower where user1 = '" + loginid + "' AND user2='"
									+ jsonObject1.get("userid").toString() + "'");

						} else {
							queryClient(2, stt, "INSERT INTO follower (user1,user2) VALUES" + "('" + loginid + "','"
									+ jsonObject1.get("userid").toString() + "')");
						}

					} else if (id1.compareTo("8") == 0) {
						// verify whether user is valid for messaging or not
						// set a global message_user
						// only userid as part of query

						ResultSet res;
						res = queryClient(1, stt, "SELECT * FROM users WHERE user_id = " + "'"
								+ jsonObject1.get("userid").toString() + "'");
						JSONObject obj1 = new JSONObject();

						if (res.next()) {

							message_user = jsonObject1.get("userid").toString();
							res = queryClient(1, stt,
									"SELECT  * FROM message where ((user1='" + loginid + "') AND (user2='"
											+ jsonObject1.get("userid").toString() + "')) or ((user1='"
											+ jsonObject1.get("userid").toString() + "') AND (user2='" + loginid
											+ "')) order by date desc limit 0, 10");
							// int loop_count=0;
							int i = 1;
							// int flag=0;
							while (res.next()) {

								obj1.put("user" + i, res.getString("user1"));
								obj1.put("msg" + i, res.getString("user_msg"));
								obj1.put("date" + i, res.getString("date"));

								i++;
								// loop_count++;

							}
							obj1.put("id", "9");
							obj1.put("valid", "1");
							obj1.put("count", Integer.toString(i - 1));

						} else {
							obj1.put("id", "9");
							obj1.put("valid", "0");
							obj1.put("count", "0");

						}

						// System.out.println("Message send from the server : "+
						// obj);
						// StringWriter out = new StringWriter();

						// obj1.writeJSONString(out);
						String jsonText = obj1.toString() + "\n";
						// System.out.println("Message received from the server
						// : " +jsonText);
						sendtoClient(jsonText);
						// bWriter.write(jsonText);
						// bWriter.flush();

					} else if (id1.compareTo("10") == 0) {
						System.out.println(jsonObject1);
						String msg = (String) jsonObject1.get("msg");
						// System.out.println(msg);
						queryClient(2, stt, "INSERT INTO message (user1,user2,user_msg) VALUES" + "('" + loginid + "','"
								+ message_user + "','" + msg + "')");

					}
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

	}// input class ends

	/**************************
	 * @@@@@@@@@@@@@@@@@@@ ACTIVITY THREAD
	 * IMPLEMENTATION @@@@@@@@@@@@@@@@@@@@@@@
	 *********************************/

	public class Activity implements Runnable {

		public Activity() {

		}

		public void run() {

			try {

				String url = "jdbc:mysql://localhost:3306/";
				String user1 = "root";
				String password = "";
				// Making the connection with the database
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection con = null;
				con = DriverManager.getConnection(url, user1, password);
				if (con != null)
					System.out.println("connection build");
				Statement stt = con.createStatement();
				stt.execute("USE microblogging");

				ResultSet res1, res2;
				String str1 = "SELECT * FROM follower WHERE user1 = '" + loginid + "'";

				res1 = queryClient(1, stt, str1);
				String str;

				str = "(user_id = '" + loginid + "') ";

				while (res1.next()) {
					str = str + " or (user_id = '" + res1.getString("user2") + "')";
				}

				// System.out.println(str);

				res2 = queryClient(1, stt, "SELECT * FROM posts WHERE " + str + " order by date desc limit 0, 10");
				JSONObject obj_loop = new JSONObject();

				int i = 1;
				int flag = 0;
				while (res2.next()) {

					obj_loop.put("user" + i, res2.getString("user_id"));
					obj_loop.put("post" + i, res2.getString("user_post"));
					obj_loop.put("date" + i, res2.getString("date"));
					i++;
					flag = 1;
				}
				String prev_date;
				if (flag == 1) {
					obj_loop.put("id", "4");
					obj_loop.put("count", Integer.toString(i - 1));
					String client_Text1 = obj_loop.toString() + "\n";
					// System.out.println(client_Text1);
					sendtoClient(client_Text1);
					System.out.println(client_Text1);
					prev_date = (String) obj_loop.get("date1");

				} else {
					prev_date = "2015-11-08 17:12:00";

				}

				while (true) {
					JSONObject obj_loop1 = new JSONObject();
					String str2 = "SELECT * FROM follower WHERE user1 = '" + loginid + "'";

					ResultSet res3 = queryClient(1, stt, str2);
					String str3 = "(user_id = '" + loginid + "') ";

					while (res3.next()) {
						str3 = str3 + " or (user_id = '" + res3.getString("user2") + "')";
					}

					ResultSet loop = queryClient(1, stt, "SELECT  * FROM posts where (date > '" + prev_date + "') and ("
							+ str3 + ") order by date desc limit 0, 10");

					i = 1;
					flag = 0;
					while (loop.next()) {

						if (flag == 0) {
							prev_date = loop.getString("date");
							flag = 1;
						}

						System.out.println(loop.getString("date"));
						obj_loop1.put("user" + i, loop.getString("user_id"));
						obj_loop1.put("post" + i, loop.getString("user_post"));
						obj_loop1.put("date" + i, loop.getString("date"));

						i++;
					}

					if (flag == 1) {
						obj_loop1.put("id", "4");
						obj_loop1.put("count", Integer.toString(i - 1));
						String client_Text1 = obj_loop1.toString() + "\n";
						sendtoClient(client_Text1);
						System.out.println(client_Text1);
						flag = 0;
					}

				}

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

	}

	/**************************
	 * @@@@@@@@@@@@@@@@@@@ OUTPUT THREAD IMPLEMENTATION @@@@@@@@@@@@@@@@@@@@@@@
	 *********************************/

	public class Message implements Runnable {

		public Message() {

		}

		public void run() {

			try {
				String url = "jdbc:mysql://localhost:3306/";
				String user1 = "root";
				String password = "";

				// Making the connection with the database

				Class.forName("com.mysql.jdbc.Driver").newInstance();

				Connection con = null;
				con = DriverManager.getConnection(url, user1, password);
				if (con != null)
					System.out.println("connection build");

				Statement stt = con.createStatement();
				stt.execute("USE microblogging");

				ResultSet res1, res2;
				String str1 = "SELECT * FROM message WHERE user2 =" + "'" + loginid + "' "
						+ "order by date desc limit 0, 5";

				res1 = queryClient(1, stt, str1);

				JSONObject obj_loop = new JSONObject();

				int i = 1;
				int flag = 0;
				while (res1.next()) {

					obj_loop.put("user" + i, res1.getString("user1"));
					obj_loop.put("msg" + i, res1.getString("user_msg"));
					obj_loop.put("date" + i, res1.getString("date"));
					i++;
					flag = 1;
				}

				String prev_date;
				if (flag == 1) {
					obj_loop.put("id", "7");
					obj_loop.put("count", Integer.toString(i - 1));
					String client_Text1 = obj_loop.toString() + "\n";
					// System.out.println(client_Text1);
					sendtoClient(client_Text1);
					System.out.println(client_Text1);
					prev_date = (String) obj_loop.get("date1");

				} else {
					prev_date = "2015-11-08 17:12:00";

				}

				while (true) {
					JSONObject obj_loop1 = new JSONObject();
					// String str2 = "SELECT * FROM follower WHERE user1 = '" +
					// loginid + "'";

					// ResultSet res3 = queryClient(1, stt, str2);

					// String str3 = "(user_id = '" + loginid + "') ";

					// while (res3.next()) {
					// str3 = str3 + " or (user_id = '" +
					// res3.getString("user2") + "')";
					// }

					ResultSet loop = queryClient(1, stt, "SELECT  * FROM message where (date > '" + prev_date + "')"
							+ " and (user2 ='" + loginid + "') order by date desc limit 0, 10");

					// System.out.println("SELECT * FROM message where ((date >
					// '" + prev_date + "')"+ " and (user2 ='"+ loginid +"'))
					// order by date desc limit 0, 10");
					i = 1;
					flag = 0;
					while (loop.next()) {

						if (flag == 0) {
							prev_date = loop.getString("date");
							flag = 1;
						}

						System.out.println("Date " + loop.getString("date"));
						obj_loop1.put("user" + i, loop.getString("user1"));
						obj_loop1.put("msg" + i, loop.getString("user_msg"));
						obj_loop1.put("date" + i, loop.getString("date"));

						i++;
					}

					if (flag == 1) {
						obj_loop1.put("id", "7");
						obj_loop1.put("count", Integer.toString(i - 1));
						String client_Text1 = obj_loop1.toString() + "\n";
						sendtoClient(client_Text1);
						System.out.println(client_Text1);
						flag = 0;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();

			}

			/*
			 * try {
			 * 
			 * System.out.println("Entered in output thread");
			 * 
			 * ResultSet res_out1 = null; // ResultSet res_out2 = null; String
			 * str1 = "SELECT * FROM message WHERE user2 =" + "'" + loginid +
			 * "' " + "order by date desc limit 0, 5"; //
			 * System.out.println("adsda" + loginid); res_out1 = queryClient(1,
			 * stt, str1);
			 * 
			 * /* String str="'"+loginid+"'"; System.out.println(
			 * "initial vaue of str is= "+ str+ " res "+res1 + " sdf ");
			 * 
			 * //System.out.println("RES1 is "+res1.next() +"hbk\n");
			 * 
			 * while(res1.next()) { str=str+" or "
			 * +"'"+res1.getString("user2")+"'";
			 * 
			 * } //res1.close(); System.out.println( "final value of string is="
			 * +str);
			 * 
			 * //res2 = stt.executeQuery( "SELECT * FROM posts WHERE user_id ="+
			 * str + "order by date desc limit 0, 5"); res2 = queryClient(1,stt,
			 * "SELECT * FROM posts WHERE user_id ="+ str +
			 * "order by date desc limit 0, 5"); DON'T REMOVE
			 */
			/*
			 * String[] user = new String[100]; String[] user_msg = new
			 * String[100]; String[] date = new String[100];
			 * 
			 * int count = 0;
			 * 
			 * while (res_out1.next() && count < 5) {
			 * 
			 * System.out.println( res_out1.getString("user1") +
			 * res_out1.getString("date") + res_out1.getString("user_msg"));
			 * 
			 * user[count] = res_out1.getString("user1"); date[count] =
			 * res_out1.getString("date"); user_msg[count] =
			 * res_out1.getString("user_msg"); count++;
			 * 
			 * } JSONObject obj_out = new JSONObject();
			 * 
			 * // Mysql query for taking the elements from the
			 * 
			 * // for(int i=0;i<count;i++)
			 * 
			 * obj_out.put("id", "7"); obj_out.put("count",
			 * Integer.toString(count));
			 * 
			 * obj_out.put("user1", user[0]); obj_out.put("msg1", user_msg[0]);
			 * obj_out.put("date1", date[0]);
			 * 
			 * obj_out.put("user2", user[1]); obj_out.put("msg2", user_msg[1]);
			 * obj_out.put("date2", date[1]);
			 * 
			 * obj_out.put("user3", user[2]); obj_out.put("msg3", user_msg[2]);
			 * obj_out.put("date3", date[2]);
			 * 
			 * obj_out.put("user4", user[3]); obj_out.put("msg4", user_msg[3]);
			 * obj_out.put("date4", date[3]);
			 * 
			 * obj_out.put("user5", user[4]); obj_out.put("msg5", user_msg[4]);
			 * obj_out.put("date5", date[4]); System.out.println("Dates: " +
			 * date[0] + " " + date[1] + " " + date[2] + " " + date[3] + " " +
			 * date[4]); // obj_out.put("id","9");
			 * 
			 * String msg_Text1 = obj_out.toString() + "\n";
			 * sendtoClient(msg_Text1); System.out.println(
			 * "First time message sent"); while (true) { // this code will run
			 * every time keeps updating the // activity // screen. ResultSet
			 * msg; JSONObject obj_msg = new JSONObject(); // loop =
			 * stt.executeQuery("SELECT * FROM post WHERE // user1 = //
			 * "+ str +"order by date desc limit 0, 5"+" and date > // "); /*
			 * SELECT * FROM `posts` WHERE date > '2015-11-12 14:33:00' AND
			 * user_id = 'ritesh' ORDER BY date DESC LIMIT 0 , 5; DON'T REMOVE
			 */
			// loop=stt.executeQuery("SELECT * FROM posts where date
			// >"+"'"+date[0]+"'"+"and user_id="+str+"order by date
			// desc
			// limit 0, 5");
			/*
			 * msg = queryClient(1, stt, "SELECT  * FROM message where date >" +
			 * "'" + date[0] + "'" + "and user2= " + "'" + loginid + "'" +
			 * "order by date desc"); int msg_count = 0; int i = 0; int flag =
			 * 0;
			 * 
			 * while (msg.next()) { System.out.println("Query run: " + date[0]);
			 * if (flag == 0) { date[0] = msg.getString("date"); flag = 1; }
			 * obj_msg.put("user" + i, msg.getString("user1"));
			 * obj_msg.put("post" + i, msg.getString("user_msg"));
			 * obj_msg.put("date" + i, msg.getString("date"));
			 * 
			 * i++; msg_count++;
			 * 
			 * } // System.out.println("new date="+date[0]);
			 * obj_msg.put("count", Integer.toString(msg_count));
			 * obj_msg.put("id", "7");
			 * 
			 * String msg_Text2 = obj_out.toString() + "\n"; if (flag == 1) {
			 * sendtoClient(msg_Text2); } Thread.sleep(1000);
			 * 
			 * }
			 * 
			 * } catch (Exception e) {
			 * 
			 * e.printStackTrace(); }
			 */
		}

	}

	/*******************************************************************************************************************/

	public synchronized void sendtoClient(String str) {
		try {
			bWriter.write(str);
			bWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static synchronized ResultSet queryClient(int w, Statement stt1, String str) {
		if (w == 1) {
			try {

				return stt1.executeQuery(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// return null;
		} else if (w == 2) {
			try {

				stt1.execute(str);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (w == 3) {
			try {

				stt1.executeUpdate(str);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

}
