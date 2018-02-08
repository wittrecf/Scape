package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

public class ExitButtonUI extends BasicButtonUI implements Serializable, MouseListener {
	private static final long serialVersionUID = 1L;

	BufferedImage currImg, default_img;
	
	private final static ExitButtonUI exitButtonUI = new ExitButtonUI();
	
	public static ComponentUI createUI(JComponent c) {
	    return exitButtonUI;
	}
	
	@Override
	public void installUI(JComponent c) {
	    super.installUI(c);
	    default_img = ImageEnum.EXIT_DEFAULT.getImg();
	    currImg = default_img;

	    c.addMouseListener(this);
	    
	    AbstractButton b = (AbstractButton) c;
	    b.setContentAreaFilled(false);
	    b.setBorderPainted(false); 
	}
	
	@Override
	public void uninstallUI(JComponent c) {
	    super.uninstallUI(c);
	    c.removeMouseListener(this);
	}
	
	public void paint(Graphics g, JComponent c) {
	    g.drawImage(currImg, 0, 0, null);
	}

	public Dimension getPreferredSize(JComponent c) {
	    Dimension d = super.getPreferredSize(c);
	    d.setSize(default_img.getWidth(), default_img.getHeight());
	    return d;
	}
	
	public void mouseClicked(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {
		currImg = default_img;
	}

	public void mouseReleased(MouseEvent arg0) {
		currImg = default_img;
	}

	public void mouseEntered(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {}	
}