package project;

import java.awt.*;
import javax.swing.*;

import data.SysInfo;

public class Consoles {
	private JScrollPane[] scrolls = new JScrollPane[SysInfo.numElevators+1];
	private JTextArea[] texts = new JTextArea[SysInfo.numElevators+1];
	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();
	
	public Consoles(){
		if(!SysInfo.gui) {
			return;
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setLayout(new FlowLayout());
		
		for(int i=0;i<SysInfo.numElevators+1;i++) {
			texts[i] = new JTextArea(42,22);
			scrolls[i] = new JScrollPane(texts[i]);
			panel.add(scrolls[i]);
		}
		
		frame.setContentPane(panel);
		frame.setSize(1280, 720);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}
		
	public void log(int i, String str) {
		texts[i].append(str+"\n");
	}
}
