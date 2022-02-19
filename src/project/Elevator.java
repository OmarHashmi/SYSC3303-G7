package project;

import java.util.*;
//import java.util.Arrays;
//import java.util.Date;
import elevator.*;

/**
 * Elevator Subsystem Class
 * 
 * @author Omar Hashmi
 * @author Sam Al Zoubi
 */
public class Elevator extends Thread {
	
	enum State{
		IDLE,
		UP,
		DOWN,
		DOORCLOSING,
		DOOROPENING
	}

	private Box schedulerBox;
	private Scheduler scheduler;
	private int currentFloor;
	private State currentState;
	
	// Subsystem variables
	private ArrivalSensor sensor;
	//private ElevatorButton buttons[];
	//private ElevatorLamp lamps[];
	private ElevatorDoor door;
	private Motor motor;

	/**
	 * Constructor for Elevator
	 * 
	 * @param schedulerBox The communication channel to scheduler
	 */
	public Elevator(Box schedulerBox, Scheduler scheduler) {
		this.schedulerBox = schedulerBox;
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
	
			
			while(!schedulerBox.isEmpty()) {
				ElevatorEvent event = schedulerBox.remove();
				
				Main.safePrint("Elevator Got:\t" + event.toString());
				
				if(door.checkDoorState().equals("OPEN")) {
					currentState = State.DOORCLOSING;
					door.closeDoors();
				}
			}
							
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			
			synchronized(schedulerBox) {
				schedulerBox.notifyAll();
			}
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
			for(;currentFloor>=desiredFloor; currentFloor--) {
				//System.out.println("Elevator is now at floor: " + currentFloor);
			}
		}
		else if(dir<0) {
			for(;currentFloor<=desiredFloor; currentFloor++) {
				//System.out.println("Elevator is now at floor: "+ currentFloor);
			}
		}
		else if(currentFloor==desiredFloor){
			//System.out.println("Arrived at destination");
		}
		this.currentFloor=currentFloor;
	}
}