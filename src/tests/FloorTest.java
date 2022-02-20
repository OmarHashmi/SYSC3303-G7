package tests;
import project.*;
import data.*;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import data.Box;
import data.Message;


public class FloorTest {

	@Test
	/** 
	 * Check that floor thread is capable of retrieving information from input file, and putting such information into the Box class.
	 * 
	 * @author Tom Pieroni
	 * @author Omar Hashmi
	 */
	public void testRead(){
		Box floorScheduler = new Box();
		Message messenger = new Message();
		Scheduler scheduler = new Scheduler(floorScheduler, messenger);
		Floor floor = new Floor(floorScheduler, scheduler);
		
		floor.readFile("resources/elevator_events.txt");
		
		String floorData = floor.getElevatorEvents().toString();
		String str = "[13:05:14.000 0 Up 1, 14:05:14.000 2 Up 4, 14:06:00.000 3 Up 4, 15:06:15.001 8 Down 1, 15:07:30.005 2 Up 9, 16:08:23.004 8 Down 4, 16:08:23.004 7 Down 3, 17:09:16.002 6 Up 8, 18:09:16.002 8 Up 9]";
		
		assertTrue(floorData.equals(str));
	}

}
