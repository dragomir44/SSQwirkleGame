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
			Set<Integer> tileNrs = new HashSet<Integer>();
			tileNrs.add(1);
			moves.add(new TradeMove(tileNrs));
		}
		return moves;
	}
}
