package model;

public abstract class TileObject {
	private int xLoc;
	private int yLoc;
	
	public void setXYLoc(int x, int y) {
		xLoc = x;
		yLoc = y;
	}
	
	public void start(){
	}
	
	public int getXLoc(){
		return xLoc;
	}
	
	public int getYLoc(){
		return yLoc;
	}
}