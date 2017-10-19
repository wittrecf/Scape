package controller;

import java.awt.Rectangle;
import java.util.ArrayList;

import model.BoardTile;
import view.Image;

public class CollisionDetection {
	
	public static int[] checkCollisionsTile(int x, int y, ArrayList<ArrayList<BoardTile>> tiles) {
		int[] tmp = new int[3];
		for (ArrayList<BoardTile> col : tiles) {
			for (BoardTile bt : col) {
				Rectangle bdClick = new Rectangle(x, y, 1, 1);
				Rectangle bdBt = new Rectangle(bt.getXLoc(), bt.getYLoc(), Image.TILEGRASS.getWidth(), Image.TILEGRASS.getHeight());
	            if (bdBt.intersects(bdClick)) {
	            	tmp[0] = bt.getXCoord();
	            	tmp[1] = bt.getYCoord();
	            	if (bt.getObj() != null) {
	            		tmp[2] = 1;
	            	} else {
	            		tmp[2] = 0;
	            	}
	            	System.out.println("collision " + tmp[0] + ", " + tmp[1]);
	            	return tmp;
	            }
	        }
		}
		return null;
	}
	
}
