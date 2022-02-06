package project;

import java.util.ArrayList;

/**
 * Data container which is shared between subsystems so they can communicate
 * 
 * @author Omar
 */
public class Box {
	private ArrayList<ElevatorEvent> contents = null;
    private boolean empty = true;
	
    /**
     * Adds item to the box
     * 
     * @param item The item to be added
     */
	public synchronized void put(ArrayList<ElevatorEvent> item) {		
		while(!empty) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}				
        this.contents = item;
        this.empty = false;
	}
	
	/**
	 * Removes the item from the box, then returns it
	 * 
	 * @return The item
	 */
	public synchronized ArrayList<ElevatorEvent> pop() {
		while(this.empty) {		
			try {
				wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		ArrayList<ElevatorEvent> item = contents;
        contents = null;
        empty = true;
        return item;
	}
	
	/**
	 * Returns the item in the box, without removing it
	 * 
	 * @return The item
	 */
    public synchronized ArrayList<ElevatorEvent> get() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        return contents;
    }
    
    public boolean getEmpty() {
    	return this.empty;
    }

    public synchronized void addEvent(ElevatorEvent event) {
    	this.contents.add(event);
    	empty=false;
    }
    
    public synchronized ElevatorEvent removeTop() {
    	if(empty) {
    		return null;
    	}
    	return contents.remove(contents.size()-1);
    }
}
