/**
 * 
 */
package elevatorSystem;

import java.io.File; 
import java.io.FileNotFoundException; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.concurrent.TimeUnit;

/**
 * @author Thomas
 *
 */
@SuppressWarnings("unused")
public class schedulerSubsystem extends Thread{

	/**
	 * @param args
	 */
	public static parentSystem boxToFloor;
	public static parentSystem boxToElevator;
	static HashMap<Integer, List<String>> table = new HashMap<Integer, List<String>>();
	
	public schedulerSubsystem(parentSystem boxToFloor, parentSystem boxToElevator) {
		this.boxToFloor = boxToFloor;
		this.boxToElevator = boxToElevator;

	}
	
	public void run(){
		// TODO Auto-generated method stub
		while(true) {
			table = (HashMap<Integer, List<String>>) boxToFloor.get();
			boxToFloor.safePrint("scheduler received: " + table.toString());

		}

	}

}
