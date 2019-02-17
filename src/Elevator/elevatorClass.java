package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
	         contactSocket = new DatagramSocket(2220+this.elevatorNumber);
	         
	        
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
				  addToVisit((int) data[2]);	


	}
	private synchronized void addToVisit(int floor)
	{
		floorsToVisit.add(floor);
		if (stateMachineEnum == StateMachineEnum.STATIONARY)
		{
			
			if (floor > motor.getCurrentFloor())
			{
				
				floorsToVisit.sort(Comparator.naturalOrder());			
				stateMachineEnum = StateMachineEnum.GOING_UP;

			}
			else if (floor < motor.getCurrentFloor())
			{
				floorsToVisit.sort(Comparator.reverseOrder());
				stateMachineEnum = StateMachineEnum.GOING_DOWN;
			}
			else
			{
				floorsToVisit.sort(Comparator.naturalOrder());
			}
		}
		else if (stateMachineEnum == StateMachineEnum.GOING_UP)
		{
			floorsToVisit.sort(Comparator.naturalOrder());
		}
		else 
		{
			floorsToVisit.sort(Comparator.reverseOrder());
		}
		
	}
	private synchronized void removeFloor(int index)
	{
		floorsToVisit.remove(index);
		if (floorsToVisit.isEmpty())
		{
			stateMachineEnum = StateMachineEnum.STATIONARY;
		}
	}
	private synchronized int getNextFloor()
	{
		return floorsToVisit.get(0);
	}
	//Notifies the floorSubsytem of the elevators status
	private void notifyScheduler(int destinationFloor, int status) {
		byte[] data = new byte[7];
	    
	    data[0] = (byte) destinationFloor;//floornumber of floor headed too
	    data[1] = (byte) stateMachineEnum.ordinal();//elevator direction ----- 0 = stationary, 1 = Up, 2 = down
	    data[2] = (byte) status;//status. 0 = on the way, 1= arrived
	    if (floorsToVisit.isEmpty()) {
			data[3]=0;
		}else {
			data[3]=1;
		}
	    data[4] = (byte) 0; // message from elevator
	    //returns 1 if the elevator is now idle 0 otherwise
	    data[5] = (byte) motor.getCurrentFloor(); //Current floor of the elevator
	    data[6] = (byte) elevatorNumber;
	    
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
		 System.out.println("Starting Elevator");
		 Thread receiving = new Thread(() -> {
				while (true) {
					receiveCall();
				}
			});
		 receiving.start();
		 int broadcast = 0;
		 while (true)
		 {		
			 	if (!floorsToVisit.isEmpty())
			 	{
			 		if (getNextFloor() > motor.getCurrentFloor())
			 		{
			 			stateMachineEnum = StateMachineEnum.GOING_UP;
			 		}
			 		else if (getNextFloor() < motor.getCurrentFloor())
			 		{
			 			stateMachineEnum = StateMachineEnum.GOING_DOWN;
			 		}
			 	}

			 switch(stateMachineEnum)
			 {

			 	case STATIONARY:
			 		if (!floorsToVisit.isEmpty() && motor.getCurrentFloor() == getNextFloor())
			 		{
		 				doors.setDoorState(true);
		 				doors.setDoorState(false);
		 				removeFloor(0);
		 				 if (floorsToVisit.isEmpty())
		 				 {
		 					 stateMachineEnum = StateMachineEnum.STATIONARY;
		 				 }
		 				System.out.println("[elevator "+elevatorNumber+"]: cycled doors at "+ motor.getCurrentFloor());		 				
			 		}
			 		else
			 		{
			 			if (broadcast == 0)
			 			{
			 				broadcast = 10;
			 			}
			 			else
			 			{
			 				broadcast--;
			 				try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			 			}
			 		}
			 		break;
			 	case GOING_UP:
			 		System.out.println("[elevator "+elevatorNumber+"]:At "+ motor.getCurrentFloor() + " enroute to floor " + getNextFloor());
	 				notifyScheduler(getNextFloor(), 0);//notify the floor subsystem of impending arrival
			 		motor.move(motor.getCurrentFloor() + 1);
			 		broadcast = 0;
			 		if (getNextFloor() == motor.getCurrentFloor())
			 		{
			 			stateMachineEnum = StateMachineEnum.STATIONARY;
			 		}
			 		break;
			 	case GOING_DOWN:
			 		System.out.println("[elevator "+elevatorNumber+"]:At "+ motor.getCurrentFloor() + " enroute to floor " + getNextFloor());
	 				notifyScheduler(getNextFloor(), 0);//notify the floor subsystem of impending arrival
			 		motor.move(motor.getCurrentFloor() - 1);
			 		broadcast = 0;
			 		if (getNextFloor() == motor.getCurrentFloor())
			 		{
			 			stateMachineEnum = StateMachineEnum.STATIONARY;
			 		}
			 		break;
			 }
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