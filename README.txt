*******************************************************
README.txt FILE FOR SYSC3303 Project iteration 0 and 1.
*******************************************************

Lab Section: L5

Group: Group 8

Members:
Usman Babakura 	101045195
Kyle Smith 	101045797
Meet Digrajkar 	101041284
Abiola Olajide 	101005746
Emmanuel Dairo	101010678

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
							VirtualElevators.java : This class is the virtual representation of the physical elevators
							ServiceRequest.java : This class tracks the service requests and their different timings
	Test - 				This package contains the ButtonTest.java, ElevatorClassTest.java, ElevatorMotor.java class files
							ButtonTest.java : The duty of this class is to test the buttons in the systems.
							ElevatorClassTest.java : The duty of this class is to test the functionality of the elevator class
							ElevatorMotor.java : The duty of this class is to test the motors in the elevator system.
							FloorSubSystemClassTest.java : The duty of this class is to test the functionality of the floor subsystem
							SchedulerClassTest.java : The duty of this class is to test the functionality of the Scheduler class

ROLES OF EACH CLASS:
The Elevator
    Roles:
        -Pickup passengers
            -Continuously polling, even when travelling, for a command from the scheduler to pick someone up
            -Ideally, the elevator selects the destination floor. Due to requirements, the floor subsystem will retain this
        -Updating scheduler of position(so the scheduler can make decisions)
            -Every time the elevator moves to a new floor, notify scheduler so it can update internals


The Floor Subsystem
    Roles:
        -Pickup requests
            -As soon as the up or down button is pressed, that info is conveyed to the scheduler
            -Once elevator arrives, request gets cleared, via message from the scheduler
        -Updating directional lamps
            -The lamps indicating the direction of each elevator need to be updated continuously
            -Continuous polling for status updates from the scheduler of elevator directions.

The Scheduler
    Roles: 
        -Receive status updates from elevators 
            -Update floor and direction of elevator within context of scheduler (via variable)
        -Receive pickup requests from floors
            -Must decide which elevator to deploy based on status of elevators/passenger count
            - keeping even number of people in each elevator ( other elevator is faster)
            -prioritize picking up any requests that appear along route in the same direction 
        -Receive destination requests from Elevator Floor Subsytem (For Now)
            -use this to update the elevator destination for use in making deployment decisions
                -i.e. if the destination ends at floor 4 it can switch directions going down once it gets there
	-Convey status messages and pickup requests between elevator and floor
		-the floor can update its directional lamps from the elevators status updates
		-the elevator chosen by the scheduler will receive a destination from the pickup requests

SETUP:

-Download the .zip which contains the entire project and source code
-Extract the zip onto your computer
-In Eclipse goto "File->Open Projects from File System..." then click the "Directory..." button beside import source and navigate to the Group8SYSC3303 folder and select it
-Next you must include the JUnit 5 library in your buildpath (right click src, build path, configure build path, goto the libraries tab, Add library... JUnit, Next, JUnit5, Finish)
-You need to pass in the ip of the computer running the scheduler into Main.java, set the constant schedulerString = to the local address of the computer I.E "192.168.0.11"
-You currently need to have reached this point on two seperate computers, one where the scheduler will run, and one where the main class will be run. 
	Alternatively, you can pass in the non local address of the scheduler, as long as portforwarding is setup to route to the correct PC's
		-Scheduler port = 1111
		-Other system port must cover   2220 - 2222 AND 3330-3336
		-Currently, you can not seperate the floors and elevators to a different PC


HOW TO RUN:
	System:
	- Run the Common/Main class as a java application
	- You can interact in the console by typing 0 to execute a set of requests from a file (a file selection prompt will appear), 1 to use the interactive demo depicted below, and 2 to show the accumulated stats
		(NOTE: you can not check the stats once the interactive demo has started, you can however press 0 to execute from file, and then enter 2 after execution)
	- With file selection the file must follow the format in the TESTInput.txt, it is based on the project description. Note that in order to invoke a fault from the file, you must add a space after the
		destination floor followed by anything, except spaces. If there is trailing spaces on a line, it can accidentally cause the hard fault to be invoked.
	- With the interactive demo you can interact in the console by typing 2 numbers 1 after another in the console, the first being which floor you want to make a request from, and the second your destination
		e.g. requesting an elevator from floor 6 to 0 you would write into the console as follows:
			6 -> press enter
			0 -> press enter
		-You can do this at any time and it will always take the 2 numbers regardless of the current state of the console.
		-There are in console instructions the first time, but they fly by to quick after the first.
	Scheduler:
	- On the PC with the scheduler you will instead execute from the Scheduler class, it has an internal main method that will begin it's service loop.


	Tests:
	-Just run the .java of the test


RATE MONOTONIC ANALYSIS:
____________________________________
|PROCESS     | PERIOD  |Priority   |
|------------|---------|-----------|
|AssignElevat| 1.0     |3	   |
|------------|---------|-----------|
|Pickup	     | 4045.0  |2	   |
|------------|---------|-----------|
|ArrivalSens | 1.5E12  |1	   | Variance = 1.01E8
|------------|---------|-----------|
|FloorInterfa| 1.5E12  |1	   | Variance = 5.44E16
|____________|_________|___________|
RESPONSIBILITIES:

[ITERATION 1]
Scheduler - 		Usman Babakura and Meet Digrajkar
FloorSubsystem - 	Kyle Smith
Elevator - 		Abiola Olajide and Emmanuel
README.txt - 		Usman Babakura and Kyle Smith
JUnit and Test Cases - 	Kyle Smith, Meet Digrajkar and Abiola Olajide
State Machine Diagram - Kyle Smith
UML Diagram - 		Meet Digrajkar

[ITERATION 2]
Scheduler - Usman Babakura and Meet Digrajkar
FloorSubsystem - Kyle Smith
Elevator - Abiola Olajide and Emmanuel and Kyle Smith
README.txt - Abiola Olajide and Emmanuel
JUnit and Test Cases - 	Kyle Smith, Meet Digrajkar and Abiola Olajide
State Machine Diagram - Kyle Smith
UML Diagram - 		Emmanuel and Meet

[ITERATION 3]
Scheduler - Usman Babakura and Meet Digrajkar And Kyle Smith
FloorSubsystem - Kyle Smith
Elevator - Abiola Olajide and Emmanuel and Kyle Smith
README.txt - Abiola Olajide and Emmanuel and Kyle Smith
JUnit and Test Cases - 	Kyle Smith, Meet Digrajkar and Abiola Olajide
State Machine Diagram - Kyle Smith
UML Diagram - 		Emmanuel and Meet
EMP document 		Kyle Smith

[ITERATION 4] (and partly catchup from iteration 3)
Testing and implementing multi-PC communication - Kyle Smith
Main Class - Kyle Smith
Scheduler - Usman Babakura and Meet Digrajkar And Kyle Smith
FloorSubsystem - Kyle Smith
Elevator - Abiola Olajide and Emmanuel and Kyle Smith and Meet Digrajkar
README.txt - Abiola Olajide and Emmanuel and Kyle Smith
JUnit and Test Cases - 	Kyle Smith, Meet Digrajkar and Abiola Olajide
State Machine Diagram - Kyle Smith
UML Diagram - 		Emmanuel and Meet
EMP document 		Kyle Smith