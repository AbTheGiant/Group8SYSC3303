package Common;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.JFileChooser;

import Elevator.elevatorClass;
import FloorSubsystem.FloorSubsystem;
import Scheduler.Scheduler;
public class Main {


	public static void main(String[] args) {
		System.out.println("[Main]Please wait while the system is initialized...");
		elevatorClass[] elevators = new elevatorClass[3];
		
		Scheduler scheduler = new Scheduler(3, 7);
		
		new Thread(() -> {
			
			while (true)
			{
				scheduler.sendReceive();
			}
		}).start();
		
		FloorSubsystem[] floors = new FloorSubsystem[7];
		for (int i = 0; i < 3; i++)
		{
			elevators[i] = new elevatorClass(7, i);

		}
		Thread e1 = new Thread (elevators[0]);
		e1.start();
		Thread e2 = new Thread (elevators[1]);
		e2.start();
		Thread e3 = new Thread (elevators[2]);
		e3.start();
		
		for (int i = 0; i < 7; i++)
		{
			floors[i] = new FloorSubsystem(i, 3);
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
			System.out.println("[Main]:Please enter 0 to select a file to read from, or 1 for the interactive demo:");
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
				}
			}
			else if (response == 1)
			{
				interactiveDemo(floors, reader);
			}
			else
			{
				System.out.println("[Main]:That input was incorrect");
			}
		}
		
	}

	private static void readFromFile(FloorSubsystem[] floors) throws FileNotFoundException, IOException {
		 
		JFileChooser chooser = new JFileChooser();
		int retval = JFileChooser.CANCEL_OPTION;
		chooser.setFileSelectionMode( JFileChooser.FILES_ONLY);
		retval = chooser.showDialog(null, "Select");    
		if (retval == JFileChooser.APPROVE_OPTION)
		{
			File f = chooser.getSelectedFile();
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			       floors[Integer.parseInt(line.split(" ")[1])].send((line.split(" ")[2].toUpperCase() == "UP" ? 1:2), Integer.parseInt(line.split(" ")[1]), line.split(" ")[0]);
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
