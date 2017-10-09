package model;

import java.util.ArrayList;

import model.Board;

public class BoardState {
	
	// Objects in the world
	public Board board;
	
	public int shoreRowsNum = 30;
	public int shoreColsNum = 30;
	
	public ArrayList<ArrayList<BoardTile>> tiles;
}