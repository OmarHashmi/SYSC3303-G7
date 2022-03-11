package elevator;
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
		String elevatorState = elevator.getCurrentState();
		if(elevatorState.equals("Up")) {
			floorNumber++;
			Main.safePrint("Elevator Arriving at Floor " + floorNumber);
			
			//*******************************************
			//		SEND FLOOR NUMBER TO SCHEDULER
			//*******************************************
			
			//elevator.getMessenger().setFloor(Integer.toString(floorNumber));
			
		}
		else if(elevatorState.equals("Down")) {
			floorNumber--;
			Main.safePrint("Elevator Arriving at Floor " + floorNumber);
			
			//*******************************************
			//		SEND FLOOR NUMBER TO SCHEDULER
			//*******************************************
			
			//elevator.getMessenger().setFloor(Integer.toString(floorNumber));
		}
		
		return floorNumber;
	}
	
	public void notifyScheduler() {

		if (elevator.getCurrentState().equals("Up")) {
			elevator.getElevatorCommunication().sendRequest(this.elevator.getElevatorNumber(),1, this.floorNumber );
		}
		else if(elevator.getCurrentState().equals("Down")){
			elevator.getElevatorCommunication().sendRequest(this.elevator.getElevatorNumber(),0, this.floorNumber );

		}
	}
}
