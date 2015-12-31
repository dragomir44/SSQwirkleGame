package controller;

import model.*;
import java.util.*;

public class Board {
	public int rows = 15;
	public int cols = 15;
	public int middleOfBoard = rows / 2;
	public String middleOfBoardS = Integer.toString(middleOfBoard);
	boolean firstMove = true;

	
	
	// instance with a map of all the tiles placed on the board
	private BoardTiles tiles = new BoardTiles();
	
	/** Get the horizontal and vertical line that the placed.
	 *  tile is going to be part of
	 * @param move
	 * @return returns two ArrayLists of Tiles
	 */
	public ArrayList<ArrayList<Tile>> getLines(Move move, BoardTiles layout) {
		ArrayList<ArrayList<Tile>> result = new ArrayList<ArrayList<Tile>>(2);
		ArrayList<Tile> hline = new ArrayList<Tile>();
		ArrayList<Tile> vline = new ArrayList<Tile>();
		ArrayList<ArrayList<Tile>> tileLines = layout.getAdjecentLines(move.row, move.col);
		// horizontal line
		hline.addAll(tileLines.get(2));
		hline.add(move.tile);
		hline.addAll(tileLines.get(0));
		if (hline.size() > 1) {
			result.add(hline);
		} else {
			// else fill with empty line
			result.add(new ArrayList<Tile>());
		}
		// vertical line
		vline.addAll(tileLines.get(1));
		vline.add(move.tile);
		vline.addAll(tileLines.get(3));
		if (vline.size() > 1) {
			result.add(vline);
		} else {
			// else fill with empty line
			result.add(new ArrayList<Tile>());
		}
		return result;
	}

	/** Make sure the move is a valid one.
	 * @param moves
	 * @return true if the move is a valid move
	 */
	public boolean isValidMove(ArrayList<Move> moves) {
		BoardTiles protoTiles = new BoardTiles(tiles); // create copy of field to test moves
		ArrayList<Tile> placedTiles = new  ArrayList<Tile>(); 
		boolean result = false;
		boolean sameLine = true;
		boolean hasTile = false;
		boolean hasAdjecent = false;
		boolean noMoreThenSix = true;
		boolean exclusiveColour = true;
		boolean exclusiveShape = true;
		boolean firstMove = false;
		
		moveLoop:
		for (Move move : moves) {
			result = false;
			hasAdjecent = false;
			firstMove = false;
			int row = move.row;
			int col = move.col;
			Tile tile = move.tile;
			
			if (protoTiles.containsKeys(row, col)) {
				hasTile = true;
				System.out.println("Invalid move." + 
						  "There is already a tile on row " + row + " and column " + col + ".");
				break moveLoop;
			}
			
			ArrayList<ArrayList<Tile>> tileLines = getLines(move, protoTiles);
			if (protoTiles.isEmpty()) {
				firstMove = true;  // is first move
			} else {
				if (!tileLines.get(0).containsAll(placedTiles) 
						  || !tileLines.get(1).containsAll(placedTiles)) {
					System.out.println("Invalid move. "
							  + "Tiles are not being placed on the same line.");
					sameLine = false;
				}
				for (ArrayList<Tile> line : tileLines) { // loop trough tile lines
					if (!line.isEmpty()) {
						hasAdjecent = true; // check if there is atleast 1 adjecent tile
						if (line.size() > 6) {
							noMoreThenSix = false;
							System.out.println("Invalid move. " 
							  		  + "Line is longer than six tiles.");
							break moveLoop;
						}
						ArrayList<Tile.Shape> lineShapes = new ArrayList<Tile.Shape>(); 
						ArrayList<Tile.Colour> lineColours = new ArrayList<Tile.Colour>(); 
						line.remove(tile); // remove placed tile 
						for (Tile lineTile : line) {
							lineShapes.add(lineTile.getShape());
							lineColours.add(lineTile.getColour());
						}
						Set<Tile.Colour> uniqueColours = new HashSet<Tile.Colour>(lineColours);
						Set<Tile.Shape> uniqueShapes = new HashSet<Tile.Shape>(lineShapes);
						exclusiveShape = 
								!lineShapes.contains(tile.getShape()) // make sure shape is unique
								&& uniqueColours.size() == 1 // make sure colors are the same
										// make sure it is the color
								&& uniqueColours.contains(tile.getColour()); 
						exclusiveColour = 
								// make sure the color is unique
								!lineColours.contains(tile.getColour()) 
								&& uniqueShapes.size() == 1 // make sure the shapes are the same
										// make sure it is the shape
								&& uniqueShapes.contains(tile.getShape()); 
						if (!(exclusiveColour ^ exclusiveShape)) {
							System.out.println("Invalid move. " 
										 + "Incorrect color/shape match");
						}
					}
				}
				if (!hasAdjecent) {
					System.out.println("Invalid move. " 
								  + "There are no adjecent tiles to form a line.");
				}
			}
			result = hasAdjecent && 
					!hasTile && 
					(exclusiveColour ^ exclusiveShape) 
					&& noMoreThenSix 
					&& sameLine 
					|| firstMove;
			if (!result) {
				break moveLoop;
			} else {
				protoTiles.put(row, col, tile);
			}
		}
 	    return result;
	}
	
	public int getPoints(ArrayList<Move> moves) {
		int points = 0;
		ArrayList<ArrayList<Tile>> tileLines;
		ArrayList<ArrayList<Tile>> prevLines = new ArrayList<ArrayList<Tile>>();
		// check if this was the first move of the game and only 1 tile was placed
		if (tiles.size() == 1) {
			points = 1; 
		}
		for (Move move : moves) { // loop trough placed tiles
			tileLines = getLines(move, tiles);
			for (ArrayList<Tile> line : tileLines) { // loop trough adjecent lines of that tile
				// check if line was already rewarded points
				if (!prevLines.contains(line) && !line.isEmpty()) { 
					points += line.size();
					if (line.size() == 6) {
						points += 6;
						System.out.println("Scored a Qwirkle!");
					}
					prevLines.add(line);
				}
			}
		}
		return points;
	}
	

	/** For placing a tile on the board.
	 * @param moves an arrayList of moves
	 * @return returns the points 
	 */
	public boolean setField(ArrayList<Move> moves) {
		boolean result = true;
		if (isValidMove(moves)) {
			for (Move move : moves) {
				tiles.put(move.row, move.col, move.tile); // place tile on the field
			}
		} else {
			result = false;
		}
		return result;
	}
	
	public boolean isEmpty() {
		return tiles.isEmpty();
	}
	
	/** Prints the board and places the tiles contained in tileMap on the board.
	 * @return returns a string containing the board 
	 */
	public String toString() {
		return toString(tiles);
	}
	
	public String toString(BoardTiles boardtiles) {
		return toString(boardtiles, new ArrayList<Move>());
	}
	
	public String toString(ArrayList<Move> moves) {
		return toString(tiles, moves);
	}
	
	public String toString(BoardTiles boardtiles, ArrayList<Move> moves) {
		BoardTiles protoTiles = new BoardTiles(boardtiles); // create copy of field to test moves
		for (Move move: moves) {
			protoTiles.put(move.row, move.col, move.tile);
		}
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
		    	if (protoTiles.containsKeys(i, j)) { // check if grid contains tile
		    		boardString.append(protoTiles.get(i, j).toString());
		    	} else {
			        boardString.append("  ");	    		
		    	}
		    }
		    boardString.append(rowline); // end of row
		}
		return boardString.toString();
	}
}