package data;

import java.util.ArrayList;

/**
 * Data container which is shared between subsystems so they can communicate
 * 
 * @author Omar
 */
public class Box {
	private ArrayList<ElevatorEvent> data;
	
	public Box() {
		this.data = new ArrayList<ElevatorEvent>();
	}
	
    public synchronized boolean isEmpty() {
    	return data.isEmpty();
    }

    public synchronized void add(ElevatorEvent event) {
    	this.data.add(event);
    }
    /**
     * Add an array list of elevator events to the box
     * @param array list of elevator events
     */
    public synchronized void add(ArrayList<ElevatorEvent> events) {
    	this.data.addAll(events);
    }
    
    public synchronized ElevatorEvent get(int index) {
    	if(index<0 || index>=data.size() || data.isEmpty()) {
    		return null;
    	}
    	return data.get(index);
    }
    public synchronized ElevatorEvent get() {
    	return this.get(data.size()-1);
    }
    
    public synchronized ElevatorEvent remove(int index) {
    	if(index<0 || index>=data.size() || data.isEmpty()) {
    		return null;
    	}
    	return data.remove(index);
    }
    public synchronized ElevatorEvent remove() {
    	return this.remove(data.size()-1);
    }
    
    public synchronized String toString() {
    	return data.toString();
    }
}
