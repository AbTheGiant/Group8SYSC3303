package Elevator;

public class elevatorDoors {
	
	private boolean doorState;

	public elevatorDoors() {
		this.setDoorState(false);
	}
	
	public boolean getDoorState() {
		return doorState;
	}
	public void waitTime() {
		 // Slow things down (wait 2 seconds)
	      try {
	          Thread.sleep(2000);
	      } catch (InterruptedException e ) {
	          e.printStackTrace();
	          System.exit(1);
	      }
	}

	public void setDoorState(boolean doorState) {
		waitTime();
		this.doorState = doorState;
	}


}
