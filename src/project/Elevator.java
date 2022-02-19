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
		DOORSCLOSING,
		DOORSOPENING
	}

	//box to communicate with scheduler
	private Message messenger;
	private Scheduler scheduler;
	private int currentFloor;
	private State currentState;
	
	// Subsystem variables
	private ArrivalSensor sensor;
	private ArrayList<ElevatorButton> buttons;
	private ArrayList<ElevatorLamp> lamps;
	private ElevatorDoor door;
	private Motor motor;

	/**
	 * Constructor for Elevator
	 * 
	 * @param schedulerBox The communication channel to scheduler
	 */
	public Elevator(Message messenger, Scheduler scheduler) {
		this.messenger = messenger;
		this.scheduler = scheduler;
		
		this.currentState = State.IDLE;
		this.currentFloor = 0;
		
		this.sensor = new ArrivalSensor(this);
		this.door = new ElevatorDoor();
		this.motor = new Motor();
		
		//temporarily initialize as empty lists
		this.buttons = new ArrayList<>();
		this.lamps = new ArrayList<>();

	}
	
	/**
	 * Runs continuously once the thread is started until the program is terminated
	 * Calls the scheduler to see if there is work to be done, moves accordingly, and then sends 
	 * a message back to the scheduler that it has moved accordingly.
	 */
	public void run() {
		while(true) {
			
			
			//**********************************************
			// 			GET MESSAGE FROM SCHEDULER
			//**********************************************
			
			String message = messenger.getMessage();
			
			switch (message) {
				case "UP":
					currentState = State.UP;
					motor.move(message);
					//get new currentFloor by contacting ArrivalSensor
						//ArrivalSensor sends message to scheduler with new floor
					currentFloor = sensor.checkNextFloor();
					break;
					
				case "DOWN":
					currentState = State.DOWN;
					motor.move(message);
					//get new currentFloor by contacting ArrivalSensor
						//ArrivalSensor sends message to scheduler with new floor
					currentFloor = sensor.checkNextFloor();
					break;
					
				case "STOP":
					currentState = State.IDLE;
					motor.stop(currentFloor);
					break;
					
				case "OPENDOORS":
					currentState = State.DOORSOPENING;
					door.openDoors();
					break;
					
				case "CLOSEDOORS":
					currentState = State.DOORSCLOSING;
					door.closeDoors();
					break;
					
				case "CONTINUE":
					if(currentState == State.UP || currentState == State.DOWN) {
						currentFloor = sensor.checkNextFloor();
					}
					break;
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
	
	public int getFloor() {
		return this.currentFloor;
	}
	
	public Message getMessenger() {
		return this.messenger;
	}
	
	public String getCurrentState() {
		return this.currentState.toString();
	}
	
	
}