package project;

import data.EState;
import data.ElevatorInfo;
import project.Scheduler;

public class FaultTimer extends Thread{
    private Scheduler scheduler;

    private EState preState;
    private int preFloor;
    private ElevatorInfo elevator;

    public FaultTimer(Scheduler scheduler, ElevatorInfo elevator) {
        this.scheduler = scheduler;
        this.elevator = elevator;
    }

    public void run() {
        while(true) {

            try {
                if(elevator.getNumber() == 1) {

                    preState = this.elevator.getState();
                    preFloor = this.elevator.getFloor();
                }
                else if(elevator.getNumber() == 2) {
                    preState = this.elevator.getState();
                    preFloor = this.elevator.getFloor();
                }
                else if (elevator.getNumber() == 3) {
                    preState = this.elevator.getState();
                    preFloor = this.elevator.getFloor();
                }

                else {
                    preState = this.elevator.getState();
                    preFloor = this.elevator.getFloor();

                }

                Thread.sleep(5000);

                if(elevator.getNumber() == 1) {

                    if(this.elevator.getState() !=  EState.IDLE &&  preFloor == this.elevator.getFloor()) {
                        System.out.println("^^^ERROR: ELEVATOR 1 BROKEN^^^^");
                    }
                }
                else if(elevator.getNumber() == 2) {

                    if(this.elevator.getState() !=  EState.IDLE &&  preFloor == this.elevator.getFloor()) {
                        System.out.println("^^^ERROR: ELEVATOR 2 BROKEN^^^^");
                    }
                }
                else if (elevator.getNumber() == 3){ //elevator 3

                    if(this.elevator.getState() !=  EState.IDLE &&  preFloor == this.elevator.getFloor()) {
                        System.out.println("^^^ERROR: ELEVATOR 3 BROKEN^^^^");
                    }

                }

                else if (elevator.getNumber() == 4){
                    if(this.elevator.getState() !=  EState.IDLE &&  preFloor == this.elevator.getFloor()) {
                        System.out.println("^^^ERROR: ELEVATOR 4 BROKEN^^^^");
                    }

                }
                else {
                    System.out.println("Elevator " + elevator.getNumber() + " appears to be working as expected");
                }
                //So elevator is attached @ creation... checks elevator state which better update if it returns as fault
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}