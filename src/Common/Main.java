package Common;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import Elevator.elevatorClass;
import FloorSubsystem.FloorSubsystem;
import Scheduler.Scheduler;
public class Main {


	public static void main(String[] args) {
		System.out.println("[Main]Please wait while the system is initialized...");
		elevatorClass[] elevators = new elevatorClass[3];
		
		Scheduler scheduler = new Scheduler(3);
		
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
						if (dest > floor)
						{
							floors[floor].send(2, dest);
						}
						else if (dest < floor)
						{
							floors[floor].send(1, dest);
						}
						else
						{
							floors[floor].send(0, dest);
						}
					}
				}
				
				
			}

		}
	}

}
