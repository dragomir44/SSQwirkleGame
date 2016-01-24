package controller;

import java.util.ArrayList;

import model.*;

public class ComputerPlayer extends Player {
	private Strategy strategy;
	public ComputerPlayer(Strategy strategy, String name) {
		super(name + " (" + strategy.getName() + ")");
		this.strategy = strategy;		
	}

	@Override
	public ArrayList<Move>  determineMove(Board board) {
		return strategy.determineMove(board, hand);
    }

	@Override
	public void writeString(String msg) {
		System.out.println("AI: " + msg);
	}
}
 