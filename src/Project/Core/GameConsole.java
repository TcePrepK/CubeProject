package Project.Core;

import java.awt.Dimension;
import java.awt.Toolkit;

import enigma.console.Console;
import enigma.console.TextWindow;
import enigma.core.Enigma;

// This console class is just to manage print functions easier and remove the code spaghetti.
public class GameConsole {
	private int WIDTH = 60 * 2;
	private int HEIGHT = 40;
	public Console generalConsole;
	
	public GameConsole(String name) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		generalConsole = Enigma.getConsole(name, WIDTH, HEIGHT, 16);
	}
	
	public void print(int x, int y, char c) {
		if (x < 0 || x * 2 >= WIDTH || y < 0 || y >= HEIGHT) return;
		getTextWindow().output(x * 2, y, c);
	}
	
	public void print(int x, int y, String s) {
		if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) return;
		getTextWindow().setCursorPosition(x, y);
		getTextWindow().output(s);
	}
	
	public TextWindow getTextWindow() {
		return generalConsole.getTextWindow();
	}
	
	public void close() {
		System.exit(1);
	}
	
	public String display() {
		return "something";
	}
}
