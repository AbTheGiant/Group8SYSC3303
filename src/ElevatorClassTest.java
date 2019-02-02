import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ElevatorClassTest {

	@Test
	
	void deployElevator() {
		System.out.println("gooooo");
		
		try {
		System.out.println("gooooo");
		elevatorClass test=  new elevatorClass(7); 
		System.out.println("gooooo");
		
		elevatorMotor tester = new elevatorMotor(3);
		test.setMotor(tester);
		System.out.println("gooooo");
		//test.setMotor(tester);
		//test.deployElevator(5);
		//elevatorMotor motor= new elevatorMotor(4); 
		//motor.move(6);
		test.deployElevator(6);
		//tester.move(4);
		System.out.println("gooooo");
		assertEquals(null,test.motor);
		
		
		
		System.out.println("gooooo");
		
		}
		catch (Exception e) {

            assertTrue(false);
		}

	}
}
