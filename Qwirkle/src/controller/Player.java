package controller;

import java.util.ArrayList;

import model.Hand;
import model.Move;

public abstract class Player {

	private String name;
	protected Hand hand = new Hand();
	private int score;
	
	public Player(String name) {
		this.name = name;
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

	public void writeString(String msg) {
		System.out.println(msg);
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