package Elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import Common.*;

public class elevatorClass {
	 int numFloors;
	 
	 StateMachineEnum stateMachineEnum ;
	 DatagramSocket receiveCallSocket;
	 DatagramPacket receivePacket;
	 buttonClass [] elevatorButtons = new buttonClass[numFloors+1];
 	 lampsClass [] elevatorLamps = new lampsClass[numFloors+1];
 	 elevatorDoors doors;
 	 elevatorMotor motor;
	 public elevatorClass(int numFloors){
		this.numFloors=numFloors; 	
		stateMachineEnum= StateMachineEnum.STATIONARY;
		//currentFloor=0;
		try {
	        //create the datagram sockets for receiving
	         receiveCallSocket = new DatagramSocket(2222);
	         
	        
	      } catch (SocketException se)
	  		{//can't create socket
	         se.printStackTrace();
	         System.exit(1);
	      } 
	 }	 
	 public void createButtons() {
		for (int i = 1; i < numFloors+1; i++) {
			elevatorButtons[i]=new buttonClass();
			elevatorLamps[i]=new lampsClass();
		}
	 }
	 
	 public StateMachineEnum getState() {
		 return this.stateMachineEnum; 
		 
	 }	 
	 
	
	public void receiveCall() {
		
		//create new datagram packet for receiving
				byte[] data= new byte[100];
			    receivePacket = new DatagramPacket(data, data.length);
			    System.out.println("elevator: Waiting for packet from scheduler\n");
			      
			      // Block until a datagram packet is received from receiveSocket.
			      try {        
			         System.out.println("Waiting for scheduler..."); // so we know we're waiting
			         receiveCallSocket.receive(receivePacket);
			      } catch (IOException e) {
			         System.out.print("IO Exception: likely:");
			         System.out.println("Receive Socket Timed Out.\n" + e);
			         e.printStackTrace();
			         System.exit(1);
			      }

			      // Process the received datagram.
			      System.out.println("elevator: packet received :");
			      System.out.println("From host: " + receivePacket.getAddress());
			      System.out.println("Host port: " + receivePacket.getPort());
			      
			      for (int i = 0; i < data.length; i++) {
					deployElevator(data[i]);
			      }
			     // deployElevator(sortPacket(receivePacket.getData()));
			      //int len = receivePacket.getLength();
			      //System.out.println("Length: " + len);
			     // System.out.println("Containing: " +receivePacket.getData()[0]);
			      

	}
	
	
	
	
	public void deployElevator(int destFloor) {
		doors.setDoorState(true);
		doors.setDoorState(false);
		
		if (motor.getCurrentFloor() > destFloor) {
			stateMachineEnum = StateMachineEnum.GOING_DOWN;
		}
		else if (motor.getCurrentFloor() < destFloor) {
			stateMachineEnum = StateMachineEnum.GOING_UP;
		}
		else {
			stateMachineEnum = StateMachineEnum.STATIONARY;
		}
		motor.move(destFloor);
		
		doors.setDoorState(true);
		doors.setDoorState(false);
	}
	
	
	public static void main(String args[])
	   {
		 //start the program
	      elevatorClass c = new elevatorClass(7);
	      c.receiveCall();
	   }

}