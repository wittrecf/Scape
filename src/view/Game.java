package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.GameTimer;
import controller.MainController;
import model.Board;
import model.BoardState;
import model.BoardTile;

public class Game extends JPanel implements ActionListener, MouseListener {
	
	private static GridBagLayout layout = new GridBagLayout();
    private static GridBagConstraints c = new GridBagConstraints();
	private boolean gameOver;
	private Board board;
	private Timer timer;
	private BoardState state;
	private int fontSize;

	/**
	 * Creates an instance of MiniTwo
	 */
	public Game(MainController mainController) {
		
    	board = new Board(mainController.getWidth(), mainController.getHeight());
    	
    	state = new BoardState();
    	state.board = board;
    	
		scaleScreenItems();
		
		this.addMouseListener(this);
	}
	
	public void startup() {
		
		gameOver = false;
		
		createShoreline();
    	
		timer = new Timer();
		timer.scheduleAtFixedRate(new GameTimer(this, board), 0, 10);
	}

	public void shutdown() {
		if(timer != null) timer.cancel();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		// Call the super class
		super.paintComponent(g);
		//super.paint(g);
		
		// Handle game over logic
		if (!gameOver) {
			
	    	//Draw the dock, and background
			DrawOps.drawScaledImage(g, Image.BACKGROUND.getImg(), this);
			
			fontSize = 36;
	    	Graphics2D g2 = (Graphics2D) g;
			
			for (ArrayList<BoardTile> ar: state.tiles) {
		    	for (BoardTile block: ar){
		    		g2.drawImage(Image.TILEGRASS.getImg(), block.getXLoc(), block.getYLoc(), null);
			    	g2.setColor(Color.WHITE);
		    		g2.setFont(new Font("TimesRoman", Font.PLAIN, (int)(0.35*fontSize)));
					g2.drawString(block.getXLoc() + ":" + block.getYLoc(), block.getXLoc() + 10, block.getYLoc() + 35);
					System.out.println("drawing string at " + block.getXLoc() + ":" + block.getYLoc());
		    	}
			}
    	}
	}
	
	public void createShoreline() {
		int q = 0;
		BufferedImage img = Image.TILEGRASS.getImg();
		state.tiles = new ArrayList<ArrayList<BoardTile>>();
		for(int x=0; x<=state.shoreRowsNum; x++) {
			state.tiles.add(new ArrayList<BoardTile>());
			for(int y=1;y<state.shoreColsNum;y++){
				if ((x*img.getWidth() < state.board.getWidth()) && ((state.board.getHeight()-(y)*img.getHeight()) >= 0) && ((state.board.getHeight()-(y)*img.getHeight()) <= state.board.getHeight() - img.getHeight())) {
					state.tiles.get(x).add(new BoardTile(x*img.getWidth(),(state.board.getHeight()-(y)*img.getHeight())));
					System.out.println("added new boardtile at : " + x*img.getWidth() + ", " + (state.board.getHeight()-(y)*img.getHeight()));
					q++;
				}
			}
		}
		System.out.println(q);
	}
	
	/**
	 * Scales image files based on the size of the screen the game is being played on.
	 */
	private void scaleScreenItems() {
		int worldWidth = board.getWidth();
		
		int newTileWidth = worldWidth/state.shoreColsNum;
		double scaleFactor = (double) newTileWidth/Image.TILEGRASS.getWidth();
		
		Image.TILEGRASS.scaleByFactor(scaleFactor);
		System.out.println("TILE:" + Image.TILEGRASS.getWidth());
	}
	
	/**
	 * Determines what button the player clicked, and adjusts the game accordingly.
	 */
	public void actionPerformed(ActionEvent event) {
		/*if(event.getSource() == btnExit) {
			mainController.exitMini();
		}
		else if(event.getSource() == btnRock) {
			if (resourceM.btnEvent("Rock")) {
				state.placementMode = 1;
			}
		} else if(event.getSource() == btnOyster) {
			if (resourceM.btnEvent("Oyster")) {
				state.placementMode = 2;
			}
		} else if(event.getSource() == btnGrass) {
			if (resourceM.btnEvent("Grass")) {
				state.placementMode = 3;
			}
		}*/
	}
	
	/**
	 * Depending on what has been clicked, assignes the crabs to either pick up a Resource, or go place an Obstacle.
	 */
	public void mouseClicked(MouseEvent event) {
		/*switch (state.placementMode) {
		case 0: crabM.assignCrabResource(resourceM.pickupEvent(event.getX(), event.getY())); break;
		case 1: crabM.assignCrabObstacle(resourceM.placeEvent(state.placementMode, event.getX(), event.getY())); break;
		case 2: crabM.assignCrabObstacle(resourceM.placeEvent(state.placementMode, event.getX(), event.getY())); break;
		case 3: crabM.assignCrabObstacle(resourceM.placeEvent(state.placementMode, event.getX(), event.getY())); break;
		}*/
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}