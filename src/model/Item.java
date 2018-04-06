package model;

import java.awt.image.BufferedImage;

import view.ImageEnum;

public enum Item {
	NULL (0, "", ImageEnum.getIcons()[0]),
    CLAYORE (10, "Clay ore", ImageEnum.getIcons()[10]),
	OAKLOG (50, "Oak logs", ImageEnum.getIcons()[50]),
	RAWSHRIMP (90, "Raw shrimp", ImageEnum.getIcons()[90]);

	private int itemId;
    private String itemName;
    private BufferedImage[] img; 
    
    Item(int itemId, String itemName, BufferedImage[] img) {
    	this.itemId = itemId;
    	this.itemName = itemName;
        this.img = img;
    }
    
    public static Item getItemById(int id) {
    	switch (id) {
    	case 0: return NULL;
    	case 10: return CLAYORE;
    	case 50: return OAKLOG;
    	case 90: return RAWSHRIMP;
    	default: return null;
    	}
    }
    
    public static Item getItemByName(String name) {
    	if (name.equals(NULL.itemName)) {
    		return NULL;
    	} else if (name.equals(CLAYORE.itemName)) {
    		return CLAYORE;
    	} else if (name.equals(OAKLOG.itemName)) {
    		return OAKLOG;
    	} else if (name.equals(RAWSHRIMP.itemName)) {
    		return RAWSHRIMP;
    	} else {
    		return null;
    	}
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