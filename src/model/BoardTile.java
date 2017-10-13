package model;

import view.Image;

public class BoardTile {
	private int xLoc;
	private int yLoc;
	private int xCoord;
	private int yCoord;
	private int type;

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
	
	public int getXCoord() {
		return xCoord;
	}

	public void setXCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getYCoord() {
		return yCoord;
	}

	public void setYCoord(int yCoord) {
		this.yCoord = yCoord;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
}