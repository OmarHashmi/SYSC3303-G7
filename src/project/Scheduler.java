package project;
import data.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Scheduler Subsystem Class
 * 
 * @author Thomas
 */
public class Scheduler extends Thread{
	private  DatagramSocket sendSocket, receiveSocket;

	private ArrayList<ElevatorEvent> newEvents  = new ArrayList<ElevatorEvent>();
	
	private ElevatorInfo[] elevators = new ElevatorInfo[SysInfo.numElevators];
	

	/**
	 * Constructor for the Scheduler
	 * 
	 *
	 */
	public Scheduler() {
		for(int i=0;i<SysInfo.numElevators;i++) {
			elevators[i] = new ElevatorInfo(i,EState.IDLE,0);
		}
		
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(SysInfo.schedulerPort);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void receive(){
		byte data[] = new byte[3];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		
		// Wait for packet
		try {
			receiveSocket.receive(receivePacket);
			String tmp = Arrays.toString(receivePacket.getData());
			tmp = tmp.replace("[", "").replace("]", "").replace(",", "");
			Main.clog(0,"Scheduler got \""+tmp+"\"");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// From Floor
		if(receivePacket.getPort() == SysInfo.floorPort){
			int dir        = data[0];
			int startFloor = data[1];
			int endFloor   = data[2];

			newEvents.add(new ElevatorEvent(dir,startFloor,endFloor));
			dispatch();
		}
		// From Elevator
		else{
			int dir            = data[0];
			int elevatorNumber = data[1];
			int floorNumber    = data[2];

			//update the correct elevator
			dropoff(elevatorNumber, floorNumber);

			sendToFloor(elevatorNumber, dir, floorNumber);
		}
	}
	
	private void sendToElevator(int elevator, int floor) {
		int destPort = SysInfo.elevatorPorts[elevator];
		
		byte[] data = new byte[1];
		data[0] = (byte) floor;
		
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(SysInfo.elevatorAddrs[elevator]);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		DatagramPacket packet = new DatagramPacket(data, data.length,addr, destPort); 
		try {
			sendSocket.send(packet);
			Main.clog(0, "Scheduler sent \""+floor+"\" to Elevator "+elevator);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void sendToFloor(int elevator, int dir, int floor) {
		byte[] data = new byte[3];
		data[0] = (byte) dir;
		data[1] = (byte) elevator;
		data[2] = (byte) floor;
		
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(SysInfo.floorAddr);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		DatagramPacket packet = new DatagramPacket(data, data.length, addr, SysInfo.floorPort);
		try {
			sendSocket.send(packet);
			Main.clog(0, "Scheduler sent \""+dir+" "+elevator+" "+floor+"\" to Floor");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void dispatch() {
		if(newEvents.size()<1) {
			return;
		}
				
		ElevatorEvent tmp = newEvents.get(0);
		for(int i=0;i<SysInfo.numElevators;i++) {
			// Found elevator going that way
			if(elevators[i].onTheWay(tmp.getDir(),tmp.getStartFloor())) {
				Main.print(tmp.getStartFloor()+"->"+tmp.getEndFloor()+" Sending Elevator "+i);
				
				tmp = newEvents.remove(0);
				elevators[i].addEvent(tmp);
								
				return;
			}
			// Found a free elevator
			else if(elevators[i].getState() == EState.IDLE) {
				Main.print(tmp.getStartFloor()+"->"+tmp.getEndFloor()+" Sending Elevator "+i);
				
				tmp = newEvents.remove(0);
				sendToElevator(i,tmp.getStartFloor());
				elevators[i].addEvent(tmp);
				
				return;
			}
		}
	}
	
	private void dropoff(int i, int floor) {
		elevators[i].updateFloor(floor);
		
		if(elevators[i].getState()==EState.IDLE) {
			dispatch();
		}
		
		if(elevators[i].hasNext()) {
			sendToElevator(i,elevators[i].nextFloor());
		}
	}
	
	/**
	 * Thread loop for Elevator
	 */
	public void run(){		
		while(true) {
			this.receive();
		}
	}
}