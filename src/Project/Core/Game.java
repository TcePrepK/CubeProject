package Project.Core;

import java.util.Random;

public class Game {
	private String currentState = "startMenu";
	
	private Random mainRNG = new Random();
	
	private GameConsole console;
	private Keyboard keyboard;
	private Mouse mouse;
   
    public Game() throws Exception {
    	console = new GameConsole("Cube Project");
    	keyboard = new Keyboard(console.getTextWindow());
    	mouse = new Mouse(console.getTextWindow());
    }
    
    public void run() throws InterruptedException {
    	console.print(2, 0, "Press SPACE to generate new sets");
    	
    	boolean isRunning = true;
    	while(isRunning) {
    		if (mouse.isLeftDown()) {
    			console.print(mouse.x, mouse.y, '#');
    		}
    		
    		if (keyboard.isKeyPressed()) {
    			String key = keyboard.readKey();
    			
    			// If escape is pressed, close the game.
    			if (key.equals("ESCAPE")) {
    				isRunning = false;
    				break;
    			}
    			
    			if (key.equals("SPACE")) {
    				int offX = 1;
    				int offY = 3;
    				// Test Place 
    				Piece testPiece = new Piece(new Random(), 3);
    				testPiece.render(console, offX, offY);
    				testPiece.rotateCW();
    				testPiece.render(console, 15 + offX, offY);
    				testPiece.rotateCW();
    				testPiece.render(console, offX, 15 + offY);
    				testPiece.rotateCW();
    				testPiece.render(console, 15 + offX, 15 + offY);
    				testPiece.rotateCW();
    				testPiece.mirror();
    				testPiece.render(console, 30 + offX, offY);
    				testPiece.rotateCW();
    				testPiece.render(console, 30 + offX, 15 + offY);
    				
    				console.print(offX + 1, offY - 1, "Original Form");
    				console.print(30 + offX + 1, offY - 1, "Rotated Once");
    				console.print(offX + 1, 15 + offY - 1, "Rotated Twice");
    				console.print(30 + offX + 1, 15 + offY - 1, "Rotated Thrice");
    				console.print(60 + offX + 1, offY - 1, "Original Mirrored");
    				console.print(60 + offX + 1, 15 + offY - 1, "Original Mirrored, Rotated");
    			}
    		}
    		Thread.sleep(1);
 	    }
    	
    	console.close();
    }
}
