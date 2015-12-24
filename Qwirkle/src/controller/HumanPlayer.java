package controller;

import java.util.Scanner;

import model.Tile;
import view.Game;

public class HumanPlayer extends Player {

	// -- Constructors -----------------------------------------------

	public HumanPlayer(String name) {
		super(name);
	}

	// -- Commands ---------------------------------------------------

	public int determineMove(Board board) {
		System.out.println("Your hand: " + this.getHand().toString());
		String prompt = "> " + getName() + ", which tile you wanna place bruh? ";

		boolean valid = false;
		while (!valid) {
			int pieceChoice = readInt(prompt);
			boolean validPiece = pieceChoice <= Game.TILES_PER_HAND && pieceChoice > 0;
			while (!validPiece) {
				System.out.println(" *ERROR* Your pieces only go from 1 to 6");
				pieceChoice = readInt(prompt);
				validPiece = pieceChoice <= Game.TILES_PER_HAND && pieceChoice > 0;
			}
			
			String prompt2 = "> In which row you wanna place it bruh? ";
			int rowChoice  = readInt(prompt2);
			boolean validRow = rowChoice <= board.rows && rowChoice >= 0;
			while (!validRow) {
				System.out.println(" *ERROR* The rows only go from 0 to " +
						  (board.rows - 1) + " right now");

				rowChoice = readInt(prompt2);
				validRow = rowChoice <= board.rows && rowChoice >= 0;
			}
			
			String prompt3 = "> In which col you wanna place it bruh? ";
			int colChoice  = readInt(prompt3);
			boolean validCol = colChoice <= board.cols && colChoice >= 0;
			while (!validCol) {
				System.out.println(" *ERROR* The rows only go from 0 to " + 
							  (board.rows - 1) + " right now");
				colChoice = readInt(prompt3);
				validCol = colChoice <= board.cols && colChoice >= 0;
			}
			if (validPiece & validRow & validCol) {
				valid = true;
			}
		}
		//return piece
		return 2;
		
	}

	private int readInt(String prompt) {
		int value = 0;
		boolean intRead = false;
		do {
			System.out.print(prompt);
			String line = (new Scanner(System.in)).nextLine();
			Scanner scannerLine = new Scanner(line);
			if (scannerLine.hasNextInt()) {
				intRead = true;
				value = scannerLine.nextInt();
			}
		} while (!intRead);

		return value;
	}

}