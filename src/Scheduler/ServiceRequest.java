package Scheduler;

public class ServiceRequest {
	private int pickup;
	private int destination;
	private int direction;
	
	public ServiceRequest(int pickup, int destination,int direction) {
		this.pickup = pickup;
		this.destination = destination;
		this.direction = direction;
	}
	
	public ServiceRequest() {
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
	
}
