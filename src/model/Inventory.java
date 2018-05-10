package model;

import java.util.ArrayList;

import view.Game;

public class Inventory {
	
	private int inventoryWidth;
    private int inventoryHeight;
    private ArrayList<InventoryTile> inventorySlots;
    private int highlightedSlot;
    private static final int INVENTORY_SIZE = 28;
    
    public Inventory(int x, int y) {
    	this.inventoryWidth = x;
    	this.inventoryHeight = y;
    	this.inventorySlots = new ArrayList<InventoryTile>();
    	this.highlightedSlot = -1;
    	this.setDefaultInventory();
    }
    
    public boolean searchInventorySpace() {
    	for (int i = 0; i < INVENTORY_SIZE; i++) {
    		if (inventorySlots.get(i).getItem() == 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public int findFirstSlot() {
    	for (int i = 0; i < INVENTORY_SIZE; i++) {
    		if (inventorySlots.get(i).getItem() == 0) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public int findExistingStack(int item) {
    	for (int i = 0; i < INVENTORY_SIZE; i++) {
    		if ((inventorySlots.get(i).getItem() == item) && (inventorySlots.get(i).getIsNoted())) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public boolean addItem(int itemID, boolean noted, int amount) {
    	int x = findExistingStack(itemID);
    	System.out.println("adding " + itemID);
    	if (noted && (x != -1)) {
    		inventorySlots.get(x).setCount(inventorySlots.get(x).getCount() + amount);
    		return true;
    	} else {
    		x = findFirstSlot();
    		if (x != -1) {
    			inventorySlots.get(x).setItem(itemID, noted, amount);
    			return true;
    		} else {
    			return false;
    		}
    	}
    }
    
    public boolean useItems(int slot1, int slot2) {
    	if (false) {
    		System.out.println("You use the two items on each other.");
    		return true;
    	} else {
    		System.out.println("Nothing interesting happens.");
    		return false;
    	}
    }
    
    public boolean highlightSlot(int slotNum) {
    	if (inventorySlots.get(slotNum).getItem() != 0) {
    		if (highlightedSlot == slotNum) {
    			inventorySlots.get(slotNum).highlightTile();
    			highlightedSlot = -1;
    		} else if (highlightedSlot != -1) {
    			useItems(highlightedSlot, slotNum);
    			inventorySlots.get(highlightedSlot).highlightTile();
    			highlightedSlot = -1;
    		} else {
    			inventorySlots.get(slotNum).highlightTile();
    			highlightedSlot = slotNum;
    		}
    		System.out.println("highlighted slot is now: " + highlightedSlot);
    		return true;
    	} else {
    		if (highlightedSlot != -1) {
    			inventorySlots.get(highlightedSlot).highlightTile();
    			highlightedSlot = -1;
    		}
    		return false;
    	}
    }
    
    public boolean decreaseSlot(int slotNum) {
    	if (inventorySlots.get(slotNum).getItem() != 0) {
    		inventorySlots.get(slotNum).setCount(inventorySlots.get(slotNum).getCount() - 1);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean dropSlot(int slotNum) {
    	if (inventorySlots.get(slotNum).getItem() != 0) {
    		if (highlightedSlot != -1) {
    			inventorySlots.get(highlightedSlot).highlightTile();
    			highlightedSlot = -1;
    		}
    		inventorySlots.get(slotNum).setItem(0, false, 0);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void swapSlots(int slot1, int slot2) {
    	InventoryTile tmp = inventorySlots.get(slot1);
    	int tmpItem = tmp.getItem();
    	boolean tmpNoted = tmp.getIsNoted();
    	int tmpCount = tmp.getCount();
    	this.setSlot(slot1, inventorySlots.get(slot2).getItem(), inventorySlots.get(slot2).getIsNoted(), inventorySlots.get(slot2).getCount());
    	this.setSlot(slot2, tmpItem, tmpNoted, tmpCount);
    }
    
    public void setSlot(int slotNum, int item, boolean noted, int amount) {
    	inventorySlots.get(slotNum).setItem(item, noted, amount);
    }
    
    public int getSlot(int slotNum) {
    	return inventorySlots.get(slotNum).getItem();
    }
    
	public int getWidth() {
		return inventoryWidth;
	}

	public int getHeight() {
		return inventoryHeight;
	}
	
	public void setDefaultInventory() {
		for (int j = 0; j < 7; j++) {
			for (int i = 0; i < 4; i++) {
				inventorySlots.add(new InventoryTile(i, j));
			}
		}
	}
	
	public ArrayList<InventoryTile> getInventory() {
		return this.inventorySlots;
	}
	
	public static int getInventorySize() {
		return INVENTORY_SIZE;
	}
	
}
