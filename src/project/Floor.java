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

	private ArrayList<Byte> elevatorSend;
	static List<String> allLines;
	
	/**
	 * Constructor for Floor
	 */
	public Floor() {
		try {
			// Construct a datagram socket and bind it to any available port on the local host machine
			sendReceiveSocket = new DatagramSocket(SysInfo.floorPort);
			} catch (SocketException e) {
			e.printStackTrace();
		}
		this.elevatorSend = new ArrayList<Byte>();

	}
	
	/**
	 * Thread loop for floor
	 * Imports data file and sends to scheduler
	 */
	public void run() {
		this.readFile("resources/elevator_events.txt");		
	}
    		
	/**
	 * 
	 * 
	 * @param path
	 */
	public boolean readFile(String path) {
		Path path2 = Paths.get(path);
		try {
			File file = new File(path);
			Scanner myReader = new Scanner(file);
			allLines = Files.readAllLines(path2);
			for(String line : allLines){
				String temp = line;
				String[] data = myReader.nextLine().split(" ");

				Date date		= new SimpleDateFormat("HH:mm:ss.SSS").parse(data[0]);
				int dir			= (data[2].equals("Up"))? 1 : 0;
				int startFloor	= Integer.parseInt(data[1]);
				int endFloor	= Integer.parseInt(data[3]);

				this.sendInstruction(dir, startFloor, endFloor);
				
				try {
					Thread.sleep(SysInfo.floorSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
				
			}
			myReader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean sendInstruction(int dir, int startFloor, int endFloor) {
		byte msg[] = new byte[3];
		msg[0] = (byte) dir;
		msg[1] = (byte) startFloor;
		msg[2] = (byte) endFloor;

		try {
			DatagramPacket packet = new DatagramPacket(msg, msg.length,InetAddress.getLocalHost(), SysInfo.schedulerPort);
			sendReceiveSocket.send(packet);
			Main.clog(0, "Floor sent \""+dir+" "+startFloor+" "+endFloor+"\" to Scheduler");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
			return false;
		}
		return true;
	}
}