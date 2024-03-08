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
    	PieceDepot depot = new PieceDepot(mainRNG);
    	depot.render(console, 50, 1);
    	
    	ConstructionRobot robot = new ConstructionRobot();
//    	Piece randomPiece = new Piece(mainRNG, 3);
//    	robot.putPiece(1, 0, randomPiece);
    	
    	robot.render(console);

    	boolean isRunning = true;
    	while(isRunning) {
    		if (mouse.isLeftDown()) {
    			depot.mouseCheck(mouse, console);
    			robot.mouseCheck(mouse, console);
    		}
    		
    		if (keyboard.isKeyPressed()) {
    			String key = keyboard.readKey();
    			
    			// If escape is pressed, close the game.
    			if (key.equals("ESCAPE")) {
    				isRunning = false;
    				break;
    			}
    			
    			depot.keyboardCheck(key, console);
    		}
    		
//    		robot.render(console);
    		
    		Thread.sleep(1);
 	    }
    	
    	console.close();
    }
}
