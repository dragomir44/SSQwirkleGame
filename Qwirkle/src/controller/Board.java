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
//		bag = new Bag();
	}

	// -- Queries ----------------------------------------------------

	/** Every tile needs to be placed next to adjecent tile (except first tile).
	 * This method checks if tile is adjecent to atleast 1 tile,
	 * n.b. (have to make sure that when placed next to end of board, board increses size)
	 * @param row
	 * @param col
	 * @return true if field has atleast 1 adjecent tile
	 */
	public boolean isAdjecent(int row, int col) {
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
		//Bag is empty
		return false;
	}

	/** Make sure the move is a valid one.
	 *  check if it has atleast 1 adjecent tile
	 *	check if adjecent tile is same color or shape
	 *	if first move, make sure it is placed in the middle (cast error)
	 *  check if it is player's turn
	 * @param row the row the player wants to place the tile
	 * @param col the column the player wants to place the tile
	 * @param tile the tile object the player wants to place
	 * @param player the player that want's to place the tile
	 * @return true if the move is a valid one
	 */
	public boolean isValidMove(int row, int col, Tile t, Player p) {
		return adjecent && (colour || shape) && turn;
	}
	/** Grow the playing field in a certain direction.
	 * @param direction 0(right), 1(top), 2(left), 3(bottom)
	 */
	public void growBoard(int direction) {
		// recreate boardString
		// if top or left, increment BoardTiles Keys
	}

	/** For placing a tile on the board.
	 * @param row the row the player wants to place the tile
	 * @param col the column the player wants to place the tile
	 * @param t the tile object that the player wants to place
	 * @param player the player that wants to place the tile
	 * @return true if the tile has been placed on the field
	 */
	public boolean setField(int row, int col, Tile t, Player p) {
		boolean result = false;
		if (isValidMove(row, col, t, p)) {
			// TODO remove tile from player hand
			// TODO check if board has to grow
			tiles.tileMap.put(row, col, t); // place tile on the field
			result = true;
		}
		return result;
	}
	
	/** Prints the board and places the tiles contained in tileMap on the board.
	 * @return returns a string containing the board 
	 */
	public String toString() {
		// use StringBuilder for better memory performance
		StringBuilder boardString = new StringBuilder("  ");
		String rowline = "|\n   " + new String(new char[cols]).replace("\0", "---") + "\n";
		
		for (int k = 0; k < cols; k++) {
			boardString.append("|" + String.format("%02d", k)); // add column numbers
		}
		boardString.append(rowline);
		for (int i = 0; i < rows; i++) { // loop trough rows
			boardString.append(String.format("%02d", i)); // add row numbers
		    for (int j = 0; j < cols; j++) { // loop trough cols
		    	boardString.append("|");
		    	if (tiles.tileMap.containsKeys(i, j)) { // check if grid contains tile
		    		boardString.append(tiles.tileMap.get(i, j).toString());
		    	} else {
			        boardString.append("  ");	    		
		    	}
		    }
		    boardString.append(rowline); // end of row
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