package project;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Scheduler Subsystem Class
 * 
 * @author Thomas
 */
public class Scheduler extends Thread{

	private Box floorBox;
	private Message messenger;
	private ArrayList<ElevatorEvent> elevatorEvents = new ArrayList<ElevatorEvent>();
	private ArrayList<ElevatorEvent> sendEvents = new ArrayList<ElevatorEvent>();
	private ArrayList<List<ElevatorEvent>> sequence = new ArrayList<List<ElevatorEvent>>();
	private String moveUp = "UP";
	private String moveDown = "DOWN";
	private String stop = "STOP";
	private String openDoors = "OPENDOORS";
	private String closeDoors = "CLOSEDOORS";
	private String continueMove = "CONTINUE";


	/**
	 * Constructor for the Scheduler
	 * 
	 * @param floorBox	Communication channel to the floor
	 * @param messenger Communication channel to the elevator
	 */
	public Scheduler(Box floorBox, Message messenger) {
		this.floorBox = floorBox;
		this.messenger = messenger;
	}

	/**
	 * Thread loop for Elevator
	 */
	public void run(){
		while(true) {
			synchronized(floorBox) {
				try {
					floorBox.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				while(!floorBox.isEmpty()) {
					elevatorEvents.add(floorBox.remove(0));
				}
			}
			
			Main.safePrint("Scheduler Got:\t" + elevatorEvents);
			
			
			organizeEvents(elevatorEvents);
			
			System.out.println(sequence.toString());
			
			List<Integer> floorsToVisit = new ArrayList<Integer>();
			int sequenceDir = 0;
			int currentFloor = 0;
			for(List<ElevatorEvent> s: sequence) {
				
				for( ElevatorEvent e: s) {				//create list of floors to visit
					floorsToVisit.add(e.getStartFloor());
					floorsToVisit.add(e.getEndFloor());
					sequenceDir = e.getDir();
					
				}
								
				floorsToVisit = floorsToVisit.stream().distinct().collect(Collectors.toList());		//remove duplicates from floors to visit
				
				if(sequenceDir == 1) { 					//if direction is up, sort list in ascending order.
					Collections.sort(floorsToVisit); 
				}
				else {									//else sort list in descending order
					Collections.sort(floorsToVisit, Collections.reverseOrder()); 
				}
				
				System.out.println("\nFloors to visit " + floorsToVisit);
				
				//find way to account for if we start on floor 0
				//sendInstructionsToElevator(moveUp);
				/*
				if(currentFloor == 0) {
					sendInstructionsToElevator(openDoors);
					sendInstructionsToElevator(closeDoors);
					sendInstructionsToElevator(moveUp);
				}
				*/
				
				boolean arrived = false;
				for(int i = 0; i < floorsToVisit.size(); i++) {
					while(!arrived) {
						if((currentFloor == floorsToVisit.get(i)) && i == 0){
							Main.safePrint("Arrived at floor to visit - start: " + currentFloor);
							sendInstructionsToElevator(openDoors);
							sendInstructionsToElevator(closeDoors);
							if(sequenceDir == 1) {
								sendInstructionsToElevator(moveUp);
							}
							else{
								sendInstructionsToElevator(moveDown);
							}
							arrived = true;
						}
						else if(i == 0) {
							if(sequenceDir == 1) {
								sendInstructionsToElevator(moveUp);
							}
							else{
								sendInstructionsToElevator(moveDown);
							}
						}
						
						if((currentFloor == floorsToVisit.get(i)) && (i == floorsToVisit.size() - 1)) {
							Main.safePrint("Arrived at floor to visit - stop: " + currentFloor);
							sendInstructionsToElevator(openDoors);
							sendInstructionsToElevator(closeDoors);
							sendInstructionsToElevator(stop);
							arrived = true;
						}
						else if((currentFloor == floorsToVisit.get(i)) && (i != 0)) {
							Main.safePrint("Arrived at floor to visit - continue: " + currentFloor);
							sendInstructionsToElevator(openDoors);
							sendInstructionsToElevator(closeDoors);
							sendInstructionsToElevator(continueMove);
							arrived = true;
							
						}
						else if(i>0) {
							Main.safePrint("Not this floor");
							sendInstructionsToElevator(continueMove);
						}
				
						
						currentFloor = Integer.parseInt(messenger.getFloor());
						Main.safePrint("Current floor " + currentFloor);
					}
					arrived = false;
					
				}
				
				System.out.println("Sequence finished\n");
				
				floorsToVisit = new ArrayList<Integer>();
				
			}
			

			/*
			 * pseudo code
			 * get floor from elevator
			 * set direction of elevator based on difference of floors
			 * start motor
			 * receive floor updates from Elevator
			 * 	- if elevator is at correct starting floor - open doors, close doors, move in direction as before
			 * 	- if elevator is at wrong floor - keep moving
			 * 	- if elevator is at final floor in sequence - open door, close doors, start next sequence
			 * 
			 * 
			 * organize floors into an array 
			 * start floor = starting floor in array
			 * destination floor = first destination floor in array 
			 * 
			 * wait() to receive current floor from elevator
			 * 
			 * while(not at start floor){
			 * 	if current floor == start event floor
			 * 		open and close door
			 * 		set the direction to event direction
			 *  	start motor
			 *  	set lamp
			 *  	exit while loop
			 * 	else if current floor is > current event floor
			 * 		set direction to up , start motor
			 * 	else if current floor is < current event floor 
			 * 		set direction to down, start motor
			 * }
			 * 
			 * move elevator in event direction
			 * 
			 * while(theres floors in the array){
			 * 
			 * 	wait and get floor updates from Elevator.java
			 * 	if(elevator reaches the first destination floor)
			 * 		stop motor
			 * 		if(not the last destination floor)
			 * 			turn on lamp if not already on
			 * 		if(destination is a end floor for an event)
			 * 			turn off lamp
			 * 		open door
			 * 		close door
			 * 		destination floor = next destination floor
			 * 	else
			 * 		do nothing
			 * 
			 * 		
			 * }
			 * 
			 * repeat for next sequences
			 * 
			 */
			
			synchronized(floorBox) {
				floorBox.notifyAll();
			}
		}
	}
	
	/** 
	 * For elevator events in elevatorEvents from Floor.java check if events are going in the same direction 
	 * and if the end floor is the same or lower/higher (depending on the direction of the previous event) of 
	 * the previous event. When the current event is not in the same direction as the previous events send the collected
	 * events. Then check whether the next event is in the same direction as the current event,
	 * if not then send the current event. If next event is in the same direction hen check if the next event 
	 * is not in the same way as the current event, if not then send the current event. 
	 * Then send remaining collected events.
	 * 
	 * @param array list of elevator events taken from floorBox
	 */
	public void /*ArrayList<ElevatorEvent>*/ organizeEvents(ArrayList<ElevatorEvent> events) {
		ElevatorEvent previousEvent = elevatorEvents.get(0);
		int index = 0;
		
		for(ElevatorEvent e: elevatorEvents) {										

			if(previousEvent.getDir() == e.getDir()) {
				if(e.getDir()==1 && (previousEvent.getEndFloor() >= e.getEndFloor())) {
					sendEvents.add(e);
				}
				else if(e.getDir()==-1 && (previousEvent.getEndFloor() <= e.getEndFloor())) {
					sendEvents.add(e);
				}	
				
				previousEvent = e;
			}				
			else {					
				//sendInstructionsToElevator(sendEvents);
				sequence.add(sendEvents);
				sendEvents = new ArrayList<ElevatorEvent>();
				sendEvents.add(e);
				
				if(index < elevatorEvents.size()-1) {
					previousEvent = elevatorEvents.get(index+1);
				}
				
				if(e.getDir() != previousEvent.getDir()) {
					//sendInstructionsToElevator(sendEvents);
					sequence.add(sendEvents);
					sendEvents = new ArrayList<ElevatorEvent>();
				}
				else if((e.getDir() == 1) && (e.getEndFloor() > previousEvent.getEndFloor())) {
					//sendInstructionsToElevator(sendEvents);		
					sequence.add(sendEvents);
				}
				else if((e.getDir() == -1) && (e.getEndFloor() < previousEvent.getEndFloor())) {
					//sendInstructionsToElevator(sendEvents);	
					sequence.add(sendEvents);
				}
			}
			index++;
		}
		
		//sendInstructionsToElevator(sendEvents);
		sequence.add(sendEvents);
		
	}
	
	/**
	 * 
	 * @param  
	 */
	public void sendInstructionsToElevator(String message) {
		
		//Main.safePrint("Scheduler Sent:\t" + message);
		messenger.setMessage(message);
		/*
		elevatorBox.add(events);
		elevatorBox.notifyAll();
		
		try {
			elevatorBox.wait();
			System.out.println("Scheduler Notified of Passenger Arrival");
		} catch (InterruptedException err) {
			err.printStackTrace();
		}
		*/
		
	}
	
	
	/**
	 * Places a request by added RequestData to requests.
	 * Notifies available elevators to accept new request
	 * 
	 * @param 
	 */
	public synchronized void callForElevator(ElevatorEvent event) {
		this.elevatorEvents.add(event);
		notifyAll();
	};
	
	/**
	 * Method invoked by Elevator subsystem to accept new request to fulfill
	 * 
	 * @return RequestData - peek of the first RequestData in requests queue
	 * @throws ParseException
	 */
	public synchronized ElevatorEvent processUserCall() {
		while(this.elevatorEvents.isEmpty()) {
			try {
				wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		ElevatorEvent event = elevatorEvents.get(elevatorEvents.size()-1);
		
		notifyAll();
		
		return event;
	}
		
	/**
	 * Validates the completion of a request by an elevator and pops that request off the queue
	 * 
	 * @param completionTime - datetime when elevator completed request so as to compare its finished after request date
	 * @param currentFloor - int of elevator's current position, gets cross referenced with original request's destination floor
	 * @param visitedRequestedFloor - boolean whether or not elevator passed/opened at the floor it was called on for, checks if thats the request source floor
	 * @return RequestData - popped RequestData from queue
	 */
	public synchronized ElevatorEvent completeCall(Date completionTime, int currentFloor, boolean visitedRequestedFloor) {
		if(currentFloor == elevatorEvents.get(elevatorEvents.size()-1).getEndFloor() &&
			visitedRequestedFloor) {

			System.out.println("Elevator completed request at "+ completionTime.toString());
			ElevatorEvent completedRequest = elevatorEvents.remove(elevatorEvents.size()-1);
			notifyAll();

			return completedRequest;
		}
		return null;
	}
}