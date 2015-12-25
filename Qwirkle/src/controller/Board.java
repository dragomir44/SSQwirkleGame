package controller;

import model.*;


import java.util.*;

public class Board {


	public int rows = 15;
	public int cols = 15;
	
	// instance with a map of all the tiles placed on the board
	public BoardTiles tiles = new BoardTiles();

	public Board() {

	}
	
	public boolean gameOver() {
		//Bag is empty
		return false;
	}
	
	public ArrayList<ArrayList<Tile>> getLines(Move move) {
		ArrayList<ArrayList<Tile>> result = new ArrayList<ArrayList<Tile>>(2);
		ArrayList<Tile> hline = new ArrayList<Tile>();
		ArrayList<Tile> vline = new ArrayList<Tile>();
		ArrayList<ArrayList<Tile>> tileLines = tiles.getAdjecentLines(move.row, move.col);
		// horizontal line
//		System.out.println("adjecent lines are: " + tileLines);
		hline.addAll(tileLines.get(0));
		hline.add(move.tile);
		hline.addAll(tileLines.get(2));
//		System.out.println("added " + tileLines.get(2) + " to " + hline);
		result.add(hline);
//		System.out.println("halfLine is " + result);
		// vertical line
		vline.addAll(tileLines.get(1));
		vline.add(move.tile);
		vline.addAll(tileLines.get(3));
		result.add(vline);
		
		System.out.println("lines from " + move.row + move.col + " are " + result);
		return result;
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
		Tile tile;
		ArrayList<ArrayList<Tile>> tileLines;
		ArrayList<Tile> prevTiles = new ArrayList<Tile>();
		for (Move move : moves) { // loop trough placed tiles
			tile = move.tile;
			tileLines = getLines(move);
			for (ArrayList<Tile> line : tileLines) { // loop trough adjecent lines of that tile
				// check if line was already rewarded points
				System.out.println("disjoint result for: " + tile + " in line " + line + "v" + prevTiles +" is " + Collections.disjoint(line, prevTiles));
				// disjoint if true if line and prevTiles have no tiles in common
				if (Collections.disjoint(line, prevTiles) && line.size() > 1) { 
					points += line.size();
					System.out.println("scored " + points + " for line " + line);
					prevTiles.add(tile);
				}
			}
		}
		return points;
	}
	
	public boolean isFirstMove() {
		return tiles.isEmpty();
	}
	
	/** For placing a tile on the board.
	 * @param moves an arrayList of moves
	 * @return returns the points 
	 */
	public boolean setField(ArrayList<Move> moves) {
		boolean result = true;
		for (Move move : moves) {
			if (isValidMove(move)) {
				tiles.put(move.row, move.col, move.tile); // place tile on the field	
			} else {
				result = false;
				break;
			}
		}
		System.out.println("total score: " + getPoints(moves) + " points!");
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
	
//	public static void main(String[] args) {
//		Tile tile = new Tile(Tile.Shape.X, Tile.Colour.R);
//		Tile tile1 = new Tile(Tile.Shape.X, Tile.Colour.B);
//		Tile tile2 = new Tile(Tile.Shape.X, Tile.Colour.G);
//		Tile tile3 = new Tile(Tile.Shape.X, Tile.Colour.P);
//		Tile tile4 = new Tile(Tile.Shape.O, Tile.Colour.O);
//		Tile tile5 = new Tile(Tile.Shape.Ø, Tile.Colour.R);
//		
//		Board board = new Board();
//		Move move1 = new Move(4, 5, tile);
//		ArrayList<Move> moves = new ArrayList<Move>();
//		
//		moves.add(move1);
////		System.out.println(board.setField(moves));
//		board.tiles.put(4, 5, tile);
//		System.out.println(board.toString());
//		Move move2 = new Move(4, 6, tile1);
//		Move move3 = new Move(4, 7, tile3);
//		
//		moves.clear();
//		moves.add(move2);
//		moves.add(move3);
////		assertTrue(board.setField(moves));
//	}
}