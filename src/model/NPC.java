package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import view.Game;
import view.ImageEnum;

public class NPC {
	private boolean isTalkable;
	private String name;
	private BufferedImage img;
	private int id;
	private static int npcCount = 0;
	protected int xHome;
	protected int yHome;
	protected int xLoc;
	protected int yLoc;
	protected int xOff = 0;
	protected int yOff = 0;
	protected int xDir = 0;
	protected int yDir = 0;
	protected int movSize = 32;
	protected int wanderRadius = 5;
	protected int[] target;
	
	public NPC(String s, BufferedImage i, int x, int y) {
		this.name = s;
		this.img = i;
		this.id = npcCount;
		this.xHome = x;
		this.yHome = y;
		this.xLoc = x;
		this.yLoc = y;
		NPC.npcCount++;
	}
	
	private void findDir() {
		int x;
		int y;
		if ((Math.abs(target[0] - this.xHome) > this.wanderRadius) || !(Game.state.mapTiles[target[0]][target[1]] != 0) || !(Game.state.objTiles[target[0]][target[1]] == 0)) {
			//System.out.println("modifed x");
			target[0] = this.xLoc;
		}
		if ((Math.abs(target[1] - this.yHome) > this.wanderRadius) || !(Game.state.mapTiles[target[0]][target[1]] != 0) || !(Game.state.objTiles[target[0]][target[1]] == 0)) {
			//System.out.println("modifed y");
			target[1] = this.yLoc;
		}
		x = target[0] - this.xLoc;
		y = target[1] - this.yLoc;
		if (x != 0) {
			this.xDir = x / Math.abs(x);
		}
		if (y != 0) {
			this.yDir = y / Math.abs(y);
		}
	}
	
	public boolean wander(int chance) {
		Random rng = new Random();
		if (rng.nextInt(chance) < 1) {
			int xStep = rng.nextInt(3) - 1;
			int yStep = rng.nextInt(3) - 1;
			//System.out.println("steps are: " + xStep + ", " + yStep);
			this.setTarget(xLoc + xStep, yLoc + yStep);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean move() {
		//System.out.println("try to move in super");
		if (target != null) {
			findDir();
			//System.out.println("DIRS: " + xDir + ", " + yDir);
			if ((Math.abs(this.xOff) != ImageEnum.TILEGRASS.getWidth()) && (Math.abs(this.yOff) != ImageEnum.TILEGRASS.getHeight()) && (Math.abs(this.xDir) == Math.abs(this.yDir)) && (this.xDir != 0)) {
				this.xOff += (ImageEnum.TILEGRASS.getWidth() * this.xDir) / this.movSize;
				this.yOff += (ImageEnum.TILEGRASS.getHeight() * this.yDir) / this.movSize;
				//System.out.println("a");
			} else if ((Math.abs(this.xOff) != ImageEnum.TILEGRASS.getWidth()) && (this.xDir != 0)) {
				this.xOff += (ImageEnum.TILEGRASS.getWidth() * this.xDir) / this.movSize;
				//System.out.println("b");
			} else if ((Math.abs(this.yOff) != ImageEnum.TILEGRASS.getHeight()) && (this.yDir != 0)) {
				this.yOff += (ImageEnum.TILEGRASS.getHeight() * this.yDir) / this.movSize;
				//System.out.println("c");
			} else {
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
		}
		return false;
	}
	
	public void setTarget(int x, int y) {
		System.out.println("new target loc at " + x + ", " + y + ". We started at " + xLoc + ", " + yLoc);
		this.target = new int[2];
		this.target[0] = x;
		this.target[1] = y;
	}
	
	public int[] getTarget() {
		return this.target;
	}

	public boolean getTalkable() {
		return isTalkable;
	}

	public void setTalkable(boolean isTalkable) {
		this.isTalkable = isTalkable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BufferedImage getImg() {
		return img;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getXLoc() {
		return this.xLoc;
	}
	
	public int getYLoc() {
		return this.yLoc;
	}
	
	public int getXOff() {
		return this.xOff;
	}
	
	public int getYOff() {
		return this.yOff;
	}
	
	public int getXHome() {
		return this.xHome;
	}
	
	public int getYHome() {
		return this.yHome;
	}
	
	public int getWanderRadius() {
		return this.wanderRadius;
	}
}
