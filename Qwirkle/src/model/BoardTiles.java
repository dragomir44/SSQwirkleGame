package model;
// class to store all the Tiles that have been placed on the board

import java.util.*;

public class BoardTiles {
	
	private final Map<Integer, Map<Integer, Tile>> tileMap;

	public BoardTiles() {
		tileMap = new HashMap<Integer, Map<Integer, Tile>>();
	}
	
	/**
	 * Associates the specified value with the specified keys in this map (optional operation). 
	 * If the map previously contained a mapping for the key, 
	 * the old value is replaced by the specified value.
	 * 
	 * @param key1
	 *            the first key
	 * @param key2
	 *            the second key
	 * @param value
	 *            the value to be set
	 * @return the value previously associated with (key1,key2), or <code>null</code> if none
	 * @see Map#put(Object, Object)
	 */
	public Tile put(Integer key1, Integer key2, Tile value) {
	    Map<Integer, Tile> map;
	    if (tileMap.containsKey(key1)) {
	        map = tileMap.get(key1);
	    } else {
	        map = new HashMap<Integer, Tile>();
	        tileMap.put(key1, map);
	    }
	
	    return map.put(key2, value);
	}
	
	public Tile get(Integer key1, Integer key2) {
	    if (tileMap.containsKey(key1)) {
	        return tileMap.get(key1).get(key2);
	    } else {
	        return null;
	    }
	}
	
	public boolean containsKeys(Integer key1, Integer key2) {
	    return tileMap.containsKey(key1) && tileMap.get(key1).containsKey(key2);
	}
	
	public void clear() {
	    tileMap.clear();
	}
	
	public boolean isEmpty() {
		return tileMap.isEmpty();
	}
	
	/** Returns Tile array of columns adjecent to the row and col.
	 * @param row
	 * @param col
	 * @return array of Tiles [Right, Top, Left, Bottom]
	 */
	public ArrayList<ArrayList<Tile>>  getAdjecentLines(int row, int col) {
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
				Tile nextTile = this.get(rowCount, colCount);
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
		b.put(1, 3, tile);
		b.put(1, 4, tile1);
		b.put(1, 5, tile2);
		System.out.println(b.getAdjecentLines(1, 6));
	}
}
