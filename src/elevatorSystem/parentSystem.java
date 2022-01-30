package elevatorSystem;

import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
/**
 * @author Thomas
 *
 */
public class parentSystem {

	/**
	 * @param args
	 */
    private Object contents = null; // contents
    private boolean empty = true; // empty?
    public static Object x = null;
    
	public static void main(String[] args) {
		parentSystem parent = new parentSystem();
        Thread floor = new floorSubsystem(parent);	       
        Thread scheduler = new schedulerSubsystem(parent);      
        Thread elevator = new elevatorSubsystem(parent);
        		
        floor.start(); 
        scheduler.start(); 
        elevator.start();


	}
	
	/* 
	 * put() takes item and puts item in box
	 * 	
	 * @param
	 * Object ingredients: takes the ingredients array
	 * Box box: a reference to the shared box class
	 * 
	 * @return 
	 * void
	 * 
	 */
	public synchronized void put(Object item) {	
		
		while(!empty) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}				
        contents = item;
        empty = false;
        notifyAll();
				
	}
	
	/*
	 * get() returns the item within the box
	 * 
	 * @param
	 * 
	 * @return  
	 * the object gotten from the box
	 */
	public synchronized Object get() {
		while(empty) {
			
			try {
				wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
        Object item = contents;
        contents = null;
        empty = true;
        notifyAll();
        return item;			
	}
	/*
	 * check() checks what item is in the box without emptying the box
	 * 
	 */
    public synchronized Object check() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        Object item = contents;
        notifyAll();
        return item;
    }
	
	/*
	 * safePrint() receives strings and prints them Thread safely 
	 * 
	 * @param
	 * String s: The string to be printed
	 * 
	 * @return
	 * void
	 */
	public void safePrint(String s) {
		synchronized (System.out) {
			System.out.println(s);
		}
		
	}

}
