package project;
import data.*;

/**
 * Parent process that initializes and starts the other threads
 * 
 * @author Thomas
 */
public class Main {
    
	public static void main(String[] args) {
		Box floorScheduler = new Box();
		//Box elevatorScheduler = new Box();
		Message messenger = new Message();

		Scheduler scheduler = new Scheduler(floorScheduler, messenger);
		Floor floor = new Floor(floorScheduler, scheduler);
		Elevator elevator = new Elevator(messenger, scheduler);
		
		Thread schedulerThread = scheduler;
        Thread floorThread = floor;	       
        Thread elevatorThread = elevator;
        		
        floorThread.start(); 
        schedulerThread.start(); 
        elevatorThread.start();
	}
	
	/**
	 * A thread safe function for printing to the console
	 * 
	 * @param str The string to be printed
	 */
	public static void safePrint(String str) {
		synchronized (System.out) {
			System.out.println(str);
		}
	}
}
