package project;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Elevator extends Thread {

	public static ParentSubsystem boxToScheduler;
	static HashMap<Integer, List<String>> table = new HashMap<Integer, List<String>>();

	public Elevator(ParentSubsystem boxToScheduler) {
		this.boxToScheduler = boxToScheduler;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			
		}

	}

}
