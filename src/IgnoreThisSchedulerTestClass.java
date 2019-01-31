// SimpleEchoClient.java
// This class is the client side for a simple echo server based on
// UDP/IP. The client sends a character string to the echo server, then waits 
// for the server to send it back to the client.
// Last edited January 9th, 2016

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

public class IgnoreThisSchedulerTestClass {

   int floorNumber, elevatorButton;
   String floorDirection;
   
   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendReceiveSocket;

   public IgnoreThisSchedulerTestClass()
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
   
   //This method will take the input and attempt to parse 
   private byte [] parseInputToBytes (String s)
   {
	   
	   String[] input = s.split("\\s+");
	   
		   try {
			   floorNumber = Integer.parseInt(input[0]);
			   floorDirection = input[1];
			   elevatorButton = Integer.parseInt(input[2]);
			   
			   byte [] floorNumberByte = new byte [1];
			   floorNumberByte [0] = (byte) floorNumber;
			   
			   byte [] floorDirectionByte = floorDirection.getBytes();
			   
			   byte [] elevatorButtonByte = new byte [1];
			   elevatorButtonByte[0] = (byte) elevatorButton;
			   
			   return combineByteArrays (floorNumberByte, floorDirectionByte, elevatorButtonByte);
			   
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
		return null;

   }
   
   private byte[] combineByteArrays(byte[] floorNumberByte, byte[] floorDirectionByte, byte[] elevatorButtonByte) throws IOException {
	
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	outputStream.write(floorNumberByte);
	outputStream.write(floorDirectionByte);
	outputStream.write(elevatorButtonByte);
	   
	return outputStream.toByteArray();
}

public void sendAndReceive()
   {
      // Prepare a DatagramPacket and send it via sendReceiveSocket
      // to port 1111 on the destination host.
 
      String s = "2 Up 4";
      System.out.println("Client: sending a packet containing:\n" + s);

      // Java stores characters as 16-bit Unicode values, but 
      // DatagramPackets store their messages as byte arrays.
      // Convert the String into bytes according to the platform's 
      // default character encoding, storing the result into a new 
      // byte array.

      byte msg[] = parseInputToBytes(s);;

      // Construct a datagram packet that is to be sent to a specified port 
      // on a specified host.
      // The arguments are:
      //  msg - the message contained in the packet (the byte array)
      //  msg.length - the length of the byte array
      //  InetAddress.getLocalHost() - the Internet address of the 
      //     destination host.
      //     In this example, we want the destination to be the same as
      //     the source (i.e., we want to run the client and server on the
      //     same computer). InetAddress.getLocalHost() returns the Internet
      //     address of the local host.
      //  5000 - the destination port number on the destination host.
      try {
         sendPacket = new DatagramPacket(msg, msg.length,
                                         InetAddress.getLocalHost(), 1111);
      } catch (UnknownHostException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Client: Sending packet:");
      System.out.println("To host: " + sendPacket.getAddress());
      System.out.println("Destination host port: " + sendPacket.getPort());
      int len = sendPacket.getLength();
      System.out.println("Length: " + len);
      System.out.print("Containing: ");
      System.out.println(new String(sendPacket.getData(),0,len)); // or could print "s"

      // Send the datagram packet to the server via the send/receive socket. 

      try {
         sendReceiveSocket.send(sendPacket);
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }

      System.out.println("Client: Packet sent.\n");

      sendReceiveSocket.close();
   }

   public static void main(String args[])
   {
      IgnoreThisSchedulerTestClass c = new IgnoreThisSchedulerTestClass();
      c.sendAndReceive();
   }
}
