package elevator;
import data.EState;
import project.*;

//@author Tom P
public class ArrivalSensor {
	private int floorNumber;
	private Elevator elevator;
	
	public ArrivalSensor(int floorNum, Elevator elevator) {
		this.elevator = elevator;
		//this.floorNumber = elevator.getFloor();
		this.floorNumber = floorNum;
	}
	
	// that determines elevator's next floor based on movement direction and sends said floor number to elevator schedule

	public int checkNextFloor() {
		EState elevatorState = elevator.getCurrentState();
		
		if(elevatorState == EState.UP) {
			floorNumber++;
		}
		else if(elevatorState == EState.DOWN) {
			floorNumber--;
		}
		
		return floorNumber;
	}
	
	public void notifyScheduler() {
		if (elevator.getCurrentState() == EState.UP) {
			elevator.sendToScheduler(1, elevator.getFloor() );
		}
		else if(elevator.getCurrentState() == EState.DOWN){
			elevator.sendToScheduler(0, elevator.getFloor() );
		}
	}
}
