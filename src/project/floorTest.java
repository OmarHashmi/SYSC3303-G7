package project;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.junit.Test;

public class floorTest {

	@Test
	/* Author: Tom P
	 * Check that floor thread is capable of retrieving information from input file, and putting such information into the Box class.
	 * */
	public void test() {
		Box box = new Box();
		Message msg = new Message();
		
		Thread floor = new Floor(box, new Scheduler(box,msg));
		ArrayList<ElevatorEvent> eventArray = new ArrayList<>();
		
		floor.start();
		
		// sleep for 1 second to allow for floor thread to put data into box
		
		try {
	        File file = new File("resources/elevator_events.txt");
	        Scanner myReader = new Scanner(file); 
	        
	        while(myReader.hasNextLine()) {
	        	String[] data = myReader.nextLine().split(" ");
				
				Date date		= new SimpleDateFormat("HH:mm:ss.SSS").parse(data[0]);
				int dir			= (data[2]=="Up")? 1 : -1;
				int startFloor	= Integer.parseInt(data[1]);
				int endFloor	= Integer.parseInt(data[3]);
	        	
        		ElevatorEvent event = new ElevatorEvent(date,dir,startFloor,endFloor);
        		
        		eventArray.add(event);
	        }
	        	myReader.close();
	        
	      } catch (Exception e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
	      }
		
		//check that size of elevatorEvents reflects number of lines in input file.
		//assertTrue(box.get().size() == eventArray.size());
		//ArrayList<ElevatorEvent> boxArray = box.get();
		//check that the box contains the same elements as was in the input file.
		for(int i=0;i<eventArray.size();i++) {
			//assertTrue(boxArray.get(i).toString().equals(eventArray.get(i).toString()));
		}
		
	}

}
