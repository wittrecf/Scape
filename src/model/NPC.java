package model;

import java.awt.image.BufferedImage;

public class NPC {
	private boolean isTalkable;
	private String name;
	private BufferedImage img;
	private int id;
	private static int npcCount = 0;
	private int xLoc;
	private int yLoc;
	
	public NPC(String s, BufferedImage i, int x, int y) {
		this.name = s;
		this.img = i;
		this.id = npcCount;
		this.xLoc = x;
		this.yLoc = y;
		NPC.npcCount++;
	}

	public boolean getTalkable() {
		return isTalkable;
	}

	public void setTalkable(boolean isTalkable) {
		this.isTalkable = isTalkable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BufferedImage getImg() {
		return img;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getXLoc() {
		return this.xLoc;
	}
	
	public int getYLoc() {
		return this.yLoc;
	}
}
