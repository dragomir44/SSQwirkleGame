package controller;

import java.util.ArrayList;

import model.Hand;
import model.Move;

public interface Strategy {
	public String getName();
	public ArrayList<Move> determineMove(Board b, Hand h);
}
