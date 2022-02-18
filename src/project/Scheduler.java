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
					elevatorEvents.add(floorBox.remove());
				}
			}
			
			Main.safePrint("Scheduler Got:\t" + elevatorEvents);
			
			ElevatorEvent event;
			synchronized(elevatorBox) {
				event = elevatorEvents.get(elevatorEvents.size()-1);
				
				Main.safePrint("Scheduler Sent:\t" + event.toString());
				elevatorBox.add(event);
				elevatorBox.notifyAll();
				
				try {
					elevatorBox.wait();
					System.out.println("Sheduler Notified of Passenger Arrival");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			synchronized(floorBox) {
				floorBox.notifyAll();
			}
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