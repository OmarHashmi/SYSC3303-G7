package tests;
import project.*;
import data.*;

import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class ElevatorTest {

	/** 
	 * 
	 * 
	 * @author Omar Hashmi
	 */
	@Test
	public void testReceive() {
		SysInfo.init();
		SysInfo.gui = false;
		int elevatorNumber = 1;
		String file = "resources/elevator_events.txt";
		Floor floor = new Floor();
		Scheduler scheduler = new Scheduler();		
		Elevator elevator = new Elevator(elevatorNumber);
		
		assertTrue(floor.readFile(file));		
				
		assertTrue(scheduler.receive());
			
		assertTrue(elevator.getCurrentState() == EState.IDLE);
		
		assertTrue(elevator.getFloor( )== 0);
		
		assertTrue(elevator.receive());	
	}

}
