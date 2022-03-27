package data;

public class SysInfo {
	public static final boolean gui = true;
	public static final boolean verbose = true;
	
	public static final int numElevators = 4;
	public static final int numFloors = 10;

	public static final int elevatorSpeed = 1000;
	public static final int errorTime = 10000;
	public static final int floorSpeed = 500;
	
	public static final String   schedulerAddr = "127.0.0.1";
	public static final String   floorAddr     = "127.0.0.1";
	public static final String[] elevatorAddrs = {"127.0.0.1","127.0.0.1","127.0.0.1","127.0.0.1"};
	
	public static final int   schedulerPort = 200;
	public static final int   floorPort     = 201;
	public static final int[] elevatorPorts = {202,203,204,205};
}