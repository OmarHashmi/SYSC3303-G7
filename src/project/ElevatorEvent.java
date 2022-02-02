package project;

import java.util.*;

/**
 * A data type which contains all information about an elevator event
 * 
 * @author Omar
 */
public class ElevatorEvent {
	private Date time = new Date();
	private boolean goingUp;
	private int startFloor;
	private int endFloor;
	
	// Temporary until Sam's work is merged 
	private String[] data;
	
	/**
	 * Temporary Constructor
	 * 
	 * @param str A space delineated string containing the data
	 */
	public ElevatorEvent(String str) {
		this.data = str.split(" ");
	}

	/**
	 * Constructor for ElevatorEvent
	 * 
	 * @param time			The event timestamp
	 * @param goingUp		Direction: true means going up, false means going down
	 * @param startFloor	The floor the elevator will starting at
	 * @param endFloor		The floor the elevator needs to go to
	 */
	public void ElevatorEvent(Date time, boolean goingUp, int startFloor, int endFloor){
		this.time=time;
		this.goingUp=goingUp;
		this.startFloor=startFloor;
		this.endFloor=endFloor;
	}
	
	public String toString(){
		return String.join(" ", this.data);
	}
}
