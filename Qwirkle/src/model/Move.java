package model;

public class Move {
	public int row;
	public int col;
	public Tile tile;
	
	public Move(int r, int c, Tile t) {
		row = r;
		col = c;
		tile = t;
	}
}
