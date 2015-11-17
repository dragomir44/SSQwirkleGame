package controller;

import model.Mark;


public class Board {

	// -- Constants --------------------------------------------------

	public static final int ROW = 6;
	public static final int COL = 7;
	public static final int COUNT = 4;
	public static final int NO_OF_DIAGONALS = 12;
	public boolean debug = true;

	// -- Instance variables -----------------------------------------


	private Mark[][] fields;
	private Mark[][] fieldsCopy;

	// -- Constructors -----------------------------------------------

	public Board() {
		fields = new Mark[ROW][COL];

	}

	// -- Queries ----------------------------------------------------


	public Board deepCopy() {
		Board copy = new Board();
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields.length; j++) {
				fieldsCopy = new Mark[ROW][COL];
			}
		}
		return copy;
	}


	public int index(int row, int col) {
		return COL * row + col;
	}


	public boolean isField(int ix) {
		return 0 <= ix && ix < ROW * COL;
	}

	public boolean isField(int row, int col) {
		int i = index(row, col);
		return 0 <= i && i < ROW * COL;
	}

	public boolean isColumn(int ix) {
		return 0 <= ix && ix < COL;
	}

	public Mark getField(int row, int col) {

		return fields[row][col];
	}

	public boolean isEmptyField(int row, int col) {
		return this.getField(row, col) == Mark.EE;
	}

	public boolean columnHasEmptyField(int col) {
		for (int i = ROW - 1; i >= 0; i--) {
			if (fields[i][col] == Mark.EE) {
				return true;
			}
		}
		return false;
	}


	public boolean isFull() {
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				if (fields[i][j] == Mark.EE) {
					return false;
				}
			}
		}
		return true;
	}


	public boolean gameOver() {
		return isFull() || hasWinner();
	}


	public boolean isWinner(Mark m) {
	//	return hasRow(m) || hasColumn(m) || hasUpperRightDiagonal(m)
	//			|| hasLowerRightDiagonal(m);
		return true;
	}


	public boolean hasWinner() {
		return isWinner(Mark.YY) || isWinner(Mark.RR);
	}

	public String toString() {
		String s = "";
		String g = " 0    1    2    3    4    5    6 ";
		for (int i = 0; i < ROW; i++) {
			String row = "";
			for (int j = 0; j < COL; j++) {
				row = row + " " + getField(i, j).toString() + " ";
				if (j < COL - 1) {
					row = row + "|";
				}
			}
			s = s + row;
			if (i < ROW - 1) {
				s = s + "\n";
			}
		}
		return g + "\n" + s;
	}

	public void reset() {
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				fields[i][j] = Mark.EE;
			}
		}
	}

	public void setField(int col, Mark m) {
		for (int i = ROW - 1; i >= 0; i--) {
			if (isEmptyField(i, col)) {
				fields[i][col] = m;
				break;
			}
		}
	}

}