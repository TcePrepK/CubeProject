package Project.Core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import enigma.console.TextWindow;

public class Keyboard {
	String pressedKey = "";
	
	public Keyboard(TextWindow textWindow) {
		textWindow.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// When a new key press happens, put it into the key buffer.
				int keyCode = e.getKeyCode();
				pressedKey = KeyEvent.getKeyText(keyCode).toUpperCase();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
		});
	}
	
	public String readKey() {
		String temp = pressedKey;
		pressedKey = "";
		return temp;
	}
	
	public boolean isKeyPressed() {
		return !pressedKey.equals("");
	}
}
