package controller;

import model.*;
import java.util.*;

public class Board {
	boolean firstMove = true;
	public String errorBuffer = "";
	// instance with a map of all the tiles placed on the board
	private BoardTiles tiles = new BoardTiles();
	
	public BoardTiles getTiles() {
		return this.tiles;
	}

	public ArrayList<ArrayList<Tile>> getAllLines(ArrayList<Move> moves) {
        ArrayList<ArrayList<Tile>> tileLines;
        ArrayList<ArrayList<Tile>> prevLines = new ArrayList<ArrayList<Tile>>();
        // create hard copy of placed tiles to test on
        BoardTiles testTiles = new BoardTiles(tiles);
        for (Move move : moves) {
            testTiles.put(move.row, move.col, move.tile); // place tile on the field
        }
        for (Move move : moves) { // loop trough placed tiles
            tileLines = getLines(move, testTiles);
            for (ArrayList<Tile> line : tileLines) { // loop trough adjecent lines of that tile
                if (!prevLines.contains(line) && !line.isEmpty()) {
                    prevLines.add(line);
                }
            }
        }
        return prevLines;
	}


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

	public boolean isValidMove(ArrayList<Move> moves) {
		BoardTiles protoTiles = new BoardTiles(tiles); // create copy of field to test moves
		ArrayList<Tile> placedTiles = new  ArrayList<Tile>(); 
		boolean result = false;
		boolean sameLine = true;
		boolean hasTile = false;
		boolean hasAdjecent = false;
		boolean exclusiveColour = true;
		boolean exclusiveShape = true;
		errorBuffer = ""; // clear error buffer
		
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
				errorBuffer += "There is already a tile on row " 
							+ row + " and column " + col + "\n";
				break moveLoop;
			}
			
