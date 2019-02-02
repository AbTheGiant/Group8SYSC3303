package Tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Elevator.elevatorMotor;

class ElevatorMotorTest {

	@Test
	void testMove() {
		
		
		try {
		elevatorMotor test=  new elevatorMotor(1); 
		elevatorMotor testTwo =new elevatorMotor(5);
		test.move( 4);
		testTwo.move(1);
		assertEquals(4,test.getCurrentFloor());
		assertEquals(1,testTwo.getCurrentFloor());
		
		}
		catch (Exception e) {

            assertTrue(false);
		}
		
	}
	
	
	
	

}
