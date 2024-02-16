package Project.Core;

import java.util.Arrays;
import java.util.Random;

public class Piece {
	public int cubeAmount; // 4, 3, 2, 1
	public Cube[] cubes;
	
	// TODO: Grosses way to do this, maybe rework ? For now, it works (I think)!
	private boolean[][][] possiblePiecesBySize = new boolean[][][] {
		{ 
			{ true } // 1-Cube 1
		},
		{ 
			{ false, false, true, true } // 2-Cube 1
		},			
		{ 
			{ false, true, false, false, true, true, false, false, false }, // 3-Cube 1
			{ false, false, false, false, false, false, true, true, true } // 3-Cube 1
		},			
		{ 
			{ false, false, false, true, false, false, true, true, true }, // 4-Cube 1
			{ false, false, false, false, true, false, true, true, true }, // 4-Cube 2
			{ false, true, true, true, true, false, false, false, false }, // 4-Cube 3
			{ true, true, false, true, true, false, false, false, false } // 4-Cube 4
		},
	};
	
	public Piece(Random rng, int cubeAmount) {
		this.cubeAmount = cubeAmount;
		this.createRandomBlocks(rng);
	}
	
	private void createRandomBlocks(Random rng) {
		boolean[][] piecesSet = this.possiblePiecesBySize[this.cubeAmount - 1];
		boolean[] selectedPiece = piecesSet[rng.nextInt(piecesSet.length)];
		
		int additionalForce = 0;
		switch (this.cubeAmount) {
			case 1:
				additionalForce = 0;
				break;
			case 2:
				additionalForce = 6;
				break;
			case 3:
				additionalForce = 12;
				break;
			case 4:
				additionalForce = 24;
				break;
		}
		
		this.cubes = new Cube[selectedPiece.length];
		for (int i = 0; i < this.cubes.length; i++) {
			if (!selectedPiece[i]) continue;
			this.cubes[i] = new Cube();
			this.cubes[i].generateValues(rng, additionalForce);
		}
	}
}
