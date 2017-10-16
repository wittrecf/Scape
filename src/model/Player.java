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

public class Player {
	private String username;
	private String password;
	private int xLoc;
	private int yLoc;
	private int[] inventory = new int[28];
	private ArrayList<Node> path;
	
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
	private final int[] DEFAULT_INVENTORY = {-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1};
	private final static int INVENTORY_SIZE = 28;
	
	public Player(String user) {
		makePassword();
		this.username = user;
		this.xLoc = DEFAULTX;
		this.yLoc = DEFAULTY;
		this.inventory = DEFAULT_INVENTORY;
		this.path = new ArrayList<Node>();
		try {
			writePlayerData();
			Files.write(Paths.get(userFile), (this.username + System.getProperty("line.separator")).getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Player(String[] arr) {
		this.username = arr[0];
		this.password = arr[1];
		this.xLoc = Integer.parseInt(arr[2]);
		this.yLoc = Integer.parseInt(arr[3]);
		for (int i = 1; i <= Player.INVENTORY_SIZE; i++) {
			this.inventory[i - 1] = Integer.parseInt(arr[3 + i]);
		}
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
	
	public static Player loadPlayer(String user) throws FileNotFoundException, UnsupportedEncodingException {
		Scanner inFile = null;
		String[] playerArr = new String[Player.INVENTORY_SIZE + 4];
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
				for (int i = 1; i <= Player.INVENTORY_SIZE; i++) {
					playerArr[3 + i] = arr[3 + i];
				}
				return new Player(playerArr);
			}
		}
		return null;
	}
	
	public static Player makeAccount(String user) {
		if (Player.searchUser(user)) {
			System.out.println("Username already taken.");
			return null;
		} else {
			return new Player(user);
		}
	}
	
	private ArrayList<Node> createNodeMap(int len, BoardState state) {
		//System.out.println("L is " + len);
		Node[][] arr = new Node[(int) (2*len + 1)][(int) (2*len + 1)];
		ArrayList<Node> list = new ArrayList<Node>();
		Node n;
		//System.out.println("i spans from " + (this.getXLoc() - len) + " to " + (this.getXLoc() + len));
		//System.out.println("j spans from " + (this.getYLoc() - len) + " to " + (this.getYLoc() + len));
		for (int i = this.getXLoc() - len; i <= this.getXLoc() + len; i++) {
			for (int j = this.getYLoc() - len; j <= this.getYLoc() + len; j++) {
				if (!((i < 0) || (i >= state.mapColsNum) || (j < 0) || (j >= state.mapRowsNum))) {
					if (state.mapTiles[i][j] == 1) {
						n = new Node(i, j);
						if ((i == this.getXLoc()) && (j == this.getYLoc())) {
							n.setDist(0);
						}
					} else {
						n = null;
					}
					arr[i - (this.getXLoc() - len)][j - (this.getYLoc() - len)] = n;
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
						if ((x - (this.getXLoc() - len) >= 0) && (x - (this.getXLoc() - len) < 2*len) &&
							(y - (this.getYLoc() - len) >= 0) && (y - (this.getYLoc() - len) < 2*len) &&
							!(arr[x - (this.getXLoc() - len)][y - (this.getYLoc() - len)] == null)) {
							n1.addConnection(arr[x - (this.getXLoc() - len)][y - (this.getYLoc() - len)]);
							//System.out.println("adding connection from " + n1.getX() + ", " + n1.getY() + " and " + arr[x - (this.getXLoc() - len)][y - (this.getYLoc() - len)].getX() + ", " + arr[x - (this.getXLoc() - len)][y - (this.getYLoc() - len)].getY());

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
	
	private Node findPath(int x, int y, BoardState state) {
		int length = 20;
		
		//System.out.println("chunk 1 done");
		
		ArrayList<Node> list = createNodeMap(length, state);
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
	
	public void moveTo(int x, int y, BoardState state) {
		if (path.size() > 0) {
			path = new ArrayList<Node>();
		}
		for (int j = 0; j < state.mapRowsNum; j++) {
    		for (int i = 0; i < state.mapColsNum; i++) {
				if (state.mapTiles[i][j] == 3) {
					state.mapTiles[i][j] = 1;
				}
			}
		}
		Node n = findPath(x, y, state);
		while (true) {
			//System.out.println(n.getX() + ", " + n.getY());
			//System.out.println("dist " + n.getDist());
			if (n != null) {
				state.mapTiles[n.getX()][n.getY()] = 3;
				if (n.getPrev() != null) {
					path.add(n);
					n = n.getPrev();
				} else {
					break;
				}
			}
		}
	}
	
	public void move() {
		if (path.size() > 0) {
			this.xLoc = path.get(path.size() - 1).getX();
			this.yLoc = path.get(path.size() - 1).getY();
			path.remove(path.size() - 1);
		}
	}
	
	public int getInventorySlot(int slot) {
		return inventory[slot];
	}
	
	public String toString() {
		String s = this.username + " " + this.password + " " + this.xLoc + " " + this.yLoc + " ";
		for (int i = 0; i < inventory.length; i++) {
			s += inventory[i] + " ";
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
}