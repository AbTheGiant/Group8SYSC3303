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
 	 //the schedulers address
 	 InetAddress SchedulerAddress;
 	 
 	 //The constructor, sets numFloors and inits all variables
	 public elevatorClass(int numFloors, int elevatorNumber, InetAddress scheduler){
		this.numFloors=numFloors; 	
		this.elevatorNumber=elevatorNumber;
		SchedulerAddress = scheduler;
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

	
	public int receiveCall() {
		
		//create new datagram packet for receiving
				byte[] data= new byte[100];
			    contactPacket = new DatagramPacket(data, data.length);
			      
			      // Block until a datagram packet is received from receiveSocket.
			      try {        
			        // System.out.println("[elevator "+elevatorNumber+"]:Waiting for scheduler..."); // so we know we're waiting
			         contactSocket.receive(contactPacket);
			      } catch (IOException e) {
			         System.out.print("IO Exception: likely:");
			         System.out.println("Receive Socket Timed Out.\n" + e);
			         e.printStackTrace();
			         System.exit(1);
			      }
			     
			      //pickup, destination
				  return (int) data[0];	


	}
	private synchronized void executeCommand(int command)
	{
		
		switch (command) {
		case 0:
			stateMachineEnum = StateMachineEnum.STATIONARY;
			break;
		case 1:
			stateMachineEnum = StateMachineEnum.GOING_UP;
			break;
		case 2:
			stateMachineEnum = StateMachineEnum.GOING_DOWN;
			break;
		case 3:
			stateMachineEnum = StateMachineEnum.OPENING_DOORS;
			break;
		case 6:
			stateMachineEnum = StateMachineEnum.SOFT_FAULT;
			break;
		case 7:
			stateMachineEnum = StateMachineEnum.HARD_FAULT;
			break;
		default:
			break;
		}
		
		
	}
	
	//Notifies the floorSubsytem of the elevators status
	private void notifyScheduler(int subsystem,int elevatorNumber,int state,int currentFloor) {
		byte[] data = new byte[4];
	    
	    data[0] = (byte)subsystem;
	    data[1] = (byte)elevatorNumber;//elevator number
	    data[2] = (byte) state;//state. 0 = stationary, 1 = Up, 2 = down, 3 = openingDoors, 4 = open, 5 = closingDoors
	    data[3]=(byte)currentFloor;//current elevator floor
	  
	    
	    try {
	          contactPacket = new DatagramPacket(data, data.length,
	                                    SchedulerAddress, 1111);
	      }
	      catch (Exception e) {
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
		 while (true)
		 {		
			 	
			 switch(stateMachineEnum)
			 {
			 	case STATIONARY:
			 		System.out.println("[elevator "+elevatorNumber+"]:At "+ motor.getCurrentFloor() + " is stationary ");
	 				notifyScheduler(0,elevatorNumber,0,motor.getCurrentFloor());//notify the scheduler of current state

			 		break;
			 	case GOING_UP:
			 		System.out.println("[elevator "+elevatorNumber+"]:At "+ motor.getCurrentFloor() + " enroute to floor " + (motor.getCurrentFloor() +1));
			 		
			 		motor.move(motor.getCurrentFloor() + 1);
	 				notifyScheduler(0,elevatorNumber,1,motor.getCurrentFloor());//notify the scheduler subsystem of impending arrival
			 		break;
			 	case GOING_DOWN:
			 		System.out.println("[elevator "+elevatorNumber+"]:At "+ motor.getCurrentFloor() + " enroute to floor " + (motor.getCurrentFloor() - 1));
			 		motor.move(motor.getCurrentFloor() - 1);
	 				notifyScheduler(0,elevatorNumber,2,motor.getCurrentFloor());//notify the scheduler subsystem of impending arrival
			 		break;
			 	case OPENING_DOORS:
			 		System.out.println("[elevator "+elevatorNumber+"]:At "+ motor.getCurrentFloor() + " Cycling Doors ");
			 		doors.setDoorState(true);
			 		stateMachineEnum=StateMachineEnum.DOORS_OPEN;
	 				notifyScheduler(0,elevatorNumber,3,motor.getCurrentFloor());//notify the scheduler subsystem of impending arrival
	 				receiveCall();
			 		doors.setDoorState(false);
			 		stateMachineEnum=StateMachineEnum.CLOSING_DOORS;
	 				notifyScheduler(0,elevatorNumber,5,motor.getCurrentFloor());//notify the scheduler subsystem of impending arrival
			 		break;
			 	case SOFT_FAULT:
			 		System.out.println("SOFT FAULT OCCURED AT: " + motor.getCurrentFloor());
			 		notifyScheduler(0,elevatorNumber,6,motor.getCurrentFloor());
			 		break;
			 	case HARD_FAULT:
			 		System.out.println("HARD FAULT OCCURED AT: " + motor.getCurrentFloor());
			 		System.exit(0);
			 		break;
			 }
			 executeCommand(receiveCall());
		 }

		 
		 
	}

}