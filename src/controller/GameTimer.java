package controller;

import java.awt.Color;
import java.sql.Time;
import java.util.ArrayList;
import java.util.TimerTask;

import model.Board;
import model.BoardState;
import model.Enemy;
import model.Item;
import model.Player;
import model.TileFishingSpot;
import model.TileRock;
import model.TileTree;
import view.Game;

public class GameTimer extends TimerTask {
	
	private Game game;
	private Board board;
	private BoardState state;
	private double startTime;

	public GameTimer(Game game, Board board, BoardState state) {
		this.game = game;
		this.board = board;
		this.state = state;
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		game.repaint();
		double tmpTime = System.currentTimeMillis();
		
		for (Player p : state.players) {
			if ((tmpTime - startTime) % 1 == 0) {
				p.move();
			}
			if ((p.getInCombatWith() != null) && (tmpTime - p.getAttackTime() > p.getAttackSpeed()*100)) {
				p.attack(p.getInCombatWith());
			}
		}
		
		if (((tmpTime - game.spokenTime) > 3000) && !game.spokenText.equals("")) {
			game.spokenText = "";
		}
		
		for (int j = 0; j < state.mapRowsNum; j++) {
    		for (int i = 0; i < state.mapColsNum; i++) {
    			if (state.objTiles[i][j] >= 10 && state.objTiles[i][j] < 50 && state.objTiles[i][j] % 2 != 1) {
    				state.objTiles[i][j] = (double)Math.round((state.objTiles[i][j] + (TileRock.pickRock((int) Math.floor(state.objTiles[i][j])).getRespawnRate())) * 1000000d) / 1000000d;
    			} else if (state.objTiles[i][j] >= 50 && state.objTiles[i][j] < 90 && state.objTiles[i][j] % 2 != 1) {
    				state.objTiles[i][j] = (double)Math.round((state.objTiles[i][j] + (TileTree.pickTree((int) Math.floor(state.objTiles[i][j])).getRespawnRate())) * 1000000d) / 1000000d;
    			} else if (state.objTiles[i][j] >= 90 && state.objTiles[i][j] < 130 && state.objTiles[i][j] % 2 != 1) {
    				state.objTiles[i][j] = (double)Math.round((state.objTiles[i][j] + (TileFishingSpot.pickFishingSpot((int) Math.floor(state.objTiles[i][j])).getRespawnRate())) * 1000000d) / 1000000d;
    			}
    			if (state.npcTiles[i][j] != null) {
    				for (int x = 0; x < state.npcTiles[i][j].size(); x++) {
    					if (state.npcTiles[i][j].get(x) instanceof Enemy) {
    						if (((Enemy) (state.npcTiles[i][j].get(x))).getCurrHealth() <= 0) {
    							if (state.itemTiles[i][j] == null) {
    								state.itemTiles[i][j] = new ArrayList<Integer>();
    							}
    							int drop = ((Enemy) (state.npcTiles[i][j].get(x))).die();
    							state.itemTiles[i][j].add(drop);
    							Game.printText(game.player.getChatbox(), game.player.getUsername() + " has received: " + Item.getItemById(drop).getItemName(), new Color(255, 0, 255));
    							state.itemTiles[i][j].sort(null);
    							state.npcTiles[i][j].remove(x);
    							if (state.npcTiles[i][j].size() == 0) {
    								state.npcTiles[i][j].add(null);
    							}
    							return;
    						}
    						if ((((Enemy) (state.npcTiles[i][j].get(x))).getInCombatWith() != null) && (tmpTime - ((Enemy) (state.npcTiles[i][j].get(x))).getAttackTime() > ((Enemy) (state.npcTiles[i][j].get(x))).getAttackSpeed()*100)) {
    							((Enemy) (state.npcTiles[i][j].get(x))).attack(((Enemy) (state.npcTiles[i][j].get(x))).getInCombatWith());
    							if (((Enemy) (state.npcTiles[i][j].get(x))).getInCombatWith().getCurrHealth() <= 0) {
    								((Enemy) (state.npcTiles[i][j].get(x))).attack(null);
    							}
    						}
    					}
    				}
    			}
    		}
		}
	}

}
