package model;

public class TileFishingSpot extends TileObject {
	private FishingSpot fishingSpotType;
	private int oldType;
	private double time;
	
	public TileFishingSpot(int x, int y, double type) {
		setXYLoc(x, y);
		this.time = type - Math.floor(type);
		this.oldType = (int) Math.floor(type);
		this.fishingSpotType = pickFishingSpot(oldType);
		this.setType("TileFishingSpot");
		depleted = "These waters have no fish anymore.";
		started = "Fishing...";
		xp = fishingSpotType.getXp();
	}
	
	public static FishingSpot pickFishingSpot(int type) {
		switch (type) {
		case 90: return FishingSpot.NULLSHRIMP;
		case 91: return FishingSpot.SHRIMP;
		default: return null;
		}
	}
	
	public int[] start(BoardState state) {
		if (this.fishingSpotType.getFishingSpotId() % 2 == 1) {
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
		this.fishingSpotType = pickFishingSpot(oldType - 1);
		state.objTiles[this.getXLoc()][this.getYLoc()] = this.fishingSpotType.getFishingSpotId();
	}
	
	public FishingSpot getFishingSpotType() {
		return this.fishingSpotType;
	}
	
	public int getItem() {
		return fishingSpotType.getFishId();
	}
}