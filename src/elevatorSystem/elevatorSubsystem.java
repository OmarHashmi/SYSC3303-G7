package elevatorSystem;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class elevatorSubsystem extends Thread {

	public static parentSystem boxToScheduler;
	static HashMap<Integer, List<String>> table = new HashMap<Integer, List<String>>();

	public elevatorSubsystem(parentSystem boxToScheduler) {
		this.boxToScheduler = boxToScheduler;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			
		}

	}

}
