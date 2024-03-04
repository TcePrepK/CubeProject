package Project.Core;

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
			this.piece = piece;
		}
	}
	
	private Stack placedPieces = new Stack(14);
	
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
	
	public void render(GameConsole console) {
		int paddingX = 1;
		int paddingY = 1;
		
		console.print(paddingX * 2, paddingY, "+ " + "-".repeat(46) + ">  X");
		for (int i = 1; i < 25; i++) {
			console.print(paddingX, paddingY + i, '║');
		}
		console.print(paddingX, paddingY + 25, 'V');
		console.print(paddingX, paddingY + 27, 'Y');
		
		for (int i = 0; i < 5; i++) {
			int nX = paddingX + i * 4 + 4;
			int nY = paddingY + i * 4 + 4;
			
			console.print(nX, paddingY, (char) ('0' + i + 1));
			console.print(nX * 2 - 1, paddingY, " ");
			console.print(nX * 2 + 1, paddingY, " ");
			
			console.print(paddingX, nY, (char) ('0' + i + 1));
			console.print(paddingX, nY - 1, " ");
			console.print(paddingX, nY + 1, " ");
		}
		
		paddingX += 2;
		paddingY += 2;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (!possiblePlaces[j][i]) {
					continue;
				}
				
				for (int w = 0; w < 5; w++) {
					for (int h = 0; h < 5; h++) {
						console.print(paddingX + i * 4 + w, paddingY + j * 4 + h, Piece.emptyCube[w][h]);
					}
				}
			}
		}
		
		System.out.println(placedPieces.size());
		for (int d = 0; d < placedPieces.size(); d++) {
			PieceByPosition data = (PieceByPosition) placedPieces.get(d);
			data.piece.renderRaw(console, paddingX + data.x * 4, paddingY + data.y * 4);
		}
	}
}
