package Scheduler;
//Scheduler.java
//This class manages the interaction between the Floor Subsytems and Elevator class.

import java.io.*;
import java.net.*;

public class Scheduler {

    //Variables to send to the floor class
    int elevatorDirection, elevatorCurrentLevel,elevatorStatus;
    
    //Variables to send to the elevator class
    int destinationFloor, pickupFloor, currentElevatorNumber, elevatorDestination;
    
    int allElevators[][];
    
    DatagramPacket sendPacket, receivePacket;
    DatagramSocket receiveSocket, SendSocket;
    
    public Scheduler(int numberOfElevatorCars) {
                allElevators = new int [numberOfElevatorCars][5];
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
        System.out.println("Scheduler: Waiting for Packet.\n");
        
        // Waiting until a datagram packet is received from receiveSocket.
          try {        
             System.out.println("Waiting..."); // so we know we're waiting
             receiveSocket.receive(receivePacket);
          } catch (IOException e) {
             System.out.print("IO Exception: likely:");
             System.out.println("Receive Socket Timed Out.\n" + e);
             e.printStackTrace();
             System.exit(1);
          }
          //write to console usefull info about the packet
          System.out.println("Scheduler: Packet received:");
          System.out.println("From host: " + receivePacket.getAddress());
          System.out.println("Host port: " + receivePacket.getPort());
          int len = receivePacket.getLength();
          
          System.out.println("Length: " + len);
          System.out.print("Containing: " );

          // Form a String from the byte array.
          System.out.println(makeString(data, len) + "\n");
         
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
              pickupFloor = dataToElevator[0] = data[0];  //This is the position of the value for the floor number
              dataToElevator[1] = data [1]; //This is the direction of the Elevator 0=down, 1=up
              destinationFloor = dataToElevator[2] = data[2]; // this is the requested destination floor 
              
              
             
              try {
              currentElevatorNumber = data[6];
              } catch (IndexOutOfBoundsException e) {
                  e.printStackTrace();
              }
              
              //Sending the floor number and elevator button to Elevator system
              
              //If the elevator is not moving
              try {
                  sendPacket = new DatagramPacket(dataToElevator, dataToElevator.length, InetAddress.getLocalHost(), 2220+decideWhichElevator());
              }
              catch (UnknownHostException e) {
                  e.printStackTrace();
                  System.exit(1);
               }
          }
          else if(data[4] == 0) {
             
              dest = "Floor Subsystem: Relaying data from elevator"; //sending packet to the floor subsystem
              byte [] dataToFloor = new byte [3];
              dataToFloor[0] = data[1];//floor number
              dataToFloor[1] = data[3];//direction, 0 = down, 1 = up;
              dataToFloor[2] = data[4];//destination of the elevator
            
              //Sending the floor number and floor direction
              try {
                  sendPacket = new DatagramPacket(dataToFloor, dataToFloor.length,InetAddress.getLocalHost(),3330 + pickupFloor);
              }
              catch (UnknownHostException e) {
                  e.printStackTrace();
                  System.exit(1);
               }
          }
       
          System.out.println("Scheduler:\n Sending packet to "+dest+" :");
          System.out.println("To host: " + sendPacket.getAddress());
          System.out.println("Destination host port: " + sendPacket.getPort());
          int len2 = sendPacket.getLength();
          System.out.println("Length: " + len2);
          System.out.println("Containing: "+ makeString(sendPacket.getData(), sendPacket.getLength()) + "\n");

          // Send the datagram packet to the server via the send/receive socket. 
          try {
                SendSocket.send(sendPacket);
              } catch (IOException e) {
                 e.printStackTrace();
                 System.exit(1);
              }


          System.out.println("Client: Packet sent.\n");

          SendSocket.close();
          receiveSocket.close();
    }
    
    public int decideWhichElevator() {
        int closestToPickup = 0;
        int floorsInbetween;
        for (int[] car: allElevators) {
            int temp = 0;
            floorsInbetween = car[0] - car[3];
            
            if (car[3] == car[0] || car[1] == 0) 
                temp = car[4];
            if (floorsInbetween < 0 && car[1] ==2)
                temp = car[4];
            else if (floorsInbetween > 0 && car[1] ==1)
                temp = car[4];
            closestToPickup = closestToPickup < temp ? closestToPickup : temp;
        }
        
        
        return closestToPickup;
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
    //The main inits the scheduler and then infinitely listens for messages
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler (3);
        while (true)
        {
            scheduler.sendReceive();
        }
    }
}
