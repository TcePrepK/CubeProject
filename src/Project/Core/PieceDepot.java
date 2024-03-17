package Project.Core;

import java.util.Random;

public class PieceDepot {
	public Piece[] pieceDepot = new Piece[20];
	private int selectedPiece = 0;

	public PieceDepot(Random rng) {
		for (int i = 0; i < pieceDepot.length; i++) {
			if (i > 12) {
				pieceDepot[i] = new Piece(rng, 1);
			} else if (i > 7) {
				pieceDepot[i] = new Piece(rng, 2);
			} else if (i > 3) {
				pieceDepot[i] = new Piece(rng, 3);
			} else {
				pieceDepot[i] = new Piece(rng, 4);
			}
		}
	}
	
	public boolean mouseCheck(Mouse mouse, GameConsole console) {
		boolean updated = false;
		for(int i = 0; i < pieceDepot.length; i++) {
			if(!pieceDepot[i].isSelected(mouse, console)) continue;
			updateSelected(console, selectedPiece, i);
			updated = true;
			break;
		}
		
		return updated;
	}
	
	public void updateSelected(GameConsole console, int prevSelected, int newSelected) {
		pieceDepot[prevSelected].clean(console);
		pieceDepot[newSelected].showSelect(console);
		selectedPiece = newSelected;
	}
	
	public boolean keyboardCheck(String key, GameConsole console) {
		boolean pieceUpdate = pieceDepot[selectedPiece].MovesCheck(key, console);
		
		int dx = key.equals("LEFT") ? -1 : key.equals("RIGHT") ? 1 : 0;
		int dy = key.equals("UP") ? -1 : key.equals("DOWN") ? 1 : 0;
		if (dx == 0 && dy == 0) return pieceUpdate;
		pieceUpdate = true;
		
		int row = 0;
		int col = selectedPiece;
		int prevColLength = 4;
		if (selectedPiece > 3) { 
			row++;
			col -= 4;
			prevColLength = 4;
		}
		if (selectedPiece > 7) {
			row++;
			col -= 4;
			prevColLength = 5;
		}
		if (selectedPiece > 12) {
			row++;
			col -= 5;
			prevColLength = 7;
		}
		
		int newRow = (row + dy + 4) % 4;
		int newColLength;
		switch (newRow) {
			case 0:
			case 1:
				newColLength = 4;
				break;
			case 2:
				newColLength = 5;
				break;
			case 3:
				newColLength = 7;
				break;
			default:
				newColLength = 0;
				break;
		}
		
		int newCol;
		if (dy == 0) newCol = (col + dx + newColLength) % newColLength;
		else newCol = (int) (col * (double) (newColLength - 1) / (prevColLength - 1));
		int prevLength = newRow == 0 ? 0 : newRow == 1 ? 4 : newRow == 2 ? 8 : 13;		
		int newSelected = newCol + prevLength;
		updateSelected(console, selectedPiece, newSelected);
		
		return pieceUpdate;
	}

	public void render(GameConsole console, int x, int y) {
		String lines = "-".repeat(56);
		console.print(x * 2, y, lines + "   P I E C E S  " + lines);

		int a = 0;
		for (int i = 0; i < pieceDepot.length; i++) {
			Piece piece = pieceDepot[i];
			
			int px, py;
			if (i < 4) {
				px = x + 2 + (a * 16);
				py = y + 2;
				a = (a + 1) % 4;
			} else if (i < 8) {
				px = x + 2 + (a * 16); 
				py = y + 2 + 15;
				a = (a + 1) % 4;
			} else if (i < 13) {
				px = x + 2 + (a * 12); 
				py = y + 2 + 30;
				a = (a + 1) % 5;
			} else {
				px = x + 2 + (a * 8); 
				py = y + 2 + 41;
				a = a + 1;
			}
			
			piece.setPosition(px, py);
			piece.render(console, px, py);
			console.print((px * 2 - 3), py, (i < 9 ? "0" : "") + (i + 1));
		}
	}
	
	public Piece getSelectedPiece() {
		return pieceDepot[selectedPiece];
	}
	
	public int getSelectedNumber() {
		return selectedPiece + 1;
	}
}
