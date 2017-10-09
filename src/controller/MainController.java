package controller;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;

import view.Game;

public class MainController {

	JFrame frame;
	Game game;
	
	/**
	 * Default constructor
	 */
	public MainController(JFrame frame) {
		// Store our jframe
		this.frame = frame;
		this.game = new Game(this);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(this.game);
		frame.getContentPane().doLayout();
		frame.revalidate();
		frame.repaint();
		this.game.startup();
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