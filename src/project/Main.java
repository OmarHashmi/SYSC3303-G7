package project;
import java.util.Scanner;

import data.*;

/**
 * Parent process that initializes and starts the other threads
 * 
 * @author Thomas
 */
public class Main {
    public static void main(String[] args) throws Exception {    	
    	System.out.print("What subsystem is this?\n\t1. Floor\n\t2. Scheduler\n\t3. Elevator\n\t4. All of the above\n\n>");
    	Scanner scanner = new Scanner(System.in);
    	int input = scanner.nextInt();
    	
    	scanner.nextLine();
    	
    	System.out.print("\nHow long should the elevator take to move between floors? (S)\n\n>");
    	scanner = new Scanner(System.in);
    	SysInfo.elevatorSpeed = scanner.nextInt()*1000;
    		
    	scanner.nextLine();
    	
    	System.out.print("\nHow long should the elevator take to load and unload passengers (S)\n\n>");
    	scanner = new Scanner(System.in);
    	SysInfo.doorSpeed = scanner.nextInt()*1000;	
    	scanner.close();
    	
    	if(SysInfo.gui) {
    		initGUI();
    	}
    	
    	if(input==1) {
    		initFloor();
    	}else if(input==2) {
    		initScheduler();
    	}else if(input==3) {
    		initElevators();
    	}else if(input==4) {
    		initFloor();
    		initScheduler();
    		initElevators();
    	}
	}
	
    private static void initGUI() {
    	consoles = new Consoles();
    	
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
    private static void initFloor() {
    	Floor floor = new Floor();
        Thread floorThread = floor;
        floorThread.start();
    }
    private static void initScheduler() {
        Scheduler scheduler = new Scheduler();
    	Thread schedulerThread = scheduler;
        schedulerThread.start();
    }
    private static void initElevators() {
    	Elevator elevators[] = new Elevator[SysInfo.numElevators];
        Thread elevatorThreads[] = new Thread[SysInfo.numElevators]; 
    	
    	for(int i=0;i<SysInfo.numElevators;i++) {
            elevators[i] = new Elevator(i);
            elevatorThreads[i] = elevators[i];
            elevatorThreads[i].start();
    	}
    }
		
	public static Consoles consoles;
	public static void clog(int i, String str) {
		if(!SysInfo.gui) {
			Main.print(str);
			return;
		}
		consoles.log(i, str);
	}
	public static void print(String str) {
		System.out.println(str);
	}
}
