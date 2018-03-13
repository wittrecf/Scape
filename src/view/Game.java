package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Menu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Timer;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import controller.CollisionDetection;
import controller.GameTimer;
import controller.MainController;
import model.Board;
import model.BoardState;
import model.BoardTile;
import model.InventoryTile;
import model.Item;
import model.KeyType;
import model.TileRock;
import model.TileTree;
import model.Player;

public class Game extends JPanel implements ActionListener {
	
	private boolean gameOver;
	private Board board;
	private Timer timer;
	private BoardState state;
	private Player player;
	private MainController mainController;
	
	private int fontSize;
	private int centerX;
	private int centerY;
	
	private int inventoryWidth;
	private int inventoryHeight;
	
	public int[] click;
	public int[] mouseLoc;
	public String hoverText = "";
	
	public JLabel label;
	
	private JButton btnExit = new JButton();
	private static GridBagLayout layout = new GridBagLayout();
    private static GridBagConstraints c = new GridBagConstraints();
	
	private String mapArrayFile = "resources/mapArray.txt";
	private String objArrayFile = "resources/objArray.txt";
	
	public Game(MainController mainController) {
		this.mainController = mainController;
		
    	board = new Board(mainController.getWidth(), mainController.getHeight());
    	
    	state = new BoardState();
    	state.board = board;
    	
    	scaleScreenItems();
    	
    	ImageEnum.groupIcons();
		
		inventoryWidth = ImageEnum.INVENTORY.getWidth();
		inventoryHeight = ImageEnum.INVENTORY.getHeight();
    	
    	try {
			player = Player.loadPlayer("SourceLink", inventoryWidth, inventoryHeight);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
    	if (player == null) {
    		player = Player.makeAccount("SourceLink", inventoryWidth, inventoryHeight);
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
			readObjArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	label = new JLabel(hoverText);
    	label.setForeground(new Color(235, 224, 188));
    	label.setOpaque(true);
    	label.setBackground(Color.BLACK);
    	add(label);
		
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
    	
    	mouseLoc = new int[2];
    	
		// Then on your component(s)
		this.addMouseListener(new PopClickListener());
		this.addMouseMotionListener(new PopupMotionListener());
		
		this.getInputMap().put(KeyStroke.getKeyStroke("P"), "doSomething");
		this.getActionMap().put("doSomething", new KeyType("P"));
	}
	
	public void startup() {
		
		gameOver = false;
		
		createBoard();
    	
		timer = new Timer();
		timer.scheduleAtFixedRate(new GameTimer(this, board, state), 0, 10);
	}

	public void shutdown() {
		player.clearPrevPath();
		try {
			player.writePlayerData();
			writeMapArray();
			writeObjArray();
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
		    		if (state.objTiles[x][y] >= 10 && state.objTiles[x][y] < 50) {
		    			block.setObj(new TileRock(x, y, state.objTiles[x][y]));
		    		} else if (state.objTiles[x][y] >= 50 && state.objTiles[x][y] < 90) {
		    			block.setObj(new TileTree(x, y, state.objTiles[x][y]));
		    		} else {
		    			block.setObj(null);
		    		}
		    		if (block.getType() == 1) {
		    			g2.drawImage(ImageEnum.TILEGRASS.getImg(), block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), null);
		    		} else if (block.getType() == 3) {
		    			g2.drawImage(ImageEnum.TILESELECTED.getImg(), block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), null);
		    		}
		    		if (block.getObj() != null) {
		    			if (block.getObj().getType().equals("TileRock")) {
		    				g2.drawImage(((TileRock) block.getObj()).getRockType().getImg(), block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), null);
		    			} else if (block.getObj().getType().equals("TileTree")) {
		    				g2.drawImage(((TileTree) block.getObj()).getTreeType().getImg(), block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), null);
		    			}
		    		}
		    		if (state.itemTiles[x][y] != null) {
		    			for (int i = 0; i < state.itemTiles[x][y].size(); i++) {
		    				g2.drawImage(ImageEnum.scaleToDimensions(ImageEnum.getIcons()[state.itemTiles[x][y].get(i)][0], ImageEnum.TILEGRASS.getWidth(), ImageEnum.TILEGRASS.getHeight()), block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), null);
		    			}
		    		}
			    	g2.setColor(Color.WHITE);
		    		g2.setFont(new Font("TimesRoman", Font.PLAIN, (int)(0.35*fontSize)));
		    		//g2.drawString(block.getXLoc() / ImageEnum.TILEGRASS.getWidth() + ":" + block.getYLoc() / ImageEnum.TILEGRASS.getHeight(), block.getXLoc() + 15 + player.getXOff(), block.getYLoc() + 15 + player.getYOff());
		    		g2.setColor(Color.YELLOW);
		    		//g2.drawString(block.getXCoord() + ":" + block.getYCoord(), block.getXLoc() + 15 + player.getXOff(), block.getYLoc() + 40 + player.getYOff());
					x++;
		    	}
		    	y++;
			}
			
			g2.drawImage(ImageEnum.PLAYER.getImg(), ((state.boardColsNum - 3) / 2) * ImageEnum.TILEGRASS.getWidth() + (int) (.05 * ImageEnum.TILEGRASS.getWidth()), ((state.boardRowsNum - 3) / 2) * ImageEnum.TILEGRASS.getHeight() + (int) (.05 * ImageEnum.TILEGRASS.getHeight()), null);
			g2.drawImage(ImageEnum.INVENTORY.getImg(), board.getWidth() - ImageEnum.INVENTORY.getWidth(), board.getHeight() - ImageEnum.INVENTORY.getHeight(), null);
			
			for (InventoryTile i : player.getInv().getInventory()) {
				g2.drawImage(ImageEnum.ICONBLANK.getImg(),
						(int) (board.getWidth() - ImageEnum.INVENTORY.getWidth() + (.06 * ImageEnum.INVENTORY.getWidth()) + (i.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .04 * ImageEnum.INVENTORY.getWidth()))),
						(int) (board.getHeight() - ImageEnum.INVENTORY.getHeight() + (.04 * ImageEnum.INVENTORY.getHeight()) + (i.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .04 * ImageEnum.INVENTORY.getWidth()))),
						null);
				if (i.getItem() > 0) {
					g2.drawImage(i.getItemImg(),
							(int) (board.getWidth() - ImageEnum.INVENTORY.getWidth() + (.06 * ImageEnum.INVENTORY.getWidth()) + (i.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .04 * ImageEnum.INVENTORY.getWidth()))),
							(int) (board.getHeight() - ImageEnum.INVENTORY.getHeight() + (.04 * ImageEnum.INVENTORY.getHeight()) + (i.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .04 * ImageEnum.INVENTORY.getWidth()))),
							null);
				}
			}
			
			if (click != null) {
				int xFar;
				int yFar;
				if ((mouseLoc[0] + 30 + label.getPreferredSize().width) > mainController.getWidth()) {
					xFar = mainController.getWidth() - (mouseLoc[0] + 30 + label.getPreferredSize().width);
				} else {
					xFar = 0;
				}
				if ((mouseLoc[1] + 30 + label.getPreferredSize().height) > mainController.getHeight()) {
					yFar = mainController.getHeight() - (mouseLoc[1] + 30 + label.getPreferredSize().height);
				} else {
					yFar = 0;
				}
				if (!hoverText.equals("")) {
					label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
					label.setText(hoverText);
					label.setBounds(mouseLoc[0] + 30 + xFar, mouseLoc[1] + 30 + yFar, label.getPreferredSize().width, label.getPreferredSize().height);
				} else {
					label.setBorder(null);
					label.setText(hoverText);
				}
			}
			
			g2.drawString("Mining: " + player.getState(0), 0, 10);
			g2.drawString("Woodcutting: " + player.getState(1), 0, 30);
		}
	}
	
	public void createBoard() {
		int q = 0;
		BufferedImage img = ImageEnum.TILEGRASS.getImg();
		state.tiles = new ArrayList<ArrayList<BoardTile>>();
		for(int y = 0; y < state.boardRowsNum + 2; y++) {
			state.tiles.add(new ArrayList<BoardTile>());
			for(int x = 0; x < state.boardColsNum + 2; x++){
				if (!(x > state.boardColsNum - 4 + 2) || !(y > state.boardRowsNum - 6 + 2)) {
					state.tiles.get(y).add(new BoardTile((x - 1)*img.getWidth(), (y - 1)*img.getHeight()));
					q++;
				}
			}
		}
		System.out.println(q);
	}
	
	private void readMapArray() throws FileNotFoundException {
		Scanner inFile = new Scanner(new File(mapArrayFile));
		
	    ArrayList<Double> tempArr = new ArrayList<Double>();
	    
	    int x = 0;
	    while (inFile.hasNext()) {
	    	tempArr.add(inFile.nextDouble());
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
	
	private void readObjArray() throws FileNotFoundException {
		Scanner inFile = new Scanner(new File(objArrayFile));
		
	    ArrayList<Double> tempArr = new ArrayList<Double>();
	    
	    int x = 0;
	    while (inFile.hasNext()) {
	    	tempArr.add(inFile.nextDouble());
	    	x++;
	    }
	    
	    inFile.close();
	    
	    x = 0;
	    state.objTiles = new double[state.mapColsNum][state.mapRowsNum];
    	for (int j = 0; j < state.mapRowsNum; j++) {
    		for (int i = 0; i < state.mapColsNum; i++) {
    			state.objTiles[i][j] = tempArr.get(x);
    			x++;
    		}
    	}
	  }
	
	private void writeMapArray() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(mapArrayFile, "UTF-8");
		
		for (int j = 0; j < state.mapRowsNum; j++) {
    		for (int i = 0; i < state.mapColsNum; i++) {
    			writer.print(state.mapTiles[i][j] + " ");
    		}
    		writer.println();
    	}
		writer.close();
	}
	
	private void writeObjArray() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(objArrayFile, "UTF-8");
		
		for (int j = 0; j < state.mapRowsNum; j++) {
    		for (int i = 0; i < state.mapColsNum; i++) {
    			writer.print(state.objTiles[i][j] + " ");
    		}
    		writer.println();
    	}
		writer.close();
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
		
		ImageEnum.TREENULL.scaleByFactor(scaleFactor);
		ImageEnum.TREEOAK.scaleByFactor(scaleFactor);
		
		ImageEnum.PLAYER.scaleByFactor(scaleFactor);
		
		ImageEnum.INVENTORY.scaleByFactor(scaleFactor);
		
		ImageEnum.ICONBLANK.scaleByFactor(scaleFactor);
		ImageEnum.ICONCLAYORE.scaleByFactor(scaleFactor);
		ImageEnum.ICONCLAYOREHIGHLIGHT.scaleByFactor(scaleFactor);
		ImageEnum.ICONOAKLOGS.scaleByFactor(scaleFactor);
		ImageEnum.ICONOAKLOGSHIGHLIGHT.scaleByFactor(scaleFactor);
		
	}
	
	public ArrayList<String> getMenuItems() {
		ArrayList<String> s = new ArrayList<String>();
		if (click[5] == 0) {
			if ((state.objTiles[click[0]][click[1]] >= 10) && state.objTiles[click[0]][click[1]] < 50) {
				s.add("<html>Mine <font color=\"#ffff00\"> " + TileRock.pickRock((int) Math.floor(state.objTiles[click[0]][click[1]])).getRockName() + " </font></html>");
			} else if ((state.objTiles[click[0]][click[1]] >= 50) && state.objTiles[click[0]][click[1]] < 90) {
				s.add("<html>Cut <font color=\"#ffff00\"> " + TileTree.pickTree((int) Math.floor(state.objTiles[click[0]][click[1]])).getTreeName() + " </font></html>");
			}
			if (state.itemTiles[click[0]][click[1]] != null) {
				for (int i = state.itemTiles[click[0]][click[1]].size() - 1; i >= 0; i--) {
					s.add("<html>Take <font color=\"#f8d56b\"> " + Item.getItemById(state.itemTiles[click[0]][click[1]].get(i)).getItemName() + " </font></html>");
				}
			}
			s.add("Walk here");
		} else if (click[5] == 1) {
			if ((click[0] >= 0) && (player.getInv().getSlot(click[0]) != 0)) {
				s.add("<html>Use <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getInv().getSlot(click[0])).getItemName() + " </font></html>");
				s.add("<html>Drop <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getInv().getSlot(click[0])).getItemName() + " </font></html>");
			}
		}
		
		s.add("Cancel");
		return s;
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == btnExit) {
			this.shutdown();
		}
	}
	
	public void movePlayer(int[] tmp, boolean interact) {
		player.moveTo(tmp[0], tmp[1], tmp[2] == 1, tmp[3], tmp[4], interact);
	}

	public void mouseReleased(MouseEvent event) {}
	
	class PopUpDemo extends JPopupMenu {
		ActionListener actionListener = new PopupActionListener();
	    JMenuItem cmp[];
	    public PopUpDemo(ArrayList<String> s){
	    	cmp = new JMenuItem[s.size()];
	    	this.setBorder(BorderFactory.createTitledBorder("Choose Option"));
	    	this.setBackground(Color.GRAY);
	    	for (int i = 0; i < s.size(); i++) {
	    		cmp[i] = new JMenuItem(s.get(i));
	    		cmp[i].setName(s.get(i));
	    		cmp[i].setForeground(new Color(235, 224, 188));
	    		cmp[i].setBackground(Color.BLACK);
	    		cmp[i].addActionListener(actionListener);
	    		add(cmp[i]);
	    	}
	    	this.setLayout(new GridLayout(0, 1));
	    	if (this.getPreferredSize().width < 100) {
	    		this.setPreferredSize(new Dimension(100, this.getPreferredSize().height));
	    	}
	    }
	}
	
	class PopupActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String tmp = event.getActionCommand();
			String str[] = tmp.split(" ");
			if (str.length > 0) {
				if (str[0].equals("Cancel")) {
					return;
				} else if ((str[0] + " " + str[1]).equals("Walk here")) {
					movePlayer(click, false);
				}  else if (str[0].equals("<html>Mine") || str[0].equals("<html>Cut")) {
					movePlayer(click, true);
				} else if (str[0].equals("<html>Take")) {
					click[2] = 1;
					String t = "";
    				for (int i = 3; i < str.length - 1; i++) {
    					System.out.println("adding " + str[i]);
    					t += str[i];
    					if (i != str.length - 2) {
    						t += " ";
    					}
    				}
    				player.pickUp(Item.getItemByName(t).getItemId(), click);
				} else if (str[0].equals("<html>Use")) {
					player.getInv().highlightSlot(click[0]);
				} else if (str[0].equals("<html>Drop")) {
					player.dropSlot(click[0]);
				}
			}
		}
	}

	class PopClickListener extends MouseAdapter {
		PopUpDemo menu = new PopUpDemo(new ArrayList<String>());
		
		public void reactClick(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	        	hoverText = "";
	            doPop(e, true, getMenuItems());
	        } else if (e.getButton() == 1) {
	        	String tmp = doPop(e, false, getMenuItems());
	        	String str[] = tmp.split(" ");
    			if ((str.length > 1) && (str[0] + " " + str[1]).equals("Walk here")) {
    				player.moveTo(click[0], click[1], click[2] == 1, click[3], click[4], false);
    			} else if (str[0].equals("<html>Mine") || str[0].equals("<html>Cut")) {
    				player.moveTo(click[0], click[1], click[2] == 1, click[3], click[4], true);
    			} else if (str[0].equals("<html>Take")) {
    				System.out.println(click[0] + ",, " + click[1]);
    				click[2] = 1;
    				String t = "";
    				for (int i = 3; i < str.length - 1; i++) {
    					t += str[i];
    					if (i != str.length - 2) {
    						t += " ";
    					}
    				}
    				player.pickUp(Item.getItemByName(t).getItemId(), click);
    			} else if (str[0].equals("<html>Use")) {
    				player.getInv().highlightSlot(click[0]);
    			} else if (str[0].equals("<html>Drop")) {
					player.dropSlot(click[0]);
    			}
	        }
		}
		
	    public void mousePressed(MouseEvent e){
	    	if (e.isPopupTrigger()) {
	    		click = CollisionDetection.checkCollisionsTile(e.getButton(), e.getX(), e.getY(), state, board, player);
	            doPop(e, true, getMenuItems());
	    	}
	    }

	    public void mouseReleased(MouseEvent e){
	    	click = CollisionDetection.checkCollisionsTile(e.getButton(), e.getX(), e.getY(), state, board, player);
	    	reactClick(e);
	    }
	    
	    public void mouseEntered(MouseEvent e) {
	    	menu.setVisible(false);
	    }

	    private String doPop(MouseEvent e, boolean doDisplay, ArrayList<String> str){
	        menu = new PopUpDemo(str);
	        if (doDisplay) {
	        	menu.show(e.getComponent(), (int) (e.getX() - menu.getPreferredSize().getWidth()/2), e.getY());
	        	menu.setVisible(true);
	        	return null;
	        } else {
	        	System.out.println(menu.getComponent(0).getName());
	        	return menu.getComponent(0).getName();
	        }
	    }
	}
	
	
	class PopupMotionListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	
		public void mouseMoved(MouseEvent e) {
			mouseLoc[0] = e.getX();
			mouseLoc[1] = e.getY();
			click = CollisionDetection.checkCollisionsTile(e.getButton(), e.getX(), e.getY(), state, board, player);
			//if (click[5] == 0) {
		    	String tmp = getMenuItems().get(0);
		    	String str[] = tmp.split(" ");
				if ((str.length > 1) && !(str[0] + " " + str[1]).equals("Walk here")) {
					hoverText = tmp;
				} else {
					hoverText = "";
				}
			//}
		}
	}
}
