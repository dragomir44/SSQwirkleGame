package controller;

import model.*;
import java.util.*;

public class Board {
	public int rows = 15;
	public int cols = 15;
	
	// instance with a map of all the tiles placed on the board
	private BoardTiles tiles = new BoardTiles();

	public Board() {

	}
	
	public boolean gameOver() {
		//Bag is empty
		return false;
	}
	
	/** Get the horizontal and vertical line that the placed.
	 *  tile is goint to be part of
	 * @param move
	 * @return returns two ArrayLists of Tiles
	 */
	public ArrayList<ArrayList<Tile>> getLines(Move move) {
		ArrayList<ArrayList<Tile>> result = new ArrayList<ArrayList<Tile>>(2);
		ArrayList<Tile> hline = new ArrayList<Tile>();
		ArrayList<Tile> vline = new ArrayList<Tile>();
		ArrayList<ArrayList<Tile>> tileLines = tiles.getAdjecentLines(move.row, move.col);
		// horizontal line
		hline.addAll(tileLines.get(0));
		hline.add(move.tile);
		hline.addAll(tileLines.get(2));
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
//		System.out.println("lines from " + move.row + move.col + " are " + result);
		return result;
	}

	/** Make sure the move is a valid one.
	 * 0. make sure it is an empty tile
	 * 1. check if it has adjecent tiles
	 * 2. make sure it fits the common type of the all the rows
	 * 3. make sure it is the only shape/color in the color/shape row
	 * 4. return true/false
	 * @param move 
	 * @return true if the move is a valid move
	 */
	public boolean isValidMove(Move move) {
		int row = move.row;
		int col = move.col;
		Tile t = move.tile;
		boolean result;
		boolean hasTile = tiles.containsKeys(row, col);
		boolean hasAdjecent = false;
		boolean noMoreThenSix = true;
		boolean exclusiveColour = true;
		boolean exclusiveShape = true;
		
		ArrayList<Tile.Shape> lineShapes;
		ArrayList<Tile.Colour> lineColours;
		Set<Tile.Colour> uniqueColours;
		Set<Tile.Shape> uniqueShapes;
		ArrayList<ArrayList<Tile>> tileLines = getLines(move);
		if (tiles.isEmpty()) {
			result = true;  // is first move
		} else {
			for (ArrayList<Tile> line : tileLines) { // loop trough tile lines
//				System.out.println("removing " + t + " from " + line);
				line.remove(t); // remove the tile that is being placed from possible line
//				System.out.println("the line is: " + line + " with size: " + line.size());
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
//					System.out.println("uniqueColours" + uniqueColours);
//					System.out.println("uniqueShapes: " + uniqueShapes);
					exclusiveColour = 
							!lineColours.contains(t.getColour()) // make sure the color is unique
							&& uniqueShapes.size() == 1 // make sure the shapes are the same
							&& uniqueShapes.contains(t.getShape()); // make sure it is the shape
					if (!(exclusiveColour ^ exclusiveShape)) {
						break; // if a line does not abide, no point to go on
					}
				}
			}
			result = hasAdjecent && !hasTile && (exclusiveColour ^ exclusiveShape) && noMoreThenSix;
//			System.out.println("hasAdjecent?: " + hasAdjecent);
//			System.out.println("hasTile?: " + !hasTile);
//			System.out.println("Ex Colour?: " + exclusiveColour);
//			System.out.println("Ex Shape?: " + exclusiveShape);
//			System.out.println("exclusive Colour ^ Shape?: " + (exclusiveColour ^ exclusiveShape));
//			System.out.println("No more then six?: " + noMoreThenSix);
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
			tileLines = getLines(move);
			for (ArrayList<Tile> line : tileLines) { // loop trough adjecent lines of that tile
				// check if line was already rewarded points
				if (!prevLines.contains(line)) { 
					points += line.size();
					System.out.println("scored " + line.size() + " points for line " + line);
					prevLines.add(line);
//					System.out.println("lines awarded points: " + prevLines);
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
		BoardTiles protoTiles = tiles;
		Tile.Colour sharedColour = moves.get(0).tile.getColour();
		Tile.Shape sharedShape = moves.get(0).tile.getShape();
		ArrayList<Tile> placedTiles = new  ArrayList<Tile>(); 
		ArrayList<ArrayList<Tile>> tileLines;
		boolean result = true;
		boolean sharesColour = true;
		boolean sharesShape = true;
		boolean sameLine = true;
		int row;
		int col;

		for (Move move : moves) {
			row = move.row;
			col = move.col;
			Tile tile = move.tile;
			System.out.println("Placing " + tile + " on " + row + " " + col);
			// make sure all the tiles placed share an attribute
			// TODO probably moot to check for same color/shape since 
			// matching tiles in same row allready need to have this feature
			if (!sharedColour.equals(tile.getColour())) {
				sharesColour = false;
			}
			if (!sharedShape.equals(tile.getShape())) {
				sharesShape = false;
			}
			// make sure all the tiles paced are in the same line
			tileLines = getLines(move);
			System.out.println("adjecent lines: " + tileLines);
			sameLine = tileLines.get(0).containsAll(placedTiles) 
					|| tileLines.get(1).containsAll(placedTiles);
			
			// make sure the tile can be placed on the board
			if (isValidMove(move) && (sharesColour || sharesShape) && sameLine) {
				protoTiles.put(row, col, tile); // place tile on the field
				placedTiles.add(tile);
			} else {
				result = false;
				break;
			}
		}
		// if not all moves were valid, remove them from the board.
		if (!result) {
			for (Move move : moves) {
				tiles.remove(move.row, move.col);
			}
		}
		System.out.println("shares Color? " + sharesColour);
		System.out.println("shares Shape? " + sharesShape);
		System.out.println("shares Line? " + sameLine);
		System.out.println("total score: " + getPoints(moves) + " points!");
		System.out.println("-----------------New Move--------------");
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
}