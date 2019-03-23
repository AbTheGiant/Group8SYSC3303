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
		fs = new FloorSubsystem(4, 3, InetAddress.getLocalHost());
		
	}

	@Test
	void test() {
		//checking initial values for both lamps are false
		assertEquals(fs.lampsUp.get(0).getLamps(), false);
		assertEquals(fs.lampsDown.get(0).getLamps(), false);
		
		//start listening on floorsubsystem which should update lamps after message is received
		new Thread(() -> {
		    fs.receive();
		  //down lamp now on because of elevator direction Down (0)
		      assertEquals(fs.lampsDown.get(0).getLamps(), true);
		      
		      System.out.println("All Tests Passed!");
		}).start();
		//emulating a message from the scheduler, indicating that the elevator is on its way
		try {
			 sendSocket =  new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] data = new byte[7];
	    
	    data[0] = (byte) 4;//floornumber of floor headed too
	    data[1] = (byte) 2;//elevator direction ----- 0 = stationary, 1 = Up, 2 = down
	    data[2] = (byte) 0;//status. 0 = on the way, 1= arrived
	    
		data[3]=0;
		
	    data[4] = (byte) 0; // message from elevator
	    data[5] = (byte) 6; //Current floor of the elevator
	    data[6] = (byte) 0;
	    
	    
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
