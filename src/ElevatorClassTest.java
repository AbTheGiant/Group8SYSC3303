import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ElevatorClassTest {

	@Test
	void deployElevator() {
		
		
		try {
		elevatorClass test=  new elevatorClass(7); 
		//elevatorMotor testTwo =new elevatorMotor(5);
		elevatorMotor tester = new elevatorMotor(3);
		
		tester.move(4);
		//test.deployElevator(4);
		assertEquals(StateMachineEnum.GOING_UP,test.getState());
		//assertTrue(StateMachineEnum.GOING_DOWN.compareTo(test.getState())==1);
		
		}
		catch (Exception e) {

            assertTrue(false);
		}

	}
}
