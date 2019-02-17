package Scheduler;
//Scheduler.java
//This class manages the interaction between the Floor Subsytems and Elevator class.

import java.io.*;
import java.net.*;

public class Scheduler {

    //Variables to send to the floor class
    int elevatorDirection, elevatorCurrentLevel,elevatorStatus;
    
    //Variables to send to the elevator class
    int  currentElevatorNumber, elevatorDestination;
    
    int allElevators[][];
    
    DatagramPacket sendPacket, receivePacket;
    DatagramSocket receiveSocket, SendSocket;
    
    public Scheduler(int numberOfElevatorCars) {
                allElevators = new int [numberOfElevatorCars][6];
                for (int i = 0; i < numberOfElevatorCars; i++)
                {
                	allElevators[i] = new int[6];
                	for (int j = 0; j < 6; j++)
                	{
                		allElevators[i][j]=-1;
                	}
                }
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

         
          //this string is used to output to the console where the scheduler is sending packets
          String dest = "";
          //Our data[4] represents which way the data shuld flow, 1 = to Elevator, 0 = to Floor
          
          if (len == 7) {
              currentElevatorNumber = data[6];
              elevatorCurrentLevel = data [5];
              elevatorStatus = data[2];
              elevatorDirection = data [1];
              elevatorDestination = data [0];
              
         
          
              for (int x = 0; x <allElevators.length; x++ ) {
                  if (x == currentElevatorNumber)
                  {
                      allElevators[x][0] = elevatorDestination;
                      allElevators[x][1] = elevatorDirection;
                      allElevators[x][2] = elevatorStatus;
                      allElevators[x][3] = elevatorCurrentLevel;
                      allElevators[x][4] = currentElevatorNumber;
                  }
              }
              

              
          }
          
                
          if(data[4] == 1) {
              dest = "Elevator"; //sending packet to the elevator
              byte [] dataToElevator = new byte [3];
              dataToElevator[0] = data[2];  //This is the position of the value for the floor number
              dataToElevator[1] = data [1]; //This is the direction of the Elevator 0=down, 1=up
              dataToElevator[2] = data[0]; // this is the requested pickup floor 
              allElevators[currentElevatorNumber][5] = data[2];//Set the pickup floor in this elevators 5th slot

             
              try {
              currentElevatorNumber = data[6];
              } catch (IndexOutOfBoundsException e) {
                  e.printStackTrace();
              }
              
              //Sending the floor number and elevator button to Elevator system
              
              //If the elevator is not moving
              try {
                  sendPacket = new DatagramPacket(dataToElevator, dataToElevator.length, InetAddress.getLocalHost(), 2220+decideWhichElevator(data[1] ,data[0]));
              }
              catch (UnknownHostException e) {
                  e.printStackTrace();
                  System.exit(1);
               }
          }
          else if(data[4] == 0) {
              dest = "Floor Subsystem: Relaying data from elevator"; //sending packet to the floor subsystem
        	  if (allElevators[currentElevatorNumber][5] == 1 )
        	  {
        		  
        	  }
              byte [] dataToFloor = new byte [3];
              dataToFloor[0] = data[1];//floor number
              dataToFloor[1] = data[3];//direction, 2 = down, 1 = up, 0 = stationary;
              dataToFloor[2] = data[4];//destination of the elevator
              

              //Sending the floor number and floor direction
              try {
                  sendPacket = new DatagramPacket(dataToFloor, dataToFloor.length,InetAddress.getLocalHost(),3330 + data[4]);
              }
              catch (UnknownHostException e) {
                  e.printStackTrace();
                  System.exit(1);
               }
          }
       
          System.out.println("[Scheduler]: Sending packet to "+dest+" :" +" Containing: "+ makeString(sendPacket.getData(), sendPacket.getLength()));
          int len2 = sendPacket.getLength();

          // Send the datagram packet to the server via the send/receive socket. 
          try {
                SendSocket.send(sendPacket);
              } catch (IOException e) {
                 e.printStackTrace();
                 System.exit(1);
              }
          //if its got a next in line and the elevators queue is empty
          //4,0,1,1,0,4,0,
          if (data[4] == 0 && allElevators[currentElevatorNumber][5] != -1 && data[2] == 1)
          {
        	  byte [] dataToElevator = new byte [3];
              dataToElevator[0] = (byte )allElevators[currentElevatorNumber][0];  //This is the position of the value for the floor number
              dataToElevator[1] = 0; //This is the direction of the Elevator and doesnt matter
              dataToElevator[2] = (byte)allElevators[currentElevatorNumber][5]; // this is the requested destination floor 
              
              try {
                  sendPacket = new DatagramPacket(dataToElevator, dataToElevator.length, InetAddress.getLocalHost(), 2220+currentElevatorNumber);
              }
              catch (UnknownHostException e) {
                  e.printStackTrace();
                  System.exit(1);
               }
              
              allElevators[currentElevatorNumber][5] = -1;
              System.out.println("[Scheduler]: Sending packet to the elevator :" +" Containing: "+ makeString(sendPacket.getData(), sendPacket.getLength()));

              try {
                  SendSocket.send(sendPacket);
                } catch (IOException e) {
                   e.printStackTrace();
                   System.exit(1);
                }
          }

          SendSocket.close();
          receiveSocket.close();
    }
    
    public int decideWhichElevator(int requestDirection, int requestedFloor) {
        int retVal = -1;
        int i = 0;
        while (retVal==-1)
        {
        	i = i%4;
        	if (allElevators[i][2] == 1 || allElevators[i][2] == -1)
        	{
        		retVal = i;
        	}
        	if (allElevators[i][1] == requestDirection || allElevators[i][1] == -1)
        	{
        		if (requestDirection == 1 && allElevators[i][3] > requestedFloor)
        		{
        			retVal = i;
        		}
        		else if (requestDirection == 2 && allElevators[i][3] < requestedFloor)
        		{
        			retVal = i;
        		}
        		else if (requestDirection == 0 || requestDirection == -1)
        		{
        			retVal = i;
        		}
        		
        	}
        	
        	i++;
        }
        
        
        return retVal;
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
    public static void main(String[] args) {

    }
}
