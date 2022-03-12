package project;
import data.*;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Floor Subsystem Class
 * 
 * @author Thomas Poetting
 * @author Sam Al Zoubi
 */
public class Floor extends Thread{
	private DatagramSocket sendReceiveSocket, receiveSocket;
	private DatagramPacket sendPacket, receivePacket, ackPacket;
	private Box schedulerBox;
	private Scheduler scheduler;
	private ArrayList<ElevatorEvent> elevatorEvents = new ArrayList<ElevatorEvent>();
	private ArrayList<Byte> elevatorSend;
	static List<String> allLines;
	static int SCHEDULER_PORT = 219, SELFPORT = 238;
	
	/**
	 * Constructor for Floor
	 * 
	 *
	 */
	public Floor() {
		try {
			// Construct a datagram socket and bind it to any available port on the local host machine
			sendReceiveSocket = new DatagramSocket(SELFPORT);
			} catch (SocketException e) {
			e.printStackTrace();
		}
		this.elevatorSend = new ArrayList<Byte>();

	}

	/**

	private void sendAndReceive(byte[] elevatorEvents){

		try {
			sendReceiveSocket = new DatagramSocket();  // create socket for receiving and sending
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}

		try {
			sendPacket = new DatagramPacket(elevatorEvents, elevatorEvents.length, InetAddress.getLocalHost(), 23); // whatever port scheduler is on
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Floor: Sending a packet to the Scheduler!");
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		int len = sendPacket.getLength();
		System.out.println("Length: " + len);
		System.out.print("Containing in String: ");
		System.out.println(new String(sendPacket.getData(), 0, len));
		System.out.print("Containing in Bytes: ");
		System.out.println(Arrays.toString(sendPacket.getData()));

		try {
			sendReceiveSocket.send(sendPacket); // send datagram via sendReceiveSocket
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Floor: Packet sent.\n");

		try {
			receiveSocket = new DatagramSocket(750); // or whatever port the floor is
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//Receive ACK from Scheduler
		byte[] ack = new byte[15];
		ackPacket = new DatagramPacket(ack, ack.length);
		try {
			receiveSocket.receive(ackPacket);
			System.out.print("Floor: Acknowledgement Packet of Events has been received from the Scheduler: " + Arrays.toString(ackPacket.getData()));
			System.out.println("\n" + "Floor: Acknowledgement Packet of Events has been received from the Scheduler: " + new String(ackPacket.getData()));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
	 */

	
	/**
	 * Thread loop for floor
	 * Imports data file and sends to scheduler
	 */
	public void run() {


		this.readFile("resources/elevator_events.txt");
		
		//synchronized(schedulerBox) {
    	//	Main.safePrint("Floor Sent:\t" + elevatorEvents.toString());
    		
    		//for(ElevatorEvent e: elevatorEvents) {
    			//schedulerBox.add(e);
    		//}

			//for(ElevatorEvent e: elevatorEvents) {
				//for(byte b: e.getElevatorBytes()){
				//	elevatorSend.add(b);
				//}
			//}

			//for(byte b: schedulerBox.getData().toString().getBytes()) {
			//	elevatorSend.add(b);
			//}

			//this.sendAndReceive(this.toByteArray(elevatorSend));


    		
        	//schedulerBox.notifyAll();
        	
        	
        	//uncomment later to be used to confirm floor has received passengers
        	//while(true) {
	        	//try {
				//	schedulerBox.wait();
				//	System.out.println("Floor Received Passengers");
				//} catch (InterruptedException e) {
				//	e.printStackTrace();
				//}
        	//}
        	
        	
        	
		}
    	
//    	while (true) {
//			while (!elevatorEvents.isEmpty()) {
//				scheduler.callForElevator(elevatorEvents.remove(elevatorEvents.size()-1));
//			}
//		}

	
	/**
	 * 
	 * 
	 * @param path
	 */
	public void readFile(String path) {
		Path path2 = Paths.get(path);
		try {
			File file = new File(path);
			Scanner myReader = new Scanner(file);
			allLines = Files.readAllLines(path2);
			for(String line : allLines){
				String temp = line;
				String[] data = myReader.nextLine().split(" ");

				Date date		= new SimpleDateFormat("HH:mm:ss.SSS").parse(data[0]);
				int dir			= (data[2].equals("Up"))? 1 : -1;
				int startFloor	= Integer.parseInt(data[1]);
				int endFloor	= Integer.parseInt(data[3]);

				this.sendInstruction(temp);
				//ElevatorEvent event = new ElevatorEvent(date,dir,startFloor,endFloor);
						
				//elevatorEvents.add(event);
			}
			myReader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendInstruction(String line) {

		System.out.println("Request made: " + line);

		String[] splitted = line.split("\\s+");

		byte msg[] = new byte[3];	// Bit 0 - Direction 	Bit 1 - initial Floor   Bit 2 - des Floor
									// before it was --  Bit 0 - Direction 	Bit 1,2 - initial Floor   Bit 3,4 - des Floor

		// Direction // assigned 1 for Up // assigned 0 for Down
		if (splitted[2].equals("Up")) {
			msg[0] = 1;

		} else if (splitted[2].equals("Down")) {
			msg[0] = 0;

		} else {
			System.out.println("Input file format incorrect");
			System.exit(1);
		}

		// Current floor
		byte n = (byte) (Integer.parseInt(splitted[1]));
		msg[1] = n;

		// destination floor
		byte d = (byte) (Integer.parseInt(splitted[3]));
		msg[2] = d;

		try {
			sendPacket = new DatagramPacket(msg, msg.length,
					InetAddress.getLocalHost(), SCHEDULER_PORT); // whatever scheduler port is
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.print("Contents sent: " );
		for (int i = 0; i < msg.length; i++) {	// Printing Byte array contents
			System.out.print(msg[i]);
		}
		System.out.println("\nElevator request sent.\n");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("cant Sleep");
		}

	}


	public void receiveMessage() {
		// Now receiving
		byte data[] = new byte[3];
		receivePacket = new DatagramPacket(data, data.length);
		try {
			// Block until a datagram is received via sendReceiveSocket.
			sendReceiveSocket.receive(receivePacket);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.print("Received content containing: ");
		// Form a String from the byte array.
		String received = new String(data,0,receivePacket.getLength());
		System.out.println(received);
	}


	private byte[] toByteArray(ArrayList<Byte> request) {
		byte[] msg = new byte[request.size()];
		for (int i = 0; i < request.size(); i++) {
			msg[i] = request.get(i);
		}
		return msg;
	}
	
	public ArrayList<ElevatorEvent> getElevatorEvents(){
		return elevatorEvents;
	}
}