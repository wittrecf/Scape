package controller;

import java.sql.Time;
import java.util.TimerTask;

import model.Board;
import model.BoardState;
import model.MiningRock;
import model.Player;
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
		}
		
		for (int j = 0; j < state.mapRowsNum; j++) {
    		for (int i = 0; i < state.mapColsNum; i++) {
    			if (state.mapTiles[i][j] >= 10 && state.mapTiles[i][j] <= 50 && state.mapTiles[i][j] % 2 != 1) {
    				state.mapTiles[i][j] += MiningRock.pickRock((int) Math.floor(state.mapTiles[i][j])).getRespawnRate();
    			}
    		}
		}
	}

}
