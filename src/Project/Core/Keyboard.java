package Project.Core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import enigma.console.TextWindow;

public class Keyboard {
	private int maxKeys = 10;
	private int lastIDX = 0; // These are to check and control key buffer.
	String[] pressedList = new String[maxKeys]; // This is key buffer. Whenever a key is pressed, it goes in here.
	
	public Keyboard(TextWindow textWindow) {
		textWindow.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (lastIDX == maxKeys) {
					return;
				}
				
				// When a new key press happens, put it into the key buffer.
				int keyCode = e.getKeyCode();
				pressedList[lastIDX++] = KeyEvent.getKeyText(keyCode);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
		});
	}
	
	/**
	 * Returns the first element from the buffer and returns "" if the buffer is empty!
	 * Returns all upper-case!
	 */
	public String readKey() {
		if (lastIDX == 0) {
			return "";
		}
		
		// When you read a key from the key buffer, read the first element [0] then shift all elements to the left (one before it)
		// To be able to get the next element when it gets called the next time.
		String key = pressedList[0];
		this.shiftList();
		return key.toUpperCase();
	}
	
	public boolean isKeyPressed() {
		return lastIDX > 0;
	}
	
	private void shiftList() {
		for (int i = 1; i < lastIDX; i++) {
			pressedList[i - 1] = pressedList[i]; 
		}
		lastIDX--;
	}
}
