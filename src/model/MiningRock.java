package model;

public class MiningRock extends TileObject {
	private Rock rockType;
	private int oldType;
	private double time;
	
	public MiningRock(int x, int y, double type) {
		setXYLoc(x, y);
		this.time = type - Math.floor(type);
		this.oldType = (int) Math.floor(type);
		this.rockType = pickRock(oldType);
		this.setType("MiningRock");
	}
	
	public static Rock pickRock(int type) {
		switch (type) {
		case 10: return Rock.NULLCLAY;
		case 11: return Rock.CLAY;
		default: return null;
		}
	}
	
	public void start(BoardState state) {
		if (this.rockType.getRockId() % 2 == 1) {
			this.deplete(state);
		} else {
			System.out.println("rock is depleted");
		}
	}
	
	private void deplete(BoardState state) {
		this.rockType = pickRock(oldType - 1);
		state.mapTiles[this.getXLoc()][this.getYLoc()] = this.rockType.getRockId();
	}
	
	public Rock getRockType() {
		return this.rockType;
	}
}