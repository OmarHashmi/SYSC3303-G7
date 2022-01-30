package project;

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
public class Scheduler extends Thread{

	/**
	 * @param args
	 */
	public static ParentSubsystem boxToFloor;
	public static ParentSubsystem boxToElevator;
	static HashMap<Integer, List<String>> table = new HashMap<Integer, List<String>>();
	
	public Scheduler(ParentSubsystem boxToFloor, ParentSubsystem boxToElevator) {
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