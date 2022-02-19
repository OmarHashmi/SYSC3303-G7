package elevator;

//@author Tom P
public class Motor {
	private boolean moving;
	
	public Motor() {
		this.moving = false;
	}
	
	public void move(String direction) {
		System.out.println("Elevator Moving " + direction);
		this.moving = true;
	}
	
	public void stop(int floorNum){
		System.out.println("Elevator Stopping at floor " + floorNum);
		this.moving = false;
	}
}
