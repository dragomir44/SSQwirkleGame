package controller;

import model.Tile;

public class ComputerPlayer extends Player {

	// -- Constants --------------------------------------------------

	private String name;
	private Tile mark;
	private Strategy strategy;

	// -- Constructors -----------------------------------------------

	public ComputerPlayer(Tile mark, Strategy strategy) {
		super(strategy.getName());
		this.name = strategy + "-" + mark;
		this.mark = mark;
		this.strategy = strategy;
	}

	public ComputerPlayer(Tile mark) {
		super("Naive");
		this.name = "naive-computer";
		this.mark = mark;
		strategy = new NaiveStrategy();
	}

	// -- Queries ----------------------------------------------------

	@Override
	public int determineMove(Board board) {
		return strategy.determineMove(board, this.mark);
	}
}
