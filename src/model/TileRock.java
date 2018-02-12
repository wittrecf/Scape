package model;

public class TileRock extends TileObject {
	private Rock rockType;
	private int oldType;
	private double time;
	
	public TileRock(int x, int y, double type) {
		setXYLoc(x, y);
		this.time = type - Math.floor(type);
		this.oldType = (int) Math.floor(type);
		this.rockType = pickRock(oldType);
		this.setType("TileRock");
		depleted = "This rock has no ore left.";
		started = "Mining...";
		xp = rockType.getXp();
	}
	
	public static Rock pickRock(int type) {
		switch (type) {
		case 10: return Rock.NULLCLAY;
		case 11: return Rock.CLAY;
		default: return null;
		}
	}
	
	public int[] start(BoardState state) {
		if (this.rockType.getRockId() % 2 == 1) {
			if (true) {
				this.deplete(state);
				int[] x = {getItem(), xp};
				return x;
			}
			return null;
		} else {
			return null;
		}
	}
	
	private void deplete(BoardState state) {
		this.rockType = pickRock(oldType - 1);
		state.mapTiles[this.getXLoc()][this.getYLoc()] = this.rockType.getRockId();
	}
	
	public Rock getRockType() {
		return this.rockType;
	}
	
	public int getItem() {
		return rockType.getOreId();
	}
}