package Project.Core;

import java.util.Arrays;
import java.util.Random;

public class Piece {
	public int cubeAmount; // 4, 3, 2, 1
	public Cube[][] cubes;
	
	// TODO: Grosses way to do this, maybe rework ? For now, it works (I think)!
	private boolean[][][] possiblePiecesBySize = new boolean[][][] {
		{ 
			{ 
				true 
			} // 1-Cube 1
		},
		{ 
			{ 
				false, false, 
				true, true 
			} // 2-Cube 1
		},			
		{ 
			{ 
				false, true, false, 
				true, true, false, 
				false, false, false 
			}, // 3-Cube 1
			{ 
				true, true, true, 
				false, false, false, 
				false, false, false 
			} // 3-Cube 1
		},			
		{ 
			{ 
				true, true, true, 
				true, false, false, 
				false, false, false 
			}, // 4-Cube 1
			{ 
				false, true, false, 
				true, true, true, 
				false, false, false 
			}, // 4-Cube 2
			{ 
				false, true, true, 
				true, true, false, 
				false, false, false 
			}, // 4-Cube 3
			{ 
				true, true, false, 
				true, true, false, 
				false, false, false 
			} // 4-Cube 4
		},
	};
	
	private char[][] emptyCube = new char[][] {
		{'•', '•', '•', '•', '•'},
		{'•', ' ', ' ', ' ', '•'},
		{'•', ' ', ' ', ' ', '•'},
		{'•', ' ', ' ', ' ', '•'},
		{'•', '•', '•', '•', '•'},
	};
	
	public Piece(Random rng, int cubeAmount) {
		this.cubeAmount = cubeAmount;
		createRandomBlocks(rng);
	}
	
	public void render(GameConsole console, int x, int y) {
		int width = cubes.length;
		
		// This part renders emptyCube for every block first. So when we don't render a cube on top, it stays as dots.
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				// 4 and not 5 because I want every cube to overlap (....|.|....) instead of (.....|.....).
				int cubeX = x + i * 4;
				int cubeY = y + j * 4;
				
				for (int cx = 0; cx < 5; cx++) {
					for (int cy = 0; cy < 5; cy++) {
						console.print(cubeX + cx, cubeY + cy, emptyCube[cy][cx]);
					}
				}
			}
		}
		
		// Calls render function over every cube.
		for(int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (cubes[j][i] == null) continue;
				
				// 4 and not 5 because I want every cube to overlap (++++|+|++++) instead of (+++++|+++++).
				int cubeX = x + i * 4;
				int cubeY = y + j * 4;
				this.cubes[j][i].render(console, cubeX, cubeY);
			}
		}
	}
	
	// Rotation of the piece! Every width has its own method 3x3 being the odd one.
	public void rotateCW() {
		int width = cubes.length;
		
		switch (width) {
		case 3:
			// (x, y) -> (-y, x) is the -90 degree (CW 90) rotation function. We apply this to every cube.

			// Create a new array of cubes to not override while rotating!
			Cube[][] newCubes = new Cube[width][width];
			for (int oldX = -1; oldX < 2; oldX++) {
				for (int oldY = -1; oldY < 2; oldY++) {
					if (cubes[oldY + 1][oldX + 1] == null) continue;
					int newX = -oldY;
					int newY = oldX;
					
					cubes[oldY + 1][oldX + 1].rotate();
					newCubes[newY + 1][newX + 1] = cubes[oldY + 1][oldX + 1];
				}
			}
			cubes = newCubes;
			
			// After rotation we fix the cube (move left/top).
			fixTheCube();
			break;
		case 2:
			if (cubes[0][1] != null) {
				cubes[1][0] = cubes[0][1];
				cubes[0][1] = null;
				
				cubes[0][0].rotate();
				cubes[1][0].rotate();
			} else {
				cubes[0][1] = cubes[0][0];
				cubes[0][0] = cubes[1][0];
				cubes[1][0] = null;
				
				cubes[0][0].rotate();
				cubes[0][1].rotate();
			}
			break;
		case 1:
			cubes[0][0].rotate();
			break;
		}
	}
	
	// This is funny but clever.
	public void rotateCCW() {
		for (int i = 0; i < 3; i++) rotateCW();
	}
	
	public void mirror() {
		int width = cubes.length;
		Cube[][] newCubes = new Cube[width][width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < width; y++) {
				int newX = width - x - 1;
				int newY = width - y - 1;
				newCubes[newY][newX] = cubes[y][x];
			}
		}
		cubes = newCubes;
		fixTheCube();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////// Misc functions
	
	// After rotation or mirror, moves the cubes to most left/top part of the piece.
	private void fixTheCube() {
		int width = 3;
		boolean leftEmpty = true;
		boolean topEmpty = true;

		// Check left most cubes to know if we are going to move left or not.
		for (int i = 0; i < 3; i++) {
			if (cubes[i][0] != null) leftEmpty = false;
		}
		
		// Same but for the top
		for (int i = 0; i < 3; i++) {
			if (cubes[0][i] != null) topEmpty = false;
		}
		
		while (leftEmpty || topEmpty) {
			// dx and dy values are movement values. If dx is -1, every cube will go 1 left (-1).
			int dx = leftEmpty ? -1 : 0;
			int dy = topEmpty ? -1 : 0;
			
			for (int x = -dx; x < width; x++) {
				for (int y = -dy; y < width; y++) {
					if (cubes[y][x] == null) continue;
					
					int newX = x + dx;
					int newY = y + dy;
					
					// If any of the newX or newY values are 0, that side can't be empty anymore.
					if (newX == 0) leftEmpty = false;
					if (newY == 0) topEmpty = false;
					
					// Move them to new position and set previous location empty.
					cubes[newY][newX] = cubes[y][x];
					cubes[y][x] = null;
				}
			}
		}
	}
	
	// Chooses a random set of cubes for this piece. Set is predefined as "possiblePiecesBySize".
	private void createRandomBlocks(Random rng) {
		boolean[][] piecesSet = possiblePiecesBySize[cubeAmount - 1];
		boolean[] selectedPiece = piecesSet[rng.nextInt(piecesSet.length)];
		
		// Additional force depends on the cube amount and effects every cube within this piece.
		int additionalForce = 0;
		int width = 0;
		switch (cubeAmount) {
			case 1:
				additionalForce = 0;
				width = 1;
				break;
			case 2:
				additionalForce = 6;
				width = 2;
				break;
			case 3:
				additionalForce = 12;
				width = 3;
				break;
			case 4:
				additionalForce = 24;
				width = 3;
				break;
		}
		
		// Create the array and cubes for this piece.
		cubes = new Cube[width][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (!selectedPiece[j * width + i]) continue;
				cubes[j][i] = new Cube(rng, additionalForce);
			}
		}
	}
}
