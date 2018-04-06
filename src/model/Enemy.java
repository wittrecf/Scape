package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends NPC {
	private int currHealth;
	private int maxHealth;
	private ArrayList<Integer> drops;
	private int damage;
	private Player inCombatWith;
	private int attackSpeed = 6;
	private double attackTime;

	public Enemy(String s, BufferedImage i, int x, int y) {
		super(s, i, x, y);
	}
	
	public Enemy(String s, BufferedImage i, int x, int y, int h, int d) {
		this(s, i, x, y);
		this.maxHealth = h;
		this.currHealth = h;
		this.damage = d;
		this.drops = new ArrayList<Integer>();
	}
	
	public void damage(int amount) {
		this.reducehealth(amount);
	}
	
	public int die() {
		Random rng = new Random();
		if (drops.size() > 0) {
			return drops.get(rng.nextInt(drops.size()));
		} else {
			return 0;
		}
	}
	
	private boolean reducehealth(int amount) {
		this.currHealth -= amount;
		if (this.currHealth <= 0) {
			this.currHealth = 0;
			return true;
		}
		return false;
	}
	
	public void attack(Player p) {
		this.inCombatWith = p;
		if (p != null) {
			p.damage(this.damage);
		}
		this.attackTime = System.currentTimeMillis();
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

	public ArrayList<Integer> getDrops() {
		return drops;
	}

	public void addDrop(int drop) {
		this.drops.add(drop);
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public Player getInCombatWith() {
		return this.inCombatWith;
	}
	
	public int getAttackSpeed() {
		return this.attackSpeed;
	}
	
	public double getAttackTime() {
		return this.attackTime;
	}
}
