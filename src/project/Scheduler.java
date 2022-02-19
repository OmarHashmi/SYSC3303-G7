package project;

import java.util.*;

/**
 * Scheduler Subsystem Class
 * 
 * @author Thomas
 */
public class Scheduler extends Thread{

	private Box floorBox;
	private Box elevatorBox;
	private ArrayList<ElevatorEvent> elevatorEvents = new ArrayList<ElevatorEvent>();
	private ArrayList<ElevatorEvent> sendEvents = new ArrayList<ElevatorEvent>();
	
	/**
	 * Constructor for the Scheduler
	 * 
	 * @param floorBox	Communication channel to the floor
	 * @param elevatorBox Communication channel to the elevator
	 */
	public Scheduler(Box floorBox, Box elevatorBox) {
		this.floorBox = floorBox;
		this.elevatorBox = elevatorBox;
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
	public void organizeEvents(ArrayList<ElevatorEvent> events) {
		ElevatorEvent previousEvent = elevatorEvents.get(0);
		int index = 0;
		boolean send  = false;
		
		for(ElevatorEvent e: elevatorEvents) {										

			//System.out.println("previous: "+ previousEvent.toString() + " Current: "+ e.toString());
			if(previousEvent.getDir() == e.getDir()) {
				//System.out.println("parent if");
				if(e.getDir()==1 && (previousEvent.getEndFloor() >= e.getEndFloor())) {
					sendEvents.add(e);
					//System.out.println("first if");
				}
				else if(e.getDir()==-1 && (previousEvent.getEndFloor() <= e.getEndFloor())) {
					//System.out.println("second if");

					sendEvents.add(e);
				}	
				
				previousEvent = e;
			}				
			else {					
				sendEventsToElevator(sendEvents);
				sendEvents = new ArrayList<ElevatorEvent>();
				sendEvents.add(e);
				
				if(index < elevatorEvents.size()-1) {
					previousEvent = elevatorEvents.get(index+1);
				}
				
				if(e.getDir() != previousEvent.getDir()) {
					sendEventsToElevator(sendEvents);
					sendEvents = new ArrayList<ElevatorEvent>();
				}
				else if((e.getDir() == 1) && (e.getEndFloor() > previousEvent.getEndFloor())) {
					sendEventsToElevator(sendEvents);
				}
				else if((e.getDir() == -1) && (e.getEndFloor() < previousEvent.getEndFloor())) {
					sendEventsToElevator(sendEvents);
				}

			}
			index++;
		}
		
		sendEventsToElevator(sendEvents);
		
	}
	/**
	 * Takes an array list of events from organizeEvents() and sends it to the Elevator.java
	 * @param array list of elevator events
	 */
	public void sendEventsToElevator(ArrayList<ElevatorEvent> events) {
		synchronized(elevatorBox) {
			Main.safePrint("Scheduler Sent:\t" + events.toString());
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