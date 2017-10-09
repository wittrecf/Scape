package view;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawOps {
	
	/**
	 * Scales the given image by the given factor and returns it.
	 * 
	 * @param imgToScale
	 * @param scale
	 * @return the original image scaled by the given factor
	 */
    public static BufferedImage scaleImgByFactor(BufferedImage imgToScale, double scale) {
    	int newW = (int) (imgToScale.getWidth()*scale);
    	int newH = (int) (imgToScale.getHeight()*scale);
    	
        BufferedImage scaledImage = null;
        if (imgToScale != null) {
            scaledImage = new BufferedImage(newW, newH, imgToScale.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(imgToScale, 0, 0, newW, newH, null);
            graphics2D.dispose();
        }
        return scaledImage;
    }    
    
	/**
	 * Scales the given image to the given dimensions and returns it.
	 * Obtained from: http://stackoverflow.com/questions/15558202/how-to-resize-image-in-java
	 * 
	 * @param imageToScale The image to scale
	 * @param newWidth the new width
	 * @param newHeight the new height
	 * @return the scaled image
	 */
	public static BufferedImage scaleImgToSize(BufferedImage imageToScale, int newWidth, int newHeight) {
        BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(newWidth, newHeight, imageToScale.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(imageToScale, 0, 0, newWidth, newHeight, null);
            graphics2D.dispose();
        }
        return scaledImage;
    }
	
	/**
	 * Lays the first given image on top of the second given image. 
	 * The second image's dimensions MUST be greater than the first image's dimensions.
	 * 
	 * @param img1 The image to overlay
	 * @param img2 The "background" image that img1 will be put on top of.
	 * @return the new overlaid image
	 */
	public static BufferedImage overlayImgsInCenter(BufferedImage img1, BufferedImage img2) {
		BufferedImage newImg = null;
		if(img2.getWidth() < img1.getWidth() || img2.getHeight() < img1.getHeight()) {
			System.err.println("Unable to overlay images, Img2's dimensions are smaller than that of Img1");
			return img2;
		}
        if(img1 != null && img2 != null) {
        	int img1XLoc, img1YLoc;
        	img1XLoc = (img2.getWidth() - img1.getWidth())/2;
        	img1YLoc = (img2.getHeight() - img1.getHeight())/2;
        	
            newImg = new BufferedImage(img2.getWidth(), img2.getHeight(), img2.getType());
            Graphics2D graphics2D = newImg.createGraphics();
            graphics2D.drawImage(img2, 0, 0, null);
            graphics2D.drawImage(img1, img1XLoc, img1YLoc, null);
            graphics2D.dispose();
        }
        return newImg;
	}
    
    /**
     * Custom painting code for drawing a TILED image from a single image.
	 * Code obtained from: https://tips4java.wordpress.com/2008/10/12/background-panel/
	 * 
     * @param g the graphics to render the image on
     * @param img the image to use for tiling
     * @param jpanel the JPanel that the image will be rendered on (can pass in a Minigame here)
     */
    public static void drawTiledImage(Graphics g, Image img, JPanel jpanel)
	{
		Dimension d = jpanel.getSize();
		int width = img.getWidth(null);
		int height = img.getHeight(null);

		for(int x = 0; x < d.width; x += width) {
			for(int y = 0; y < d.height; y += height) {
				g.drawImage(img, x, y, null, null);
			}
		}
	}
    
    /**
     * Custom painting code for drawing a specified TILED image from a single image.
	 * Code modified from: https://tips4java.wordpress.com/2008/10/12/background-panel/
	 * 
     * @param g the graphics to render the image on
     * @param img the image to use for tiling
     * @param jpanel the JPanel that the image will be rendered on (can pass in a Minigame here)
     * @param numHorizontalRepeats number of times to repeat the image in the horizontal direction
     * @param numVerticalRepeats number of times to repeat the image in the horizontal direction
     * @param scale whether to scale the image to the size of the given JPanel
     * @param hozOffset how much to offset the drawing by horizontally compared to the origin
     * @param vertOffset how much to offset the drawing by vertically compared to the origin
     */
    public static void drawCustomTiledImage(Graphics g, Image img, JPanel jpanel, 
    		int numHorizontalRepeats, int numVerticalRepeats, boolean scale,
    		int hozOffset, int vertOffset)
	{
		Dimension d = jpanel.getSize();
		int width = img.getWidth(null);
		int height = img.getHeight(null);	
		if(scale) {
			width = d.width;
			height = d.height;			
		}
		
		for(int x = 0; x < numHorizontalRepeats + 1; x++)
			for(int y = 0; y < numVerticalRepeats + 1; y++)
				g.drawImage(img, x*width + hozOffset, y*height + vertOffset, width, height, null);
	}
	
    /**
     * Custom painting code for drawing a SCALED image from a single image onto the given panel.
	 * Code obtained from: https://tips4java.wordpress.com/2008/10/12/background-panel/
	 * 
     * @param g the graphics to render the image on
     * @param img the image to scale to fit the given JPanel
     * @param jpanel the JPanel that the image will be rendered on (can pass in a Minigame here)
     */
	public static void drawScaledImage(Graphics g, Image img, JPanel jpanel)
	{
		Dimension d = jpanel.getSize();
		g.drawImage(img, 0, 0, d.width, d.height, null);
	}
	
	
	
	/**
	 * TODO: Add documentation on this function, and how it is used.
	 */
	public static void drawReSizedImage(Graphics g, Image img, JPanel jpanel, double x, double y, double origWidth, double origHeight) {
		Dimension d = jpanel.getSize();
		int newX = (int) ((x / 900) * d.getWidth());
		int newY = (int) ((y / 500) * d.getHeight());
		int newWidth = (int) ((origWidth / 900) * d.getWidth());
		int newHeight = (int) ((origHeight / 500) * d.getHeight());
		System.out.println(y + " / 500 * " + d.getHeight() + " = " + newY);
		System.out.println("draw sandblock at: " + newX + ", " + newY + ". Dimensions: " + newWidth + ", " + newHeight + ". Screen size: " + d.getWidth() + ", " + d.getHeight());
		g.drawImage(img, newX, newY, newWidth, newHeight, null);
	}
	
	/**
	 * Draws the given string on the given graphics center-justified at the given y position.
	 * You MUST specify the font, color, etc. of the Graphics ahead of time prior to calling this method.
	 * 
	 * @param g the graphics to use for drawing
	 * @param str the string to draw
	 * @param panel the panel the string will be drawn on (needed for width/height data)
	 * @param vert the yLoc of the text
	 */
	public static void drawHozCenteredString(Graphics g, String str, JComponent c, int vert) {
		FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(str, g);
        int x = (c.getWidth() - (int) r.getWidth()) / 2;
        int y = (c.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
        g.drawString(str, x, vert);
	}
}