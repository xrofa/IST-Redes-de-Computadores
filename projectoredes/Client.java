// java Client tejo.tecnico.ulisboa.pt 58011
import java.io.*;
import java.net.*;

public class Client extends Thread {
	
	static Socket clientSocket = null;  
	static DataOutputStream os = null;
	static BufferedReader is = null;

    public static void main(String[] args) {
	
        int port;
        String hostname = args[0];
        
        if(args.length == 1 ){
            port = 58000 + 32;
        } else{
            port = Integer.parseInt(args[1]);
        }
        
        System.out.println("Input Port: " + port);
        
///// CREATING THE SOCKET ///// 
        try {
            clientSocket = new Socket(hostname, port);
            os = new DataOutputStream(clientSocket.getOutputStream()); // Output
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Input
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostname);
        } catch (IOException e) {
            System.err.println("Couldn't connect to: " + hostname  + " with port: " + port);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Please use a valid host and port.");
	    return;
        }
	
        if (clientSocket == null || os == null || is == null) {
            System.err.println( "Perhaps the server is not running?" );
            return;
	       }

///// INITIALIZING CLIENT - WRITE SIDE /////	
	
        Client cliente = new Client();
        cliente.start();

        System.out.println("/--- AVAILABLE COMMANDS ARE ---/");
        System.out.println("-list");
        System.out.println("-request [PTCn] [filename]");
        System.out.println("-exit");
        System.out.print( "Enter a command: " );
        
        try {
            while ( true ) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String keyboardInput = br.readLine();

                if (keyboardInput.equals("list")) os.writeBytes("LST\n");
                if (keyboardInput.equals("request")); //TODO
                if (keyboardInput.equals("exit")) break; //break;
            }
            
            os.close();
            is.close();
            clientSocket.close(); 
            
        } catch (UnknownHostException e) {
            System.err.println("Trying to connect to unknown host: " + e);
        } catch (IOException e) {}
    }  

	
    public void run(){
///// INITIALIZING CLIENT - READ SIDE /////	
		try {
	  	  while ( true ) {
			String responseLine = is.readLine();
			if (responseLine.equals("exit")) break;
			System.out.print("Received Message from connection: " + responseLine + "\n");
		    }
		}  catch (IOException e) {
		    System.err.println("Connection with server closed.");
            return;
		}
		  catch (NullPointerException e) {
		    System.err.println("Connection with server has been lost.");
            return;
		}
	}         
}
