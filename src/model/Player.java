package model;

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
	private ArrayList<Node> path;
	private int[] target;
	private BoardState state;
	private boolean transfer = false;
	private boolean doInteract;
	private Inventory inv;
	
	private int[] statXP = new int[2];
	
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
	
	
	private final int DEFAULTX = 50;
	private final int DEFAULTY = 50;
	
	public Player(String user, int width, int height) {
		makePassword();
		this.username = user;
		this.xLoc = DEFAULTX;
		this.yLoc = DEFAULTY;
		this.inv = new Inventory(width, height);
		inv.setDefaultInventory();
		this.statXP[0] = 0;
		this.statXP[1] = 0;
		this.path = new ArrayList<Node>();
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
		for (int i = 0; i < Inventory.getInventorySize(); i++) {
			this.inv.setSlot(i, Integer.parseInt(arr[4 + i]));
		}
		for (int j = 0; j < 2; j++) {
			this.statXP[j] = Integer.parseInt(arr[4 + Inventory.getInventorySize() + j]);
		}
		this.target = null;
		this.path = new ArrayList<Node>();
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
		String[] playerArr = new String[4 + Inventory.getInventorySize() + 2];
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
				for (int j = 0; j < 2; j++) {
					playerArr[4 + Inventory.getInventorySize() + j] = arr[4 + Inventory.getInventorySize() + j];
				}
				return new Player(playerArr, width, height);
			}
		}
		return null;
	}
	
	public static Player makeAccount(String user, int width, int height) {
		if (Player.searchUser(user)) {
			System.out.println("Username already taken.");
			return null;
		} else {
			return new Player(user, width, height);
		}
	}
	
	private ArrayList<Node> createNodeMap(int len) {
		//System.out.println("L is " + len);
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
					if (state.mapTiles[i][j] == 1) {
						n = new Node(i, j);
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
		
		ArrayList<Node> list = createNodeMap(length);
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
		Node tmp = null;
		this.doInteract = interact;
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
			if ((n != null) && ((path.size() == 0) || (checkAdjacency(true, new int[] {path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY()}, new int[] {n.getX(), n.getY()})))) {
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
	
	private void findDir() {Node n = path.get(path.size() - 1);
		int x = this.xLoc - n.getX();
		int y = this.yLoc - n.getY();
		if (x != 0) {
			this.xDir = x / Math.abs(x);
		}
		if (y != 0) {
			this.yDir = y / Math.abs(y);
		}
	}
	
	private boolean checkAdjacency(boolean checkDiagonal, int[] t1, int[] t2) {
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
		}
	}
	
	public void interact(int x, int y, int[] t) {
		if (checkAdjacency(false, new int[] {xLoc, yLoc}, target)) {
			if (this.inv.searchInventorySpace()) {
				System.out.println(state.tiles.get(target[3]/ImageEnum.TILEGRASS.getWidth() + 1).get(target[2]/ImageEnum.TILEGRASS.getHeight() + 1).getObj().getStarted());
				int[] tmp = state.tiles.get(target[3]/ImageEnum.TILEGRASS.getWidth() + 1).get(target[2]/ImageEnum.TILEGRASS.getHeight() + 1).getObj().start(state);
				if (tmp != null) {
					inv.addItem(tmp[0]);
					addXP(state.tiles.get(target[3]/ImageEnum.TILEGRASS.getWidth() + 1).get(target[2]/ImageEnum.TILEGRASS.getHeight() + 1).getObj().getType(), tmp[1]);
					path.clear();
					target = null;
				} else {
					System.out.println(state.tiles.get(target[3]/ImageEnum.TILEGRASS.getWidth() + 1).get(target[2]/ImageEnum.TILEGRASS.getHeight() + 1).getObj().getDepleted());
					path.clear();
					target = null;
				}
			} else {
				System.out.println("Your inventory is full.");
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
				//System.out.println("d");
				//System.out.println("moved to " + xLoc + ", " + yLoc);
			}
		} else if ((target != null) && doInteract) {
			interact(xLoc, yLoc, target);
		}
	}
	
	public int getInventorySlot(int slot) {
		return inv.getSlot(slot);
	}
	
	public String toString() {
		String s = this.username + " " + this.password + " " + this.xLoc + " " + this.yLoc + " ";
		for (int i = 0; i < Inventory.getInventorySize(); i++) {
			s += inv.getSlot(i) + " ";
		}
		for (int j = 0; j < 2; j++) {
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
}