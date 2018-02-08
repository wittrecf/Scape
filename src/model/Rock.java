package model;

import java.awt.image.BufferedImage;

import view.ImageEnum;

public enum Rock {
	NULLCLAY (10, 10, 20, 10, .0025, ImageEnum.ROCKNULL),
    CLAY (11, 10, 20, 10, .0025, ImageEnum.ROCKCLAY);

	private int rockId;
    private int level;
    private double xp;
    private int oreId;
    private double respawnRate;
    private ImageEnum img;
    
    Rock(int rockId, int level, double xp, int oreId, double respawnRate, ImageEnum img) {
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

	public double getXp() {
		return this.xp;
	}

	public int getOreId() {
		return this.oreId;
	}

	public double getRespawnRate() {
		return this.respawnRate;
	}
	
	public ImageEnum getImg() {
		return this.img;
	}
	
	public int getRockId() {
		return this.rockId;
	}
}