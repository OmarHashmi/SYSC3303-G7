package data;

import java.util.*;

import project.*;
import elevator.*;


public class ElevatorInfo {
	private int number;
	private EState state;
	private int floor;
	private ArrayList<Integer> stops = new ArrayList<Integer>();
	private ArrayList<ElevatorEvent> events = new ArrayList<ElevatorEvent>();
	
	private ElevatorDoor door;
	
	public ElevatorInfo(int number,EState state, int floor) {
		this.number = number;
		this.state = state;
		this.floor = floor;
		this.door = new ElevatorDoor(number);
	}
	
	public void addEvent(ElevatorEvent event) {
		if(events.size()==0) {
			state = event.getState();
		}		
		
		events.add(event);
		stops.add(event.getStartFloor());
		stops.add(event.getEndFloor());
		
		Main.clog(number+1, "Assigned from "+event.getStartFloor()+" to "+event.getEndFloor());
		
		if(state == EState.UP) {
			Collections.sort(stops);
		}
		else {
			Collections.sort(stops,Collections.reverseOrder());
		}
		
		// Remove duplicates
		Set<Integer> tmp = new LinkedHashSet<Integer>(stops);
		stops.clear();
		stops.addAll(tmp);
	}
	
	public void updateFloor(int floor) {
		this.setFloor(floor);
		
		if(stops.isEmpty() || floor != stops.get(0)) {
			return;
		}
		
		stops.remove((Object) floor);
		
		for(int i=0;i<events.size();i++) {
			if(events.get(i).getStartFloor() == floor) {
				Main.print("     Elevator "+number+" Arrived at "+floor);
				door.open();
				Main.clog(number+1,"Picked Up from "+events.get(i).getStartFloor()+" to "+floor);
				door.close();
			}
			if(events.get(i).getEndFloor() == floor) {
				Main.print("     Elevator "+number+" Arrived at "+floor);
				door.open();
				Main.clog(number+1,"Delivered from "+events.get(i).getStartFloor()+" to "+floor);
				door.close();
				events.remove(i);
				i--;
			}
		}
		
		if(stops.isEmpty()) {
			Main.print("     Elevator "+number+" is idle");
			Main.clog(number+1, "Idle");
			state = EState.IDLE;
		}
	}

	public boolean onTheWay(int dir, int floor) {
		if(state == EState.UP) {
			if(stops.get(0) < floor && floor <= stops.get(stops.size()-1) && dir == 1) {
				return true;
			}
		}else if(state == EState.DOWN) {
			if(stops.get(0) > floor && floor >= stops.get(stops.size()-1) && dir != 1) {
				return true;
			}
		}
		return false;
	}
	
	public int nextFloor() {
		return stops.get(0);
	}
	public boolean hasNext() {
		return !stops.isEmpty();
	}
	
	public EState getState() {
		return state;
	}
	public void setState(EState state) {
		this.state = state;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
}
