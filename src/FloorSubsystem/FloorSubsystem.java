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

import java.io.File;


import java.util.Scanner;



public class FloorSubsystem {
	
	//this will be used when the timing is introduced in the next iteration, right now it does nothing
	Date time;
	//These two lamps are the directional lamp indicating which way the elevator is headed.
	public ArrayList<lampsClass> lampsUp = new ArrayList<lampsClass>();
	public ArrayList<lampsClass> lampsDown = new ArrayList<lampsClass>();
	//This is the floornumber of the subsystem
	int floorNumber; 
	//this boolean is used exclusively for the iteration1 interaction i set up
	boolean interact;
	//this int is used exclusively for the iteration1 interaction i set up
	int nextFloor;
	
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
	public void send(int direction, int destination)
	{
		
	    System.out.println("FloorSubsystem: Requesting Elevator to floor "+floorNumber+" to ride to floor "+destination+".\n");
	    byte[] data = new byte[5];
	    
	    data[0] = (byte) floorNumber;//floornumber of subsystem/destination requested
	    data[1] = (byte) direction;//request direction ----- 0 = Down, 1 = Up
	    data[2] = (byte) destination;//The final destination after getting picked up
	    data[3] = (byte) 0;//leaving extra space for future endeavors
	    data[4] = (byte) 1; // sending this to the elevator, 1= elevator, 0=floor subsystem 
	    
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
	      int len = receivePacket.getLength();
	      System.out.print("Containing: " );
	      // Form a String from the byte array.
	      System.out.println(makeString(data, len) + "\n");
	      

	      //first i update the directional lamp
	      if (data[1] == 0)
	      {
	    	  lampsUp.get(data[4]).setLamps(false);
	    	  lampsDown.get(data[4]).setLamps(false);
	      }
	      else if (data[1] == 1) {
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
		      interact = true;
	    	  System.out.println("Elevator arrived!");
	    	  
	    	  nextFloor = data[2];
	    	  
	      }
	      else
	      {
	    	  System.out.println("Elevator on the way!");
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
	
	//the following method (and scanner) are both so that the TA can easily interact with our system
	Scanner reader = new Scanner(System.in);  // Reading from System.in
	public void iteration1Interact()
	{
		while (interact)
		{
			System.out.println("Please enter destination floor: ");
			int n = reader.nextInt(); // Scans the next token of the input as an int.
			if (n > floorNumber)
			{
				send(1,n);
				interact = false;
				ButtonUp.setButton(true);
				ButtonUpLamp.setLamps(true);
			}
			else if (n < floorNumber)
			{
				send(0,n);
				interact = false;
				ButtonDown.setButton(true);
				ButtonDownLamp.setLamps(true);
			}
			else
			{
				System.out.println("That is the floor you are currently on");
			}
		}
	}
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
	//The main inits the subsystem and then lets the user input a floor to goto and switches subsystems to that floor after the elevator 
	//arrives at the current floor. 
	public static void main( String args[] )
	{
	 
		FloorSubsystem f1 = new FloorSubsystem(5, 3);
		f1.interact = true;
		while(true)
		{
			f1.iteration1Interact();
			f1.receive();
			if (f1.interact && f1.floorNumber != f1.nextFloor)
			{
				System.out.println("Switching subsystem to " + f1.nextFloor + ", the destination of the elevator that just came.");
				f1 = new FloorSubsystem(f1.nextFloor, 3);
			}
		}
	}
	
}

