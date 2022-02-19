package elevator;

// @author Tom P
public class ElevatorDoor {
	enum State{
		OPEN,
		CLOSED
	}
	
	private State currentDoorState;
	
	public ElevatorDoor() {
		this.currentDoorState = State.CLOSED;
	}
	
	public String checkDoorState() {
		return currentDoorState.toString();
	}
	
	public void closeDoors() {
		if(currentDoorState == State.OPEN) {
			System.out.println("Closing Elevator Doors.....");
			currentDoorState = State.CLOSED;
		}
	}
	
	public void openDoors() {
		if(currentDoorState == State.CLOSED) {
			System.out.println("Closing Elevator Doors.....");
			currentDoorState = State.OPEN;
		}
	}
}
