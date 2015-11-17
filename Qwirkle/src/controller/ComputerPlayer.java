package pp.controller;

import pp.model.Mark;

/**
 * Class for maintaining a computer player in the Connect Four game.
 * 
 * @author Sergey Dragomiretskiy and Amber Altenburg
 * @version $Date: 21-1-2015 $
 *
 */

public class ComputerPlayer extends Player {

	// -- Constants --------------------------------------------------

	private String name;
	private Mark mark;
	private Strategy strategy;

	// -- Constructors -----------------------------------------------

	/**
	 * Creates a new computer player object.
	 * 
	 * @param mark
	 *            the mark of interest
	 * @param strategy
	 *            the strategy of interest
	 */

	public ComputerPlayer(Mark mark, Strategy strategy) {
		super(strategy.getName(), mark);
		this.name = strategy + "-" + mark;
		this.mark = mark;
		this.strategy = strategy;
	}

	/**
	 * Creates a new naive computer player object.
	 * 
	 * @param mark
	 *            the mark of interest
	 */
	public ComputerPlayer(Mark mark) {
		super("Naive", mark);
		this.name = "naive-computer";
		this.mark = mark;
		strategy = new NaiveStrategy();

	}

	// -- Queries ----------------------------------------------------

	@Override
	/**
	 * Asks the computer to input the field where to place the next mark.
	 * 
	 * @param board
	 * 		the game board
	 * 		
	 */
	public int determineMove(Board board) {
		return strategy.determineMove(board, this.mark);
	}
}
