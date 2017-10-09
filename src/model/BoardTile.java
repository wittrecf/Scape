package model;

import view.Image;

public class BoardTile {
	private int xLoc;
	private int yLoc;
	private static int width = Image.TILEGRASS.getWidth();
	private static int height = Image.TILEGRASS.getHeight();
	
	public BoardTile(int xLoc, int yLoc){
		this.xLoc = xLoc;
		this.yLoc = yLoc;
	}
	
	public int getXLoc() {
		return xLoc;
	}

	public int getYLoc() {
		return yLoc;
	}
	
	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
}