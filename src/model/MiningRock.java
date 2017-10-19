package model;

public class MiningRock extends TileObject {
	private Rock rockType;
	
	public MiningRock(int x, int y, int type) {
		setXYLoc(x, y);
		this.rockType = pickRock(type);
	}
	
	private Rock pickRock(int type) {
		switch (type) {
		case 1: return Rock.CLAY;
		default: return null;
		}
	}
}