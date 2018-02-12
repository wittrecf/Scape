package model;

import java.awt.image.BufferedImage;

import view.ImageEnum;

public enum Rock {
	NULLCLAY (10, 10, 5, 10, 1.0/400.0, ImageEnum.ROCKNULL.getImg()),
    CLAY (11, 10, 5, 10, 1.0/400.0, ImageEnum.ROCKCLAY.getImg());

	private int rockId;
    private int level;
    private int xp;
    private int oreId;
    private double respawnRate;
    private BufferedImage img; 
    
    Rock(int rockId, int level, int xp, int oreId, double respawnRate, BufferedImage img) {
    	this.rockId = rockId;
        this.level = level;
        this.xp = xp;
        this.oreId = oreId;
        this.respawnRate = respawnRate;
        this.img = img;
    }

	public int getLevel() {
		return this.level;
	}

	public int getXp() {
		return this.xp;
	}

	public int getOreId() {
		return this.oreId;
	}

	public double getRespawnRate() {
		return this.respawnRate;
	}
	
	public BufferedImage getImg() {
		return this.img;
	}
	
	public int getRockId() {
		return this.rockId;
	}
}