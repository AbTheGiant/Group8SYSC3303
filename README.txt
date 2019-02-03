*******************************************************
README.txt FILE FOR SYSC3303 Project iteration 0 and 1.
*******************************************************

Lab Section: L5

Group: Group 8

Members:
Usman Babakura 	101045195
Kyle Smith 		101045797
Meet Digrajkar 	101041284
Abiola Olajide 	101005746
Emmanuel 

TABLE OF CONTENT: PACKAGES, SETUP, HOW TO RUN, RESPONSIBILITIES

PACKAGES: There are five packages included in the src folder. Each package contains .java class files for the different systems.

	Common - 			This package contains the buttonClass.java, lampsClass.java and StateMachineEnum.java class files
							buttonClass.java : This class is in charge of keeping track of the state of each button. i.e. pressed or not pressed
							lampsClass.java : This class is in charge of keeping track of the state of the lamps in the systems. i.e. on or off
							StateMachineEnum.java : This class is in charge of keep track of the current state of the elevator system. i.e Going up, down, or not moving
							
	Elevator - 			This package contains the elevatorClass.java, elevatorDoors.java, and elevatorMotor.java class files
							elevatorClass.java : This is the class in-charge of communicating with the Scheduler to get information on what actions to take next.
							elevatorDoors.java : The duty of this class is to handle the opening and closing of the elevator car doors
							elevatorMotor.java : The duty of this class is to handle the movement of the elevator cars.
							
	FloorSubsystem - 	This package contains the FloorSubsystem.java class file
							FloorSubsystem.java : This class represents the Floor system. It is responsible for communicating with the Scheduler by sending and receiving packets to the Scheduler
							
	Scheduler - 		This package contains the Scheduler.java class file
							Scheduler.java : This class represents the Scheduler system. It is the middle system between the Floor and the Elevator and is in charge of communicating with both systems through sending and receiving datagram packets
	
	Test - 				This package contains the ButtonTest.java, ElevatorClassTest.java, ElevatorMotor.java class files
							ButtonTest.java : The duty of this class is to test the buttons in the systems.
							ElevatorClassTest.java : The duty of this class is to test the functionality of the elevator class
							ElevatorMotor.java : The duty of this class is to test the motors in the elevator system.
							
SETUP:

Before you begin, please be sure to include JUnit 5 as a library in the project for the Test classes to work appropriately

HOW TO RUN:

- Begin by running the elevatorClass.java file
- Once the elevator is waiting to receive a packet, proceed to run the Scheduler.java file
- Finally run the FloorSubsystem.java file. This file contains an interactive demo for the T.A's

RESPONSIBILITIES:

Scheduler - 			Usman Babakura and Meet Digrajkar
FloorSubsystem - 		Kyle Smith
Elevator - 				Abiola Olajide and Emmanuel
README.txt - 			Usman Babakura
JUnit and Test Cases - 	Kyle Smith, Meet Digrajkar and Abiola Olajide
State Machine Diagram - Kyle Smith
UML Diagram - 

