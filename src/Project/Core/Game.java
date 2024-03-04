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
    	Piece randomPiece = new Piece(mainRNG, 3);
    	robot.putPiece(1, 0, randomPiece);
    	
    	robot.render(console);

    	int the_last_one_selected = 0;
    	boolean isRunning = true;
    	while(isRunning) {
    		if (mouse.isLeftDown()) {
    			for(int i = 0; i < depot.pieceDepot.length; i++) {
        			if(depot.pieceDepot[i].isSelected(mouse, console)) {
        				depot.pieceDepot[i].selected = true;
        				if(the_last_one_selected != i) {
        					depot.pieceDepot[the_last_one_selected].clean(console);
        					depot.pieceDepot[the_last_one_selected].selected = false;
            				the_last_one_selected=i;
        				}
        				break;
        			}
        		}
    		}
    		
    		if (keyboard.isKeyPressed()) {
    			String key = keyboard.readKey();
    			
    			// If escape is pressed, close the game.
    			if (key.equals("ESCAPE")) {
    				isRunning = false;
    				break;
    			}
    			
        		depot.pieceDepot[the_last_one_selected].Moves(key, console);
    		}
    		
//    		robot.render(console);
    		
    		Thread.sleep(1);
 	    }
    	
    	console.close();
    }
}
