package controller;

import model.Tile;

import java.util.Arrays;

import model.BoardTiles;

public class Board {

	// -- Constants --------------------------------------------------

	// -- Instance variables -----------------------------------------
	public int rows = 20;
	public int cols = 20;

	
	// instance with a map of all the tiles placed on the board
	BoardTiles tiles = new BoardTiles();
	// -- Constructors -----------------------------------------------
	public Board() {
//		bag = new Bag();
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
	public boolean isValidMove(int row, int col, Tile t) {
		// TODO add valid move for player turn
		boolean hasTile = tiles.tileMap.containsKeys(row, col);
		boolean hasAdjecent = false;
		boolean hasColour = false;
		boolean hasShape = false;
		boolean isTurn = true;
		int count = 0;
		Tile[] adjecent =  tiles.getAdjecentTiles(row, col);
		Tile.Shape[] shapes = new Tile.Shape[3];
		Tile.Colour[] colours = new Tile.Colour[3];
		for (Tile tile : adjecent) {
			if (tile != null) {
				hasAdjecent = true;
				shapes[count] = tile.getShape();
				colours[count] = tile.getColour();
			}
			count++;
		}
		hasShape = Arrays.asList(shapes).contains(t.getShape());
		hasColour = Arrays.asList(colours).contains(t.getColour());
			
		return hasAdjecent && (hasColour || hasShape) && isTurn && !hasTile;
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
	public boolean setField(int row, int col, Tile t) {
		boolean result = false;
		if (isValidMove(row, col, t)) {
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
		String rowline = "|\n";
		
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
		Tile tile1 = new Tile(Tile.Shape.X, Tile.Colour.B);
		Tile tile2 = new Tile(Tile.Shape.O, Tile.Colour.R);
		b.tiles.tileMap.put(1, 3, tile);
		System.out.println(b.toString());
		System.out.println(b.setField(1, 4, tile1));
		System.out.println(b.toString());
		System.out.println(b.setField(1, 5, tile2));
		System.out.println(b.toString());
		

	}
}