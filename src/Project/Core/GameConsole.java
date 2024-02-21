package Project.Core;

import enigma.console.Console;
import enigma.console.TextWindow;
import enigma.core.Enigma;

// This console class is just to manage print functions easier and remove the code spaghetti.
public class GameConsole {
	public Console generalConsole;
	
	public GameConsole(String name) {
		generalConsole = Enigma.getConsole(name);
	}
	
	public void print(int x, int y, char c) {
		getTextWindow().output(x, y, c);
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
