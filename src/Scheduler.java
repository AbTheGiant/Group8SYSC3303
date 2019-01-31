//Scheduler.java
//This class manages the interaction between the Floor Subsytems and Elevator class.

import java.io.*;
import java.net.*;

public class Scheduler {

	//Floor variables
	int floorNumber;
	String floorDirection;
	
	//Elevator variables
	int elevatorButton;
	
	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket, receiveSocket;
	
	public Scheduler() {
		
		try {
	         // Construct a datagram socket and bind it to
	         // port 2222. This socket will be used to
	         // send UDP Datagram packets to the floor and elevator systems
	         sendSocket = new DatagramSocket(2222);

	         // Construct a datagram socket and bind it to port 1111 
	         // This socket will be used to
	         // receive UDP Datagram packets.from the floor and elevator systems
	         receiveSocket = new DatagramSocket(1111);
	         
	         // to test socket timeout (2 seconds)
	         //receiveSocket.setSoTimeout(2000);
	      } catch (SocketException se) {
	         se.printStackTrace();
	         System.exit(1);
	      } 
		
	}
	
	public void Receive() {
		
	    byte data[] = new byte[100];
	    receivePacket = new DatagramPacket(data, data.length);
	    System.out.println("scheduler: Waiting for Packet.\n");
	    
	    // Waiting until a datagram packet is received from receiveSocket.
	      try {        
	         System.out.println("Waiting..."); // so we know we're waiting
	         receiveSocket.receive(receivePacket);
	      } catch (IOException e) {
	         System.out.print("IO Exception: likely:");
	         System.out.println("Receive Socket Timed Out.\n" + e);
	         e.printStackTrace();
	         System.exit(1);
	      }
	      
	      System.out.println("Server: Packet received:");
	      System.out.println("From host: " + receivePacket.getAddress());
	      System.out.println("Host port: " + receivePacket.getPort());
	      int len = receivePacket.getLength();
	      System.out.println("Length: " + len);
	      System.out.print("Containing: " );

	      sortPacket(data);
	      // Form a String from the byte array.
	      String received = new String(data,0,len);   
	      System.out.println(received + "\n");
	      
	      receiveSocket.close();
	}
	public byte[]  sortPacket(byte[] data) {
		
		
		return null;
		
	}
	public void send(byte[] sendData) {
		
	}
	
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler ();
		scheduler.Receive();
	}
}
