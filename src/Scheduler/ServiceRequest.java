package Scheduler;

public class ServiceRequest {
	private int pickup;
	private int destination;
	private int direction;
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
			setInvokeSendStats(true);
		}
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
