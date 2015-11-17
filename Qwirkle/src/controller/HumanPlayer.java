package pp.controller;

import java.util.Scanner;

import pp.model.Mark;

/**
 * Class for maintaining a human player in the Connect Four game.
 *
 * @author Sergey Dragomiretskiy and Amber Altenburg
 * @version $Date: 21-1-2015 $
 */
public class HumanPlayer extends Player {

	// -- Constructors -----------------------------------------------

	/*
	 * @ requires name != null; requires mark == Mark.RR || mark == Mark.YY;
	 * ensures this.getName() == name; ensures this.getMark() == mark;
	 */
	/**
	 * Creates a new human player object.
	 * 
	 * @param name
	 *            name of the player
	 * @param mark
	 *            mark of interest
	 */
	public HumanPlayer(String name, Mark mark) {
		super(name, mark);
	}

	// -- Commands ---------------------------------------------------

	/*
	 * @ requires board != null; ensures board.isField(\result) &&
	 * board.isEmptyField(\result);
	 */
	/**
	 * Asks the user to input the field where to place the next mark. This is
	 * done using the standard input/output.
	 *
	 * @param board
	 *            the game board
	 * @return the player's chosen field
	 */
	public int determineMove(Board board) {
		String prompt = "> " + getName() + " (" + getMark().toString() + ")"
				  + ", what is your choice? ";
		int choice = readInt(prompt);
		boolean valid = board.isColumn(choice) && board.isField(choice)
				  && board.columnHasEmptyField(choice);
		while (!valid) {
			System.out.println("ERROR: field " + choice
					  + " is no valid choice. Please choose a colum(0-6).");
			choice = readInt(prompt);
			valid = board.isColumn(choice) && board.isField(choice)
					&& board.columnHasEmptyField(choice);
		}
		return choice;
	}

	/**
	 * Writes a prompt to standard out and tries to read an int value from
	 * standard in. This is repeated until an int value is entered.
	 *
	 * @param prompt
	 *            the question to prompt the user
	 * @return the first int value which is entered by the user
	 */
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