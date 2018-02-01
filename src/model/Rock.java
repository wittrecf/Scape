package model;

import view.Image;

public enum Rock {
    CLAY (10, 20, 10, 10);

    private int level;
    private double xp;
    private int oreId;
    private int baseTime;
    
    Rock(int level, double xp, int oreId, int baseTime) {
        this.level = level;
        this.xp = xp;
        this.oreId = oreId;
        this.baseTime = baseTime;
    }

	public int getLevel() {
		return level;
	}

	public double getXp() {
		return xp;
	}

	public int getOreId() {
		return oreId;
	}

	public int getBaseTime() {
		return baseTime;
	}
}