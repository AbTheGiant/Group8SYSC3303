import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java. util. Scanner;



import java.io.File;




public class FloorSubsystem {
	
	
	Date time;
	lampsClass lampUp ;
	lampsClass lampDown;
	int floorNumber; 
	String floorButton;
	int carButton;
	buttonClass ButtonUp ;
	buttonClass ButtonDown;
	
	
	
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;

	// we can run in normal (send directly to server) or test
	// (send to simulator) mode
	

	public FloorSubsystem() {
		//
		lampUp.setLamps(false);
		lampDown.setLamps(false);
		ButtonUp.setButton(false);
		ButtonDown.setButton(false);
		
		{
			
			
			
			   try {
			      // Construct a datagram socket and bind it to any available
			      // port on the local host machine. This socket will be used to
			      // send and receive UDP Datagram packets.
			      sendReceiveSocket = new DatagramSocket();
			   } catch (SocketException se) {   // Can't create the socket.
			      se.printStackTrace();
			      System.exit(1);
			   }
			}
	}
	
	
	

	public void send()
	{
		
		
	}
	
	
	public void receive() {
		
	}

	
	
	

	
	public static void main( String args[] )
	{
	 
	  
	}
	
}

