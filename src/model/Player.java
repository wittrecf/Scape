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

public class Player {
	private String username;
	private String password;
	private int xLoc;
	private int yLoc;
	private int[] inventory = new int[28];
	
	private String userFile = "resources/userFile";
	private String playerDataFile = "resources/playerDataFile";
	private final int DEFAULTX = 0;
	private final int DEFAULTY = 0;
	private final int[] DEFAULTINVENTORY = {-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1,
											-1, -1, -1, -1};
	
	private Player(String user) {
		makePassword();
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
}