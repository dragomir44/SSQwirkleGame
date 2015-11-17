package controller;

import java.util.Scanner;

import model.Mark;

public class HumanPlayer extends Player {

	// -- Constructors -----------------------------------------------

	public HumanPlayer(String name) {
		super(name);
	}

	// -- Commands ---------------------------------------------------

	public int determineMove(Board board) {
		String prompt = "> " + getName() + " (what is your choice? ";
		int choice = readInt(prompt);
		boolean valid = board.isColumn(choice) && board.isField(choice);
		while (!valid) {
			System.out.println("ERROR: field " + choice
					  + " is no valid choice. Please choose a colum(0-6).");
			choice = readInt(prompt);
			valid = board.isColumn(choice) && board.isField(choice);
		}
		return choice;
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