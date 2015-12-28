package controller;

import java.util.ArrayList;

import model.Move;
import model.Tile;

public interface Strategy {
	
	public String getName();
	public ArrayList<Move> determineMove(Board b);

}
