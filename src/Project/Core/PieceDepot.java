package Project.Core;

import java.util.Random;

public class PieceDepot {
	public Piece[] pieceDepot = new Piece[20];
	private final Random rng;

	public PieceDepot(Random rng) {
		this.rng = rng;

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

	public void render(GameConsole console, int x, int y) {
		String lines = "-".repeat(50);
		console.print(x * 2 + 4, y, lines + "   P I E C E S  " + lines);

		int a = 0;
		for (int i = 0; i < pieceDepot.length; i++) {
			Piece piece = pieceDepot[i];
			if (i < 4) {
				piece.setPosition(x + 2 + (a * 14), y + 2);
				piece.render(console, x + 2 + (a * 14), y + 2);
				a = (a + 1) % 4;
			} else if (i < 8) {
				piece.setPosition(x + 2 + (a * 14), y + 2 + 15);
				piece.render(console, x + 2 + (a * 14), y + 2 + 15);
				a = (a + 1) % 4;
			} else if (i < 13) {
				piece.setPosition(x + 2 + (a * 11), y + 2 + 30);
				piece.render(console, x + 2 + (a * 11), y + 2 + 30);
				a = (a + 1) % 5;
			} else {
				piece.setPosition(x + 2 + (a * 7), y + 2 + 40);
				piece.render(console, x + 2 + (a * 7), y + 2 + 40);
				a = a + 1;
			}
		}
	}
}
