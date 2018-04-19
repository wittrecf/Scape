package model;

import java.awt.image.BufferedImage;

import view.ImageEnum;

public class BankTile {
	private Item itemType;
	private int xLoc;
	private int yLoc;
	private int item;
	private int count;
	private BufferedImage currImg;

	private static int width = ImageEnum.ICONBLANK.getWidth();
	private static int height = ImageEnum.ICONBLANK.getHeight();
	
	public BankTile(int xLoc, int yLoc, int item){
		this.itemType = pickItem(item);
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.item = item;
		this.currImg = itemType.getImg()[0];
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
	
	public void  decreaseCount(int amount) {
		this.count -= amount;
		if (this.count <= 0) {
			this.setItem(0);
		}
	}
	
	public int getXLoc() {
		return this.xLoc;
	}

	public int getYLoc() {
		return this.yLoc;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public int getItem() {
		return this.item;
	}
	
	public void setItem(int itemId) {
		this.item = itemId;
		this.itemType = pickItem(itemId);
		this.currImg = itemType.getImg()[0];
		this.count = 1;
	}
	
	public BufferedImage getItemImg() {
		return this.currImg;
	}

	public static int getWidth() {
		return BankTile.width;
	}

	public static int getHeight() {
		return BankTile.height;
	}
}