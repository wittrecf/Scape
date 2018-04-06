package model;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends NPC {
	private int currHealth;
	private int maxHealth;
	private int[] drops;
	private int damage;
	private boolean inCombat;

	public Enemy(String s, BufferedImage i) {
		super(s, i);
	}
	
	public Enemy(String s, BufferedImage i, int h, int d) {
		this(s, i);
		this.maxHealth = h;
		this.currHealth = h;
		this.damage = d;
		this.inCombat = true;
	}
	
	public void damage(int amount) {
		this.inCombat = true;
		this.reducehealth(amount);
		System.out.println("I've been damaged for " + amount + ". My health is now " + currHealth);
	}
	
	public int die() {
		Random rng = new Random();
		return drops[rng.nextInt(drops.length)];
	}
	
	private boolean reducehealth(int amount) {
		this.currHealth -= amount;
		if (this.currHealth <= 0) {
			this.currHealth = 0;
			return true;
		}
		return false;
	}

	public int getCurrHealth() {
		return currHealth;
	}

	public void setCurrHealth(int health) {
		this.currHealth = health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int health) {
		this.maxHealth = health;
	}

	public int[] getDrops() {
		return drops;
	}

	public void setDrops(int[] drops) {
		this.drops = drops;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public boolean getInCombat() {
		return this.inCombat;
	}
}
