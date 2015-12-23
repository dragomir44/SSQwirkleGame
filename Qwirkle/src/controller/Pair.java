package controller;

public class Pair<L, R> {

	private final int row;
	private final int col;

	public Pair(int row, int col) {
	    this.row = row;
	    this.col = col;
	}

	public int getLeft() { 
		return row; 
	}
	
	public int getRight() { 
		return col; 
	}
}
