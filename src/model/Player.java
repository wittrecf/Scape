package model;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JTextPane;

import view.Game;
import view.ImageEnum;

public class Player {
	private String username;
	private String password;
	private int xLoc;
	private int yLoc;
	private int xOff = 0;
	private int yOff = 0;
	private int xDir = 0;
	private int yDir = 0;
	private int movSize = 16;
	private int pickItem = 0;
	private ArrayList<Node> path;
	private int[] target;
	private BoardState state;
	private boolean transfer = false;
	private boolean doInteract;
	private Inventory inv;
	private JTextPane chatbox;
	private int updateState = 0;
	private static int skillNum = 3;
	private int maxHealth = 100;
	private int currHealth;
	private int damage = 1;
	private int attackSpeed = 5;
	private Enemy inCombatWith = null;
	private double attackTime = 0;
	private boolean autoRetaliate = false;
	private static int bankSize = 100;
	private Bank bank;
	
	private int[] statXP = new int[skillNum];
	
	private int[][] dir = {{1, 1},
			   			   {1, -1},
			   			   {-1, 1},
			   			   {-1, -1},
			   			   {1, 0},
						   {0, 1},
						   {-1, 0},
						   {0, -1}};
	
	private static String userFile = "resources/userFile";
	private static String playerDataFile = "resources/playerDataFile";
	private String userBankfile;
	
	
	private final int DEFAULTX = 50;
	private final int DEFAULTY = 50;
	
