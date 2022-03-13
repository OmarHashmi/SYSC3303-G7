package elevator;

//@author Tom P
public class ElevatorLamp {
	private boolean lit;
	private int floorNum;
	
	public ElevatorLamp(int floorNum) {
		this.lit = false;
		this.floorNum = floorNum;
	}
	
	// if light is already on ignore
	public void turnOn() {
		if(lit != true) {
			lit = true;
		}
	}
	
	// if light is already off ignore
	public void turnOff() {
		if(lit != false) {
			lit = false;
		}
	}

	/**
	 * @return the floornumber
	 */
	public int getFloornumber() {
		return floorNum;
	}

	/**
	 * @param floornumber the floornumber to set
	 */
	public void setFloornumber(int floornumber) {
		this.floorNum = floornumber;
	}
}
