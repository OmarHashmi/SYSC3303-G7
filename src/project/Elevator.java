package project;

/**
 * Elevator Subsystem Class
 * 
 * @author Omar
 */
public class Elevator extends Thread {

	private Box boxToScheduler;

	/**
	 * Constructor for Elevator
	 * 
	 * @param boxToScheduler The communication channel to scheduler
	 */
	public Elevator(Box boxToScheduler) {
		this.boxToScheduler = boxToScheduler;
	}
	
	/**
	 * Thread loop for Elevator
	 */
	public void run() {
		
	}
}
