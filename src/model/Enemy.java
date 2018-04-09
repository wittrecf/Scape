package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import view.Game;
import view.ImageEnum;

public class Enemy extends NPC {
	private int currHealth;
	private int maxHealth;
	private ArrayList<Integer[]> drops;
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
		this.drops = new ArrayList<Integer[]>();
	}
	
	public void damage(int amount) {
		this.reducehealth(amount);
	}
	
	public int[] die() {
		Random rng = new Random();
		if (drops.size() > 0) {
			int dropNum = rng.nextInt(drops.size());
			return new int[] {drops.get(dropNum)[0].intValue(), drops.get(dropNum)[1].intValue()};
		} else {
			return new int[]{0,1};
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
		if (this.currHealth > 0) {
			this.inCombatWith = p;
			if ((p != null) && (Player.checkAdjacency(false, false, new int[] {xLoc, yLoc}, new int[] {p.getXLoc(), p.getYLoc()}))) {
				System.out.println("try to attack");
				if ((p.getCurrHealth() > 0) && (System.currentTimeMillis() - this.getAttackTime() > this.getAttackSpeed()*100)) {
					p.damage(this.damage);
					if ((p.getInCombatWith() == null) && (p.getAutoRetaliate())) {
						p.attack(this);
					}
					if (this.getInCombatWith().getCurrHealth() <= 0) {
						this.inCombatWith = null;
					}
					this.attackTime = System.currentTimeMillis();
				}
			} else if ((p != null) && ((target == null) || ((target != null) && !Player.checkAdjacency(false, false, new int[] {xLoc, yLoc}, new int[] {p.getXLoc(), p.getYLoc()})))) {
				int tmpX;
				int tmpY;
				if ((xLoc == p.getXLoc()) && (yLoc == p.getYLoc()) && (System.currentTimeMillis() - this.getAttackTime() > this.getAttackSpeed()*100)) {
					if ((Game.state.mapTiles[p.getXLoc() + 1][p.getYLoc()] == 1) && (Game.state.objTiles[p.getXLoc() + 1][p.getYLoc()] == 0)) {
						tmpX = p.getXLoc() + 2;
						tmpY = p.getYLoc();
					} else if ((Game.state.mapTiles[p.getXLoc() - 1][p.getYLoc()] == 1) && (Game.state.objTiles[p.getXLoc() - 1][p.getYLoc()] == 0)) {
						tmpX = p.getXLoc() - 2;
						tmpY = p.getYLoc();
					} else if ((Game.state.mapTiles[p.getXLoc()][p.getYLoc() + 1] == 1) && (Game.state.objTiles[p.getXLoc()][p.getYLoc() + 1] == 0)) {
						tmpX = p.getXLoc();
						tmpY = p.getYLoc() + 2;
					} else if ((Game.state.mapTiles[p.getXLoc()][p.getYLoc() - 1] == 1) && (Game.state.objTiles[p.getXLoc()][p.getYLoc() - 1] == 0)) {
						tmpX = p.getXLoc();
						tmpY = p.getYLoc() - 2;
					} else {
						tmpX = p.getXLoc() + 2;
						tmpY = p.getYLoc();
					}
					System.out.println("one of these");
				} else {
					System.out.println("elseded");
					tmpX = p.getXLoc();
					tmpY = p.getYLoc();
				}
				//System.out.println("GOTO: " + tmpX + ", " + tmpY + " from " + xLoc + ", " + yLoc);
				this.attackTime = System.currentTimeMillis();
				setTarget(tmpX, tmpY);
			} else {
				System.out.println("umm why are you here: " + (target == null));
			}
		}
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

	public ArrayList<Integer[]> getDrops() {
		return drops;
	}

	public void addDrop(int drop, int ammount) {
		this.drops.add(new Integer[] {drop, ammount});
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
