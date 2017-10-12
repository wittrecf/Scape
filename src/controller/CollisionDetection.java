package controller;

import java.awt.Rectangle;
import java.util.ArrayList;

import model.BoardTile;
import view.Image;

public class CollisionDetection {
	
	public static int[] checkCollisionsTile(int x, int y, ArrayList<ArrayList<BoardTile>> tiles) {
		for (ArrayList<BoardTile> col : tiles) {
			for (BoardTile bt : col) {
				Rectangle bdClick = new Rectangle(x, y, 1, 1);
				Rectangle bdBt = new Rectangle(bt.getXLoc(), bt.getYLoc(), Image.TILEGRASS.getWidth(), Image.TILEGRASS.getHeight());
	            if (bdBt.intersects(bdClick)) {
	            	int[] tmp = {bt.getXCoord(), bt.getYCoord()};
	            	System.out.println("collision " + tmp[0] + ", " + tmp[1]);
	            	return tmp;
	            }
	        }
		}
		return null;
	}
	
}
