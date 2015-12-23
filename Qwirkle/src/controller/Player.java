package controller;

import java.util.ArrayList;

import model.Bag;
import model.Hand;
import model.Tile;

public abstract class Player {


	private String name;
	private Hand hand;
	
	public Player(String theName) {
		this.name = theName;
	}

	public void setHand(Hand h) {
		this.hand = h;
	}

	public String getName() {
		return name;
	}
	
	public String getHand() {
		return hand.toString();
	}

	public abstract int determineMove(Board board);

	// geef een steen + locatie
	public void makeMove(Board board) {
		int keuze = determineMove(board);
	}

	
	// Stenen ruilen

}