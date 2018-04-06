package model;

import java.awt.image.BufferedImage;

public class NPC {
	private boolean isTalkable;
	private String name;
	private BufferedImage img;
	private int id;
	private static int npcCount = 0;
	
	public NPC(String s, BufferedImage i) {
		this.name = s;
		this.img = i;
		this.id = npcCount;
		this.npcCount++;
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
}
