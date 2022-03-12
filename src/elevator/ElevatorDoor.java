package elevator;
import java.util.concurrent.TimeUnit;

import data.SysInfo;
import project.Main;

// @author Tom P
public class ElevatorDoor {
	enum State{
		OPEN,
		CLOSED
	}
	
	private State currentDoorState;
	private int number;
	
	public ElevatorDoor(int number) {
		this.currentDoorState = State.CLOSED;
		this.number = number;
	}
	
	// Function will close the elevator doors
	public void close() {
		if(currentDoorState == State.OPEN) {
			clog(number+1, "Doors Closing");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}
			currentDoorState = State.CLOSED;
			clog(number+1, "Doors Closed");
		}
	}
	
	// Function will close the elevator doors
	public void open() {
		if(currentDoorState == State.CLOSED) {
			clog(number+1, "Doors Opening");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}
			currentDoorState = State.OPEN;
			clog(number+1, "Doors Open");
		}
	}
	
	private void clog(int i, String str) {
		if(SysInfo.verbose) {
			Main.clog(i, str);
		}
	}
}
