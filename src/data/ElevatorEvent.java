package data;

/**
 * A data type which contains all information about an elevator event
 * 
 * @author Omar
 */
public class ElevatorEvent {
	private int dir;
	private int startFloor;
	private int endFloor;
	
	/**
	 * Constructor for ElevatorEvent
	 * 
	 * @param dir			Direction: 1 means going up, 0 means not moving, -1 means going down
	 * @param startFloor	The floor the elevator will starting at
	 * @param endFloor		The floor the elevator needs to go to
	 */
	public ElevatorEvent(int dir, int startFloor, int endFloor){
		this.dir        = dir;
		this.startFloor = startFloor;
		this.endFloor   = endFloor;
	}
	
	public String toString(){
		String str="";
		str+=startFloor+" ";
		str+=(dir>0)? "Up ":"Down ";
		str+=endFloor;
		
		return str;
	}

	/**
	 * @return the dir
	 */
	public int getDir() {
		return dir;
	}
	
	public EState getState() {
		return (dir>0)? EState.UP:EState.DOWN;
	}

	/**
	 * @return the startFloor
	 */
	public int getStartFloor() {
		return startFloor;
	}

	/**
	 * @return the endFloor
	 */
	public int getEndFloor() {
		return endFloor;
	}
}