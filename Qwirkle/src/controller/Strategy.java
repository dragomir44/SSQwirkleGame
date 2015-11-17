package controller;

import model.Tile;

public interface Strategy {
	
	public String getName();
	public int determineMove(Board b, Tile m);

}
