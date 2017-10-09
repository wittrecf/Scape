package model;

public class Board {
	
	private int worldWidth;
    private int worldHeight;
    
    private final static int defaultWidth = 900;
    private final static int defaultHeight = 500;
    
    public Board() {
    	this(defaultWidth, defaultHeight);
    }
    
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
