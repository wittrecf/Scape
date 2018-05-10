package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.MaskFormatter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import controller.CollisionDetection;
import controller.GameTimer;
import controller.MainController;
import model.BankTile;
import model.Board;
import model.BoardState;
import model.BoardTile;
import model.Enemy;
import model.Inventory;
import model.InventoryTile;
import model.Item;
import model.NPC;
import model.TileRock;
import model.TileTree;
import model.Player;
import model.TileFishingSpot;

public class Game extends JPanel implements ActionListener, KeyListener {
	
	private boolean gameOver;
	private Board board;
	private Timer timer;
	public static BoardState state;
	public Player player;
	public static MainController mainController;
	
	private int fontSize;
	private int centerX;
	private int centerY;
	
	private int inventoryWidth;
	private int inventoryHeight;
	
	public int[] click;
	public int[] mouseLoc;
	public String hoverText = "";
	public String spokenText = "";
	public double spokenTime = 0;
	public int makeNum = 0;
	public String makeXString = "";
	public String makeXType = "";
	
	public int dx;
	public int dy;
	
	public static boolean bankOpen = false;
	public boolean makeXOpen = false;
	
	public JLabel label;
	
	private JButton btnExit = new JButton();
	private JButton btnNote = new JButton();
	
	private String mapArrayFile = "resources/mapArray.txt";
	private String objArrayFile = "resources/objArray.txt";
	
	protected JTextField textField;
    protected JTextPane textPane;
    protected JScrollPane scrollPane;
    private final static String newline = "\n";
	
