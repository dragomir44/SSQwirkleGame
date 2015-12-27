package controller;

import java.util.ArrayList;

import model.Hand;
import model.Move;

public abstract class Player {


	private String name;
	public Hand hand;
	private int score;
	
	public Player(String name) {
		this.name = name;
	}

	public void setHand(Hand h) {
		this.hand = h;
	}

	public String getName() {
		return name;
	}
	
<<<<<<< HEAD
	public String handToString() {
		return hand.toString();
	}
	
	public abstract ArrayList<Move> determineMove(Board board);
=======
	public String printHand() {
		return hand.toString();
	}
	
	public Hand getHand() {
		return hand;
	}

>>>>>>> bfe2bf89a4d10ac4431e5bc35fb37d5ab81b1bdb

	public void makeMove(Board board) {
		ArrayList<Move> keuze = determineMove(board);
		if (keuze.isEmpty()) {
			System.out.println(name + " traded tiles.");
		} else {
			for (Move move : keuze) {
				System.out.println("Placing " + move.tile + " on "
							 + move.row + " " + move.col + ".");
				hand.removeTile(move.tile);
			}
			board.setField(keuze);

		}
		int points = board.getPoints(keuze);
		incrementScore(points);
		System.out.println(name + " scored " + points + " points.");
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