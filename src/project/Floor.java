package project;

import java.io.File; 
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Floor Subsystem Class
 * 
 * @author Thomas
 */
class Floor extends Thread{
	private Box boxToScheduler;
	private ArrayList<ElevatorEvent> elevatorEvents = new ArrayList<ElevatorEvent>(); 
	
	/**
	 * Constructor for Floor
	 * 
	 * @param boxToScheduler The communication channel to scheduler
	 */
	public Floor(Box boxToScheduler) {
		this.boxToScheduler = boxToScheduler;
	}
	
	/**
	 * Thread loop for floor
	 * Imports data file and sends to scheduler
	 */
	public void run() {		
		try {
	        File file = new File("resources/elevator_events.txt");
	        Scanner myReader = new Scanner(file); 
	        
	        while(myReader.hasNextLine()) {
	        	String data = myReader.nextLine();
        		ElevatorEvent event = new ElevatorEvent(data);
        		
	        	elevatorEvents.add(event);
	        }
	        	myReader.close();
	        
	      } catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
	      }
	    Main.safePrint("Floor Sent:\t" + elevatorEvents.toString());
    	boxToScheduler.put(elevatorEvents);
	}
}