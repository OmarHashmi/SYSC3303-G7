package elevator;

//@author Tom P
public class ElevatorLamp {
	private boolean lit;
	private int floornumber;
	
	public ElevatorLamp(int floorNum) {
		this.lit = false;
		this.floornumber = floorNum;
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
}
