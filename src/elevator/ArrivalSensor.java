package elevator;
import project.*;

//@author Tom P
public class ArrivalSensor {
	private int floorNumber;
	private Elevator elevator;
	
	public ArrivalSensor(Elevator elevator) {
		this.elevator = elevator;
		this.floorNumber = elevator.getFloor();
	}
	
	// that determines elevator's next floor based on movement direction and sends said floor number to elevator schedule
	public int checkNextFloor() {
		String elevatorState = elevator.getCurrentState();
		if(elevatorState.equals("UP")) {
			floorNumber++;
			Main.safePrint("Elevator Arriving at Floor " + floorNumber);
			
			//*******************************************
			//		SEND FLOOR NUMBER TO SCHEDULER
			//*******************************************
			
			elevator.getMessenger().setFloor(Integer.toString(floorNumber));
			
		}
		else if(elevatorState.equals("DOWN")) {
			floorNumber--;
			Main.safePrint("Elevator Arriving at Floor " + floorNumber);
			
			//*******************************************
			//		SEND FLOOR NUMBER TO SCHEDULER
			//*******************************************
			
			elevator.getMessenger().setFloor(Integer.toString(floorNumber));
		}
		
		return floorNumber;
	}
}
