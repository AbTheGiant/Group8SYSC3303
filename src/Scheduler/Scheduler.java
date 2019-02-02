package Scheduler;
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
	DatagramSocket receiveSocket, SendSocket;
	
	public Scheduler() {
				
	}
	
	private void initSockets() {
		try {
	         // Construct a datagram socket and bind it to
	         // port 2222. This socket will be used to
	         // send UDP Datagram packets to the elevator systems
	         SendSocket = new DatagramSocket();
	         
	         //floor send socket
	         SendSocket =  new DatagramSocket();

	         // Construct a datagram socket and bind it to port 1111 
	         // This socket will be used to
	         // receive UDP Datagram packets.from the floor and elevator systems````
	         receiveSocket = new DatagramSocket(1111);
	         
	         // to test socket timeout (2 seconds)
	         //receiveSocket.setSoTimeout(2000);
	      } catch (SocketException se) {
	         se.printStackTrace();
	         System.exit(1);
	      } 
		
	}

	public void sendReceive() {
		
		initSockets();
		
	    byte data[] = new byte[100];
	    receivePacket = new DatagramPacket(data, data.length);
	    System.out.println("Scheduler: Waiting for Packet.\n");
	    
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
	      
	      System.out.println("Scheduler: Packet received:");
	      System.out.println("From host: " + receivePacket.getAddress());
	      System.out.println("Host port: " + receivePacket.getPort());
	      int len = receivePacket.getLength();
	      System.out.println("Length: " + len);
	      System.out.print("Containing: " );

	      // Form a String from the byte array.
	      System.out.println(makeString(data, len) + "\n");
	     
	      //if the the 4th info is 1 then sendpacket to elevator,
	      //otherwise if 0, then send to the floor 
	      String dest = "";
	      if(data[4] == 1) {
	    	  dest = "Elevator"; 
	    	  byte [] dataToElevator = new byte [2];
		      dataToElevator[0] = data[0];  //This is the position of the value for the floor number
		      dataToElevator[1] = data [1]; //This is the direction of the Elevator 0=down, 1=up
		      //Sending the floor number and elevator button to Elevator system
		      try {
		          sendPacket = new DatagramPacket(dataToElevator, dataToElevator.length,
		                                    InetAddress.getLocalHost(), 2222);
		      }
		      catch (UnknownHostException e) {
		          e.printStackTrace();
		          System.exit(1);
		       }
	      }
	      else if(data[4] == 0) {
	    	  dest = "Floor Subsystem"; 
	    	  byte [] dataToFloor = new byte [1];
		      dataToFloor[0] = data[0];//floor number
		      dataToFloor[1] = data[1];//direction, 0 = down, 1 = up;
		      //Sending the floor number and floor direction
		      try {
		    	  sendPacket = new DatagramPacket(dataToFloor, dataToFloor.length,InetAddress.getLocalHost(),3333);
		      }
		      catch (UnknownHostException e) {
		          e.printStackTrace();
		          System.exit(1);
		       }
	      }
	   
	      System.out.println("Scheduler:\n Sending packet to "+dest+" :");
	      System.out.println("To host: " + sendPacket.getAddress());
	      System.out.println("Destination host port: " + sendPacket.getPort());
	      int len2 = sendPacket.getLength();
	      System.out.println("Length: " + len2);
	      System.out.println("Containing: "+ makeString(sendPacket.getData(), sendPacket.getLength()) + "\n");

	      // Send the datagram packet to the server via the send/receive socket. 



	      System.out.println("Client: Packet sent.\n");

	      SendSocket.close();
	      receiveSocket.close();
	}
	public static String makeString(byte[] data, int length)
	{
		String retVal = "";
		for (int i = 0; i < length; i++)
		{
			retVal+= data[i] + ",";
		}
		retVal.substring(0, retVal.length()-1);
		return retVal;
	}
	
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler ();
		while (true)
		{
			scheduler.sendReceive();
		}
	}
}
