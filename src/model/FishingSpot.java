package model;

import java.awt.image.BufferedImage;

import view.ImageEnum;

public enum FishingSpot {
	NULLSHRIMP (90, "Shrimp", 10, 5, 90, 1.0/400.0, ImageEnum.FISHINGSPOTNULL.getImg()),
    SHRIMP (91, "Shrimp", 10, 5, 90, 1.0/400.0, ImageEnum.FISHINGSPOTSHRIMP.getImg());

	private int FishingSpotId;
	private String FishingSpotName;
    private int level;
    private int xp;
    private int fishId;
    private double respawnRate;
    private BufferedImage img; 
    
    FishingSpot(int FishingSpotId, String FishingSpotName, int level, int xp, int fishId, double respawnRate, BufferedImage img) {
    	this.FishingSpotId = FishingSpotId;
    	this.FishingSpotName = FishingSpotName;
        this.level = level;
        this.xp = xp;
        this.fishId =fishId;
        this.respawnRate = respawnRate;
        this.img = img;
    }

	public int getLevel() {
		return this.level;
	}

	public int getXp() {
		return this.xp;
	}

	public int getFishId() {
		return this.fishId;
	}

	public double getRespawnRate() {
		return this.respawnRate;
	}
	
	public BufferedImage getImg() {
		return this.img;
	}
	
	public String getFishingSpotName() {
		return FishingSpotName;
	}
	
	public int getFishingSpotId() {
		return this.FishingSpotId;
	}
}