package project;

/**
 * Parent process that initializes and starts the other threads
 * 
 * @author Thomas
 */
public class Main {
    
	public static void main(String[] args) {
		Box floorScheduler = new Box();
		Box elevatorScheduler = new Box();

		Scheduler scheduler = new Scheduler(floorScheduler, elevatorScheduler);
		Floor floor = new Floor(floorScheduler, scheduler);
		Elevator elevator = new Elevator(elevatorScheduler, scheduler);
		
		Thread schedulerThread = scheduler;
        Thread floorThread = floor;	       
        Thread elevatorThread = elevator;
        		
        floorThread.start(); 
        schedulerThread.start(); 
        elevatorThread.start();
        
        // Temporary to terminate program
        try {
			Thread.sleep(20000);
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.exit(0);
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
