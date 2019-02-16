package Common;
import static org.junit.jupiter.api.Assertions.assertEquals;

import Elevator.elevatorClass;
import FloorSubsystem.FloorSubsystem;
import Scheduler.Scheduler;
public class Main {


	public static void main(String[] args) {
		elevatorClass[] elevators = new elevatorClass[3];
		
		Scheduler scheduler = new Scheduler();
		
		FloorSubsystem[] floors = new FloorSubsystem[7];
		int count = 0;
		for (elevatorClass elevator : elevators)
		{
			elevator = new elevatorClass(7, count);
			count++;

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
		
		for (FloorSubsystem floor : floors)
		{
			floor = new FloorSubsystem(7, 3);
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
		

	}

}
