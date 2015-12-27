package controller;

import model.Tile;

public class ComputerPlayer extends Player {

	// -- Constants --------------------------------------------------

	private String name;
	private Strategy strategy;

	// -- Constructors -----------------------------------------------

	public ComputerPlayer(Strategy strategy) {
		super(strategy.getName());
		this.name = "" + strategy;
		this.strategy = strategy;
	}

	public ComputerPlayer() {
		super("Naive");
		this.name = "naive-computer";
		strategy = new NaiveStrategy();
	}

	// -- Queries ----------------------------------------------------

	@Override
	public int determineMove(Board board) {
		return strategy.determineMove(board);
	}
}
