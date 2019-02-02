package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Common.*;

public class elevatorClass {
	//the number of floors the elevator services
	 int numFloors;
	 //the enum holding the state of the elevator
	 StateMachineEnum stateMachineEnum ;
	 //the socket used for sending and receiving messages
	 DatagramSocket contactSocket;
	 //the packet used for sending and receiving messages
	 DatagramPacket contactPacket;
	 //the buttons on the elevator (unused in iteration1)
	 buttonClass [] elevatorButtons = new buttonClass[numFloors+1];
	 //the lamps for the buttons (unused in iteration1)
 	 lampsClass [] elevatorLamps = new lampsClass[numFloors+1];
 	 //the elevator doors
 	 elevatorDoors doors;
 	 //the elevator's motor
 	 elevatorMotor motor;
 	 
 	 //The constructor, sets numFloors and inits all variables
	 public elevatorClass(int numFloors){
		this.numFloors=numFloors; 	
		stateMachineEnum= StateMachineEnum.STATIONARY;
		//currentFloor=0;
		try {
	        //create the datagram sockets for receiving
	         contactSocket = new DatagramSocket(2222);
	         
	        
	      } catch (SocketException se)
	  		{//can't create socket
	         se.printStackTrace();
	         System.exit(1);
	      } 
		
		doors = new elevatorDoors();
		motor = new elevatorMotor(0);
		createButtons();
	 }	 
	 //used to create the buttons
	 public void createButtons() {
		 elevatorButtons = new buttonClass[numFloors];
		 elevatorLamps = new lampsClass[numFloors];
		for (int i = 0; i < numFloors; i++) {
			elevatorButtons[i]=new buttonClass();
			elevatorLamps[i]=new lampsClass();
		}
	 }

	
	public void receiveCall() {
		
		//create new datagram packet for receiving
				byte[] data= new byte[100];
			    contactPacket = new DatagramPacket(data, data.length);
			    System.out.println("elevator: Waiting for packet from scheduler\n");
			      
			      // Block until a datagram packet is received from receiveSocket.
			      try {        
			         System.out.println("Waiting for scheduler..."); // so we know we're waiting
			         contactSocket.receive(contactPacket);
			      } catch (IOException e) {
			         System.out.print("IO Exception: likely:");
			         System.out.println("Receive Socket Timed Out.\n" + e);
			         e.printStackTrace();
			         System.exit(1);
			      }

			      // Process the received datagram.
			      System.out.println("elevator: packet received :");
			      System.out.println("From host: " + contactPacket.getAddress());
			      System.out.println("Host port: " + contactPacket.getPort());
			      System.out.print("Containing: " );

			      // Form a String from the byte array.
			      System.out.println(makeString(data, contactPacket.getLength()) + "\n");
			     

				  deployElevator(data[0], data[2], data[3]);
			      
			     // deployElevator(sortPacket(receivePacket.getData()));
			      //int len = receivePacket.getLength();
			      //System.out.println("Length: " + len);
			     // System.out.println("Containing: " +receivePacket.getData()[0]);

	}
	
	//deploys the elevator to a pickup location, then to the destination the user selects.
	//Expecting heavy changes as the intent of each iteration is made more clear.
	public void deployElevator(int pickupFloor, int destFloor, int direction) {

		//pickup the passenger
		gotoFloor(pickupFloor, destFloor, direction);
		//Deliver the passenger
		gotoFloor(destFloor, destFloor, direction);

	}
	//Helper for the deployElevator method that goes to a floor and opens/closes the door.
	private void gotoFloor(int finalFloor, int goalFloor, int direction) {
		if (motor.getCurrentFloor() > finalFloor) {
			stateMachineEnum = StateMachineEnum.GOING_DOWN;
			notifyFloor(finalFloor, 0, goalFloor, 0);//notify the floor subsystem of impending arrival
		}
		else if (motor.getCurrentFloor() < finalFloor) {
			stateMachineEnum = StateMachineEnum.GOING_UP;
			notifyFloor(finalFloor, 1, goalFloor, 0);//notify the floor subsystem of impending arrival
		}
		else {//if youre already on the pickup floor, toggle doors
			stateMachineEnum = StateMachineEnum.STATIONARY;
			doors.setDoorState(true);
			doors.setDoorState(false);
		}
		
		System.out.println("Elevator: deploying to floor " + finalFloor);
		motor.move(finalFloor);//move to pickup floor
		
		System.out.println("Elevator: Arrived at destination, floor " + finalFloor);
		notifyFloor(finalFloor, direction, goalFloor, 1);//notify the floor subsystem of arrival
		
		doors.setDoorState(true);
		doors.setDoorState(false);
	}
	//Notifies the floorSubsytem of the elevators status
	private void notifyFloor(int floorToNotify, int direction, int destination, int status) {
		byte[] data = new byte[5];
	    
	    data[0] = (byte) floorToNotify;//floornumber of floor to get notified
	    data[1] = (byte) direction;//request direction ----- 0 = Down, 1 = Up
	    data[2] = (byte) destination;//The final destination after getting picked up
	    data[3] = (byte) status; //status. 0 = on the way, 1= arrived
	    data[4] = (byte) 0; // sending this to the floor
	    
	    try {
	          contactPacket = new DatagramPacket(data, data.length,
	                                    InetAddress.getLocalHost(), 1111);
	      }
	      catch (UnknownHostException e) {
	          e.printStackTrace();
	          System.exit(1);
	       }
	    
	      try {
		        contactSocket.send(contactPacket);
		      } catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
		      }
	}
	
	
	 //getter for state
	 public StateMachineEnum getState() {
		 return this.stateMachineEnum; 
		 
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
	 
	//when run, this will turn on the elevator and put it into an endless listening loop.
	public static void main(String args[])
	   {
		 //start the program
	      elevatorClass c = new elevatorClass(7);
	      while (true)
	      {
	    	  c.receiveCall();
	      }
	   }

}