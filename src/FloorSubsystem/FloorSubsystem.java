package FloorSubsystem;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java. util. Scanner;

import Common.buttonClass;
import Common.lampsClass;

import java.io.File;




public class FloorSubsystem {
	
	
	Date time;
	lampsClass lampUp = new lampsClass();
	lampsClass lampDown = new lampsClass();
	int floorNumber; 
	
	
	buttonClass ButtonUp = new buttonClass();
	buttonClass ButtonDown = new buttonClass();
	
	lampsClass ButtonUpLamp = new lampsClass();
	lampsClass ButtonDownLamp = new lampsClass();

	
	
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;

	// we can run in normal (send directly to server) or test
	// (send to simulator) mode
	

	public FloorSubsystem() {
		//
		lampUp.setLamps(false);
		lampDown.setLamps(false);
		ButtonUp.setButton(false);
		ButtonDown.setButton(false);
		ButtonUpLamp.setLamps(false);
		ButtonDownLamp.setLamps(false);
		
		floorNumber = 5;//CONFIGURABLE FLOOR NUMBER GOES HERE

		{
			   try {
			      // Construct a datagram socket and bind it to any available
			      // port on the local host machine. This socket will be used to
			      // send and receive UDP Datagram packets. binding to 3333, although it doesnt matter because it gets remade
			      sendReceiveSocket = new DatagramSocket(3333);
			   } catch (SocketException se) {   // Can't create the socket.
			      se.printStackTrace();
			      System.exit(1);
			   }
			}
	}
	
	public void send(int direction)
	{
		sendReceiveSocket.close();
		   try {
			      sendReceiveSocket = new DatagramSocket();//re-open the socket for Sending
			   } catch (SocketException se) {   // Can't create the socket.
			      se.printStackTrace();
			      System.exit(1);
			   }
	    System.out.println("FloorSubsystem: Requesting Elevator to floor"+floorNumber+".\n");
	    byte[] data = new byte[5];
	    
	    data[0] = (byte) floorNumber;//floornumber of subsystem/destination requested
	    data[1] = (byte) direction;//request direction ----- 0 = Down, 1 = Up
	    data[2] = (byte) 0;//leaving extra space for future endeavors
	    data[3] = (byte) 0;//leaving extra space for future endeavors
	    data[4] = (byte) 1; // sending this to the elevator 
	    
	    try {
	          sendPacket = new DatagramPacket(data, data.length,
	                                    InetAddress.getLocalHost(), 1111);
	      }
	      catch (UnknownHostException e) {
	          e.printStackTrace();
	          System.exit(1);
	       }
	}
	
	
	public void receive() {
		sendReceiveSocket.close();
		   try {
			      sendReceiveSocket = new DatagramSocket(3333);//re-bind the socket for receiving
			   } catch (SocketException se) {   // Can't create the socket.
			      se.printStackTrace();
			      System.exit(1);
			   }
	    byte data[] = new byte[100];
	    receivePacket = new DatagramPacket(data, data.length);
	    System.out.println("FloorSubsystem: Waiting for Packet.\n");
	    // Waiting until a datagram packet is received from receiveSocket.
	      try {        
	         System.out.println("Waiting..."); // so we know we're waiting
	         sendReceiveSocket.receive(receivePacket);
	      } catch (IOException e) {
	         System.out.print("IO Exception: likely:");
	         System.out.println("Receive Socket Timed Out.\n" + e);
	         e.printStackTrace();
	         System.exit(1);
	      }
	      
	      System.out.println("FloorSubsystem: Packet received:");
	      System.out.println("From host: " + receivePacket.getAddress());
	      System.out.println("Host port: " + receivePacket.getPort());
	      int len = receivePacket.getLength();
	      System.out.println("Length: " + len);
	      System.out.print("Containing: " );

	      // Form a String from the byte array.
	      String received = new String(data,0,len);   
	      System.out.println(received + "\n");
	      
	      for (int i = 0; i < data.length;i++)
	      {
	    	  System.out.println(data[0]);
	      }	      
	      //Do something with the given information here
	      if (data[1] == 0)
	      {
	    	  lampUp.setLamps(true);
	    	  lampDown.setLamps(false);
	      }
	      else if (data[1] == 1) {
	    	  lampDown.setLamps(true);
	    	  lampUp.setLamps(false);
	      }
	      
	}
	
	public void pressUpButton()//simulates what happens when someone presses the up button
	{
		ButtonUp.setButton(true);
		ButtonUpLamp.setLamps(true);
		send(1);// send up request
	}
	
	public void pressDownButton()
	{
		ButtonDown.setButton(true);
		ButtonDownLamp.setLamps(true);
		send(0);//send down request

	}

	public static void main( String args[] )
	{
	 
		FloorSubsystem f1 = new FloorSubsystem();
		f1.send(1);
	}
	
}

