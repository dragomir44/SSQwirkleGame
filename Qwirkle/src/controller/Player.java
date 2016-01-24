package controller;

import java.util.ArrayList;

import model.Hand;
import model.Move;
import model.TradeMove;

public abstract class Player {


	private String name;
	protected Hand hand;
	private int score;
	
	public Player(String name) {
		this.name = name;
	}

	public void setHand(Hand h) {
		this.hand = h;
	}
	
	public Hand getHand() {
		return this.hand;
	}

	public String getName() {
		return name;
	}
	
	public String handToString() {
		return hand.toString();
	}
	
	public abstract ArrayList<Move> determineMove(Board board);

	public void makeMove(Board board) {
		ArrayList<Move> keuze = determineMove(board);
		if (!keuze.isEmpty()) {
			if (keuze.get(0) instanceof TradeMove) {

			} else {
				for (Move move : keuze) {
					hand.removeTile(move.tile);
				}
				board.setField(keuze);
				int points = board.getPoints(keuze);
				incrementScore(points);
			}
		} else {
			System.err.println("*ERROR* Empty move was made!");
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void incrementScore(int points) {
		score += points;
	}
}