package model;

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
    private int level() {return level;}
    private double xp() {return xp;}
    private int ordId() {return oreId;}
    private int baseTime() {return baseTime;}
}