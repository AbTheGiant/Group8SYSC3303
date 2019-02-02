import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ElevatorMotorTest {

	@Test
	void testMove() {
		
		
		try {
		elevatorMotor test=  new elevatorMotor(1); 
		elevatorMotor testTwo =new elevatorMotor(5);
		elevatorMotor testThree = new elevatorMotor(4);
		test.move( 4);
		testTwo.move(1);
		testThree.move(4);
		
		assertEquals(4,test.getCurrentFloor());
		assertEquals(1,testTwo.getCurrentFloor());
		
		assertEquals(4,testThree.getCurrentFloor());
		
		}
		catch (Exception e) {

            assertTrue(false);
		}
		
	}
	
	
	
	

}
