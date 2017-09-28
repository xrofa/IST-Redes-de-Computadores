import java.io.*;
import java.net.*;
import java.util.*;



// Classe TCP Server
class TCPServer implements Runnable {
    private int port;
    
    
    
    ServerSocket echoServer = null;
    Socket clientSocket = null;
    int numConnections = 0;
    
    public TCPServer(int port) {
        this.port = port;
    }
    
    public void getIP(){
    InetAddress ip;
    
    try {
        ip = InetAddress.getLocalHost();
        System.out.println("Current IP address : " + ip.getHostAddress());
    }
    catch (UnknownHostException e) {
        e.printStackTrace(); //TODO
    }
    }
    
    public void stopServer() {
        System.out.println( "Server shutting down." );
		System.exit(0);
    }
    
    public void startServer() {
        try {
            echoServer = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println(e);
        }
        
        getIP();
		
        System.out.println( "Server has been started on port: " + port );	
		
        while ( true ) {
            try {
                clientSocket = echoServer.accept();
                numConnections ++;
                ServerConnection oneconnection = new ServerConnection(clientSocket, numConnections, this);
                
                new Thread(oneconnection).start();
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    
    
//Metodo para correr o servidor
    public void run() {
        //System.out.println("TCP Porta: " + port);
        TCPServer server = new TCPServer( port );
		server.startServer();
    }
}

// Classe UDP Server
class UDPServer implements Runnable {
    private int port;
    public UDPServer(int port) {
        this.port = port;
    }
    
     
    //simple function to echo data to terminal
    public static void echo(String msg)
    {
        System.out.println("---WORKING SERVER---");
        System.out.println(msg);
        System.out.println("---WORKING SERVER---");
    }
    
//Metodo para correr o servidor
    public void run() {
        //System.out.println("UDP Porta: " + port);
        
        DatagramSocket sock = null;
         
        try
        {
            //1. creating a server socket, parameter is local port number
            sock = new DatagramSocket(port);
             
            //buffer to receive incoming data
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
             
            //2. Wait for an incoming data
            echo("UDP Server socket created. Waiting for incoming data...");
             
            //communication loop
            while(true)
            {
                sock.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());
                 
                //echo the details of incoming data - client ip : client port - client message
                echo(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);
                 
                s = "OK : " + s;
                DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
                sock.send(dp);
            }
        }
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }
}
   
class ServerConnection implements Runnable {
  	  BufferedReader is;
  	  PrintStream os;
  	  Socket clientSocket;
  	  int id;
  	  TCPServer server;
	  String loggedUser = null;

  	  public ServerConnection(Socket clientSocket, int id, TCPServer server) {
		this.clientSocket = clientSocket;
		this.id = id;
		this.server = server;
		System.out.println("Connection " + id + " established");
        System.out.println("ClientSocket: " + clientSocket);
        //System.out.println("From IP: " + clientSocket.getInetAddress() );
          
		try {
		    is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    os = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
		    System.out.println(e);
		}
 	   }
//////////////////////////// Threaded Server Connection Instance //////////////////
 	    	   public void run() {
 	       	String line;
		String response;
		String inbox;
		int nrTokens;
		
           try {
		    boolean serverStop = false;
       	     while (true) {
			response = "Invalid Command";
			
       	       		line = is.readLine();
			StringTokenizer str = new StringTokenizer(line);
			nrTokens = str.countTokens();
			//System.out.println( "Received -" + line + "- from Connection " + id + "." );
            System.out.println("---CENTRAL SERVER---");
            System.out.println("Received the command -"+line+"- from "+ clientSocket.getInetAddress() + ":"+ clientSocket.getLocalPort());
            System.out.println("---CENTRAL SERVER---");
			//System.out.println( "Number of tokens: " + nrTokens + "\nToken 1: -" + str.nextToken() + "-." );
			
            str = new StringTokenizer(line);
			if (str.nextToken().equals("LST")){
				if (nrTokens != 3) {  response = "Recebi o comando LST";
				} else {
				//response = server.addUser(str.nextToken() + " " + str.nextToken());
				}	
			}
			
            os.println(response); 
                 
			if (line.equals("exit")) {
				if (loggedUser != null) {//server.logoutUser(loggedUser);
				System.out.println( "passo por aqui");}
			 	break;
            }
		}

	   	 System.out.println( "Connection " + id + " closed." );
           	 is.close();
           	 os.close();
           	 clientSocket.close();

	   	 if ( serverStop ) server.stopServer();
		} catch (IOException e) {
		    System.out.println(e);
		    if (loggedUser != null) {}//server.logoutUser(loggedUser);}
		}
		  catch (NullPointerException e) {
		    System.err.println("Connection has been lost with Client " + id);
		    if (loggedUser != null) {}//server.logoutUser(loggedUser);}
		}
    	}
}

public class CS {
  public static void main (String[] args) {
    
    int port = Integer.parseInt(args[0]); //recebe a porta para criar os servidores
      
      
    Thread tcp = new Thread(new TCPServer(port));
    tcp.start(); //cria thread para o server de TCP
      
    Thread udp = new Thread(new UDPServer(port));
    udp.start(); //cria thread para o server de UDP
    
  }
}