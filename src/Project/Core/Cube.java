package Project.Core;

import java.util.Random;

public class Cube {
	public int[] valueX = new int[2]; // [0, 0]-[9, 9]
	public int[] valueY = new int[2]; // [0, 0]-[9, 9]
	
	public void generateValues(Random rng, int additionalForce) {
		final int fullX = rng.nextInt(76) + additionalForce;
		final int fullY = rng.nextInt(76) + additionalForce;

		valueX[0] = fullX / 10;
		valueX[1] = fullX % 10;
		valueY[0] = fullY / 10;
		valueY[1] = fullY % 10;
	}
}
