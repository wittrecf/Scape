package controller;

import java.awt.Rectangle;
import java.util.ArrayList;

import model.BankTile;
import model.Board;
import model.BoardState;
import model.BoardTile;
import model.InventoryTile;
import model.Player;
import view.Game;
import view.ImageEnum;

public class CollisionDetection {
	
	public static int[] checkCollisionsTile(int button, int x, int y, BoardState state, Board board, Player player) {
		System.out.println("collision check");
		int[] tmp = new int[6];
		ArrayList<ArrayList<BoardTile>> tiles = state.tiles;
		Rectangle bdClick = new Rectangle(x, y, 1, 1);
		if (Game.bankOpen && bdClick.intersects(new Rectangle((Game.mainController.getWidth() / 2) - (ImageEnum.BANK.getImg().getWidth() / 2), 50, ImageEnum.BANK.getWidth(), ImageEnum.BANK.getHeight()))) {
			int num = 0;
			for (BankTile b : player.getBank().getBank()) {
				Rectangle bdBt = new Rectangle((int) ((Game.mainController.getWidth() / 2) - (ImageEnum.BANK.getImg().getWidth() / 2) + (.03 * ImageEnum.BANK.getWidth()) + (b.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .02 * ImageEnum.BANK.getWidth()))),
						(int) (50 + (.02 * ImageEnum.BANK.getHeight()) + (b.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .02 * ImageEnum.BANK.getWidth()))),
				ImageEnum.ICONBLANK.getWidth(),
				ImageEnum.ICONBLANK.getHeight());
				if (bdBt.intersects(bdClick)) {
					tmp[0] = num;
					tmp[5] = 2;
					return tmp;
				}
				num++;
			}
			tmp[0] = -1;
			tmp[5] = 1;
			return tmp;
		} else if ((x <= board.getWidth() - ImageEnum.INVENTORY.getWidth() || (y <= board.getHeight() - ImageEnum.INVENTORY.getHeight()))) {
			for (ArrayList<BoardTile> col : tiles) {
				for (BoardTile bt : col) {
					Rectangle bdBt = new Rectangle(bt.getXLoc(), bt.getYLoc(), ImageEnum.TILEGRASS.getWidth(), ImageEnum.TILEGRASS.getHeight());
		            if (bdBt.intersects(bdClick)) {
		            	tmp[0] = bt.getXCoord();
		            	tmp[1] = bt.getYCoord();
		            	tmp[3] = bt.getXLoc();
		            	tmp[4] = bt.getYLoc();
		            	if (bt.getObj() != null) {
		            		tmp[2] = 1;
		            	} else {
		            		tmp[2] = 0;
		            	}
		            	tmp[5] = 0;
		            	//System.out.println("collision " + tmp[0] + ", " + tmp[1]);
		            	return tmp;
		            }
		        }
			}
			return null;
		} else {
			//System.out.println("clicked inventory");
			int num = 0;
			for (InventoryTile i : player.getInv().getInventory()) {
				Rectangle bdIt = new Rectangle((int) (board.getWidth() - ImageEnum.INVENTORY.getWidth() + (.06 * ImageEnum.INVENTORY.getWidth()) + (i.getXLoc() * (ImageEnum.ICONBLANK.getWidth() + .04 * ImageEnum.INVENTORY.getWidth()))),
				(int) (board.getHeight() - ImageEnum.INVENTORY.getHeight() + (.04 * ImageEnum.INVENTORY.getHeight()) + (i.getYLoc() * (ImageEnum.ICONBLANK.getHeight() + .04 * ImageEnum.INVENTORY.getWidth()))),
				ImageEnum.ICONBLANK.getWidth(),
				ImageEnum.ICONBLANK.getHeight());
				if (bdIt.intersects(bdClick)) {
					tmp[0] = num;
					tmp[5] = 1;
					return tmp;
				}
				num++;
			}
			tmp[0] = -1;
			tmp[5] = 1;
			return tmp;
		}
	}
	
}
