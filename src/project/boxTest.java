package project;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

public class boxTest {

	@Test
	/* Author: Tom Pieroni
	 * Check that box thread methods function.
	 * */
	public void test() {
		Box box = new Box();
		ArrayList<ElevatorEvent> exampleEventArray= new ArrayList<ElevatorEvent>();
		ElevatorEvent eventData = new ElevatorEvent(new Date(),1,1,2);
		exampleEventArray.add(eventData);
		
		// check that box starts off empty
		assertTrue(box.isEmpty());
		
		// check that box accepts input and updates empty attribute
		//box.put(exampleEventArray);
		assertFalse(box.isEmpty());
		
		// check that information stored in box can be accessed properly
		//assertTrue(box.get() == exampleEventArray);
		assertFalse(box.isEmpty());
		
		// check that information is removed and empty attribute is adjusted accordingly
		//assertTrue(box.pop() == exampleEventArray);
		assertTrue(box.isEmpty());
	}

}
