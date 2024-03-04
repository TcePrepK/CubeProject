package Project.Core;

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
				true, true, 
				false, false
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
	
	public static char[][] emptyCube = new char[][] {
		{'•', '•', '•', '•', '•'},
		{'•', ' ', ' ', ' ', '•'},
		{'•', ' ', ' ', ' ', '•'},
		{'•', ' ', ' ', ' ', '•'},
		{'•', '•', '•', '•', '•'},
	};
	
	private int[] loc = { 0, 0 };
	public boolean selected = false;
	
	public Piece(Random rng, int cubeAmount) {
		this.cubeAmount = cubeAmount;
		createRandomBlocks(rng);
	}
	
	public void render(GameConsole console, int x, int y) {
		int size = getSize();
		
		// This part renders emptyCube for every block first. So when we don't render a cube on top, it stays as dots.
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
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
		
		renderRaw(console, x, y);
	}
	
	public void renderRaw(GameConsole console, int x, int y) {
		int size = getSize();
		
		// Calls render function over every cube.
		for(int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (cubes[j][i] == null) continue;
				
				// 4 and not 5 because I want every cube to overlap (++++|+|++++) instead of (+++++|+++++).
				int cubeX = x + i * 4;
				int cubeY = y + j * 4;
				this.cubes[j][i].render(console, cubeX, cubeY);
			}
		}
	}
	
	// Rotation of the piece! Every size has its own method 3x3 being the odd one.
	public void rotateCW() {
		int size = cubes.length;
		
		switch (size) {
		case 3:
			// (x, y) -> (-y, x) is the -90 degree (CW 90) rotation function. We apply this to every cube.

			// Create a new array of cubes to not override while rotating!
			Cube[][] newCubes = new Cube[size][size];
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
		int size = getSize();
		Cube[][] newCubes = new Cube[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int newX = size - x - 1;
				newCubes[y][newX] = cubes[y][x];
			}
		}
		cubes = newCubes;
		fixTheCube();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////// Select

	public void Moves(String keyboard,GameConsole console) {
		if(keyboard.equals("3")) {
			rotateCCW();
			render(console,loc[0],loc[1]);
		}
		if(keyboard.equals("4")) {
			rotateCW();
			render(console,loc[0],loc[1]);
		}
		if(keyboard.equals("5")) {
			mirror();
			render(console,loc[0],loc[1]);
		}
	}
	
	public void clean(GameConsole console) {
		console.print(loc[0]-1, loc[1]-1, ' ');
	}
	
	public boolean isSelected(Mouse mouse,GameConsole console) {
		if (mouse.isLeftDown()) {
			String a=String.valueOf(mouse.x);
			String b=String.valueOf(mouse.y);
			if(this.cubeAmount==3||this.cubeAmount==4) {
			if(mouse.x>=loc[0] &&mouse.x<=loc[0]+12&&mouse.y>=loc[1]&&mouse.y<=loc[1]+12) {
				
				console.print(loc[0]-1, loc[1]-1, '#');
				return true;
			}
			else {
				
				return false;
			}
			}
			else if(this.cubeAmount==2) {
				if(mouse.x>=loc[0] &&mouse.x<=loc[0]+8&&mouse.y>=loc[1]&&mouse.y<=loc[1]+8) {
					
					console.print(loc[0]-1, loc[1]-1, '#');
					return true;
				}
				else {
					
					return false;
				}
			}
			else if(this.cubeAmount==1) {
				if(mouse.x>=loc[0] &&mouse.x<=loc[0]+4&&mouse.y>=loc[1]&&mouse.y<=loc[1]+4) {
					
					console.print(loc[0]-1, loc[1]-1, '#');
					return true;
				}
				else {
					
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			
			return false;
			}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////// Misc functions
	
	// After rotation or mirror, moves the cubes to most left/top part of the piece.
	private void fixTheCube() {
		int size = getSize();
		boolean leftEmpty = true;
		boolean topEmpty = true;

		// Check left most cubes to know if we are going to move left or not.
		for (int i = 0; i < size; i++) {
			if (cubes[i][0] != null) leftEmpty = false;
		}
		
		// Same but for the top
		for (int i = 0; i < size; i++) {
			if (cubes[0][i] != null) topEmpty = false;
		}
		
		while (leftEmpty || topEmpty) {
			// dx and dy values are movement values. If dx is -1, every cube will go 1 left (-1).
			int dx = leftEmpty ? -1 : 0;
			int dy = topEmpty ? -1 : 0;
			
			for (int x = -dx; x < size; x++) {
				for (int y = -dy; y < size; y++) {
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
		int size = 0;
		switch (cubeAmount) {
			case 1:
				additionalForce = 0;
				size = 1;
				break;
			case 2:
				additionalForce = 6;
				size = 2;
				break;
			case 3:
				additionalForce = 12;
				size = 3;
				break;
			case 4:
				additionalForce = 24;
				size = 3;
				break;
		}
		
		// Create the array and cubes for this piece.
		cubes = new Cube[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (!selectedPiece[j * size + i]) continue;
				cubes[j][i] = new Cube(rng, additionalForce);
			}
		}
	}
	
	public int getSize() {
		return cubes.length;
	}
	
	public void setPosition(int x, int y) {
		loc[0] = x;
		loc[1] = y;
	}
}
