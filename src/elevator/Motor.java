package elevator;

//@author Tom P
public class Motor {
	private boolean moving;
	
	public Motor() {
		this.moving = false;
	}
	
	public void move(String direction) {
		if(moving == false) {
			System.out.println("Elevator Moving " + direction);
			this.moving = true;
		}
	}
	
	public void stop(int floorNum){
		if(moving == true) {
			System.out.println("Elevator Stopping at floor " + floorNum);
			this.moving = false;
		}
	}
}
