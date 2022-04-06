package data;

import java.time.Instant;

public class SysInfo {
	public static boolean gui = true;
	public static boolean verbose = true;
	
	public static int numElevators = 4;
	public static int numFloors = 10;

	public static int elevatorSpeed;
	public static int errorTime = 10000;
	public static int floorSpeed = 500;
	public static int doorSpeed;
	public static Instant startTime;
	
	public static String   schedulerAddr = "127.0.0.1";
	public static String   floorAddr     = "127.0.0.1";
	public static String[] elevatorAddrs = {"127.0.0.1","127.0.0.1","127.0.0.1","127.0.0.1"};
	
	public static int   schedulerPort = 200;
	public static int   floorPort     = 201;
	public static int[] elevatorPorts = {202,203,204,205};
}