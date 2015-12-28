package controller;

import java.util.ArrayList;

import model.Move;
import model.Tile;

public class ComputerPlayer extends Player {

	private String name;
	private Strategy strategy;

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

	@Override
	public ArrayList<Move> determineMove(Board board) {
		return strategy.determineMove(board);
	}
}
