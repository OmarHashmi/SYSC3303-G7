package elevator;

//@author Tom P
public class Motor {
	private boolean moving;
	
	// by default the motor is not moving because the elevator is stationary.
	public Motor() {
		this.moving = false;
	}
	
	// set the motor to start moving the elevator display a message including the direction.
	public void move(String direction) {
		if(moving == false) {
			System.out.println("Elevator Moving " + direction);
			this.moving = true;
		}
	}
	
	// set the motor to stop moving, display message including the floor stopped at.
	public void stop(int floorNum){
		if(moving == true) {
			System.out.println("Elevator Stopping at floor " + floorNum);
			this.moving = false;
		}
	}
}
