package Project.Core;

import java.util.Random;

public class Cube {
	private int valueX = 0; // [0, 0]-[9, 9]
	private int valueY = 0; // [0, 0]-[9, 9]
	
	private char[][] printValues = new char[][] {
		{'+', '+', '+', '+', '+'},
		{'+', ' ', ' ', ' ', '+'},
		{'+', ' ', ' ', ' ', '+'},
		{'+', ' ', ' ', ' ', '+'},
		{'+', '+', '+', '+', '+'},
	};
	
	public Cube(Random rng, int min, int max) {
		initialize(rng, min, max);
	}
	
	public Cube(Random rng, int additionalForce) {
		initialize(rng, additionalForce, 75 + additionalForce);
	}
	
	private void initialize(Random rng, int min, int max) {
		// Sets values randomly depending on minimum and maximum values.
		valueX = rng.nextInt(min, max + 1);
		valueY = rng.nextInt(min, max + 1);

		// Depending on the values set the "printValues" correctly.
		printValues[1][2] = (char) ('0' + (valueY / 10));
		printValues[2][1] = (char) ('0' + (valueX / 10));
		printValues[3][2] = (char) ('0' + (valueY % 10));
		printValues[2][3] = (char) ('0' + (valueX % 10));
	}
	
	// Rotates the cube meaning just changes the X and Y values.
	public void rotate() {
		int temp = valueX;
		valueX = valueY;
		valueY = temp;
	
		// Fix the values again.
		printValues[1][2] = (char) ('0' + (valueY / 10));
		printValues[2][1] = (char) ('0' + (valueX / 10));
		printValues[3][2] = (char) ('0' + (valueY % 10));
		printValues[2][3] = (char) ('0' + (valueX % 10));
	}
	
	// To render all we do is printing the "printValues"
	public void render(GameConsole console, int x, int y) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				console.print(x + i, y + j, printValues[j][i]);
			}
		}
	}
	
	// Getters
	public int yForce() {
		return valueY;
	}
	
	public int xForce() {
		return valueX;
	}
}