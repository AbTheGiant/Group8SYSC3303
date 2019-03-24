package Scheduler;

public class ServiceRequest {
	private int pickup;
	private int destination;
	private int direction;
	private int elevatorAssigned;
	
	private long[] times;

	public ServiceRequest(int pickup, int destination,int direction) {
		this.pickup = pickup;
		this.destination = destination;
		this.direction = direction;
		
		times = new long[3];
		//Time before assigned
		times[0] = System.currentTimeMillis();
	}
	
	public ServiceRequest() {
	}
	
	public void setTimes(int index) {
		times[index] = System.currentTimeMillis();
	}
	
	public long getTimes(int index) {
		return times[index];
	}
	
	//GETTER METHODS
	public int getDestination() {
		return destination;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public int getPickup() {
		return pickup;
	}
	
	//SETTER METHODS
	public void setDestination(int destination) {
		this.destination = destination;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void setPickup(int pickup) {
		this.pickup = pickup;
	}

	public int getElevatorAssigned() {
		return elevatorAssigned;
	}

	public void setElevatorAssigned(int elevatorAssigned) {
		this.elevatorAssigned = elevatorAssigned;
	}
	
}
