
public class elevatorMotor {
	 
	
	private int currentFloor;
	StateMachineEnum stateMachineEnum ;
	
	public elevatorMotor(int current) {
		this.currentFloor= current;
		this.stateMachineEnum = StateMachineEnum.STATIONARY ;
	}
	
	public StateMachineEnum getState() {
		return this.stateMachineEnum;
	}
	
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public  void move( int destFloor) {
		if (currentFloor>destFloor) {
			moveDown(Math.abs(currentFloor-destFloor));
			
			System.out.println("GOING_DOWN");
			stateMachineEnum = StateMachineEnum.GOING_DOWN;
		}else if (currentFloor < destFloor){
			 moveUp(Math.abs(currentFloor-destFloor));
			 System.out.println("GOING_UP");
			 stateMachineEnum = StateMachineEnum.GOING_UP;
			 
		}
		else {
			System.out.println("STATIONARY");
			
		}
	}
	public void moveUp(int distance) {
		for (int i = 0; i < distance; i++) {
			waitTime();
			currentFloor++;
		}
		//return distance;
	}
	public void moveDown(int distance) {
		for (int i = 0; i < distance; i++) {
			waitTime();
			currentFloor--;
		}
		
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
