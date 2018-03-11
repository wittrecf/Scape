package model;

import java.awt.image.BufferedImage;

import view.ImageEnum;

public enum Rock {
	NULLCLAY (10, "Clay", 10, 5, 10, 1.0/400.0, ImageEnum.ROCKNULL.getImg()),
    CLAY (11, "Clay", 10, 5, 10, 1.0/400.0, ImageEnum.ROCKCLAY.getImg());

	private int rockId;
	private String rockName;
    private int level;
    private int xp;
    private int oreId;
    private double respawnRate;
    private BufferedImage img; 
    
    Rock(int rockId, String rockName, int level, int xp, int oreId, double respawnRate, BufferedImage img) {
    	this.rockId = rockId;
    	this.rockName = rockName;
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
	
	public String getRockName() {
		return rockName;
	}
	
	public int getRockId() {
		return this.rockId;
	}
}