package tests;
import project.*;
import data.*;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import data.Box;
import data.ElevatorEvent;



public class BoxTest {
	/**
	 * Data container which is shared between subsystems so they can communicate
	 * 
	 * @author Tom Pieroni
	 * @author Omar Hashmi
	 */
	public Box filledBox() {
		Box box = new Box();
		ArrayList<ElevatorEvent> events= new ArrayList<ElevatorEvent>();
		
		try {
			events.add(new ElevatorEvent(new SimpleDateFormat("HH:mm:ss.SSS").parse("19:29:03.168"),1,1,2));
			events.add(new ElevatorEvent(new SimpleDateFormat("HH:mm:ss.SSS").parse("19:29:04.168"),1,2,8));
			events.add(new ElevatorEvent(new SimpleDateFormat("HH:mm:ss.SSS").parse("19:29:05.168"),-1,7,3));
			events.add(new ElevatorEvent(new SimpleDateFormat("HH:mm:ss.SSS").parse("19:29:06.168"),-1,6,3));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		box.add(events);
		
		return box;
	}
	@Test
	public void testEmpty() {
		Box box = new Box();
		
		assertTrue(box.isEmpty());
		
		box = filledBox();
		
		assertFalse(box.isEmpty());
	}
	@Test
	public void testAdd() {
		Box box = new Box();
		ElevatorEvent event = new ElevatorEvent(new Date(),1,1,2);
		
		box.add(event);
		
		assertTrue(event == box.get());
	}
	@Test
	public void testRemove() {
		Box box = filledBox();
		
		assertFalse(box.isEmpty());
		box.remove();
		box.remove();
		box.remove();
		box.remove();
		assertTrue(box.isEmpty());
	}
	@Test
	public void testString() {
		Box box = filledBox();
		String str = "[19:29:03.168 1 Up 2, 19:29:04.168 2 Up 8, 19:29:05.168 7 Down 3, 19:29:06.168 6 Down 3]";
		
		assertTrue(box.toString().equals(str));
	}

}
