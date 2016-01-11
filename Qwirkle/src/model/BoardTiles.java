package model;
// class to store all the Tiles that have been placed on the board

import java.util.*;

public class BoardTiles {
	
	private Map<Integer, Map<Integer, Tile>> tileMap;

	public BoardTiles() {
		tileMap = new HashMap<Integer, Map<Integer, Tile>>();
	}
	
	public BoardTiles(BoardTiles toCopy) {
		tileMap = new HashMap<Integer, Map<Integer, Tile>>();
		Set<Integer> key1Set = toCopy.getMap().keySet();
		Map<Integer, Tile> map;
		for (int key1 : key1Set) {
			map = new HashMap<Integer, Tile>(toCopy.getMap().get(key1));
			tileMap.put(key1, map);
		}	
	}
	
	public Map<Integer, Map<Integer, Tile>> getMap() {
		return tileMap;
	}

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
	
	public Tile remove(Integer key1, Integer key2) {
		Tile result = null;
		Map<Integer, Tile> map;
	    if (tileMap.containsKey(key1)) {
	        map = tileMap.get(key1);
	        result = map.remove(key2);
	    }
	    return result;
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
	
	public int size() {
		Set<Integer> rows = tileMap.keySet();
		int size = 0;
		for (int key1 : rows) {
			size += tileMap.get(key1).size();
		}
		return size;
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
					case 1: // walk down
						rowCount++;
						break;
					case 2: // walk left
						colCount--;
						break;
					case 3: // walk up
						rowCount--;
						break;
				}
				Tile nextTile = this.get(rowCount, colCount);
				if (nextTile == null) { // if no next tile walk next direction
					walking = false;
					switch (i) { // make sure it returns lines in same order
						case 2: // walk left
							Collections.reverse(tileRow);
							break;
						case 1: // walk down
							Collections.reverse(tileRow);
							break;
					}
				} else { // else store the tile
					tileRow.add(nextTile);
				}
			}
			returnTiles.add(i, tileRow);
			
		}
		return returnTiles;
	}	
}
