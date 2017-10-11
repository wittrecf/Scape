package model;

public class Board {
	
	private int worldWidth;
    private int worldHeight;
    
    public Board(int x, int y) {
    	this.worldWidth = x;
    	this.worldHeight = y;
    }
    
	public int getWidth() {
		return worldWidth;
	}

	public int getHeight() {
		return worldHeight;
	}  	
}
