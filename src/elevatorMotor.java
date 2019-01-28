
public class elevatorMotor {
	//int currentFloor,destinationFloor;
	
	public void move(int currFloor, int destFloor) {
		if (currFloor>destFloor) {
			moveDown(Math.abs(currFloor-destFloor));
		}else {
			moveUp(Math.abs(currFloor-destFloor));
		}
	}
	public int moveUp(int distance) {
		for (int i = 0; i < distance; i++) {
			waitTime();
			distance++;
		}
		return distance;
	}
	public int moveDown(int distance) {
		for (int i = 0; i < distance; i++) {
			waitTime();
			distance--;
		}
		return distance;
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
}
