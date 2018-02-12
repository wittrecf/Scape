package model;

import java.awt.image.BufferedImage;

import view.ImageEnum;

public class InventoryTile {
	private int xLoc;
	private int yLoc;
	private int item;
	private BufferedImage itemImg;
	private boolean isHighlighted;

	private static int width = ImageEnum.ICONBLANK.getWidth();
	private static int height = ImageEnum.ICONBLANK.getHeight();
	
	public InventoryTile(int xLoc, int yLoc, int item){
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.item = item;
		this.itemImg = ImageEnum.getIcons()[item][0];
		this.isHighlighted = false;
	}
	
	public void highlightTile() {
		isHighlighted = !isHighlighted;
		if (isHighlighted) {
			this.itemImg = ImageEnum.getIcons()[item][1];
		} else {
			this.itemImg = ImageEnum.getIcons()[item][0];
		}
	}
	
	public int getXLoc() {
		return xLoc;
	}

	public int getYLoc() {
		return yLoc;
	}
	
	public int getItem() {
		return item;
	}
	
	public void setItem(int itemID) {
		this.item = itemID;
		this.itemImg = ImageEnum.getIcons()[itemID][0];
	}
	
	public BufferedImage getItemImg() {
		return itemImg;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
}