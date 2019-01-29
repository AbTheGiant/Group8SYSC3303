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
		byte sendData[] = new byte[4];
		   
	     
	     
	    
	     
	     
	    try {
			if (receivePacket.getData()[0]==0 && receivePacket.getData()[1]==1) {
				sendData[0]=0;
				sendData[1]=3;
				sendData[2]=0;
				sendData[3]=1;
				sendPacket= new DatagramPacket(sendData,sendData.length,
			    		  receivePacket.getAddress(), receivePacket.getPort());

				
			}else if (receivePacket.getData()[0]==0 && receivePacket.getData()[1]==2) {
				sendData[0]=0;
				sendData[1]=4;
				sendData[2]=0;
				sendData[3]=0;
				sendPacket= new DatagramPacket(sendData,sendData.length,
			    		  receivePacket.getAddress(), receivePacket.getPort());
			}else {
				System.err.println("Error");
				throw new Exception("Invalid packet");
			}
		} catch (Exception e) {
			
			 e.printStackTrace();
	         System.exit(1);
		}
	     
	     System.out.println(""
	     		+ " "
	     		+ " ");
	     
	    //print the content of the packet
	     System.out.println( "Server: Sending packet to intermediate:");
	      System.out.println("To host: " + sendPacket.getAddress());
	      System.out.println("Destination host port: " + sendPacket.getPort());
	     int  len = sendPacket.getLength();
	      System.out.println("Length: " + len);
	      System.out.print("Containing: ");
	     
	      
	      for (int i = 0; i < sendPacket.getLength(); i++) {
			System.out.print(sendPacket.getData()[i]);
	      }
	      System.out.println("");
		     //sending the new packet back to the intermediate
	      try {
	         sendReceiveSocket.send(sendPacket);
	      } catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }

	      System.out.println("Server: packet sent");	
		
	}
	
	
	public void receive() {
		
	}

	
	
	

	
	public static void main( String args[] )
	{
	   FloorSubsystem c = new FloorSubsystem();
	  // File simulate = new File (getfile ();
	  
	  while(c!=null) {
	   c.send();
	   c.receive();
	  }
	}
	
}

