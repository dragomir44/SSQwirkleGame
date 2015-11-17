package pp.controller;

import pp.model.Mark;

/**
 * Abstract class for keeping a player in the Connect Four game.
 *
 *
 * @author Sergey Dragomiretskiy and Amber Altenburg
 * @version $Date: 22-1-2015 $
 */
public abstract class Player {

	// -- Instance variables -----------------------------------------

	private String name;
	private Mark mark;

	// -- Constructors -----------------------------------------------

	/*
	 * @ requires theName != null; requires theMark == theMark.XX || theMark ==
	 * theMark.OO; ensures this.getName() == theName; ensures this.getMark() ==
	 * theMark;
	 */
	/**
	 * Creates a new Player object.
	 * 
	 * @param theName
	 * @param theMark
	 */
	public Player(String theName, Mark theMark) {
		this.name = theName;
		this.mark = theMark;
	}

	// -- Queries ----------------------------------------------------

	/**
	 * Returns the name of the player.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the mark of the player.
	 */
	public Mark getMark() {
		return mark;
	}

	/*
	 * @ requires board != null & !board.isFull(); ensures
	 * board.isField(\result) & board.isEmptyField(\result);
	 */
	/**
	 * Determines the field for the next move.
	 *
	 * @param board
	 *            the current game board
	 */
	public abstract int determineMove(Board board);

	// -- Commands ---------------------------------------------------

	/*
	 * @ requires board != null & !board.isFull();
	 */
	/**
	 * Makes a move on the board.
	 *
	 * @param board
	 *            the current board
	 */
	public void makeMove(Board board) {
		int keuze = determineMove(board);
		board.setField(keuze, getMark());
	}

}