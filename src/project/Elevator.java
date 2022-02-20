package project;

import java.util.*;

import data.*;
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
	private int numfloors = 10;
	private State currentState;
	
	// Subsystem variables
	private ArrivalSensor sensor;
	private ArrayList<ElevatorButton> buttons;
	private ArrayList<ElevatorLamp> lamps;
	private ArrayList<ArrivalSensor> sensors;
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
		
		this.sensor = new ArrivalSensor(0,this);
		this.door = new ElevatorDoor();
		this.motor = new Motor();
	
		this.buttons = new ArrayList<>();
		this.lamps = new ArrayList<>();
		this.sensors = new ArrayList<>();
		
		// populate the lists with 10 elements each
		for(int i=0;i<numfloors;i++) {
			ElevatorLamp newLamp = new ElevatorLamp(i);
			this.lamps.add(newLamp);
			ElevatorButton newButton = new ElevatorButton(i,newLamp);
			this.buttons.add(newButton);
			ArrivalSensor newSensor = new ArrivalSensor(i, this);
			this.sensors.add(newSensor);
		}

	}
	
	/**
	 * Method will run infinitely until program termination
	 * Method will repeatedly check for a message from the scheduler in the "messenger", will respond and perform actions according to the message from scheduler ONLY IF
	 * the current state of the elevator does not disqualify it from completing such behavior. e.g. Cannot move up/down when the doors are closing/opening
	 * When the elevator is moving this method will check for the next floor the elevator will arrive on using the ArrivalSensors which will notify the scheduler of the elevator's location
	 */
	public void run() {
		while(true) {
			
			
			//**********************************************
			// 			GET MESSAGE FROM SCHEDULER
			//**********************************************
			
			String message = messenger.getMessage();
			System.out.println(this.getCurrentState());
			System.out.println(message);
			switch (message) {
				// Can only move up when currently not moving	
				case "UP":
					//if(currentState == State.IDLE || currentState == State.UP) {
						currentState = State.UP;
						motor.move(message);
						//get new currentFloor by contacting ArrivalSensor
							//ArrivalSensor sends message to scheduler with new floor
						move();
					//}
					break;
					
				// Can only move down when currently not moving	
				case "DOWN":
					//if(currentState == State.IDLE || currentState == State.DOWN) {
						currentState = State.DOWN;
						motor.move(message);
						//get new currentFloor by contacting ArrivalSensor
							//ArrivalSensor sends message to scheduler with new floor
						move();
					//}
					break;
					
				// Can only stop moving if it is currently moving
				case "STOP":
					//if(currentState == State.UP || currentState == State.DOWN) {
						currentState = State.IDLE;
						motor.stop(currentFloor);
					//}
					break;
					
				// Can only open doors if not moving (ElevatorDoor class will ignore this if the door is already open)	
				case "OPENDOORS":
					//if(currentState == State.IDLE) {
						currentState = State.DOORSOPENING;
						door.openDoors();
						currentState = State.IDLE;
					//}
					break;
				// Can only close doors if not moving (ElevatorDoor class will ignore this if the door is already closed)		
				case "CLOSEDOORS":
					//if(currentState == State.IDLE) {
						currentState = State.DOORSCLOSING;
						door.closeDoors();
						currentState = State.IDLE;
					//}
					break;
				
				// valid for all states, will however only have action when elevator is moving 
				case "CONTINUE":
					//if(currentState == State.UP || currentState == State.DOWN) {
						move();
					//}
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

	private void move() {
		if(currentState == State.UP) {
			this.currentFloor++;
			sensors.get(currentFloor).notifyScheduler();
		}
		else if(currentState == State.DOWN) {
			this.currentFloor--;
			sensors.get(currentFloor).notifyScheduler();
		}
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