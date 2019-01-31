import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SecondTestClassReceiver {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket, receiveSocket;
	
	public SecondTestClassReceiver() {
		
		try {
	         // Construct a datagram socket and bind it to
	         // port 2222. This socket will be used to
	         // send UDP Datagram packets to the elevator systems
	         sendSocket = new DatagramSocket();

	         // Construct a datagram socket and bind it to port 1111 
	         // This socket will be used to
	         // receive UDP Datagram packets.from the floor and elevator systems
	         receiveSocket = new DatagramSocket(2222);
	         
	         // to test socket timeout (2 seconds)
	         //receiveSocket.setSoTimeout(2000);
	      } catch (SocketException se) {
	         se.printStackTrace();
	         System.exit(1);
	      } 
		
	}
	
	public void sendReceive() {
			
		    byte data[] = new byte[100];
		    receivePacket = new DatagramPacket(data, data.length);
		    System.out.println("Elevator: Waiting for Packet.\n");
		    
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
		      
		      System.out.println("Elevator: Packet received:");
		      System.out.println("From host: " + receivePacket.getAddress());
		      System.out.println("Host port: " + receivePacket.getPort());
		      int len = receivePacket.getLength();
		      System.out.println("Length: " + len);
		      System.out.print("Containing: " );
	
		      // Form a String from the byte array.
		      String received = new String(data,0,len);   
		      System.out.println(received + "\n");
	}
	
	
	public static void main(String args[])
	   {
	      SecondTestClassReceiver c = new SecondTestClassReceiver();
	      c.sendReceive();
	   }
	
}
