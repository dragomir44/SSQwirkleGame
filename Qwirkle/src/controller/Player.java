package controller;

import java.util.ArrayList;
import java.util.Set;

import model.Hand;
import model.Move;
import model.Tile;
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