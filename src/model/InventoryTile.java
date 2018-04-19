package model;

import java.awt.image.BufferedImage;

import view.ImageEnum;

public class InventoryTile {
	private Item itemType;
	private int xLoc;
	private int yLoc;
	private int item;
	private BufferedImage currImg;
	private boolean isHighlighted;
	private boolean isNoted;
	private int count;

	private static int width = ImageEnum.ICONBLANK.getWidth();
	private static int height = ImageEnum.ICONBLANK.getHeight();
	
	public InventoryTile(int xLoc, int yLoc){
		this.itemType = pickItem(item);
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.item = 0;
		this.currImg = itemType.getImg()[0];
		this.isHighlighted = false;
	}
	
	public static Item pickItem(int id) {
		switch (id) {
		case 0: return Item.NULL;
		case 10: return Item.CLAYORE;
		case 50: return Item.OAKLOG;
		case 90: return Item.RAWSHRIMP;
		default: return null;
		}
	}
	
	public void highlightTile() {
		isHighlighted = !isHighlighted;
		if (isHighlighted) {
			this.currImg = itemType.getImg()[1];
		} else {
			this.currImg = itemType.getImg()[0];
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
	
	public void setItem(int itemId, boolean noted, int amount) {
		this.item = itemId;
		this.itemType = pickItem(itemId);
		this.isNoted = noted;
		this.count = amount;
		if (noted) {
			this.currImg = itemType.getImg()[0];
		} else {
			this.currImg = itemType.getImg()[0];
		}
	}
	
	public BufferedImage getItemImg() {
		return currImg;
	}
	
	public void setCount(int c) {
		this.count = c;
		if (this.count <= 0) {
			this.setItem(0, false, 0);
		}
	}
	
	public int getCount() {
		return this.count;
	}
	
	public boolean getIsNoted() {
		return this.isNoted;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
}