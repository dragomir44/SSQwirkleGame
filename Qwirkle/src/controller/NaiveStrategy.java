package controller;

import model.Tile;

public class NaiveStrategy implements Strategy {

	// -- Constants --------------------------------------------------

	String name = "Naive";
	public static final int ROW = 6;
	public static final int COL = 7;

	// -- Queries ----------------------------------------------------

	/**
     * Returns the name of the naive computer player.
     * 
     * @return the name of the naive computer player
     */
	@Override
	public String getName() {
		return name;
	}
	
	// -- Commands ---------------------------------------------------

	/**
	 * Asks the computer where to place the next mark.
	 * 
	 * @param board
	 *            the game board
	 * @param mark
	 *            the mark of interest
	 * @return the computer player's choice
	 */
	@Override
	public int determineMove(Board board, Tile mark) {
		int choice = (int) (Math.random() * 100) % COL;
		boolean valid = board.isColumn(choice) && board.isField(choice);
		while (!valid) {
			choice = (int) (Math.random() * 100) % COL;
			valid = board.isColumn(choice) && board.isField(choice);
		}
		return choice;
	}
}
