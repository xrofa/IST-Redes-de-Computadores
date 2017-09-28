import java.io.*;
import java.net.*;
import java.util.*;



class TCPServer implements Runnable {
    private int port;
    public TCPServer(int port) {
        this.port = port;
    }

    public void run() {
        System.out.println("TCP Porta: " + port);
    }
}

class UDPServer implements Runnable {
    private int port;
    public UDPServer(int port) {
        this.port = port;
    }

    public void run() {
        System.out.println("UDP Porta: " + port);
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