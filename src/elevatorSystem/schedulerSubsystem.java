/**
 * 
 */
package elevatorSystem;

import java.io.File; 
import java.io.FileNotFoundException; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.concurrent.TimeUnit;

/**
 * @author Thomas
 *
 */
@SuppressWarnings("unused")
public class schedulerSubsystem extends Thread{

	/**
	 * @param args
	 */
	public static parentSystem parent;
	static HashMap<Integer, List<String>> table = new HashMap<Integer, List<String>>();
	
	public schedulerSubsystem(parentSystem parent) {
		this.parent = parent;
	}
	
	public void run(){
		// TODO Auto-generated method stub
		while(true) {
			table = (HashMap<Integer, List<String>>) parent.get();
			parent.safePrint("scheduler received: " + table.toString());
		}

	}

}
