package Project.Core;

import java.awt.Color;
import java.util.Random;

public class ConstructionRobot {
	private boolean[][] possiblePlaces = {
			{ false, false, true, false, false },
			{ true, true, true, true, true },
			{ false, true, true, true, false },
			{ false, true, true, true, false },
			{ false, true, false, true, false },
	};
	
	private int[][] placeToIndex = {
			{ -1, -1, 0, -1, -1 },
			{ 4, 5, 1, 6, 7 },
			{ -1, 8, 2, 11, -1 },
			{ -1, 9, 3, 12, -1 },
			{ -1, 10, -1, 13, -1 },
	};
	
	private Cube[] bodyParts = new Cube[14];
	private boolean full = false;
	
	private Stack placedPieces = new Stack(14);
	private int[] selectedGrid = { 0, 0 };
	private final int robotNumber;
	
	public PieceDepot depot;
	
	public ConstructionRobot(Random mainRNG, PieceDepot depot, int robotNumber) {
		this.depot = depot;
		this.robotNumber = robotNumber;
	}
	
	private boolean isPlaceable(int x, int y, Piece piece) {
		if (piece.usedOnRobot[robotNumber]) return false;

		int size = piece.getSize();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (piece.cubes[j][i] != null) {
					if (!possiblePlaces[y + j][x + i]) return false;
				}
			}
		}

		return true;
	}
	
	public boolean putPiece(int x, int y, Piece piece) {
		if (!isPlaceable(x, y, piece)) return false;
		
		piece.usedOnRobot[robotNumber] = true;
		piece.robotX[robotNumber] = x;
		piece.robotY[robotNumber] = y;
		placedPieces.push(piece.clone());
		int size = piece.getSize();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (piece.cubes[j][i] == null) continue;
				possiblePlaces[y + j][x + i] = false;
				bodyParts[placeToIndex[y + j][x + i]] = piece.cubes[j][i].clone();
			}
		}
		
		return true;
	}
	
	private boolean removePiece(Piece piece) {
		if (!piece.usedOnRobot[robotNumber]) return false;
		int x = piece.robotX[robotNumber];
		int y = piece.robotY[robotNumber];
		
		piece.usedOnRobot[robotNumber] = false;
		piece.robotX[robotNumber] = -1;
		piece.robotY[robotNumber] = -1;
		
		int amount = placedPieces.size();
		Stack newStack = new Stack(amount);
		for (int i = 0; i < amount; i++) {
			Piece data = (Piece) placedPieces.pop();
			if (data.robotX != piece.robotX || data.robotY != piece.robotY) {
				newStack.push(data);
				continue;
			}
			
			int size = piece.getSize();
			for (int w = 0; w < size; w++) {
				for (int h = 0; h < size; h++) {
					if (piece.cubes[h][w] == null) continue;
					possiblePlaces[y + h][x + w] = true;
					bodyParts[placeToIndex[y + h][x + w]] = null;
				}
			}
		}
		
		for (int i = 0; i < amount - 1; i++) {
			placedPieces.push(newStack.pop());
		}
		
		return true;
	}
	
	private void checkForFull(GameConsole console) {
		full = true;
		for (int i = 0; i < 14; i++) if (bodyParts[i] == null) {
			full = false;
		};
		
		if (isFull()) {
			console.setColor(new Color(255, 100, 100));
			console.print(11, 28, "Robot is full! Press (X) to continue.");
			console.resetColor();
		} else {
			console.print(11, 28, " ".repeat(38));
		}
	}
	
	public void mouseCheck(Mouse mouse, GameConsole console) {
		boolean updated = depot.mouseCheck(mouse, console);
		
		int x = mouse.x - 4;
		int y = mouse.y - 4;
		
		int gridX = selectedGrid[0];
		int gridY = selectedGrid[1];
		if (!(x < 0 || y < 0 || x > 20 || y > 20)) {			
			gridX = x / 4;
			gridY = y / 4;
			updated = gridX != selectedGrid[0] || gridY != selectedGrid[1];
		}
		
		if (updated) updateSelected(console, gridX, gridY);
	}
	
	public void keyboardCheck(String key, GameConsole console) {
		boolean pieceUpdated = depot.keyboardCheck(key, console, robotNumber);
		
		Piece selected = depot.getSelectedPiece();
		if (key.equals("1")) {
			// Place Piece
			pieceUpdated = putPiece(selectedGrid[0], selectedGrid[1], selected);
		} else if (key.equals("2")) {
			// Remove Piece
			pieceUpdated = removePiece(selected);
		}
		
		int dx = key.equals("A") ? -1 : key.equals("D") ? 1 : 0;
		int dy = key.equals("W") ? -1 : key.equals("S") ? 1 : 0;
		if (!pieceUpdated && dx == 0 && dy == 0) return;
		
		int newX = (selectedGrid[0] + dx + 5) % 5;
		int newY = (selectedGrid[1] + dy + 5) % 5;
		updateSelected(console, newX, newY);
	}
	
	public void updateSelected(GameConsole console, int newX, int newY) {
		selectedGrid[0] = newX;
		selectedGrid[1] = newY;

		clean(console, 4, 4);
		render(console);
		checkForFull(console);
		
		Piece selectedPiece = depot.getSelectedPiece();
		boolean holdingPlacedPiece = selectedPiece.usedOnRobot[robotNumber];
		
		// Figure out if any of blocks are outside of robot or not.
		boolean inside = true;
		int size = selectedPiece.getSize();
		for (int i = 0; i < size && inside; i++) {
			for (int j = 0; j < size && inside; j++) {
				if (selectedPiece.cubes[j][i] == null) continue;	
				if (newX + i < 0 || newX + i >= 5 || newY + j < 0 || newY + j >= 5) inside = false;
			}
		}
		
		if (holdingPlacedPiece) {
			// Holding an already placed piece.
			
			int pieceX = selectedPiece.robotX[robotNumber] * 4 + 4;
			int pieceY = selectedPiece.robotY[robotNumber] * 4 + 4;
			
			Piece pieceOverRobot = null;			
			int amount = placedPieces.size();
			Stack newStack = new Stack(amount);
			for (int i = 0; i < amount; i++) {
				Piece data = (Piece) placedPieces.pop();
				if (data.robotX == selectedPiece.robotX && data.robotY == selectedPiece.robotY) pieceOverRobot = data;
				newStack.push(data);
			}
			
			for (int i = 0; i < amount; i++) {
				placedPieces.push(newStack.pop());
			}
			
			for(int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (pieceOverRobot.cubes[j][i] == null) continue;
					
					int cubeX = pieceX + i * 4;
					int cubeY = pieceY + j * 4;
					for (int w = 0; w < 5; w++) {
						console.print(cubeX + w, cubeY, '✖');
						console.print(cubeX + w, cubeY + 4, '✖');
					}
					for (int h = 0; h < 5; h++) {
						console.print(cubeX, cubeY + h, '✖');
						console.print(cubeX + 4, cubeY + h, '✖');
					}
					
					if (j > 0 && pieceOverRobot.cubes[j - 1][i] != null) {
						for (int w = 1; w < 4; w++) {
							console.print(cubeX + w, cubeY, ' ');
						}
					}
					if (i > 0 && pieceOverRobot.cubes[j][i - 1] != null) {
						for (int h = 1; h < 4; h++) {
							console.print(cubeX, cubeY + h, ' ');
						}
					}
				}
			}
		} else if (inside) {
			// Selected piece is not used yet, render the selected one.
			
			int pieceX = newX * 4 + 4;
			int pieceY = newY * 4 + 4;
			
			Color border = new Color(200, 200, 200);
			console.setColor(border);
			for(int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (selectedPiece.cubes[j][i] == null) continue;
					
					boolean canPlace = false;
					if (newX + i < 5 && newY + j < 5) {
						canPlace = possiblePlaces[newY + j][newX + i];
					}
					
					int cubeX = pieceX + i * 4;
					int cubeY = pieceY + j * 4;
					for (int w = 0; w < 5; w++) {
						console.print(cubeX + w, cubeY, 'O');
						console.print(cubeX + w, cubeY + 4, 'O');
					}
					for (int h = 0; h < 5; h++) {
						console.print(cubeX, cubeY + h, 'O');
						console.print(cubeX + 4, cubeY + h, 'O');
					}
					
					if (j > 0 && selectedPiece.cubes[j - 1][i] != null) {
						for (int w = 1; w < 4; w++) {
							console.print(cubeX + w, cubeY, ' ');
						}
					}
					if (i > 0 && selectedPiece.cubes[j][i - 1] != null) {
						for (int h = 1; h < 4; h++) {
							console.print(cubeX, cubeY + h, ' ');
						}
					}
					
					if (canPlace) {
						console.setColor(new Color(0, 255, 0));
						console.print(cubeX + 2, cubeY + 2, '✔');						
					} else {
						console.setColor(new Color(255, 0, 0));
						console.print(cubeX + 2, cubeY + 2, '✖');												
					}
					console.setColor(border);
				}
			}
			console.resetColor();
		}
		
		console.print(newX * 4 + 5, newY * 4 + 7, '\\');
		console.print(newX * 4 + 5, newY * 4 + 5, '/');
		console.print(newX * 4 + 7, newY * 4 + 5, '\\');
		console.print(newX * 4 + 7, newY * 4 + 7, '/');
		
		console.print(newX * 4 + 6, newY * 4 + 5, '‾');
		console.print(newX * 4 + 6, newY * 4 + 7, '_');
		console.print(newX * 4 + 5, newY * 4 + 6, '|');
		console.print(newX * 4 + 7, newY * 4 + 6, '|');
	}
	
	public void render(GameConsole console) {
		depot.render(console, 35, 1);
		
		console.print(4, 2, "+ " + "-".repeat(46) + ">  X");
		for (int i = 1; i < 25; i++) {
			console.print(2, 2 + i, '║');
		}
		console.print(2, 2 + 25, 'V');
		console.print(2, 2 + 27, 'Y');
		
		for (int i = 0; i < 5; i++) {
			int pos = i * 4 + 6;
			
			console.print(pos, 2, (char) ('0' + i + 1));
			console.print(pos * 2 - 1, 2, " ");
			console.print(pos * 2 + 1, 2, " ");
			
			console.print(2, pos, (char) ('0' + i + 1));
			console.print(2, pos - 1, " ");
			console.print(2, pos + 1, " ");
		}
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (!possiblePlaces[j][i]) continue;
				for (int w = 0; w < 5; w++) {
					for (int h = 0; h < 5; h++) {
						console.print(i * 4 + w + 4, j * 4 + h + 4, Piece.emptyCube[w][h]);
					}
				}
			}
		}
		
		int size = placedPieces.size();
		Stack newStack = new Stack(size);
		for (int i = 0; i < size; i++) {
			Piece data = (Piece) placedPieces.pop();
			newStack.push(data);
			data.renderRaw(console, data.robotX[robotNumber] * 4 + 4, data.robotY[robotNumber] * 4 + 4);
		}
		for (int i = 0; i < size; i++) {
			placedPieces.push(newStack.pop());
		}
		
		int statY = 32;
		console.print(4, statY, "Current Piece (#): " + depot.getSelectedNumber());
		console.print(4, statY + 1, "Cursor Position: (X: " + (selectedGrid[0] + 1) + ", " + (selectedGrid[1] + 1) + ")");
		console.print(4, statY + 2, "Used Pieces (=/-): ");
		
		console.setColor(new Color(150, 150, 150), null);
		for (int i = 0; i < 20; i++) {
			int x, y;
			if (i >= 13) {
				x = i - 13;
				y = 3;
			} else if (i >= 8) {
				x = i - 8;
				y = 2;
			} else if (i >= 4) {
				x = i - 4;
				y = 1;
			} else {
				x = i;
				y = 0;
			}
			
			int usedAmount = depot.pieceDepot[i].getUsedAmount();
			String key = usedAmount == 0 ? " " : usedAmount == 1 ? "-" : "=";
			console.print(4 + x * 5, statY + 3 + y, key + (i >= 9 ? "" : "0") + (i + 1));
		}
		console.resetColor();
		
		for (int x = 15; x < 25; x++) {
			for (int y = statY + 9; y < statY + 16; y++) {
				console.print(x, y, " ");
			}
		}
		
		console.print(4, statY + 8, "Human Robot " + (robotNumber + 1) + "(HR" + (robotNumber + 1) + ")");
		FinalRobot statRobot = finishTheRobot();
		
		console.setColor(new Color(100, 100, 255));
		console.print(5, statY + 9, "> Intelligence: " + statRobot.getIn());
		
		console.setColor(new Color(255, 100, 100));
		console.print(5, statY + 10, "> Skill: " + statRobot.getSk());
		console.print(6, statY + 11, "-> Left Arm: " + statRobot.getLA());
		console.print(6, statY + 12, "-> Right Arm: " + statRobot.getRA());
		
		console.setColor(new Color(150, 255, 150));
		console.print(5, statY + 13, "> Speed: " + statRobot.getSp());
		console.print(6, statY + 14, "-> Left Leg: " + statRobot.getLL());
		console.print(6, statY + 15, "-> Right Leg: " + statRobot.getRL());
		console.resetColor();
	}
	
	public boolean isFull() {
//		return full;
		return true;
	}
	
	public FinalRobot finishTheRobot() {
		return new FinalRobot(bodyParts, robotNumber);
	}
	
	private void clean(GameConsole console, int x, int y) {
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 30; j++) {
				console.print(x + i - 1, y + j - 1, ' ');
			}
		}
	}
}
