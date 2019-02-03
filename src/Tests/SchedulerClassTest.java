package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import Scheduler.Scheduler;

class SchedulerClassTest {

	private Scheduler s;
	private DatagramPacket sendPacket,receivePacket;
	private DatagramSocket sendSocket,receiveSocket;
	
	@Test
	void testSendReceive() {
		s = new Scheduler();
		
		new Thread(() -> {
		    s.sendReceive();
		}).start();
		
		new Thread(() -> {
			try {
				 receiveSocket =  new DatagramSocket(2222);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			byte[] data1= new byte[100];
			receivePacket = new DatagramPacket(data1, data1.length);
		      
		      // Block until a datagram packet is received from receiveSocket.
		      try {        
		         System.out.println("Waiting for scheduler..."); // so we know we're waiting
		         receiveSocket.receive(receivePacket);
		      } catch (IOException e) {
		         System.out.print("IO Exception: likely:");
		         System.out.println("Receive Socket Timed Out.\n" + e);
		         e.printStackTrace();
		         System.exit(1);
		      }
		      assertEquals(data1[0],4);
		      assertEquals(data1[1],1);
		      assertEquals(data1[2],5);
		      System.out.println("ALL TESTS PASSED!");
		}).start();
		
		try {
			 sendSocket =  new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = new byte[5];
	    
	    data[0] = (byte) 4;//floornumber of subsystem/destination requested
	    data[1] = (byte) 1;//request direction ----- 0 = Down, 1 = Up
	    data[2] = (byte) 5;//The final destination after getting picked up
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
		        sendSocket.send(sendPacket);
		      } catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
		      }
		
	}

}