	public Player(String user, int width, int height) {
		makePassword();
		this.username = user;
		this.userBankfile = "resources/" + this.username;
		this.xLoc = DEFAULTX;
		this.yLoc = DEFAULTY;
		this.inv = new Inventory(width, height);
		this.currHealth = this.maxHealth;
		inv.setDefaultInventory();
		for (int j = 0; j < skillNum; j++) {
			this.statXP[j] = 0;
		}
		this.path = new ArrayList<Node>();
		this.chatbox = new JTextPane();
		this.bank = new Bank(10, 14, this.bankSize, new int[0][0]);
		try {
			writePlayerData();
			Files.write(Paths.get(userFile), (this.username + System.getProperty("line.separator")).getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Player(String[] arr, int width, int height) {
		this.username = arr[0];
		this.password = arr[1];
		this.xLoc = Integer.parseInt(arr[2]);
		this.yLoc = Integer.parseInt(arr[3]);
		this.inv = new Inventory(width, height);
		this.currHealth = this.maxHealth; //TEMP------
		for (int i = 0; i < Inventory.getInventorySize(); i++) {
			this.inv.setSlot(i, Integer.parseInt(arr[4 + i]), arr[4 + Inventory.getInventorySize() + i].equals("true"), Integer.parseInt(arr[4 + 2*Inventory.getInventorySize() + i]));
		}
		for (int j = 0; j < skillNum; j++) {
			this.statXP[j] = Integer.parseInt(arr[4 + 3*Inventory.getInventorySize() + j]);
		}
		this.target = null;
		this.path = new ArrayList<Node>();
		this.chatbox = new JTextPane();
		this.bank = new Bank(10, 14, this.bankSize, new int[0][0]);
	}
	
	private void makePassword() {
		String input;
		boolean done = false;
		while (!done) {
			System.out.print("Enter password: ");
			Scanner scanner = new Scanner(System.in);
			input = scanner.nextLine();
			System.out.print("Enter password again: ");
			if (scanner.nextLine().equals(input)) {
				this.password = input;
				done = true;
			} else {
				System.out.println("Passwords do not match.");
			}
		}
	}
	
	private static boolean searchUser(String u) {
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(userFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	    while (inFile.hasNextLine()) {
	    	if (inFile.nextLine().toLowerCase().equals(u.toLowerCase())) {
	    		return true;
	    	}
	    }
	    inFile.close();
	    return false;
	}
	
	public void writePlayerData() throws FileNotFoundException, UnsupportedEncodingException {
		Scanner inFile = null;
		Boolean found = false;
		try {
			inFile = new Scanner(new File(playerDataFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> tmp = new ArrayList<String>();
		
	    while (inFile.hasNext()) {
	    	tmp.add(inFile.nextLine());
	    }
	    inFile.close();
	    
		PrintWriter writer = new PrintWriter(playerDataFile, "UTF-8");
		
		Iterator<String> it = tmp.iterator();
		String s = "";
		while (it.hasNext()) {
			s = it.next();
			if (s.contains(this.getUsername())) {
				s = this.toString();
				found  = true;
			}
			writer.println(s);
		}
		if (!found) {
			s = this.toString();
			writer.println(s);
		}
		writer.close();
	}
	
	public static Player loadPlayer(String user, int width, int height) throws FileNotFoundException, UnsupportedEncodingException {
		Scanner inFile = null;
		String[] playerArr = new String[4 + 3*Inventory.getInventorySize() + skillNum];
		try {
			inFile = new Scanner(new File(playerDataFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> tmp = new ArrayList<String>();
		
	    while (inFile.hasNext()) {
	    	tmp.add(inFile.nextLine());
	    }
	    inFile.close();
		
		Iterator<String> it = tmp.iterator();
		String s = "";
		while (it.hasNext()) {
			s = it.next();
			if (s.toLowerCase().contains(user.toLowerCase())) {
				String[] arr = s.split(" ");
				playerArr[0] = arr[0];
				playerArr[1] = arr[1];
				playerArr[2] = arr[2];
				playerArr[3] = arr[3];
				for (int i = 0; i < Inventory.getInventorySize(); i++) {
					playerArr[4 + i] = arr[4 + i];
				}
				for (int i = 0; i < Inventory.getInventorySize(); i++) {
					playerArr[4 + Inventory.getInventorySize() + i] = arr[4 + Inventory.getInventorySize() + i];
				}
				for (int i = 0; i < Inventory.getInventorySize(); i++) {
					playerArr[4 + 2*Inventory.getInventorySize() + i] = arr[4 + 2*Inventory.getInventorySize() + i];
				}
				for (int j = 0; j < skillNum; j++) {
					playerArr[4 + 3*Inventory.getInventorySize() + j] = arr[4 + 3*Inventory.getInventorySize() + j];
				}
				return new Player(playerArr, width, height);
			}
		}
		return null;
	}
	
	public void loadBank(String user) throws FileNotFoundException, UnsupportedEncodingException {
		int[][] bankArr = new int[2][Player.bankSize];
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(this.userBankfile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> tmp = new ArrayList<String>();
		
	    while (inFile.hasNext()) {
	    	tmp.add(inFile.nextLine());
	    }
	    inFile.close();
    
		String[] arr1 = tmp.get(0).split(" ");
		String[] arr2 = tmp.get(1).split(" ");
		for (int i = 0; i < arr1.length; i++) {
			bankArr[0][i] = Integer.parseInt(arr1[i]);
			bankArr[1][i] = Integer.parseInt(arr2[i]);
		}
		
		this.bank = new Bank(10, 14, this.bankSize, bankArr);
	}
	
	public static Player makeAccount(String user, int width, int height) {
		if (Player.searchUser(user)) {
			System.out.println("Username already taken.");
			return null;
		} else {
			return new Player(user, width, height);
		}
	}
	
	private ArrayList<Node> createNodeMap(int len, int xPass, int yPass) {
		//System.out.println("L is " + len);
		//System.out.println("xPass: " + xPass + ", yPass: " + yPass + ". Current loc is: " + xLoc + ", " + yLoc);
		Node[][] arr = new Node[(int) (2*len + 1)][(int) (2*len + 1)];
		ArrayList<Node> list = new ArrayList<Node>();
		Node n;
		int tmpX = this.getXLoc() - xDir;
		int tmpY = this.getYLoc() - yDir;
		//System.out.println("i spans from " + (tmpX - len) + " to " + (this.getXLoc() + len));
		//System.out.println("j spans from " + (tmpY - len) + " to " + (tmpY + len));
		for (int i = tmpX - len; i <= tmpX + len; i++) {
			for (int j = tmpY - len; j <= tmpY + len; j++) {
				if (!((i < 0) || (i >= state.mapColsNum) || (j < 0) || (j >= state.mapRowsNum))) {
					if ((state.mapTiles[i][j] == 1) && (state.objTiles[i][j] == 0) && ((this.inCombatWith == null) || ((this.inCombatWith != null) && !((this.inCombatWith.getXLoc() == i) && (this.inCombatWith.getYLoc() == j) && (xPass == this.inCombatWith.getXLoc()) && (yPass == this.inCombatWith.getYLoc()))))) {
						n = new Node(i, j);
						//System.out.println("made a new node at " + i + ", " + j);
						if ((i == tmpX) && (j == tmpY)) {
							//System.out.println("zero node at " + i + ", " + j);
							//System.out.println("xDir: " + xDir + ", yDir: " + yDir);
							n.setDist(0);
						}
					} else {
						n = null;
					}
					arr[i - (tmpX - len)][j - (tmpY - len)] = n;
				}
			}
		}
		
		int x;
		int y;
		
		for (Node[] ls : arr) {
			for (Node n1 : ls) {
				if (n1 != null) {
					//System.out.println("trying out " + n1.getX() + ", " + n1.getY());
					for (int i = 0; i < dir.length; i++) {
						x = n1.getX() + dir[i][0];
						y = n1.getY() + dir[i][1];
						//System.out.println(x + " / " + y);
						if ((x - (tmpX - len) >= 0) && (x - (tmpX - len) < 2*len) &&
							(y - (tmpY - len) >= 0) && (y - (tmpY - len) < 2*len) &&
							!(arr[x - (tmpX - len)][y - (tmpY - len)] == null)) {
							n1.addConnection(arr[x - (tmpX - len)][y - (tmpY - len)]);
							//System.out.println("adding connection from " + n1.getX() + ", " + n1.getY() + " and " + arr[x - (tmpX - len)][y - (tmpY - len)].getX() + ", " + arr[x - (tmpX - len)][y - (tmpY - len)].getY());

						}
					}
				}
			}
		}
		
		for (Node[] ls : arr) {
			for (Node n1 : ls) {
				if (n1 != null) {
					list.add(n1);
				}
			}
		}
		return list;
	}
	
	private int calcDist(Node n1, Node n2) {
		int d;
		if ((n1.getX() != n2.getX()) && (n1.getY() != n2.getY())) {
			d = 3;
		} else {
			d = 2;
		}
		return d;
	}
	
	private Node findPath(int x, int y) {
		int length = 20;
		
		//System.out.println("chunk 1 done");
		//System.out.println("finding path to " + x + ", " + y);
		
		ArrayList<Node> list = createNodeMap(length, x, y);
		Node min;
		Node close = null;
		int prox = 999;
		
		//System.out.println("nodemap is length " + list.size());
		
		while (!list.isEmpty()) {
			min = new Node(0,0);
			min.setDist(999);
			for (Node n : list) {
				if (n.getDist() <= min.getDist()) {
					min = n;
				}
			}
			
			//System.out.println("min is " + min.getX() + ", " + min.getY());
			
			list.remove(min);
			
			if ((min.getX() == x) && (min.getY() == y) && (min.getDist() < 999)) {
				//System.out.println("final min: " + min.getX() + ", " + min.getY());
				return min;
			} else if (min.getDist() < 999) {
				int xDif = Math.abs(min.getX() - x);
				int yDif = Math.abs(min.getY() - y);
				int tmpProx;
				if (xDif > yDif) {
					tmpProx = (3 * yDif) + (2 * xDif - yDif);
				} else {
					tmpProx = (3 * xDif) + (2 * yDif - xDif);
				}
				if (tmpProx < prox) {
					prox = tmpProx;
					close = min;
				}
			}
			
			for (Node n : min.getConnections()) {
				int alt = calcDist(min, n);
				if (list.contains(n)) {
					if (min.getDist() + alt < n.getDist()) {
						n.setDist(min.getDist() + alt);
						n.setPrev(min);
					}
				}
			}
		}
		return close;
	}
	
	public void clearPrevPath() {
		for (int j = 0; j < state.mapRowsNum; j++) {
    		for (int i = 0; i < state.mapColsNum; i++) {
				if (state.mapTiles[i][j] == 3) {
					state.mapTiles[i][j] = 1;
				}
			}
		}
	}
	
	public void moveTo(int x, int y, boolean isTarget, int xSet, int ySet, boolean interact) {
		System.out.println("Go to " + x + ", " + y + ". " + isTarget + ". " + xSet + ", " + ySet + ". " + interact);
		Node tmp = null;
		this.doInteract = interact;
		pickItem = 0;
		if (isTarget) {
			this.target = new int[4];
			this.target[0] = x;
			this.target[1] = y;
			this.target[2] = xSet;
			this.target[3] = ySet;
		} else {
			this.target = null;
		}
		if (path.size() > 0) {
			if ((Math.abs(this.xOff) != ImageEnum.TILEGRASS.getWidth()) || (Math.abs(this.yOff) != ImageEnum.TILEGRASS.getHeight())) {
				//System.out.println("bleh");
				tmp = path.get(path.size() - 1);
			}
			path = new ArrayList<Node>();
		}
		clearPrevPath();
		Node n = findPath(x, y);
		while (true) {
			//System.out.println(n.getX() + ", " + n.getY());
			//System.out.println("dist " + n.getDist());
			if ((n != null) && ((path.size() == 0) || (checkAdjacency(true, true, new int[] {path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY()}, new int[] {n.getX(), n.getY()})))) {
				state.mapTiles[n.getX()][n.getY()] = 3;
				if (n.getPrev() != null) {
					path.add(n);
					n = n.getPrev();
				} else {
					if (tmp != null) {
						path.add(tmp);
					}
					break;
				}
			} else {
				path = new ArrayList<Node>();
				return;
			}
		}
	}
	
	private void findDir() {
		Node n = path.get(path.size() - 1);
		int x = this.xLoc - n.getX();
		int y = this.yLoc - n.getY();
		if (x != 0) {
			this.xDir = x / Math.abs(x);
		}
		if (y != 0) {
			this.yDir = y / Math.abs(y);
		}
	}
	
	public static boolean checkAdjacency(boolean checkDiagonal, boolean checkUnder, int[] t1, int[] t2) {
		if (checkUnder) {
			if ((t1[0] == t2[0]) && (t1[1] == t2[1])) {
				return true;
			}
		}
		if (checkDiagonal) {
			return (Math.abs(t2[0] - t1[0]) <= 1) && (Math.abs(t2[1] - t1[1]) <= 1);
		} else {
			return ((Math.abs(Math.abs(t2[0] - t1[0]) + Math.abs(t2[1] - t1[1])) == 1));
		}
		
	}
	
	private void addXP(String type, int xp) {
		if (type.equals("TileRock")) {
			statXP[0] += xp;
		} else if (type.equals("TileTree")) {
			statXP[1] += xp;
		} else if (type.equals("TileFishingSpot")) {
			statXP[2] += xp;
		}
	}
	
	public void interact() {
		if (checkAdjacency(false, false, new int[] {xLoc, yLoc}, target)) {
			System.out.println(target[0] + "-" + xLoc + "+" + 15);
			System.out.println(target[1] + "-" + yLoc + "+" + 7);
			System.out.println((target[0] - xLoc + 15) + ", " + (target[1] - yLoc + 7));
			if (this.inv.searchInventorySpace()) {
				System.out.println(state.tiles.get(target[1] - yLoc + 7).get(target[0] - xLoc + 15).getObj().getStarted());
				int[] tmp = state.tiles.get(target[1] - yLoc + 7)
						.get(target[0] - xLoc + 15)
						.getObj()
						.start(state);
				if (tmp != null) {
					inv.addItem(tmp[0], false, 1);
					addXP(state.tiles.get(target[1] - yLoc + 7).get(target[0] - xLoc + 15).getObj().getType(), tmp[1]);
					path.clear();
					target = null;
				} else {
					System.out.println(state.tiles.get(target[1] - yLoc + 7).get(target[0] - xLoc + 15).getObj().getDepleted());
					Game.printText(chatbox, state.tiles.get(target[1] - yLoc + 7).get(target[0] - xLoc + 15).getObj().getDepleted() + "\n", Color.GRAY);
					path.clear();
					target = null;
				}
			} else {
				Game.printText(chatbox, "Your inventory is full.\n", Color.GRAY);
				path.clear();
				target = null;
			}
			/*if (state.tiles.get(target[3]/ImageEnum.TILEGRASS.getWidth() + 1).get(target[2]/ImageEnum.TILEGRASS.getHeight() + 1).getObj().getType().equals("MiningRock")
				&& ((MiningRock) state.tiles.get(target[3]/ImageEnum.TILEGRASS.getWidth() + 1).get(target[2]/ImageEnum.TILEGRASS.getHeight() + 1).getObj()).getRockType().getRockId() % 2 != 0) {
				System.out.println("adj");
				state.tiles.get(target[3]/ImageEnum.TILEGRASS.getWidth() + 1).get(target[2]/ImageEnum.TILEGRASS.getHeight() + 1).getObj().start(state);
			} else if (state.tiles.get(target[3]/ImageEnum.TILEGRASS.getWidth() + 1).get(target[2]/ImageEnum.TILEGRASS.getHeight() + 1).getObj().getType().equals("MiningRock")) {
				System.out.println("Rock is depleted.");
				path.clear();
				target = null;
			}*/
		}
	}
	
	public void move() {
		if (path.size() > 0) {
			findDir();
			if ((Math.abs(this.xOff) != ImageEnum.TILEGRASS.getWidth()) && (Math.abs(this.yOff) != ImageEnum.TILEGRASS.getHeight()) && (Math.abs(this.xDir) == Math.abs(this.yDir)))  {
				this.xOff += (ImageEnum.TILEGRASS.getWidth() * this.xDir) / this.movSize;
				this.yOff += (ImageEnum.TILEGRASS.getHeight() * this.yDir) / this.movSize;
				//System.out.println("a");
			} else if ((Math.abs(this.xOff) != ImageEnum.TILEGRASS.getWidth()) && (this.xDir != 0)) {
				this.xOff += (ImageEnum.TILEGRASS.getWidth() * this.xDir) / this.movSize;
				//System.out.println("b");
			} else if ((Math.abs(this.yOff) != ImageEnum.TILEGRASS.getHeight()) && (this.yDir != 0)) {
				this.yOff += (ImageEnum.TILEGRASS.getHeight() * this.yDir) / this.movSize;
				//System.out.println("c");
			} else {
				this.xLoc = path.get(path.size() - 1).getX();
				this.yLoc = path.get(path.size() - 1).getY();
				path.remove(path.size() - 1);
				this.xOff = 0;
				this.yOff = 0;
				if (target != null) {
					target[2] += xDir * ImageEnum.TILEGRASS.getHeight();
					target[3] += yDir * ImageEnum.TILEGRASS.getWidth();
				}
				this.xDir = 0;
				this.yDir = 0;
				System.out.println("I moved to " + this.xLoc + ", " + this.yLoc
						);
				//System.out.println("d");
				//System.out.println("moved to " + xLoc + ", " + yLoc);
				updateState = -1;
			}
		} else if ((target != null) && doInteract) {// && (updateState == 1)) {
			interact();
			updateState = 0;
		} else if ((pickItem > 0) && (target != null)) {
			pickUp(pickItem, target);
		}
	}
	
	public void damage(int dmg) {
		this.currHealth -= dmg;
		if (this.currHealth < 0) {
			this.currHealth = 0;
		}
		Game.printText(chatbox, "Health is now: " + this.currHealth + "\n", Color.GRAY);
	}
	
	public void attack(Enemy e) {
		if (this.currHealth > 0) {
			this.inCombatWith = e;
			if ((e != null) && (checkAdjacency(false, false, new int[] {xLoc, yLoc}, new int[] {e.getXLoc(), e.getYLoc()}))) {
				//System.out.println("try to attack");
				if ((e.getCurrHealth() > 0) && (System.currentTimeMillis() - this.getAttackTime() > this.getAttackSpeed()*100)) {
					e.damage(this.damage);
					if (e.getInCombatWith() == null) {
						e.attack(this);
					}
					if (this.getInCombatWith().getCurrHealth() <= 0) {
						this.inCombatWith = null;
					}
					this.attackTime = System.currentTimeMillis();
				}
			} else if ((e != null) && (path.size() == 0)) {
				int tmpX;
				int tmpY;
				if ((xLoc == e.getXLoc()) && (yLoc == e.getYLoc()) && (System.currentTimeMillis() - this.getAttackTime() > this.getAttackSpeed()*100)) {
					if ((state.mapTiles[e.getXLoc() + 1][e.getYLoc()] == 1) && (state.objTiles[e.getXLoc() + 1][e.getYLoc()] == 0)) {
						tmpX = e.getXLoc() + 1;
						tmpY = e.getYLoc();
					} else if ((state.mapTiles[e.getXLoc() - 1][e.getYLoc()] == 1) && (state.objTiles[e.getXLoc() - 1][e.getYLoc()] == 0)) {
						tmpX = e.getXLoc() - 1;
						tmpY = e.getYLoc();
					} else if ((state.mapTiles[e.getXLoc()][e.getYLoc() + 1] == 1) && (state.objTiles[e.getXLoc()][e.getYLoc() + 1] == 0)) {
						tmpX = e.getXLoc();
						tmpY = e.getYLoc() + 1;
					} else if ((state.mapTiles[e.getXLoc()][e.getYLoc() - 1] == 1) && (state.objTiles[e.getXLoc()][e.getYLoc() - 1] == 0)) {
						tmpX = e.getXLoc();
						tmpY = e.getYLoc() - 1;
					} else {
						tmpX = e.getXLoc() + 1;
						tmpY = e.getYLoc();
					}
				} else {
					System.out.println("elseded");
					tmpX = e.getXLoc();
					tmpY = e.getYLoc();
				}
				//System.out.println("GOTO: " + tmpX + ", " + tmpY + " from " + xLoc + ", " + yLoc);
				this.attackTime = System.currentTimeMillis();
				moveTo(tmpX, tmpY, false, (tmpX - this.getXLoc() + ((state.boardColsNum - 3) / 2)) * ImageEnum.TILEGRASS.getWidth(), (tmpY - this.getYLoc() + ((state.boardRowsNum - 3) / 2)) * ImageEnum.TILEGRASS.getHeight(), false);
			}
		}
	}
	
	public void pickUp(int item, int[] tmp) {
		if ((xLoc == tmp[0]) && (yLoc == tmp[1])) {
			for (int i = 0; i < state.itemTiles[tmp[0]][tmp[1]].size(); i++) {
				if (state.itemTiles[tmp[0]][tmp[1]].get(i)[0] == item) {
					if (inv.addItem(state.itemTiles[tmp[0]][tmp[1]].get(i)[0], state.itemTiles[tmp[0]][tmp[1]].get(i)[1] == 1, state.itemTiles[tmp[0]][tmp[1]].get(i)[2])) {
						state.itemTiles[tmp[0]][tmp[1]].remove(state.itemTiles[tmp[0]][tmp[1]].get(i));
					} else {
						Game.printText(chatbox, "Your inventory is full.\n", Color.GRAY);
					}
					pickItem = 0;
					return;
				}
			}
		} else {
			moveTo(tmp[0], tmp[1], tmp[2] == 1, tmp[3], tmp[4], false);
			pickItem = item;
		}
	}
	
	public boolean dropSlot(int slot) {
		int item = inv.getSlot(slot);
		if (item != 0) {
			if (state.itemTiles[xLoc][yLoc] == null) {
				state.itemTiles[xLoc][yLoc] = new ArrayList<Integer[]>();
			}
			state.itemTiles[xLoc][yLoc].add(new Integer[] {item, inv.getInventory().get(slot).getIsNoted() ? 1 : 0, inv.getInventory().get(slot).getCount()});
			state.itemTiles[xLoc][yLoc].sort(null);
		}
		return (inv.dropSlot(slot));
	}
	
	public int getInventorySlot(int slot) {
		return inv.getSlot(slot);
	}
	
	public String toString() {
		String s = this.username + " " + this.password + " " + this.xLoc + " " + this.yLoc + " ";
		for (int i = 0; i < Inventory.getInventorySize(); i++) {
			s += inv.getSlot(i) + " ";
		}
		for (int i = 0; i < Inventory.getInventorySize(); i++) {
			s += inv.getInventory().get(i).getIsNoted() + " ";
		}
		for (int i = 0; i < Inventory.getInventorySize(); i++) {
			s += inv.getInventory().get(i).getCount() + " ";
		}
		for (int j = 0; j < skillNum; j++) {
			s += statXP[j] + " ";
		}
		return s;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getXLoc() {
		return this.xLoc;
	}
	
	public int getYLoc() {
		return this.yLoc;
	}
	
	public int getXOff() {
		return this.xOff;
	}
	
	public int getYOff() {
		return this.yOff;
	}
	
	public Inventory getInv() {
		return this.inv;
	}
	
	public Bank getBank() {
		return this.bank;
	}
	
	public int getState(int n) {
		return this.statXP[n];
	}
	
	public void setState(BoardState state) {
		this.state = state;
	}
	
	public boolean getInteract() {
		return doInteract;
	}
	
	public void setInteract(boolean b) {
		doInteract = b;
	}
	
	public JTextPane getChatbox() {
		return this.chatbox;
	}
	
	public void setUpdateState(int i) {
		this.updateState = i;
	}
	
	public int getUpdateState() {
		return this.updateState;
	}
	
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	public int getCurrHealth() {
		return this.currHealth;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public int getAttackSpeed() {
		return this.attackSpeed;
	}
	
	public Enemy getInCombatWith() {
		return this.inCombatWith;
	}
	
	public void setInCombatWith(Enemy e) {
		this.inCombatWith = e;
	}
	
	public double getAttackTime() {
		return this.attackTime;
	}
	
	public void setAttackTime(double t) {
		this.attackTime = t;
	}
	
	public boolean getAutoRetaliate() {
		return this.autoRetaliate;
	}
	
	public void setAutoRetaliate(boolean b) {
		this.autoRetaliate = b;
	}
}