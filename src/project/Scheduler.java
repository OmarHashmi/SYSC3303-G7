package project;

import java.util.*;

/**
 * Scheduler Subsystem Class
 * 
 * @author Thomas
 */
public class Scheduler extends Thread{

	private Box boxToFloor;
	private Box boxToElevator;
	private ArrayList<ElevatorEvent> elevatorEvents = new ArrayList<ElevatorEvent>();
	
	/**
	 * Constructor for the Scheduler
	 * 
	 * @param boxToFloor	Communication channel to the floor
	 * @param boxToElevator Communication channel to the elevator
	 */
	public Scheduler(Box boxToFloor, Box boxToElevator) {
		this.boxToFloor = boxToFloor;
		this.boxToElevator = boxToElevator;
	}

	/**
	 * Thread loop for Elevator
	 */
	public void run(){
		while(true) {
			elevatorEvents = boxToFloor.pop();
			Main.safePrint("Scheduler Got:\t" + Arrays.deepToString(elevatorEvents.toArray()));
		}
	}
}