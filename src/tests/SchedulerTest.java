package tests;
import project.*;
import data.*;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.Test;

import data.Box;
import data.ElevatorEvent;
import data.Message;


public class SchedulerTest {
	public ArrayList<ElevatorEvent> events() {
		ArrayList<ElevatorEvent> events= new ArrayList<ElevatorEvent>();
		
		try {
			events.add(new ElevatorEvent(new SimpleDateFormat("HH:mm:ss.SSS").parse("19:29:03.168"),1,1,2));
			events.add(new ElevatorEvent(new SimpleDateFormat("HH:mm:ss.SSS").parse("19:29:04.168"),1,2,8));
			events.add(new ElevatorEvent(new SimpleDateFormat("HH:mm:ss.SSS").parse("19:29:05.168"),-1,7,3));
			events.add(new ElevatorEvent(new SimpleDateFormat("HH:mm:ss.SSS").parse("19:29:06.168"),-1,6,3));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return events;
	}
	
	@Test
	public void testOrganize() {
		Box floorScheduler = new Box();
		Message messenger = new Message();
		Scheduler scheduler = new Scheduler(floorScheduler, messenger);
		
		ArrayList<ElevatorEvent> events = events();
		scheduler.elevatorEvents = events;
		
		scheduler.organizeEvents(events);

		String data = scheduler.sequence.toString();
		String str = "[[19:29:03.168 1 Up 2], [19:29:05.168 7 Down 3, 19:29:06.168 6 Down 3]]";
		
		assertTrue(data.equals(str));
	}

}
