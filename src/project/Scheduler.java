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
	public ArrayList<ElevatorEvent> elevatorEvents = new ArrayList<ElevatorEvent>();
	private ArrayList<ElevatorEvent> sendEvents = new ArrayList<ElevatorEvent>();
	public ArrayList<List<ElevatorEvent>> sequence = new ArrayList<List<ElevatorEvent>>();
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
			
			communicateWithElevator();
			
			/*
			synchronized(floorBox) {
				floorBox.notifyAll();
			}
			*/
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
	 * Takes a string from the set instruction list and sends it to the Elevator 
	 * @param  Takes in a string of one of the preset instructions
	 */
	public void sendInstruction(String message) {
		
		//Main.safePrint("Scheduler Sent:\t" + message);
		messenger.setMessage(message);		
	}
	/**
	 * communicateWithElevator() uses the sequence list gathered by organizeEvents and uses that list to 
	 * communicate with the elevator using sendInstruction() to determine what the elevators next instruction will be
	 * @param void
	 */
	public void communicateWithElevator() {

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
			//System.out.println("\ndirection " + sequenceDir);				

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			boolean arrived = false;
			boolean arrivedAtLastFloor = false;
			
			//loop through each index in the list of floors to visit
			for(int i = 0; i <= floorsToVisit.size(); i++) {
				//loop while the elevator has not reached the destination floor 
				while(!arrived) {
					/*If the current floor in the opposite direction of the destination floor from
					* the starting floor, move up or down repeatedly until we reach the starting floor
					*/
					if((currentFloor < floorsToVisit.get(i) && sequenceDir == -1)) {
						sendInstruction(moveUp);
					}
					else if((currentFloor > floorsToVisit.get(i) && sequenceDir == 1)) {
						sendInstruction(moveDown);
					}
					//Else if the current floor is the starting floor set the movement direction from there
					else if((currentFloor == floorsToVisit.get(i)) && i == 0){
						Main.safePrint("Arrived at floor to visit - start: " + currentFloor);
						sendInstruction(openDoors);
						sendInstruction(closeDoors);
						if(sequenceDir == 1) {
							sendInstruction(moveUp);
						}
						else{
							sendInstruction(moveDown);
						}
						arrived = true;
					}
					//else set the direction to either up or down depending on the sequence
					else if(i == 0) {
						if(sequenceDir == 1) {
							sendInstruction(moveUp);
						}
						else{
							sendInstruction(moveDown);
						}
					}
					//If the current floor is the last floor to visit don't move the elevator after it stopped
					if((currentFloor == floorsToVisit.get(i)) && (i == floorsToVisit.size() - 1)) {
						Main.safePrint("Arrived at floor to visit - stop: " + currentFloor);
						sendInstruction(stop);
						sendInstruction(openDoors);
						sendInstruction(closeDoors);
						arrived = true;
						arrivedAtLastFloor = true;
					}
					/*else if the elevator is at a destination floor before the last floor
					* stop the elevator for a moment before starting up again
					*/
					else if((currentFloor == floorsToVisit.get(i)) && (i != 0)) {
						Main.safePrint("Arrived at floor to visit - continue: " + currentFloor);
						sendInstruction(stop);
						sendInstruction(openDoors);
						sendInstruction(closeDoors);
						
						if(sequenceDir == 1) {
							sendInstruction(moveUp);
						}
						else{
							sendInstruction(moveDown);
						}
						
						arrived = true;
						
					}
					//else continue to move the elevator in it's previously set direction
					else if(i>0) {
						Main.safePrint("Not this floor");
						sendInstruction(continueMove);
					}
					
					//if the arrived at destination floor is not the last destination floor get the next floor
					if(!arrivedAtLastFloor) {
						currentFloor = Integer.parseInt(messenger.getFloor());
					}
				}
				
				//if the current index is not the last one set the loop to go again
				if(i != floorsToVisit.size() - 1) {
					arrived = false;
				}				
			}
			
			//sleep to account for the last doors to open and close
			try {
				TimeUnit.SECONDS.sleep(7);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println("Sequence finished\n");
		
			floorsToVisit = new ArrayList<Integer>();
			
		}
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