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

	public void makeMove(Board board) {
		//TODO koopel bag los van player
		//TODO verplaats plaatsen van move naar play (return moves ipv void)
		//TODO score verplaatsen naar play
		//TODO gebruik minimum bag size ipv echte bag
		String retry = " please try again, ";
		String resultString = "";
		boolean validMove = false;
		boolean validFirstMove = true;

		writeString(getName() + "'s turn:");
		do {
			ArrayList<Move> moves = determineMove(board);
			if (!moves.isEmpty()) {
				// trade tiles
				if (moves.get(0) instanceof TradeMove) {
					Set<Integer> tileNrs = ((TradeMove) moves.get(0)).tileNrs;
					if (board.isEmpty()) {
						resultString = "You can't trade tiles in the first move," + retry;
					} else if (tileNrs.size() <= getHand().getBag().getSize()) {
						ArrayList<Tile> replacements = hand.replaceTiles(tileNrs);
						if (replacements != null) {
							resultString = "Drew: " + replacements;
							validMove = true;
						} else {
							resultString = "Invalid trade," + retry;
						}
					} else {
						resultString = "Not enough tiles in bag to trade," + retry;
					}
				} else { // place tiles
					if (board.isEmpty()) { // make sure longest move is played in first turn
						int firstMoveSize = board.getPossibleMoves(hand.getTiles()).firstKey().size();
						if (firstMoveSize != moves.size()) {
							validFirstMove = false;
						}
					}
					if (validFirstMove) {
						if (board.isValidMove(moves)) {
							resultString = getName() + " scored "
									+ board.getPoints(moves) + " points.";
							resultString += "\n End of turn.";
							for (Move move : moves) {
								hand.removeTile(move.tile);
							}
							board.setField(moves);
							int points = board.getPoints(moves);
							incrementScore(points);
							validMove = true;
						} else {
							resultString = "Invalid move " + board.getErrors();
						}
					} else {
						resultString = "In the first turn the longest row possible has to be placed,";
						resultString += retry;
					}

				}
			} else {
				resultString = "No moves were given," + retry;
			}
			this.writeString(resultString);
		} while (!validMove);
	}

	protected void writeString(String msg) {
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