package controller;

import model.Tile;

import java.util.ArrayList;

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
	 * 0. make sure it is an empty tile
	 * 1. check if it has adjecent tiles
	 * 2. make sure it fits the common type of the all the rows
	 * 3. make sure it is the only shape/color in the color/shape row
	 * 4. return true/false
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
		boolean noMoreThenSix = true;
		boolean exclusiveColour = true;
		boolean exclusiveShape = true;
		ArrayList<ArrayList<Tile>> tileRows = tiles.getAdjecentRows(row, col);
		for (ArrayList<Tile> tileRow : tileRows) { // loop trough all the tile rows
			if (!tileRow.isEmpty()) { 
				hasAdjecent = true; // check if there is atleast 1 adjecent tile
				if (tileRow.size() >= 6 && noMoreThenSix) {
					noMoreThenSix = false;
				}
				for (Tile tile : tileRow) {
					if (tile.getColour().equals(t.getColour()) && exclusiveColour) { 
						exclusiveColour = false;	// check if exclusive color
					}
					if (tile.getShape().equals(t.getShape()) && exclusiveShape) { 
						exclusiveShape = false; 	// check if exclusive shape
					}
				}
			}
		}
		return hasAdjecent && !hasTile && (exclusiveColour ^ exclusiveShape) && noMoreThenSix;
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
//			getPoints(row, col);
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