package project;
import data.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Scheduler Subsystem Class
 * 
 * @author Thomas
 */
public class Scheduler extends Thread{
	public enum State{
		UP,
		DOWN,
		STOP,
		OPEN_DOORS,
		CLOSE_DOORS,
		CONTINUE
	}

	static int SELFPORT = 219;
	private DatagramPacket sendPacket, receivePacket;
	private  DatagramSocket sendSocket, receiveSocket;

	State elevatorState1, elevatorState2, elevatorState3, elevatorState4;

	private int  elevatorFloor1, elevatorFloor2, elevatorFloor3, elevatorFloor4;




	static int ELEVATORPORT1 = 69, ELEVATORPORT2 = 70, ELEVATORPORT3 = 71, ELEVATORPORT4 = 72, //-- WHEN WE HAVE MULTIPLE CARS
			PACKETSIZE = 25,  FLOORPORT = 238;


	
	private Box floorBox;
	private Message messenger;
	public ArrayList<ElevatorEvent> elevatorEvents = new ArrayList<ElevatorEvent>();
	private ArrayList<ElevatorEvent> sendEvents = new ArrayList<ElevatorEvent>();
	public ArrayList<List<ElevatorEvent>> sequence = new ArrayList<List<ElevatorEvent>>();
	private String moveUp = "UP";
	private String moveDown = "DOWN";
	private String stop = "STOP";
	private String openDoors = "OPENDOORS";
	private String closeDoors = "CLOSEDOORS";
	private String continueMove = "CONTINUE";

	/**
	 * Constructor for the Scheduler
	 * 
	 *
	 */
	public Scheduler() {
		this.floorBox = floorBox;
		this.messenger = messenger;

		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			sendSocket = new DatagramSocket();

			// Construct a datagram socket and bind it to port 23
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets from the client
			receiveSocket = new DatagramSocket(SELFPORT);

			// to test socket timeout (2 seconds)
			//receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	private void sendElevator(int elev, int floor, byte msg[]) {
		//from the best elevator create the correct packet and send
		//correct data
		int toPort;
		//assign proper port
		switch(elev) {
			case(4):
				toPort = ELEVATORPORT4;
				if(msg[1] == 1) {
					elevatorState4 = State.UP;
				}
				else if(msg[1] == 0){
					elevatorState4 = State.DOWN;
				}
			case(3):
				toPort = ELEVATORPORT3;
				if(msg[1] == 1) {
					elevatorState4 = State.UP;
				}
				else if(msg[1] == 0){
					elevatorState4 = State.DOWN;
				}
			case(2):
				toPort = ELEVATORPORT2;
				if(msg[1] == 1) {
					elevatorState4 = State.UP;
				}
				else if(msg[1] == 0){
					elevatorState4 = State.DOWN;
				}
			default:
				toPort = ELEVATORPORT1;
				if(msg[1] == 1) {
					elevatorState4 = State.UP;
				}
				else if(msg[1] == 0){
					elevatorState4 = State.DOWN;
				}
		}

		sendPacket = new DatagramPacket(msg, msg.length,
				receivePacket.getAddress(), 68); // 68 is supposed to be the Port ElevatorCommunication listens too
													// which will then communicate with out Elevator class

		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Server: packet sent");

	}

	// THIS IS THE POINT OF COMMUNICATION - WHERE SCHEDULER RECEIVES PACKETS AND SENDS THEM

