package Common;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Elevator.elevatorClass;
import FloorSubsystem.FloorSubsystem;
import Scheduler.Scheduler;
import Scheduler.View;
public class Main {

	//THIS STRING WAS USED TO ALLOW MULTI-PC OPERATION
	private static final String schedulerString = "";
	private static View view;
	
	public static void main(String[] args) {
		System.out.println("[Main]Please wait while the system is initialized...");
		//init display
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					view = new View();
					view.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		elevatorClass[] elevators = new elevatorClass[3];
		//EXCLUDING SCHEDULER FOR RUNNING ON A SEPERATE PC
		Scheduler scheduler = new Scheduler(4, 7);
		
		new Thread(() -> {
			
			while (true)
			{
				scheduler.sendReceive();
			}
		}).start();
		
		FloorSubsystem[] floors = new FloorSubsystem[7];
		for (int i = 0; i < 3; i++)
		{
			try {
				elevators[i] = new elevatorClass(7, i, InetAddress.getLocalHost());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		Thread e1 = new Thread (elevators[0]);
		e1.start();
		Thread e2 = new Thread (elevators[1]);
		e2.start();
		Thread e3 = new Thread (elevators[2]);
		e3.start();
		
		for (int i = 0; i < 7; i++)
		{
			try {
				floors[i] = new FloorSubsystem(i, 3, InetAddress.getLocalHost());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		new Thread(() -> {
			
			while (true)
			{
				floors[0].receive();
			}
		}).start();
		
		new Thread(() -> {
			
			while (true)
			{
				floors[1].receive();
			}
		}).start();
		
		new Thread(() -> {
			
			while (true)
			{
				floors[2].receive();
			}
		}).start();
		
		new Thread(() -> {
			
			while (true)
			{
				floors[3].receive();
			}
		}).start();
		
		new Thread(() -> {
			
			while (true)
			{
				floors[4].receive();
			}
		}).start();

		new Thread(() -> {
			
			while (true)
			{
				floors[5].receive();
			}
		}).start();
		
		new Thread(() -> {
			
			while (true)
			{
				floors[6].receive();
			}
		}).start();
		

		
		//setsup interactive demo
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Scanner reader = new Scanner(System.in);  // Reading from System.in

		while (true)
		{
			System.out.println("[Main]:Please enter 0 to select a file to read from, or 1 for the interactive demo, or 2 to print out the stats:");
			int response = reader.nextInt();
			if (response == 0)
			{
				try {
					
						readFromFile(floors);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (response == 1)
			{
				interactiveDemo(floors, reader);
			}
			else if (response == 2)
			{
				floors[0].send(-13, 0, LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
			}
			else
			{
				System.out.println("[Main]:That input was incorrect");
			}
		}
		
	}

	private static void readFromFile(FloorSubsystem[] floors) throws FileNotFoundException, IOException, InterruptedException {
		JFileChooser jfc = new JFileChooser();
        JFrame f = new JFrame();

        int retval = jfc.showOpenDialog(f);

        if (retval == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = jfc.getSelectedFile();

            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = br.readLine()) != null) {
	                if (line.split(" ").length == 4)
	                {
	                   floors[Integer.parseInt(line.split(" ")[1])].send((line.split(" ")[2].toUpperCase().equals("UP") ? 2:1), Integer.parseInt(line.split(" ")[3]), line.split(" ")[0]);
	                }
	                else if (line.split(" ").length == 5)
	                {
		               floors[Integer.parseInt(line.split(" ")[1])].send(-1, Integer.parseInt(line.split(" ")[3]), line.split(" ")[0]);

	                }	
	                Thread.sleep(2000);

                }
            }
        }
	}

	private static void interactiveDemo(FloorSubsystem[] floors, Scanner reader) {
		while (true)
		{
			System.out.println("[Main]:Please enter the floor number you'd like to make a request from: ");
			int floor = reader.nextInt(); // Scans the next token of the input as an int.
			if (floor > 7 || floor < 0)
			{
				System.out.println("[Main]:INVALID FLOOR, try again.");
			}
			else 
			{
				int dest = -1;
				while (dest<0)
				{
					System.out.println("[Main]:Now enter the floor number you'd like to go to: ");
					dest = reader.nextInt();
					if (dest > 7 || dest < 0)
					{
						System.out.println("Invalid floor");
						dest = -1;
					}
					else
					{
						String t = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
						if (dest > floor)
						{
							floors[floor].send(2, dest, t);
						}
						else if (dest < floor)
						{
							floors[floor].send(1, dest, t);
						}
						else
						{
							floors[floor].send(0, dest, t);
						}
					}
				}
				
				
			}

		}
	}

}
