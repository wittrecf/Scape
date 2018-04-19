package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import view.Game;
import view.ImageEnum;

public class Enemy extends NPC {
	private int currHealth;
	private int maxHealth;
	private ArrayList<ArrayList<Integer[]>> drops;
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
		this.drops = new ArrayList<ArrayList<Integer[]>>();
	}
	
	public void damage(int amount) {
		this.reducehealth(amount);
	}
	
	public ArrayList<Integer[]> die() {
		Random rng = new Random();
		ArrayList<Integer[]> tmp;
		if (drops.size() > 0) {
			int dropNum = rng.nextInt(drops.size());
			tmp = drops.get(dropNum);
		} else {
			tmp = new ArrayList<Integer[]>();
			tmp.add(new Integer[] {0, 0, 1});
		}
		return tmp;
	}
	
	private boolean reducehealth(int amount) {
		this.currHealth -= amount;
		if (this.currHealth <= 0) {
			this.currHealth = 0;
			return true;
		}
		return false;
	}
	
	private void findDir() {
		System.out.println("checking tile at: " + target[0] + ", " + target[1]);
		int x = target[0] - this.xLoc;
		int y = target[1] - this.yLoc;
		if (x != 0) {
			this.xDir = x / Math.abs(x);
		}
		if (y != 0) {
			this.yDir = y / Math.abs(y);
		}
		System.out.println("checking into at: " + (this.xLoc + this.xDir) + ", " + (this.yLoc + this.yDir));
		if (!(Game.state.mapTiles[this.xLoc + this.xDir][this.yLoc + this.yDir] != 0) || !(Game.state.objTiles[this.xLoc + this.xDir][this.yLoc + this.yDir] == 0) || (Player.checkAdjacency(true, false, new int[] {this.xLoc, this.yLoc}, new int[] {target[0], target[1]}) && !Player.checkAdjacency(false, false, new int[] {this.xLoc, this.yLoc}, new int[] {target[0], target[1]}))) {
			if ((Game.state.mapTiles[this.xLoc][this.yLoc + this.yDir] != 0) && (Game.state.objTiles[this.xLoc][this.yLoc + this.yDir] == 0)) {
				this.xDir = 0;
			} else if ((Game.state.mapTiles[this.xLoc + this.xDir][this.yLoc] != 0) && (Game.state.objTiles[this.xLoc + this.xDir][this.yLoc] == 0)) {
				this.yDir = 0;
			} else {
				this.xDir = 0;
				this.yDir = 0;
			}
		}
		System.out.println("xdir: " + this.xDir + ", ydir: " + this.yDir);
	}
	
	@Override
	public boolean move() {
		if ((target != null) && (inCombatWith != null) && ((this.xOff != 0) || (this.yOff != 0) || !Player.checkAdjacency(false, false, new int[] {xLoc, yLoc}, new int[] {this.getInCombatWith().getXLoc(), this.getInCombatWith().getYLoc()}))) {
			findDir();
			if ((Math.abs(this.xOff) != ImageEnum.TILEGRASS.getWidth()) && (Math.abs(this.yOff) != ImageEnum.TILEGRASS.getHeight()) && (Math.abs(this.xDir) == Math.abs(this.yDir)) && (this.xDir != 0))  {
				this.xOff += (ImageEnum.TILEGRASS.getWidth() * this.xDir) / this.movSize;
				this.yOff += (ImageEnum.TILEGRASS.getHeight() * this.yDir) / this.movSize;
				//System.out.println("a");
			} else if ((Math.abs(this.xOff) != ImageEnum.TILEGRASS.getWidth()) && (this.xDir != 0)) {
				this.xOff += (ImageEnum.TILEGRASS.getWidth() * this.xDir) / this.movSize;
				//System.out.println(this.xOff);
				//System.out.println("b");
			} else if ((Math.abs(this.yOff) != ImageEnum.TILEGRASS.getHeight()) && (this.yDir != 0)) {
				this.yOff += (ImageEnum.TILEGRASS.getHeight() * this.yDir) / this.movSize;
				//System.out.println("c");
			} else if ((this.xDir != 0) || (this.yDir != 0)) {
				Game.state.npcTiles[xLoc][yLoc].remove(this);
				if (Game.state.npcTiles[xLoc][yLoc].size() == 0) {
					Game.state.npcTiles[xLoc][yLoc] = null;
				}
				this.xLoc += this.xDir;
				this.yLoc += this.yDir;
				this.xOff = 0;
				this.yOff = 0;
				this.xDir = 0;
				this.yDir = 0;
				if (Game.state.npcTiles[xLoc][yLoc] == null) {
					Game.state.npcTiles[xLoc][yLoc] = new ArrayList<NPC>();
				}
				Game.state.npcTiles[xLoc][yLoc].add(this);
				if ((this.xLoc == target[0]) && (this.yLoc == target[1])) {
					target = null;
				}
				//System.out.println("d");
				return true;
				//System.out.println("moved to " + xLoc + ", " + yLoc);
			}
		} else if (inCombatWith == null) {
			System.out.println("move in super");
			return(super.move());
		} else if (target != null) {
			System.out.println("is " + xLoc + ", " + yLoc + " nearby to " + target[0] + ", " + target[1] + ": " + Player.checkAdjacency(false, false, new int[] {xLoc, yLoc}, new int[] {target[0], target[1]}));
			System.out.println("is " + xLoc + ", " + yLoc + " nearby to " + this.getInCombatWith().getXLoc() + ", " + this.getInCombatWith().getYLoc() + ": " + Player.checkAdjacency(false, false, new int[] {xLoc, yLoc}, new int[] {this.getInCombatWith().getXLoc(), this.getInCombatWith().getYLoc()}));
			System.out.println("shouldnt be here");
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
						System.out.println("dedd");
						this.inCombatWith = null;
					}
					this.attackTime = System.currentTimeMillis();
				}
			} else if ((p != null) && ((target == null) || ((target != null) && !Player.checkAdjacency(false, false, new int[] {xLoc, yLoc}, new int[] {p.getXLoc(), p.getYLoc()})))) {
				System.out.println("cant attack");
				int tmpX;
				int tmpY;
				if ((xLoc == p.getXLoc()) && (yLoc == p.getYLoc())) {
					if ((Game.state.mapTiles[p.getXLoc() + 1][p.getYLoc()] == 1) && (Game.state.objTiles[p.getXLoc() + 1][p.getYLoc()] == 0)) {
						tmpX = p.getXLoc() + 1;
						tmpY = p.getYLoc();
					} else if ((Game.state.mapTiles[p.getXLoc() - 1][p.getYLoc()] == 1) && (Game.state.objTiles[p.getXLoc() - 1][p.getYLoc()] == 0)) {
						tmpX = p.getXLoc() - 1;
						tmpY = p.getYLoc();
					} else if ((Game.state.mapTiles[p.getXLoc()][p.getYLoc() + 1] == 1) && (Game.state.objTiles[p.getXLoc()][p.getYLoc() + 1] == 0)) {
						tmpX = p.getXLoc();
						tmpY = p.getYLoc() + 1;
					} else if ((Game.state.mapTiles[p.getXLoc()][p.getYLoc() - 1] == 1) && (Game.state.objTiles[p.getXLoc()][p.getYLoc() - 1] == 0)) {
						tmpX = p.getXLoc();
						tmpY = p.getYLoc() - 1;
					} else {
						tmpX = p.getXLoc() + 1;
						tmpY = p.getYLoc();
					}
					//System.out.println("one of these");
				} else {
					//System.out.println("elseded");
					tmpX = p.getXLoc();
					tmpY = p.getYLoc();
				}
				System.out.println("GOTO: " + tmpX + ", " + tmpY + " from " + xLoc + ", " + yLoc);
				//this.attackTime = System.currentTimeMillis();
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

	public ArrayList<ArrayList<Integer[]>> getDrops() {
		return drops;
	}

	public void addDrop(int[] drop, int[] noted, int[] amount) {
		ArrayList<Integer[]> tmp = new ArrayList<Integer[]>();
		for (int i = 0; i < drop.length; i++) {
			if (noted[i] == 0) {
				for (int j = 0; j < amount[i]; j++) {
					tmp.add(new Integer[] {drop[i], noted[i], 1});
				}
			} else {
				tmp.add(new Integer[] {drop[i], noted[i], amount[i]});
			}
		}
		this.drops.add(tmp);
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
