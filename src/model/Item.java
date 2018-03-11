package model;

import java.awt.image.BufferedImage;

import view.ImageEnum;

public enum Item {
	NULL (0, "", ImageEnum.getIcons()[0]),
    CLAYORE (10, "Clay ore", ImageEnum.getIcons()[10]),
	OAKLOG (50, "Oak logs", ImageEnum.getIcons()[50]);

	private int itemId;
    private String itemName;
    private BufferedImage[] img; 
    
    Item(int itemId, String itemName, BufferedImage[] img) {
    	this.itemId = itemId;
    	this.itemName = itemName;
        this.img = img;
    }
    
	public int getItemId() {
		return itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public BufferedImage[] getImg() {
		return img;
	}
}