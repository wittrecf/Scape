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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Timer;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.CollisionDetection;
import controller.GameTimer;
import controller.MainController;
import model.Board;
import model.BoardState;
import model.BoardTile;
import model.Player;

public class Game extends JPanel implements ActionListener, MouseListener {
	
	private static GridBagLayout layout = new GridBagLayout();
    private static GridBagConstraints c = new GridBagConstraints();
	private boolean gameOver;
	private Board board;
	private Timer timer;
	private BoardState state;
	private int fontSize;
	private int centerX;
	private int centerY;
	//private int leftX;
	//private int topY;
	private Player player;
	private String arrayFile = "resources/mapArray.txt";
	
	public Game(MainController mainController) {
		
    	board = new Board(mainController.getWidth(), mainController.getHeight());
    	
    	state = new BoardState();
    	state.board = board;
    	
    	player = new Player("source link");
    	
    	try {
			readMapArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
		scaleScreenItems();
		
		centerX = ((board.getWidth() - Image.TILEGRASS.getWidth()) / 2) / Image.TILEGRASS.getWidth();
    	centerY = ((board.getHeight() - Image.TILEGRASS.getHeight()) / 2) / Image.TILEGRASS.getHeight();
    	System.out.println("CENTER: " + centerX + ", " + centerY);
		
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
			
			fontSize = 36;
	    	Graphics2D g2 = (Graphics2D) g;
			
	    	int x = player.getXLoc() - ((state.boardColsNum - 1) / 2);
	    	int y = player.getYLoc() - ((state.boardRowsNum - 1) / 2);
			for (ArrayList<BoardTile> ar: state.tiles) {
				x = player.getXLoc() - ((state.boardColsNum - 1) / 2);
		    	for (BoardTile block: ar){
		    		block.setXCoord(x);
		    		block.setYCoord(y);
		    		block.setType(state.mapTiles[x][y]);
		    		if (block.getType() == 1) {
		    			g2.drawImage(Image.TILEGRASS.getImg(), block.getXLoc(), block.getYLoc(), null);
		    		} else if (block.getType() == 3) {
		    			g2.drawImage(Image.TILESELECTED.getImg(), block.getXLoc(), block.getYLoc(), null);
		    		}
		    		if ((block.getXLoc() / Image.TILEGRASS.getWidth() == centerX) && (block.getYLoc() / Image.TILEGRASS.getHeight() == centerY)) {
		    			g2.drawImage(Image.PLAYER.getImg(), block.getXLoc() + (int) (.05 * Image.TILEGRASS.getWidth()), block.getYLoc() + (int) (.05 * Image.TILEGRASS.getHeight()), null);
		    		}
			    	g2.setColor(Color.WHITE);
		    		g2.setFont(new Font("TimesRoman", Font.PLAIN, (int)(0.35*fontSize)));
		    		g2.drawString(block.getXLoc() / Image.TILEGRASS.getWidth() + ":" + block.getYLoc() / Image.TILEGRASS.getHeight(), block.getXLoc() + 15, block.getYLoc() + 15);
		    		g2.setColor(Color.YELLOW);
		    		g2.drawString(block.getXCoord() + ":" + block.getYCoord(), block.getXLoc() + 15, block.getYLoc() + 40);
					x++;
		    	}
		    	y++;
			}
    	}
	}
	
	public void createShoreline() {
		int q = 0;
		BufferedImage img = Image.TILEGRASS.getImg();
		state.tiles = new ArrayList<ArrayList<BoardTile>>();
		for(int y = 0; y < state.boardRowsNum; y++) {
			state.tiles.add(new ArrayList<BoardTile>());
			for(int x = 0; x < state.boardColsNum; x++){
				state.tiles.get(y).add(new BoardTile(x*img.getWidth(), y*img.getHeight()));
				q++;
			}
		}
		System.out.println(q);
	}
	
	private void readMapArray() throws FileNotFoundException {
		Scanner inFile = new Scanner(new File(arrayFile));
		
	    ArrayList<Integer> tempArr = new ArrayList<Integer>();
	    
	    int x = 0;
	    while (inFile.hasNext()) {
	    	tempArr.add(inFile.nextInt());
	    	x++;
	    }
	    
	    inFile.close();
	    
	    x = 0;
	    state.mapTiles = new int[state.mapColsNum][state.mapRowsNum];
    	for (int j = 0; j < state.mapRowsNum; j++) {
    		for (int i = 0; i < state.mapColsNum; i++) {
    			state.mapTiles[i][j] = tempArr.get(x);
    			if ((i == 46) && (j == 50)) {
    				state.mapTiles[i][j] = 0;
    			}
    			if ((i == 46) && (j == 51)) {
    				state.mapTiles[i][j] = 0;
    			}
    			if ((i == 46) && (j == 49)) {
    				state.mapTiles[i][j] = 0;
    			}
    			x++;
    		}
    	}
	  }
	
	/**
	 * Scales image files based on the size of the screen the game is being played on.
	 */
	private void scaleScreenItems() {
		int worldWidth = board.getWidth();
		
		int newTileWidth = worldWidth/state.boardColsNum;
		double scaleFactor = (double) newTileWidth/Image.TILEGRASS.getWidth();
		
		Image.TILEGRASS.scaleByFactor(scaleFactor);
		Image.TILESELECTED.scaleByFactor(scaleFactor);
		
		Image.PLAYER.scaleByFactor(scaleFactor);
	}
	
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
	
	public void mouseClicked(MouseEvent event) {
		System.out.println("click");
		int[] tmp = CollisionDetection.checkCollisionsTile(event.getX(), event.getY(), state.tiles);
		player.moveTo(tmp[0], tmp[1], state);
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}