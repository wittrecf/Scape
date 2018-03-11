package model;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class KeyType extends AbstractAction {
	private String key;
	
	public KeyType(String key) {
		this.key = key;
		System.out.println("key is " + key);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(key);
	}
	
}
