package elevatorSystem;

import java.util.Scanner;
import java.io.File; 
import java.io.FileNotFoundException; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.concurrent.TimeUnit;

/**
 * @author Thomas
 *
 */
public class floorSubsystem {

	/**
	 * @param args
	 */
	static HashMap<Integer, List<String>> table = new HashMap<Integer, List<String>>();
			
	public floorSubsystem() {
		
	}
	
	public static void main(String[] args) {		
		
	    try {
	        File file = new File("resources/tablefile.txt");
	        Scanner myReader = new Scanner(file); 
	        
	        for(int count = 0; myReader.hasNextLine(); count++) {
	        	List<String> list = new ArrayList<String>();
        		String data = myReader.nextLine();
        		String[] array = data.split(" ");
        		
        		list.addAll(Arrays.asList(array));
	        	table.put(count, list);

	        }
	        	myReader.close();
	        
	      } catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
	      } 
	      
	      System.out.print(table);

	}

}
