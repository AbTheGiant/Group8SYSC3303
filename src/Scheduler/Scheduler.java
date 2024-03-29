package Scheduler;
//Scheduler.java
//This class manages the interaction between the Floor Subsytems and Elevator class.

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Scheduler {

    //Variables to send to the floor class
    int elevatorDirection, elevatorCurrentLevel, elevatorStatus;
    
    //Variables to send to the elevator class
    int currentElevatorNumber, elevatorDestination, numFloors;
    
    VirtualElevator[] virtualElevators;
    ArrayList<ServiceRequest> elevatorServiceRequests;
    ArrayList<ServiceRequest> processedRequests;
    ArrayList<ServiceRequest> completedRequests;
    
    DatagramPacket sendPacket, receivePacket;
    DatagramSocket receiveSocket, SendSocket;
    
    InetAddress subsystemsAddress;
    
    ArrayList<Long> elevatorProcessTimes;
    ArrayList<Long> floorProcessTimes;
    
    public Scheduler(int numberOfElevatorCars, int numberOfFloors) {
    	numFloors = numberOfFloors;
    	virtualElevators= new VirtualElevator[numberOfElevatorCars];
		for(int i = 0; i < numberOfElevatorCars; i++) {
			virtualElevators[i] = new VirtualElevator(i);
		}
		
		elevatorServiceRequests = new ArrayList<ServiceRequest>();
		processedRequests = new ArrayList<ServiceRequest>();
		completedRequests = new ArrayList<ServiceRequest>();
		elevatorProcessTimes = new ArrayList<Long>();
		floorProcessTimes = new ArrayList<Long>();
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
            // System.out.println("[Scheduler]Waiting..."); // so we know we're waiting
             receiveSocket.receive(receivePacket);
          } catch (IOException e) {
             System.out.print("IO Exception: likely:");
             System.out.println("Receive Socket Timed Out.\n" + e);
             e.printStackTrace();
             System.exit(1);
          }
          //write to console usefull info about the packet
          int len = receivePacket.getLength();
          subsystemsAddress = receivePacket.getAddress();
         // System.out.println("[Scheduler]: Packet received: Containing: " + makeString(data, len));
          
          
          if(data[0] == 1) { 

              handleFloorMessage(data);
          }
          else if(data[0] == 0) {
        	  int previousState = virtualElevators[data[1]].getState();
              int elevatorNum = handleElevatorMessage(data);
              Set elevatorsToCommand = checkServiceRequest();
              notifyFloors(elevatorNum);
              if (virtualElevators[elevatorNum].floorsToVisit.size() != 0 || previousState != 0)              
              {
            	  commandElevator(elevatorNum);
              }              
              
          }
         
          View.updateSoftFaults(virtualElevators[0].isSoftFault(), virtualElevators[1].isSoftFault(), virtualElevators[2].isSoftFault(), virtualElevators[3].isSoftFault());
          View.updateHardFaults(virtualElevators[0].isHardFault(), virtualElevators[1].isHardFault(), virtualElevators[2].isHardFault(),virtualElevators[3].isHardFault());
          View.updateElevatorFloors(virtualElevators[0].getCurrentFloor(), virtualElevators[1].getCurrentFloor(),virtualElevators[2].getCurrentFloor(),virtualElevators[3].getCurrentFloor());
          SendSocket.close();
          receiveSocket.close();
    }
    //Notifies every floor of the elevators status 
    private void notifyFloors(int elevatorNumber)
    {
    	for (int i  = 0; i < numFloors; i++)
    	{
		  byte [] dataToFloor = new byte [4];
		  dataToFloor[0] = (byte) virtualElevators[elevatorNumber].getElevatorNumber();
		  dataToFloor[1] = (byte) virtualElevators[elevatorNumber].getServiceDirection();
		  dataToFloor[2] = (byte) virtualElevators[elevatorNumber].getCurrentFloor();
		  
		  if (virtualElevators[elevatorNumber].getState() > 2 && virtualElevators[elevatorNumber].getCurrentFloor() == i)
		  {
			  dataToFloor[3] = 1;
		  }
		  else
		  {
			  dataToFloor[3] = 0;
		  }	  		  
		  //Sending the elevator info to the floor 
		  try {
		      sendPacket = new DatagramPacket(dataToFloor, dataToFloor.length,subsystemsAddress,3330 + i);
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
      
    }
    
    private void commandElevator(int elevatorNum)
    {
    	byte[] dataToElevator = new byte[1];
    	
    	if(virtualElevators[elevatorNum].isSoftFault())
    	{
    		dataToElevator[0] = (byte) 3;//send it the command to cycle its doors
    	}
    	else if(virtualElevators[elevatorNum].isHardFault())
    	{
    		dataToElevator[0] = (byte) 7;
    	} 	
    	else if (virtualElevators[elevatorNum].floorsToVisit.size() == 0)
    	{
    		dataToElevator[0] = (byte) 0;
    		virtualElevators[elevatorNum].setT(new Timer(999999999));
    		virtualElevators[elevatorNum].getT().start();
    	}
    	else if (virtualElevators[elevatorNum].getCurrentFloor() < virtualElevators[elevatorNum].floorsToVisit.get(0))
    	{
    		dataToElevator[0] = (byte) 1;
    		virtualElevators[elevatorNum].setT(new Timer(3000));
    		virtualElevators[elevatorNum].getT().start();
    	}
    	else if (virtualElevators[elevatorNum].getCurrentFloor() > virtualElevators[elevatorNum].floorsToVisit.get(0))
    	{
    		dataToElevator[0] = (byte) 2;
    		virtualElevators[elevatorNum].setT(new Timer(3000));
    		virtualElevators[elevatorNum].getT().start();
    	}
    	else if (virtualElevators[elevatorNum].getCurrentFloor() == virtualElevators[elevatorNum].floorsToVisit.get(0))
    	{

    		if (virtualElevators[elevatorNum].getState() < 3)
    		{
    			dataToElevator[0] = (byte) 3;
    			virtualElevators[elevatorNum].setT(new Timer(5000));
        		virtualElevators[elevatorNum].getT().start();
    		}
    		else
    		{
    			dataToElevator[0] = (byte) virtualElevators[elevatorNum].getState();
    		}
    		

    	}
    	

		  //Sending the elevator a command
    	String command = commandToString(dataToElevator[0]);
		  try {
	          System.out.println("[Scheduler]Sent " + String.format("%-15s", command) + " to [elevator" + elevatorNum  + "] current state:" + virtualElevators[elevatorNum].getState() + " floor:" + virtualElevators[elevatorNum].getCurrentFloor()
	        		  ); 
		      sendPacket = new DatagramPacket(dataToElevator, dataToElevator.length,subsystemsAddress,2220 + elevatorNum);
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
		  virtualElevators[elevatorNum].setState(dataToElevator[0]);
    }
    
    
    private String commandToString(byte b) {
		String retVal = "";
    	if (b == 0)
		{
			retVal = "{Go Stationary}";
		}
    	else if (b == 1)
    	{
			retVal = "{Go Up}";

    	}
    	else if (b == 2)
    	{
			retVal = "{Go Down}";

    	}
    	else if (b >= 3)
    	{
    		retVal = "{Cycle Doors}";
    	}
		return retVal;
	}

	//Handles the message from the floor
	private void handleFloorMessage(byte[] data) {
		int requestType = 0;
		if (data[2] == -1)
		{
			requestType = 1;
		}
		else if (data[2] == -13)
		{
			requestType = 2;
		}
		  elevatorServiceRequests.add(new ServiceRequest(data[1],data[3],data[2], requestType));
          Set elevatorsToService = checkServiceRequest();
          if (elevatorsToService.contains(0) && virtualElevators[0].getState() == 0)
          {
        	  commandElevator(0);
          }
          if (elevatorsToService.contains(1) && virtualElevators[1].getState() == 0)
          {
        	  commandElevator(1);
          }
          if (elevatorsToService.contains(2) && virtualElevators[2].getState() == 0)
          {
        	  commandElevator(2);
          }
          floorProcessTimes.add((long)(System.currentTimeMillis() -
				  ((data[4] * 3.6 * Math.pow(10, 6)) + (data[5] * 60000) + (data[6] * 1000) + twoBytesToShort(data[7],data[8]))));
	}
	//Handles the message from the elevator
	private int handleElevatorMessage(byte[] data) {
		int x = data[1];
		  virtualElevators[x].setState(data[2]);
		  virtualElevators[x].setCurrentFloor(data[3]);

			 
		  
		  if (virtualElevators[x].getState() == 5 && virtualElevators[x].getCurrentFloor() == virtualElevators[x].floorsToVisit.get(0))
		  {
			  for(int i = 0;i < processedRequests.size(); i++) {
				if(processedRequests.get(i).getElevatorAssigned() == x
						&& processedRequests.get(i).getDestination() == virtualElevators[x].floorsToVisit.get(0)) {
					processedRequests.get(i).setTimes(3);
					completedRequests.add(processedRequests.remove(i));
				}
				if(processedRequests.get(i).getElevatorAssigned() == x
						&& processedRequests.get(i).getPickup() == virtualElevators[x].floorsToVisit.get(0)) {
					processedRequests.get(i).setTimes(2);
					completedRequests.add(processedRequests.remove(i));
				}
			  }
			  
			  virtualElevators[x].floorsToVisit.remove(0);
			  
			  if (virtualElevators[x].floorsToVisit.size() == 0)
			  {
				  virtualElevators[x].setServiceDirection(0);
				  virtualElevators[x].setState(0);
			  }
		  }
		  if(virtualElevators[x].getT().isExpired() && virtualElevators[x].getState() != 0) {
			  virtualElevators[x].setHardFault(true);
			  commandElevator(x);
		  }
		  elevatorProcessTimes.add((long)(System.currentTimeMillis() -
				  ((data[4] * 3.6 * Math.pow(10, 6)) + (data[5] * 60000) + (data[6] * 1000) + twoBytesToShort(data[7],data[8]))));
		  return x;
	}
	//Checks if any of the pending service requests are servicable
	public Set checkServiceRequest() {
		int[] bestCases = new int[elevatorServiceRequests.size()];
		int[] bestElevators = new int[elevatorServiceRequests.size()];
		
		//init the bestIdentifiers
		for(int i = 0; i < virtualElevators.length; i++) {			
			for(int j = 0; j < elevatorServiceRequests.size(); j++) {
				bestCases[j] = 10;
				bestElevators[j] = 0;
			}
		}
		
		for(int i = 0; i < virtualElevators.length; i++) {
			for(int j = 0; j < elevatorServiceRequests.size(); j++) {
			 //if the elevator is hard faulted, skip elevator
			 if(virtualElevators[i].isHardFault()) {
			 }
			 //if the elevator current floor is either above or below the pickup floor and is headed in the same direction and current direction
		   	 else if((bestCases[j] > 1) &&
					  (virtualElevators[i].getState() == elevatorServiceRequests.get(j).getDirection() && virtualElevators[i].getServiceDirection() == elevatorServiceRequests.get(j).getDirection()) 
					  &&
					  ((virtualElevators[i].getCurrentFloor() < elevatorServiceRequests.get(j).getPickup() && virtualElevators[i].getState() == 1)
					  ||
					  ((virtualElevators[i].getCurrentFloor() > elevatorServiceRequests.get(j).getPickup() && virtualElevators[i].getState() == 2)))) 
					 {
				  bestCases[j] = 1;
				  bestElevators[j] = i;
			  }
			  else if((bestCases[j] > 2) &&
					  (virtualElevators[i].getServiceDirection() == elevatorServiceRequests.get(j).getDirection()) 
					  &&
					  (virtualElevators[i].getState() == 1 && virtualElevators[i].getServiceDirection() == 2)
					  ||
					  ((virtualElevators[i].getState() == 2 && virtualElevators[i].getServiceDirection() == 1))){
				  bestCases[j] = 2;
				  bestElevators[j] = i;
			  }
			  //if the elevator is stationary
			  else if((bestCases[j] > 3) && virtualElevators[i].getState() == 0) {
				 bestCases[j] = 3;
				 bestElevators[j] = i;
			  }
		  }	
		}
		
		Set elevatorsWithPendingCommands = new HashSet();
		for (int i = elevatorServiceRequests.size()-1; i >= 0; i--)
		{			
		  	if (bestCases[i] <= 3 && !elevatorServiceRequests.get(i).isInvokeFault() && !elevatorServiceRequests.get(i).isInvokeSendStats())//if going same direction of serviceRequest
			{
		  		if (!virtualElevators[bestElevators[i]].floorsToVisit.contains(elevatorServiceRequests.get(i).getPickup()))
		  		{
		  			virtualElevators[bestElevators[i]].floorsToVisit.add(elevatorServiceRequests.get(i).getPickup());
		  		}
		  		if (!virtualElevators[bestElevators[i]].floorsToVisit.contains(elevatorServiceRequests.get(i).getDestination()))
		  		{
		  			virtualElevators[bestElevators[i]].floorsToVisit.add(elevatorServiceRequests.get(i).getDestination());

		  		}
				virtualElevators[bestElevators[i]].setServiceDirection(elevatorServiceRequests.get(i).getDirection());

				virtualElevators[bestElevators[i]].sortFloorsToVisit(elevatorServiceRequests.get(i).getDirection() == 2);
				
				elevatorServiceRequests.get(i).setTimes(1);
				processedRequests.add(elevatorServiceRequests.remove(i));
				elevatorsWithPendingCommands.add(bestElevators[i]);			
			}
			else if (bestCases[i] > 3 || elevatorServiceRequests.get(i).isInvokeFault() || elevatorServiceRequests.get(i).isInvokeSendStats())
			{
				//Leave the request to be serviced at a later date.
				
				//Invoke hard fault, for iteration 3
				if (elevatorServiceRequests.get(i).isInvokeFault())
				{
					virtualElevators[bestElevators[i]].setHardFault(true);
					elevatorServiceRequests.remove(i);
					commandElevator(bestElevators[i]);

				}
				else if (elevatorServiceRequests.get(i).isInvokeSendStats())
				{
					elevatorServiceRequests.remove(i);
					checkStats();
				}
			}
		}

		return elevatorsWithPendingCommands;

	}
	private void checkStats() {
		double assignedTime, pickupTime, completionTime, elevatorprocesstime, floorprocesstime;
		double variAssigned,variPickup, variEProcessTime, variFProcessTime;
		
		assignedTime = 0;
		pickupTime = 0;
		completionTime = 0;
		elevatorprocesstime = 0;
		floorprocesstime = 0;
		variAssigned = 0;
		variPickup = 0;
		variEProcessTime = 0;
		variFProcessTime = 0;
		
		for(ServiceRequest sr : completedRequests) {
			assignedTime += sr.getTimes(1) - sr.getTimes(0);
			pickupTime += sr.getTimes(2) - sr.getTimes(0);
			completionTime += sr.getTimes(3) - sr.getTimes(0);			
		}
		
		//average pickup and completion time
		assignedTime /= completedRequests.size();
		pickupTime /= completedRequests.size();
		completionTime /= completedRequests.size();
		
		for(ServiceRequest sr : completedRequests) {
			variAssigned += assignedTime - Math.pow(sr.getTimes(1) - sr.getTimes(0),2);
			variPickup += pickupTime - Math.pow(sr.getTimes(2) - sr.getTimes(0),2);			
		}
		
		//variance of assigned and pickup time
		variAssigned /= (completedRequests.size() - 1);
		variPickup /= (completedRequests.size() - 1);
		
		for(Long ept: elevatorProcessTimes) {
			elevatorprocesstime += ept;
		}
		
		//average elevator process time
		elevatorprocesstime /= elevatorProcessTimes.size();
		
		for(Long ept: elevatorProcessTimes) {
			variEProcessTime += Math.pow(ept - elevatorprocesstime, 2);
		}
		
		//variance of elevator process time
		variEProcessTime /= (elevatorProcessTimes.size()-1);
		
		for(Long fpt: floorProcessTimes) {
			floorprocesstime += fpt;
		}
		
		//average floor process time
		floorprocesstime /= floorProcessTimes.size();
	
		for(Long ept: elevatorProcessTimes) {
			variFProcessTime += Math.pow(ept - floorprocesstime, 2);
		}
		
		//variance of floor process time
		variFProcessTime /= (floorProcessTimes.size()-1);
		
		//print all STATS
		System.out.println("Average Of Assgined Time: " + assignedTime);
		System.out.println("Variance Of Assgined Time: " + variAssigned);
		
		System.out.println("Average Of Pickup Time: " + pickupTime);
		System.out.println("Variance Of Pickup Time: " + variPickup);
		
		System.out.println("Average Of Arrival Process Time: " + elevatorprocesstime);
		System.out.println("Variance Of Arrival Process Time: " + variEProcessTime);
		
		System.out.println("Average Of Floor Interface Time: " + floorprocesstime);
		System.out.println("Variance Of Floor Interface Time: " + variFProcessTime);
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
    //conversion from byte to short
    public static short twoBytesToShort(byte b1, byte b2) {
    	return (short) ((b1 << 8) | (b2 & 0xFF));
    }
    public static void main(String[] args) {
    	System.out.println("Starting Scheduler...");
    	Scheduler scheduler = new Scheduler(3, 7);
    	while (true)
		{
			scheduler.sendReceive(); 
			/*usefull printout for debugging
			 * for (int j = 0; j < scheduler.virtualElevators.length; j++)
			{
				System.out.print("Elevator " + j + "floorsToVisit: ");
				for (int i = 0; i < scheduler.virtualElevators[j].floorsToVisit.size(); i++)
				{
					System.out.print(scheduler.virtualElevators[j].floorsToVisit.get(i) + " ");
				}
				System.out.println("");
			}
			*/
		}	
    }
    
}
    
