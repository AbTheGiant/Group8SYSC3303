package Common;

import Elevator.elevatorClass;
import FloorSubsystem.FloorSubsystem;
import Scheduler.Scheduler;

public class main {

	static elevatorClass elevator;
	static Scheduler scheduler;
	static FloorSubsystem[] floors;
	static int numberOfFloors = 7;
	
	public static void main(String[] args) {
		elevator = new elevatorClass(numberOfFloors);
		
		scheduler = new Scheduler();
		
		floors = new FloorSubsystem[numberOfFloors];
		
		
		

	}

}
