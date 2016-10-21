
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.HashMap;  
import java.util.Map; 
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
 
public class Client1
{
 
    private static Socket socket;
 
    public static void main(String args[])
    {
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
            
            
            JSONObject obj = new JSONObject();

            obj.put("name","Tinku");
            obj.put("id","1");
            obj.put("loginid","tinku");
            obj.put("password","tinku007");
            
            JSONObject obj1 = new JSONObject();

            obj1.put("post","I am happy code is running  now");
            obj1.put("id","3");
            obj1.put("date","2015-11-11 17:12:00");
            
            
            
         //   System.out.println("Message send from the server : "+obj);
            

            //StringWriter out = new StringWriter();
            //obj.writeJSONString(out);
            
            String jsonText = obj.toString()+"\n";
            String jsonText1 = obj1.toString()+"\n";
          //  System.out.println("Message received from the server : " +jsonText);
            System.out.print(jsonText);
            

            bw.write(jsonText);
            bw.flush();
            bw.write(jsonText1);
            bw.flush();
            System.out.println("Message sent to the server : ");
 
            //Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message = br.readLine();
            System.out.println("Message received from the server : " +message);
           while((message=br.readLine())!=null)
            {
            	System.out.println("Message received from the server : " +message);
            	
            }
            
            
            
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
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
    }
}