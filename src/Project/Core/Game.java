package Project.Core;

import java.util.Random;

public class Game {
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
    	PieceDepot mainDepot = new PieceDepot(mainRNG);
    	
    	FinalRobot[] humanRobots = new FinalRobot[2];
    	FinalRobot[] computerRobots = new FinalRobot[2];

      	int totalHumanRobots = 0;    	
    	ConstructionRobot currentRobot = new ConstructionRobot(mainRNG, mainDepot, totalHumanRobots);
    	currentRobot.render(console);
    	currentRobot.updateSelected(console, 0, 0);
    	currentRobot.depot.updateSelected(console, 0, 0);

    	boolean isInGames = false;
    	boolean isRunning = true;
    	while(isRunning) {
    		if (!isInGames) {
    			if (mouse.isLeftDown()) {
    				currentRobot.mouseCheck(mouse, console);
    			}
    			
    			if (keyboard.isKeyPressed()) {
    				String key = keyboard.readKey();
    				
    				// If escape is pressed, close the game.
    				if (key.equals("ESCAPE")) {
    					isRunning = false;
    					break;
    				}
    				
    				if (key.equals("X") && currentRobot.isFull()) {
    					humanRobots[totalHumanRobots] = currentRobot.finishTheRobot();
    					computerRobots[totalHumanRobots] = new FinalRobot(mainRNG, totalHumanRobots);
    					totalHumanRobots++;
    					
    					if (totalHumanRobots < 2) {
    						currentRobot = new ConstructionRobot(mainRNG, mainDepot, totalHumanRobots);
    						currentRobot.render(console);
    						currentRobot.updateSelected(console, 0, 0);
    						
    						humanRobots[0].renderStats(console, 49);
        			    	computerRobots[0].renderStats(console, 52);
    					} else {
    						isInGames = true;
//    						console.cleanScreen();
    						continue;
    					}
    					
    				}
    				
    				currentRobot.keyboardCheck(key, console);
    			}
    		} else {
    			// Render the games.
    		}
    		
    		Thread.sleep(1);
 	    }
    	
    	console.close();
    }
}
