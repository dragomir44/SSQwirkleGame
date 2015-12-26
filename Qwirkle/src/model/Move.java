package model;

public class Move {
	public final int row;
	public final int col;
	public final Tile tile;
	
	public Move(int r, int c, Tile t) {
		this.row = r;
		this.col = c;
		this.tile = t;
	}
}
