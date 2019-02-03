package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import FloorSubsystem.FloorSubsystem;

class FloorSubSystemClassTest {
	
	FloorSubsystem fs;
	DatagramSocket sendSocket;
	DatagramPacket sendPacket;
	
	@BeforeEach
	void setUp() throws Exception {
		//floor system for floor 4
		fs = new FloorSubsystem(4);
		
	}

	@Test
	void test() {
		//checking initial values for both lamps are false
		assertEquals(fs.lampUp.getLamps(), false);
		assertEquals(fs.lampDown.getLamps(), false);
		
		//start listening on floorsubsystem which should update lamps after message is received
		new Thread(() -> {
		    fs.receive();
		  //down lamp now on because of elevator direction Down (0)
		      assertEquals(fs.lampDown.getLamps(), true);
		      
		      System.out.println("All Tests Passed!");
		}).start();
		//emulating a message from the scheduler, indicating that the elevator is on its way
		try {
			 sendSocket =  new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] data = new byte[4];
	    
	    data[0] = (byte) 4;//floornumber of floor to get notified
	    data[1] = (byte) 0;//request direction ----- 0 = Down, 1 = Up
	    data[2] = (byte) 2;//The final destination after getting picked up
	    data[3] = (byte) 0; //status. 0 = on the way, 1= arrived
	    
	    
	    //send the packet
	    try {
	         sendPacket = new DatagramPacket(data, data.length,
	                                    InetAddress.getLocalHost(), 3334);
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
