// java WS PTC1...PTCn -p WSport -n CSname -e CSport
// WCT = Word Count
// FLW = Find Longets Word
// UPP = Convert Text To Upper Case
// LOW = Convert Text to Lower Case

import java.io.*;
import java.net.*;
 
public class WS
{
    private static final int NUMERO_GRUPO = 7;
    
    public static void main(String args[])
    {
        DatagramSocket sock = null;
        
        int wsport = 59000;
        String CSname = args[1];
        int port = Integer.parseInt(args[0]);
        String s;
        boolean FLW = false;
        
        if(args[1].equals("FLW")){
            System.out.println("FLW: " + FLW);
            FLW = true;
            System.out.println("FLW: " + FLW);
        }
        
        
        
         
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
         
        try
        {
            sock = new DatagramSocket();
             
            InetAddress host = InetAddress.getByName(CSname);
             
            while(true)
            {
                //take input and send the packet
                echo("Enter message to send : ");
                s = (String)cin.readLine();
                byte[] b = s.getBytes();
                 
                DatagramPacket  dp = new DatagramPacket(b , b.length , host , port);
                sock.send(dp);
                 
                //now receive reply
                //buffer to receive incoming data
                byte[] buffer = new byte[65536];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                sock.receive(reply);
                 
                byte[] data = reply.getData();
                s = new String(data, 0, reply.getLength());
                 
                //echo the details of incoming data - client ip : client port - client message
                echo(reply.getAddress().getHostAddress() + " : " + reply.getPort() + " - " + s);
            }
        }
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }
     
    //simple function to echo data to terminal
    public static void echo(String msg)
    {
        
        System.out.println(msg);
        
    }
}