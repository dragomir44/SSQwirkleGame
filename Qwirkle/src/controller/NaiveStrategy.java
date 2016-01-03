package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

import model.Hand;
import model.Move;
import model.Tile;

public class NaiveStrategy implements Strategy {

	String name = "Naive";
	Hand hand;
	Random randomGenerator = new Random();
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public ArrayList<Move> determineMove(Board board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		boolean correct = false;
		//get random stones
		//place random stones in found spots
		//if none of the stones fit 
		//switch random tiles
		if (board.firstMove) {
			Move fMove = new Move(board.middleOfBoard, board.middleOfBoard, hand.getHand().get(randomTile()));
			moves.add(fMove);
			board.firstMove = false;
			return moves;
		} else {
			//get open spots
		}
		return moves;
	}
	
	
	public int randomTile(){
		int randomInt = randomGenerator.nextInt(6);
		return randomInt;
	}

	@Override
	public void setHand(Hand hand) {
		this.hand = hand;
	}
	
	
}
