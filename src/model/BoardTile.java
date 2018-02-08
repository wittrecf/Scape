package model;

import view.ImageEnum;

public class BoardTile {
	private int xLoc;
	private int yLoc;
	private int xCoord;
	private int yCoord;
	private double type;
	private TileObject obj;

	private static int width = ImageEnum.TILEGRASS.getWidth();
	private static int height = ImageEnum.TILEGRASS.getHeight();
	
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
	
	public double getType() {
		return type;
	}
	
	public void setType(double mapTiles) {
		this.type = mapTiles;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public TileObject getObj() {
		return obj;
	}

	public void setObj(TileObject obj) {
		this.obj = obj;
	}
}