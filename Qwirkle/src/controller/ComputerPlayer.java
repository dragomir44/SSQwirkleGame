package controller;

import java.util.ArrayList;

import model.Move;

public class ComputerPlayer extends Player {

	private Strategy strategy;
	boolean firstMove = true;
	

	public ComputerPlayer(Strategy strategy) {
		super(strategy.getName());
		this.strategy = strategy;
	}

	public ComputerPlayer() {
		super("Naive");
		strategy = new NaiveStrategy();
	}

	public ArrayList<Move> determineMove(Board board) {
		strategy.setHand(hand);
		return strategy.determineMove(board);
	}
}
