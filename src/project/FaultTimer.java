package project;

import data.*;
import project.Scheduler;

public class FaultTimer extends Thread{
	private EState preState;
    private Scheduler scheduler;
    private int preFloor;
    private int elevatorNum;
    private ElevatorInfo elevator;

    public FaultTimer(Scheduler scheduler, ElevatorInfo elevator) {
        this.scheduler = scheduler;
        this.elevator = elevator;
        this.elevatorNum = elevator.getNumber();
    }
    
    // every 5 seconds check whether each non-idle elevator is still at the same floor, if so, assume elevator is stuck and set the state to EState to STUCK

    public void run() {
        while(true) {
        	preFloor = this.elevator.getFloor();
        	preState = this.elevator.getState();
            try {
            	Thread.sleep(5000);
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
            
            // if timer detects an elevator has broken down, timer stops.
            if(this.elevator.getState() !=  EState.IDLE  && preState == elevator.getState() && preFloor == this.elevator.getFloor()) {
            	System.out.println("***ERROR: ELEVATOR " + elevatorNum + " BROKEN***");
            	Main.clog(0, "***ERROR: ELEVATOR " + elevatorNum + " BROKEN***");
                elevator.setState(EState.STUCK);
                Main.clog(elevatorNum+1,"STUCK");
                break;
            }
            //So elevator is attached @ creation... checks elevator state which better update if it returns as fault
        }
    }
}