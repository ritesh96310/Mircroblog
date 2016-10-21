import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class client
{
 
    private static Socket socket;
 
    public static void main(String args[])
    {
        try
        {
            String host = "localhost";
            int port = 25000;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            System.out.println(address);
            System.out.println(socket);
 
            //Send the message to the server
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
 
            //String number = "3";
 
            //String sendMessage = number + "\n";

              JSONObject obj = new JSONObject();
              obj.put("name", "foo");
              obj.put("num", new Integer(100));
              obj.put("balance", new Double(1000.21));
              obj.put("is_vip", new Boolean(true));            

              try (OutputStreamWriter out = new OutputStreamWriter(
            	        socket.getOutputStream(), StandardCharsets.UTF_8)) {
            	        bw.write(obj.toString());
            	}
            ///bw.write(sendMessage);
            bw.flush();
            //System.out.println("Message sent to the server : "+ sendMessage);
 
            //Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message = br.readLine();
            System.out.println("Message received from the server : " +message);
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