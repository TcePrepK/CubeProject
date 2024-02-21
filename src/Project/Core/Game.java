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
    	boolean isRunning = true;
    	int testX = 0;
    	int testY = 0;
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
    			
    			System.out.println(key);
    			// Arrow Keys
    			switch (key) {
    				case "LEFT":
	    				testX -= 1;
	    				break;
	    			case "RIGHT":
	    				testX += 1;
	    				break;
	    			case "UP":
	    				testY -= 1;
	    				break;
	    			case "DOWN":
	    				testY += 1;
	    				break;
    			}
    			
    			console.print(testX, testY, '#');
    		}
    		Thread.sleep(1);
 	    }
    	
    	console.close();
    }
}
