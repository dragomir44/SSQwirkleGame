package controller;

import java.util.ArrayList;

import model.Hand;
import model.Move;

public interface Strategy {
	
	public String getName();
	public void setHand(Hand hand);
	public ArrayList<Move> determineMove(Board b);

}
