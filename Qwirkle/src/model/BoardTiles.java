package model;
// class to store all the Tiles that have been placed on the board

import java.util.*;

public class BoardTiles {
	// use tileMap to store tiles:
	// placing;  BoardTiles.tileMap.put(row, col, tile);
	// getting:  BoardTiles.tileMap.get(row, col) (returns tile) 

	public BiHashMap<Integer, Integer, Tile> tileMap = new BiHashMap<Integer, Integer, Tile>();

	public BoardTiles() {

	}
	
	/** Returns Tile array of columns adjecent to the row and col.
	 * @param row
	 * @param col
	 * @return array of Tiles [Right, Top, Left, Bottom]
	 */
	public ArrayList<ArrayList<Tile>>  getAdjecentRows(int row, int col) {
		ArrayList<ArrayList<Tile>> returnTiles = new ArrayList<ArrayList<Tile>>(4);
		for (int i = 0; i < 4; i++) { // check the 4 directions for adjecent tiles
			ArrayList<Tile> tileRow = new ArrayList<Tile>();
			int rowCount = row;
			int colCount = col;
			boolean walking = true;
			while (walking) { // walk in direction until no next tile
				switch (i) {
					case 0: // walk right
						colCount++;
						break;
					case 1: // walk top
						rowCount++;
						break;
					case 2: // walk left
						colCount--;
						break;
					case 3: // walk bottom
						rowCount--;
						break;
				}
				Tile nextTile = tileMap.get(rowCount, colCount);
				if (nextTile == null) { // if no next tile walk next direction
					walking = false;
				} else { // else store the tile
					tileRow.add(nextTile);
				}
			}
			returnTiles.add(i, tileRow);
			
		}
		return returnTiles;
	}
	
	public static void main(String[] args) {
		BoardTiles b = new BoardTiles();
		Tile tile = new Tile(Tile.Shape.X, Tile.Colour.R);
		Tile tile1 = new Tile(Tile.Shape.X, Tile.Colour.B);
		Tile tile2 = new Tile(Tile.Shape.O, Tile.Colour.R);
		b.tileMap.put(1, 3, tile);
		b.tileMap.put(1, 4, tile1);
		b.tileMap.put(1, 5, tile2);
		System.out.println(b.getAdjecentRows(1, 7));
	}
}
