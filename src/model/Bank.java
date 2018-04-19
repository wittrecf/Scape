package model;

import java.util.ArrayList;

public class Bank {
	private int bankWidth;
    private int bankHeight;
    private ArrayList<BankTile> bankSlots;
	private int bankSize;
	private int[][] items;
	private boolean withdrawNotes;
	
	public Bank(int w, int h, int size, int[][] arr) {
		this.bankWidth = w;
		this.bankHeight = h;
		this.bankSize = size;
		this.items = arr;
		this.bankSlots = new ArrayList<BankTile>();
		this.setDefaultBank();
	}
	
	public int findFirstSlot() {
    	for (int i = 0; i < bankSize; i++) {
    		if (bankSlots.get(i).getItem() == 0) {
    			return i;
    		}
    	}
    	return -1;
    }
	
	public int findExistingStack(int item) {
    	for (int i = 0; i < bankSize; i++) {
    		if (bankSlots.get(i).getItem() == item) {
    			return i;
    		}
    	}
    	return -1;
    }
	
	public boolean addItem(int itemID, int amount) {
    	int x = findExistingStack(itemID);
    	System.out.println("adding..");
    	if (x != -1) {
    		bankSlots.get(x).decreaseCount(-amount);
    		return true;
    	} else {
    		x = findFirstSlot();
    		if (x != -1) {
    			bankSlots.get(x).setItem(itemID);
    			bankSlots.get(x).decreaseCount(-amount + 1);
        		return true;
    		} else {
    			return false;
    		}
    	}
    }
	
	public void setDefaultBank() {
		for (int j = 0; j < this.bankHeight; j++) {
			for (int i = 0; i < this.bankWidth; i++) {
				this.bankSlots.add(new BankTile(i, j, 0));
			}
		}
	}
	
	public void setSlot(int slotNum, int item) {
    	bankSlots.get(slotNum).setItem(item);
    }
    
    public int getSlot(int slotNum) {
    	return bankSlots.get(slotNum).getItem();
    }

	public int getBankWidth() {
		return bankWidth;
	}

	public int getBankHeight() {
		return bankHeight;
	}

	public int getBankSize() {
		return bankSize;
	}
	
	public ArrayList<BankTile> getBank() {
		return this.bankSlots;
	}
	
	public void setWithdrawNotes(boolean b) {
		this.withdrawNotes = b;
	}
	
	public boolean getWithdrawNotes() {
		return this.withdrawNotes;
	}
}
