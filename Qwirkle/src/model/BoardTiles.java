package model;
// class to store all the Tiles that have been placed on the board

public class BoardTiles {
	// use tileMap to store tiles:
	// placing;  BoardTiles.tileMap.put(row, col, tile);
	// getting:  BoardTiles.tileMap.get(row, col) (returns tile) 

	public BiHashMap<Integer, Integer, Tile> tileMap = new BiHashMap<Integer, Integer, Tile>();

	public BoardTiles() {
//		tileMap(1, 2, new Tile(Tile.Shape.X, Tile.Colour.R));
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
	
	static public void main(String[] args) {
		BoardTiles tiles = new BoardTiles(); 
		Tile tile = new Tile(Tile.Shape.X, Tile.Colour.R);
		tiles.tileMap.put(1, 3, tile);
		
		Tile value = tiles.tileMap.get(1, 2);
		if (value == null) {
			System.out.println("isNull");
		} else {
			System.out.println("strange");
		}
		
	}
}
