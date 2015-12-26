package controller;

import java.util.Scanner;

import view.Game;

public class HumanPlayer extends Player {

	public HumanPlayer(String name) {
		super(name);
	}

	public int determineMove(Board board) {
		System.out.println("Your hand: " + this.getHand().toString());		
		String prompt = "> " + getName() 
						+ ", which tile do you want to use?  [Type -1 if you want to switch tiles]";

		boolean valid = false;
		while (!valid) {
			int pieceChoice = readInt(prompt) - 1;
			boolean validPiece = pieceChoice < Game.TILES_PER_HAND && pieceChoice >= 0;
			if (validPiece) {
				System.out.println("> You chose " + this.getHand().getTile(pieceChoice).toString());
			}
			while (!validPiece) {
				if (pieceChoice == -2) {
					String Switch = "> Which tile do you want to switch?";
					pieceChoice = readInt(Switch) - 1;
					validPiece = pieceChoice < Game.TILES_PER_HAND && pieceChoice >= 0;
					if (validPiece) {
						System.out.println("> You chose " 
										+ this.getHand().getTile(pieceChoice).toString());
					}
				} else {
					System.out.println("> *ERROR* Your pieces only go from 1 to 6");
					pieceChoice = readInt(prompt);
					validPiece = pieceChoice <= Game.TILES_PER_HAND && pieceChoice > 0;
					if (validPiece) {
						System.out.println(">You chose " 
										+ this.getHand().getTile(pieceChoice - 1).toString());
					}
				}
			}
			
			String prompt2 = "> In which row you wanna place it? ";
			int rowChoice  = readInt(prompt2);
			boolean validRow = rowChoice <= board.rows && rowChoice >= 0;
			while (!validRow) {
				System.out.println("> *ERROR* The rows only go from 0 to " 
						  + (board.rows - 1) + " right now");
				System.out.println(" *ERROR* The rows only go from 0 to " +
						  (board.rows - 1) + " right now");

				rowChoice = readInt(prompt2);
				validRow = rowChoice <= board.rows && rowChoice >= 0;
			}
			
			String prompt3 = "> In which col you wanna place it? ";
			int colChoice  = readInt(prompt3);
			boolean validCol = colChoice <= board.cols && colChoice >= 0;
			while (!validCol) {
				System.out.println("> *ERROR* The rows only go from 0 to " 
							 + (board.rows - 1) + " right now");
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