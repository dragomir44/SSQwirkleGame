package model;

import java.util.ArrayList;

public class Hand {
	
	private int tilesPerPlayer;
	private Bag bag;
	public ArrayList<Tile> hand = new ArrayList<Tile>();
	
	public Hand(Bag bag, int tilesPerPlayer) {
		this.bag = bag;
		this.tilesPerPlayer = tilesPerPlayer;
		drawTiles();
	}
     
	public ArrayList<Tile> getHand() {
		return new ArrayList<Tile>(this.hand);
	}
	
    public String toString() { 
    	return hand.toString(); 
    }
    
	public ArrayList<Tile> removeTiles(ArrayList<Tile> tiles) {
		hand.removeAll(tiles);
		return drawTiles();
	}
	
	public ArrayList<Tile> removeTile(Tile tile) {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.add(tile);
		return removeTiles(tiles);
	}
	
	public ArrayList<Tile> replaceTiles(ArrayList<Integer> tilenrs) {
		ArrayList<Tile> handCopy = getHand();
		ArrayList<Tile> tiles = null;
		for (int i : tilenrs) {
			tiles  = new ArrayList<Tile>();
			if (i <= 0 || i > hand.size()) {
				tiles = null;
				break;
			} else {
				if (handCopy.contains(hand.get(i))) {
					tiles.add(hand.get(i));
					// make sure tile is never chosen twice
					handCopy.remove(hand.get(i)); 
				} else {
					tiles = null;
					break;
				}
			}
		}
		if (tiles != null) {
			bag.addTiles(tiles);
			tiles = removeTiles(tiles);
		}
		return tiles;
	}
	
	private ArrayList<Tile> drawTiles() {
		ArrayList<Tile> tilesDrawn = new ArrayList<Tile>();
		int tilesToDraw = tilesPerPlayer - hand.size();
        for (int i = 0; i < tilesToDraw; i++) {
        	if (!bag.getBag().isEmpty()) {
        		Tile drawnTile = bag.getBag().remove(0);
        		tilesDrawn.add(drawnTile);
        		hand.add(drawnTile);	
        	}
        }
        return tilesDrawn;
	}
}