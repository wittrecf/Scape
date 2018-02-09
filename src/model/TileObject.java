package model;

public abstract class TileObject {
	private int xLoc;
	private int yLoc;
	private String type;
	protected static String depleted;
	protected static String started;
	
	public void setXYLoc(int x, int y) {
		xLoc = x;
		yLoc = y;
	}
	
	public boolean start(BoardState state){
		return false;
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
	
	public static String getDepleted() {
		return depleted;
	}
	
	public static String getStarted() {
		return started;
	}
}