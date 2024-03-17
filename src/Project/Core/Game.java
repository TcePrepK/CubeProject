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
    	ConstructionRobot robot = new ConstructionRobot(mainRNG, 0);
    	
    	FinalRobot pcRobot1 = new FinalRobot(mainRNG);
    	FinalRobot pcRobot2 = new FinalRobot(mainRNG);
    	
    	pcRobot1.renderStats(console, 1, 0 + 40, 1);
    	pcRobot2.renderStats(console, 1, 3 + 40, 2);
    	
    	robot.render(console);
    	
    	robot.updateSelected(console, 0, 0);
    	robot.depot.updateSelected(console, 0, 0);

    	boolean isRunning = true;
    	while(isRunning) {
    		if (mouse.isLeftDown()) {
    			robot.mouseCheck(mouse, console);
    		}
    		
    		if (keyboard.isKeyPressed()) {
    			String key = keyboard.readKey();
    			
    			// If escape is pressed, close the game.
    			if (key.equals("ESCAPE")) {
    				isRunning = false;
    				break;
    			}
    			
    			robot.keyboardCheck(key, console);
    		}
    		
//    		robot.render(console);
    		
    		Thread.sleep(1);
 	    }
    	
    	console.close();
    }
}
