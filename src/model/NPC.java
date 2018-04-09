package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import view.Game;
import view.ImageEnum;

public class NPC {
	private boolean isTalkable;
	private String name;
	private BufferedImage img;
	private int id;
	private static int npcCount = 0;
	protected int xLoc;
	protected int yLoc;
	protected int xOff = 0;
	protected int yOff = 0;
	protected int xDir = 0;
	protected int yDir = 0;
	protected int movSize = 16;
	protected int[] target;
	
	public NPC(String s, BufferedImage i, int x, int y) {
		this.name = s;
		this.img = i;
		this.id = npcCount;
		this.xLoc = x;
		this.yLoc = y;
		NPC.npcCount++;
	}
	
	public void clearPrevPath() {
		for (int j = 0; j < Game.state.mapRowsNum; j++) {
    		for (int i = 0; i < Game.state.mapColsNum; i++) {
				if (Game.state.mapTiles[i][j] == 3) {
					Game.state.mapTiles[i][j] = 1;
				}
			}
		}
	}
	
	private void findDir() {
		int x = target[0] - this.xLoc;
		int y = target[1] - this.yLoc;
		if (x != 0) {
			this.xDir = x / Math.abs(x);
		}
		if (y != 0) {
			this.yDir = y / Math.abs(y);
		}
	}
	
	public boolean move() {
		if ((target != null) && !Player.checkAdjacency(false, false, new int[] {xLoc, yLoc}, new int[] {target[0], target[1]})) {
			findDir();
			if ((Math.abs(this.xOff) != ImageEnum.TILEGRASS.getWidth()) && (Math.abs(this.yOff) != ImageEnum.TILEGRASS.getHeight()) && (Math.abs(this.xDir) == Math.abs(this.yDir)))  {
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
				return true;
				//System.out.println("d");
				//System.out.println("moved to " + xLoc + ", " + yLoc);
			}
		}
		return false;
	}
	
	public void setTarget(int x, int y) {
		System.out.println("new target loc at " + x + ", " + y);
		this.target = new int[2];
		this.target[0] = x;
		this.target[1] = y;
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
}
