package Project.Core;

import java.util.Random;

public class ConstructionRobot {
	private boolean[][] possiblePlaces = {
			{ false, false, true, false, false },
			{ true, true, true, true, true },
			{ false, true, true, true, false },
			{ false, true, true, true, false },
			{ false, true, false, true, false },
	};
	
	private class PieceByPosition {
		public final int x, y;
		public final Piece piece;
		
		public PieceByPosition(int x, int y, Piece piece) {
			this.x = x;
			this.y = y;
			this.piece = piece.clone();
		}
		
		public boolean contains(int x, int y) {
			int dx = x - this.x;
			int dy = y - this.y;
			int size = piece.getSize();
			if (dx < 0 || dx >= size || dy < 0 || dy >= size) return false;
			return piece.cubes[dy][dx] != null;
		}
	}
	
	private Stack placedPieces = new Stack(14);
	private int[] selectedGrid = { 0, 0 };
	
	public PieceDepot depot;
	
	public ConstructionRobot(Random mainRNG) {
		depot = new PieceDepot(mainRNG);
	}
	
	private boolean isPlaceable(int x, int y, Piece piece) {
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
	
	public void putPiece(int x, int y, Piece piece) {
		if (!isPlaceable(x, y, piece)) return;
		
		placedPieces.push(new PieceByPosition(x, y, piece));
		int size = piece.getSize();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (piece.cubes[j][i] == null) continue;
				possiblePlaces[y + j][x + i] = false;
			}
		}
	}
	
	private void removePiece(PieceByPosition removed) {
		int amount = placedPieces.size();
		Stack newStack = new Stack(amount);
		for (int i = 0; i < amount; i++) {
			PieceByPosition data = (PieceByPosition) placedPieces.pop();
			if (data != removed) {
				newStack.push(data);
				continue;
			}
			
			int x = data.x;
			int y = data.y;
			int size = data.piece.getSize();
			for (int w = 0; w < size; w++) {
				for (int h = 0; h < size; h++) {
					if (data.piece.cubes[h][w] == null) continue;
					possiblePlaces[y + h][x + w] = true;
				}
			}
		}
		for (int i = 0; i < amount - 1; i++) {
			placedPieces.push(newStack.pop());
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
		boolean pieceUpdated = depot.keyboardCheck(key, console);
		
		if (key.equals("1")) {
			// Place Piece
			putPiece(selectedGrid[0], selectedGrid[1], depot.getSelectedPiece());
			pieceUpdated = true;
		} else if (key.equals("2")) {
			removePiece(findSelectedPiece());
			pieceUpdated = true;
			// Remove Piece
		}
		
		int dx = key.equals("A") ? -1 : key.equals("D") ? 1 : 0;
		int dy = key.equals("W") ? -1 : key.equals("S") ? 1 : 0;
		if (!pieceUpdated && dx == 0 && dy == 0) return;
		
		int newX = (selectedGrid[0] + dx + 5) % 5;
		int newY = (selectedGrid[1] + dy + 5) % 5;
		updateSelected(console, newX, newY);
	}
	
	public void updateSelected(GameConsole console, int newX, int newY) {
		console.getTextWindow().repaint();
		selectedGrid[0] = newX;
		selectedGrid[1] = newY;

		clean(console, 4, 4);
		render(console);
		
		PieceByPosition selectedPiece = findSelectedPiece();
		if (possiblePlaces[newY][newX] || selectedPiece == null) {
			// Empty, render the piece currently holding.
			Piece piece = depot.getSelectedPiece();
			
			int pieceX = newX * 4 + 4;
			int pieceY = newY * 4 + 4;
			int size = piece.getSize();
			
			for(int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (piece.cubes[j][i] == null) continue;
					
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
					
					if (j > 0 && piece.cubes[j - 1][i] != null) {
						for (int w = 1; w < 4; w++) {
							console.print(cubeX + w, cubeY, ' ');
						}
					}
					if (i > 0 && piece.cubes[j][i - 1] != null) {
						for (int h = 1; h < 4; h++) {
							console.print(cubeX, cubeY + h, ' ');
						}
					}
					
					for (int w = -1; w < 2; w++) {
						for (int h = -1; h < 2; h++) {
							console.print(cubeX + w + 2, cubeY + h + 2, ' ');							
						}
					}
					console.print(cubeX + 2, cubeY + 2, canPlace ? '✔' : '✖');
				}
			}
		} else if (selectedPiece != null) {
			// Full, render the selected piece.
			int pieceX = selectedPiece.x * 4 + 4;
			int pieceY = selectedPiece.y * 4 + 4;
			Piece piece = selectedPiece.piece;
			int size = piece.getSize();
			
			for(int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (piece.cubes[j][i] == null) continue;
					
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
				}
			}
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
	
	private PieceByPosition findSelectedPiece() {
		int x = selectedGrid[0];
		int y = selectedGrid[1];
		if (possiblePlaces[y][x]) return null;
		
		PieceByPosition found = null;
		int size = placedPieces.size();
		Stack newStack = new Stack(size);
		for (int i = 0; i < size; i++) {
			PieceByPosition data = (PieceByPosition) placedPieces.pop();
			newStack.push(data);
			if (data.contains(x, y)) found = data;
		}
		for (int i = 0; i < size; i++) {
			placedPieces.push(newStack.pop());
		}
		
		return found;
	}
	
	public void render(GameConsole console) {
		depot.render(console, 50, 1);
		
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
			PieceByPosition data = (PieceByPosition) placedPieces.pop();
			newStack.push(data);
			data.piece.renderRaw(console, data.x * 4 + 4, data.y * 4 + 4);
		}
		for (int i = 0; i < size; i++) {
			placedPieces.push(newStack.pop());
		}
	}
	
	private void clean(GameConsole console, int x, int y) {
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 30; j++) {
				console.print(x + i - 1, y + j - 1, ' ');
			}
		}
	}
}
