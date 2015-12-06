package controller;

import model.Tile;
import model.BoardTiles;

public class Board {

	// -- Constants --------------------------------------------------

	public int rows = 20;
	public int cols = 20;

	// -- Instance variables -----------------------------------------
	
	// instance with a map of all the tiles placed on the board
	BoardTiles tiles = new BoardTiles();
	// -- Constructors -----------------------------------------------
	public Board() {
	}

	// -- Queries ----------------------------------------------------

	/** Every tile needs to be placed next to adjecent tile (except first tile).
	 * This method checks if tile is adjecent to atleast 1 tile,
	 * n.b. (have to make sure that when placed next to end of board, board increses size)
	 * @param row
	 * @param col
	 * @return true if field has atleast 1 adjecent tile
	 */
	public boolean isField(int row, int col) {
		Tile[] adjecent =  tiles.getAdjecentTiles(row, col);
		boolean isAdjecent = false;
		for (Tile tile : adjecent) {
			if (tile != null) {
				isAdjecent = true;
				break;
			}
		}
		return isAdjecent;
	}

	public boolean gameOver() {
		//All tiles gone
		return false;
	}


//	public String toString() {
//		String res = "    ";
//		for (int k = 0; k < cols; k++) {
//			if (k < 10) {
//				res = res + "   " + k;
//			} else {
//				res = res + "  " + k;
//			}
//		}
//		res = res + "\n";
//		for (int i = 0; i < rows; i++) {
//			if (i < 10) {
//				//add \n for blocks
//				res = res + i + " ";
//			} else {
//				//add \n for blocks
//				res = res + i + "";
//			}
//			for (int j = 0; j < cols; j++) {
//				//actually empty tile
//				res = res + "   ";
//				if (j < cols) {
//					res = res + "|";
//				}
//			}
//			res = res + "\n";
//		}
//		return res;
//	}

	/** Grow the playing field in a certain direction
	 * @param direction 0(right), 1(top), 2(left), 3(bottom)
	 */
	public void growField(int direction){
		// recreate boardString
		// if top or left, increment BoardTiles Keys
	}
	
	public void reset() {
//		Board b = new Board();
	}

	public void setField(int col, int row, Tile m) {
		// place tile in BoardTiles map
		// put tile in BoardString
	}
	
	public String toString() {
		// use StringBuilder for better memory performance
		StringBuilder boardString = new StringBuilder();
		for (int i = 0; i < rows; i++) { // loop trough rows
		    for (int j = 0; j < cols; j++) { // loop trough cols
		    	boardString.append("|");
		    	if (tiles.tileMap.containsKeys(i, j)) { // check if grid contains tile
		    		boardString.append(tiles.tileMap.get(i, j).toString());
		    	} else {
			        boardString.append("  ");	    		
		    	}
		    }
		    boardString.append("|\n"); // end of row
		    for (int j = 0; j < cols; j++){
		    	boardString.append("---");
		    }
		    boardString.append("\n"); // end of row
		}
		return boardString.toString();
	}
	
	public static void main(String[] args) {
		Board b = new Board();
		Tile tile = new Tile(Tile.Shape.X, Tile.Colour.R);
		b.tiles.tileMap.put(1, 3, tile);
		System.out.println(b.toString());
	}
}