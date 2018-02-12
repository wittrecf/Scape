package model;

public abstract class TileObject {
	private int xLoc;
	private int yLoc;
	private String type;
	protected String depleted;
	protected String started;
	protected int xp;
	
	public void setXYLoc(int x, int y) {
		xLoc = x;
		yLoc = y;
	}
	
	public int[] start(BoardState state){
		return null;
	}
	
	public int getXLoc(){
		return xLoc;
	}
	
	public int getYLoc(){
		return yLoc;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String t) {
		this.type = t;
	}
	
	public int getItem() {
		return -1;
	}
	
	public String getDepleted() {
		return depleted;
	}
	
	public String getStarted() {
		return started;
	}
	
	public int getXp() {
		return xp;
	}
}