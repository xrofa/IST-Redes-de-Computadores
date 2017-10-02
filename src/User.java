// java User tejo.tecnico.ulisboa.pt 58011
import java.io.*;
import java.net.*;

public class User extends Thread {
	private static final int NUMERO_GRUPO = 7;
    
	static Socket clientSocket = null;  
	static DataOutputStream os = null;
	static BufferedReader is = null;
    
    static int port = 58000+NUMERO_GRUPO;
    static String hostname = "localhost";
    
        
    public static void main(String[] args) {
	
        //TODO
        if(args.length == 4 && args[0].equals("-n") && args[2].equals("-p")){
            hostname = args[1];
            port = Integer.parseInt(args[3]);
            System.out.println("Caso1:hostname: "+hostname);
            System.out.println("Caso1:port: "+port);
        }
        if(args.length == 2 && args[0].equals("-n")){
            hostname = args[1];
            System.out.println("Caso2:hostname: "+hostname);
            System.out.println("Caso2:port: "+port);
        }
        if(args.length == 2 && args[0].equals("-p")){
            port = Integer.parseInt(args[1]);
            //System.out.println("Caso2:hostname: "+hostname);
            System.out.println("Caso2:port: "+port);
        }
           

///// INITIALIZING CLIENT - WRITE SIDE /////	
	
        User cliente = new User();
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
        } catch (IOException e) {
            // TODO
        }
    }  

	
    public void run(){
///// INITIALIZING CLIENT - READ SIDE /////	
        try {
            while ( true ) {
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
                
                String responseLine = is.readLine();
                os.close();
                is.close();
                clientSocket.close(); 
                
                if (responseLine.equals("exit")) break;
                
                System.out.print("Received Message from connection: " + responseLine + "\n");
            }
            
        } catch (IOException e) {
            System.err.println("Connection with server closed.");
            return;
		}
		  catch (NullPointerException e) {
		    System.err.println("Connection with server has been lost.");
            return;
		}
	}        
}