	public void receiveAndSend(){
		byte data[] = new byte[25];
		byte msg[] = new byte[25];
		receivePacket = new DatagramPacket(data, data.length);
		System.out.println("Scheduler: Waiting for Packet.\n");

		// Block until a datagram packet is received from receiveSocket.
		try {
			System.out.println("Waiting..."); // so we know we're waiting
			receiveSocket.receive(receivePacket);

			//catch IO exception and print stack trace
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		// Process the received datagram.
		System.out.println("Scheduler: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());

		int fromPort = receivePacket.getPort(); // if fromPort is not equal to ELEVATORPORT 1 2 3 or 4 then it is a packet received from client!

		int len = receivePacket.getLength();
		System.out.println("Length: " + len);
		System.out.print("Containing in Bytes: " );
		System.out.println(Arrays.toString(this.receivePacket.getData()));
		System.out.print("Containing in String: " );
		System.out.println(new String(this.receivePacket.getData(), 0, len));


		// This is where you would process the elevator datagram...

		//decode request and assign toFloor as the floor that will be sent to elevator
		if(fromPort == ELEVATORPORT1 || fromPort == ELEVATORPORT2 || fromPort == ELEVATORPORT3 || fromPort == ELEVATORPORT4) { //-- WHEN WE HAVE MULTIPLE CARS
			//received from elevator
			//         				0				1					2
			// data contains [ elevator number, direction (1 or 0), floorNumber]

			System.out.println("Received from elevator");
			int elevatorNumber = data[0];
			int floorNumber = data[2];
			int currFloor;

			//update the correct elevator
			if (elevatorNumber == 1) {
				//direction: 0 = stop; 1 = up; 2 = down
				if(data[1] == 1) {
					this.elevatorState1 = State.UP;
				}
				else if(data[1] == 0){
					this.elevatorState1 = State.DOWN;
				}
				elevatorFloor1 = floorNumber;
				System.out.println("\n Updating E1: " + elevatorState1 + ", " + elevatorFloor1 + "\n");
			}

			msg[0] = data[0];
			msg[1] = data[1];
			msg[2] = data[2];
			sendPacket = new DatagramPacket(msg, msg.length,
					receivePacket.getAddress(), FLOORPORT);
			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		else {


			// if it was from floor which means someone wants an elevator...
			System.out.println("Received from Floor");
			int toFloor = 0;
			int direction = data[0];


			//         0             1               2

			// data [direction, current floor, destination floor]

			msg[1] = (byte) direction; //direction

			toFloor = data[2];

			msg[2] = data[1];
			msg[3] = data[2];


			///int toElevator = getBestElevator(toFloor, direction); // get best elevator -- waiting for MULTIPLE CARS
			//msg[0] = (byte) toElevator;
			msg[0] = (byte) 1;

			// msg to elevator will contain [elevator number, direction/state, current floor, destination floor]

			sendElevator(1, toFloor, msg); // then send the packet to elevator

			System.out.println("Server: Sending packet:");
			System.out.println("To host: " + sendPacket.getAddress());
			System.out.println("Destination host port: " + sendPacket.getPort());
			len = sendPacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing: ");
			System.out.println(new String(sendPacket.getData(), 0, len));
			System.out.println(Arrays.toString(this.receivePacket.getData()) + "\n");
		}

	}

	// get the best elevator to send to user -- WHEN WE HAVE MULTIPLE CARS

	private int getBestElevator(int toFloor, int direction) {
		//from current data which elevator is best to send
		//check to see if any elevator status is marked as idle. if it is return that one!
		if(elevatorState1 == State.STOP ) return 1;
		if(elevatorState2 == State.STOP ) return 2;
		if(elevatorState3 == State.STOP) return 3;
		if(elevatorState4 == State.STOP) return 4;


		//No Elevator idle Check other specs to find best a case

		if((elevatorState1 == State.UP && direction == 1) && toFloor >= elevatorFloor1)
			return 1;
		if((elevatorState2 == State.UP && direction == 1 ) && toFloor >= elevatorFloor2)
			return 2;
		if((elevatorState3 == State.UP && direction == 1) && toFloor >= elevatorFloor3)
			return 3;
		if((elevatorState4 == State.UP && direction == 1 && toFloor >= elevatorFloor4))
			return 4;
		if((elevatorState1 == State.DOWN && direction == 2) && toFloor <= elevatorFloor1)
			return 1;
		if((elevatorState2 == State.DOWN && direction == 2) && toFloor <= elevatorFloor2)
			return 2;
		if((elevatorState3 == State.DOWN && direction == 2) && toFloor <= elevatorFloor3)
			return 3;
		if((elevatorState4 == State.DOWN && direction == 2) && toFloor <= elevatorFloor4)
			return 4;

		//Implementation should prevent any situation where all of these fail; however recall function
		return getBestElevator(toFloor, direction);
	}




	/**
	 * Thread loop for Elevator
	 */
	public void run(){
		while(true) {
			this.receiveAndSend();
			}
			/**

			byte[] data = new byte[100];
			floorReceivePacket = new DatagramPacket(data, data.length);
			try {

				receiveFloorSocket.receive(floorReceivePacket);
			} catch (IOException e) {
				System.out.print("IO Exception: likely:");
				e.printStackTrace();
				System.exit(1);
			}

			// Process the received datagram from Floor
			System.out.println("Scheduler: Packet received from Floor:");
			System.out.println("From host: " + floorReceivePacket.getAddress());
			System.out.println("Host port: " + floorReceivePacket.getPort());
			int len = floorReceivePacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing in String: " );
			System.out.println(new String(floorReceivePacket.getData(), 0, len));
			System.out.print("Containing in Bytes: ");
			System.out.println(Arrays.toString(floorReceivePacket.getData()));
			System.out.println("\n");



			
			Main.safePrint("Scheduler Got:\t" + elevatorEvents);
			Main.safePrint("Scheduler Got:\t" + new String(floorReceivePacket.getData()));

			// maybe I should fill the elevator events list with the data in the floor receive packet, it will be in bytes
			// then can be converted t string and then I can parse it and create elevator events
			// I am confused as to how to put the data retrieved from the packet to the elevator event list in scheduler
			 */

			
			
			//organizeEvents(elevatorEvents);
			
			//System.out.println(sequence.toString());
			
			//communicateWithElevator();

	}
	
	/** 
	 * For elevator events in elevatorEvents from Floor.java check if events are going in the same direction 
	 * and if the end floor is the same or lower/higher (depending on the direction of the previous event) of 
	 * the previous event. When the current event is not in the same direction as the previous events send the collected
	 * events. Then check whether the next event is in the same direction as the current event,
	 * if not then send the current event. If next event is in the same direction hen check if the next event 
	 * is not in the same way as the current event, if not then send the current event. 
	 * Then send remaining collected events.
	 * 
	 *
	 */
	public void /*ArrayList<ElevatorEvent>*/ organizeEvents(ArrayList<ElevatorEvent> events) {
		ElevatorEvent previousEvent = elevatorEvents.get(0);
		int index = 0;
		
		for(ElevatorEvent e: elevatorEvents) {										

			if(previousEvent.getDir() == e.getDir()) {
				if(e.getDir()==1 && (previousEvent.getEndFloor() >= e.getEndFloor())) {
					sendEvents.add(e);
				}
				else if(e.getDir()==-1 && (previousEvent.getEndFloor() <= e.getEndFloor())) {
					sendEvents.add(e);
				}	
				
				previousEvent = e;
			}				
			else {					
				//sendInstructionsToElevator(sendEvents);
				sequence.add(sendEvents);
				sendEvents = new ArrayList<ElevatorEvent>();
				sendEvents.add(e);
				
				if(index < elevatorEvents.size()-1) {
					previousEvent = elevatorEvents.get(index+1);
				}
				
				if(e.getDir() != previousEvent.getDir()) {
					//sendInstructionsToElevator(sendEvents);
					sequence.add(sendEvents);
					sendEvents = new ArrayList<ElevatorEvent>();
				}
				else if((e.getDir() == 1) && (e.getEndFloor() > previousEvent.getEndFloor())) {
					//sendInstructionsToElevator(sendEvents);		
					sequence.add(sendEvents);
				}
				else if((e.getDir() == -1) && (e.getEndFloor() < previousEvent.getEndFloor())) {
					//sendInstructionsToElevator(sendEvents);	
					sequence.add(sendEvents);
				}
			}
			index++;
		}
		
		//sendInstructionsToElevator(sendEvents);
		sequence.add(sendEvents);
		
	}
	
	/**
	 * Takes a string from the set instruction list and sends it to the Elevator 
	 */
	public void sendInstruction(String message) {
		
		//Main.safePrint("Scheduler Sent:\t" + message);
		messenger.setMessage(message);		
	}
	/**
	 * communicateWithElevator() uses the sequence list gathered by organizeEvents and uses that list to 
	 * communicate with the elevator using sendInstruction() to determine what the elevators next instruction will be
	 *
	 */
	public void communicateWithElevator() {

		List<Integer> floorsToVisit = new ArrayList<Integer>();
		int sequenceDir = 0;
		int currentFloor = 0;
		
		for(List<ElevatorEvent> s: sequence) {
			
			for( ElevatorEvent e: s) {				//create list of floors to visit
				floorsToVisit.add(e.getStartFloor());
				floorsToVisit.add(e.getEndFloor());
				sequenceDir = e.getDir();
				
			}
							
			floorsToVisit = floorsToVisit.stream().distinct().collect(Collectors.toList());		//remove duplicates from floors to visit
			
			if(sequenceDir == 1) { 					//if direction is up, sort list in ascending order.
				Collections.sort(floorsToVisit); 
			}
			else {									//else sort list in descending order
				Collections.sort(floorsToVisit, Collections.reverseOrder()); 
			}
			
			System.out.println("\nFloors to visit " + floorsToVisit);				
			//System.out.println("\ndirection " + sequenceDir);				

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			boolean arrived = false;
			boolean arrivedAtLastFloor = false;
			
			//loop through each index in the list of floors to visit
			for(int i = 0; i <= floorsToVisit.size(); i++) {
				//loop while the elevator has not reached the destination floor 
				while(!arrived) {
					/*If the current floor in the opposite direction of the destination floor from
					* the starting floor, move up or down repeatedly until we reach the starting floor
					*/
					if((currentFloor < floorsToVisit.get(i) && sequenceDir == -1)) {
						sendInstruction(moveUp);
					}
					else if((currentFloor > floorsToVisit.get(i) && sequenceDir == 1)) {
						sendInstruction(moveDown);
					}
					//Else if the current floor is the starting floor set the movement direction from there
					else if((currentFloor == floorsToVisit.get(i)) && i == 0){
						Main.safePrint("Arrived at floor to visit - start: " + currentFloor);
						sendInstruction(openDoors);
						sendInstruction(closeDoors);
						if(sequenceDir == 1) {
							sendInstruction(moveUp);
						}
						else{
							sendInstruction(moveDown);
						}
						arrived = true;
					}
					//else set the direction to either up or down depending on the sequence
					else if(i == 0) {
						if(sequenceDir == 1) {
							sendInstruction(moveUp);
						}
						else{
							sendInstruction(moveDown);
						}
					}
					//If the current floor is the last floor to visit don't move the elevator after it stopped
					if((currentFloor == floorsToVisit.get(i)) && (i == floorsToVisit.size() - 1)) {
						Main.safePrint("Arrived at floor to visit - stop: " + currentFloor);
						sendInstruction(stop);
						sendInstruction(openDoors);
						sendInstruction(closeDoors);
						arrived = true;
						arrivedAtLastFloor = true;
					}
					/*else if the elevator is at a destination floor before the last floor
					* stop the elevator for a moment before starting up again
					*/
					else if((currentFloor == floorsToVisit.get(i)) && (i != 0)) {
						Main.safePrint("Arrived at floor to visit - continue: " + currentFloor);
						sendInstruction(stop);
						sendInstruction(openDoors);
						sendInstruction(closeDoors);
						
						if(sequenceDir == 1) {
							sendInstruction(moveUp);
						}
						else{
							sendInstruction(moveDown);
						}
						
						arrived = true;
						
					}
					//else continue to move the elevator in it's previously set direction
					else if(i>0) {
						Main.safePrint("Not this floor");
						sendInstruction(continueMove);
					}
					
					//if the arrived at destination floor is not the last destination floor get the next floor
					if(!arrivedAtLastFloor) {
						currentFloor = Integer.parseInt(messenger.getFloor());
					}
				}
				
				//if the current index is not the last one set the loop to go again
				if(i != floorsToVisit.size() - 1) {
					arrived = false;
				}				
			}
			
			//sleep to account for the last doors to open and close
			try {
				TimeUnit.SECONDS.sleep(7);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			System.out.println("Sequence finished\n");
		
			floorsToVisit = new ArrayList<Integer>();
			
		}
	}
		
	/**
	 * Validates the completion of a request by an elevator and pops that request off the queue
	 * 
	 * @param completionTime - datetime when elevator completed request so as to compare its finished after request date
	 * @param currentFloor - int of elevator's current position, gets cross referenced with original request's destination floor
	 * @param visitedRequestedFloor - boolean whether or not elevator passed/opened at the floor it was called on for, checks if thats the request source floor
	 * @return RequestData - popped RequestData from queue
	 */
	public synchronized ElevatorEvent completeCall(Date completionTime, int currentFloor, boolean visitedRequestedFloor) {
		if(currentFloor == elevatorEvents.get(elevatorEvents.size()-1).getEndFloor() &&
			visitedRequestedFloor) {

			System.out.println("Elevator completed request at "+ completionTime.toString());
			ElevatorEvent completedRequest = elevatorEvents.remove(elevatorEvents.size()-1);
			notifyAll();

			return completedRequest;
		}
		return null;
	}

	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		while(true) {
			scheduler.start();
		}
	}
}