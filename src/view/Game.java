package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
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
import model.MiningRock;
import model.Player;

public class Game extends JPanel implements ActionListener, MouseListener {
	
	private boolean gameOver;
	private Board board;
	private Timer timer;
	private BoardState state;
	private Player player;
	private MainController mainController;
	
	private int fontSize;
	private int centerX;
	private int centerY;
	
	private JButton btnExit = new JButton();
	private static GridBagLayout layout = new GridBagLayout();
    private static GridBagConstraints c = new GridBagConstraints();
	
	private String arrayFile = "resources/mapArray.txt";
	
	public Game(MainController mainController) {
		this.mainController = mainController;
		
    	board = new Board(mainController.getWidth(), mainController.getHeight());
    	
    	state = new BoardState();
    	state.board = board;
    	
    	try {
			player = Player.loadPlayer("SourceLink");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
    	if (player == null) {
    		player = Player.makeAccount("SourceLink");
    	}
    	if (player == null) {
    		this.shutdown();
    	} else {
    		player.setState(state);
    	}
    	state.players = new ArrayList<Player>();
    	state.players.add(player);
    	
    	try {
			readMapArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
		scaleScreenItems();
		
		btnExit.setPreferredSize(new Dimension(ImageEnum.TILEGRASS.getWidth()*2, ImageEnum.TILEGRASS.getHeight()));
		btnExit.setBackground(Color.WHITE);
		btnExit.setForeground(Color.BLACK);
		btnExit.setFont(new Font("TimesRoman", Font.BOLD, 20));
		btnExit.setHorizontalTextPosition(JButton.CENTER);
		btnExit.setVerticalTextPosition(JButton.CENTER);
		btnExit.setText("Logout");
		
		setLayout(layout);
        c.weightx = 0.1;
        c.weighty = 0.1;
    	c.anchor = GridBagConstraints.NORTHEAST;
    	add(btnExit, c);
    	c.anchor = GridBagConstraints.CENTER;
		
		btnExit.addActionListener(this);
		
		centerX = ((board.getWidth() - ImageEnum.TILEGRASS.getWidth()) / 2) / ImageEnum.TILEGRASS.getWidth();
    	centerY = ((board.getHeight() - ImageEnum.TILEGRASS.getHeight()) / 2) / ImageEnum.TILEGRASS.getHeight();
    	System.out.println("CENTER: " + centerX + ", " + centerY);
		
		this.addMouseListener(this);
	}
	
	public void startup() {
		
		gameOver = false;
		
		createBoard();
    	
		timer = new Timer();
		timer.scheduleAtFixedRate(new GameTimer(this, board, state), 0, 10);
	}

	public void shutdown() {
		try {
			player.writePlayerData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(timer != null) timer.cancel();
		mainController.getFrame().getContentPane().removeAll();
		WindowEvent wev = new WindowEvent(mainController.getFrame(), WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		setVisible(false);
		mainController.getFrame().dispose();
		System.exit(0);
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
		    		if (block.getType() >= 10 && block.getType() <= 50) {
		    			block.setObj(new MiningRock(x, y, block.getType()));
		    		} else {
		    			block.setObj(null);
		    		}
		    		if (block.getType() == 1) {
		    			g2.drawImage(ImageEnum.TILEGRASS.getImg(), block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), null);
		    		} else if (block.getType() == 3) {
		    			g2.drawImage(ImageEnum.TILESELECTED.getImg(), block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), null);
		    		}
		    		if (block.getObj() != null) {
		    			if (block.getObj().getType().equals("MiningRock")) {
		    				g2.drawImage(((MiningRock) block.getObj()).getRockType().getImg().getImg(), block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), null);
		    			}
		    		}
			    	g2.setColor(Color.WHITE);
		    		g2.setFont(new Font("TimesRoman", Font.PLAIN, (int)(0.35*fontSize)));
		    		g2.drawString(block.getXLoc() / ImageEnum.TILEGRASS.getWidth() + ":" + block.getYLoc() / ImageEnum.TILEGRASS.getHeight(), block.getXLoc() + 15 + player.getXOff(), block.getYLoc() + 15 + player.getYOff());
		    		g2.setColor(Color.YELLOW);
		    		g2.drawString(block.getXCoord() + ":" + block.getYCoord(), block.getXLoc() + 15 + player.getXOff(), block.getYLoc() + 40 + player.getYOff());
					x++;
		    	}
		    	y++;
			}
			
			g2.drawImage(ImageEnum.PLAYER.getImg(), ((state.boardColsNum - 3) / 2) * ImageEnum.TILEGRASS.getWidth() + (int) (.05 * ImageEnum.TILEGRASS.getWidth()), ((state.boardRowsNum - 3) / 2) * ImageEnum.TILEGRASS.getHeight() + (int) (.05 * ImageEnum.TILEGRASS.getHeight()), null);
    	}
	}
	
	public void createBoard() {
		int q = 0;
		BufferedImage img = ImageEnum.TILEGRASS.getImg();
		state.tiles = new ArrayList<ArrayList<BoardTile>>();
		for(int y = 0; y < state.boardRowsNum + 2; y++) {
			state.tiles.add(new ArrayList<BoardTile>());
			for(int x = 0; x < state.boardColsNum + 2; x++){
				state.tiles.get(y).add(new BoardTile((x - 1)*img.getWidth(), (y - 1)*img.getHeight()));
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
	    state.mapTiles = new double[state.mapColsNum][state.mapRowsNum];
    	for (int j = 0; j < state.mapRowsNum; j++) {
    		for (int i = 0; i < state.mapColsNum; i++) {
    			state.mapTiles[i][j] = tempArr.get(x);
    			x++;
    		}
    	}
	  }
	
	/**
	 * Scales ImageEnum files based on the size of the screen the game is being played on.
	 */
	private void scaleScreenItems() {
		int worldWidth = board.getWidth();
		
		int newTileWidth = worldWidth/state.boardColsNum;
		double scaleFactor = (double) newTileWidth/ImageEnum.TILEGRASS.getWidth();
		
		ImageEnum.TILEGRASS.scaleByFactor(scaleFactor);
		ImageEnum.TILESELECTED.scaleByFactor(scaleFactor);
		
		ImageEnum.ROCKNULL.scaleByFactor(scaleFactor);
		ImageEnum.ROCKCLAY.scaleByFactor(scaleFactor);
		
		ImageEnum.PLAYER.scaleByFactor(scaleFactor);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnExit) {
			this.shutdown();
		}
	}
	
	public void mouseClicked(MouseEvent event) {
		System.out.println("click");
		int[] tmp = CollisionDetection.checkCollisionsTile(event.getX(), event.getY(), state.tiles);
		player.moveTo(tmp[0], tmp[1], tmp[2] == 1, tmp[3], tmp[4]);
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}