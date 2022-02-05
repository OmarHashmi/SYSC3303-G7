package project;

import java.io.File; 
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Floor Subsystem Class
 * 
 * @author Thomas Poetting
 * @author Sam Al Zoubi
 */
class Floor extends Thread{
	private Box boxToScheduler;
	private Scheduler scheduler;
	private ArrayList<ElevatorEvent> elevatorEvents = new ArrayList<ElevatorEvent>();
	
	/**
	 * Constructor for Floor
	 * 
	 * @param boxToScheduler The communication channel to scheduler
	 */
	public Floor(Box boxToScheduler, Scheduler scheduler) {
		this.boxToScheduler = boxToScheduler;
		this.scheduler = scheduler;
	}
	
	/**
	 * Thread loop for floor
	 * Imports data file and sends to scheduler
	 */
	public void run() {		
		this.readFile("resources/elevator_events.txt");
		
		synchronized(boxToScheduler) {
    		Main.safePrint("Floor Sent:\t" + elevatorEvents.toString());
        	boxToScheduler.put(elevatorEvents);
        	boxToScheduler.notifyAll();
        	
        	try {
				boxToScheduler.wait();
				System.out.println("Floor Received Passengers");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    	
//    	while (true) {
//			while (!elevatorEvents.isEmpty()) {
//				scheduler.callForElevator(elevatorEvents.remove(elevatorEvents.size()-1));
//			}
//		}
	}
	
	/**
	 * 
	 * 
	 * @param path
	 */
	public void readFile(String path) {
		try {
			File file = new File(path);
			Scanner myReader = new Scanner(file); 
			    
			while(myReader.hasNextLine()) {
				String[] data = myReader.nextLine().split(" ");
				
				Date date		= new SimpleDateFormat("HH:mm:ss.SSS").parse(data[0]);
				int dir			= (data[2]=="Up")? 1 : -1;
				int startFloor	= Integer.parseInt(data[1]);
				int endFloor	= Integer.parseInt(data[3]);
				
				ElevatorEvent event = new ElevatorEvent(date,dir,startFloor,endFloor);
						
				elevatorEvents.add(event);
			}
			myReader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}