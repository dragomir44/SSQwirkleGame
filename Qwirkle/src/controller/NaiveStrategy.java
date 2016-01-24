package controller;

import java.util.*;

import model.*;

public class NaiveStrategy implements Strategy {
	private String name = "Naive";

	public String getName() {
		return name;
	}

	public ArrayList<Move> determineMove(Board board, Hand hand) {
		ArrayList<Move> moves = new ArrayList<Move>();
		TreeMap<ArrayList<Move>, Integer> possibleMoves = 
				  new TreeMap<ArrayList<Move>, Integer>();
		System.out.println(hand);
		possibleMoves = board.getPossibleMoves(hand.getTiles());
		if (!possibleMoves.isEmpty()) {
			moves = possibleMoves.firstKey();
		} else {
			ArrayList<Integer> tilenrs = new ArrayList<Integer>();
			tilenrs.add(1);
			ArrayList<Tile> replacements = hand.replaceTiles(tilenrs);
			if (replacements != null) {
				System.out.println("Drew: " + replacements);
			}
		}
		if (board.isValidMove(moves)) {
			return moves;
		} else {
			System.err.println("AI created invalid move");
			System.err.println(moves.toString());
			moves.clear();
		}
		return moves;
	}
}
