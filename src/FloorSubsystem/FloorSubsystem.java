package FloorSubsystem;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.ArrayList;
import java. util. Scanner;

import Common.buttonClass;
import Common.lampsClass;

import java.io.ByteArrayOutputStream;
import java.io.File;


import java.util.Scanner;



public class FloorSubsystem {	
	//this will be used when the timing is introduced in the next iteration, right now it does nothing
	Date time;
	//These two lamps are the directional lamp indicating which way the elevator is headed.
	public ArrayList<lampsClass> lampsUp = new ArrayList<lampsClass>();
	public ArrayList<lampsClass> lampsDown = new ArrayList<lampsClass>();
	//This is the floornumber of the subsystem
	public int floorNumber; 
	//this boolean is used exclusively for the iteration1 interaction i set up
	//this int is used exclusively for the iteration1 interaction i set up
	
	//this will be the buttons on the wall, to call an elevator up/down. Not used in iteration1
	buttonClass ButtonUp = new buttonClass();
	buttonClass ButtonDown = new buttonClass();
	//These lamps indicate which of the above buttons have been pressed. Not used in iteration1
	lampsClass ButtonUpLamp = new lampsClass();
	lampsClass ButtonDownLamp = new lampsClass();

	//these are the packets i use to send/ receive data. I've determined it is unnecessary to have them as fields and thiswill be changed
	//in the next iteration
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;

	// we can run in normal (send directly to server) or test
	// (send to simulator) mode
	
	//the constructor, initializes fields and opens socket
	public FloorSubsystem(int floor, int numberOfElevators) {
		//init fields
		for (int i = 0; i < numberOfElevators; i++)
		{
			lampsUp.add(new lampsClass());
			lampsDown.add(new lampsClass());
			
			lampsUp.get(i).setLamps(false);
			lampsDown.get(i).setLamps(false);		
		}
		ButtonUp.setButton(false);
		ButtonDown.setButton(false);
		ButtonUpLamp.setLamps(false);
		ButtonDownLamp.setLamps(false);
		
		floorNumber = floor;

		{
			   try {
			      // Construct a datagram socket and bind it to any available
			      // port on the local host machine. This socket will be used to
			      // send and receive UDP Datagram packets. binding to 3330 + floor number, 
			      sendReceiveSocket = new DatagramSocket(3330 + floor);
			   } catch (SocketException se) {   // Can't create the socket.
			      se.printStackTrace();
			      System.exit(1);
			   }
			}
	}
	//this sends a pickup request along with the requested destination, design will change as we pass through each iteration
	//it was unclear how the floor the elevator should goto gets handled this iteration, so I decided to let the floor subsytem 
	//do it since apparently it will be doing so in the future when it reads from a text file.
	public void send(int direction, int destination, String timeStamp)
	{
		
	    System.out.println("[FloorSubsystem]: Requesting Elevator to floor "+floorNumber+" to ride to floor "+destination+".\n");
	    byte[] data = new byte[4];
	    
	    data[0] = (byte) 1;//SENDER [who this packet came from, 0 = elevator, ***1 = FLOOR SUBSYSTEM***]
	    data[1] = (byte) floorNumber;//PICKUP FLOOR [the floor number to pickup the passenger]
	    data[2] = (byte) direction;//DIRECTION [The direction the elevator will go AFTER reaching the pickup 1 = Up, 2 = down]
	    data[3] = (byte) destination;//DESTINATION FLOOR [the floor the elevator must stop at to drop off its passenger]
	    
	    data[4] = (byte) Integer.parseInt(timeStamp.split(":")[0]);//hours
	    data[5] = (byte) Integer.parseInt(timeStamp.split(":")[1]);//minutes
	    data[6] = (byte) Integer.parseInt(timeStamp.split(":")[2]);//seconds
	    data[7] = (byte) Integer.parseInt(timeStamp.split(":")[3]);//Last byte of the milliseconds
	    data[8] = (byte) Integer.parseInt(timeStamp.split(":")[3]);//second last byte of the milliseconds
	    
	    //send the packet
	    try {
	          sendPacket = new DatagramPacket(data, data.length,
	                                    InetAddress.getLocalHost(), 1111);
	      }
	      catch (UnknownHostException e) {
	          e.printStackTrace();
	          System.exit(1);
	       }
	    
	      try {
		        sendReceiveSocket.send(sendPacket);
		      } catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
		      }
	}
	
	
	public void receive() {
	    byte data[] = new byte[100];
	    receivePacket = new DatagramPacket(data, data.length);
	    // Waiting until a datagram packet is received from receiveSocket.
	      try {        
	         System.out.println("[FloorSubsystem "+floorNumber+"]Waiting..."); // so we know we're waiting
	         sendReceiveSocket.receive(receivePacket);
	      } catch (IOException e) {
	         System.out.print("IO Exception: likely:");
	         System.out.println("Receive Socket Timed Out.\n" + e);
	         e.printStackTrace();
	         System.exit(1);
	      }
	      
	      int len = receivePacket.getLength();
	      System.out.println("[FloorSubsystem " + floorNumber + "]: Packet received containing: " + makeString(data, len) );

	      

	      //first i update the directional lamp
	      if (data[1] == 1)
	      {
	    	  lampsUp.get(data[4]).setLamps(true);
	    	  lampsDown.get(data[4]).setLamps(false);
	      }
	      else if (data[1] == 2) {
	    	  lampsDown.get(data[4]).setLamps(false);
	    	  lampsUp.get(data[4]).setLamps(true);

	      }
	      else if (data[1] == 0)
	      {
	    	  lampsDown.get(data[4]).setLamps(false);
	    	  lampsUp.get(data[4]).setLamps(false);
	      }
	      //Then I check to see if it has arrived or is on the way
	      if (data[3] == 1)
	      {
	    	  //if its arrived I turn off the buttons used to call the elevator based on the direction of the elevator
		      if (data[1] == 0)
		      {
		  		ButtonUpLamp.setLamps(false);
		  		ButtonUp.setButton(false);
		      }
		      else if (data[1] == 1) {
				ButtonDownLamp.setLamps(false);
				ButtonDown.setButton(false);
		      }
	    	  System.out.println("[FloorSubsystem "+floorNumber+"]Elevator arrived!");
	    	  
	    	  
	      }
	      else
	      {
	    	  System.out.println("[FloorSubsystem "+floorNumber+"]Elevator on the way!");
	      }
	}
	
	/* This is how i thought the subsystem should work, and then the elevator would decide what floor
	 * however based on the future requirement of this class reading from a file and sending what floor to goto
	 * i decided to refactor a bit
	 *  and replace this with iteration1Interact() so the scheduler could be easily interacted with.
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
	 */
	
	
	//A support method that converts a byte[] into a string;
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

	public static void main( String args[] )
	{

	}
	
}

