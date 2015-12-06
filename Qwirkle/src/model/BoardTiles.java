package model;
// class to store all the Tiles that have been placed on the board

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
	public Tile[] getAdjecentTiles(int row, int col) {
		Tile[] adjecentTiles = new Tile[]{tileMap.get(row, col + 1), // Right
				tileMap.get(row + 1, col), // Top
				tileMap.get(row, col - 1), // Left
				tileMap.get(row - 1, col)}; // Bottom 
		return adjecentTiles;
	}
}
