import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Date;







public class FloorSubsystem {
	
	
	Date time;
	lampsClass lampUp, lampDown, upDirectionLamp, downDirectionLamp ;
	int floorNumber, carButton; 
	String floorButton;
	buttonClass ButtonUp,ButtonDown ;	
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendSocket;

	// we can run in normal (send directly to server) or test
	// (send to simulator) mode
	

	public FloorSubsystem(int floorNumber) {
		//
		lampUp.setLamps(false);
		lampDown.setLamps(false);
		ButtonDown.setButton(false);
		ButtonUp.setButton(false);
		upDirectionLamp.setLamps(false);
		downDirectionLamp.setLamps(false);
		
		{
			
			
			
			   try {
			      // Construct a datagram socket and bind it to any available
			      // port on the local host machine. This socket will be used to
			      // send and receive UDP Datagram packets.
			      sendSocket = new DatagramSocket(2222);
			   } catch (SocketException se) {   // Can't create the socket.
			      se.printStackTrace();
			      System.exit(1);
			   }
			}
	}
	
	
//	 //This method will take the input and attempt to parse 
//	   private byte [] parseInputToBytes (String s)
//	   {
//		   
//		   String[] input = s.split("\\s+");
//		   
//			   try {
//				   //time will go here
//				   floorNumber = Integer.parseInt(input[0]);
//				   floorDirection = input[1];
//				   elevatorButton = Integer.parseInt(input[2]);
//				   
//				   byte [] floorNumberByte = new byte [1];
//				   floorNumberByte [0] = (byte) floorNumber;
//				   
//				   byte [] floorDirectionByte = floorDirection.getBytes();
//				   
//				   byte [] elevatorButtonByte = new byte [1];
//				   elevatorButtonByte[0] = (byte) elevatorButton;
//				   
//				   return combineByteArrays (floorNumberByte, floorDirectionByte, elevatorButtonByte);
//				   
//			   } catch (Exception e) {
//				   e.printStackTrace();
//			   }
//			return null;
//
//	   }
//	   
//	   private byte[] combineByteArrays(byte[] floorNumberByte, byte[] floorDirectionByte, byte[] elevatorButtonByte) throws IOException {
//		
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		outputStream.write(floorNumberByte);
//		outputStream.write(floorDirectionByte);
//		outputStream.write(elevatorButtonByte);
//		   
//		return outputStream.toByteArray();
//	}

	public void send()
	{
		
		
	}
	
	
	public void receive() {
		
	}

	
	
	

	
	public static void main( String args[] )
	{
	 
	  
	}
	
}

