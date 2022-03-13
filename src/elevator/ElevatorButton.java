package elevator;

//@author Tom P
public class ElevatorButton {
	private int floorNum;
	private ElevatorLamp lamp;
	
	public ElevatorButton(int floorNum, ElevatorLamp lamp){
		this.floorNum = floorNum;
		this.lamp = lamp;
	}
	
	// User presses button, light up corresponding lamp
	public void press() {
		this.lamp.turnOn();
	}

	/**
	 * @return the floorNum
	 */
	public int getFloorNum() {
		return floorNum;
	}

	/**
	 * @param floorNum the floorNum to set
	 */
	public void setFloorNum(int floorNum) {
		this.floorNum = floorNum;
	}
}
