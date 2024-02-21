package Project.Core;

import java.util.Random;

public class Cube {
	public int valueX = 0; // [0, 0]-[9, 9]
	public int valueY = 0; // [0, 0]-[9, 9]
	
	public Cube(Random rng, int min, int max) {
		this.initialize(rng, min, max);
	}
	
	public Cube(Random rng, int additionalForce) {
		this.initialize(rng, additionalForce, 75 + additionalForce);
	}
	
	private void initialize(Random rng, int min, int max) {
		valueX = rng.nextInt(max - min + 1) + min;
		valueY = rng.nextInt(max - min + 1) + min;
	}
	
	public int yForce() {
		return valueY;
	}
	
	public int xForce() {
		return valueX;
	}
}