package model;

import java.awt.image.BufferedImage;

import view.ImageEnum;

public enum Tree {
	NULLOAK (50, "Oak tree", 10, 25, 50, 1.0/400.0, ImageEnum.TREENULL.getImg()),
    OAK (51, "Oak tree", 10, 25, 50, 1.0/400.0, ImageEnum.TREEOAK.getImg());

	private int treeId;
	private String treeName;
    private int level;
    private int xp;
    private int logId;
    private double respawnRate;
    private BufferedImage img;
    
    Tree(int treeId, String treeName, int level, int xp, int logId, double respawnRate, BufferedImage img) {
    	this.treeId = treeId;
    	this.treeName = treeName;
        this.level = level;
        this.xp = xp;
        this.logId = logId;
        this.respawnRate = respawnRate;
        this.img = img;
    }

	public int getLevel() {
		return this.level;
	}

	public int getXp() {
		return this.xp;
	}

	public int getLogId() {
		return this.logId;
	}

	public double getRespawnRate() {
		return this.respawnRate;
	}
	
	public BufferedImage getImg() {
		return this.img;
	}
	
	public String getTreeName() {
		return treeName;
	}
	
	public int getTreeId() {
		return this.treeId;
	}
}