			ArrayList<ArrayList<Tile>> tileLines = getLines(move, protoTiles);
			if (protoTiles.isEmpty()) {
				firstMove = true;  // is first move
			} else {
				if (!tileLines.get(0).containsAll(placedTiles) 
						  || !tileLines.get(1).containsAll(placedTiles)) {
					errorBuffer += "Tiles are not being placed on the same line. \n";
					sameLine = false;
				}
				for (ArrayList<Tile> line : tileLines) { // loop trough tile lines
					ArrayList<Tile> backupLine = new ArrayList<Tile>(line);
					boolean uniqueAll = true;
					if (!line.isEmpty()) {
						hasAdjecent = true; // check if there is atleast 1 adjecent tile

						ArrayList<Tile.Shape> lineShapes = new ArrayList<Tile.Shape>();
						ArrayList<Tile.Colour> lineColours = new ArrayList<Tile.Colour>(); 
						line.remove(tile); // remove placed tile
						for (Tile lineTile : line) {
							lineShapes.add(lineTile.getShape()); // add all shapes in line
							lineColours.add(lineTile.getColour()); // add all colors in line
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
								lineColours.contains(tile.getColour())
								&& uniqueShapes.size() == 1 // make sure the shapes are the same
										// make sure it is the shape
								&& uniqueShapes.contains(tile.getShape());
						if (exclusiveShape) {
							for (Tile testTile : backupLine) {
								if (lineShapes.contains(testTile.getShape())) {
									exclusiveShape = false;
								}
							}
						}
						if (exclusiveColour) {
							for (Tile testTile : backupLine) {
								if (lineColours.contains(testTile.getColour())) {
									exclusiveColour = false;
								}
							}
						}
						
						if (!(exclusiveColour ^ exclusiveShape)) {
							errorBuffer += "Incorrect color/shape match \n";
							break moveLoop;
						}
					}
				}
				if (!hasAdjecent) {
					errorBuffer += "There are no adjecent tiles to form a line. \n";
				}
			}
			result = (hasAdjecent &&
					!hasTile &&
					(exclusiveColour ^ exclusiveShape)
					&& sameLine)
					|| firstMove;
			if (!result) {
				System.out.println("Move " + move.toString() + " was invalid");
				break moveLoop;
			} else {
				protoTiles.put(row, col, tile);
			}
		}
 	    return result;
	}

	public String getErrors() {
		return errorBuffer;
	}

	public int getPoints(ArrayList<Move> moves) {
		int points = 0;
		ArrayList<ArrayList<Tile>> tileLines;
		ArrayList<ArrayList<Tile>> prevLines = new ArrayList<ArrayList<Tile>>();
		// create hard copy of placed tiles to test on
		BoardTiles testTiles = new BoardTiles(tiles);
		boolean metaMove = false; // boolean for test cases
		// if move has not yet been made, make moves (be sure moves are valid, before testing!)
		if (!moves.isEmpty()) {
			Move headMove = moves.get(0);
			if (!testTiles.containsKeys(headMove.row, headMove.col)) {
				metaMove = true;
				for (Move move : moves) {
					testTiles.put(move.row, move.col, move.tile); // place tile on the field
				}
			}
		}
		// check if this was the first move of the game and only 1 tile was placed
		// TODO double check this
		if (tiles.size() == 1 && moves.size() == 1) {
			points = 1;
		}
		for (Move move : moves) { // loop trough placed tiles
			tileLines = getLines(move, testTiles);
			for (ArrayList<Tile> line : tileLines) { // loop trough adjecent lines of that tile
				// check if line was already rewarded points
				if (!prevLines.contains(line) && !line.isEmpty()) {
					points += line.size();
//					System.out.println("Scored " + line.size() + " for line " + line +
//									" with move " + move.toString() + " total points: " + points);
					if (line.size() == 6) {
						points += 6;
						if (!metaMove) { // prevents spam during test points
							System.out.println("Scored a Qwirkle!");
						}
					}
					prevLines.add(line);
				}
			}
		}

		return points;
	}


	public boolean setField(ArrayList<Move> moves, BoardTiles tileMapToPlace) {
		BoardTiles tileMap = tileMapToPlace;
		boolean result = true;
		if (isValidMove(moves)) {
			for (Move move : moves) {
				tileMap.put(move.row, move.col, move.tile); // place tile on the field
			}
		} else {
			result = false;
		}
		return result;
	}

	// default setField behaviour: place on tiles opbject
	public boolean setField(ArrayList<Move> moves) {
		return setField(moves, tiles);
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
		BoardTiles protoTiles = new BoardTiles(boardtiles); // create copy of field
		for (Move move: moves) {
			protoTiles.put(move.row, move.col, move.tile);
		}
		// use StringBuilder for better memory performance
		StringBuilder boardString = new StringBuilder("   ");
		String rowline = "|\n";
		// get the size of the board
		int borderSize = 1;
		int[] bounds = protoTiles.getBoardBounds();
		int minRow = bounds[0] - borderSize;
		int maxRow = bounds[1] + borderSize + 1;
		int minCol = bounds[2] - borderSize;
		int maxCol = bounds[3] + borderSize + 1;


		for (int k = minCol; k < maxCol; k++) {
			boardString.append("|" + String.format("%03d", k)); // add column numbers
		}
		boardString.append(rowline);
		for (int i = minRow; i < maxRow; i++) { // loop trough rows
			boardString.append(String.format("%03d", i)); // add row numbers
		    for (int j = minCol; j < maxCol; j++) { // loop trough cols
		    	boardString.append("|");
		    	if (protoTiles.containsKeys(i, j)) { // check if grid contains tile
		    		boardString.append(protoTiles.get(i, j).toString()+ " ");
		    	} else {
			        boardString.append("  ");
		    	}
		    }
		    boardString.append(rowline); // end of row
		}
		return boardString.toString();
	}

	public boolean hasPossibleMoves(ArrayList<Tile> useTiles) {
		boolean result = false;
		fieldLoop:
		for (int[] emptyPos : tiles.getEmptyFields()) {
			for (Tile tile : useTiles) {
				ArrayList<Move> newMove = new ArrayList<Move>();
				newMove.add(new Move(emptyPos[0], emptyPos[1], tile));
				if (this.isValidMove(newMove)) {
					result = true;
					break fieldLoop;
				}
			}
		}
		return result;
	}

	public TreeMap<ArrayList<Move>, Integer> getPossibleMoves(ArrayList<Tile> useTiles) {
		HashMap<ArrayList<Move>, Integer> result = new HashMap<ArrayList<Move>, Integer>();
		TreeMap<ArrayList<Move>, Integer> sortedResult = new TreeMap<ArrayList<Move>, Integer>();
		// if this is the first move, the only possible move is the largest row possible
		// loop trough all the empty fields where a tile can be placed
		for (int[] emptyPos : tiles.getEmptyFields()) { 
//			System.out.println("Empty spot is " + emptyPos[0] + ", " + emptyPos[1]);
			for (Tile tile : useTiles) { // check for every tile if it can be placed

				ArrayList<Move> newMove = new ArrayList<Move>();
				newMove.add(new Move(emptyPos[0], emptyPos[1], tile));
				if (this.isValidMove(newMove)) {  // if it can be placed
//					System.out.println("test move is: " + newMove.toString());
					result.put(newMove, getPoints(newMove)); // add as possible move
					ArrayList<Tile> testTiles = new ArrayList<Tile>(useTiles);
					testTiles.remove(tile); // create a copy of the hand, and remove placed tile
					for (int dir = 0; dir < 4; dir++) { // test row creation in every direction
						HashMap<ArrayList<Move>, Integer> moveMap = 
										recursiveMoveCalc(testTiles, newMove, dir);
						if (!moveMap.isEmpty()) {
							result.putAll(moveMap);
						}
					}
				}
			}
		}
		MoveComparator moveComp = new MoveComparator();
		sortedResult = moveComp.sortByPoints(result);
		return sortedResult;
	}
	
	public HashMap<ArrayList<Move>, Integer> recursiveMoveCalc(
					ArrayList<Tile> useTiles, ArrayList<Move> prevMoves, int direction) {
		HashMap<ArrayList<Move>, Integer> result = new HashMap<ArrayList<Move>, Integer>();
		Move headMove = prevMoves.get(prevMoves.size() - 1); // get the last move
		int row = headMove.row;
		int col = headMove.col;
		// could already filter out tiles, but this could be done just as well with isValidMOve()
		// do prevent unnecessary invalid moves by walking in 1 direction
		switch (direction) {
			case 0: // walk right
				col++;
				break;
			case 1: // walk down
				row--;
				break;
			case 2: // walk left
				col--;
				break;
			case 3: // walk up
				row++;
				break;
		}
		for (Tile tile : useTiles) { // check all available tiles
			Move newMove = new Move(row, col, tile);
			ArrayList<Move> testMoves = new ArrayList<Move>(prevMoves);
			testMoves.add(newMove);
			if (this.isValidMove(testMoves)) {
				result.put(testMoves, this.getPoints(testMoves));
				// create deep copy of tiles to pass on.
				ArrayList<Tile> testTiles = new ArrayList<Tile>(useTiles);
				testTiles.remove(tile);
				HashMap<ArrayList<Move>, Integer> moveMap = 
								recursiveMoveCalc(useTiles, testMoves, direction);
				if (!moveMap.isEmpty()) {
					result.putAll(moveMap);
				}
			}
		}
		return result;
	}
}