package Scheduler;
//Scheduler.java
//This class manages the interaction between the Floor Subsytems and Elevator class.

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Queue;

public class Scheduler {

    //Variables to send to the floor class
    int elevatorDirection, elevatorCurrentLevel, elevatorStatus;
    
    //Variables to send to the elevator class
    int  currentElevatorNumber, elevatorDestination, numFloors;
    
    VirtualElevator[] virtualElevators;
    ArrayList<ServiceRequest> elevatorServiceRequests;
    
    DatagramPacket sendPacket, receivePacket;
    DatagramSocket receiveSocket, SendSocket;
    
    public Scheduler(int numberOfElevatorCars, int numberOfFloors) {
    	numFloors = numberOfFloors;
		for(int i = 0; i < numberOfElevatorCars; i++) {
			virtualElevators[i] = new VirtualElevator(i);
		}
		elevatorServiceRequests = new ArrayList<ServiceRequest>();
    }
    
    public void initSockets() {
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
    //Main function that determines behaviour
    public void sendReceive() {
        
        initSockets();
        
        byte data[] = new byte[100];
        receivePacket = new DatagramPacket(data, data.length);
        
        // Waiting until a datagram packet is received from receiveSocket.
          try {        
             System.out.println("[Scheduler]Waiting..."); // so we know we're waiting
             receiveSocket.receive(receivePacket);
          } catch (IOException e) {
             System.out.print("IO Exception: likely:");
             System.out.println("Receive Socket Timed Out.\n" + e);
             e.printStackTrace();
             System.exit(1);
          }
          //write to console usefull info about the packet
          int len = receivePacket.getLength();
          System.out.println("[Scheduler]: Packet received: Containing: " + makeString(data, len));
          
          
          if(data[0] == 1) {   
              handleFloorMessage(data);           
          }
          else if(data[0] == 0) {
              int elevatorNum = handleElevatorMessage(data);
              notifyFloors(elevatorNum);
              commandElevator(elevatorNum);
          }
       
          checkServiceRequest();
            

          receiveSocket.close();
    }
    //Notifies every floor of the elevators status 
    private void notifyFloors(int elevatorNumber)
    {
    	initSockets();
    	for (int i  = 0; i < numFloors; i++)
    	{
		  byte [] dataToFloor = new byte [4];
		  dataToFloor[0] = (byte) virtualElevators[elevatorNumber].getElevatorNumber();
		  dataToFloor[1] = (byte) virtualElevators[elevatorNumber].getServiceDirection();
		  dataToFloor[2] = (byte) virtualElevators[elevatorNumber].getCurrentFloor();
		  
		  if (virtualElevators[elevatorNumber].getState() > 2 && virtualElevators[i].getCurrentFloor() == i)
		  {
			  dataToFloor[3] = 1;
		  }
		  else
		  {
			  dataToFloor[3] = 0;
		  }	  		  
		  //Sending the elevator info to the floor 
		  try {
		      sendPacket = new DatagramPacket(dataToFloor, dataToFloor.length,InetAddress.getLocalHost(),3330 + i);
		      SendSocket.send(sendPacket);
		  }
		  catch (UnknownHostException e) {
		      e.printStackTrace();
		      System.exit(1);
		  }
		  catch (IOException e) {
	             e.printStackTrace();
	             System.exit(1);
	       }
		  
    	}
      SendSocket.close();
    }
    
    private void commandElevator(int elevatorNum)
    {
    	byte[] dataToElevator = new byte[1];
    	
    	if (virtualElevators[elevatorNum].floorsToVisit.size() == 0)
    	{
    		dataToElevator[0] = (byte) 0;
    	}
    	else if (virtualElevators[elevatorNum].getCurrentFloor() < virtualElevators[elevatorNum].floorsToVisit.get(0))
    	{
    		dataToElevator[0] = (byte) 1;
    	}
    	else if (virtualElevators[elevatorNum].getCurrentFloor() > virtualElevators[elevatorNum].floorsToVisit.get(0))
    	{
    		dataToElevator[0] = (byte) 2;
    	}
    	else if (virtualElevators[elevatorNum].getCurrentFloor() == virtualElevators[elevatorNum].floorsToVisit.get(0))
    	{
    		dataToElevator[0] = (byte) 3;
    	}

		  //Sending the elevator a command
		  try {
		      sendPacket = new DatagramPacket(dataToElevator, dataToElevator.length,InetAddress.getLocalHost(),2220 + elevatorNum);
		      SendSocket.send(sendPacket);
		  }
		  catch (UnknownHostException e) {
		      e.printStackTrace();
		      System.exit(1);
		  }
		  catch (IOException e) {
	             e.printStackTrace();
	             System.exit(1);
	      }
    }
    
    
    //Handles the message from the floor
	private void handleFloorMessage(byte[] data) {	  
		  elevatorServiceRequests.add(new ServiceRequest(data[1],data[3],data[2]));
	}
	//Handles the message from the elevator
	private int handleElevatorMessage(byte[] data) {
		  int x = data[1];
   
		  virtualElevators[x].setServiceDirection(data[2]);
		  virtualElevators[x].setState(data[2]);
		  virtualElevators[x].setCurrentFloor(data[3]);
      
		  return x;
	}
	//Checks if any of the pending service requests are servicable
	public void checkServiceRequest() {
		int bestCase = 10;
		int bestElevator = 0;
		
		for(int i = 0; i < virtualElevators.length; i++) {
			for(int j = 0; j < elevatorServiceRequests.size(); j++) {
			  //if the elevator current floor is either above or below the pickup floor and is headed in the same direction and current direction
			  if((bestCase > 1) &&
					  (virtualElevators[i].getState() == elevatorServiceRequests.get(j).getDirection()) 
					  &&
					  ((virtualElevators[i].getCurrentFloor() < elevatorServiceRequests.get(j).getPickup() && virtualElevators[i].getState() == 1)
					  ||
					  ((virtualElevators[i].getCurrentFloor() > elevatorServiceRequests.get(j).getPickup() && virtualElevators[i].getState() == 2)))) 
					 {
				  bestCase = 1;
				  bestElevator = i;
			  }
			  else if((bestCase > 2) &&
					  (virtualElevators[i].getServiceDirection() == elevatorServiceRequests.get(j).getDirection()) 
					  &&
					  (virtualElevators[i].getState() == 1 && virtualElevators[i].getServiceDirection() == 2)
					  ||
					  ((virtualElevators[i].getState() == 2 && virtualElevators[i].getServiceDirection() == 1))){
				  bestCase = 2;
				  bestElevator = i;
			  }
			  //if the elevator is stationary
			  else if((bestCase > 3) && virtualElevators[i].getState() == 0) {
				 bestCase = 3;
				 bestElevator = i;
			  }
		  }
		}
		
		if (bestCase == 1)
		{
			
		}
		else if (bestCase == 2)
		{
			
		}
		else if (bestCase == 3)
		{
			
		}
		else if (bestCase > 3)
		{
			
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
    
}
