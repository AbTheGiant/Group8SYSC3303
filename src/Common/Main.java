package Common;
import static org.junit.jupiter.api.Assertions.assertEquals;

import Elevator.elevatorClass;
import FloorSubsystem.FloorSubsystem;
import Scheduler.Scheduler;
public class Main {


	public static void main(String[] args) {
		System.out.println("[Main]Please wait while the system is initialized...");
		elevatorClass[] elevators = new elevatorClass[3];
		
		Scheduler scheduler = new Scheduler();
		
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
		new Thread(() -> {
			
			while (true)
			{
				elevators[0].receiveCall();
			}
		}).start();
		
		new Thread(() -> {
			
			while (true)
			{
				elevators[1].receiveCall();
			}
		}).start();
		
		new Thread(() -> {
			
			while (true)
			{
				elevators[2].receiveCall();
			}
		}).start();
		
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
		FloorSubsystem f1 = floors[6];
		f1.interact = true;
		while(true)
		{
			f1.iteration1Interact();
			f1.receive();
			if (f1.interact && f1.floorNumber != f1.nextFloor)
			{
				System.out.println("[Main]Switching subsystem to " + f1.nextFloor + ", the destination of the elevator that just came.");
				f1 = floors[f1.nextFloor];
			}
		}
		

	}

}
