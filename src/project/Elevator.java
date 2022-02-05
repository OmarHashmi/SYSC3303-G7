package project;

import java.util.Date;

/**
 * Elevator Subsystem Class
 * 
 * @author Omar Hashmi
 * @author Sam Al Zoubi
 */
public class Elevator extends Thread {

	private Box boxToScheduler;
	private Scheduler scheduler;
	private int currentFloor;

	/**
	 * Constructor for Elevator
	 * 
	 * @param boxToScheduler The communication channel to scheduler
	 */
	public Elevator(Box boxToScheduler, Scheduler scheduler) {
		this.boxToScheduler = boxToScheduler;
		this.scheduler = scheduler;
		this.currentFloor = 0;
	}
	
	/**
	 * Runs continuously once the thread is started until the program is terminated
	 * Calls the scheduler to see if there is work to be done, moves accordingly, and then sends 
	 * a message back to the scheduler that it has moved accordingly.
	 */
	public void run() {
		while(true) {
			
			if(true) {continue;}
			
			// Get the call
			ElevatorEvent event = scheduler.processUserCall();
			
			// Go to the start floor
			int diff = event.getStartFloor()-currentFloor;
			if (diff > 0) {
				move(currentFloor, -1, event.getStartFloor());
			} else if (diff < 0) {
				move(currentFloor, 1, event.getStartFloor());
			} else {
				move(currentFloor, 0, event.getStartFloor());
			}
			
			// Go to the end floor
			diff = event.getEndFloor()-currentFloor;
			if (diff > 0) {
				move(currentFloor, -1, event.getEndFloor());
			} else if (diff < 0) {
				move(currentFloor, 1, event.getEndFloor());
			} else {
				move(currentFloor, 0, event.getEndFloor());
			}
			
			// Mark call as complete
			Date completionTime = new Date();
			scheduler.completeCall(completionTime, currentFloor, true);
		}
	}
	
	/**
	 * Method moves the elevator either up or down to the desired floor
	 * 
	 * @param currentFloor - int representing the floor the elevator is currently one
	 * @param d - Direction the elevator must move to reach desired floor
	 * @param desiredFloor - int representing the desired floor that has been requested to go to
	 */
	private void move(int currentFloor, int dir, int desiredFloor) {
		if(dir>0) {
			System.out.println("Elevator is currently on floor " + currentFloor);
			for(;currentFloor<desiredFloor; currentFloor++) {

				System.out.println("Elevator is now at floor: " + currentFloor);
			}
		}
		else if(dir<0) {
			for(;currentFloor>desiredFloor; currentFloor--) {
				System.out.println("Elevator is now at floor: "+ currentFloor);
			}
		}
		else if(currentFloor==desiredFloor){
			System.out.println("Arrived at destination");
		}
	}
}