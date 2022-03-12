package project;
import data.*;

/**
 * Parent process that initializes and starts the other threads
 * 
 * @author Thomas
 */
public class Main {
    public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		Floor floor = new Floor();
		Elevator elevators[] = new Elevator[SysInfo.numElevators];
		
		Thread schedulerThread = scheduler;
        Thread floorThread = floor;
        Thread elevatorThreads[] = new Thread[SysInfo.numElevators]; 

        floorThread.start();
        schedulerThread.start();
		
		for(int i=0;i<SysInfo.numElevators;i++) {
	        elevators[i] = new Elevator(i);
	        elevatorThreads[i] = elevators[i];
	        elevatorThreads[i].start();
		}
		
		if(SysInfo.gui) {
			clog(0,"Communication Logs\n--------------------------------------------\n\n");
			clog(1,"Elevator 0 Logs\n--------------------------------------------\n\n");
			Main.clog(1, "At floor 0");
			clog(2,"Elevator 1 Logs\n--------------------------------------------\n\n");
			Main.clog(2, "At floor 0");
			clog(3,"Elevator 2 Logs\n--------------------------------------------\n\n");
			Main.clog(3, "At floor 0");
			clog(4,"Elevator 3 Logs\n--------------------------------------------\n\n");
			Main.clog(4, "At floor 0");
		}
	}
	
	public static void print(String str) {
		System.out.println(str);
	}
	public static Consoles  c = new Consoles();
	public static void clog(int i, String str) {
		if(!SysInfo.gui) {
			Main.print(str);
			return;
		}
		c.log(i, str);
	}
}
