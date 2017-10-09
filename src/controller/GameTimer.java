package controller;

import java.util.TimerTask;

import model.Board;
import view.Game;

public class GameTimer extends TimerTask {
	
	private Game game;
	private Board board;

	public GameTimer(Game game, Board board) {
		this.game = game;
		this.board = board;
	}
	
	@Override
	public void run() {
		game.repaint();
	}

}
