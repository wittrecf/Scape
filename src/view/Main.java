package view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import controller.MainController;

/**
 * This is the main class that has the main function in it
 * It will create the master controller that allows for switching between games
 * This should be the only entry point into the program.
 */
public class Main {
	
	// Master objects
	private static JFrame frame;
	private static MainController mainController;
	private static int screenWidth = 1860;
    private static int screenHeight = 900;
	
	public static void main(String[] args) {
		
		// Comment this out if you don't want full screen logic
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//screenWidth = screenSize.width;
		//screenHeight = screenSize.height;
		
		// Create our frame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(screenWidth, screenHeight);
        frame.setResizable(true);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        
		// Create controller, and go to chooser menu
        mainController = new MainController(frame);
        
        // Set visible
        //title.setVisible(true);
        frame.setVisible(true);
        
       
	}
}