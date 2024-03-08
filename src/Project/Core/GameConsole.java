package Project.Core;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import enigma.console.Console;
import enigma.console.java2d.Java2DTextWindow;
import enigma.core.Enigma;

// This console class is just to manage print functions easier and remove the code spaghetti.
public class GameConsole {
	private int MAX_CHARS_X, MAX_CHARS_Y;
	private int FONT_SIZE = 12;
	public Console generalConsole;
	
	public GameConsole(String name) {
        generalConsole = Enigma.getConsole(name, 60, 40, FONT_SIZE);
		
		// Don't look here... Just a few lines of codes to full-screen the window. I had to do this because Enigma sucks.
        Java2DTextWindow textWindow = getTextWindow();
        JFrame frame = (JFrame) textWindow.getParent().getParent().getParent().getParent().getParent().getParent();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        // This part makes the window full-screen and sets the max char variables.
        textWindow.setVisible(false);
        frame.setVisible(false);
        
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gd.setFullScreenWindow(frame);
        
        // TODO: Check! Setting a new font breaks it for some reason :?
        
//        Font newFont = new Font("Noto Sans", Font.PLAIN, FONT_SIZE);
//        textWindow.setFont(newFont);
        
        MAX_CHARS_X = textWindow.getColumns();
		MAX_CHARS_Y = textWindow.getRows();
		
		frame.setVisible(true);
		textWindow.setVisible(true);
	}
	
	public void print(int x, int y, char c) {
		if (x < 0 || x * 2 >= MAX_CHARS_X || y < 0 || y >= MAX_CHARS_Y) return;
		getTextWindow().output(x * 2, y, c);
	}
	
	public void print(int x, int y, String s) {
		if (x < 0 || x + s.length() >= MAX_CHARS_X || y < 0 || y >= MAX_CHARS_Y) return;
		getTextWindow().setCursorPosition(x, y);
		getTextWindow().output(s);
	}
	
	public Java2DTextWindow getTextWindow() {
		return (Java2DTextWindow) generalConsole.getTextWindow();
	}
	
	public void close() {
		System.exit(1);
	}
	
	public String display() {
		return "something";
	}
}
