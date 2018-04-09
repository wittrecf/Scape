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
    
    public boolean addItem(int itemID) {
    	int x = findFirstSlot();
    	System.out.println("adding..");
    	if (x != -1) {
    		inventorySlots.get(x).setItem(itemID);
    		return true;
    	} else {
    		return false;
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
    
    public boolean dropSlot(int slotNum) {
    	if (inventorySlots.get(slotNum).getItem() != 0) {
    		if (highlightedSlot != -1) {
    			inventorySlots.get(highlightedSlot).highlightTile();
    			highlightedSlot = -1;
    		}
    		inventorySlots.get(slotNum).setItem(0);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void setSlot(int slotNum, int item) {
    	inventorySlots.get(slotNum).setItem(item);
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
				inventorySlots.add(new InventoryTile(i, j, 0));
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
