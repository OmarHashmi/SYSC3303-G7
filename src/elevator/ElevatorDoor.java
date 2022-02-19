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
	
	// Function will close the elevator doors and display a message
	// Will only execute if the doors are not already closed
	public void closeDoors() {
		if(currentDoorState == State.OPEN) {
			System.out.println("Closing Elevator Doors.....");
			
			// Simulate elevator doors closing over the span of 3 seconds
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
			
			System.out.println("Doors Closed.");
			
			currentDoorState = State.CLOSED;
		}
	}
	
	// Function will close the elevator doors and display a message
	// Will only execute if the doors are not already closed
	public void openDoors() {
		if(currentDoorState == State.CLOSED) {
			System.out.println("Opening Elevator Doors.....");
			
			// Simulate elevator doors opening over the span of 3 seconds
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
			
			System.out.println("Doors Open.");
			
			currentDoorState = State.OPEN;
		}
	}
}
