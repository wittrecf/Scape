package model;

public abstract class TileObject {
	private int xLoc;
	private int yLoc;
	private String type;
	
	public void setXYLoc(int x, int y) {
		xLoc = x;
		yLoc = y;
	}
	
	public void start(BoardState state){
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
}