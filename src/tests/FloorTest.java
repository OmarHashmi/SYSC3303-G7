package tests;

import project.*;
import data.*;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class FloorTest {

	@Test
	/** 
	 * Check that floor thread is capable of retrieving information from input file, and putting such information into the Box class.
	 * 
	 * @author Tom Pieroni
	 * @author Omar Hashmi
	 */
	public void testRead(){
		SysInfo.gui = false;
		Floor floor = new Floor();
		String file = "resources/elevator_events.txt";
		
		assertTrue(floor.readFile(file));
		assertTrue(floor.sendInstruction(1,2,3,0));			
	}

}
