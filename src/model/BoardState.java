package model;

import java.util.ArrayList;

import model.Board;

public class BoardState {
	
	// Objects in the world
	public Board board;
	
	public int boardRowsNum = 15;
	public int boardColsNum = 31;
	
	public int mapRowsNum = 100;
	public int mapColsNum = 100;
	
	public double[][] mapTiles;
	public double[][] objTiles;
	public ArrayList<Integer[]>[][] itemTiles = new ArrayList[mapRowsNum][mapColsNum];
	public ArrayList<NPC>[][] npcTiles = new ArrayList[mapRowsNum][mapColsNum];
	
	public ArrayList<ArrayList<BoardTile>> tiles;
	
	public ArrayList<Player> players;
}