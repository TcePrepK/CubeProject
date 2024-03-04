package Project.Core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import enigma.console.TextWindow;

public class Keyboard {
	Stack pressedList = new Stack(5); // This is key buffer. Whenever a key is pressed, it goes in here.
	
	public Keyboard(TextWindow textWindow) {
		textWindow.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// When a new key press happens, put it into the key buffer.
				int keyCode = e.getKeyCode();
				pressedList.push(KeyEvent.getKeyText(keyCode).toUpperCase());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
		});
	}
	
	/**
	 * Returns the first element from the stack and returns "" if the stack is empty!
	 * Returns all upper-case!
	 */
	public String readKey() {
		Object popValue = pressedList.pop();
		if (popValue == null) return "";
		return (String) popValue;
	}
	
	public boolean isKeyPressed() {
		return !pressedList.isEmpty();
	}
}