	public Game(MainController mainController) {
		this.mainController = mainController;
		this.setFocusable(true);
		
    	board = new Board(mainController.getWidth(), mainController.getHeight());
    	
    	state = new BoardState();
    	state.board = board;
    	
    	//state.npcTiles[35][51] = new ArrayList<NPC>();
    	//state.npcTiles[35][51].add(new NPC("Dave", ImageEnum.PLAYER.getImg(), 35, 51));
    	//state.npcTiles[35][51].get(0).setTalkable(true);
    	
    	state.npcTiles[35][52] = new ArrayList<NPC>();
    	state.npcTiles[35][52].add(new Enemy("Goblin General", ImageEnum.PLAYER.getImg(), 35, 52, 10, 1));
    	state.npcTiles[35][52].get(0).setTalkable(false);
    	((Enemy) state.npcTiles[35][52].get(0)).addDrop(new int[] {10, 10}, new int[] {1, 0}, new int[] {3, 2});
    	((Enemy) state.npcTiles[35][52].get(0)).addDrop(new int[] {50}, new int[] {0}, new int[] {2});
    	((Enemy) state.npcTiles[35][52].get(0)).addDrop(new int[] {90}, new int[] {1}, new int[] {1});
    	
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
		
		btnExit.setBackground(Color.WHITE);
		btnExit.setForeground(Color.BLACK);
		btnExit.setFont(new Font("TimesRoman", Font.BOLD, 20));
		btnExit.setHorizontalTextPosition(JButton.CENTER);
		btnExit.setVerticalTextPosition(JButton.CENTER);
		btnExit.setText("Logout");
		btnExit.setBounds(mainController.getWidth() - btnExit.getPreferredSize().width, 0, btnExit.getPreferredSize().width, btnExit.getPreferredSize().height);
		btnExit.addActionListener(this);
		
		btnNote.setBackground(Color.WHITE);
		btnNote.setForeground(Color.BLACK);
		btnNote.setFont(new Font("TimesRoman", Font.BOLD, 20));
		btnNote.setHorizontalTextPosition(JButton.CENTER);
		btnNote.setVerticalTextPosition(JButton.CENTER);
		btnNote.setText("Note");
		btnNote.setBounds((int) ((mainController.getWidth() / 2) - (btnNote.getPreferredSize().getWidth() / 2)), 50 + ImageEnum.BANK.getHeight(), btnNote.getPreferredSize().width, btnNote.getPreferredSize().height);
		btnNote.addActionListener(this);
		
		textField = new JTextField(50);
		textField.setBounds(0, mainController.getHeight() - textField.getPreferredSize().height, textField.getPreferredSize().width, textField.getPreferredSize().height);
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setOpaque(true);
		textField.setSelectionEnd(10);
		textField.setText("Press enter to chat");
		textField.addActionListener(this);
 
        textPane = player.getChatbox();
        textPane.setPreferredSize(new Dimension(textField.getPreferredSize().width, 150));
        textPane.setMargin(new Insets(5, 5, 5, 5));
        textPane.setFocusable(false);
        textPane.setBackground(Color.LIGHT_GRAY);
        textPane.setOpaque(true);
        scrollPane = new JScrollPane(textPane);
        scrollPane.setBounds(0, mainController.getHeight() - textField.getPreferredSize().height - textPane.getPreferredSize().height, textPane.getPreferredSize().width, textPane.getPreferredSize().height);
        scrollPane.setPreferredSize(new Dimension(textField.getPreferredSize().width, 150));

        label = new JLabel(hoverText);
    	label.setForeground(new Color(235, 224, 188));
    	label.setBackground(Color.BLACK);
        
		this.setLayout(null);
		
    	add(btnExit);
    	add(btnNote);
    	add(textField);
    	add(scrollPane);
    	add(label);

        //JOptionPane.showMessageDialog(null, this);
		
		centerX = ((board.getWidth() - ImageEnum.TILEGRASS.getWidth()) / 2) / ImageEnum.TILEGRASS.getWidth();
    	centerY = ((board.getHeight() - ImageEnum.TILEGRASS.getHeight()) / 2) / ImageEnum.TILEGRASS.getHeight();
    	System.out.println("CENTER: " + centerX + ", " + centerY);
    	
    	mouseLoc = new int[2];
    	
		// Then on your component(s)
		this.addMouseListener(new PopClickListener());
		this.addMouseMotionListener(new PopupMotionListener());
		this.addKeyListener(this);
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
		    		} else if (state.objTiles[x][y] >= 90 && state.objTiles[x][y] < 130) {
		    			block.setObj(new TileFishingSpot(x, y, state.objTiles[x][y]));
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
		    			} else if (block.getObj().getType().equals("TileFishingSpot")) {
		    				g2.drawImage(((TileFishingSpot) block.getObj()).getFishingSpotType().getImg(), block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), null);
		    			}
		    		}
		    		if (state.itemTiles[x][y] != null) {
		    			for (int i = 0; i < state.itemTiles[x][y].size(); i++) {
		    				g2.drawImage(ImageEnum.scaleToDimensions(ImageEnum.getIcons()[state.itemTiles[x][y].get(i)[0]][0], ImageEnum.TILEGRASS.getWidth(), ImageEnum.TILEGRASS.getHeight()), block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), null);
		    			}
		    		}
		    		if (state.npcTiles[x][y] != null) {
		    			for (int i = 0; i < state.npcTiles[x][y].size(); i++) {
		    				if (state.npcTiles[x][y].get(i) != null) {
			    				g2.drawImage(ImageEnum.scaleToDimensions(state.npcTiles[x][y].get(i).getImg(), ImageEnum.TILEGRASS.getWidth(), ImageEnum.TILEGRASS.getHeight()), block.getXLoc() + player.getXOff() + state.npcTiles[x][y].get(i).getXOff(), block.getYLoc() + player.getYOff() + state.npcTiles[x][y].get(i).getYOff(), null);
			    				if ((state.npcTiles[x][y].get(i) instanceof Enemy) && ((Enemy) (state.npcTiles[x][y].get(i))).getInCombatWith() != null) {
			    					g2.setColor(Color.RED);
			    					g2.fillRect(block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), BoardTile.getWidth(), 10);
			    					g2.setColor(Color.GREEN);
			    					g2.fillRect(block.getXLoc() + player.getXOff(), block.getYLoc() + player.getYOff(), (int) ((double) ((Enemy) (state.npcTiles[x][y].get(i))).getCurrHealth() /  (double) ((Enemy) (state.npcTiles[x][y].get(i))).getMaxHealth() * (BoardTile.getWidth())), 10);
			    				}
			    			} else {
		    					state.npcTiles[x][y].remove(state.npcTiles[x][y].size() - 1);
    							if (state.npcTiles[x][y].size() == 0) {
    								state.npcTiles[x][y] = null;
    							}
    							return;
		    				}
		    			}
		    		}
			    	g2.setColor(Color.WHITE);
		    		g2.setFont(new Font("TimesRoman", Font.PLAIN, (int)(0.5*fontSize)));
		    		//g2.drawString(block.getXLoc() / ImageEnum.TILEGRASS.getWidth() + ":" + block.getYLoc() / ImageEnum.TILEGRASS.getHeight(), block.getXLoc() + 15 + player.getXOff(), block.getYLoc() + 15 + player.getYOff());
		    		g2.setColor(Color.YELLOW);
		    		//g2.drawString(block.getXCoord() + ":" + block.getYCoord(), block.getXLoc() + 15 + player.getXOff(), block.getYLoc() + 40 + player.getYOff());
					x++;
		    	}
		    	y++;
			}
			
			g2.drawImage(ImageEnum.PLAYER.getImg(), ((state.boardColsNum - 3) / 2) * ImageEnum.TILEGRASS.getWidth() + (int) (.05 * ImageEnum.TILEGRASS.getWidth()), ((state.boardRowsNum - 3) / 2) * ImageEnum.TILEGRASS.getHeight() + (int) (.05 * ImageEnum.TILEGRASS.getHeight()), null);
			g2.setColor(Color.WHITE);
    		g2.setFont(new Font("TimesRoman", Font.PLAIN, (int)(0.5*fontSize)));
    		g2.drawString(player.getXLoc() + ":" + player.getYLoc(), player.getXLoc() + 15 + player.getXOff(), player.getYLoc() + 15 + player.getYOff());
			g2.drawImage(ImageEnum.INVENTORY.getImg(), board.getWidth() - ImageEnum.INVENTORY.getWidth(), board.getHeight() - ImageEnum.INVENTORY.getHeight(), null);
			
			int num = 0;
			for (InventoryTile i : player.getInv().getInventory()) {
				g2.drawImage(ImageEnum.ICONBLANK.getImg(),
						(int) (board.getWidth() - ImageEnum.INVENTORY.getWidth() + (.06 * ImageEnum.INVENTORY.getWidth()) + (i.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .04 * ImageEnum.INVENTORY.getWidth()))),
						(int) (board.getHeight() - ImageEnum.INVENTORY.getHeight() + (.04 * ImageEnum.INVENTORY.getHeight()) + (i.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .04 * ImageEnum.INVENTORY.getWidth()))),
						null);
				if ((click != null) && (click[0] == num)) {
					if (i.getItem() > 0) {
						g2.drawImage(i.getItemImg(),
								(int) (board.getWidth() - ImageEnum.INVENTORY.getWidth() + (.06 * ImageEnum.INVENTORY.getWidth()) + (i.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .04 * ImageEnum.INVENTORY.getWidth()))) + dx,
								(int) (board.getHeight() - ImageEnum.INVENTORY.getHeight() + (.04 * ImageEnum.INVENTORY.getHeight()) + (i.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .04 * ImageEnum.INVENTORY.getWidth()))) + dy,
								null);
						if (i.getIsNoted()) {
							g2.drawString(Integer.toString(i.getCount()),
									(int) (board.getWidth() - ImageEnum.INVENTORY.getWidth() + (.06 * ImageEnum.INVENTORY.getWidth()) + (i.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .04 * ImageEnum.INVENTORY.getWidth()))) + dx,
									10 + (int) (board.getHeight() - ImageEnum.INVENTORY.getHeight() + (.04 * ImageEnum.INVENTORY.getHeight()) + (i.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .04 * ImageEnum.INVENTORY.getWidth()))) + dy);
						}
					}
				} else {
					if (i.getItem() > 0) {
						g2.drawImage(i.getItemImg(),
								(int) (board.getWidth() - ImageEnum.INVENTORY.getWidth() + (.06 * ImageEnum.INVENTORY.getWidth()) + (i.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .04 * ImageEnum.INVENTORY.getWidth()))),
								(int) (board.getHeight() - ImageEnum.INVENTORY.getHeight() + (.04 * ImageEnum.INVENTORY.getHeight()) + (i.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .04 * ImageEnum.INVENTORY.getWidth()))),
								null);
						if (i.getIsNoted()) {
							g2.drawString(Integer.toString(i.getCount()),
									(int) (board.getWidth() - ImageEnum.INVENTORY.getWidth() + (.06 * ImageEnum.INVENTORY.getWidth()) + (i.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .04 * ImageEnum.INVENTORY.getWidth()))),
									10 + (int) (board.getHeight() - ImageEnum.INVENTORY.getHeight() + (.04 * ImageEnum.INVENTORY.getHeight()) + (i.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .04 * ImageEnum.INVENTORY.getWidth()))));
						}
					}
				}
				num++;
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
					label.setOpaque(true);
					label.setText(hoverText);
					label.setBounds(mouseLoc[0] + 30 + xFar, mouseLoc[1] + 30 + yFar, label.getPreferredSize().width, label.getPreferredSize().height);
				} else {
					label.setBorder(null);
					label.setOpaque(false);
					label.setText(hoverText);
				}
			}
			
			g2.setColor(Color.RED);
			g2.fillRect(0, (int) (mainController.getHeight() - textField.getPreferredSize().getHeight() - scrollPane.getPreferredSize().getHeight() - 30), 200, 30);
			
			if (player.getCurrHealth() > 0) {
				g2.setColor(Color.GREEN);
				g2.fillRect(0, (int) (mainController.getHeight() - textField.getPreferredSize().getHeight() - scrollPane.getPreferredSize().getHeight() - 30), (int) ((double) player.getCurrHealth() /  (double) player.getMaxHealth() * 200), 30);
			}
			
			g2.setColor(Color.YELLOW);
			g2.drawString("Mining: " + player.getState(0), 5, 15);
			g2.drawString("Woodcutting: " + player.getState(1), 5, 35);
			g2.drawString("Fishing: " + player.getState(2), 5, 55);
			g2.drawString("MakeXNum: " + makeXString, 5, 75);
			
			FontMetrics fm = this.getFontMetrics(new Font("TimesRoman", Font.PLAIN, (int)(0.5*fontSize)));
			g2.setColor(Color.WHITE);
			
			g2.drawString(spokenText,((state.boardColsNum - 3) / 2) * ImageEnum.TILEGRASS.getWidth() + (int) (.5 * ImageEnum.TILEGRASS.getWidth()) - fm.stringWidth(spokenText)/2, ((state.boardRowsNum - 3) / 2) * ImageEnum.TILEGRASS.getHeight() + (int) (.05 * ImageEnum.TILEGRASS.getHeight()));
			
			if (bankOpen) {
				g2.drawImage(ImageEnum.BANK.getImg(), (mainController.getWidth() / 2) - (ImageEnum.BANK.getImg().getWidth() / 2), 50, null);
				for (BankTile b : player.getBank().getBank()) {
					g2.drawImage(ImageEnum.ICONBLANK.getImg(),
							(int) ((mainController.getWidth() / 2) - (ImageEnum.BANK.getImg().getWidth() / 2) + (.03 * ImageEnum.BANK.getWidth()) + (b.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .02 * ImageEnum.BANK.getWidth()))),
							(int) (50 + (.02 * ImageEnum.BANK.getHeight()) + (b.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .02 * ImageEnum.BANK.getWidth()))),
							null);
					btnNote.setVisible(true);
					if (b.getItem() > 0) {
						g2.drawImage(b.getItemImg(),
								(int) ((mainController.getWidth() / 2) - (ImageEnum.BANK.getImg().getWidth() / 2) + (.03 * ImageEnum.BANK.getWidth()) + (b.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .02 * ImageEnum.BANK.getWidth()))),
								(int) (50 + (.02 * ImageEnum.BANK.getHeight()) + (b.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .02 * ImageEnum.BANK.getWidth()))),
								null);
						g2.drawString(Integer.toString(b.getCount()),
								(int) ((mainController.getWidth() / 2) - (ImageEnum.BANK.getImg().getWidth() / 2) + (.03 * ImageEnum.BANK.getWidth()) + (b.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .02 * ImageEnum.BANK.getWidth()))),
								(int) (60 + (.02 * ImageEnum.BANK.getHeight()) + (b.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .02 * ImageEnum.BANK.getWidth()))));
					}
				}
			} else {
				btnNote.setVisible(false);
			}
			
			if (player.getUpdateState() == -1) {
				player.setUpdateState(1);
			}
			//System.out.println("updatestate: " + player.getUpdateState());
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
		ImageEnum.BANK.scaleByFactor(scaleFactor);
		
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
			} else if ((state.objTiles[click[0]][click[1]] >= 90) && state.objTiles[click[0]][click[1]] < 130) {
				s.add("<html>Fish <font color=\"#ffff00\"> " + TileFishingSpot.pickFishingSpot((int) Math.floor(state.objTiles[click[0]][click[1]])).getFishingSpotName() + " </font></html>");
			}
			if (state.itemTiles[click[0]][click[1]] != null) {
				for (int i = state.itemTiles[click[0]][click[1]].size() - 1; i >= 0; i--) {
					s.add("<html>Take <font color=\"#f8d56b\"> " + Item.getItemById(state.itemTiles[click[0]][click[1]].get(i)[0]).getItemName() + " </font></html>");
				}
			}
			if (state.npcTiles[click[0]][click[1]] != null) {
				for (int i = state.npcTiles[click[0]][click[1]].size() - 1; i >= 0; i--) {
					if (state.npcTiles[click[0]][click[1]].get(i) instanceof Enemy) {
						s.add("<html>Attack <font color=\"#f8d56b\"> " + state.npcTiles[click[0]][click[1]].get(i).getName() + " </font></html>");
					}
					if (state.npcTiles[click[0]][click[1]].get(i).getTalkable()) {
						s.add("<html>Talk-to <font color=\"#f8d56b\"> " + state.npcTiles[click[0]][click[1]].get(i).getName() + " </font></html>");
					}
				}
			}
			s.add("Walk here");
		} else if (click[5] == 1) {
			if ((click[0] >= 0) && (player.getInv().getSlot(click[0]) != 0)) {
				if (this.bankOpen) {
					s.add("<html>Deposit <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getInv().getSlot(click[0])).getItemName() + " </font></html>");
					s.add("<html>Deposit 5 <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getInv().getSlot(click[0])).getItemName() + " </font></html>");
					s.add("<html>Deposit 10 <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getInv().getSlot(click[0])).getItemName() + " </font></html>");
					s.add("<html>Deposit 100 <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getInv().getSlot(click[0])).getItemName() + " </font></html>");
					s.add("<html>Deposit all <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getInv().getSlot(click[0])).getItemName() + " </font></html>");
				} else {
					s.add("<html>Use <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getInv().getSlot(click[0])).getItemName() + " </font></html>");
					s.add("<html>Drop <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getInv().getSlot(click[0])).getItemName() + " </font></html>");
				}
			}
		} else if (click[5] == 2) {
			if ((click[0] >= 0) && (player.getBank().getSlot(click[0]) != 0)) {
				s.add("<html>Withdraw <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getBank().getSlot(click[0])).getItemName() + " </font></html>");
				s.add("<html>Withdraw 5 <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getBank().getSlot(click[0])).getItemName() + " </font></html>");
				s.add("<html>Withdraw 10 <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getBank().getSlot(click[0])).getItemName() + " </font></html>");
				s.add("<html>Withdraw 100 <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getBank().getSlot(click[0])).getItemName() + " </font></html>");
				s.add("<html>Withdraw x <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getBank().getSlot(click[0])).getItemName() + " </font></html>");
				s.add("<html>Withdraw all-but-1 <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getBank().getSlot(click[0])).getItemName() + " </font></html>");
				s.add("<html>Withdraw all <font color=\"#f8d56b\"> " + InventoryTile.pickItem(player.getBank().getSlot(click[0])).getItemName() + " </font></html>");
			}
		}
		s.add("Cancel");
		return s;
	}
	
	private String formatText(String text) {
		text = text.replaceAll(" i ", " I ");
		text = text.replaceFirst(text.substring(0, 1), text.substring(0, 1).toUpperCase());
		String[] tmp = text.split(" ");
		for (int j = 0; j < tmp.length - 1; j++) {
			if (tmp[j].endsWith(".")) {
				tmp[j+1] = tmp[j+1].substring(0, 1).toUpperCase() + tmp[j+1].substring(1, tmp[j+1].length());
			}
		}
		if (text.endsWith(" i")) {
			tmp[tmp.length - 1] = "I";
		}
		text = "";
		for (int i = 0; i < tmp.length; i++) {
			text += tmp[i].substring(0, 1) + tmp[i].substring(1, tmp[i].length()).toLowerCase();
			if (i != tmp.length - 1) {
				text += " ";
			}
		}
		return text;
	}
	
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
	
	public void actionPerformed(ActionEvent event) {
		System.out.println("event");
		if(event.getSource() == btnExit) {
			this.shutdown();
		} else if (event.getSource() == btnNote) {
			if (btnNote.getBackground().equals(Color.WHITE)) {
				btnNote.setBackground(Color.LIGHT_GRAY);
			} else {
				btnNote.setBackground(Color.WHITE);
			}
			player.getBank().setWithdrawNotes(!player.getBank().getWithdrawNotes());
			this.requestFocusInWindow();
		} else if (event.getSource() == textField) {
			String text = textField.getText();
			
			if (!text.equals("")) {
				text = formatText(text);
				appendToPane(textPane, "[" + new SimpleDateFormat("HH:mm").format(new Timestamp(System.currentTimeMillis())) + "] ", Color.BLACK);
				appendToPane(textPane, player.getUsername(), Color.BLUE);
				appendToPane(textPane, ": " + text + newline, Color.BLACK);
				spokenText = text;
				spokenTime = System.currentTimeMillis();
				textPane.setCaretPosition(textPane.getDocument().getLength());
			}
			textField.setText("Press enter to chat");
			textPane.setCaretPosition(textPane.getDocument().getLength());
			this.requestFocusInWindow();
		}/* else if (event.getSource() == makeX) {
			String text = makeX.getText().trim();
			System.out.println(text);
			if (!text.equals("")) {
				if (text.matches("-?\\d+")) {
					makeNum = Integer.parseInt(text);
				}
				makeX.setText("");
				makeXOpen = false;
				if (makeNum > player.getBank().getBank().get(click[0]).getCount()) {
					makeNum = player.getBank().getBank().get(click[0]).getCount();
				}
				if (player.getBank().getWithdrawNotes()) {
					if (player.getInv().addItem(player.getBank().getSlot(click[0]), player.getBank().getWithdrawNotes(), makeNum)) {
						player.getBank().getBank().get(click[0]).decreaseCount(makeNum);
					}
				} else {
					for (int i = 0; i < makeNum; i++) {
						if (player.getInv().searchInventorySpace()) {
							if (player.getInv().addItem(player.getBank().getSlot(click[0]), player.getBank().getWithdrawNotes(), 1)) {
								player.getBank().getBank().get(click[0]).decreaseCount(1);
							}
						} else {
							return;
						}
					}
				}
			}
			this.requestFocusInWindow();
		}*/
	}
	
	private static void appendToPane(JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
	}
	
	public static void printText(JTextPane tp, String msg, Color c) {
		appendToPane(tp, msg, c);
	}
      
	
	public void movePlayer(int[] tmp, boolean interact) {
		player.moveTo(tmp[0], tmp[1], tmp[2] == 1, tmp[3], tmp[4], interact);
	}
	
	public void processMakeX() {
		if (makeXType.equals("BankWithdraw")) {
			if (makeNum > player.getBank().getBank().get(click[0]).getCount()) {
				makeNum = player.getBank().getBank().get(click[0]).getCount();
			}
			if (player.getBank().getWithdrawNotes()) {
				if (player.getInv().addItem(player.getBank().getSlot(click[0]), player.getBank().getWithdrawNotes(), makeNum)) {
					player.getBank().getBank().get(click[0]).decreaseCount(makeNum);
				}
			} else {
				for (int i = 0; i < makeNum; i++) {
					if (player.getInv().searchInventorySpace()) {
						if (player.getInv().addItem(player.getBank().getSlot(click[0]), player.getBank().getWithdrawNotes(), 1)) {
							player.getBank().getBank().get(click[0]).decreaseCount(1);
						}
					} else {
						return;
					}
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		if (makeXOpen) {
			if (Character.isDigit(e.getKeyChar())) {
				makeXString += e.getKeyChar();
			} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				makeXString = makeXString.substring(0, makeXString.length() - 1);
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (!makeXString.equals("")) {
					makeNum = Integer.parseInt(makeXString);
					System.out.println(makeNum);
					processMakeX();
				}
				makeXString = "";
				makeXOpen = false;
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				makeXString = "";
				makeXOpen = false;
			}
		} else {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				System.out.println("enter");
				textField.requestFocusInWindow();
				textField.setText("");
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				if (Game.bankOpen) {
					Game.bankOpen = false;
				} else {
					shutdown();
				}
			} else if (e.getKeyCode() == KeyEvent.VK_5) {
				player.damage(10);
			} else if (e.getKeyCode() == KeyEvent.VK_B) {
				if (!Game.bankOpen) {
					Game.bankOpen = true;
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		
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
	    	if (this.getPreferredSize().width < 100) {
	    		this.setPreferredSize(new Dimension(100, this.getPreferredSize().height));
	    	}
	    }
	}
	
	class PopupActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String tmp = event.getActionCommand();
			String str[] = tmp.split(" ");
			player.attack(null);
			if (str.length > 0) {
				if (str[0].equals("Cancel")) {
					return;
				} else if ((str[0] + " " + str[1]).equals("Walk here")) {
					movePlayer(click, false);
				}  else if (str[0].equals("<html>Mine") || str[0].equals("<html>Cut") || str[0].equals("<html>Fish")) {
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
				} else if (str[0].equals("<html>Deposit")) {
    				int amt = 1;
					if (str[1].equals("5") || str[1].equals("10") || str[1].equals("100")) {
						amt = Integer.parseInt(str[1]);
					} else if (str[1].equals("all")) {
						if (player.getInv().getInventory().get(click[0]).getIsNoted()) {
							amt = player.getInv().getInventory().get(click[0]).getCount();
						} else {
							amt = Inventory.getInventorySize();
						}
					}
					int itemNum = player.getInv().getInventory().get(click[0]).getItem();
					if (player.getInv().getInventory().get(click[0]).getIsNoted()) {
						if (player.getBank().addItem(itemNum, amt)) {
							player.getInv().getInventory().get(click[0]).setCount(player.getInv().getInventory().get(click[0]).getCount() - amt);
						}
					} else {
						int curr = 0;
						for (int b = 0; b < Inventory.getInventorySize(); b++) {
	    					if ((player.getInv().getSlot(b) == itemNum) && !player.getInv().getInventory().get(b).getIsNoted()) {
	    						if (player.getBank().addItem(itemNum, 1)) {
	    							player.getInv().decreaseSlot(b);
	    							curr++;
	    							if (curr >= amt) {
	    								break;
	    							}
	    						}
	    					}
	    				}
					}
    			} else if (str[0].equals("<html>Withdraw")) {
    				int amt = 1;
					if (str[1].equals("5") || str[1].equals("10") || str[1].equals("100")) {
						amt = Integer.parseInt(str[1]);
					} else if (str[1].equals("x")) {
						makeXOpen = true;
						makeXType = "BankWithdraw";
						amt = 0;
					} else if (str[1].equals("all-but-1")) {
						amt = player.getBank().getBank().get(click[0]).getCount() - 1;
					} else if (str[1].equals("all")) {
						amt = player.getBank().getBank().get(click[0]).getCount();
					}
					if (amt > player.getBank().getBank().get(click[0]).getCount()) {
						amt = player.getBank().getBank().get(click[0]).getCount();
					}
					if (player.getBank().getWithdrawNotes()) {
						if (player.getInv().addItem(player.getBank().getSlot(click[0]), player.getBank().getWithdrawNotes(), amt)) {
							player.getBank().getBank().get(click[0]).decreaseCount(amt);
						}
					} else {
						for (int i = 0; i < amt; i++) {
							if (player.getInv().searchInventorySpace()) {
								if (player.getInv().addItem(player.getBank().getSlot(click[0]), player.getBank().getWithdrawNotes(), 1)) {
									player.getBank().getBank().get(click[0]).decreaseCount(1);
								}
							} else {
								return;
							}
						}
					}
				} else if (str[0].equals("<html>Attack")) {
					click[2] = 1;
					String t = "";
					for (int i = 3; i < str.length - 1; i++) {
    					System.out.println("adding " + str[i]);
    					t += str[i];
    					if (i != str.length - 2) {
    						t += " ";
    					}
    				}
					for (int i = 0; i < state.npcTiles[click[0]][click[1]].size(); i ++) {
						if (state.npcTiles[click[0]][click[1]].get(i).getName().equals(t) && (state.npcTiles[click[0]][click[1]].get(i) instanceof Enemy)) {
							player.setInCombatWith((Enemy) state.npcTiles[click[0]][click[1]].get(i));
						}
					}
				} else if (str[0].equals("<html>Talk-to")) {
					System.out.println("talk");
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
	        	player.attack(null);
    			if ((str.length > 1) && (str[0] + " " + str[1]).equals("Walk here")) {
    				player.moveTo(click[0], click[1], click[2] == 1, click[3], click[4], false);
    			} else if (str[0].equals("<html>Mine") || str[0].equals("<html>Cut") || str[0].equals("<html>Fish")) {
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
    			} else if (str[0].equals("<html>Deposit")) {
    				int amt = 1;
					if (str[1].equals("5") || str[1].equals("10") || str[1].equals("100")) {
						amt = Integer.parseInt(str[1]);
					} else if (str[1].equals("all")) {
						if (player.getInv().getInventory().get(click[0]).getIsNoted()) {
							amt = player.getInv().getInventory().get(click[0]).getCount();
						} else {
							amt = Inventory.getInventorySize();
						}
					}
					int itemNum = player.getInv().getInventory().get(click[0]).getItem();
					if (player.getInv().getInventory().get(click[0]).getIsNoted()) {
						if (player.getBank().addItem(itemNum, amt)) {
							player.getInv().getInventory().get(click[0]).setCount(player.getInv().getInventory().get(click[0]).getCount() - amt);
						}
					} else {
						int curr = 0;
						for (int b = 0; b < Inventory.getInventorySize(); b++) {
	    					if ((player.getInv().getSlot(b) == itemNum) && !player.getInv().getInventory().get(b).getIsNoted()) {
	    						if (player.getBank().addItem(itemNum, 1)) {
	    							player.getInv().decreaseSlot(b);
	    							curr++;
	    							if (curr >= amt) {
	    								break;
	    							}
	    						}
	    					}
	    				}
					}
    			} else if (str[0].equals("<html>Withdraw")) {
    				int amt = 1;
					if (str[1].equals("5") || str[1].equals("10") || str[1].equals("100")) {
						amt = Integer.parseInt(str[1]);
					} else if (str[1].equals("x")) {
						makeXOpen = true;
						makeXType = "BankWithdraw";
						amt = 0;
					} else if (str[1].equals("all-but-1")) {
						amt = player.getBank().getBank().get(click[0]).getCount() - 1;
					} else if (str[1].equals("all")) {
						amt = player.getBank().getBank().get(click[0]).getCount();
					}
					if (amt > player.getBank().getBank().get(click[0]).getCount()) {
						amt = player.getBank().getBank().get(click[0]).getCount();
					}
					if (player.getBank().getWithdrawNotes()) {
						if (player.getInv().addItem(player.getBank().getSlot(click[0]), player.getBank().getWithdrawNotes(), amt)) {
							player.getBank().getBank().get(click[0]).decreaseCount(amt);
						}
					} else {
						
						for (int i = 0; i < amt; i++) {
							if (player.getInv().searchInventorySpace()) {
								if (player.getInv().addItem(player.getBank().getSlot(click[0]), player.getBank().getWithdrawNotes(), 1)) {
									player.getBank().getBank().get(click[0]).decreaseCount(1);
								}
							} else {
								return;
							}
						}
					}
    			} else if (str[0].equals("<html>Attack")) {
    				click[2] = 1;
					String t = "";
					for (int i = 3; i < str.length - 1; i++) {
    					System.out.println("adding " + str[i]);
    					t += str[i];
    					if (i != str.length - 2) {
    						t += " ";
    					}
    				}
					for (int i = 0; i < state.npcTiles[click[0]][click[1]].size(); i ++) {
						if (state.npcTiles[click[0]][click[1]].get(i).getName().equals(t) && (state.npcTiles[click[0]][click[1]].get(i) instanceof Enemy)) {
							player.setInCombatWith((Enemy) state.npcTiles[click[0]][click[1]].get(i));
						}
					}
				} else if (str[0].equals("<html>Talk-to")) {
					System.out.println("talk");
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
	    	if ((dx == 0) && (dy == 0)) {
	    		click = CollisionDetection.checkCollisionsTile(e.getButton(), e.getX(), e.getY(), state, board, player);
	    		reactClick(e);
	    	} else {
	    		int tmpSlot = click[0];
	    		click = CollisionDetection.checkCollisionsTile(e.getButton(), e.getX(), e.getY(), state, board, player);
	    		if ((click[5] == 1) && (click[0] > -1)) {
	    			player.getInv().swapSlots(tmpSlot, click[0]);
	    		} else if (click[5] == 0) {
	    			player.dropSlot(tmpSlot);
	    		}
	    	}
	    	dx = 0;
	    	dy = 0;
	    }
	    
	    public void mouseEntered(MouseEvent e) {
	    	menu.setVisible(false);
	    }

	    private String doPop(MouseEvent e, boolean doDisplay, ArrayList<String> str){
	        menu = new PopUpDemo(str);
	        if (doDisplay) {
	        	int xFar;
	        	if ((mouseLoc[0] + menu.getPreferredSize().getWidth()/2) > mainController.getWidth()) {
					xFar = (int) (mainController.getWidth() - (mouseLoc[0] + menu.getPreferredSize().getWidth()/2));
				} else {
					xFar = 0;
				}
	        	int yFar;
	        	if ((mouseLoc[1] + menu.getPreferredSize().getHeight()) > mainController.getHeight()) {
					yFar = (int) (mainController.getHeight() - (mouseLoc[1] + menu.getPreferredSize().getHeight()));
				} else {
					yFar = 0;
				}
	        	menu.show(e.getComponent(), (int) (e.getX() - menu.getPreferredSize().getWidth()/2 + xFar), e.getY() + yFar );
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
			int[] clickTmp = CollisionDetection.checkCollisionsTile(e.getButton(), mouseLoc[0], mouseLoc[1], state, board, player);
			if (clickTmp[5] == 1) {
				dx = e.getX() - mouseLoc[0];
				dy = e.getY() - mouseLoc[1];
				if ((dx > 0) || (dy > 0)) {
					hoverText = "";
				}
			}
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
