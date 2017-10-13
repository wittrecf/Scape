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
	private Stack<int[]> path;
	private int[] inventory = new int[28];
	
	private int[][] dir = {{1, 1},
			   			   {1, -1},
			   			   {-1, 1},
			   			   {-1, -1},
			   			   {1, 0},
						   {0, 1},
						   {-1, 0},
						   {0, -1}};
	
	private String userFile = "resources/userFile";
	private String playerDataFile = "resources/playerDataFile";
	
	
	private final int DEFAULTX = 50;
	private final int DEFAULTY = 50;
	private final int[] DEFAULTINVENTORY = {-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1};
	
	public Player(String user) {
		//makePassword();
		xLoc = DEFAULTX;
		yLoc = DEFAULTY;
		inventory = DEFAULTINVENTORY;
		try {
			Files.write(Paths.get(playerDataFile), this.toString().getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				done = true;
			} else {
				System.out.println("Passwords do not match.");
			}
		}
	}
	
	private boolean searchUser(String u) {
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(userFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	    while (inFile.hasNext()) {
	    	if (inFile.next().toLowerCase().equals(u.toLowerCase())) {
	    		return true;
	    	}
	    }
	    inFile.close();
	    return false;
	}
	
	public void writePlayerData(Player player) throws FileNotFoundException, UnsupportedEncodingException {
		Scanner inFile = null;
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
			if (s.contains(player.getUsername())) {
				s = player.toString();
			}
			writer.println(s);
		}
		writer.close();
	}
	
	public Player makeAccount(String user) {
		if (searchUser(user)) {
			System.out.println("Username already taken.");
			return null;
		} else {
			return new Player(user);
		}
	}
	
	private ArrayList<Node> createNodeMap(int len, BoardState state) {
		System.out.println("L is " + len);
		Node[][] arr = new Node[(int) (2*len + 1)][(int) (2*len + 1)];
		System.out.println("arr size x: " + arr.length);
		System.out.println("arr size y: " + arr[0].length);
		ArrayList<Node> list = new ArrayList<Node>();
		Node n;
		System.out.println("i spans from " + (this.getXLoc() - len) + " to " + (this.getXLoc() + len));
		System.out.println("j spans from " + (this.getYLoc() - len) + " to " + (this.getYLoc() + len));
		for (int i = this.getXLoc() - len; i <= this.getXLoc() + len; i++) {
			for (int j = this.getYLoc() - len; j <= this.getYLoc() + len; j++) {
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
		
		int x;
		int y;
		
		for (Node[] ls : arr) {
			for (Node n1 : ls) {
				if (n1 != null) {
					System.out.println("trying out " + n1.getX() + ", " + n1.getY());
					for (int i = 0; i < dir.length; i++) {
						x = n1.getX() + dir[i][0];
						y = n1.getY() + dir[i][1];
						System.out.println(x + " / " + y);
						if ((x - (this.getXLoc() - len) >= 0) && (x - (this.getXLoc() - len) < 2*len) &&
							(y - (this.getYLoc() - len) >= 0) && (y - (this.getYLoc() - len) < 2*len) &&
							!(arr[x - (this.getXLoc() - len)][y - (this.getYLoc() - len)] == null)) {
							n1.addConnection(arr[x - (this.getXLoc() - len)][y - (this.getYLoc() - len)]);
							System.out.println("adding connection from " + n1.getX() + ", " + n1.getY() + " and " + arr[x - (this.getXLoc() - len)][y - (this.getYLoc() - len)].getX() + ", " + arr[x - (this.getXLoc() - len)][y - (this.getYLoc() - len)].getY());
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
		boolean isStart = false;
		int size;
		int length = 0;
		Stack<int[]> tmpPath = new Stack<int[]>();
		path = new Stack<int[]>();
		path.push(new int[]{x, y, 0});
		
		int level = 0;
		/*
		while (!isStart) {
			for (int[] p : path) {
				//System.out.println("at level " + level + ", we have " + p[0] + ", " + p[1]);
				if ((p[2] == level) && !isStart) {
					for (int i = 0; i < dir.length; i++) {
						int[] tmp = new int[3];
						tmp[0] = p[0] + dir[i][0];
						tmp[1] = p[1] + dir[i][1];
						tmp[2] = p[2] + 1;
						//System.out.println("basis: " + p[0] + ", " + p[1] + ", " + p[2]);
						//System.out.println("tmp: " + tmp[0] + ", " + tmp[1] + ", " + tmp[2]);
						if ((state.mapTiles[tmp[0]][tmp[1]] == 1) && !path.contains(tmp)) {
							tmpPath.push(tmp);
							//System.out.println("adding: " + tmp[0] + ", " + tmp[1]);
						}
						if ((tmp[0] == this.getXLoc()) && (tmp[1] == this.getYLoc())) {
							isStart = true;
							System.out.println("end");
							break;
						}
					}
				}
			}
			size = tmpPath.size();
			for (int i = 0; i < size; i++) {
				if ((i == 0) && isStart) {
					length = tmpPath.peek()[2];
				}
				path.push(tmpPath.pop());
				//System.out.println(path.peek()[0] + ",,, " + path.peek()[1] + ",,, " + path.peek()[2]);
			}
			level++;
		}*/
		
		length = 20;
		
		System.out.println("chunk 1 done");
		
		ArrayList<Node> list = createNodeMap(length, state);
		Node min;
		
		System.out.println("nodemap is length " + list.size());
		
		while (!list.isEmpty()) {
			min = new Node(0,0);
			min.setDist(999);
			for (Node n : list) {
				if (n.getDist() <= min.getDist()) {
					min = n;
				}
			}
			
			System.out.println("min is " + min.getX() + ", " + min.getY());
			
			list.remove(min);
			
			if ((min.getX() == x) && (min.getY() == y)) {
				return min;
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
		return null;
	}
	
	public void moveTo(int x, int y, BoardState state) {
		for (int j = 0; j < state.mapRowsNum; j++) {
    		for (int i = 0; i < state.mapColsNum; i++) {
				if (state.mapTiles[i][j] == 3) {
					state.mapTiles[i][j] = 1;
				}
			}
		}
		Node n = findPath(x, y, state);
		for (int i = 0; i < 10; i++) {
			System.out.println(n.getX() + ", " + n.getY());
			System.out.println("dist " + n.getDist());
			state.mapTiles[n.getX()][n.getY()] = 3;
			if (n.getPrev() != null) {
				n = n.getPrev();
			} else {
				break;
			}
		}
		this.xLoc = x;
		this.yLoc = y;
	}
	
	public int getInventorySlot(int slot) {
		return inventory[slot];
	}
	
	public String toString() {
		String s = this.username + " " + this.password + " " + this.xLoc + " " + this.yLoc;
		for (int i = 0; i < inventory.length; i++) {
			s += inventory[i];
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