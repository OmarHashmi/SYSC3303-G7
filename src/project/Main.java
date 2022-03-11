package project;
import data.*;

/**
 * Parent process that initializes and starts the other threads
 * 
 * @author Thomas
 */
public class Main {
    
	public static void main(String[] args) {



		Scheduler scheduler = new Scheduler();
		Floor floor = new Floor();
		ElevatorCommunication elevatorCommunication = new ElevatorCommunication();




		Thread schedulerThread = scheduler;
		Thread elevatorCommunicationThread = elevatorCommunication;
        Thread floorThread = floor;


        floorThread.start();
        schedulerThread.start();
		elevatorCommunication.start();

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
