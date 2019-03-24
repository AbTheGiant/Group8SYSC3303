package Scheduler;

public class ServiceRequest {
	private int pickup;
	private int destination;
	private int direction;
	private int elevatorAssigned;
	
	private long[] times;
	private boolean invokeFault;
	private boolean invokeSendStats;

	public ServiceRequest(int pickup, int destination,int direction, int type) {
		this.pickup = pickup;
		this.destination = destination;
		this.direction = direction;
		if (type == 1)
		{
			invokeFault = true;
		}
		else if (type == 2)
		{
			invokeSendStats = true;
		}
		times = new long[4];
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
	
	public boolean isInvokeFault() {
		return invokeFault;
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
	public void setInvokeFault(boolean invokeFault) {
		this.invokeFault = invokeFault;
	}

	public boolean isInvokeSendStats() {
		return invokeSendStats;
	}

	public void setInvokeSendStats(boolean invokeSendStats) {
		this.invokeSendStats = invokeSendStats;
	}
}
