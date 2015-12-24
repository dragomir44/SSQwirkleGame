package controller;

import model.Tile;
import java.util.*;


import model.BoardTiles;

public class Board {


	public int rows = 20;
	public int cols = 20;
	
	// instance with a map of all the tiles placed on the board
	BoardTiles tiles = new BoardTiles();

	public Board() {

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
		boolean hasTile = tiles.containsKeys(row, col);
		boolean hasAdjecent = false;
		boolean noMoreThenSix = true;
		boolean exclusiveColour = true;
		boolean exclusiveShape = true;
		
		ArrayList<Tile.Shape> lineShapes;
		ArrayList<Tile.Colour> lineColours;
		Set<Tile.Colour> uniqueColours;
		Set<Tile.Shape> uniqueShapes;
		ArrayList<ArrayList<Tile>> tileLines = tiles.getAdjecentLines(row, col);
		for (ArrayList<Tile> line : tileLines) { // loop trough all the tile lines
			if (!line.isEmpty()) { 
				hasAdjecent = true; // check if there is atleast 1 adjecent tile
				if (line.size() >= 6 && noMoreThenSix) {
					noMoreThenSix = false;
					break;
				}
				lineShapes = new ArrayList<Tile.Shape>(); // all the shapes in the line
				lineColours = new ArrayList<Tile.Colour>(); // all the colors in the line
				for (Tile tile : line) {
					lineShapes.add(tile.getShape());
					lineColours.add(tile.getColour());
				}
				uniqueColours = new HashSet<Tile.Colour>(lineColours);
				uniqueShapes = new HashSet<Tile.Shape>(lineShapes);
				exclusiveShape = 
						!lineShapes.contains(t.getShape()) // make sure shape is unique
						&& uniqueColours.size() == 1 // make sure colors are the same
						&& uniqueColours.contains(t.getColour()); // make sure it is the color
				System.out.println("uniqueColours" + uniqueColours);
				System.out.println("uniqueShapes: " + uniqueShapes);
				exclusiveColour = 
						!lineColours.contains(t.getColour()) // make sure the color is unique
						&& uniqueShapes.size() == 1 // make sure the shapes are the same
						&& uniqueShapes.contains(t.getShape()); // make sure it is the shape
				if (!(exclusiveColour ^ exclusiveShape)) {
					break; // if a line does not abide, no point to go on
				}
			}
		}
//		System.out.println("hasAdjecent?: " + hasAdjecent);
//		System.out.println("hasTile?: " + !hasTile);
//		System.out.println("Ex Colour?: " + exclusiveColour);
//		System.out.println("Ex Shape?: " + exclusiveShape);
//		System.out.println("exclusive Colour ^ Shape?: " + (exclusiveColour ^ exclusiveShape));
//		System.out.println("No more then six?: " + noMoreThenSix);
		return hasAdjecent && !hasTile && (exclusiveColour ^ exclusiveShape) && noMoreThenSix;
	}
	
	public boolean isFirstMove() {
		return false;
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
			tiles.put(row, col, t); // place tile on the field
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
		    	if (tiles.containsKeys(i, j)) { // check if grid contains tile
		    		boardString.append(tiles.get(i, j).toString());
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
		Tile tile2 = new Tile(Tile.Shape.X, Tile.Colour.G);
		b.tiles.put(1, 3, tile);
		System.out.println(b.toString());
//		b.tiles.put(1,4, tile1);
		System.out.println(b.setField(1, 4, tile1));
		System.out.println(b.toString());
		System.out.println(b.setField(1, 5, tile2));
		System.out.println(b.toString());
		
	}
}