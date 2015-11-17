package controller;

import model.Mark;

public class ComputerPlayer extends Player {

	// -- Constants --------------------------------------------------

	private String name;
	private Mark mark;
	private Strategy strategy;

	// -- Constructors -----------------------------------------------

	public ComputerPlayer(Mark mark, Strategy strategy) {
		super(strategy.getName(), mark);
		this.name = strategy + "-" + mark;
		this.mark = mark;
		this.strategy = strategy;
	}

	public ComputerPlayer(Mark mark) {
		super("Naive", mark);
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
