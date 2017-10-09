package controller;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;

public class MainController {

	JFrame frame;
	int currentPanel;
	
	/**
	 * Default constructor
	 */
	public MainController(JFrame frame) {
		// Store our jframe
		this.frame = frame;
		
        frame.setLocationRelativeTo(null);
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public int getHeight() {
		return frame.getHeight();
	}
	
	public int getWidth() {
		return frame.getWidth();
	}
	
	public static void execDelay(int ms) {
		try {
			Thread.sleep(ms);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}