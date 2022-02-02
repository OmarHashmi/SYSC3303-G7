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

        Thread floor = new Floor(floorScheduler);	       
        Thread scheduler = new Scheduler(floorScheduler, elevatorScheduler);     
        Thread elevator = new Elevator(elevatorScheduler);
        		
        floor.start(); 
        scheduler.start(); 
        elevator.start();
        
        // Temporary to terminate program
        try {
			Thread.sleep(2000);
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
