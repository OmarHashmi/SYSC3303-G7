package project;

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
	
    public boolean isEmpty() {
    	return data.isEmpty();
    }

    public void add(ElevatorEvent event) {
    	this.data.add(event);
    }
    
    public ElevatorEvent get(int index) {
    	if(index<0 || index>=data.size() || data.isEmpty()) {
    		return null;
    	}
    	return data.get(index);
    }
    public ElevatorEvent get() {
    	return this.get(data.size()-1);
    }
    
    public ElevatorEvent remove(int index) {
    	if(index<0 || index>=data.size() || data.isEmpty()) {
    		return null;
    	}
    	return data.remove(index);
    }
    public ElevatorEvent remove() {
    	return this.remove(data.size()-1);
    }
    
    public String toString() {
    	return data.toString();
    }
}
