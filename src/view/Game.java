package view;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.GameTimer;
import controller.MainController;
import model.Board;

public class Game extends JPanel implements ActionListener, MouseListener {
	
	private static GridBagLayout layout = new GridBagLayout();
    private static GridBagConstraints c = new GridBagConstraints();
	private int nOfGridCells = 3;
	private boolean gameOver;
	private Board board;
	private Timer timerMini2;
	private int fontSize;

	/**
	 * Creates an instance of MiniTwo
	 */
	public Game(MainController mainController) {
		
    	board = new Board(mainController.getWidth(), mainController.getHeight());
    	
		//scaleScreenItems();
		
		this.addMouseListener(this);
	}
	
	/**
	 * Sets the state of minigame 2 to the starting conditions.
	 */
	public void startup() {
		
		gameOver = false;
    	
		timerMini2 = new Timer();
		timerMini2.scheduleAtFixedRate(new GameTimer(this), 0, 10);
		

	}

	/**
	 * Shuts game 2 down.
	 */
	public void shutdown() {
		if(timerMini2 != null) timerMini2.cancel();
	}
	
	/**
	 *  Add custom painting, used to render background once for the panel
	 *  Paints everything needed in the game
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		// Call the super class
		super.paintComponent(g);
		//super.paint(g);
		/*
		// Handle game over logic
		if (!gameOver) {
			
	    	//Draw the dock, and background
			DrawOps.drawScaledImage(g, G2Image.BACKGROUND.getImg(), this);
			g.drawImage(G2Image.DOCK.getImg(), 0, state.board.getHeight()-G2Image.DOCK.getHeight(), null);
		
			// Next lets draw our other components
			Graphics2D g2 = (Graphics2D) g;
			fontSize = 36;
	    	g.setFont(new Font("default", Font.BOLD, fontSize));
	   	
	    	//Draw all small wakes
			Iterator<SmallWake> itS = state.smallWakes.iterator();
			SmallWake smallWake;
			while(itS.hasNext()) {
				try {
					smallWake = itS.next();
				} catch (ConcurrentModificationException e) {
					break;
				}
	    		g.drawImage(G2Image.SMALLWAKE.getImg(), smallWake.getXLoc(), smallWake.getYLoc(), null);
			}
			
			// Draw all large wakes
			Iterator<LargeWake> itL = state.largeWakes.iterator();
			LargeWake largeWake;
			while(itL.hasNext()) {
				try {
					largeWake = itL.next();
				} catch (ConcurrentModificationException e) {
					break;
				}
	    		g.drawImage(G2Image.LARGEWAKE.getImg(), largeWake.getXLoc(), largeWake.getYLoc(), null);
			}
			
	    	// Draw all the Boats
			Iterator<Boat> it1 = state.boats.iterator();
			while(it1.hasNext()) {
				Boat boat = it1.next();
	    		if(boat instanceof SmallBoat) {
	    			g.drawImage(G2Image.SMALLBOAT.getImg(), boat.getXLoc(), boat.getYLoc(), null);
	    		} else if(boat instanceof LargeBoat) {
	    			g.drawImage(G2Image.LARGEBOAT.getImg(), boat.getXLoc(), boat.getYLoc(), null);
	    		}
	    	}
			
			// Next we are drawing obstacles
			Obstacle o;
			BufferedImage img = null;
			
	    	// Draw all the obstacle that are on the shoreline
			// Note we also draw the sandblocks here
	    	for (ArrayList<SandBlock> ar: state.shoreline) {
		    	for (SandBlock block: ar){
		    		int obsHealth = 0, obsWidth = Obstacle.getDefaultWidth(), obsHeight = Obstacle.getDefaultHeight();
		    		if (block.getHealth() != 0) {
			    		o = block.getObs();
		    			g.drawImage(G2Image.SANDBLOCK.getImg(), block.getXLoc(), block.getYLoc(), null);
		    			
		    			if (o != null) {
				    		if (o instanceof RockWall)
				    			img = G2Image.ROCK_OBS.getImg();
				    		else if (o instanceof OysterGabion)
				    			img = G2Image.OYSTER_OBS.getImg();
				    		else if (o instanceof CordGrass)
				    			img = G2Image.GRASS_OBS.getImg();
				    		
				    		g.drawImage(img,
			    					block.getXLoc() + (G2Image.SANDBLOCK.getWidth() - o.getWidth())/2, 
			    					block.getYLoc() + (G2Image.SANDBLOCK.getHeight() - o.getHeight())/2,
			    					null);
				    		obsHealth = o.getHealth();
				    		obsWidth = o.getWidth();
				    		obsHeight = o.getHeight();
			    		}
	 			
			    		if (block.isPlacement()) {
			    			g.drawImage(G2Image.PLACEMENT.getImg(),
			    					block.getXLoc() + (G2Image.SANDBLOCK.getWidth() - obsWidth)/2, 
			    					block.getYLoc() + (G2Image.SANDBLOCK.getHeight() - obsHeight)/2,
			    					null);
			    			if (!tutorial.getIsDone()) {
			    				g.drawImage(G2Image.POINTER.getImg(), block.getXLoc(), block.getYLoc(),null);
			    			}
			    		}
	
		    			g2.setColor(Color.WHITE);
			    		g2.setFont(new Font("TimesRoman", Font.PLAIN, (int)(0.75*fontSize)));
		    			g2.drawString("HP:" + (block.getHealth() + obsHealth), block.getXLoc() + 105, block.getYLoc() + 45); //TEMPORARY
		    			
			    		if (block.getAffected()) {
				    		if (block.getDamage().equals("1")) {
								g2.setColor(Color.GREEN);
					    		g2.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
								g2.drawString("+" + block.getDamage(), block.getXLoc() + 115, block.getYLoc() - 10);
							} else if (!block.getDamage().equals("0")) {
								g2.setColor(Color.RED);
					    		g2.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
								g2.drawString(block.getDamage(), block.getXLoc() + 115, block.getYLoc() - 10);
							}
			    		}
			    	}		    	
		    	}
	    	}
	    	
	    	// Draw all resources
	    	for (Resource re : state.resources) {
	    		if (re instanceof Rock && re.getClickable()) g.drawImage(G2Image.ROCK_PICKUP.getImg(), re.getXLoc(), re.getYLoc(), this);
	    		else if (re instanceof Oyster && re.getClickable()) g.drawImage(G2Image.OYSTER_PICKUP.getImg(), re.getXLoc(), re.getYLoc(), this);
	    		else if (re instanceof Grass && re.getClickable()) g.drawImage(G2Image.GRASS_PICKUP.getImg(), re.getXLoc(), re.getYLoc(), this);
	    		if(!tutorial.getIsDone()){
	    			g.drawImage(G2Image.POINTER.getImg(), re.getXLoc() - G2Image.POINTER.getWidth(), re.getYLoc(), null);
	    		}
	    	}
	    	
	    	// Make all 2d graphics 30% opaque after this point
	    	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.3));
	    	for (Resource re : state.resources) {
	    		if (re instanceof Rock && !re.getClickable()) g2.drawImage(G2Image.ROCK_PICKUP.getImg(), re.getXLoc(), re.getYLoc(), this);
	    		else if (re instanceof Oyster && !re.getClickable()) g2.drawImage(G2Image.OYSTER_PICKUP.getImg(), re.getXLoc(), re.getYLoc(), this);
	    		else if (re instanceof Grass && !re.getClickable()) g2.drawImage(G2Image.GRASS_PICKUP.getImg(), re.getXLoc(), re.getYLoc(), this);
	    	}
	    	
	    	// Make all 2d graphics 100% opaque again after this point
	    	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1.0));
	    	
	      	// Draw all the Crabs
			Iterator<Crab> it2 = state.crabs.iterator();
			while(it2.hasNext()) {
				Crab crab = it2.next();
	    		g.drawImage(G2Image.CRAB.getImg(), crab.getXLoc(), crab.getYLoc(), null);
	    		if(crab.getBusy()){
	    			g.drawImage(G2Image.BUSY.getImg(), (int) (crab.getXLoc() + crab.getBusyImgXOffset()/1.4), crab.getYLoc() + crab.getBusyImgYOffset() - 11, null);
	    		}
	    	}
			
			// Calculate where buttons go
			int btnSpacing = 4;
			g.setColor(Color.BLACK);
			double scaleFactor = (double) state.board.getWidth()/state.shoreColsNum/G2Image.SANDBLOCK.getWidth();//Can we not do this every frame?
			
			//Draw arrows on obstacle stockpiles
			if(!tutorial.getIsDone()){
				switch(tutorial.getStepNumber()){
				case 1://start
					g.setFont(new Font("TimesRoman", Font.PLAIN, (int)(4*fontSize)));
					DrawOps.drawHozCenteredString(g, "Tutorial", this, this.board.getHeight()/2);
					break;
				case 3://grass
					// Draw left arrow
		    		AffineTransform trans1 = new AffineTransform();
		    		trans1.setToTranslation((int) (3*btnSpacing + 2.5*scaleFactor*btnWidth),(int) (state.board.getHeight() - G2Image.DOCK.getHeight() - G2Image.POINTER.getHeight()));
		    		trans1.rotate(Math.toRadians(90));
		    		// Display
		    		((Graphics2D)g).drawImage(G2Image.POINTER.getImg(), trans1, this);
					break;
				case 8://oyster gabion
					// Draw left arrow
		    		AffineTransform trans2 = new AffineTransform();
		    		trans2.setToTranslation((int) (3*btnSpacing + 1.5*scaleFactor*btnWidth),(int) (state.board.getHeight() - G2Image.DOCK.getHeight() - G2Image.POINTER.getHeight()));
		    		trans2.rotate(Math.toRadians(90));
		    		// Display
		    		((Graphics2D)g).drawImage(G2Image.POINTER.getImg(), trans2, this);
					break;
				case 12://rock wall
					// Draw left arrow
		    		AffineTransform trans3 = new AffineTransform();
		    		trans3.setToTranslation((int) (3*btnSpacing + 0.5*scaleFactor*btnWidth),(int) (state.board.getHeight() - G2Image.DOCK.getHeight() - G2Image.POINTER.getHeight()));
		    		trans3.rotate(Math.toRadians(90));
		    		// Display
		    		((Graphics2D)g).drawImage(G2Image.POINTER.getImg(), trans3, this);
					break;
				case 15://start
					g.setFont(new Font("TimesRoman", Font.PLAIN, (int)(4*fontSize)));
					DrawOps.drawHozCenteredString(g, "Game Start", this, this.board.getHeight()/2);
					g.setFont(new Font("TimesRoman", Font.PLAIN, (int)(2*fontSize)));
					DrawOps.drawHozCenteredString(g, "Defend the Shoreline!", this, this.board.getHeight()/2 + 75);
					break;
				}
			}			
	    	
			// Update resource amount
			btnRock.setText("" + state.rockNum);
			btnOyster.setText("" + state.oysterNum);
			btnGrass.setText("" + state.grassNum);
    	
    	}
		// Show the scoreboard to the user
		else {
			// Draw the nice background for them
			DrawOps.drawScaledImage(g, G1Image.LEADERBOARD_BG.getImg(), this);
			fontSize = board.getWidth()/20;
    		// Set text details
    		g.setColor(Color.WHITE);
        	g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize)); 
    		
    		// Loop through 5 scores if we have them
        	// Always display 5 high scores
        	Score s;
    		for(int i=0; i<5; i++) {
    			// If we have a score then display it
    			if(i < state.scores.size()) {
    				s = state.scores.get(i);
    				DrawOps.drawHozCenteredString(g, s.getName() + " - " + s.getTime(), this, fontSize*i+this.getHeight()/3);
    			}
    			// Else display it as unknown user
    			else {
    				DrawOps.drawHozCenteredString(g, "EMPTY - ????", this, fontSize*i+this.getHeight()/3);
    			}
    		}
    		
    		// Set text details
    		g.setColor(Color.RED);
        	g.setFont(new Font("TimesRoman", Font.BOLD, fontSize)); 
    		
    		// Finally display our score at the top
        	if(state.lastScore != null) {
        		DrawOps.drawHozCenteredString(g, state.lastScore.getName()+" - "+state.lastScore.getTime(), this, (int)(1.5*fontSize));
        	}
    	}
		
		// Change the button color based on if we have enough resources
		// If we do, display the colored version
		if(state.grassNum>=state.grassObstacleAmount){
			btnGrass.setIcon(new ImageIcon(G2Image.GRASS_OBS.getImg()));
		}
		else{
			btnGrass.setIcon(new ImageIcon(G2Image.GRASS_OBS2.getImg()));
		}
		
		if(state.oysterNum>=state.oysterObstacleAmount){
			btnOyster.setIcon(new ImageIcon(G2Image.OYSTER_OBS.getImg()));
		}
		else{
			btnOyster.setIcon(new ImageIcon(G2Image.OYSTER_OBS2.getImg()));
		}
		
		if(state.rockNum>=state.rockObstacleAmount){
			btnRock.setIcon(new ImageIcon(G2Image.ROCK_OBS.getImg()));
		}
		else{
			btnRock.setIcon(new ImageIcon(G2Image.ROCK_OBS2.getImg()));
		}
		*/
	}
	
	
	/**
	 * Scales image files based on the size of the screen the game is being played on.
	 */
	/*private void scaleScreenItems() {
		int worldWidth = board.getWidth();
		
		// Calculate the font size
		fontSize = board.getHeight()/25;
		
		int newSmallBoatWidth = worldWidth/15; //smallboat is 1/15 the screenwidth 
		double scaleFactor = (double) newSmallBoatWidth/G2Image.SMALLBOAT.getWidth();		
		G2Image.SMALLBOAT.scaleByFactor(scaleFactor);
		
		int newLargeBoatWidth = worldWidth/3; //largeboat is 1/3 screenwidth
		scaleFactor = (double) newLargeBoatWidth/G2Image.LARGEBOAT.getWidth();
		G2Image.LARGEBOAT.scaleByFactor(scaleFactor);
		
		int newSandblockWidth = worldWidth/state.shoreColsNum;
		scaleFactor = (double) newSandblockWidth/G2Image.SANDBLOCK.getWidth();
		
		G2Image.SANDBLOCK.scaleByFactor(scaleFactor);
		G2Image.PLACEMENT.scaleByFactor(scaleFactor);
		G2Image.CRAB.scaleByFactor(scaleFactor);
		G2Image.BUSY.scaleByFactor(scaleFactor);
		
		G2Image.SMALLWAKE.scaleByFactor(scaleFactor);
		G2Image.LARGEWAKE.scaleByFactor(scaleFactor);
		
		G2Image.DOCK.scaleByFactor(scaleFactor);
		
		G2Image.ROCK_OBS.scaleByFactor(scaleFactor);
		G2Image.OYSTER_OBS.scaleByFactor(scaleFactor);
		G2Image.GRASS_OBS.scaleByFactor(scaleFactor);
		
		G2Image.ROCK_OBS2.scaleByFactor(scaleFactor);
		G2Image.OYSTER_OBS2.scaleByFactor(scaleFactor);
		G2Image.GRASS_OBS2.scaleByFactor(scaleFactor);
		
		btnWidth *= scaleFactor;
 		btnHeight *= scaleFactor;
 		
 		btnRock.setBounds(4, state.board.getHeight() - G2Image.ROCK_OBS.getHeight(), btnWidth, btnHeight);
 		btnOyster.setBounds(btnWidth + 8, state.board.getHeight() - G2Image.OYSTER_OBS.getHeight(), btnWidth, btnHeight);
 		btnGrass.setBounds(2*btnWidth + 12, state.board.getHeight() - G2Image.GRASS_OBS.getHeight(), btnWidth, btnHeight);
	
		state.rockX = btnRock.getX();
		state.rockY = btnRock.getY();
		state.oysterX = btnOyster.getX();
		state.oysterY = btnOyster.getY();
		state.grassX = btnGrass.getX();
		state.grassY = btnGrass.getY();
		
		Crab.setVelocity( (int) (Crab.getVelocity() * 1.8 * scaleFactor));
		Boat.setSpeed((int) (Boat.getSpeed() * 1.5 * scaleFactor));
		Boat.setDefaultSpawnY((int) (Boat.getDefaultSpawnY() * scaleFactor));
	}*/
	
	/**
	 * Determines what button the player clicked, and adjusts the game accordingly.
	 */
	public void actionPerformed(ActionEvent event) {
		/*if(event.getSource() == btnExit) {
			mainController.exitMini();
		}
		else if(event.getSource() == btnRock) {
			if (resourceM.btnEvent("Rock")) {
				state.placementMode = 1;
			}
		} else if(event.getSource() == btnOyster) {
			if (resourceM.btnEvent("Oyster")) {
				state.placementMode = 2;
			}
		} else if(event.getSource() == btnGrass) {
			if (resourceM.btnEvent("Grass")) {
				state.placementMode = 3;
			}
		}*/
	}
	
	/**
	 * Depending on what has been clicked, assignes the crabs to either pick up a Resource, or go place an Obstacle.
	 */
	public void mouseClicked(MouseEvent event) {
		/*switch (state.placementMode) {
		case 0: crabM.assignCrabResource(resourceM.pickupEvent(event.getX(), event.getY())); break;
		case 1: crabM.assignCrabObstacle(resourceM.placeEvent(state.placementMode, event.getX(), event.getY())); break;
		case 2: crabM.assignCrabObstacle(resourceM.placeEvent(state.placementMode, event.getX(), event.getY())); break;
		case 3: crabM.assignCrabObstacle(resourceM.placeEvent(state.placementMode, event.getX(), event.getY())); break;
		}*/
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}