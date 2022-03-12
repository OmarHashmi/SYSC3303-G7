package project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

import data.*;
import elevator.*;

/**
 * Elevator Subsystem Class
 * 
 * @author Omar Hashmi
 * @author Sam Al Zoubi
 */
public class Elevator extends Thread {
	// box to communicate with scheduler
	private int currentFloor;

	private EState currentState;
	private int elevatorNumber;
	
	// Subsystem variables
	private ArrivalSensor sensor;
	private ArrayList<ElevatorButton> buttons;
	private ArrayList<ElevatorLamp> lamps;
	private ArrivalSensor sensors;
	private ElevatorDoor door;
	private Motor motor;
	
	private  DatagramSocket sendSocket, receiveSocket;

	/**
	 * Constructor for Elevator
	 * 
	 *
	 */
	public Elevator(int elevatorNumber) {
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(SysInfo.elevatorPorts[elevatorNumber]);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		this.elevatorNumber = elevatorNumber;
		
		this.currentState = EState.IDLE;
		this.currentFloor = 0;
		
		this.sensor = new ArrivalSensor(0,this);
		this.door = new ElevatorDoor(elevatorNumber);
		this.motor = new Motor();
	
		this.buttons = new ArrayList<>();
		this.lamps = new ArrayList<>();
		this.sensors = new ArrivalSensor(0, this);
		
		for(int i=0;i<SysInfo.numFloors;i++) {
			ElevatorLamp newLamp = new ElevatorLamp(i);
			this.lamps.add(newLamp);
			ElevatorButton newButton = new ElevatorButton(i,newLamp);
			this.buttons.add(newButton);
		}
	}

	public int getElevatorNumber() {
		return elevatorNumber;
	}

	/**
	 * Method will run infinitely until program termination
	 */
	public void run() {
		while(true) {
			receive();
		}
	}

	public void receive(){
		byte data[] = new byte[1];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		
		// Wait for packet
		try {
			receiveSocket.receive(receivePacket);
			String tmp = Arrays.toString(receivePacket.getData());
			tmp = tmp.replace("[", "").replace("]", "").replace(",", "");
			Main.clog(0, "Elevator "+elevatorNumber+" got \""+tmp+"\"");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		int startFloor = data[0];
		
		move(startFloor);
	}
	
	public void sendToScheduler(int dir, int floor) {
		byte[] data = new byte[3];
		data[0] = (byte) dir;
		data[1] = (byte) this.elevatorNumber;
		data[2] = (byte) floor;
		
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(SysInfo.schedulerAddr);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		DatagramPacket packet = new DatagramPacket(data, data.length, addr, SysInfo.schedulerPort);
		try {
			sendSocket.send(packet);
			Main.clog(0, "Elevator "+elevatorNumber+" sent \""+dir+" "+this.elevatorNumber+" "+floor+"\" to Scheduler");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void move(int floor) {
		if(currentFloor<floor) {
			currentState=EState.UP;
		}else {
			currentState=EState.DOWN;
		}
		
		if(this.currentFloor != floor) {
			 if(currentState == EState.UP) {
				currentFloor++;
			}else if(this.currentState == EState.DOWN){
				currentFloor--;
			}
			
			Main.print(" ->"+floor+" Elevator "+elevatorNumber+" at floor "+currentFloor);
			Main.clog(elevatorNumber+1, "At floor "+currentFloor);
		}
		
		try {
			sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (currentState == EState.UP) {
			sendToScheduler(1, currentFloor);
		}
		else if(currentState == EState.DOWN){
			sendToScheduler(0, currentFloor);
		}
	}
	
	public int getFloor() {
		return this.currentFloor;
	}
	
	public EState getCurrentState() {
		return this.currentState;
	}
}