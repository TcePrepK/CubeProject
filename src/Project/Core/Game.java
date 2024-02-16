package Project.Core;

import java.util.Arrays;
import java.util.Random;

public class Game {
	private String currentState = "startMenu";
	
	private Random mainRNG = new Random();
	
	public void run() {
		this.initialize();
		this.mainGameLoop();
	}
	
	public void initialize() {
		// TODO: Initialize variables, game states!
		
		// Testing code
		for (int i = 1; i <= 4; i++) {
			Piece testPiece = new Piece(this.mainRNG, i);
			System.out.println(Arrays.toString(testPiece.cubes));			
		}
		
		Cube myCube = new Cube();
		myCube.generateValues(mainRNG, 0);
	}
	
	public void mainGameLoop() {
		// TODO: While the game is running, render the game state AND check inputs!
	}
}
