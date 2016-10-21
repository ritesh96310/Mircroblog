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


import org.json.simple.JSONValue;
 
public class Server
{
 
    private static Socket clientSocket;
    static ServerSocket serverSocket;
 
    public static void main(String[] args)
    {
        try
        {
 
            int port = 30000;
            serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 30000");
 
            //Server is running always. This is done using this while(true) loop
            
            while(true)
            {
             
                clientSocket = serverSocket.accept();
                System.out.println(clientSocket);
            
                // Make separate thread for each client
                
                Thread t= new  Thread(new ClientHandler(clientSocket));
               // if(t!=null)
                t.start();
                System.out.println("got a connection");
               
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            
        }
        finally
        {
            try
            {
                serverSocket.close();
            }
            catch(Exception e){}
        }
    }
}
































