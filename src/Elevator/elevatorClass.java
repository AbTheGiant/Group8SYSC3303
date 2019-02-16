package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

import Common.*;

public class elevatorClass implements Runnable{
	//the number of floors the elevator services
	 int numFloors,elevatorNumber;
	 
	ArrayList<Integer> floorsToVisit= new ArrayList<Integer>();
	
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
	 public elevatorClass(int numFloors, int elevatorNumber){
		this.numFloors=numFloors; 	
		this.elevatorNumber=elevatorNumber;
		stateMachineEnum= StateMachineEnum.STATIONARY;
		//currentFloor=0;
		try {
	        //create the datagram sockets for receiving
	         contactSocket = new DatagramSocket(2222+this.elevatorNumber);
	         
	        
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
			    System.out.println("[elevator "+elevatorNumber+"]: Waiting for packet from scheduler\n");
			      
			      // Block until a datagram packet is received from receiveSocket.
			      try {        
			         System.out.println("[elevator "+elevatorNumber+"]:Waiting for scheduler..."); // so we know we're waiting
			         contactSocket.receive(contactPacket);
			      } catch (IOException e) {
			         System.out.print("IO Exception: likely:");
			         System.out.println("Receive Socket Timed Out.\n" + e);
			         e.printStackTrace();
			         System.exit(1);
			      }

			      // Process the received datagram.
			      System.out.println("[elevator "+elevatorNumber+"]: packet received :");
			      System.out.println("[elevator "+elevatorNumber+"]:From host: " + contactPacket.getAddress() +":"+ contactPacket.getPort());
			      System.out.println("[elevator "+elevatorNumber+"]:Containing: "+ makeString(data, contactPacket.getLength()) );

			      // Form a String from the byte array.
			     
			      //pickup, destination
				  deployElevator(data[0], data[1]);
			      
			     // deployElevator(sortPacket(receivePacket.getData()));
			      //int len = receivePacket.getLength();
			      //System.out.println("Length: " + len);
			     // System.out.println("Containing: " +receivePacket.getData()[0]);

	}
	
	//deploys the elevator to a pickup location, then to the destination the user selects.
	//Expecting heavy changes as the intent of each iteration is made more clear.
	public void deployElevator(int pickupFloor, int destFloor) {

		//pickup the passenger
		gotoFloor(pickupFloor, destFloor);
		

	}
	//Helper for the deployElevator method that goes to a floor and opens/closes the door.
	private void gotoFloor(int finalFloor, int goalFloor) {
		int direction=0;
		floorsToVisit.add(finalFloor);
		floorsToVisit.add(goalFloor);
		Collections.sort(floorsToVisit);
		
		
		if (motor.getCurrentFloor() > floorsToVisit.get(0)) {
			stateMachineEnum = StateMachineEnum.GOING_DOWN;
			System.out.println("[elevator "+elevatorNumber+"]: deploying to floor " + floorsToVisit.get(0));
			direction=0;
			notifyFloor(floorsToVisit.get(0), direction, 0);//notify the floor subsystem of impending arrival
			motor.move(floorsToVisit.get(0));
			stateMachineEnum = StateMachineEnum.STATIONARY;
			System.out.println("[elevator "+elevatorNumber+"]: Arrived at floor " + motor.getCurrentFloor());
			doors.setDoorState(true);
			doors.setDoorState(false);
			notifyFloor(motor.getCurrentFloor(), direction, 1);
			floorsToVisit.remove(0);
		}
		else if (motor.getCurrentFloor() < floorsToVisit.get(0)) {
			stateMachineEnum = StateMachineEnum.GOING_UP;
			System.out.println("[elevator "+elevatorNumber+"]: deploying to floor " + floorsToVisit.get(0));
			direction=1;
			notifyFloor(floorsToVisit.get(0), direction, 0);//notify the floor subsystem of impending arrival
			motor.move(floorsToVisit.get(0));
			stateMachineEnum = StateMachineEnum.STATIONARY;
			System.out.println("[elevator "+elevatorNumber+"]: Arrived at floor " + motor.getCurrentFloor());
			doors.setDoorState(true);
			doors.setDoorState(false);
			notifyFloor(motor.getCurrentFloor(), direction, 1);
			floorsToVisit.remove(0);
		}
		else if (floorsToVisit.isEmpty()) {
			stateMachineEnum = StateMachineEnum.STATIONARY;
			System.out.println("[elevator "+elevatorNumber+"]: "+elevatorNumber+" is Idle on "+ motor.getCurrentFloor());
			notifyFloor(motor.getCurrentFloor(),-1 , 1);//notify the floor subsystem of arrival(-1 for direction if elevator is stationary)

		}
		
		
		
		
		
	}
	//Notifies the floorSubsytem of the elevators status
	private void notifyFloor(int floorToNotify, int direction, int status) {
		byte[] data = new byte[5];
	    
	    data[0] = (byte) floorToNotify;//floornumber of floor to get notified
	    data[1] = (byte) direction;//request direction ----- 0 = Down, 1 = Up
	    data[2] = (byte) status;//status. 0 = on the way, 1= arrived
	    data[3] = (byte) 0; // sending this to the floor
	    //returns 1 if the elevator is now idle 0 otherwise
	    if (floorsToVisit.isEmpty()) {
			data[4]=0;
		}else {
			data[4]=1;
		}
	   
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
	 @Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			receiveCall();
			
		}
		 
		 
	}
	//when run, this will turn on the elevator and put it into an endless listening loop.
//	public static void main(String args[])
//	   {
//		 //start the program
//	      elevatorClass c = new elevatorClass(7,1);
//	      while (true)
//	      {
//	    	  c.receiveCall();
//	      }
//	   }

}