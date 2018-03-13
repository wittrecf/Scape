package model;

public class TileTree extends TileObject {
	private Tree treeType;
	private int oldType;
	private double time;
	private boolean storedItem;
	private boolean storedXp;
	
	public TileTree(int x, int y, double type) {
		setXYLoc(x, y);
		this.time = type - Math.floor(type);
		this.oldType = (int) Math.floor(type);
		this.treeType = pickTree(oldType);
		this.setType("TileTree");
		depleted = "This tree has no logs left.";
		started = "Cutting...";
		xp = treeType.getXp();
	}
	
	public static Tree pickTree(int type) {
		switch (type) {
		case 50: return Tree.NULLOAK;
		case 51: return Tree.OAK;
		default: return null;
		}
	}
	
	public int[] start(BoardState state) {
		if (this.treeType.getTreeId() % 2 == 1) {
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
		this.treeType = pickTree(oldType - 1);
		state.objTiles[this.getXLoc()][this.getYLoc()] = this.treeType.getTreeId();
	}
	
	public Tree getTreeType() {
		return this.treeType;
	}
	
	public int getItem() {
		return treeType.getLogId();
	}
}