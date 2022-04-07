package data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SysInfo {
	public static boolean gui;
	public static boolean verbose;
	
	public static int numElevators;
	public static int numFloors;

	public static int elevatorSpeed;
	public static int errorTime;
	public static int floorSpeed;
	public static int doorSpeed;
	public static int faultTime;
	
	public static Instant startTime;
	
	public static String   schedulerAddr;
	public static String   floorAddr;
	public static String[] elevatorAddrs;
	
	public static Integer   schedulerPort;
	public static Integer   floorPort;
	public static Integer[] elevatorPorts;
	
	public static void init() {
		Path path = Paths.get("resources/info.ini");
		try {
			File file = new File("resources/info.ini");
			Scanner myReader = new Scanner(file);
			List<String> lines = Files.readAllLines(path);
			for(String line : lines){
				String[] data = line.split(" ");

				if(data[0].equals("gui")){
					SysInfo.gui = data[2].equals("true")?true:false;
				}else if(data[0].equals("verbose")){
					SysInfo.verbose = data[2].equals("true")?true:false;
				}else if(data[0].equals("numElevators")){
					SysInfo.numElevators = Integer.parseInt(data[2]);
				}else if(data[0].equals("numFloors")){
					SysInfo.numFloors = Integer.parseInt(data[2]);					
				}else if(data[0].equals("elevatorSpeed")){
					SysInfo.elevatorSpeed = Integer.parseInt(data[2]);
				}else if(data[0].equals("errorTime")){
					SysInfo.errorTime = Integer.parseInt(data[2]);
				}else if(data[0].equals("floorSpeed")){
					SysInfo.floorSpeed = Integer.parseInt(data[2]);
				}else if(data[0].equals("doorSpeed")){
					SysInfo.doorSpeed = Integer.parseInt(data[2]);
				}else if(data[0].equals("faultTime")){
					SysInfo.faultTime = Integer.parseInt(data[2]);
				}else if(data[0].equals("shedulerAddr")){
					SysInfo.schedulerAddr = data[2];
				}else if(data[0].equals("floorAddr")){
					SysInfo.floorAddr = data[2];
				}else if(data[0].equals("elevatorAddrs")){
					String tmp = data[2];
					SysInfo.elevatorAddrs = tmp.replace("{","").replace("}","").split(",");
				}else if(data[0].equals("schedulerPort")){
					SysInfo.schedulerPort = Integer.parseInt(data[2]);
				}else if(data[0].equals("floorPort")){
					SysInfo.floorPort = Integer.parseInt(data[2]);
				}else if(data[0].equals("elevatorPorts")){
					String tmp = data[2];
					String[] tmpStrArr = tmp.split(",");
					ArrayList<Integer> tmpIntArr = new ArrayList<Integer>();
					
					for(String s: tmpStrArr) {
						tmpIntArr.add(Integer.parseInt(s.replace("{","").replace("}","")));
					}
					SysInfo.elevatorPorts = new Integer[tmpIntArr.size()];
					SysInfo.elevatorPorts = tmpIntArr.toArray(SysInfo.elevatorPorts);
				}
			}
			myReader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}