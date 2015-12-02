package controller;

import model.Bag;
import model.Tile;

public class Board {

	// -- Constants --------------------------------------------------

	public static final int ROW = 16;
	public static final int COL = 16;

	// -- Instance variables -----------------------------------------


	private Tile[][] fields;
	private Tile[][] fieldsCopy;
	private Bag bag;

	// -- Constructors -----------------------------------------------

	public Board() {
		fields = new Tile[ROW][COL];
		bag = new Bag();

	}

	// -- Queries ----------------------------------------------------

/*  SMART COMPUTER PLAYER
	public Board deepCopy() {
		Board copy = new Board();
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields.length; j++) {
				fieldsCopy = new Mark[ROW][COL];
			}
		}
		return copy;
	}
*/
	
	//werkt
	public int index(int row, int col) {
		return COL * row + col;
	}

	//werkt
	public boolean isField(int ix) {
		return 0 <= ix && ix < ROW * COL;
	}

	//werkt
	public boolean isField(int row, int col) {
		int i = index(row, col);
		return 0 <= i && i < ROW * COL;
	}

	//werkt
	public boolean isColumn(int ix) {
		return 0 <= ix && ix < COL;
	}

	public Tile getField(int row, int col) {
		return fields[row][col];
	}

	//TODO
	public boolean isEmptyField(int row, int col) {
		return this.getField(row, col) == Tile.EE;
	}

	public boolean gameOver() {
		//All tiles gone
		return false;
	}

	public boolean isWinner(Tile m) {
	//	return hasRow(m) || hasColumn(m) || hasUpperRightDiagonal(m)
	//			|| hasLowerRightDiagonal(m);
		return true;
	}

	public boolean hasWinner() {
		return isWinner(Tile.YY) || isWinner(Tile.RR);
	}

	public String toString() {
		String res = "    ";
		for (int k = 0; k < COL; k++) {
			if (k < 10) {
				res = res + "   " + k;
			} else {
				res = res + "  " + k;
			}
		}
		res = res + "\n";
		for (int i = 0; i < ROW; i++) {
			if (i < 10) {
				//add \n for blocks
				res = res + i + " ";
			} else {
				//add \n for blocks
				res = res + i + "";
			}
			for (int j = 0; j < COL; j++) {
				//actually empty tile
				res = res + "   ";
				if (j < COL) {
					res = res + "|";
				}
			}
			res = res + "\n";
		}
		return res;
	}

	public void reset() {
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				fields[i][j] = Tile.EE;
			}
		}
	}

	public void setField(int col, Tile m) {
		for (int i = ROW - 1; i >= 0; i--) {
			if (isEmptyField(i, col)) {
				fields[i][col] = m;
				break;
			}
		}
	}
	
	public static void main(String [] args) {
		Board b = new Board();
		System.out.println(b.toString());
		System.out.println(b.getField(1, 1));
		System.out.println(b.bag.toString());
		System.out.println(b.bag.getBag().get(1));
		b.setField(5, b.bag.getBag().get(1));
	}

}