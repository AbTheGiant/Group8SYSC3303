package Scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Queue;

public class VirtualElevator {
	
	private int currentFloor,serviceDirection,state,elevatorNumber,timeStamp;
	public ArrayList<Integer> floorsToVisit;
	
	public VirtualElevator(int floor) {
		currentFloor = floor;
		floorsToVisit = new ArrayList<Integer>();
	}
	
	public void sortFloorsToVisit(boolean ascending)
	{
			if (ascending)
			{
				floorsToVisit.sort(Comparator.naturalOrder());			
			}
			else
			{
				floorsToVisit.sort(Comparator.reverseOrder());
			}	
	}
	
	//SETTER METHODS
	public void setCurrentFloor(int floor) {
		currentFloor = floor;
	}
	
	public void setServiceDirection(int direction) {
		this.serviceDirection = direction;
	}
	
	public void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	//GETTER METHODS
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public int getServiceDirection() {
		return serviceDirection;
	}
	
	public int getElevatorNumber() {
		return elevatorNumber;
	}
	
	public int getState() {
		return state;
	}
	
	public int getTimeStamp() {
		return timeStamp;
	}
}
