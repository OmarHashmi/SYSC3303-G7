package tests;
import project.*;
import data.*;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import data.Box;
import data.Message;


public class ElevatorTest {

	/** 
	 * 
	 * 
	 * @author Omar Hashmi
	 */
	@Test
	public void testMove() {
		Box floorScheduler = new Box();
		Message messenger = new Message();
		Scheduler scheduler = new Scheduler(floorScheduler, messenger);

		Elevator elevator = new Elevator(messenger, scheduler);
		
		
		assertTrue(elevator.getCurrentState().equals("IDLE"));
		assertTrue(elevator.getFloor()==0);
		
		elevator.move(0,1,3);
	}

}
