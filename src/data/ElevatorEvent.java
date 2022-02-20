package data;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A data type which contains all information about an elevator event
 * 
 * @author Omar
 */
public class ElevatorEvent {
	private Date time = new Date();
	private int dir;
	private int startFloor;
	private int endFloor;
	
	// Temporary until Sam's work is merged 
	private String[] data={};
	
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
	 * @param dir			Direction: 1 means going up, 0 means not moving, -1 means going down
	 * @param startFloor	The floor the elevator will starting at
	 * @param endFloor		The floor the elevator needs to go to
	 */
	public ElevatorEvent(Date time, int dir, int startFloor, int endFloor){
		this.setTime(time);
		this.setDir(dir);
		this.setStartFloor(startFloor);
		this.setEndFloor(endFloor);
	}
	
	public String toString(){
		if(data.length!=0) {
			return String.join(" ", data);
		}
		
		String str="";
		str+=new SimpleDateFormat("HH:mm:ss.SSS").format(time)+" ";
		str+=startFloor+" ";
		str+= (dir>0)? "Up ":"Down ";
		str+=endFloor;
		
		return str;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the dir
	 */
	public int getDir() {
		return dir;
	}

	/**
	 * @param dir the dir to set
	 */
	public void setDir(int dir) {
		this.dir = dir;
	}

	/**
	 * @return the startFloor
	 */
	public int getStartFloor() {
		return startFloor;
	}

	/**
	 * @param startFloor the startFloor to set
	 */
	public void setStartFloor(int startFloor) {
		this.startFloor = startFloor;
	}

	/**
	 * @return the endFloor
	 */
	public int getEndFloor() {
		return endFloor;
	}

	/**
	 * @param endFloor the endFloor to set
	 */
	public void setEndFloor(int endFloor) {
		this.endFloor = endFloor;
	}
}
